package com.zhaofei.framework.common.base.entity;

import com.zhaofei.framework.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

public class PageResponseBean<E> implements Serializable {

    public PageResponseBean() {
    }

    public PageResponseBean(PageRequestBean pageRequestBean, long total, List<E> result) {
        this.pageNum = pageRequestBean.getPageNum();
        this.pageSize = pageRequestBean.getPageNum();
        this.total = total;
        this.result = result;
        this.clazz = result.get(0).getClass().getName();
    }

    private String clazz;

    public int size;
    private int pageNum;
    private int pageSize;
    private long total;
    private List<E> result;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<E> getResult(Class<E> clazz) {
        return JsonUtils.jsonToList(JsonUtils.objtoJson(result), clazz);
    }

    public List<E> getResult() {
        try {
            return JsonUtils.jsonToList(JsonUtils.objtoJson(result), (Class<E>) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void setResult(List<E> result) {
        this.result = result;
    }

    public int getSize() {
        return result.size();
    }


}
