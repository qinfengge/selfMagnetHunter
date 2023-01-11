package xyz.qinfengge.rsscode.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qinfengge.rsscode.config.Result;
import xyz.qinfengge.rsscode.dto.ApiDetailDto;
import xyz.qinfengge.rsscode.dto.SummaryDto;
import xyz.qinfengge.rsscode.utils.ApiHandelUtil;
import xyz.qinfengge.rsscode.utils.ThreadPoolUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author lza
 * @Date 2023/01/11/16/12
 **/
@RestController
@RequestMapping("ranking")
public class RankingController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ApiHandelUtil apiHandelUtil;

    @GetMapping("ranking/{type}")
    public Result<Object> ranking(@PathVariable Integer type) {
        switch (type) {
            case 1:
               return day();
            case 2:
               return week();
            case 3:
               return month();
            default:
               return day();
        }
    }

    @GetMapping("day")
    public Result<Object> day() {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("dayRankingDetail"))) {
            return Result.ok(JSON.parseArray(stringRedisTemplate.opsForValue().get("dayRankingDetail"), SummaryDto.class));
        } else {
            String dayRanking = stringRedisTemplate.opsForValue().get("dayRanking");
            assert dayRanking != null;
            List<String> list = Arrays.asList(dayRanking.split(","));
            // 排行榜全是有码
            List<SummaryDto> daySummary = apiHandelUtil.getMoviesSummary(list, "1");
            stringRedisTemplate.opsForValue().set("dayRankingDetail", JSON.toJSONString(daySummary));
            return Result.ok(daySummary);
        }
    }

    @GetMapping("week")
    public Result<Object> week() {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("weekRankingDetail"))) {
            return Result.ok(JSON.parseArray(stringRedisTemplate.opsForValue().get("weekRankingDetail"), SummaryDto.class));
        } else {
            String weekRanking = stringRedisTemplate.opsForValue().get("weekRanking");
            assert weekRanking != null;
            List<String> list = Arrays.asList(weekRanking.split(","));
            // 排行榜全是有码
            List<SummaryDto> weekSummary = apiHandelUtil.getMoviesSummary(list, "1");
            stringRedisTemplate.opsForValue().set("weekRankingDetail", JSON.toJSONString(weekSummary));
            return Result.ok(weekSummary);
        }
    }

    @GetMapping("month")
    public Result<Object> month() {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("monthRankingDetail"))) {
            return Result.ok(JSON.parseArray(stringRedisTemplate.opsForValue().get("monthRankingDetail"), SummaryDto.class));
        } else {
            String monthRanking = stringRedisTemplate.opsForValue().get("monthRanking");
            assert monthRanking != null;
            List<String> list = Arrays.asList(monthRanking.split(","));
            // 排行榜全是有码
            List<SummaryDto> monthSummary = apiHandelUtil.getMoviesSummary(list, "1");
            stringRedisTemplate.opsForValue().set("monthRankingDetail", JSON.toJSONString(monthSummary));
            return Result.ok(monthSummary);
        }
    }
}
