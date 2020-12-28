package suite04;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import thedrake.Army;
import thedrake.Board;
import thedrake.BoardTroops;
import thedrake.CaptureOnly;
import thedrake.GameState;
import thedrake.Move;
import thedrake.PlayingSide;
import thedrake.PositionFactory;
import thedrake.StandardDrakeSetup;
import thedrake.StepAndCapture;
import thedrake.StepOnly;

public class ActionsTest {

	private Set<Move> makeSet(Move ...moves) {
		Set<Move> result = new HashSet<Move>();
		for(Move m : moves) {
			result.add(m);
		}

		return result;
	}

	@Test
	public void test()  {
		Board board = new Board(4);
		PositionFactory pf = board.positionFactory();
		StandardDrakeSetup setup = new StandardDrakeSetup();

		BoardTroops blueTroops = new BoardTroops(PlayingSide.BLUE);
		blueTroops = blueTroops
				.placeTroop(setup.DRAKE, pf.pos("a1"))
				.placeTroop(setup.CLUBMAN, pf.pos("a2"))
				.placeTroop(setup.SPEARMAN, pf.pos("b2"));
		Army blueArmy = new Army(blueTroops, Collections.emptyList(), Collections.emptyList());

		BoardTroops orangeTroops = new BoardTroops(PlayingSide.ORANGE);
		orangeTroops = orangeTroops
				.placeTroop(setup.DRAKE, pf.pos("c4"))
				.placeTroop(setup.MONK, pf.pos("c3"))
				.placeTroop(setup.CLUBMAN, pf.pos("b3"));
		Army orangeArmy = new Army(orangeTroops, Collections.emptyList(), Collections.emptyList());

		GameState state = new GameState(board, blueArmy, orangeArmy);

		//Slide: drake at a1 can move to free spaces
		assertEquals(
			makeSet(
				new StepOnly(pf.pos("a1"), pf.pos("b1")),
				new StepOnly(pf.pos("a1"), pf.pos("c1")),
				new StepOnly(pf.pos("a1"), pf.pos("d1"))
			),
			new HashSet<Move>(
				state.tileAt(pf.pos("a1")).movesFrom(pf.pos("a1"), state)
			)
		);

		assertEquals(
				makeSet(
						new StepAndCapture(pf.pos("b2"), pf.pos("b3")),
						new CaptureOnly(pf.pos("b2"), pf.pos("c4"))
				),
				new HashSet<Move>(
						state.tileAt(pf.pos("b2")).movesFrom(pf.pos("b2"), state)
				)
		);

		state = state.stepOnly(pf.pos("a1"), pf.pos("d1"));

		//Slide: drake at c4 can move to free spaces in both directions
		assertEquals(
				makeSet(
						new StepOnly(pf.pos("c4"), pf.pos("a4")),
						new StepOnly(pf.pos("c4"), pf.pos("b4")),
						new StepOnly(pf.pos("c4"), pf.pos("d4"))
				),
				new HashSet<Move>(
						state.tileAt(pf.pos("c4")).movesFrom(pf.pos("c4"), state)
				)
		);

		//Slide: monk at c3 can capture enemy unit, but cannot pass through it
		assertEquals(
				makeSet(
						new StepAndCapture(pf.pos("c3"), pf.pos("b2")),
						new StepOnly(pf.pos("c3"), pf.pos("d2")),
						new StepOnly(pf.pos("c3"), pf.pos("b4")),
						new StepOnly(pf.pos("c3"), pf.pos("d4"))
				),
				new HashSet<Move>(
						state.tileAt(pf.pos("c3")).movesFrom(pf.pos("c3"), state)
				)
		);

		assertEquals(
				makeSet(
					new StepOnly(pf.pos("b3"), pf.pos("a3")),
					new StepOnly(pf.pos("b3"), pf.pos("b4")),
					new StepAndCapture(pf.pos("b3"), pf.pos("b2"))
				),
				new HashSet<Move>(
					state.tileAt(pf.pos("b3")).movesFrom(pf.pos("b3"), state)
				)
			);

	}
}
