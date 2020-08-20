package com.zhaofei.framework.article.api.entity;

import com.zhaofei.framework.common.base.entity.BaseBean;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleData extends BaseBean {

    public ArticleData() {
    }

    public ArticleData(String username) {
        super.setUsername(username);
    }

    public ArticleData(Object o, String username) {
        super.setUsername(username);
    }

    /**
     * 主键
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 分类：0.文章
     */
    private String sort;
    /**
     * 文章标签
     */
    private String label;
    /**
     * state
     */
    private Integer state;
    /**
     * state
     */
    private String stateStr;
    /**
     * 浏览次数
     */
    private Integer visitsNumber;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建时间
     */
    private String updateTime;


    public Integer getState() {
        return state == null ? 0 : state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getVisitsNumber() {
        return visitsNumber == null ? 0 : visitsNumber;
    }

    public void setVisitsNumber(Integer visitsNumber) {
        this.visitsNumber = visitsNumber;
    }
}
