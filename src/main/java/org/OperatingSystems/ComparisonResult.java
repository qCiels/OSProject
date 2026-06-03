package org.OperatingSystems;

public class ComparisonResult {

    private String algorithmName;
    private double averageWaitingTime;
    private double averageTurnaroundTime;
    private double averageResponseTime;
    private int completionTime;

    public ComparisonResult(String algorithmName, double averageWaitingTime, double averageTurnaroundTime, double averageResponseTime, int completionTime) {
        this.algorithmName = algorithmName;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageResponseTime = averageResponseTime;
        this.completionTime = completionTime;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }
}