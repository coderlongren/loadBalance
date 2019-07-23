package com.aliware.tianchi;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.status.StatusChecker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackServiceImpl implements CallbackService {

    static final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    static String maxThreads(String statusMessage, String mark) {
        int maxIndex = statusMessage.indexOf(mark);
        int commaIndex = statusMessage.indexOf(",", maxIndex);
        return statusMessage.substring(maxIndex + mark.length(), commaIndex);
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        StatusChecker statusChecker = ExtensionLoader.getExtensionLoader(StatusChecker.class).getExtension("threadpool");
        String poolStatus = statusChecker.check().getMessage();
        listener.receiveServerMsg(
                "CheckStatus: Host:" + RpcContext.getContext().getUrl().getHost() +
                        ", Port:" + RpcContext.getContext().getUrl().getPort() +
                        ", Threads:" + maxThreads(poolStatus, "max:") + ","
        );
    }
}

