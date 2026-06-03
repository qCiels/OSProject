package org.OperatingSystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//Priority scheduler Non-Premptive
public class ProcessSchedulerPN implements Scheduler, Runnable {

    //Variable Fields
    private Thread schedulerThread;
    private PriorityBlockingQueue<Process> readyQueue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Process::getPriority));
    private List<String> timeline = new ArrayList<>();
    private AtomicInteger currentTime = new AtomicInteger(0);
    private volatile boolean isRunning = false;
    private Process currentProcess;

    //concurrency methods
    public void startScheduler() {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            isRunning = true;
            schedulerThread = new Thread(this, "PN Scheduler Thread");
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

    @Override
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

                // In non-preemptive scheduling, response time equals waiting time because the process starts only once.
                currentProcess.setResponseTime(currentProcess.getWaitingTime());
                //In operating systems aging happens after the completion of a process.
                applyAging();

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
        return "PRIORITY NON-PREMPTIVE";
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
        return "PN";
    }
}
