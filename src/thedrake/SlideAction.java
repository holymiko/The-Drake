package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction{           // Nakopirovano z ShiftAction
    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (state.canStep(origin, target)) {
            result.add(new StepOnly(origin, (BoardPos) target));
            target = target.stepByPlayingSide(offset(), side);

            while (target != TilePos.OFF_BOARD
                    && state.tileAt(target).canStepOn()
                    && !state.armyOnTurn().boardTroops().troopPositions().contains(target)
                    && !state.armyNotOnTurn().boardTroops().troopPositions().contains(target)) {
                result.add(new StepOnly(origin, (BoardPos) target));
                target = target.stepByPlayingSide(offset(), side);
            }
        } else
            if( state.canCapture(origin, target)
//            && state.army(side).boardTroops().at(origin).isPresent()         // Monk can capture, but cannot pass throw it
//            && state.army(side).boardTroops().at(origin).get().troop().name().equals("Monk")
            )
                result.add(new StepAndCapture(origin, (BoardPos) target));

        return result;
    }
}
