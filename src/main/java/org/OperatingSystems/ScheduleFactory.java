package org.OperatingSystems;

public class ScheduleFactory {

    public static Scheduler createScheduler(String algorithm) {
        switch (algorithm.toUpperCase()) {
            case "FIFO":
                return new ProcessSchedulerFIFO();

            /*case "SJF":
                return new ProcessSchedulerSJF();

            case "PRIORITY":
                return new ProcessSchedulerPriority();

            /*case "ROUND_ROBIN":
                return new ProcessSchedulerRoundRobin();*/

            default:
                throw new IllegalArgumentException("Unknown scheduling algorithm: " + algorithm);
        }
    }
}