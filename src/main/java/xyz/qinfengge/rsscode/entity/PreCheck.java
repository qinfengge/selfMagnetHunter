package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName pre_check
 */
@TableName(value ="pre_check")
@Data
public class PreCheck implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 番号
     */
    private String code;

    /**
     * 是否等待下载中文字幕（1：等待中文磁力，2：不等待）
     */
    private Integer waitCnsub;

    /**
     * 是否自动过期（0：不自动过期，5：5天后自动过期）
     */
    private Integer autoExpired;

    /**
     * 状态（0：未捕捉，1：已捕捉，2：已过期）
     */
    private Integer status;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}