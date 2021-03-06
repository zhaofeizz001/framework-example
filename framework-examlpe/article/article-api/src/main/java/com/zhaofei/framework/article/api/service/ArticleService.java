package com.zhaofei.framework.article.api.service;

import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.common.base.service.BaseService;

public interface ArticleService extends BaseService<ArticleData> {


    ArticleData insertArticleEntity(ArticleData articleData);

    void increaseVisitsNumberById(ArticleData articleData);

    ArticleData getArticlesById(ArticleData articleData) throws Exception;
}
