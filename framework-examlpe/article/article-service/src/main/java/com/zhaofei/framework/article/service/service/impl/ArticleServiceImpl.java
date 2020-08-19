package com.zhaofei.framework.article.service.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.article.service.entity.ArticleEntity;
import com.zhaofei.framework.article.api.service.ArticleService;
import com.zhaofei.framework.article.service.dao.ArticleDao;
import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.zhaofei.framework.article.api.constant.ArticleConstant.LABEL_TYPE;
import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_KEY;
import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_TYPE;

@DubboService
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public PageResponseBean<ArticleData> selectList(PageRequestBean<ArticleData> pageRequestBean) {
        PageHelper.startPage(pageRequestBean.getPageNum(), pageRequestBean.getPageSize());
        Page<ArticleEntity> page =  (Page<ArticleEntity>) articleDao.getList();
        List<ArticleData> list = new ArrayList<>();
        page.getResult().forEach(bean -> {
            ArticleData articleData = new ArticleData();
            BeanUtils.copyProperties(bean, articleData);
            articleData.setLabel(RedisUtils.getMapField(
                            DICT_TYPE.getKey(LABEL_TYPE.getType().toString()),
                            DICT_KEY.getKey(bean.getLabel().toString()),
                            String.class
            ));
            list.add(articleData);
        });


        return new PageResponseBean<>(pageRequestBean,page.getTotal(), list);
    }

    @Override
    public ArticleData insertArticleEntity(ArticleData articleData) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        int i = articleDao.insertArticleEntity(articleEntity);
        if(i > 0){
            articleEntity.getId();
            ArticleEntity articleEntity1 = articleDao.selectOneArticleById(articleEntity);
            ArticleData articleData1 = new ArticleData();
            BeanUtils.copyProperties(articleEntity1, articleData1);
            return articleData1;
        } else {
            return null;
        }

    }

    @Override
    public ArticleData selectOneById(ArticleData articleData) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        ArticleEntity articleEntity1 = articleDao.selectOneArticleById(articleEntity);
        ArticleData articleData1 = new ArticleData();
        BeanUtils.copyProperties(articleEntity1, articleData1);
        return articleData1;
    }

}
