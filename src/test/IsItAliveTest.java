package test;

import main.Model.AIPlayer;
import main.Model.Board;
import main.Model.Line;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class IsItAliveTest {

    private Board board;
    private int size = 4;

    @Before
    public void before(){
        board = new Board(size);
    }

    @Test
    public void evaluateAvailableTest(){
        assertEquals(24,board.availableMoves());
    }


    @Test
    public void isItAliveDepthTest(){
        AIPlayer cortana = new AIPlayer(1,2, 1,3,true);

        board.makeMove(new Line(0,1,1,1),1);
        board.makeMove(new Line(0,0,0,1),1);
        board.makeMove(new Line(0,0,1,0),1);
        board.makeMove(new Line(2,2,3,2),1);
        board.makeMove(new Line(2,2,2,3),1);
        board.printBoard();


        Line line = null;
        try {
            line = cortana.miniMax(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
        board.makeMove(line,1);




        board.printBoard();
    }

    @Test
    public void isItAliveTimeTest(){
        AIPlayer cortana = new AIPlayer(1,2, 0,4,true);
        long i = System.currentTimeMillis();
        Line line = null;
        try {
            line = cortana.miniMax(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
        i = System.currentTimeMillis() - i;
        System.out.print(i);
        System.out.print('\n');
        board.makeMove(line,1);

        board.printBoard();
    }
}
