package constant;

public enum CommonRedisKey {
    COMMON_METHOD_CACHE("common:method:cache:%s", 1000 * 60 * 60 * 60 * 24 * 30L, "方法级缓存");

    CommonRedisKey(String key, Long expTime, String desc) {
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
