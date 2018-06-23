package main.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Node { //Stands for possibility node, not for position node(but it might ass well be so)

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

    public Node(Board board, int max, int min, Line added) {
        this.board = board.cloneBoard();
        this.outcomes = new HashSet<Node>();
        this.max = max;
        this.min = min;
        this.added = added;
        this.score = 0;
        this.used = false;
        this.pruned = true;
        ID = (ID + 1) % Long.MAX_VALUE;
        this.id = ID;
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

    public void possiblities(){
        createOutcomes(board);
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

    public void createFile() throws IOException {
        File file = new File("dotFile.gv");
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
        fileWriter.write("(" + node.added.getX1()+","+node.added.getY1()+","+node.added.getX2()+","+node.added.getY2()+")");
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
}



