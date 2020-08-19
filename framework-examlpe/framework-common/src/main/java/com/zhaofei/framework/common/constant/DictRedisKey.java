package com.zhaofei.framework.common.constant;

public enum DictRedisKey {

    DICT_TYPE("dict:type_%s", 60L * 60 * 24 * 365, "字典表type")
    ,DICT_KEY("Key_%s", 60L * 60 * 24 * 365, "字典表key")
    ;

    DictRedisKey(String key, Long expTime, String desc) {
        this.key = key;
        this.expTime = expTime;
        this.desc = desc;
    }

    public String getKey(String... key){
        if(key == null && key.length == 0){
            return null;
        }
        return String.format(this.getKey(), key);
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
        return expTime;
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
