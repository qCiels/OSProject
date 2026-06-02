package org.OperatingSystems;


//Factory Class
public class ScheduleFactory {

    public static Scheduler createScheduler(String algorithm) {
        switch (algorithm.toUpperCase()) {
            //IN-CLASS
            case "FIFO": return new ProcessSchedulerFIFO();

            //IN-CLASS
            case "SJF": return new ProcessSchedulerSJF();

            //IN-CLASS
            case "PN": return new ProcessSchedulerPN();

            //IN-CLASS
            case "PP": return new ProcessSchedulerPP();

            //IN-CLASS
            case "RRS": return new ProcessSchedulerRRS();

            //BONUS
            case "SRTF": return new ProcessSchedulerSRTF();

            //BONUS
            case "LRTF": return new ProcessSchedulerLRTF();

            //BONUS
            case "GS": return new ProcessSchedulerGS();

            default:
                throw new IllegalArgumentException("Unknown scheduling algorithm: " + algorithm);
        }
    }
}