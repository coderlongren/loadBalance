package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

public class RdpCallbackListener implements CallbackListener, Serializable {

    private String parseData(String statusMessage, String mark) {
        int maxIndex = statusMessage.indexOf(mark);
        int commaIndex = statusMessage.indexOf(",", maxIndex);
        return statusMessage.substring(maxIndex + mark.length() + 1, commaIndex);
    }


    @Override
    public void receiveServerMsg(String msg) {
        System.out.println("received message from server: " + msg);
        if (msg.startsWith("CheckStatus")) {
            int port = Integer.parseInt(parseData(msg, "Port"));
            int maxThreads = Integer.parseInt(parseData(msg, "Threads"));
            ProviderStatus provider = new ProviderStatus(port, maxThreads);
            ProviderStatus.providerMap.putIfAbsent(port, provider);
            provider.initialRequests();
        }
    }
}
