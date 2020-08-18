package com.zhaofei.framework.admin.bean.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleListResponseBean implements Serializable {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容id
     */
    private Integer contentId;
    /**
     * 分类：0.文章
     */
    private String sort;
    /**
     * 文章标签
     */
    private String label;
    /**
     * state
     */
    private Integer state;
    /**
     * state
     */
    private String stateStr;
    /**
     * 浏览次数
     */
    private Integer visitsNumber;
    /**
     * 创建时间
     */
    private String updateTime;
}
