package com.lab2;

import java.util.HashMap;

public class CPUManager {
    private final HashMap<Integer, CPU> cpus = new HashMap<Integer, CPU>();

    public void addCpu(int cpuId, CPU cpu) {
        cpus.put(cpuId, cpu);
    }

    public boolean putTask(int cpuId, CPUProcess task) {
        CPU cpu = cpus.get(cpuId);
        boolean isBusy = cpu.getLocker().isLocked();
        if (!isBusy) {
            try {
                cpu.getTaskBus().put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
