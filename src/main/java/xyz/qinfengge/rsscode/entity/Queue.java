package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName queue
 */
@TableName(value ="queue")
@Data
public class Queue implements Serializable {
    /**
     * 队列ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 队列名称
     */
    private String name;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 查询条件（1：演员名，2：标签名，3：系列名）
     */
    private Integer type;

    /**
     * 定时任务（1：每天，2：每周，3：每月）
     */
    private Integer schedule;

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
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}