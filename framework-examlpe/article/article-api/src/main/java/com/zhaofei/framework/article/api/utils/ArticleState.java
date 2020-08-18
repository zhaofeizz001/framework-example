package com.zhaofei.framework.article.api.utils;

public enum ArticleState {

    DRAFT(0, "草稿"),
    SUBMITTED(1, "已提交"),
    APPROVED(2, "已审批"),
    PUBLISHED(3, "已发布"),
    OFFLINE(4, "已下线"),
    DELETED(5, "已删除"),
    UNKNOWN(-1, "未知");

    ArticleState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code){
        for(ArticleState f : ArticleState.values()){
            if(f.getCode() == code){
                return f.name;
            }
        }
        return UNKNOWN.name;
    }

    private int code;

    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
