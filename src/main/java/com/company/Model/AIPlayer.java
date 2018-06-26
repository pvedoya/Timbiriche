package com.company.Model;

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

    //Constructor

    public AIPlayer(int color, int otherColor, int mode, int tod, boolean prune) {
        super(color,false);
        this.mode = mode;
        this.tod = tod;
        this.prune = prune;
        this.otherColor = otherColor;
    }

    /*
    * Calls minimax algorithm to search for a solution using depth or time
    * */

    public Line miniMax(Board board) throws IOException {
        currBoard = board;
        if(tod == 0) {
            return MiniMax.timeMiniMax(currBoard, tod, prune, getColour(), otherColor);
        }

        return MiniMax.depthMiniMax(currBoard, tod, prune, getColour(), otherColor);
    }

    //Used for saving and loading

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AIPlayer aiPlayer = (AIPlayer) o;

        if (mode != aiPlayer.mode) return false;
        if (tod != aiPlayer.tod) return false;
        if (prune != aiPlayer.prune) return false;
        if (otherColor != aiPlayer.otherColor) return false;
        return currBoard != null ? currBoard.equals(aiPlayer.currBoard) : aiPlayer.currBoard == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mode;
        result = 31 * result + tod;
        result = 31 * result + (prune ? 1 : 0);
        result = 31 * result + (currBoard != null ? currBoard.hashCode() : 0);
        result = 31 * result + otherColor;
        return result;
    }
}
