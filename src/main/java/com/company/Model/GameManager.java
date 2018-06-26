package com.company.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameManager  implements Serializable{
    private static final int PLAYING = 1;
    private static final int FINALIZED = 0;

    //ASSETS
    private Board board;
    private Player player1;
    private Player player2;
    private Player current;

    //MODE
    private int size;
    private int ai; //0(human vs human), 1(human vs AI(AI moves first)), 2(human vs AI(human moves first)), 3(AI vs AI)
    private int mode; //0 (time) , 1(depth)
    private int tod; //contains time if mode is 0 or depth to reach if mode is 1
    private boolean prune; //activates or de activates alphabeta prune

    //GAME
    private int state; //0(FINALIZED), 1(playing)

    //Constructor

    public GameManager(int size, int ai, int mode, int tod, boolean prune) {
        this.size = size;

        if(ai == 0){
            this.player1 = new Player(1, true);
            this.player2 = new Player(2,true);
        }else if (ai == 1){
            this.player1 = new AIPlayer(1,2, mode,tod,prune);
            this.player2 = new Player(2,true);
        }else if(ai == 2){
            this.player1 = new Player(1,true);
            this.player2 = new AIPlayer(2,1, mode, tod, prune);
        }else{
            this.player1 = new AIPlayer(1,2, mode, tod, prune);
            this.player2 = new AIPlayer(2,1, mode, tod, prune);
        }
        this.board = new Board(size);

        this.current = this.player1;
        this.state = PLAYING;
        this.mode = mode;
        this.tod = tod;
        this.prune = prune;
    }

    /*
    *called when the undo button is pressed, it retrieves the board of the CURRENT PLAYER'S state before the last move.
     */

    public void undo(){
        if(current.canUndo()) {
            board = current.getBoard();
            player1.setScore(board.getScore(1));
            player2.setScore(board.getScore(2));
        }else{
            System.out.print("No previous play found!");
        }
    }

    /*
    *Calls on the Board method to add a line, also saving the previous board state for the undo, and checks if the game is ended.
     */

    public void move(Line line) {
        current.addBoard(board);
        int closedSquares = board.makeMove(line, current.getColour());
        current.addPoints(closedSquares);
        nextTurn(closedSquares);
        if(board.availableMoves().size() == 0){
            endGame();
        }
        return;
    }

    /*
    *Used to symbolize the change of turn by changing the pointer to who is the current player.
    * If the player hsa closed one or more squares, thus scoring points, the turn doesn't change.
     */

    private void nextTurn(int closedSquares) {
        if(closedSquares != 0) return;
        if(current == player1){
            current = player2;
        }else{
            current = player1;
        }
        board.setCurrentPlayer(current.getColour());
    }

    /*
    *Used to get the next computer move, it calls on the minimax algorithm, and returns the best possible move option.
    * For better understanding on how it works see the Project's Report
     */

    public Line aiMove() throws IOException {
        Line line;
        int other = 1;
        if(current.getColour() == 1){
            other = 2;
        }

        if(mode == 1){
            line = MiniMax.depthMiniMax(board,tod,prune,current.getColour(),other);
        }else{
            line = MiniMax.timeMiniMax(board,tod,prune,current.getColour(),other);
        }

        return line;
    }

    //Used to end the game

    public void endGame() {
        state = FINALIZED;
    }

    //Getters and Setters

    public Board getBoard() {
        return board;
    }

    public int getState() {
        return state;
    }

    public Player getCurrent() {
        return current;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getSize() {
        return size;
    }

    public Player getWinningPlayer(){
        if(board.getWinner() == 1) return player1;
        if(board.getWinner() == 2)return player2;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameManager that = (GameManager) o;

        if (size != that.size) return false;
        if (ai != that.ai) return false;
        if (mode != that.mode) return false;
        if (tod != that.tod) return false;
        if (prune != that.prune) return false;
        if (state != that.state) return false;
        if (board != null ? !board.equals(that.board) : that.board != null) return false;
        if (player1 != null ? !player1.equals(that.player1) : that.player1 != null) return false;
        if (player2 != null ? !player2.equals(that.player2) : that.player2 != null) return false;
        return current != null ? current.equals(that.current) : that.current == null;
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (player1 != null ? player1.hashCode() : 0);
        result = 31 * result + (player2 != null ? player2.hashCode() : 0);
        result = 31 * result + (current != null ? current.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + ai;
        result = 31 * result + mode;
        result = 31 * result + tod;
        result = 31 * result + (prune ? 1 : 0);
        result = 31 * result + state;
        return result;
    }

    //Used for saving and loading

    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(board);
        out.writeObject(player1);
        out.writeObject(player2);
        out.writeObject(current);
        out.writeObject(size);
        out.writeObject(ai);
        out.writeObject(mode);
        out.writeObject(tod);
        out.writeObject(prune);
        out.writeObject(state);


    }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        board = (Board) ois.readObject();
        player1 = (Player) ois.readObject();
        player2 = (Player) ois.readObject();
        current = (Player)ois.readObject();
        size = (Integer)ois.readObject();
        ai = (Integer) ois.readObject();
        mode = (Integer) ois.readObject();
        tod = (Integer)ois.readObject();
        prune = (Boolean) ois.readObject();
        state = (Integer) ois.readObject();
    }
}

