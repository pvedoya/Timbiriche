package com.company.Model;

import java.io.IOException;

public class MiniMax {

    //Values used for pruning and as a base for calculating best scores

    private static int MIN = Integer.MIN_VALUE;
    private static int MAX = Integer.MAX_VALUE;

    /*
    *Creates the fist node for the minimax depth algorithm, and its respective sons, then iterates through said sons, finding the best option, returning said option, by calling the algorithm and constantly comparing the results.
     */

    public static Line depthMiniMax(Board currBoard, int level, boolean prune, int player, int other) throws IOException {
        Node node  = new Node(currBoard,player,other,null);
        int best = MIN;
        Line toAdd = null;
        Node bestBranch = null;

        node.possibilities();
        node.visit();

        for(Node out : node.outcomes){
            int local = depthMiniMax(out,false,prune, level,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if(local > best){
                toAdd = out.getAdded();
                best = local;
                bestBranch = out;
            }
        }
        //Prepares Node for the .dot file
        bestBranch.use();
        node.setScore(best);
        node.use();
        node.createFile();
        return toAdd;
    }

    /*
    *Minimax depth wrapper, it works recursively by first checking if the base node was reached (checking and returning the move's score if that was the case), or else,
    * it acts as the player or opponent by simulating the best play for both(it does this by the boolean parameter and an if), and for each player, simulates possible outcomes,
    * calling again the minimax method, and iterating through the results, returning the best one, which would be the worst one fot the other player. It also creates a nre .dot connection for each node.
    * This algorithm uses a pre defined depth as a stop parameter.
     */

    private static int depthMiniMax(Node node, boolean isMax, boolean prune, int level, int alpha, int beta) {
        node.visit();
        if (level == 0 || node.getBoard().availableMoves().isEmpty()) {
            int eval = evaluate(node.getAdded(), node.getBoard(), node.getMax());
            node.setScore(eval);
            return eval;
        }
        if (isMax) {
            Node maxNode = null;
            int max = MIN;

            node.possibilities();

            for (Node out : node.outcomes) {
                int local = depthMiniMax(out, false, prune, level - 1, alpha, beta);
                if(local > max){
                    max = local;
                    alpha = Math.max(alpha, local);
                    maxNode = out;
                    if (prune && beta <= alpha) {
                        return max;
                    }
                }
            }
            node.setScore(max);
            maxNode.use();
            return max;
        } else {
            Node minNode = null;
            int min = MAX;
            node.possibilities();

            for (Node out : node.outcomes) {
                int local = depthMiniMax(out, true, prune, level - 1, alpha, beta);
                if(local < min){
                    min = local;
                    beta = Math.min(beta, local);
                    minNode = out;
                    if (prune && beta <= alpha) {
                        return min;
                    }
                }
            }
            node.setScore(min);
            minNode.use();
            return min;
        }
    }

    /*
    *Creates the fist node for the minimax time algorithm, and its respective sons, then iterates through said sons, finding the best option, returning said option, by calling the algorithm and constantly comparing the results.
     */

    public static Line timeMiniMax(Board currBoard, int time, boolean prune,int player, int other) throws IOException {
        int best = MIN;
        Node node = new Node(currBoard, player,other,null);
        Line toAdd = null;
        Node bestBranch = null;

        node.possibilities();
        node.visit();

        for(Node out : node.outcomes){
            int local = timeMiniMax(out,false,time + System.currentTimeMillis(),prune,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if(local > best){
                toAdd = out.getAdded();
                best = local;
                bestBranch = out;
            }
        }
        //Prepares Node for the .dot file
        bestBranch.use();
        node.setScore(best);
        node.use();
        node.createFile();
        return toAdd;
    }

    /*
    *Minimax time wrapper, it works recursively by first checking if the base node was reached (checking and returning the move's score if that was the case), or else,
    * it acts as the player or opponent by simulating the best play for both(it does this by the boolean parameter and an if), and for each player, simulates possible outcomes,
    * calling again the minimax method, and iterating through the results, returning the best one, which would be the worst one fot the other player. It also creates a nre .dot connection for each node.
    * This algorithm uses a pre defined time as a stop parameter.
     */

    //TODO:corregir que no se si anda joya

    private static int timeMiniMax(Node node, boolean isMax, long maxTime, boolean prune, int alpha, int beta) {
        node.visit();

        if(System.currentTimeMillis() >= maxTime ||  node.getBoard().availableMoves().isEmpty()){
            int eval = evaluate(node.getAdded(), node.getBoard(), node.getMax());
            node.setScore(eval);
            return eval;
        }
        if(isMax){
            Node maxNode = null;
            int max = MIN;

            node.possibilities();

            for(Node out : node.outcomes){
                int local = timeMiniMax(out,!isMax,maxTime,prune,alpha,beta);
                if(local > max){
                    max = local;
                    alpha = Math.max(alpha, local);
                    maxNode = out;
                    if (prune && beta <= alpha) {
                        return max;
                    }
                }
            }
            node.setScore(max);
            maxNode.use();
            return max;
        }else{
            Node minNode = null;
            int min= MAX;
            node.possibilities();

            for(Node out: node.outcomes){
                int local = timeMiniMax(out,true,maxTime,prune,alpha,beta);
                if(local < min){
                    min = local;
                    beta = Math.min(beta, local);
                    minNode = out;
                    if (prune && beta <= alpha) {
                        return min;
                    }
                }
            }
            node.setScore(min);
            minNode.use();
            return min;
        }
    }

    /*
    *Heuristic, called for each leaf of the minimax tree. For more information see the Project Report.
     */

    private static int evaluate(Line added, Board board, int player){
        int value;
        value = board.scoreDifference(player)  * 20;
        if(board.getCurrentPlayer() == player){
            value = value - board.sizeNSquares(3)*5 + board.sizeNSquares(2); //the idea is that the ai doesn't want to leave size 3 squares but it wants to leave size two squares tho
        }else{
            value = value + board.sizeNSquares(3)*5 - board.sizeNSquares(2);
        }
        return value;
    }

}




