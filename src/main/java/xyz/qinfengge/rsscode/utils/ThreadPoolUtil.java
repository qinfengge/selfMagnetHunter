package xyz.qinfengge.rsscode.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import xyz.qinfengge.rsscode.config.APIConfig;
import xyz.qinfengge.rsscode.dto.ApiDetailDto;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author lza
 * @Date 2023/01/11/16/19
 **/
@Component
public class ThreadPoolUtil {
    @Resource
    private APIConfig apiConfig;

    public List<ApiDetailDto> threadUtil(List<String> list, String type) {
        List<ApiDetailDto> apiDetailDtoList = new ArrayList<>();
        //最大30线程，外加30队列；【拒绝策略AbortPolicy（默认）： 丢弃任务并抛出异常】
        ExecutorService executorService = new ThreadPoolExecutor(list.size(), list.size(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(30), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (String id : list) {
            Runnable apiRequest = () -> {
                String url = apiConfig.getJavbusapiUrl() + "movies/" + id;
                String result = HttpUtil.get(url);
                ApiDetailDto apiDetailDto = JSONObject.parseObject(result, ApiDetailDto.class);
                if (apiDetailDto.getTitle() != null) {
                    apiDetailDto.setIsCensored(type);
                    apiDetailDtoList.add(apiDetailDto);
                }
            };
            executorService.submit(apiRequest);
        }
        executorService.shutdown();
        try {
            boolean termination = executorService.awaitTermination(1, TimeUnit.MINUTES);
            if (termination) {
                System.err.println("线程池关闭成功");
                return apiDetailDtoList;
            } else {
                System.err.println("线程池关闭失败");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
