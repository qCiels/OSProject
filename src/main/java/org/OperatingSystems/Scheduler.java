package org.OperatingSystems;

import java.util.List;

public interface Scheduler {
    void schedule(List<Process> processes);
    String getSchedulerName();
    List<String> getTimeline();
}
