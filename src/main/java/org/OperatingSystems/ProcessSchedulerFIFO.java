package org.OperatingSystems;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//First In First Out Scheduler
public class ProcessSchedulerFIFO implements Scheduler , Runnable {

    //fields
    private volatile boolean isRunning = false;
    private BlockingQueue<Process> readyQueue = new LinkedBlockingQueue<>();
    private List<String> timeline = new ArrayList<>();
    private AtomicInteger currentTime = new AtomicInteger(0);
    private Thread schedulerThread;
    private Process currentProcess;


    //concurrency methods
    public void startScheduler() {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "FIFO Scheduler Thread");
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

    public void run() {
        while (isRunning) {
            try {

                currentProcess = readyQueue.take();
                currentProcess.setState("RUNNING");
                currentProcess.setStartTime(currentTime.get());
                int remainingTime = currentProcess.getRemainingBurstTime();

                while (remainingTime > 0 && isRunning) {
                    Thread.sleep(1000);
                    timeline.add("P" + currentProcess.getProcessId());
                    remainingTime--;
                    currentProcess.setRemainingBurstTime(remainingTime);
                    currentTime.incrementAndGet();
                }

                if (!isRunning) {
                    break;
                }

                currentProcess.setState("COMPLETED");
                currentProcess.setCompletionTime(currentTime.get());

                // Turnaround time is simply completion time - arrival time.
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());

                // Waiting time is simply startTime - arrival time.
                currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());

                // In FIFO, response time equals waiting time because the process starts only once.
                currentProcess.setResponseTime(currentProcess.getWaitingTime());
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
        return "FIRST IN FIRST OUT";
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
}