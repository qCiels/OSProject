package org.OperatingSystems;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage Primarystage) {
       stage = Primarystage;
       stage.setTitle("OS Project - Omar Al-Shalabi, Karem Al-Halawani, Mustabir Islam");
       stage.getIcons().add(new javafx.scene.image.Image("/images/Abu_Dhabi_University.png"));
    }

    public static Stage getStage() {
        return stage;
    }
    public static void showHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/home.fxml"));

            Scene scene = new Scene(loader.load());
            stage.setWidth(600);
            stage.setHeight(450);
            stage.centerOnScreen();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showComparisonPage() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/comparison.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setWidth(1400);
            stage.setHeight(980);
            stage.centerOnScreen();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showSimulationPage(Scheduler scheduler) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/simulation.fxml"));
            Scene scene = new Scene(loader.load());
            SimulationController controller = loader.getController();
            controller.setScheduler(scheduler);
            stage.setWidth(1400);
            stage.setHeight(950);
            stage.centerOnScreen();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
