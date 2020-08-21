package com.zhaofei.framework.admin.controller.article;

import com.zhaofei.framework.admin.bean.req.InsertArticleEntityRequestBean;
import com.zhaofei.framework.admin.utils.UserTokenUtils;
import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.article.api.service.ArticleService;
import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.base.entity.RestResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController extends AbstractController {

    @DubboReference(retries = 0, cluster = "failfast")
    private ArticleService articleService;

    @PostMapping("/getList/{size}/{index}")
    public RestResult<PageResponseBean> getArticleList(
            @PathVariable("size") Integer size,
            @PathVariable("index") Integer index){
        ArticleData articleEntity = new ArticleData(UserTokenUtils.getUserInfo().getUsername());
        PageRequestBean<ArticleData> requestBean = new PageRequestBean<>(articleEntity);
        requestBean.setPageNum(index);
        requestBean.setPageSize(size);
        PageResponseBean<ArticleData> pageResponseBean = articleService.selectList(requestBean);
        return this.genSuccessResult(pageResponseBean);
    }

    @PostMapping("/insertArticle")
    public RestResult<ArticleData> insertArticle(@RequestBody @Validated InsertArticleEntityRequestBean entityRequestBean){
        ArticleData resultBean = null;
        try {
            ArticleData articleEntity = new ArticleData();
            BeanUtils.copyProperties(entityRequestBean, articleEntity);
            articleEntity.setUsername(UserTokenUtils.getUserInfo().getUsername());
            articleEntity.setLabel(entityRequestBean.getLabel().toString());
            articleEntity.setSort(entityRequestBean.getSort().toString());
            resultBean = articleService.insertArticleEntity(articleEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.genSuccessResult(resultBean);
    }

    @PostMapping("/selectArticlesById/{id}")
    public RestResult<ArticleData> selectArticlesById( @PathVariable("id") Integer id
    ) throws Exception{
        ArticleData articleData = new ArticleData(UserTokenUtils.getUserInfo().getUsername());
        articleData.setId(id);
        ArticleData resultBean = articleService.getArticlesById(articleData);
        return this.genSuccessResult(resultBean);
    }
}
