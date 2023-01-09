package xyz.qinfengge.rsscode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.qinfengge.rsscode.entity.Queue;
import xyz.qinfengge.rsscode.service.QueueService;
import xyz.qinfengge.rsscode.mapper.QueueMapper;
import org.springframework.stereotype.Service;

/**
* @author 71856
* @description 针对表【queue】的数据库操作Service实现
* @createDate 2022-12-16 14:30:43
*/
@Service
public class QueueServiceImpl extends ServiceImpl<QueueMapper, Queue>
    implements QueueService{

}




