package com.zhaofei.framework.article.api.utils;

public enum ArticleSort {
    ARTICLE(0, "文章"),
    UNKNOWN(1, "未知");

    ArticleSort(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code){
        for(ArticleSort f : ArticleSort.values()){
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
