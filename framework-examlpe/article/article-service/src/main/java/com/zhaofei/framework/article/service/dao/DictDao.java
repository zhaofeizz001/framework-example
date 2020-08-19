package com.zhaofei.framework.article.service.dao;


import com.zhaofei.framework.article.service.entity.DictEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictDao {

    List<DictEntity> getList();
}
