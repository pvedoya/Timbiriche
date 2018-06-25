package main.Model;

import java.io.IOException;

public class MiniMax {
    private static int MIN = Integer.MIN_VALUE;
    private static int MAX = Integer.MAX_VALUE;

    public static Line depthMiniMax(Board currBoard, int level, boolean prune, int player, int other) throws IOException {
        Node node  = new Node(currBoard,player,other,null);
        int best = MIN;
        Line toAdd = null;
        node.possiblities();
        node.visit();
        Node bestBranch = null;

        for(Node out : node.outcomes){
            int local = depthMiniMax(out,false,prune, level,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if(local > best){
                toAdd = out.getAdded();
                best = local;
                bestBranch = out;
            }
        }
        bestBranch.use();
        node.setScore(best);
        node.use();
        node.createFile();
        return toAdd;
    }

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
            node.possiblities();

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
            node.possiblities();

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

    //revisar
    public static Line timeMiniMax(Board currBoard, int time, boolean prune,int player, int other) throws IOException {
        Node node = new Node(currBoard, player,other,null);
        Line toAdd = null;
        node.possiblities();
        int best = MIN;
        node.visit();
        Node bestBranch = null;

        for(Node out : node.outcomes){
            int local = timeMiniMax(out,false,time + System.currentTimeMillis(),prune,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if(local > best){
                toAdd = out.getAdded();
                best = local;
                bestBranch = out;
            }
        }
        bestBranch.use();
        node.setScore(best);
        node.use();
        node.createFile();
        return toAdd;
    }

    private static int timeMiniMax(Node node, boolean isMax, long timeLimit, boolean prune, int alpha, int beta) {
        node.visit();
        if(System.currentTimeMillis() == timeLimit){
            int eval = evaluate(node.getAdded(), node.getBoard(), node.getMax());
            node.setScore(eval);
            return eval;
        }
        if(isMax){
            int max = MIN;
            node.possiblities();
            Node maxNode = null;

            for(Node out : node.outcomes){
                int local = timeMiniMax(out,!isMax,timeLimit,prune,alpha,beta);
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
            node.possiblities();

            for(Node out: node.outcomes){
                int local = timeMiniMax(out,true,timeLimit,prune,alpha,beta);
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

    private static int evaluate(Line added, Board board, int player){
        int value;
        value = board.getScore(player) * 10;
        if(board.getCurrentPlayer() == player){
            value -= board.sizeNSquares(3)*50;
            value += board.sizeNSquares(2);
        }else{
            value += board.sizeNSquares(3)*50;
            value -= board.sizeNSquares(2);
        }
        return value;
    }

}




