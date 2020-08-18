package com.zhaofei.framework.common.constant;

public enum CommonRedisKey {

    COMMON_METHOD_CACHE("common:method:cache:%s:%s", 60 * 60 * 60 * 24 * 30L, "方法级缓存")
    ,SELECT_ONE_ARTICLE_BY_ID(10L, "common:method:cache:admin:com.zhaofei.framework.article.service.service.impl.ArticleServiceImpl:selectOneById(com.zhaofei.framework.article.api.entity.ArticleData)")
//    ,X1(6000L, "test")
//    ,X2("test2")
    ;

    CommonRedisKey(String key, Long expTime, String desc) {
        this.key = key;
        this.expTime = expTime;
        this.desc = desc;
    }

    CommonRedisKey(Long expTime, String desc) {
        this.expTime = expTime;
        this.desc = desc;
    }

    CommonRedisKey(String desc) {
        this.desc = desc;
    }

    public String getKey(String... key){
        if(key == null && key.length == 0){
            return null;
        }
        return String.format(COMMON_METHOD_CACHE.getKey(), key);
    }

    public static CommonRedisKey getCommonMethodCacheByDesc(){
        return COMMON_METHOD_CACHE;
    }

    public static CommonRedisKey getCommonMethodCacheByDesc(String s){
        for (CommonRedisKey commonRedisKey: CommonRedisKey.values()) {
            if(commonRedisKey.desc.equals(s)){
                return commonRedisKey;
            }
        }
        return COMMON_METHOD_CACHE;
    }

    private String key;

    private Long expTime;

    private String desc;

    private String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public Long getExpTime() {
        return expTime == null ? COMMON_METHOD_CACHE.getExpTime() : expTime;
    }

    private void setExpTime(Long expTime) {
        this.expTime = expTime;
    }

    private String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }
}
