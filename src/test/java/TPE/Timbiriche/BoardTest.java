package TPE.Timbiriche;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class BoardTest {
    private Board board;
    private int size = 4;

    @Before
    public void before(){
        board = new Board(size);
    }

    @Test
    public void evaluateAvailableTest(){
        assertEquals(24,board.availableMoves().size());
    }

    @Test
    public void printSquareTest(){
        board.printSquares();
    }

    @Test
    public void squareTest(){
        Line line1 = new Line(1,1,1,2);
        Line line2 = new Line(1,1,2,1);
        Line line3 = new Line(1,2,2,2);
        Line line4 = new Line(2,1,2,2);
        Line line5 = new Line(1,0,2,0);
        Line line6 = new Line(1,0,1,1);
        Line line7 = new Line(2,0,2,1);

        assertEquals(false,board.makeMove(line2,1));
        assertEquals(false,board.makeMove(line1,1));
        assertEquals(false,board.makeMove(line3,1));
        assertEquals(true,board.makeMove(line4,1));

        assertEquals(20,board.availableMoves().size());
        assertEquals(false,board.availableMoves().containsKey(line1));

        board.makeMove(line5,1);
        board.makeMove(line6,1);
        board.makeMove(line7,1);

        assertEquals(1,board.getWinner());
        assertEquals(2, board.getScore(1));

    }

    @Test
    public void undoTest(){
        GameManager gm = new GameManager(4,0,0,0,false);

        Line line1 = new Line(1,1,1,2);
        Line line2 = new Line(1,1,2,1);
        Line line3 = new Line(1,2,2,2);
        Line line4 = new Line(2,1,2,2);

        gm.move(line1);
        gm.move(line2);
        gm.undo();

        assertEquals(23,gm.getBoard().availableMoves().size());

        gm.move(line3);
        gm.move(line4);
        gm.undo();

        gm.getBoard().checkState();

        assertEquals(22,gm.getBoard().availableMoves().size());
        assertEquals(0,gm.getBoard().getScore(1));
        assertEquals(0,gm.getBoard().getScore(2));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void wrongSizeArgumentTest(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("ERROR: entry value must be greater than 1");
        Board board = new Board(-1);
    }

    @Test
    public void wrongLineArgumentTest(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("ERROR: coordinates must be positive or 0");

        Line line1 = new Line(0,0,0,-1);
    }

}
