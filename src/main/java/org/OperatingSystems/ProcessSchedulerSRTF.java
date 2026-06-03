package org.OperatingSystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Shortest Remaining Time First Scheduler
public class ProcessSchedulerSRTF implements Scheduler, Runnable {

    private Thread schedulerThread;

    private PriorityBlockingQueue<Process> readyQueue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Process::getRemainingBurstTime));
    private List<String> timeline = new ArrayList<>();
    private AtomicInteger currentTime = new AtomicInteger(0);
    private volatile boolean isRunning = false;
    private Process currentProcess;

    public void startScheduler() {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "SRTF Scheduler Thread");
            schedulerThread.start();
        }
    }

    public void stopScheduler() {
        isRunning = false;

        if (schedulerThread != null) {
            schedulerThread.interrupt();
        }
    }

    public void addProcess(Process process) {
        process.setArrivalTime(currentTime.get());
        readyQueue.add(process);
    }

    public void addProcesses(List<Process> processes) {
        for (Process process : processes) {
            addProcess(process);
        }
    }

    private boolean shorterRemainingTimeExists(Process currentProcess) {
        Process nextProcess = readyQueue.peek();

        if (nextProcess == null) {
            return false;
        }

        boolean shorterProcessFound = nextProcess.getRemainingBurstTime() < currentProcess.getRemainingBurstTime();

        return shorterProcessFound;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                currentProcess = readyQueue.take();
                currentProcess.setState("RUNNING");

                if (currentProcess.getRemainingBurstTime() == currentProcess.getBurstTime()) {
                    currentProcess.setStartTime(currentTime.get());
                }

                int remainingTime = currentProcess.getRemainingBurstTime();

                while (remainingTime > 0 && isRunning) {
                    Thread.sleep(1000);timeline.add("P" + currentProcess.getProcessId());
                    remainingTime--;
                    currentProcess.setRemainingBurstTime(remainingTime);
                    currentTime.incrementAndGet();

                    if (shorterRemainingTimeExists(currentProcess)) {
                        currentProcess.setState("READY");
                        readyQueue.add(currentProcess);
                        break;
                    }
                }

                if (!isRunning) {
                    break;
                }

                if (currentProcess.getRemainingBurstTime() == 0) {
                    currentProcess.setState("COMPLETED");
                    currentProcess.setCompletionTime(currentTime.get());

                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());

                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

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
    public String getSchedulerName() {
        return "SHORTEST REMAINING TIME FIRST";
    }
    public List<String> getTimeline() {
        return timeline;
    }
    public boolean isRunning() {
        return isRunning;
    }
    @Override
    public AtomicInteger getCurrentTime() {
        return currentTime;
    }
    @Override
    public Process getNextProcess() {
        return readyQueue.peek();
    }
    public Process getCurrentProcess() {
        return currentProcess;
    }
    @Override
    public String getReadyQueueNext() {
        if (readyQueue.isEmpty()) {
            return "Empty";
        }

        StringBuilder builder = new StringBuilder();

        for (Process process : readyQueue) {
            builder.append("P")
                    .append(process.getProcessId())
                    .append(" ");
        }

        return builder.toString();
    }
    public String getSchedulerCode() {
        return "SRTF";
    }
}