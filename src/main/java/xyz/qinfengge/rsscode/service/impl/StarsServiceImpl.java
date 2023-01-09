package xyz.qinfengge.rsscode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.qinfengge.rsscode.dto.StarsDetailDto;
import xyz.qinfengge.rsscode.entity.Stars;
import xyz.qinfengge.rsscode.service.StarsService;
import xyz.qinfengge.rsscode.mapper.StarsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 71856
* @description 针对表【stars】的数据库操作Service实现
* @createDate 2022-12-16 14:30:43
*/
@Service
public class StarsServiceImpl extends ServiceImpl<StarsMapper, Stars>
    implements StarsService{
    
    @Resource
    private StarsMapper starsMapper;

    @Override
    public List<StarsDetailDto> getStarsDetail(List<String> ids) {
        LambdaQueryWrapper<Stars> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Stars::getId, ids);
        List<Stars> stars = starsMapper.selectList(queryWrapper);
        List<StarsDetailDto> result = new ArrayList<>();
        for (Stars star : stars) {
            StarsDetailDto starsDetailDto = new StarsDetailDto();
            BeanUtils.copyProperties(star, starsDetailDto);
            result.add(starsDetailDto);
        }
        return result;
    }
}

