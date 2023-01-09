package xyz.qinfengge.rsscode.dto;

import lombok.Data;

/**
 * @Author lza
 * @Date 2022/12/19/17/56
 **/
@Data
public class SummaryDto {
    private String id;
    private String title;
    private String img;
    private String date;
    /**
     * 是否有磁力链接
     * 0：无，1：有
     */
    private Integer hasMagnet;
    /**
     * 是否有中文字幕
     * 0：无，1：有
     */
    private Integer hasSub;
}
