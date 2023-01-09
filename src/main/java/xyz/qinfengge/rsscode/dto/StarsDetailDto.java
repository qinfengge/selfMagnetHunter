package xyz.qinfengge.rsscode.dto;

import lombok.Data;

/**
 * @Author lza
 * @Date 2022/12/27/19/22
 **/
@Data
public class StarsDetailDto {
    private String id;

    /**
     * 演员头像
     */
    private String avatar;

    /**
     * 演员名
     */
    private String name;
}
