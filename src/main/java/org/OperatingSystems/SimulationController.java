package org.OperatingSystems;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.util.Duration.ZERO;

public class SimulationController {

    private Timeline guiUpdater;
    private Scheduler scheduler;
    // Header
    @FXML
    private Label schedulerNameLabel;

    // Process Management
    @FXML
    private Button addProcessButton;

    @FXML
    private Button addProcessesButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button backButton;

    // Process Table
    @FXML
    private TableView<Process> processTable;

    @FXML
    private TableColumn<Process, String> pidColumn;

    @FXML
    private TableColumn<Process, Integer> arrivalColumn;

    @FXML
    private TableColumn<Process, Integer> burstColumn;

    @FXML
    private TableColumn<Process, Integer> remainingColumn;

    @FXML
    private TableColumn<Process, Integer> priorityColumn;

    @FXML
    private TableColumn<Process, String> stateColumn;

    // Simulation Section
    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label schedulerStatusLabel;

    @FXML
    private Label runningProcessLabel;

    @FXML
    private Label readyQueueLabel;

    @FXML
    private Label nextProcessLabel;

    @FXML
    private Label completedProcessesLabel;

    // Gantt Chart
    @FXML
    private HBox ganttHBox;

    // Completed Processes Table
    @FXML
    private TableView<Process> completedProcessesTable;

    @FXML
    private TableColumn<Process, String> completedPidColumn;

    @FXML
    private TableColumn<Process, Integer> waitingTimeColumn;

    @FXML
    private TableColumn<Process, Integer> turnaroundTimeColumn;

    @FXML
    private TableColumn<Process, Integer> responseTimeColumn;

    @FXML
    private TableColumn<Process, Integer> completionTimeColumn;

    @FXML
    private Button calculateButton;
    @FXML
    private Label completedInfoLabel;

    @FXML
    public void initialize() {
        System.out.println(completedInfoLabel);
        addProcessButton.setOnAction(event -> handlerAddProcess());
        backButton.setOnAction(event -> handlerBackButton());
        startButton.setOnAction(event -> handlerStartButton());
        stopButton.setOnAction(event -> handlerStopButton());
        addProcessesButton.setOnAction(event -> handlerAddProcesses());


        pidColumn.setCellValueFactory(new PropertyValueFactory<>("processId"));
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remainingBurstTime"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        waitingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        calculateButton.setOnAction(event -> handlerStatisticsButton());

        completedPidColumn.setCellValueFactory(new PropertyValueFactory<>("processId"));
        waitingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        turnaroundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        responseTimeColumn.setCellValueFactory(new PropertyValueFactory<>("responseTime"));
        completionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("completionTime"));



        Tooltip tooltip = new Tooltip("This table displays completed processes and their final metrics.");

        tooltip.setShowDelay(Duration.ZERO);
        completedInfoLabel.setTooltip(tooltip);

    }

    private void handlerAddProcess() {

        Dialog<Process> dialog = new Dialog<>();
        dialog.setTitle("Add Process");
        dialog.setHeaderText("Enter process details");

        TextField pidField = new TextField();
        pidField.setPromptText("Process ID");

        TextField burstField = new TextField();
        burstField.setPromptText("Burst Time");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("PID:"), 0, 0);
        grid.add(pidField, 1, 0);

        grid.add(new Label("Burst Time:"), 0, 1);
        grid.add(burstField, 1, 1);

        if (scheduler instanceof ProcessSchedulerPP || scheduler instanceof ProcessSchedulerPN) {
            grid.add(new Label("Priority:"), 0, 2);
            grid.add(priorityField, 1, 2);
        }

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == addButtonType) {


                if (pidField.getText().isEmpty() || burstField.getText().trim().isEmpty() ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter all fields.");
                    alert.showAndWait();
                }

                if ( (scheduler instanceof ProcessSchedulerPP || scheduler instanceof ProcessSchedulerPN ) && priorityField.getText().trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter priority.");
                    alert.showAndWait();
                }
                int pid = Integer.parseInt(pidField.getText());
                if (processIDExists(pid)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Process ID already exists.");
                    alert.showAndWait();

                    return null;

                }
                Process process = new Process();

                process.setProcessId(Integer.parseInt(pidField.getText()));
                process.setBurstTime(Integer.parseInt(burstField.getText()));

                if (!priorityField.getText().isEmpty()) {
                    process.setPriority(Integer.parseInt(priorityField.getText()));
                } else {
                    process.setPriority(-1);
                }

                return process;
            }

            return null;
        });


        dialog.showAndWait().ifPresent(process -> {processTable.getItems().add(process);

            if (scheduler.isRunning()) {
                scheduler.addProcess(process);
            }
        });


    }

    private void handlerStartButton() {
        List<Process> processes = new ArrayList<>(processTable.getItems());

        if (processes.isEmpty()) {showAlert("Please add at least one process before starting.");
            return;
        }


        scheduler.schedule(processes);
        startGuiUpdater();
        updateGanttChart();
    }

    private void handlerStopButton() {
        if (scheduler != null) {
            scheduler.stopScheduler();
        }
        if (guiUpdater != null) {
            guiUpdater.stop();
        }
        schedulerStatusLabel.setText("Scheduler State: Stopped");
        startButton.setDisable(false);
    }

    public void handlerBackButton() {
        SceneManager.showHomePage();
    }
    private void handlerAddProcesses() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Processes");
        dialog.setHeaderText("Choose how to add multiple processes");

        TextField countField = new TextField();
        countField.setPromptText("Number of processes");

        Button randomButton = new Button("Generate Random");
        Button sampleButton = new Button("Load Sample Processes");

        VBox box = new VBox(10, new Label("Number of Processes:"), countField, randomButton, sampleButton);
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        randomButton.setOnAction(e -> {
            if (countField.getText().trim().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the number of processes.");
                alert.showAndWait();

                return;
            }

            try {

                int count = Integer.parseInt(countField.getText());
                generateRandomProcesses(count);
                dialog.close();

            }
            catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number.");
                alert.showAndWait();
            }
        });

        sampleButton.setOnAction(e -> {
            loadSampleProcesses();
            dialog.close();
        });

        dialog.showAndWait();
    }

    private void handlerStatisticsButton() {

        double avgWaiting = 0;
        double avgTurnaround = 0;
        double avgResponse = 0;

        int count = completedProcessesTable.getItems().size();

        if (count == 0) {
            showAlert("No completed processes.");
            return;
        }

        for (Process process : completedProcessesTable.getItems()) {
            avgWaiting += process.getWaitingTime();
            avgTurnaround += process.getTurnaroundTime();
            avgResponse += process.getResponseTime();
        }

        avgWaiting /= count;
        avgTurnaround /= count;
        avgResponse /= count;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Simulation Statistics");
        alert.setHeaderText("Average Metrics");

        alert.setContentText(
                "Average Waiting Time: " + avgWaiting +
                        "\nAverage Turnaround Time: " + avgTurnaround +
                        "\nAverage Response Time: " + avgResponse
        );

        alert.showAndWait();
    }


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        schedulerNameLabel.setText(scheduler.getSchedulerName());
    }

    private void updateGanttChart() {
        ganttHBox.getChildren().clear();

        List<String> timeline = scheduler.getTimeline();

        for (int i = 0; i < timeline.size(); i++) {
            Label block = new Label(i + "\n" + timeline.get(i));
            block.setMinWidth(45);
            block.setMinHeight(45);
            block.setStyle("-fx-border-color: black; -fx-alignment: center;");
            ganttHBox.getChildren().add(block);
        }
    }
    private boolean processIDExists(int processID) {

        for (Process process : processTable.getItems()) {

            if (process.getProcessId() == processID) {
                return true;
            }
        }

        return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void startGuiUpdater() {

        guiUpdater = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {

                    updateGanttChart();
                    processTable.refresh();
                    updateCompletedProcessesTable();
                    updateSimulationLabels();

                    if (!scheduler.isRunning()) {
                        updateGanttChart(); // final refresh
                        guiUpdater.stop();
                    }
                })
        );

        guiUpdater.setCycleCount(Timeline.INDEFINITE);
        guiUpdater.play();
    }

    private void updateSimulationLabels() {
        currentTimeLabel.setText("Current Time: " + scheduler.getCurrentTime());
        schedulerStatusLabel.setText("Scheduler State: " + (scheduler.isRunning() ? "Running" : "Finished"));
        Process currentProcess = scheduler.getCurrentProcess();

        if (currentProcess == null) {
            runningProcessLabel.setText("Running Process: None");
        } else {
            runningProcessLabel.setText("Running Process: " + currentProcess.getProcessId());
        }

        Process next = scheduler.getNextProcess();
        if (next == null) {
            nextProcessLabel.setText("Next Process: None");
        } else {
            nextProcessLabel.setText("Next Process: " + next.getProcessId());
        }

        readyQueueLabel.setText("Ready Queue:" + scheduler.getReadyQueueNext());

        completedProcessesLabel.setText("Completed Processes: " + countCompletedProcesses());
    }

    private int countCompletedProcesses() {

        int count = 0;

        for (Process process : processTable.getItems()) {
            if (process.getState().equals("COMPLETED")) {
                count++;
            }
        }

        return count;
    }

    private void updateCompletedProcessesTable() {
        completedProcessesTable.getItems().clear();

        for (Process process : processTable.getItems()) {
            if ("COMPLETED".equals(process.getState())) {
                completedProcessesTable.getItems().add(process);
            }
        }
    }
    private void loadSampleProcesses() {
        addProcessToTable(1, 0, 3, 2);
        addProcessToTable(2, 2, 5, 1);
        addProcessToTable(3, 4, 4, 3);
        addProcessToTable(4, 6, 2, 0);
    }
    private void addProcessToTable(int pid, int arrival, int burst, int priority) {
        Process process = new Process();
        process.setProcessId(pid);
        process.setArrivalTime(arrival);
        process.setBurstTime(burst);
        process.setPriority(priority);

        processTable.getItems().add(process);
    }
    private void generateRandomProcesses(int count) {

        Random random = new Random();

        int nextPID = getNextAvailablePID();

        for (int i = 0; i < count; i++) {

            Process process = new Process();

            process.setProcessId(nextPID++);

            process.setArrivalTime(random.nextInt(10)); // 0-9

            process.setBurstTime(random.nextInt(10) + 1); // 1-10

            if (scheduler instanceof ProcessSchedulerPP
                    || scheduler instanceof ProcessSchedulerPN) {

                process.setPriority(random.nextInt(5)); // 0-4
            } else {

                process.setPriority(-1);
            }

            processTable.getItems().add(process);
        }
    }

    private int getNextAvailablePID() {

        int maxPID = 0;

        for (Process process : processTable.getItems()) {
            maxPID = Math.max(maxPID, process.getProcessId());
        }

        return maxPID + 1;
    }


}