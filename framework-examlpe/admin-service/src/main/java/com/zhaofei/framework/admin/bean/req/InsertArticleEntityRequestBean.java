package com.zhaofei.framework.admin.bean.req;

import com.zhaofei.framework.common.base.entity.BaseRequestBean;
import lombok.Data;

import java.io.Serializable;

@Data
public class InsertArticleEntityRequestBean extends BaseRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
