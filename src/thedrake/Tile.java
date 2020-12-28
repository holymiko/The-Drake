package thedrake;

import java.util.List;

public interface Tile {

	// Vrací True, pokud je tato dlaždice volná a lze na ni vstoupit.
	public boolean canStepOn();

	// Vrací True, pokud tato dlaždice obsahuje jednotku
	public boolean hasTroop();

	public List<Move> movesFrom(BoardPos pos, GameState state);
}
