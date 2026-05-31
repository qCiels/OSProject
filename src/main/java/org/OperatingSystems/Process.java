package org.OperatingSystems;

public class Process {
        private int processId;
        private int arrivalTime;
        private int burstTime;
        private int startTime;
        private int completionTime;
        private int waitingTime;
        private int turnaroundTime;
        private int responseTime;
        private Integer priority;
        private int remainingBurstTime;

    // constructors, getters, setters
    public Process() {

    }
    //Setters
    public void setProcessId(int processId) {
        if (processId <= 0) {
            throw new ProcessException("Process ID cannot be negative");
        }
        this.processId = processId;
    }
    public void setArrivalTime(int arrivalTime) {
        if (arrivalTime < 0) {
            throw new ProcessException("Arrival time cannot be negative");
        }
        this.arrivalTime = arrivalTime;
    }
    public void setBurstTime(int burstTime) {
        if (burstTime <= 0) {
            throw new ProcessException("Burst time cannot be negative");
        }
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }
    public void setRemainingBurstTime(int remainingBurstTime) {
        if (remainingBurstTime < 0) {
            throw new ProcessException("Remaining burst time cannot be negative");
        }
        this.remainingBurstTime = remainingBurstTime;
    }


    //Priority 0 is the highest priority
    public void setPriority(Integer priority) {
        if (priority < 0) {
            throw new ProcessException("Priority cannot be negative");
        }
        this.priority = priority;
    }



    //Getters
    public int getProcessId() {
        return processId;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getStartTime() {
        return startTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public int getResponseTime() {
        return responseTime;
    }
    public Integer getPriority() {
        return priority;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }


}
