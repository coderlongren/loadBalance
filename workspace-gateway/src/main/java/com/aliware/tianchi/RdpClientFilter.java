package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = Constants.CONSUMER)
public class RdpClientFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            long start = System.currentTimeMillis();
            AsyncRpcResult result = (AsyncRpcResult) invoker.invoke(invocation);
            result.getResultFuture().thenAccept(result1 -> {
                int port = invoker.getUrl().getPort();
                if(result1.getException() == null) {
                    ProviderStatus.recordLatency(port, System.currentTimeMillis() - start);
                } else {
                    ProviderStatus.recordLatency(port, -1);
                }
            });
            return result;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
