package xyz.qinfengge.rsscode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.qinfengge.rsscode.entity.Detail;
import xyz.qinfengge.rsscode.service.DetailService;
import xyz.qinfengge.rsscode.mapper.DetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 71856
* @description 针对表【detail】的数据库操作Service实现
* @createDate 2022-12-16 14:30:43
*/
@Service("detailService")
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail>
    implements DetailService{

}




