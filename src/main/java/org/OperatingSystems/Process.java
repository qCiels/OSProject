package org.OperatingSystems;

public class Process {
    //Process identifiers
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private String state = "READY";

    //Process metrics
    private int startTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;

    //Scheduling Helpers
    private Integer priority;
    private int remainingBurstTime;
    private int cpuTimeReceived;


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

    public void setCpuTimeReceived(int cpuTimeReceived) {
        if (cpuTimeReceived < 0) {
            throw new ProcessException("CPU time received cannot be negative");
        }
        this.cpuTimeReceived = cpuTimeReceived;
    }


    //Priority 0 is the highest priority, -1 is an error value for when process lacks priority
    public void setPriority(Integer priority) {
        if (priority < -1 ) {
            throw new ProcessException("Priority cannot be negative");
        }
        this.priority = priority;
    }

    public void setState(String state) {
        this.state = state;
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


    public int getCpuTimeReceived() {
        return cpuTimeReceived;
    }

    public String getState() {
        return state;
    }


}
