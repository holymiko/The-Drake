package thedrake;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class BoardTroops implements JSONSerializable{
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) { 
		this.leaderPosition = TilePos.OFF_BOARD;
		this.troopMap = Collections.emptyMap();
		this.playingSide = playingSide;
		this.guards = 0;
	}
	
	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {

		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		if( troopMap.containsKey(pos) )
			return Optional.of(troopMap.get(pos));
		return Optional.empty();
	}
	
	public PlayingSide playingSide() {
		return playingSide;
	}
	
	public TilePos leaderPosition() {
		return leaderPosition;
	}

	public int guards() {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		return leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		if( ! isLeaderPlaced() )
			return false;
		return guards < 2;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		Map<BoardPos, TroopTile> x = new HashMap<>(troopMap);
		if( x.containsKey(target) )
			throw new IllegalArgumentException();
		x.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
		if( ! isLeaderPlaced() )
			return new BoardTroops(playingSide, x, target, guards);
		if( isPlacingGuards() )
			return new BoardTroops(playingSide, x, leaderPosition, guards+1);
		return new BoardTroops(playingSide, x, leaderPosition, guards);

	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if( !isLeaderPlaced() )
			throw new IllegalStateException();
		if( isPlacingGuards() )
			throw new IllegalStateException();

		if( !troopMap.containsKey(origin) || troopMap.containsKey(target) )
			throw new IllegalArgumentException();
		Map<BoardPos, TroopTile> map = new HashMap<>(troopMap);
		TroopTile t = map.get(origin);
		t = t.flipped();						// Flip the unit
		map.remove(origin);
		map.put(target, t);
		if( leaderPosition.i() == origin.i() && leaderPosition.j() == origin.j() )
			return new BoardTroops(playingSide, map, target, guards);
		return new BoardTroops(playingSide, map, leaderPosition, guards);
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");			
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");			
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if( !isLeaderPlaced() )
			throw new IllegalStateException();
		if( isPlacingGuards() )
			throw new IllegalStateException();
		if( !troopMap.containsKey(target) )
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> x = new HashMap<>(troopMap);

		x.remove(target);

		if( leaderPosition.i() == target.i() && leaderPosition.j() == target.j() )
			return new BoardTroops(playingSide, x, TilePos.OFF_BOARD, guards);
		return new BoardTroops(playingSide, x, leaderPosition, guards);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"side\":");
		this.playingSide().toJSON(writer);
		writer.printf(",\"leaderPosition\":");
		this.leaderPosition().toJSON(writer);
		writer.printf(",\"guards\":");
		writer.printf(String.valueOf(this.guards));
		writer.printf(",\"troopMap\":{");

		Set<BoardPos> positions = new HashSet<>(this.troopPositions());
		List<BoardPos> listPos = new LinkedList<>(positions);
		Collections.<BoardPos>sort(listPos);

		int x = 0;
		for ( BoardPos position : listPos ) {
			position.toJSON(writer);
			writer.printf(":");
			this.troopMap.get(position).toJSON(writer);

			x++;
			if( x < listPos.toArray().length)
				writer.printf(",");
		}

		writer.printf("}}");
	}
}
