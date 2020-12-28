package thedrake.ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardView extends Pane {
    private Border selectBorder = new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private CardViewSelected cardViewSelected;

    public CardView(CardViewSelected cardViewSelected) {
        setOnMouseClicked(e -> onClick());
        this.cardViewSelected = cardViewSelected;
    }

    private void onClick() {
//        if (move != null)
//            tileViewContext.executeMove(move);
//        else
//            if (tile.hasTroop())
            select();
    }

    public void select() {                          // Black line around selected tile
        setBorder(selectBorder);
        cardViewSelected.cardViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }
}
