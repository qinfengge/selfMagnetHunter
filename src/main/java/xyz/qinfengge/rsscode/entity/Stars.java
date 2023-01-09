package xyz.qinfengge.rsscode.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName stars
 */
@TableName(value ="stars")
@Data
public class Stars implements Serializable {
    /**
     * 此id为javBus对应演员id
     */
    @TableId
    private String id;

    /**
     * 演员头像
     */
    private String avatar;

    /**
     * 演员名
     */
    private String name;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 年龄
     */
    private String age;

    /**
     * 身高
     */
    private String height;

    /**
     * 胸围
     */
    private String bust;

    /**
     * 腰围
     */
    private String waistline;

    /**
     * 臀围
     */
    private String hipline;

    /**
     * 出生地
     */
    private String birthplace;

    /**
     * 爱好
     */
    private String hobby;

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
     * 逻辑删除(1:已删除，0:未删除)
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}