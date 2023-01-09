package xyz.qinfengge.rsscode.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;
import xyz.qinfengge.rsscode.dto.*;
import xyz.qinfengge.rsscode.entity.Detail;
import xyz.qinfengge.rsscode.entity.Stars;
import xyz.qinfengge.rsscode.service.StarsService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 此方法用来转换数据库内容和API返回内容一致
 * @Author lza
 * @Date 2022/12/17/17/40
 **/
@Component
public class Detail2Api {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StarsService starsService;

    public ApiDetailDto d2a(Detail detail){
        ApiDetailDto dto = new ApiDetailDto();
        dto.setId(detail.getId());
        dto.setTitle(detail.getTitle());
        dto.setImg(detail.getPreview());
        dto.setDate(detail.getPubDate());
        dto.setVideoLength(detail.getDuration());
        dto.setDirector(JSONObject.parseObject(detail.getDirector(), DirectorDto.class));
        dto.setStars(getActor(detail.getActor()));
        dto.setSeries(getSeries(detail.getSeries()));
        ApiDetailDto result = getRedis(detail.getId());
        dto.setTags(result.getTags());
        dto.setMagnets(result.getMagnets());
        dto.setSamples(result.getSamples());

        return dto;
    }

    public Detail a2d(ApiDetailDto dto){
        if (dto.getId() == null){
            return null;
        }else {
            Detail detail = new Detail();
            detail.setId(dto.getId());
            detail.setTitle(dto.getTitle());
            detail.setDuration((int) dto.getVideoLength());
            detail.setPreview(dto.getImg());
            detail.setPubDate(dto.getDate());
            detail.setDirector(dto.getDirector() == null ? "" : dto.getDirector().toString());
            detail.setActor(dto.getStars() == null ? "" : setActor(dto.getStars()));
            detail.setSeries(dto.getSeries() == null ? "" : setSeries(dto.getSeries()));
            setRedis(dto.getId(), dto);

            MagnetDto[] magnets = dto.getMagnets();
            for (MagnetDto magnet : magnets) {
                if (magnet.isHasSubtitle()){
                    detail.setCnSub(1);
                    break;
                }else {
                    detail.setCnSub(0);
                }
            }

            String id = dto.getId();
            String[] split = id.split("-");
            detail.setPrefix(split[0].toUpperCase());

            return detail;
        }
    }


    public StarsDto[] getActor(String actor) {
         return JSON.parseObject(actor, StarsDto[].class);
    }

    public String setActor(StarsDto[] starsDtos) {
        List<Map<String, String>> list = new ArrayList<>();
        if (starsDtos != null) {
            for (int i = 0; i < starsDtos.length; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("starId", starsDtos[i].getStarId());
                map.put("starName", starsDtos[i].getStarName());
                list.add(map);
            }
            return JSON.toJSONString(list);
        }else {
            return "";
        }
    }

    public SeriesDto getSeries(String series) {
        return JSON.parseObject(series, SeriesDto.class);
    }

    public String setSeries(SeriesDto seriesDtos) {
        return JSON.toJSONString(seriesDtos);
    }

    private void setRedis(String key, ApiDetailDto dto) {
        ApiDetailDto result = new ApiDetailDto();
        result.setTags(dto.getTags());
        result.setMagnets(dto.getMagnets());
        result.setSamples(dto.getSamples());
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(result));
    }

    private ApiDetailDto getRedis(String key) {
        String s = stringRedisTemplate.opsForValue().get(key);
        return JSON.parseObject(s, ApiDetailDto.class);
    }

//    private List<Stars> getStars(StarsDto[] starsDtos){
//        List<String> ids = Arrays.stream(starsDtos).map(StarsDto::getStarId).collect(Collectors.toList());
//        LambdaQueryWrapper<Stars> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(Stars::getId, ids);
//        return starsService.getBaseMapper().selectList(wrapper);
//    }
}
