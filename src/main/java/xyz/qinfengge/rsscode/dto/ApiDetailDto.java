package xyz.qinfengge.rsscode.dto;

import lombok.Data;
import xyz.qinfengge.rsscode.entity.Stars;

import java.util.List;

/**
 * @Author lza
 * @Date 2022/12/16/17/30
 **/
@Data
public class ApiDetailDto {
    private String id;
    private String title;
    private String img;
    private String date;
    private long videoLength;
    private Object director;
    /**
     * 是否有码（1：有码，2：无码）
     */
    private String isCensored;
    private SeriesDto series;
    private TagDto[] tags;
    private StarsDto[] stars;
    private MagnetDto[] magnets;
    private SampleDto[] samples;
}
