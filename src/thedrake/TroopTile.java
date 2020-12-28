package thedrake;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable {

    final Troop troop;
    final PlayingSide side;
    final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.face = face;
        this.troop = troop;
        this.side = side;
    }

    public PlayingSide side(){
        return side;
    }

    public TroopFace face(){
        return face;
    }

    public Troop troop(){
        return troop;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> returnList = new java.util.ArrayList<>(Collections.emptyList());
        List<TroopAction> troopActions = troop.actions(face);

        for ( TroopAction troopAction : troopActions) {
           List<Move> tmpList = troopAction.movesFrom(pos, this.side, state);

           returnList.addAll(tmpList);
        }
        return returnList;
    }

    public TroopTile flipped() {
        if (face == TroopFace.AVERS)
            return new TroopTile(this.troop, this.side, TroopFace.REVERS);
        else
            return new TroopTile(this.troop, this.side, TroopFace.AVERS);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"troop\":");
        this.troop().toJSON(writer);
        writer.printf(",\"side\":");
        this.side().toJSON(writer);
        writer.printf(",\"face\":");
        this.face().toJSON(writer);
        writer.printf("}");
    }
}
