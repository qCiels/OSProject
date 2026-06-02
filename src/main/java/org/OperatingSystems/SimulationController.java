package org.OperatingSystems;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class SimulationController {

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
    public void initialize() {
        addProcessButton.setOnAction(event -> handlerAddProcess());
        backButton.setOnAction(event -> handlerBackButton());
        startButton.setOnAction(event -> handlerStartButton());


        pidColumn.setCellValueFactory(new PropertyValueFactory<>("processId"));
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remainingTime"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
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


        dialog.showAndWait().ifPresent(process -> {processTable.getItems().add(process);});
    }

    private void handlerStartButton() {
        List<Process> processes = new ArrayList<>(processTable.getItems());

        if (processes.isEmpty()) {showAlert("Please add at least one process before starting.");
            return;
        }

        scheduler.schedule(processes);
        new Thread(() -> {
            try {
                Thread.sleep(5000); // temporary only
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> updateGanttChart());
        }).start();


        updateGanttChart();
    }

    public void handlerBackButton() {
        SceneManager.showHomePage();
    }


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        schedulerNameLabel.setText(scheduler.getSchedulerName());
    }

    private void updateGanttChart() {
        ganttHBox.getChildren().clear();

        for (String pid : scheduler.getTimeline()) {
            Label block = new Label(pid);
            block.setMinWidth(40);
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
}