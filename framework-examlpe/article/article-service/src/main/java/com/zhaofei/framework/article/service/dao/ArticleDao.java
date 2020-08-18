package com.zhaofei.framework.article.service.dao;


import com.zhaofei.framework.article.service.entity.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDao {

    List<ArticleEntity> getList();

    int insertArticleEntity(ArticleEntity articleEntity);

    ArticleEntity selectOneArticleById(ArticleEntity articleEntity);
}
