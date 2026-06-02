package org.OperatingSystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Priority scheduler Premptive
public class ProcessSchedulerPP implements Scheduler, Runnable {

    private Thread schedulerThread;
    private PriorityBlockingQueue<Process> readyQueue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Process::getPriority));
    private AtomicInteger currentTime = new AtomicInteger(0);
    private volatile boolean isRunning = false;


    //concurrency methods
    public void startScheduler() {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "PP Scheduler Thread");
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
        if (process.getPriority() == null) {
            throw new ProcessException("Priority Scheduling requires a priority value.");
        }

        process.setArrivalTime(currentTime.get());
        readyQueue.add(process);
    }

    public void addProcesses(List<Process> processes) {
        for (Process process : processes) {
            addProcess(process);
        }
    }


    private void applyAging() {
        ArrayList<Process> temp = new ArrayList<>();

        readyQueue.drainTo(temp);

        for (Process process : temp) {
            if (process.getPriority() > 0) {
                process.setPriority(process.getPriority() - 1);
            }
        }

        readyQueue.addAll(temp);
    }

    private boolean higherPriorityProcessExists(Process currentProcess) {

        Process nextProcess = readyQueue.peek();

        if (nextProcess == null) {
            return false;
        }

        boolean higherPriorityFound = nextProcess.getPriority() < currentProcess.getPriority();

        return higherPriorityFound;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Process currentProcess = readyQueue.take();
                currentProcess.setState("RUNNING");
                if (currentProcess.getRemainingBurstTime() == currentProcess.getBurstTime()) {
                    currentProcess.setStartTime(currentTime.get());
                }

                int remainingTime = currentProcess.getRemainingBurstTime();

                while (remainingTime > 0 && isRunning) {
                    Thread.sleep(1000);
                    remainingTime--;
                    currentProcess.setRemainingBurstTime(remainingTime);
                    currentTime.incrementAndGet();

                    if (higherPriorityProcessExists(currentProcess)) {
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

                    // Turnaround time is simply completion time - arrival time.
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());

                    // Waiting time is simply Turnaround time - burst time.
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

                    // Response time is the time from arrival until the first CPU access.
                    currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
                    applyAging();
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
        return "PRIORITY PREMPTIVE";
    }
}
