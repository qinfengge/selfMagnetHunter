package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * 
 * @author 71856
 * @TableName detail
 */
@TableName(value ="detail")
@Data
public class Detail implements Serializable {
    /**
     * 番号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String preview;

    /**
     * 系列，根据番号分割，如IPX系列
     */
    private String series;

    /**
     * 磁力集合
     * private String magnet;
     * 转到redis
     */

    /**
     * 预览图片数组
     * private String picture;
     */


    /**
     * 发行日期
     */
    private String pubDate;

    /**
     * 时长
     */
    private Integer duration;

    /**
     * 演员
     */
    private String actor;

    /**
     * 标签，数组形式 [1,2]
     * private String tags;
     */

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 导演
     */
    private String director;

    /**
     * 是否有码（1：有码，2：无码）
     */
    private String isCensored;

    /**
     * 原始链接URL
     * private String detailLink;
     * 格式固定为https://www.javbus.com/番号
     */


    /**
     * aria2推送状态（0：未推送，1：已推送）
     */
    private Integer aria2Status;

    /**
     * 是否有中文字幕(1：有字幕，0：无字幕)
     */
    private Integer cnSub;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}