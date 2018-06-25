package main.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/*
 * Used as a nexus to build the minimax search tree. It has the information relevant to each move, such as the line added, the current board, score made and the players,
 * taking into consideration who is moving. The outcomes of making this move(other player's turn) are stored in 'outcomes'
 */

public class Node {

    private Board board;
    Set<Node> outcomes;
    private int max;
    private int min;
    private Line added;

    //parameters for the .dot file
    private static long ID = 0;
    private long id;
    private int score;
    private boolean used;
    private boolean pruned;

    //Constructor

    public Node(Board board, int max, int min, Line added) {
        this.board = board.cloneBoard();
        this.outcomes = new HashSet<>();
        this.max = max;
        this.min = min;
        this.added = added;
        this.score = 0;
        this.used = false;
        this.pruned = true;
        ID = (ID + 1) % Long.MAX_VALUE;
        this.id = ID;
    }

    /*
    *Used as a pair, when possibilities is called, it calls the wrapper method that fills the outcomes set with all the possible moves that can be made in the board,
    * if the board is completed it creates a leaf node with no move(null).
     */

    //Getters and Setters

    public void possibilities(){
        createOutcomes(board);
    }

    private void createOutcomes( Board board ){

        if(board.isComplete()){
            outcomes.add(new Node(board.cloneBoard(), min, max, null));
            return;
        }

        for(Line line : board.availableMoves().keySet()){
            Board aux = board.cloneBoard();
            if(aux.canMakeMove(line)) {
                aux.makeMove(line, max);
                Node t = new Node(aux, min, max, line);
                outcomes.add(t);
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getMax() {
        return max;
    }

    public Line getAdded() {
        return added;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void use(){
        this.used = true;
    }

    public void visit(){
        this.pruned = false;
    }

    /*
    *Used to create a new file containing the .dot information, which is updated after every move, thus making the tree shorter, because there are less options.
    * It is based on the exercise on the graphs Guide, after building the tree by adding the relevant information from each node,it exports it to the source file.
     */

    public void createFile() throws IOException {
        File file = new File("dotFile.dot");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("digraph Tree {\n");
        fileWriter.write(this.id + "[label=\"" +"Start " + "score: " + this.score + "\"");
        if(this.used){
            fileWriter.write(", style=filled, fillcolor=green");
        }else if (this.pruned){
            fileWriter.write(", style=filled, fillcolor=grey");
        }
        fileWriter.write("];\n");
        for(Node node : this.outcomes){
            createFile(node, fileWriter);
        }
        for(Node node : this.outcomes){
            fileWriter.write(this.id + "->"+ node.id +"\n");
        }
        fileWriter.write("}\n");
        fileWriter.close();
    }

    private void createFile(Node node, FileWriter fileWriter) throws IOException {
        fileWriter.write(node.id + " [label=\"");
        fileWriter.write("Score: " + node.score + "\"");
        if(node.pruned){
            fileWriter.write(", style=filled, fillcolor=grey");
        }
        if(node.used){
            fileWriter.write("style=filled, fillcolor=green");
        }
        fileWriter.write("];\n");
        for(Node outcome : node.outcomes){
            createFile(outcome,fileWriter);
        }
        for (Node outcome : node.outcomes){
            fileWriter.write(this.id + "->" + outcome.id + "\n");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (max != node.max) return false;
        if (min != node.min) return false;
        if (id != node.id) return false;
        if (score != node.score) return false;
        if (used != node.used) return false;
        if (pruned != node.pruned) return false;
        if (board != null ? !board.equals(node.board) : node.board != null) return false;
        if (outcomes != null ? !outcomes.equals(node.outcomes) : node.outcomes != null) return false;
        return added != null ? added.equals(node.added) : node.added == null;
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (outcomes != null ? outcomes.hashCode() : 0);
        result = 31 * result + max;
        result = 31 * result + min;
        result = 31 * result + (added != null ? added.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + score;
        result = 31 * result + (used ? 1 : 0);
        result = 31 * result + (pruned ? 1 : 0);
        return result;
    }
}



