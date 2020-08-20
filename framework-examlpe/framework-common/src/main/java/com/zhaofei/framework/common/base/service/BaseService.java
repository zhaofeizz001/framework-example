package com.zhaofei.framework.common.base.service;

import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;

public interface BaseService<T> {

    PageResponseBean<T> selectList(PageRequestBean<T> t);

    T selectOneById(T t) throws Exception;

    int updateById(T t);
}
