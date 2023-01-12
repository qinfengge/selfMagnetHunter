package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName download
 */
@TableName(value ="download")
@Data
public class Download implements Serializable {
    /**
     * 下载表ID
     */
    @TableId
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 磁力链接
     */
    private String link;

    /**
     * 大小
     */
    private String size;

    /**
     * 是否有字幕（0：无，1：有）
     */
    private Integer hasSub;

    /**
     * 分享日期
     */
    private String shareDate;

    /**
     * 下载状态 0：下载中，1：已完成
     */
    private Integer status;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}