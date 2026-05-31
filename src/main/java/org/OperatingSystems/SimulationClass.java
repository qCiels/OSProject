package org.OperatingSystems;

import java.util.ArrayList;
import java.util.List;

public class SimulationClass {

    public static void simulateFifo() {
        List<Process> processes = new ArrayList<>();

        processes.add(new Process(1, 0, 5));
        processes.add(new Process(2, 2, 3));
        processes.add(new Process(3, 4, 2));
        processes.add(new Process(4, 6, 4));

        Scheduler scheduler = new ProcessSchedulerFIFO();
        scheduler.schedule(processes);

        for (Process p : processes) {
            System.out.println(
                    "\nProcess ID: " + p.getProcessId() + "\n" +
                            "Arrival Time: " + p.getArrivalTime() + "\n" +
                            "Burst Time: " + p.getBurstTime() + "\n" +
                            "Start Time: " + p.getStartTime() + "\n" +
                            "Completion Time: " + p.getCompletionTime() + "\n" +
                            "Waiting Time: " + p.getWaitingTime() + "\n" +
                            "Turnaround Time: " + p.getTurnaroundTime() + "\n" +
                            "Response Time: " + p.getResponseTime() + "\n" +
                            "------------------- NEXT PROCESS -------------------\n"
            );
        }
    }
}