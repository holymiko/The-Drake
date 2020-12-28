package thedrake.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new StageConfig().start(primaryStage);

        primaryStage.show();
    }



}
