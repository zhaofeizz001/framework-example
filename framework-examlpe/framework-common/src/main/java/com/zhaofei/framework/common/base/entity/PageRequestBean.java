package com.zhaofei.framework.common.base.entity;

import java.io.Serializable;

public class PageRequestBean<T> extends BaseBean implements Serializable {

    public PageRequestBean(T t) {
        this.t = t;
        if(t instanceof BaseBean){
            BaseBean baseBean = (BaseBean) t;
            super.setUsername(baseBean.getUsername());
        }
    }

    private int pageNum;
    private int pageSize;

    private T t;

    public int getPageNum() {
        return pageNum == 0 ? 1 : pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize == 0 ? 10 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
