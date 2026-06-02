package org.OperatingSystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Longest Remaining Time First Scheduler, pre-emptive.
public class ProcessSchedulerLRTF implements Scheduler , Runnable {

        private Thread schedulerThread;
        private PriorityBlockingQueue<Process> readyQueue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Process::getRemainingBurstTime).reversed());
        private List<String> timeline = new ArrayList<>();
        private AtomicInteger currentTime = new AtomicInteger(0);
        private volatile boolean isRunning = false;

        public void startScheduler() {
            if (schedulerThread == null || !schedulerThread.isAlive()) {
                isRunning = true;
                schedulerThread = new Thread(this, "LRTF Scheduler Thread");
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

        private boolean longerRemainingTimeExists(Process currentProcess) {
            Process nextProcess = readyQueue.peek();

            if (nextProcess == null) {
                return false;
            }

            boolean longerProcessFound = nextProcess.getRemainingBurstTime() > currentProcess.getRemainingBurstTime();

            return longerProcessFound;
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
                        timeline.add("P" + currentProcess.getProcessId());
                        remainingTime--;
                        currentProcess.setRemainingBurstTime(remainingTime);
                        currentTime.incrementAndGet();

                        if (longerRemainingTimeExists(currentProcess)) {
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
            return "LONGEST REMAINING TIME FIRST";
        }

        public List<String> getTimeline() {
            return timeline;
        }
    }


