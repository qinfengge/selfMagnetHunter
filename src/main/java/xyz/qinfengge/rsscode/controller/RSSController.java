package xyz.qinfengge.rsscode.controller;

import org.springframework.web.bind.annotation.*;
import xyz.qinfengge.rsscode.config.APIConfig;
import xyz.qinfengge.rsscode.config.Result;
import xyz.qinfengge.rsscode.dto.ApiDetailDto;
import xyz.qinfengge.rsscode.dto.StarsDetailDto;
import xyz.qinfengge.rsscode.dto.SummaryDto;
import xyz.qinfengge.rsscode.service.StarsService;
import xyz.qinfengge.rsscode.utils.ApiHandelUtil;
import xyz.qinfengge.rsscode.utils.RssHandelUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lza
 * @Date 2022/12/16/17/12
 **/
@RestController
@RequestMapping("rss")
public class RSSController {

    @Resource
    private RssHandelUtil rssHandelUtil;

    @Resource
    private ApiHandelUtil apiHandelUtil;

    @Resource
    private APIConfig apiConfig;


    @Resource
    private StarsService starsService;

    /**
     * 设置主页返回内容
     * @param page 页数
     * @param magnetType 磁力链接类型 1：全部，2：有磁力链接
     * @param type 类型 1：普通，2：无码 可以为空
     * @return 返回摘要列表，包含是否有磁力链接和字幕
     */
    @GetMapping(value = {"index/{page}/{magnetType}/{type}", "index/{page}/{magnetType}"})
    public Result<Object> index(@PathVariable Integer page, @PathVariable Integer magnetType, @PathVariable(required = false) Integer type) {
        List<String> list = apiHandelUtil.getMoviesList(page, magnetType, type);
        List<SummaryDto> moviesSummary = apiHandelUtil.getMoviesSummary(list, type == null ? "0" : type.toString());
        return Result.ok(moviesSummary);
    }

    /**
     * 根据番号获取详情
     * @param id 番号
     * @return 返回番号详情
     */
    @GetMapping(value = {"detail/{id}"})
    public Result<Object> detail(@PathVariable String id) {
        ApiDetailDto apiDetailDto = apiHandelUtil.getDetail(id);
        return Result.ok(apiDetailDto);
    }


    /**
     * 关键字搜索
     * @param page 页数
     * @param keyword  关键字
     * @return 返回搜索摘要列表
     */
    @GetMapping(value = {"search/{page}/{keyword}"})
    public Result<Object> search(@PathVariable Integer page, @PathVariable String keyword) {
        List<SummaryDto> moviesSummary = apiHandelUtil.searchMoviesList(page, keyword);
        return Result.ok(moviesSummary);
    }

    /**
     * 获取演员详情
     * @param ids
     * @return
     */
    @PostMapping(value = {"starsDetail"})
    public Result<Object> starsDetail(@RequestBody List<String> ids) {
        List<StarsDetailDto> details = starsService.getStarsDetail(ids);
        return Result.ok(details);
    }

    @GetMapping(value = {"starSearch/{page}/{id}"})
    public Result<Object> starSearch(@PathVariable Integer page, @PathVariable String id) {
        Map<String, Object> map = apiHandelUtil.starSearchMoviesList(page, id);
        return Result.ok(map);
    }
}
