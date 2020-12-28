package thedrake.ui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import thedrake.PlayingSide;
import thedrake.Troop;
import thedrake.TroopFace;

import java.util.List;

public class CapturedView extends AnchorPane {

    private final PlayingSide playingSide;

    public CapturedView (BoardView boardView, PlayingSide playingSide) {                 // Makes package out of cards on stack
        this.playingSide = playingSide;
        List<Troop> troops = boardView.getGameState().army(playingSide).captured();
        double i = 0;
        for (Troop troop:
                troops) {
            Pane pane = new Pane();
            pane.setPrefSize(100, 100);
            pane.setBackground(new TileBackgrounds().getTroop(troop, playingSide, TroopFace.AVERS));
            if(playingSide == PlayingSide.ORANGE) {
                pane.setBackground(new TileBackgrounds().getTroop(troop, PlayingSide.BLUE, TroopFace.AVERS));
                thedrake.ui.CapturedView.setBottomAnchor(pane, boardView.getPadding().getBottom()+200+i);
            } else {
                pane.setBackground(new TileBackgrounds().getTroop(troop, PlayingSide.ORANGE, TroopFace.AVERS));
                thedrake.ui.CapturedView.setTopAnchor(pane, boardView.getPadding().getTop()+200+i);
            }
            thedrake.ui.CapturedView.setRightAnchor(pane, boardView.getPadding().getRight());
//            thedrake.ui.CapturedView.setLeftAnchor(pane, boardView.getPadding().getLeft());
            this.getChildren().add(pane);

            i+=40;
        }
    }

    public PlayingSide getPlayingSide() {
        return playingSide;
    }


}
