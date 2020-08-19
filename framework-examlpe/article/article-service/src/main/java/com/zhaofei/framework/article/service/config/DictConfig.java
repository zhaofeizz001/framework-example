package com.zhaofei.framework.article.service.config;

import com.zhaofei.framework.article.service.dao.DictDao;
import com.zhaofei.framework.article.service.entity.DictEntity;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_KEY;
import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_TYPE;

@Component
public class DictConfig {

    @Autowired
    private DictDao dictDao;

    @Bean
    public String setDictRedis(){
        List<DictEntity> list = dictDao.getList();
        list.forEach(dictEntity -> {
            RedisUtils.addMap(
                    DICT_TYPE.getKey(dictEntity.getType().toString()),
                    DICT_KEY.getKey(dictEntity.getKey().toString()),
                    dictEntity.getValue());
        });

        return "";
    }
}
