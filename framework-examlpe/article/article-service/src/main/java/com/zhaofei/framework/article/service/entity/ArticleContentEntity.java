package com.zhaofei.framework.article.service.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleContentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 文章表id
     */
    private Integer articleId;
    /**
     * 内容
     */
    private String content;
}


