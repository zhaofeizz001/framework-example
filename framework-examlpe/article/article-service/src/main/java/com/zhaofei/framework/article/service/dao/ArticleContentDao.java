package com.zhaofei.framework.article.service.dao;


import com.zhaofei.framework.article.service.entity.ArticleContentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleContentDao {

    int insert(ArticleContentEntity articleContentEntity);

    ArticleContentEntity selectOneById(ArticleContentEntity articleContentEntity);
}
