package xyz.qinfengge.rsscode.service;

import xyz.qinfengge.rsscode.dto.StarsDetailDto;
import xyz.qinfengge.rsscode.entity.Stars;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 71856
* @description 针对表【stars】的数据库操作Service
* @createDate 2022-12-16 14:30:43
*/
public interface StarsService extends IService<Stars> {

    List<StarsDetailDto> getStarsDetail(List<String> ids);
}
