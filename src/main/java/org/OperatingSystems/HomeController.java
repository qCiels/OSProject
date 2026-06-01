package org.OperatingSystems;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HomeController {
    @FXML
    private ComboBox<String> schedulerComboBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label timeQuantumLabel;

    @FXML
    private TextField timeQuantumField;

    @FXML
    private Button startButton;


    @FXML
    public void initialize() {

        schedulerComboBox.getItems().addAll("FIFO","SJF","SRTF","LRTF","PP","PN","GS","RRS");

        schedulerComboBox.setOnAction(event -> handlerSchedulerSelection());

        startButton.setOnAction(event -> handlerStartButton());
    }

    public void handlerSchedulerSelection() {
        if (schedulerComboBox.getValue().equals("RRS")) {
            timeQuantumLabel.setVisible(true);
            timeQuantumField.setVisible(true);
        } else {
            timeQuantumLabel.setVisible(false);
            timeQuantumField.setVisible(false);
        }

        descriptionLabelHelper(schedulerComboBox.getValue());
    }

    public void handlerStartButton() {
        String selected = schedulerComboBox.getValue();

        if (selected == null) {
            showAlert("Please select a scheduler.");
            return;
        }

        Scheduler scheduler = ScheduleFactory.createScheduler(selected);

        if (selected.equals("RRS")) {
            try {
                int quantum = Integer.parseInt(timeQuantumField.getText());
                ( (ProcessSchedulerRRS) scheduler).setTimeQuantum(quantum);
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid time quantum.");
                return;
            }
        }

        // Later:
        // SceneManager.showSimulationPage(scheduler, selected);
    }


    public void descriptionLabelHelper(String scheduler) {

        switch (scheduler) {

            case "FIFO":
                descriptionLabel.setText(
                        "First In First Out, non-preemptive. The first process in the ready queue is executed first."
                );
                break;

            case "SJF":
                descriptionLabel.setText(
                        "Shortest Job First, non-preemptive. The process with the shortest burst time is executed first."
                );
                break;

            case "SRTF":
                descriptionLabel.setText(
                        "Shortest Remaining Time First, preemptive. The process with the shortest remaining burst time is executed first."
                );
                break;

            case "LRTF":
                descriptionLabel.setText(
                        "Longest Remaining Time First, preemptive. The process with the longest remaining burst time is executed first."
                );
                break;

            case "PP":
                descriptionLabel.setText(
                        "Priority Scheduling. Processes are executed according to their priority level, with higher priority processes receiving CPU access first."
                );
                break;
            case "PN":
                descriptionLabel.setText(
                        "Priority Scheduling , non-preemptive. The process with the lowest priority that is ready to run is executed first."
                );

            case "Round Robin":
                descriptionLabel.setText(
                        "Round Robin, preemptive. Each process receives a fixed time quantum before the CPU is assigned to the next process."
                );
                break;

            case "Guaranteed Scheduling":
                descriptionLabel.setText(
                        "Guaranteed Scheduling, preemptive. Processes that have received less CPU time are given higher priority to ensure fair CPU allocation."
                );
                break;

            default:
                descriptionLabel.setText(
                        "Select a scheduler to view its description."
                );
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


