package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName aria2
 */
@TableName(value ="aria2")
@Data
public class Aria2 implements Serializable {
    /**
     * aria2下载器ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * aria2下载器名称
     */
    private String name;

    /**
     * aria2下载器地址
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 路径
     */
    private String path;

    /**
     * 凭证
     */
    private String auth;

    /**
     * 最大任务量
     */
    private Integer maxTask;

    /**
     * 是否默认下载器
     */
    private Integer isDefault;

    @TableField(exist = false)
    private Boolean defaultStatus;

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

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
        this.defaultStatus = isDefault == 1;
    }
}