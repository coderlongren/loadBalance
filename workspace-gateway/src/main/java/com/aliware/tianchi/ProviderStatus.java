package com.aliware.tianchi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ProviderStatus {

    static ConcurrentHashMap<Integer, ProviderStatus> providerMap = new ConcurrentHashMap<>();

    static ConcurrentSkipListSet<WorkRequest> requestQueue = new ConcurrentSkipListSet<>(new WorkRequest.Compartor());

    static void recordLatency(int port, long latency) {
        providerMap.get(port).recordLatency(latency);
    }

    static WorkRequest select() {
        return requestQueue.pollFirst();
    }

    final int port;
    final int maxThreads;

    void request(long latency) {
        requestQueue.add(new WorkRequest(port, latency));
    }

    public ProviderStatus(int port, int reportedMax) {
        this.port = port;
        this.maxThreads = reportedMax;
    }

    synchronized void initialRequests() {
        for (int i = 0; i < maxThreads; i++) {
            request(50);
        }
        System.out.println("complete initialization for port: " + port + ", maxthreads: " + maxThreads);
    }


    private void recordLatency(long latency) {
        if (latency != -1) {
            if (latency < 3) {
                for (int i = 0; i < 1; i++) {
                    requestQueue.pollLast();
                    requestQueue.add(new WorkRequest(port, latency));
                }
            }
            request(50);
        }
    }
}
