package thedrake.ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardView extends Pane {
    private final Border selectBorder = new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private final CardViewSelected cardViewSelected;

    public CardView(CardViewSelected cardViewSelected) {
        setOnMouseClicked(e -> select());
        this.cardViewSelected = cardViewSelected;
    }

    public void select() {                          // Black line around selected tile
        setBorder(selectBorder);
        cardViewSelected.cardViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }
}
