package xyz.qinfengge.rsscode.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author lza
 * @Date 2022/12/19/15/53
 **/
@Data
public class MovieDto {
    private Date date;
    private String id;
    private String img;
    private String title;
    private Object tags;
}
