package com.zhaofei.framework.admin.controller.dict;

import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.zhaofei.framework.common.constant.DictRedisKey.DICT_TYPE;

@RequestMapping("/dict")
@RestController
public class DictController extends AbstractController {

    @PostMapping("/getDict/{biz}")
    public RestResult<Map<String, Object>> getDict(@PathVariable(name = "biz") String biz){

        HashMap map = RedisUtils.getMap(DICT_TYPE.getKey(biz), HashMap.class);

        return this.genSuccessResult(map);
    }
}
