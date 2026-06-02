package org.OperatingSystems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Round Robin Scheduling
public class ProcessSchedulerRRS implements Scheduler , Runnable {

    private volatile boolean isRunning = false;
    private BlockingQueue<Process> readyQueue = new LinkedBlockingQueue<>();
    private AtomicInteger currentTime = new AtomicInteger(0);
    private List<String> timeline = new ArrayList<>();
    private Thread schedulerThread;
    private int timeQuantum;


    //concurrency methods
    public void startScheduler() {
        if (timeQuantum <= 0) {
            throw new ProcessException("Time quantum must be set before starting Round Robin scheduler.");

        }

        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "RRS Scheduler Thread");
            schedulerThread.start();
        }
    }

    public void stopScheduler() {
        isRunning = false;
        if (schedulerThread != null) {
            schedulerThread.interrupt();
        }
    }

    //Process Handling Methods
    public void addProcess(Process process) {
        process.setArrivalTime(currentTime.get());
        readyQueue.add(process);
    }

    public void addProcesses(List<Process> processes) {
        for (Process process : processes) {
            addProcess(process);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Process currentProcess = readyQueue.take();
                currentProcess.setState("RUNNING");
                if ( currentProcess.getRemainingBurstTime() == currentProcess.getBurstTime()) {
                    currentProcess.setStartTime(currentTime.get());
                }
                int remainingTime = currentProcess.getRemainingBurstTime();
                int timeUsed = 0;

                while ( (remainingTime > 0) && (isRunning) && (timeUsed < timeQuantum) ) {
                    Thread.sleep(1000);
                    timeline.add("P" + currentProcess.getProcessId());
                    remainingTime--;
                    timeUsed++;
                    currentProcess.setRemainingBurstTime(remainingTime);
                    currentTime.incrementAndGet();
                }

                if (!isRunning) {
                    break;
                }

                if (currentProcess.getRemainingBurstTime() > 0) {
                    currentProcess.setState("READY");
                    readyQueue.add(currentProcess);
                } else {
                    currentProcess.setState("COMPLETED");
                    currentProcess.setCompletionTime(currentTime.get());

                    // Turnaround time is simply completion time - arrival time.
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());

                    // Waiting time is simply startTime - arrival time.
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

                    // IN Round Robin Scheduling, response time equals start time minus arrival time.
                    currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
            }

        }
    }

    @Override
    public void schedule(List<Process> processes) {
        addProcesses(processes);
        startScheduler();
    }


    //Getters and Setters for Time Quantum
    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        if (timeQuantum <= 0) {
            throw new ProcessException("Time quantum must be a positive integer");
        }
        this.timeQuantum = timeQuantum;
    }
    public String getSchedulerName() {
        return "Round Robin Scheduling";
    }

    public List<String> getTimeline() {
        return timeline;
    }
}
