package thedrake.ui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import thedrake.*;

public class StageConfig implements HViewContext {

    private Stage stage;
    private StackPane stackScene;

    public Stage start(Stage primaryStage) throws Exception {
//        primaryStage.close();
        final Parent root = FXMLLoader.load(getClass().getResource("/thedrake/ui/drake.fxml"));
        final Scene scene = new Scene(root);
        scene.getStylesheets().add("/thedrake/ui/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Drake");
        primaryStage.setMinWidth(440);
        primaryStage.setMinHeight(350);
//        primaryStage.show();

//        DoubleBinding w = stage.heightProperty().subtract(28).divide(0.553125);        // Fixed ratio resize
//        stage.minWidthProperty().bind(w);
//        stage.maxWidthProperty().bind(w);

        return primaryStage;
    }

    public Stage game(Stage primaryStage){
        StackPane stackScene = new StackPane();
        VBox vBox = new VBox();
        vBox.prefHeight(100);                 // To keep packs next to board

        HView hView = new HView(createSampleGameState(), this);

//        hView.setSpacing(10);
        hView.setAlignment(Pos.CENTER);               // Right / Left
        vBox.getChildren().add(hView);
//        vBox.getChildren().add(label);
        vBox.setAlignment(Pos.CENTER);                // Up / Down
        stackScene.getChildren().add(vBox);
        this.stackScene = stackScene;

        final double oldWidth = primaryStage.getWidth();
        final double oldHeight = primaryStage.getHeight();
        final Scene scene = new Scene(stackScene);
        scene.getStylesheets().add("/thedrake/ui/style.css");
//        primaryStage.hide();
        primaryStage.setScene(scene);
        primaryStage.setWidth(oldWidth);
        primaryStage.setHeight(oldHeight);
//        primaryStage.show();
        primaryStage.setMinWidth(830);
        primaryStage.setMinHeight(480);

        this.stage = primaryStage;
        return primaryStage;
    }

    private static GameState createSampleGameState() {          // Example with troops on board
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board)
                .placeFromStack(positionFactory.pos(0, 0))
                .placeFromStack(positionFactory.pos(3, 3))
                .placeFromStack(positionFactory.pos(0, 1))
                .placeFromStack(positionFactory.pos(3, 2))
                .placeFromStack(positionFactory.pos(1, 0))
                .placeFromStack(positionFactory.pos(2, 3))

                .placeFromStack(positionFactory.pos(0, 2))
                .placeFromStack(positionFactory.pos(3, 1))
                .placeFromStack(positionFactory.pos(2, 0))
                .placeFromStack(positionFactory.pos(1, 3))/**/;
    }


    @Override
    public void gameOver(GameState gameState) {
        Label label = new Label();
        if(gameState.getResult() == GameResult.DRAW)
            label.setText("Draw");
        else if(gameState.sideOnTurn() == PlayingSide.BLUE) {
            label.setText("Orange won");
        } else
            label.setText("Blue won");
        label.setFont(new Font(50));
        label.setStyle("-fx-font-weight: bold");
//        label.setTextFill(Color.RED);

        Button button1 = new Button();
        button1.setText("Play again");
        button1.setOnMouseClicked(e -> game(this.stage));

        Button button2 = new Button();
        button2.setText("Main menu");
        button2.setOnMouseClicked(e -> {
            try {
                start(this.stage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(button1);
        vBox.getChildren().add(button2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.stackScene.getChildren().add(vBox);
    }

}
