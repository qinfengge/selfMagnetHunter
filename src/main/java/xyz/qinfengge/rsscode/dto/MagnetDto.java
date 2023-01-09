package xyz.qinfengge.rsscode.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author lza
 * @Date 2022/12/16/17/34
 **/
@Data
public class MagnetDto {
    private String id;
    private String link;
    private boolean isHD;
    private String title;
    private String size;
    private long numberSize;
    private String shareDate;
    private boolean hasSubtitle;
}
