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
import java.util.*;
import java.util.concurrent.*;

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

    /**
     * 此标记用于判断是否已执行过list的入库操作
     */
    private List<String> flags = new ArrayList<>();

    /**
     * 获取番号列表
     * @param page 页数
     * @param magnetType 磁力链接类型 1：全部，2：有磁力链接
     * @param type 类型 1：普通，2：无码 可以为空
     * @return 返回番号列表
     */
    public List<String> getMoviesList(Integer page, Integer magnetType, Integer type) {
        String magnet = magnetType == 1 ? "all" : "exist";
        String url = apiConfig.getJavbusapiUrl() + "movies?" + "page=" + page + "&magnet=" + magnet;
        if (type != null) {
            String typeStr = type == 1 ? "normal" : "uncensored";
            url += "&type=" + typeStr;
        }
        return getCodeListByUrl(url);
    }

    /**
     * 获取番号列表
     * @param url 请求地址
     * @return 返回番号列表
     */
    private List<String> getCodeListByUrl(String url) {
        String result = HttpUtil.get(url);
        MoviesDto moviesDto = JSONObject.parseObject(result, MoviesDto.class);
        List<String> list = new ArrayList<>();
        for (MovieDto movie : moviesDto.getMovies()) {
            list.add(movie.getId());
        }
        return list;
    }

    /**
     * 使用多线程获取列表的详情信息
     * @param list 番号列表
     * @param type 类型 1：普通，2：无码 可以为空
     * @return 返回番号详情
     */
    public List<ApiDetailDto> getMoviesDetail(List<String> list, String type) {
        List<ApiDetailDto> apiDetailDtoList = new ArrayList<>();
        List<Detail> detailList = new ArrayList<>();
        //最大30线程，外加30队列；【拒绝策略AbortPolicy（默认）： 丢弃任务并抛出异常】
        ExecutorService executorService = new ThreadPoolExecutor(30, 30, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(30), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (String id : list) {
            Runnable apiRequest = () -> {
                String url = apiConfig.getJavbusapiUrl() + "movies/" + id;
                String result = HttpUtil.get(url);
                ApiDetailDto apiDetailDto = JSONObject.parseObject(result, ApiDetailDto.class);
                apiDetailDtoList.add(apiDetailDto);
                if (flags.size() == 0 || !flags.equals(list)) {
                    Detail d = saveStarsAndDetail(type, apiDetailDto);
                    detailList.add(d);
                }
            };
            executorService.submit(apiRequest);
        }
        executorService.shutdown();
        try {
            boolean termination = executorService.awaitTermination(1, TimeUnit.MINUTES);
            if (termination) {
                System.err.println("线程池关闭成功");
                flags = list;
                detailService.saveOrUpdateBatch(detailList);
                return apiDetailDtoList;
            } else {
                System.err.println("线程池关闭失败");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 保存明星和番号详情
     * @param type 类型 1：普通，2：无码 可以为空
     * @param apiDetailDto 网络接口对应对象
     * @return 数据库的对应对象
     */
    private Detail saveStarsAndDetail(String type, ApiDetailDto apiDetailDto) {
        //保存演员信息
        StarsDto[] stars = apiDetailDto.getStars();
        //对于合集番号，只截取10个女优，防止超出数据库长度及增加耗时
        if (stars != null) {
            stars = stars.length >= 10 ? Arrays.copyOfRange(stars, 0, 10) : stars;
        }
        saveStars(stars);
        apiDetailDto.setStars(stars);
        //设置类型
        apiDetailDto.setIsCensored(type);
        Detail d = detail2Api.a2d(apiDetailDto);
        d.setIsCensored(type);
        return d;
    }

    /**
     * 获取列表的摘要信息
     * @param list 番号集合
     */
    public List<SummaryDto> getMoviesSummary(List<String> list, String type) {
        List<ApiDetailDto> moviesDetail = getMoviesDetail(list, type);
        List<SummaryDto> summaryDtoList = new ArrayList<>();
        for (ApiDetailDto dto : moviesDetail) {
            SummaryDto summaryDto = new SummaryDto();
            BeanUtils.copyProperties(dto, summaryDto);
            MagnetDto[] magnets = dto.getMagnets();
            if (magnets != null && magnets.length > 0) {
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
     * 获取某部作品的详情
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
                detailService.saveOrUpdate(saveStarsAndDetail(null, apiDetailDto));
                return apiDetailDto;
            }
        }
        return null;
    }

    /**
     * 关键字搜索
     * @param page 页码
     * @param keyword 关键字
     * @return 搜索结果摘要
     */
    public List<SummaryDto> searchMoviesList(Integer page, String keyword) {
        String url = apiConfig.getJavbusapiUrl() + "movies/search?" + "keyword=" + keyword + "&page=" + page + "&magnet=all";
        List<String> list = getCodeListByUrl(url);
        return getMoviesSummary(list, null);
    }

    /**
     * 女优搜索结果
     * @param page 页码
     * @param starId 女优id
     * @return 搜索结果摘要
     */
    public List<SummaryDto> starSearchList(Integer page, String starId) {
        String url = apiConfig.getJavbusapiUrl() + "movies?" + "page=" + page + "&starId=" + starId + "&magnet=all";
        List<String> list = getCodeListByUrl(url);
        return getMoviesSummary(list, null);
    }

    /**
     * 保存女优信息
     * @param stars 网络接口对象
     */
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

    /**
     * 根据女优Id搜索
     * @param page 页码
     * @param id 女优id
     * @return 搜索结果摘要
     */
    public Map<String, Object> starSearchMoviesList(Integer page, String id) {
        Stars stars = starsService.getById(id);
        List<SummaryDto> summaryDtos = starSearchList(page, id);
        Map<String, Object> map = new HashMap<>();
        map.put("star", stars);
        map.put("movies", summaryDtos);
        return map;
    }

}
