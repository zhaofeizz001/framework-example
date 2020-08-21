package com.zhaofei.framework.article.service.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.article.api.utils.ArticleSort;
import com.zhaofei.framework.article.api.utils.ArticleState;
import com.zhaofei.framework.article.service.dao.ArticleContentDao;
import com.zhaofei.framework.article.service.entity.ArticleContentEntity;
import com.zhaofei.framework.article.service.entity.ArticleEntity;
import com.zhaofei.framework.article.api.service.ArticleService;
import com.zhaofei.framework.article.service.dao.ArticleDao;
import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.zhaofei.framework.article.api.constant.ArticleConstant.LABEL_TYPE;
import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_KEY;
import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_TYPE;

@DubboService
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleContentDao articleContentDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            articleData.setSort(ArticleSort.getName(bean.getSort()));
            articleData.setStateStr(ArticleState.getName(bean.getState()));
            articleData.setCreateTime(simpleDateFormat.format(bean.getCreateTime()));
            articleData.setUpdateTime(simpleDateFormat.format(bean.getUpdateTime()));
            list.add(articleData);
        });


        return new PageResponseBean<>(pageRequestBean,page.getTotal(), list);
    }

    @Override
    @Transactional
    public ArticleData insertArticleEntity(ArticleData articleData) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        articleEntity.setLabel(Integer.valueOf(articleData.getLabel()));
        articleEntity.setSort(Integer.valueOf(articleData.getSort()));
        articleEntity.setContentId(-1);
        int i = articleDao.insertArticleEntity(articleEntity);
        if(i > 0){
            Integer id = articleEntity.getId();
            ArticleContentEntity articleContentEntity = new ArticleContentEntity();
            articleContentEntity.setArticleId(id);
            articleContentEntity.setContent(articleData.getContent());
            int insert = articleContentDao.insert(articleContentEntity);
            if(insert > 0){
                Integer articleContentId = articleContentEntity.getId();
                articleEntity.setContentId(articleContentId);
                articleDao.updateById(articleEntity);
            }
            ArticleEntity articleEntity1 = articleDao.selectOneArticleById(articleEntity);
            ArticleData articleData1 = new ArticleData();
            BeanUtils.copyProperties(articleEntity1, articleData1);
            return articleData1;
        } else {
            return null;
        }

    }

    @Override
    public ArticleData selectOneById(ArticleData articleData) throws Exception {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        ArticleEntity articleBean = articleDao.selectOneArticleById(articleEntity);
        ArticleData result = new ArticleData();
        BeanUtils.copyProperties(articleBean, result);
        result.setLabel(RedisUtils.getMapField(
                DICT_TYPE.getKey(LABEL_TYPE.getType().toString()),
                DICT_KEY.getKey(articleBean.getLabel().toString()),
                String.class
        ));
        ArticleContentEntity entity = new ArticleContentEntity();
        entity.setId(articleBean.getContentId());
        ArticleContentEntity contentEntity = articleContentDao.selectOneById(entity);
        if(contentEntity == null && StringUtils.isEmpty(contentEntity.getContent())){
            throw new Exception();
        }
        result.setContent(contentEntity.getContent());
        return result;
    }

    @Override
    public int updateById(ArticleData articleData) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        return articleDao.updateById(articleEntity);
    }

    @Override
    public void increaseVisitsNumberById(ArticleData articleData) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleData, articleEntity);
        articleDao.updateVisitsNumberById(articleEntity);
    }

    @Override
    @Transactional
    public ArticleData getArticlesById(ArticleData articleData) throws Exception {
        ArticleData result = this.selectOneById(articleData);
        this.increaseVisitsNumberById(articleData);
        return result;
    }
}
