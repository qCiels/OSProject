package org.OperatingSystems;

import java.util.ArrayList;
import java.util.List;

//Priority scheduler Non-Premptive
public class ProcessSchedulerPN implements Scheduler {
    public ArrayList<Process> processes = new ArrayList<>();

    public void addProcess(Process process) {
        processes.add(process);
    }
    @Override
    public void schedule(List<Process> processes) {

    }

}
