package thedrake.ui;

import javafx.scene.layout.HBox;
import thedrake.*;

public class HView extends HBox implements HViewSelected {               // This class enables communication between packages and board

    private CapturedView orangeByCaptured;
    private CapturedView blueByCaptured;
    private PackageView orangePack;
    private PackageView bluePack;
    private BoardView boardView;
    private GameStatus gameStatus;

    public HView(GameState gameState, GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.boardView = new BoardView(gameState, this);
        this.orangePack = new PackageView(boardView, PlayingSide.ORANGE, this);
        this.bluePack = new PackageView(boardView, PlayingSide.BLUE, this);
        this.blueByCaptured = new CapturedView(boardView, PlayingSide.ORANGE);
        this.orangeByCaptured = new CapturedView(boardView, PlayingSide.BLUE);

        this.orangeByCaptured.setMinWidth(100);
        this.blueByCaptured.setMinWidth(100);

        this.getChildren().add(0, orangeByCaptured);
        this.getChildren().add(1, orangePack);
        this.getChildren().add(2, boardView);
        this.getChildren().add(3, bluePack);
        this.getChildren().add(4, blueByCaptured);
    }

    @Override
    public void cardPackageViewSelected(PackageView packageView) {
        boardView.unselect();
        if(packageView.getPlayingSide() == PlayingSide.ORANGE)
            bluePack.unselect();
        else
            orangePack.unselect();
    }

    @Override
    public void cardPackageViewSelected(BoardView boardView) {
        bluePack.unselect();
        orangePack.unselect();
    }

    @Override
    public void showMovesFromStack(PackageView packageView) {                           // Show possible moves on Board from Troop in package
        if(packageView.getPlayingSide() == boardView.getGameState().sideOnTurn())
            boardView.showMovesFromStack();
        else
            boardView.clearMoves();
    }

    @Override
    public void newPackage(BoardView boardView, PlayingSide playingSide) {
        if(playingSide == PlayingSide.BLUE)
            this.bluePack = new PackageView(boardView, PlayingSide.BLUE, this);
        else
            this.orangePack = new PackageView(boardView, PlayingSide.ORANGE, this);
        update();
    }

    @Override
    public void newCapture(BoardView boardView, PlayingSide playingSide) {
        if(playingSide == PlayingSide.BLUE)
            this.orangeByCaptured = new CapturedView(boardView, PlayingSide.ORANGE);
        else
            this.blueByCaptured = new CapturedView(boardView, PlayingSide.BLUE);
        update();
    }

    @Override
    public void gameOver(GameState gameState) {
        gameStatus.gameOver(gameState);
    }

    private void update() {
        this.getChildren().clear();
        this.getChildren().add(0, this.orangeByCaptured);
        this.getChildren().add(1, this.orangePack);
        this.getChildren().add(2, this.boardView);
        this.getChildren().add(3, this.bluePack);
        this.getChildren().add(4, this.blueByCaptured);
    }

}
