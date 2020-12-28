package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

	TileAt[][] board;

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(int dimension) {

		board = new TileAt[dimension][dimension];
		PositionFactory position = positionFactory();

		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++) {
				board[i][j] = new TileAt(position.pos( i, j), BoardTile.EMPTY);
			}
	}

	// Rozměr hrací desky
	public int dimension() {
		return board[0].length;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(TilePos pos) {
		return board[pos.i()][pos.j()].tile;
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt ...ats) {
		Board tmpBoard = new Board(dimension());

		for (int i = 0; i < dimension(); i++){
			tmpBoard.board[i] = board[i].clone();
		}

		for (TileAt x : ats) {
			tmpBoard.board[x.pos.i()][x.pos.j()] = x;
		}

		return tmpBoard;
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {
		return new PositionFactory(dimension());
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"dimension\":");
		writer.printf(String.valueOf(this.dimension()));
		writer.printf(",\"tiles\":[");
		for (int i = 0; i < dimension(); i++) {
			for (int j = 0; j < dimension(); j++) {
				board[j][i].tile.toJSON(writer);
				if( j+1 < dimension() || i+1 < dimension() )
					writer.printf(",");
			}
		}
		writer.printf("]}");
	}

	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

