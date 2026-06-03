package org.OperatingSystems;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BatchSchedulerRunner {

    public static void runFIFO(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            process.setStartTime(currentTime);
            process.setResponseTime(process.getStartTime() - process.getArrivalTime());

            currentTime += process.getBurstTime();

            process.setRemainingBurstTime(0);
            process.setCompletionTime(currentTime);
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            process.setState("COMPLETED");
        }
    }

    public static void runSJF(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process shortest = null;

            for (Process process : processes) {
                if (!"COMPLETED".equals(process.getState())
                        && process.getArrivalTime() <= currentTime) {

                    if (shortest == null
                            || process.getBurstTime() < shortest.getBurstTime()) {
                        shortest = process;
                    }
                }
            }

            if (shortest == null) {
                currentTime++;
                continue;
            }

            shortest.setStartTime(currentTime);
            shortest.setResponseTime(shortest.getStartTime() - shortest.getArrivalTime());

            currentTime += shortest.getBurstTime();

            shortest.setRemainingBurstTime(0);
            shortest.setCompletionTime(currentTime);
            shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
            shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBurstTime());
            shortest.setState("COMPLETED");

            completed++;
        }
    }

    public static void runPN(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process highestPriority = null;

            for (Process process : processes) {
                if (!"COMPLETED".equals(process.getState())
                        && process.getArrivalTime() <= currentTime) {

                    if (highestPriority == null
                            || process.getPriority() < highestPriority.getPriority()) {
                        highestPriority = process;
                    }
                }
            }

            if (highestPriority == null) {
                currentTime++;
                continue;
            }

            highestPriority.setStartTime(currentTime);
            highestPriority.setResponseTime(highestPriority.getStartTime() - highestPriority.getArrivalTime());

            currentTime += highestPriority.getBurstTime();

            highestPriority.setRemainingBurstTime(0);
            highestPriority.setCompletionTime(currentTime);
            highestPriority.setTurnaroundTime(highestPriority.getCompletionTime() - highestPriority.getArrivalTime());
            highestPriority.setWaitingTime(highestPriority.getTurnaroundTime() - highestPriority.getBurstTime());
            highestPriority.setState("COMPLETED");

            completed++;
        }
    }
    public static void runPP(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process selected = null;

            for (Process p : processes) {
                if (!"COMPLETED".equals(p.getState()) && p.getArrivalTime() <= currentTime) {
                    if (selected == null || p.getPriority() < selected.getPriority()) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                currentTime++;
                continue;
            }

            if (selected.getRemainingBurstTime() == selected.getBurstTime()) {
                selected.setStartTime(currentTime);
                selected.setResponseTime(selected.getStartTime() - selected.getArrivalTime());
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            currentTime++;

            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(selected.getCompletionTime() - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());
                selected.setState("COMPLETED");
                completed++;
            }
        }
    }
    public static void runSRTF(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process selected = null;

            for (Process p : processes) {
                if (!"COMPLETED".equals(p.getState()) && p.getArrivalTime() <= currentTime) {
                    if (selected == null || p.getRemainingBurstTime() < selected.getRemainingBurstTime()) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                currentTime++;
                continue;
            }

            if (selected.getRemainingBurstTime() == selected.getBurstTime()) {
                selected.setStartTime(currentTime);
                selected.setResponseTime(selected.getStartTime() - selected.getArrivalTime());
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            currentTime++;

            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(selected.getCompletionTime() - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());
                selected.setState("COMPLETED");
                completed++;
            }
        }
    }
    public static void runLRTF(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process selected = null;

            for (Process p : processes) {
                if (!"COMPLETED".equals(p.getState()) && p.getArrivalTime() <= currentTime) {
                    if (selected == null || p.getRemainingBurstTime() > selected.getRemainingBurstTime()) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                currentTime++;
                continue;
            }

            if (selected.getRemainingBurstTime() == selected.getBurstTime()) {
                selected.setStartTime(currentTime);
                selected.setResponseTime(selected.getStartTime() - selected.getArrivalTime());
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            currentTime++;

            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(selected.getCompletionTime() - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());
                selected.setState("COMPLETED");
                completed++;
            }
        }
    }
    public static void runRRS(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !"COMPLETED".equals(p.getState()) && !queue.contains(p)) {
                    queue.add(p);
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = queue.poll();

            if (current.getRemainingBurstTime() == current.getBurstTime()) {
                current.setStartTime(currentTime);
                current.setResponseTime(current.getStartTime() - current.getArrivalTime());
            }

            int timeUsed = 0;

            while (timeUsed < quantum && current.getRemainingBurstTime() > 0) {
                current.setRemainingBurstTime(current.getRemainingBurstTime() - 1);
                currentTime++;
                timeUsed++;

                for (Process p : processes) {
                    if (p.getArrivalTime() <= currentTime && !"COMPLETED".equals(p.getState())
                            && p != current && !queue.contains(p)) {
                        queue.add(p);
                    }
                }
            }

            if (current.getRemainingBurstTime() == 0) {
                current.setCompletionTime(currentTime);
                current.setTurnaroundTime(current.getCompletionTime() - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
                current.setState("COMPLETED");
                completed++;
            } else {
                queue.add(current);
            }
        }
    }
    public static void runGS(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process selected = null;

            for (Process p : processes) {
                if (!"COMPLETED".equals(p.getState()) && p.getArrivalTime() <= currentTime) {
                    if (selected == null || p.getCpuTimeReceived() < selected.getCpuTimeReceived()) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                currentTime++;
                continue;
            }

            if (selected.getRemainingBurstTime() == selected.getBurstTime()) {
                selected.setStartTime(currentTime);
                selected.setResponseTime(selected.getStartTime() - selected.getArrivalTime());
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            selected.setCpuTimeReceived(selected.getCpuTimeReceived() + 1);
            currentTime++;

            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(selected.getCompletionTime() - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());
                selected.setState("COMPLETED");
                completed++;
            }
        }
    }

}