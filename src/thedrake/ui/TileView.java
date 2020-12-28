package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import thedrake.BoardPos;
import thedrake.Move;
import thedrake.Tile;

public class TileView extends Pane {

    private BoardPos boardPos;

    private Tile tile;

    private TileBackgrounds backgrounds = new TileBackgrounds();

    private Border selectBorder = new Border(
        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private TileViewContext tileViewContext;

    private Move move;

    private final ImageView moveImage;

    public TileView(BoardPos boardPos, Tile tile, TileViewContext tileViewContext) {
        this.boardPos = boardPos;
        this.tile = tile;
        this.tileViewContext = tileViewContext;

        setPrefSize(100, 100);
        setMinWidth(100);
        update();

        setOnMouseClicked(e -> onClick());                                  // On click action

        moveImage = new ImageView(getClass().getResource("/assets/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }

    private void onClick() {
        if (move != null)
            tileViewContext.executeMove(move);
        else if (tile.hasTroop())
            select();
    }

    public void select() {                          // Black line around selected tile
        setBorder(selectBorder);
        tileViewContext.tileViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);
    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public BoardPos position() {
        return boardPos;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

}
