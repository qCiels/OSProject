package org.OperatingSystems;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class ComparisonController {

    private List<Process> sampleProcesses = new ArrayList<>();

    @FXML
    private Button generateSampleButton;

    @FXML
    private Button startComparisonButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ComparisonResult> comparisonTable;

    @FXML
    private TableColumn<ComparisonResult, String> algorithmColumn;

    @FXML
    private TableColumn<ComparisonResult, Double> avgWaitingColumn;

    @FXML
    private TableColumn<ComparisonResult, Double> avgTurnaroundColumn;

    @FXML
    private TableColumn<ComparisonResult, Double> avgResponseColumn;

    @FXML
    private TableColumn<ComparisonResult, Integer> completionTimeColumn;

    @FXML
    private Label bestWaitingLabel;

    @FXML
    private Label bestTurnaroundLabel;

    @FXML
    private Label bestResponseLabel;

    @FXML
    private Label bestCompletionLabel;

    @FXML
    public void initialize() {

        generateSampleButton.setOnAction(event -> handlerGenerateSample());
        startComparisonButton.setOnAction(event -> handlerStartComparison());
        backButton.setOnAction(event -> SceneManager.showHomePage());
        
        
        algorithmColumn.setCellValueFactory(new PropertyValueFactory<>("algorithmName"));
        avgWaitingColumn.setCellValueFactory(new PropertyValueFactory<>("averageWaitingTime"));
        avgTurnaroundColumn.setCellValueFactory(new PropertyValueFactory<>("averageTurnaroundTime"));
        avgResponseColumn.setCellValueFactory(new PropertyValueFactory<>("averageResponseTime"));
        completionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("completionTime"));


    }

    private void handlerGenerateSample() {
        sampleProcesses.clear();

        sampleProcesses.add(createProcess(1, 0, 5, 2));
        sampleProcesses.add(createProcess(2, 1, 3, 1));
        sampleProcesses.add(createProcess(3, 2, 8, 3));
        sampleProcesses.add(createProcess(4, 3, 6, 0));
        sampleProcesses.add(createProcess(5, 4, 4, 2));

        showAlert("Sample processes generated.");
    }

    private void handlerStartComparison() {

        if (sampleProcesses.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Sample Processes");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Please generate sample processes before starting the comparison."
            );

            alert.showAndWait();
            return;
        }

        comparisonTable.getItems().clear();


        List<Process> fifoProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runFIFO(fifoProcesses);
        comparisonTable.getItems().add(calculateResult("FIFO", fifoProcesses));

        List<Process> sjfProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runSJF(sjfProcesses);
        comparisonTable.getItems().add(calculateResult("SJF", sjfProcesses));

        List<Process> pnProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runPN(pnProcesses);
        comparisonTable.getItems().add(calculateResult("PN", pnProcesses));

        List<Process> ppProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runPP(ppProcesses);
        comparisonTable.getItems().add(calculateResult("PP", ppProcesses));

        List<Process> rrsProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runRRS(rrsProcesses, 2);
        comparisonTable.getItems().add(calculateResult("RRS", rrsProcesses));

        List<Process> srtfProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runSRTF(srtfProcesses);
        comparisonTable.getItems().add(calculateResult("SRTF", srtfProcesses));

        List<Process> lrtfProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runLRTF(lrtfProcesses);
        comparisonTable.getItems().add(calculateResult("LRTF", lrtfProcesses));

        List<Process> gsProcesses = cloneProcesses(sampleProcesses);
        BatchSchedulerRunner.runGS(gsProcesses);
        comparisonTable.getItems().add(calculateResult("GS", gsProcesses));

        updateBestLabels();

    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private Process createProcess(int pid, int arrival, int burst, int priority) {
        Process process = new Process();
        process.setProcessId(pid);
        process.setArrivalTime(arrival);
        process.setBurstTime(burst);
        process.setPriority(priority);
        return process;
    }
    private List<Process> cloneProcesses(List<Process> originalProcesses) {
        List<Process> copies = new ArrayList<>();

        for (Process original : originalProcesses) {
            Process copy = new Process();

            copy.setProcessId(original.getProcessId());
            copy.setArrivalTime(original.getArrivalTime());
            copy.setBurstTime(original.getBurstTime());
            copy.setPriority(original.getPriority());

            copies.add(copy);
        }

        return copies;
    }

    private ComparisonResult calculateResult(String algorithmName, List<Process> processes) {
        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;
        int completionTime = 0;

        for (Process process : processes) {
            totalWaiting += process.getWaitingTime();
            totalTurnaround += process.getTurnaroundTime();
            totalResponse += process.getResponseTime();

            completionTime = Math.max(completionTime, process.getCompletionTime());
        }

        int count = processes.size();

        return new ComparisonResult(
                algorithmName,
                totalWaiting / count,
                totalTurnaround / count,
                totalResponse / count,
                completionTime
        );
    }
    private void updateBestLabels() {
        if (comparisonTable.getItems().isEmpty()) {
            return;
        }

        ComparisonResult bestWaiting = comparisonTable.getItems().get(0);
        ComparisonResult bestTurnaround = comparisonTable.getItems().get(0);
        ComparisonResult bestResponse = comparisonTable.getItems().get(0);
        ComparisonResult bestCompletion = comparisonTable.getItems().get(0);

        for (ComparisonResult result : comparisonTable.getItems()) {
            if (result.getAverageWaitingTime() < bestWaiting.getAverageWaitingTime()) {
                bestWaiting = result;
            }

            if (result.getAverageTurnaroundTime() < bestTurnaround.getAverageTurnaroundTime()) {
                bestTurnaround = result;
            }

            if (result.getAverageResponseTime() < bestResponse.getAverageResponseTime()) {
                bestResponse = result;
            }

            if (result.getCompletionTime() < bestCompletion.getCompletionTime()) {
                bestCompletion = result;
            }
        }

        bestWaitingLabel.setText(String.format("BEST WAITING TIME :- %s (%.2f)", bestWaiting.getAlgorithmName(), bestWaiting.getAverageWaitingTime()));
        bestTurnaroundLabel.setText(String.format("BEST TURNAROUND TIME :- %s (%.2f)", bestTurnaround.getAlgorithmName(), bestTurnaround.getAverageTurnaroundTime()));
        bestResponseLabel.setText(String.format("BEST RESPONSE TIME :- %s (%.2f)", bestResponse.getAlgorithmName(), bestResponse.getAverageResponseTime()));
        bestCompletionLabel.setText(String.format("BEST COMPLETION TIME :- %s (%d)", bestCompletion.getAlgorithmName(), bestCompletion.getCompletionTime()));
    }

}