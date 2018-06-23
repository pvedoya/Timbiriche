package main.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AIPlayer extends Player implements Serializable{
    private int mode; // 0 for time, 1 for depth
    private int tod; //time or depth
    boolean prune;
    private Board currBoard;
    private int otherColor;

    public AIPlayer(int color, int otherColor, int mode, int tod, boolean prune) {
        super(color,false);
        this.mode = mode;
        this.tod = tod;
        this.prune = prune;
        this.otherColor = otherColor;
    }

    /*
    * Calls minimax algorithm to seaerch for a solution using depth or time
    * */

    public Line miniMax(Board board) throws IOException {
        currBoard = board;
        if(tod == 0) {
            return MiniMax.timeMiniMax(currBoard, tod, prune, getColour(), otherColor);
        }

        return MiniMax.depthMiniMax(currBoard, tod, prune, getColour(), otherColor);
    }
    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(otherColor);
        out.writeObject(mode);
        out.writeObject(tod);
        out.writeObject(prune);
        out.writeObject(currBoard);
    }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        otherColor = (Integer) ois.readObject();
        mode = (Integer) ois.readObject();
        tod = (Integer) ois.readObject();
        prune= (Boolean)ois.readObject();
        currBoard = (Board) ois.readObject();
    }

}
