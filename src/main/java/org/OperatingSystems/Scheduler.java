package org.OperatingSystems;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Scheduler {
    void schedule(List<Process> processes);
    String getSchedulerName();
    List<String> getTimeline();

    boolean isRunning();
    AtomicInteger getCurrentTime();
    void addProcesses(List<Process> processes);
    void addProcess(Process process);
    Process getNextProcess();
    Process getCurrentProcess();
    String getReadyQueueNext();
    void stopScheduler();
    String getSchedulerCode();
}
