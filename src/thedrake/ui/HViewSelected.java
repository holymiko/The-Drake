package thedrake.ui;

import thedrake.GameState;
import thedrake.PlayingSide;

public interface HViewSelected {

    void cardPackageViewSelected(PackageView packageView);
    void cardPackageViewSelected(BoardView boardView);
    void showMovesFromStack(PackageView packageView);
    void newPackage(BoardView boardView, PlayingSide playingSide);
    void newCapture(BoardView boardView, PlayingSide playingSide);
    void gameOver(GameState gameState);
}
