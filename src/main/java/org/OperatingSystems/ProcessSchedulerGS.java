package org.OperatingSystems;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Guranteed Scheduling Scheduler, pre-emptive, based on CPU time received. The process with the least CPU time received gets scheduled first.
public class ProcessSchedulerGS implements Scheduler , Runnable {

    //Variable Fields
    private Thread schedulerThread;
    private PriorityBlockingQueue<Process> readyQueue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Process::getCpuTimeReceived));
    private AtomicInteger currentTime = new AtomicInteger(0);
    private volatile boolean isRunning = false;

    //concurrency methods
    public void startScheduler() {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "GS Scheduler Thread");
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

    private boolean lowerCpuTimeReceivedExists(Process currentProcess) {
        Process nextProcess = readyQueue.peek();

        if (nextProcess == null) {
            return false;
        }

        boolean lowerCpuTimeFound = nextProcess.getCpuTimeReceived() < currentProcess.getCpuTimeReceived();

        return lowerCpuTimeFound;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Process currentProcess = readyQueue.take();

                if (currentProcess.getRemainingBurstTime() == currentProcess.getBurstTime()) {
                    currentProcess.setStartTime(currentTime.get());
                }

                int remainingTime = currentProcess.getRemainingBurstTime();

                while (remainingTime > 0 && isRunning) {
                    Thread.sleep(1000);
                    remainingTime--;
                    currentProcess.setRemainingBurstTime(remainingTime);
                    currentProcess.setCpuTimeReceived(currentProcess.getCpuTimeReceived() + 1);
                    currentTime.incrementAndGet();

                    if (lowerCpuTimeReceivedExists(currentProcess)) {
                        readyQueue.add(currentProcess);
                        break;
                    }
                }

                if (!isRunning) {
                    break;
                }

                if (currentProcess.getRemainingBurstTime() == 0) {
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

}
