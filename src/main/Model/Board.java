package main.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Board implements Cloneable,Serializable{

    private Square[][] board;
    private int size;
    private int currentPlayer,score1,score2;
    private Map<Line,Set<Square>> available; //Set of available moves, and the squares it affects, a very effective way of making all the changes in each square.

    //Constructors

    public Board(int n){
        if(n < 2) throw new IllegalArgumentException("ERROR: entry value must be greater than 1");
        this.board = new Square[n-1][n-1];
        fillBoard(board);
        this.currentPlayer = 1;
        this.score1 = 0;
        this.score2 = 0;
        this.size = n-1;
        this.available = availableMoves();
    }

    public Board(int n, int current,int p1score, int p2score){
        if(n < 2) throw new IllegalArgumentException("ERROR: entry value must be greater than 1");
        this.board = new Square[n-1][n-1];
        fillBoard(board);
        this.size = n-1;
        this.currentPlayer = current;
        this.score1 = p1score;
        this.score2 = p2score;
        this.available = availableMoves();
    }

    /*
    *Receives an empty matrix, and fills it with new Square instances, it creates the board.
     */

    private void fillBoard(Square[][] b){
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b.length ; j++){
                Square square = new Square(i,j);
                b[j][i] = square;
            }
        }
    }

    /*
    *Creates a new instance of the board.
     */

    public Board cloneBoard(){
        Board clone = new Board(size+1,currentPlayer,score1,score2);

        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length;j++){
                clone.board[i][j] = board[i][j].cloneSquare();
            }
        }
        clone.available = clone.availableMoves();
        return clone;
    }

    /*
    *Adds a new line by getting it from the available method, and iterating through the squares in the set, painting said line in each.
    * It also checks if there were squares completed, and returns how many were complated.
     */

    public int makeMove(Line line, int player){
        int completed = 0;

        if(!available.containsKey(line)){
            throw new IllegalStateException("Line already taken or out of bounds");
        }

        Set<Square> toChange = available.get(line);

        for(Square sq : toChange){
            if(line.equals(sq.getBottom())){
                sq.getBottom().paint();
                if(checkCompleted(sq,player)){
                    completed++;
                }
            }else if(line.equals(sq.getTop())){
                sq.getTop().paint();
                if(checkCompleted(sq,player)){
                    completed++;
                }
            }else if(line.equals(sq.getRight())){
                sq.getRight().paint();
                if(checkCompleted(sq,player)){
                    completed++;
                }
            }else if(line.equals(sq.getLeft())){
                sq.getLeft().paint();
                if(checkCompleted(sq,player)){
                    completed++;
                }
            }
        }
        available=getAvailable();
        return completed;
    }

    /*
    *Given a square and the last player to have made a move, it returns if said square was completed.
     */

    private boolean checkCompleted(Square s, int player) {
        if(s.checkComplete()){
            s.setOwner(player);
            if(player==1){
                score1++;
            }else if (player ==2){
                score2++;
            }

            return true;
        }
        return false;
    }

    /*
    * Used to know if the game should be ended, by verifying of there are moves that can be made.
     */

    public boolean isComplete(){
        return availableMoves().size() == 0;
    }

    /*
    *Checks if the received line can be added to the board, by checking if the line belongs to the available map.
     */

    public boolean canMakeMove(Line line) {
        return available.containsKey(line);
    }



    //Getters and Setters
    public int getScore(int player){
        if(player == 1){
            return score1;
        }
        return score2;
    }

    public Square[][] getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Map<Line, Set<Square>> getAvailable() {
        return available;
    }



    /*
    *Returns the player with the higher score.
     */
    public int getWinner(){
        if(score1 > score2) return 1;
        else if(score2 > score1) return 2;
        return 0;
    }

    /*
    *Returns a map containing, for each line still available to paint, a set containing all the squares where it is present.
     */
    public Map<Line,Set<Square>> availableMoves(){
        Map<Line,Set<Square>> moves = new HashMap<Line,Set<Square>>();

        for(int i =0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                Square square = board[i][j];
//                if(square.getAvailable() > 0){ //skips this box if available equals 0
                if(!square.getBottom().isPainted()){
                    if(moves.containsKey(square.getBottom())){
                        moves.get(square.getBottom()).add(square);
                    }else{
                        Set<Square> aux = new HashSet<Square>();
                        aux.add(square);
                        moves.put(square.getBottom(),aux);
                    }}
                if(!square.getLeft().isPainted()){
                    if(moves.containsKey(square.getLeft())){
                        moves.get(square.getLeft()).add(square);
                    }else{
                        Set<Square> aux = new HashSet<Square>();
                        aux.add(square);
                        moves.put(square.getLeft(),aux);
                    }}
                if(!square.getTop().isPainted()){
                    if(moves.containsKey(square.getTop())){
                        moves.get(square.getTop()).add(square);
                    }else{
                        Set<Square> aux = new HashSet<Square>();
                        aux.add(square);
                        moves.put(square.getTop(),aux);
                    }}
                if(!square.getRight().isPainted()){
                    if(moves.containsKey(square.getRight())){
                        moves.get(square.getRight()).add(square);
                    }else{
                        Set<Square> aux = new HashSet<Square>();
                        aux.add(square);
                        moves.put(square.getRight(),aux);
                    }
                }
            }
        }
//        }
        return moves;
    }


    /*
    *Used for calculating the heuristic, it returns how many squares of size N are present in the board.
     */
    public int sizeNSquares(int N) {
        int count = 0;
        for(int i = 0; i< board.length;i++){
            for(int j = 0; j< board.length; j++){
                if(board[i][j].getAvailable() == N){
                    count++;
                }
            }
        }
        return count;
    }


    /*
    *Used for calculating the heuristic, it returns the difference between the current player and the other player.
     */
    public int scoreDifference(int player){
        if(player == 1){
            return score1 - score2;
        }
        return score2 - score1;
    }

    /*
    *Methods that prints a rustic board with 1's and 0's, used before there was a UI, to know the state of the board.
     */

    public void checkState() {
        System.out.print("Board:" + '\n');
        printBoard();
        System.out.print('\n');

        System.out.print("Info:" + '\n');
        System.out.print("Available:" + availableMoves().size() + '\n');
        System.out.print("Player one: " + score1 + ", player two: " + score2+ '\n');

        System.out.print("Can be painted: " + '\n');
        for(Line l : available.keySet()){
            System.out.print("" + l + '\n');
        }
        System.out.print('\n');

    }

    public void printBoard(){
        String string;
        for(int i = 0; i < board.length; i++){
            for(int x = 0; x < 3; x++){
                string = "";
                for ( int j = 0; j < board.length; j++){
                    Square sq = board[i][j];
                    if(x == 0 || x==2 ) {
                        if((sq.getTop().isPainted() && x==0) || (x==2 && sq.getBottom().isPainted())){
                            string += 1;
                            string += " ";
                            string += 1;
                            string += " ";
                            string += 1;
                            string += " ";
                        }else {
                            string += 0;
                            string += " ";
                            string += 0;
                            string += " ";
                            string += 0;
                            string += " ";
                        }
                    }else if (x==1){
                        if(sq.getLeft().isPainted()){
                            string +=1;
                            string += " ";
                        }else{
                            string+=0;
                            string += " ";
                        }
                        if(sq.checkComplete()){
                            string+=sq.getOwner();
                            string += " ";
                        }else{
                            string+=0;
                            string += " ";
                        }
                        if(sq.getRight().isPainted()){
                            string += 1;
                            string += " ";
                        }else{
                            string +=0;
                            string += " ";
                        }
                    }
                    string += " ";
                }
                System.out.print(string);
                System.out.print('\n');
            }
            System.out.print('\n');
        }
        System.out.print("\n---------------\n");
    }

    public void printSquares(){
        for(Line l : available.keySet()){
            System.out.print(l.toString() + '\n');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        if (size != board1.size) return false;
        if (currentPlayer != board1.currentPlayer) return false;
        if (score1 != board1.score1) return false;
        if (score2 != board1.score2) return false;
        if (!Arrays.deepEquals(board, board1.board)) return false;
        return available != null ? available.equals(board1.available) : board1.available == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + size;
        result = 31 * result + currentPlayer;
        result = 31 * result + score1;
        result = 31 * result + score2;
        result = 31 * result + (available != null ? available.hashCode() : 0);
        return result;
    }

    //Used for loading and saving

    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(board);
        out.writeObject(size);
        out.writeObject(currentPlayer);
        out.writeObject(score1);
        out.writeObject(score2);
        out.writeObject(available);
    }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        board = (Square[][]) ois.readObject();
        size= (int) ois.readObject();
        currentPlayer = (int) ois.readObject();
        score1 = (int)ois.readObject();
        score2 = (int)ois.readObject();
        available= (Map<Line,Set<Square>>) ois.readObject();
    }

}
