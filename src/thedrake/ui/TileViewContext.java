package thedrake.ui;

import thedrake.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

}
