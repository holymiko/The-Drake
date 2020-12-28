package thedrake;

import java.io.PrintWriter;

public class GameState implements JSONSerializable{
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy, 
			PlayingSide sideOnTurn, 
			GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}
	
	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		return orangeArmy;
	}
	
	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}
	
	public Tile tileAt(TilePos pos) {

		if( armyOnTurn().boardTroops().at(pos).isPresent() )
			return armyOnTurn().boardTroops().at(pos).get();

		if( armyNotOnTurn().boardTroops().at(pos).isPresent() )
			return armyNotOnTurn().boardTroops().at(pos).get();

		return board.at(pos);

		// BoardTile je potomek Tile
		// BoardPos implementuje Tilepos
		// TroopTile implementuje Tile

	}
	
	private boolean canStepFrom(TilePos origin) {
		if( this.result != GameResult.IN_PLAY )
			return false;
		if( origin == TilePos.OFF_BOARD )
			return false;
		if( !armyOnTurn().boardTroops().isLeaderPlaced() || !armyNotOnTurn().boardTroops().isLeaderPlaced() )	// Cant step now
			return false;
		if( armyOnTurn().boardTroops().isPlacingGuards() || armyNotOnTurn().boardTroops().isPlacingGuards() )	// Cant step now
			return false;
		if( armyNotOnTurn().boardTroops().troopPositions().contains(origin) )				// Not your troop here
			return false;
		if( !armyOnTurn().boardTroops().troopPositions().contains(origin) )					// Your troop not here
			return false;

		return true;
	}

	private boolean canStepTo(TilePos target) {
		if( this.result != GameResult.IN_PLAY )
			return false;
		if( target == TilePos.OFF_BOARD )
			return false;
		if( !tileAt(target).canStepOn() )
			return false;
//		if( armyOnTurn().boardTroops().troopPositions().contains(target) )			// Your unit unit here // Doesnt work ?
//			return false;
//		if( armyNotOnTurn().boardTroops().troopPositions().contains(target) )			// Not your unit unit here // Doesnt work ?
//			return false;

		return true;
	}
	
	private boolean canCaptureOn(TilePos target) {
		if( this.result != GameResult.IN_PLAY )
			return false;
		if( !armyNotOnTurn().boardTroops().troopPositions().contains(target) )			// Not your unit unit here // Doesnt work ?
			return false;

		return  true;
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	
	public boolean canPlaceFromStack(TilePos target) {
		if( this.result != GameResult.IN_PLAY )
			return false;
		if( armyOnTurn().stack().isEmpty() )
			return false;
		if( target == TilePos.OFF_BOARD )
			return false;

		if( !armyOnTurn().boardTroops().isLeaderPlaced() ) {					// Placing the Leaders
			if (armyOnTurn().side() == PlayingSide.BLUE)
				return target.row() == 1 && tileAt(target).canStepOn();
			else
				return target.row() == board.dimension() && tileAt(target).canStepOn();
		}
		if( armyOnTurn().boardTroops().isPlacingGuards() ) {					// Placing guards
			return ( target.isNextTo( armyOnTurn().boardTroops().leaderPosition() ) && !armyOnTurn().boardTroops().troopPositions().contains(target) );
		}

		if( armyOnTurn().boardTroops().troopPositions().contains(target) || armyNotOnTurn().boardTroops().troopPositions().contains(target) )
			return false;
		if( !tileAt(target).canStepOn() )
			return false;

		for (TilePos friend : target.neighbours()) {
			if( armyOnTurn().boardTroops().troopPositions().contains(friend) )
				return true;
		}

		return false;
	}
	
	public GameState stepOnly(BoardPos origin, BoardPos target) {		
		if(canStep(origin, target))		 
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(), 
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"result\":");
		result.toJSON(writer);
		writer.printf(",\"board\":");
		board.toJSON(writer);
		writer.printf(",\"blueArmy\":");
		blueArmy.toJSON(writer);
		writer.printf(",\"orangeArmy\":");
		orangeArmy.toJSON(writer);
		writer.printf("}");
	}

	public GameResult getResult() {
		return result;
	}
}
