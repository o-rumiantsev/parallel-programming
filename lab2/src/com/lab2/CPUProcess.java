package com.lab2;

import java.util.UUID;

public class CPUProcess {
    private final UUID id = UUID.randomUUID();
    private final long executionTime;
    private boolean isRejected = true;

    public CPUProcess(long executionTime) {
        this.executionTime = executionTime;
    }

    public UUID getId() {
        return id;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public boolean getIsRejected() {
        return isRejected;
    }

    public void accept() {
        isRejected = false;
    }
}
