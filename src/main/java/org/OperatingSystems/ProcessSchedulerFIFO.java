package org.OperatingSystems;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class ProcessSchedulerFIFO implements Scheduler {
    public void schedule(List<Process> processes) {
        //sorts processes using the built-in Comparator as we're comparing the arrival time which is an int type variable.
        //Process::getArrivalTime means that for every process we're calling getArrivalTime, then after-wards we sort in ascending order by default.
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        Queue<Process> readyQueue = new ArrayDeque<>();
        readyQueue.addAll(processes);

        int currentTime = 0;

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            currentProcess.setStartTime(currentTime);
            //Completion time in FIFO is simply startTime + burstTime, After the idle-time check; startTime = currentTime.
            currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());

            //Turnaround time is simply completion time - arrival time.
            currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());

            //Waiting time is simply startTime - arrival time.
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());


            currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());

            currentTime = currentProcess.getCompletionTime();
        }


    }
}
