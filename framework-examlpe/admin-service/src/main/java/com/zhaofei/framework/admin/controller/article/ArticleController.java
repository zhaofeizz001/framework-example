package com.zhaofei.framework.admin.controller.article;

import com.zhaofei.framework.admin.bean.req.ArticleListResponseBean;
import com.zhaofei.framework.admin.bean.req.InsertArticleEntityRequestBean;
import com.zhaofei.framework.admin.utils.UserTokenUtils;
import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.article.api.service.ArticleService;
import com.zhaofei.framework.article.api.utils.ArticleSort;
import com.zhaofei.framework.article.api.utils.ArticleState;
import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController extends AbstractController {

    @DubboReference(retries = 0, cluster = "failfast")
    private ArticleService articleService;

    @PostMapping("/getList")
    public RestResult<List<ArticleListResponseBean>> getArticleList(){
        ArticleData articleEntity = new ArticleData(UserTokenUtils.getUserInfo().getUsername());
        PageResponseBean<ArticleData> pageResponseBean =
                articleService.selectList(new PageRequestBean<>(articleEntity));
        List<ArticleListResponseBean> result = new ArrayList<>(pageResponseBean.getSize());
        pageResponseBean.getResult().forEach(bean -> {
            try {
                ArticleListResponseBean articleListResponseBean = new ArticleListResponseBean();
                BeanUtils.copyProperties(bean, articleListResponseBean);
                result.add(articleListResponseBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return this.genSuccessResult(result);
    }

    @PostMapping("/insertArticle")
    public RestResult<ArticleData> insertArticle(@RequestBody @Validated InsertArticleEntityRequestBean entityRequestBean){
        ArticleData resultBean = null;
        try {
            ArticleData articleEntity = new ArticleData();
            BeanUtils.copyProperties(entityRequestBean, articleEntity);
            articleEntity.setUsername(UserTokenUtils.getUserInfo().getUsername());
            articleEntity.setLabel(entityRequestBean.getLabel().toString());
            resultBean = articleService.insertArticleEntity(articleEntity);
            System.out.println(JsonUtils.objtoJson(resultBean));
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
        System.out.println(JsonUtils.objtoJson(resultBean));
        return this.genSuccessResult(resultBean);
    }
}
