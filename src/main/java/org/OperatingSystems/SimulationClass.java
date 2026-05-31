package org.OperatingSystems;

import java.util.ArrayList;
import java.util.List;

public class SimulationClass {

    public static void simulate(String algorithm) {
        List<Process> processes = new ArrayList<>();
        Builder builder = new Builder();


        processes.add(builder.processId(1).arrivalTime(0).burstTime(5).build());
        processes.add(builder.processId(2).arrivalTime(2).burstTime(3).build());
        processes.add(builder.processId(3).arrivalTime(4).burstTime(2).build());
        processes.add(builder.processId(4).arrivalTime(6).burstTime(4).build());

        Scheduler scheduler = new ScheduleFactory().createScheduler(algorithm);
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