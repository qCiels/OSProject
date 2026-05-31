package org.OperatingSystems;


//Factory Class
public class ScheduleFactory {

    public static Scheduler createScheduler(String algorithm) {
        switch (algorithm.toUpperCase()) {
            case "FIFO":
                return new ProcessSchedulerFIFO();

            /*case "SJF":
                return new ProcessSchedulerSJF();*/

            case "PRIORITY NON-PREEMPTIVE":
                return new ProcessSchedulerPN();

            case "PRIORITY PREEMPTIVE":
                return new ProcessSchedulerPP();

            /*case "ROUND_ROBIN":
                return new ProcessSchedulerRoundRobin();*/

            case "SHORTEST REMAINING TIME FIRST":
                return new ProcessSchedulerSRTF();

            default:
                throw new IllegalArgumentException("Unknown scheduling algorithm: " + algorithm);
        }
    }
}