package org.OperatingSystems;


//Factory Class
public class ScheduleFactory {

    public static Scheduler createScheduler(String algorithm) {
        switch (algorithm.toUpperCase()) {
            //IN-CLASS
            case "FIRST IN FIRST OUT": return new ProcessSchedulerFIFO();

            //IN-CLASS
            case "SHORTEST JOB FIRST": return new ProcessSchedulerSJF();

            //IN-CLASS
            case "PRIORITY NON-PREEMPTIVE": return new ProcessSchedulerPN();

            //IN-CLASS
            case "PRIORITY PREEMPTIVE": return new ProcessSchedulerPP();

            //IN-CLASS
            case "ROUND_ROBIN SCHEDULING": return new ProcessSchedulerRRS();

            //BONUS
            case "SHORTEST REMAINING TIME FIRST": return new ProcessSchedulerSRTF();

            //BONUS
            case "LONGEST REMAINING TIME FIRST": return new ProcessSchedulerLRTF();

            //BONUS
            case "GURANTEED SCHEDULING": return new ProcessSchedulerPP();

            default:
                throw new IllegalArgumentException("Unknown scheduling algorithm: " + algorithm);
        }
    }
}