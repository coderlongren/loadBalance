package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;

public class UserLoadBalance implements LoadBalance {


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        WorkRequest request;
        while ((request = ProviderStatus.select()) == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Invoker<T> invoker = null;
        for (Invoker<T> tInvoker : invokers) {
            if (tInvoker.getUrl().getPort() == request.port) {
                invoker = tInvoker;
                break;
            }
        }
        return invoker;
    }
}

