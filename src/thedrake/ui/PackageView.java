package thedrake.ui;

import javafx.scene.layout.AnchorPane;
import thedrake.*;

import java.util.Collections;
import java.util.List;

public class PackageView extends AnchorPane implements CardViewSelected{

    private final PlayingSide playingSide;

    private CardView selected;

    private final PackageViewContext packageViewContext;

    public PackageView (BoardView boardView, PlayingSide playingSide, PackageViewContext packageViewContext) {                 // Makes package out of cards on stack
        this.packageViewContext = packageViewContext;
        this.playingSide = playingSide;
        List<Troop> troops = boardView.getGameState().army(playingSide).stack();
        Collections.reverse(troops);
        double i = 0;
        for (Troop troop:
                troops) {
            CardView pane = new CardView(this);
            pane.setPrefSize(100, 100);
            pane.setBackground(new TileBackgrounds().getTroop(troop, playingSide, TroopFace.AVERS));
            if(playingSide == PlayingSide.ORANGE) {
                PackageView.setRightAnchor(pane, boardView.getPadding().getRight() + i);
                PackageView.setTopAnchor(pane, boardView.getPadding().getTop()/* - i*/);
            } else {
                PackageView.setRightAnchor(pane, boardView.getPadding().getRight()+i);
                PackageView.setBottomAnchor(pane, boardView.getPadding().getBottom()+i);
            }
            this.getChildren().add(pane);

            i+=10;
        }
        Collections.reverse(troops);
    }

    @Override
    public void cardViewSelected(CardView cardView) {
        if (selected != null && selected != cardView)
            selected.unselect();

        selected = cardView;

        packageViewContext.cardPackageViewSelected(this);        // Message to HBox to unselect other package
        packageViewContext.showMovesFromStack(this);
    }

    public void unselect(){
        if( selected != null )
            selected.unselect();
    }

    public PlayingSide getPlayingSide() {
        return playingSide;
    }
}
