package com.zhaofei.framework.article.api.constant;

public enum ArticleConstant {
    LABEL_TYPE(1);


    ArticleConstant(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
