package xyz.qinfengge.rsscode.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import xyz.qinfengge.rsscode.config.APIConfig;
import xyz.qinfengge.rsscode.dto.*;
import xyz.qinfengge.rsscode.entity.Detail;
import xyz.qinfengge.rsscode.entity.Stars;
import xyz.qinfengge.rsscode.service.DetailService;
import xyz.qinfengge.rsscode.service.StarsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lza
 * @Date 2022/12/16/17/17
 **/
@Component
public class ApiHandelUtil {

    @Resource
    private APIConfig apiConfig;

    @Resource
    private DetailService detailService;

    @Resource
    private StarsService starsService;

    @Resource
    private Detail2Api detail2Api;

    public List<String> getMoviesList(Integer page, Integer magnetType, Integer type) {
        String magnet = magnetType == 1 ? "all" : "exist";
        String url = apiConfig.getJavbusapiUrl() + "movies?" + "page=" + page + "&magnet=" + magnet;
        if (type != null) {
            String typeStr = type == 1 ? "normal" : "uncensored";
            url += "&type=" + typeStr;
        }
        String result = HttpUtil.get(url);
        MoviesDto moviesDto = JSONObject.parseObject(result, MoviesDto.class);
        List<String> list = new ArrayList<>();
        for (MovieDto movie : moviesDto.getMovies()) {
            list.add(movie.getId());
        }
        return list;
    }

    //获取列表的详情信息
    public List<ApiDetailDto> getMoviesDetail(List<String> list, String type) {
        List<ApiDetailDto> apiDetailDtoList = new ArrayList<>();
        for (String l : list) {
            Detail detail = detailService.getBaseMapper().selectById(l);
            if (detail != null) {
                ApiDetailDto dto = detail2Api.d2a(detail);
                apiDetailDtoList.add(dto);
            } else {
                String movieUrl = apiConfig.getJavbusapiUrl() + "movies/" + l;
                String result = HttpUtil.get(movieUrl);
                ApiDetailDto apiDetailDto = JSONObject.parseObject(result, ApiDetailDto.class);
                if (apiDetailDto.getId() != null) {
                    //保存演员信息
                    StarsDto[] stars = apiDetailDto.getStars();
                    saveStars(stars);
                    //设置类型
                    apiDetailDto.setIsCensored(type);
                    Detail d = detail2Api.a2d(apiDetailDto);
                    d.setIsCensored(type);
                    detailService.save(d);
                    apiDetailDtoList.add(apiDetailDto);
                }
            }
        }
        return apiDetailDtoList;
    }

    //获取列表的摘要信息
    public List<SummaryDto> getMoviesSummary(List<String> list, String type) {
        List<ApiDetailDto> moviesDetail = getMoviesDetail(list, type);
        List<SummaryDto> summaryDtoList = new ArrayList<>();
        for (ApiDetailDto dto : moviesDetail) {
            SummaryDto summaryDto = new SummaryDto();
            BeanUtils.copyProperties(dto, summaryDto);
            MagnetDto[] magnets = dto.getMagnets();
            if (magnets.length > 0) {
                for (MagnetDto magnet : magnets) {
                    if (magnet != null) {
                        summaryDto.setHasMagnet(1);
                        if (magnet.isHasSubtitle()) {
                            summaryDto.setHasSub(1);
                            break;
                        } else {
                            summaryDto.setHasSub(0);
                        }
                    }
                }
            } else {
                summaryDto.setHasMagnet(0);
                summaryDto.setHasSub(0);
            }
            summaryDtoList.add(summaryDto);
        }
        return summaryDtoList;
    }

    /**
     * 获取某个电影的详情
     */
    public ApiDetailDto getDetail(String id) {
        Detail detail = detailService.getBaseMapper().selectById(id);
        if (detail != null) {
            return detail2Api.d2a(detail);
        } else {
            String movieUrl = apiConfig.getJavbusapiUrl() + "movies/" + id;
            String result = HttpUtil.get(movieUrl);
            ApiDetailDto apiDetailDto = JSONObject.parseObject(result, ApiDetailDto.class);
            if (apiDetailDto.getId() != null) {
                //保存演员信息
                StarsDto[] stars = apiDetailDto.getStars();
                saveStars(stars);
                Detail d = detail2Api.a2d(apiDetailDto);
                detailService.save(d);
                return apiDetailDto;
            }
        }
        return null;
    }

    public List<SummaryDto> searchMoviesList(Integer page, String keyword) {
        String url = apiConfig.getJavbusapiUrl() + "movies/search?" + "keyword=" + keyword + "&page=" + page + "&magnet=all";
        String result = HttpUtil.get(url);
        MoviesDto moviesDto = JSONObject.parseObject(result, MoviesDto.class);
        List<String> list = new ArrayList<>();
        for (MovieDto movie : moviesDto.getMovies()) {
            list.add(movie.getId());
        }
        return getMoviesSummary(list, null);
    }

    private void saveStars(StarsDto[] stars) {
        if (stars != null) {
            for (StarsDto star : stars) {
                String url = apiConfig.getJavbusapiUrl() + "stars/" + star.getStarId();
                String result = HttpUtil.get(url);
                if (result != null) {
                    Stars s = JSONObject.parseObject(result, Stars.class);
                    starsService.saveOrUpdate(s);
                }
            }
        }
    }

    public Map<String, Object> starSearchMoviesList(Integer page, String id) {
        Stars stars = starsService.getById(id);
        List<SummaryDto> summaryDtos = searchMoviesList(page, stars.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("star", stars);
        map.put("movies", summaryDtos);
        return map;
    }

}
