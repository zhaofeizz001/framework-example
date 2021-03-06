package com.zhaofei.framework.article.service.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private Integer sort;
    /**
     * 文章标签
     */
    private Integer label;
    /**
     * state
     */
    private Integer state;
    /**
     * 浏览次数
     */
    private Integer visitsNumber;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间
     */
    private Date updateTime;

}


