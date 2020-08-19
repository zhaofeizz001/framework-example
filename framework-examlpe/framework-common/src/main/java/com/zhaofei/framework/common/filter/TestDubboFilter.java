package com.zhaofei.framework.common.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

@Activate(group = {CONSUMER}, order = -20000)
public class TestDubboFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("开始调用 MyFilter");
        Result result = invoker.invoke(invocation);
        System.out.println("结束调用 MyFilter");
        return result;
    }
}
