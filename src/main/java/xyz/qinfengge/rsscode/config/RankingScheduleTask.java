package xyz.qinfengge.rsscode.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.qinfengge.rsscode.utils.RssHandelUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lza
 * @Date 2023/01/11/15/39
 **/
@Component
public class RankingScheduleTask {

    @Resource
    private RssHandelUtil rssHandelUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(fixedDelay = 5000)
    private void dailyRankingTask() throws Exception {
        List<String> list = rssHandelUtil.rssHandel("http://107.173.156.30:1200/javdb/rankings/censored/daily?limit=48");
        stringRedisTemplate.opsForValue().set("dayRanking", String.join(",", parseXml(list)));
    }

    //    @Scheduled(cron = "0 0 1 ? * 1")
//    @Scheduled(fixedDelay = 5000)
    private void weeklyRankingTask() throws Exception {
        List<String> list = rssHandelUtil.rssHandel("http://107.173.156.30:1200/javdb/rankings/censored/weekly?limit=48");
        stringRedisTemplate.opsForValue().set("weekRanking", String.join(",", parseXml(list)));
    }

    //    @Scheduled(cron = "0 0 2 1 * ?")
//    @Scheduled(fixedDelay = 5000)
    private void monthlyRankingTask() throws Exception {
        List<String> list = rssHandelUtil.rssHandel("http://107.173.156.30:1200/javdb/rankings/censored/monthly?limit=48");
        stringRedisTemplate.opsForValue().set("monthRanking", String.join(",", parseXml(list)));
    }

    /**
     * 解析xml
     *
     * @param list
     * @return
     */
    private List<String> parseXml(List<String> list) {
        List<String> codes = new ArrayList<>();
        for (String code : list) {
            String[] strings = code.split(" ");
            codes.add(strings[0]);
        }
        return codes;
    }
}
