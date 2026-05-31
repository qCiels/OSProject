package org.OperatingSystems;
public class Builder {

    private int processId;
    private int arrivalTime;
    private int burstTime;
    private Integer priority;

    public Builder processId(int processId) {
        this.processId = processId;
        return this;
    }

    public Builder arrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public Builder burstTime(int burstTime) {
        this.burstTime = burstTime;
        return this;
    }

    public Builder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public Process build() {

        Process process = new Process();

        process.setProcessId(processId);
        process.setArrivalTime(arrivalTime);
        process.setBurstTime(burstTime);

        if (priority != null ) {
            process.setPriority(priority);
        }

        return process;
    }
}