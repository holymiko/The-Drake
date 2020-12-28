package thedrake.ui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import thedrake.*;

import static java.lang.String.valueOf;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;                                            // Stav hry s Tiles

    private ValidMoves validMoves;

    private TileView selected;

    private final PackageViewContext packageViewContext;                    // The Parent HBox

    public BoardView(GameState gameState, PackageViewContext packageViewContext) {
        this.gameState = gameState;
        this.packageViewContext = packageViewContext;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();          // Ziskani pozic
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void tileViewSelected(TileView tileView) {                   // Change of selected Tile
        if (selected != null && selected != tileView)
            selected.unselect();

        selected = tileView;
        packageViewContext.cardPackageViewSelected(this);

        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void executeMove(Move move) {
        if(selected != null)
            selected.unselect();
        selected = null;
        clearMoves();

        Tile tile = gameState.tileAt(move.target());
        boolean willCapture = tile.hasTroop();

        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();

        if(willCapture)                                                             // Message to HBox to update Captured
            packageViewContext.newCapture(this, gameState.sideOnTurn());
        if(gameState.getResult() != GameResult.IN_PLAY)                             // Message to StackPane in StageConfig
            packageViewContext.gameOver(gameState);
        if(gameState.sideOnTurn() == PlayingSide.ORANGE)                            // Message to HBox to update Package
            packageViewContext.newPackage(this, PlayingSide.BLUE);
        else
            packageViewContext.newPackage(this, PlayingSide.ORANGE);
    }

    private void updateTiles() {                     // Prekresleni
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }

    public void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }

    public void unselect(){
        if( selected != null )
            selected.unselect();
    }

    public void showMovesFromStack() {
        clearMoves();
        showMoves(validMoves.movesFromStack());
    }

}
