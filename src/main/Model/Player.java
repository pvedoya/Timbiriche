package main.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;

public class Player implements Serializable{
    private int colour;
    private int score;
    private Stack<Board> playerUndo;

    private boolean human;

    public Player(int colour, boolean human) {
        this.colour = colour;
        this.playerUndo = new Stack<Board>();
        this.human = human;
    }

    public boolean isHuman(){
        return human;
    }

    public void addBoard(Board board){
        playerUndo.push(board.cloneBoard());
    }

    public Board getBoard(){
        return playerUndo.pop();
    }

    public boolean canUndo(){
        return !playerUndo.isEmpty();
    }

    public int getColour() {
        return colour;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoints(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public Player clonePlayer(){
        Player aux = new Player(colour,human);
        this.score = score;
        this.playerUndo = playerUndo;
        return aux;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (colour != player.colour) return false;
        if (score != player.score) return false;
        if (human != player.human) return false;
        return playerUndo != null ? playerUndo.equals(player.playerUndo) : player.playerUndo == null;
    }

    @Override
    public int hashCode() {
        int result = colour;
        result = 31 * result + score;
        result = 31 * result + (playerUndo != null ? playerUndo.hashCode() : 0);
        result = 31 * result + (human ? 1 : 0);
        return result;
    }

    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(score);
        out.writeObject(colour);
        out.writeObject(playerUndo);

    }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        score = (Integer) ois.readObject();
        colour = (Integer) ois.readObject();
        playerUndo = (Stack<Board>) ois.readObject();
    }
}
