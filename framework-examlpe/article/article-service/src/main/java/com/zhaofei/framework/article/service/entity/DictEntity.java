package com.zhaofei.framework.article.service.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DictEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 业务码
     */
    private Integer type;
    /**
     * 键
     */
    private Integer key;
    /**
     * 值
     */
    private String value;
}


