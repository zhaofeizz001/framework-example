package com.zhaofei.framework.admin.controller.user;

import com.zhaofei.framework.article.api.entity.ArticleData;
import com.zhaofei.framework.article.api.service.ArticleService;
import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.RestResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController extends AbstractController {

    @DubboReference
    private ArticleService articleService;

    @PostMapping("/getList")
    public RestResult<List<ArticleData>> getArticleList(){

        List<ArticleData> list = articleService.getList();

        return this.genSuccessResult(list);
    }
}
