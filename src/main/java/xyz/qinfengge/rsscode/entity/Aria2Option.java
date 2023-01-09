package xyz.qinfengge.rsscode.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author lza
 * @Date 2022/12/16/11/27
 **/
@Data
@Accessors(chain = true)
public class Aria2Option {
    /**
     * 下载根目录
     */
    String dir;
    /**
     * 目标文件名
     */
    String out;
    /**
     * referer 用来绕开部分防盗链机制 星号表示使用url作为referer
     */
    String referer;
}
