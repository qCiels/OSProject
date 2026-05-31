package org.OperatingSystems;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class ProcessSchedulerFIFO implements Scheduler {

    private Queue<Process> readyQueue = new ArrayDeque<>();
    private int currentTime = 0;

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public void addProcesses(List<Process> processes) {
        // Sorts processes using the built-in Comparator, as we are comparing the arrival time, which is an int variable.
        // Process::getArrivalTime means that for every process, we call getArrivalTime(), then sort in ascending order by default.
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        readyQueue.addAll(processes);
    }

    public void run() {
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            // Idle-time check: if the next process has not arrived yet,
            // advance the current time to its arrival time.
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            currentProcess.setStartTime(currentTime);

            // Completion time in FIFO is simply startTime + burstTime.
            // After the idle-time check, startTime = currentTime.
            currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());

            // Turnaround time is simply completion time - arrival time.
            currentProcess.setTurnaroundTime(
                    currentProcess.getCompletionTime() - currentProcess.getArrivalTime()
            );

            // Waiting time is simply startTime - arrival time.
            currentProcess.setWaitingTime(
                    currentProcess.getStartTime() - currentProcess.getArrivalTime()
            );

            // In FIFO, response time equals waiting time because the process starts only once.
            currentProcess.setResponseTime(currentProcess.getWaitingTime());

            currentTime = currentProcess.getCompletionTime();
        }
    }

    @Override
    public void schedule(List<Process> processes) {
        addProcesses(processes);
        run();
    }
}