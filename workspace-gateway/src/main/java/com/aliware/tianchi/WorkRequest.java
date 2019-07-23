package com.aliware.tianchi;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class WorkRequest {
    final int port;
    final Double latency;

    public WorkRequest(int port, long latency) {
        this.port = port;
        this.latency = latency + ThreadLocalRandom.current().nextDouble();
    }

    static class Compartor implements Comparator<WorkRequest> {

        @Override
        public int compare(WorkRequest o1, WorkRequest o2) {
            return o1.latency.compareTo(o2.latency);
        }
    }

}
