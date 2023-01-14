package xyz.qinfengge.rsscode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;
import xyz.qinfengge.rsscode.config.Result;
import xyz.qinfengge.rsscode.dto.MagnetDto;
import xyz.qinfengge.rsscode.entity.Aria2;
import xyz.qinfengge.rsscode.entity.Download;
import xyz.qinfengge.rsscode.service.Aria2Service;
import xyz.qinfengge.rsscode.service.DownloadService;
import xyz.qinfengge.rsscode.utils.Aria2Util;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lza
 * @Date 2023/01/12/13/40
 **/
@RestController
@RequestMapping("/aria2")
public class Aria2Controller {

    @Resource
    private Aria2Service aria2Service;

    @Resource
    private Aria2Util aria2Util;

    @Resource
    private DownloadService downloadService;

    @PostMapping("add")
    public Result<Object> addAria2(@RequestBody Aria2 aria2) {
        boolean result = aria2Service.saveOrUpdate(aria2);
        if (result) {
            return getAria2Status(aria2.getId());
        } else {
            return Result.fail("添加失败");
        }
    }

    @PutMapping("setDefault/{id}")
    public Result<Object> setDefault(@PathVariable Integer id) {
        LambdaQueryWrapper<Aria2> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Aria2::getIsDefault, 1);
        Aria2 aria2 = aria2Service.getBaseMapper().selectOne(lambdaQueryWrapper);
        if (aria2 != null) {
            aria2.setIsDefault(0);
            aria2Service.getBaseMapper().updateById(aria2);
        }
        aria2 = aria2Service.getBaseMapper().selectById(id);
        aria2.setIsDefault(1);
        aria2Service.getBaseMapper().updateById(aria2);
        return Result.ok();
    }

    @DeleteMapping("delete/{id}")
    public Result<Object> deleteAria2(@PathVariable Integer id) {
        int i = aria2Service.getBaseMapper().deleteById(id);
        if (i > 0) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("list")
    public Result<Object> listAria2() {
        return Result.ok(aria2Service.getBaseMapper().selectList(null));
    }

    @GetMapping("get/{id}")
    public Result<Object> getAria2(@PathVariable Integer id) {
        return Result.ok(aria2Service.getBaseMapper().selectById(id));
    }

    @GetMapping("status/{id}")
    public Result<Object> getAria2Status(@PathVariable Integer id) {
        Aria2 aria2 = aria2Service.getBaseMapper().selectById(id);
        String url = aria2.getHost() + ":" + aria2.getPort() + "/" + aria2.getPath();
        try {
            String result = Aria2Util.getVersion(url, aria2.getAuth());
            if (result != null) {
                return Result.ok("Aria2连接成功");
            } else {
                deleteAria2(id);
                return Result.fail("Aria2连接失败");
            }
        } catch (Exception e) {
            deleteAria2(id);
            return Result.fail("连接失败,请检查配置");
        }
    }

    /**
     * 用于排序，把default下载器置前
     *
     * @return
     */
    @GetMapping("seqList")
    public Result<Object> getSeqList() {
        LambdaQueryWrapper<Aria2> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Aria2::getIsDefault);
        List<Aria2> aria2s = aria2Service.getBaseMapper().selectList(wrapper);
        return Result.ok(aria2s);
    }

    /**
     * 获取默认下载器后直接下载，不支持高级设置
     *
     * @param magnetDto
     * @return
     */
    @PostMapping("send")
    public Result<Object> sendUrl(@RequestBody MagnetDto magnetDto) {
        Aria2 aria2;
        // 先查默认
        LambdaQueryWrapper<Aria2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Aria2::getIsDefault, 1);
        aria2 = aria2Service.getBaseMapper().selectOne(wrapper);
        // 没有默认再设置第一个
        if (aria2 == null) {
            List<Aria2> list = aria2Service.getBaseMapper().selectList(null);
            if (list.size() > 0) {
                aria2 = list.get(0);
            } else {
                return Result.fail("请先添加Aria2");
            }
        }

        String host = aria2.getHost() + ":" + aria2.getPort() + "/" + aria2.getPath();
        aria2Util.setMethod(Aria2Util.METHOD_ADD_URI)
                .addParam("token:" + aria2.getAuth())
                .addParam(new String[]{magnetDto.getLink()});
        ;
        String send = aria2Util.send(host);
        if (send != null) {
            return Result.ok("发送成功");
        } else {
            return Result.fail("发送失败");
        }
    }

    /**
     * 选择下载器下载
     *
     * @param magnetDto
     * @return
     */
    @PostMapping("send/{id}")
    public Result<Object> sendUrl(@PathVariable Integer id, @RequestBody MagnetDto magnetDto) {
        Aria2 aria2 = aria2Service.getBaseMapper().selectById(id);
        String host = aria2.getHost() + ":" + aria2.getPort() + "/" + aria2.getPath();
        aria2Util.setId(magnetDto.getId())
                .setMethod(Aria2Util.METHOD_ADD_URI)
                .addParam("token:" + aria2.getAuth())
                .addParam(new String[]{magnetDto.getLink()});
        ;
        String send = aria2Util.send(host);
        if (send != null) {
            // TODO 保存到下载表
            saveDownload(magnetDto);
            return Result.ok(send);
        } else {
            return Result.fail("发送失败");
        }
    }

    /**
     * 下载完成后使用aria2的--on-download-complete修改下载状态
     *
     * @param id
     */
    @PutMapping("complete/{id}")
    public void downloadComplete(@PathVariable Integer id) {
        Download download = downloadService.getBaseMapper().selectById(id);
        download.setStatus(1);
        downloadService.getBaseMapper().updateById(download);
    }

    private void saveDownload(MagnetDto magnetDto) {
        Download download = new Download();
        download.setId(magnetDto.getId());
        download.setTitle(magnetDto.getTitle());
        download.setLink(magnetDto.getLink());
        download.setSize(magnetDto.getSize());
        download.setShareDate(magnetDto.getShareDate());
        if (magnetDto.isHasSubtitle()) {
            download.setHasSub(1);
        } else {
            download.setHasSub(0);
        }
        download.setStatus(0);
        downloadService.getBaseMapper().insert(download);
    }
}
