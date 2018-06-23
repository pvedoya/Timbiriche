/*package main.Model;

import java.io.IOException;
import java.util.Scanner;


public class App {

 public static void main(String[] args) throws IOException {

     //manera temporal de probar

     GameManager manager = new GameManager(4,2,1,3,true);

     while(manager.getState() == 1){
         System.out.print("Player " + manager.getCurrent().getColour() + " is playing" + '\n');
         if(manager.getCurrent().isHuman()) {
             Scanner scanner = new Scanner(System.in);

             System.out.print("Undo: ");
             int undo = scanner.nextInt();
             if(undo == 1){
                 manager.undo();
             }
             System.out.print("Argument x1: ");
             int x1 = scanner.nextInt();
             System.out.print("Argument y1: ");
             int y1 = scanner.nextInt();
             System.out.print("Argument x2: ");
             int x2 = scanner.nextInt();
             System.out.print("Argument y2: ");
             int y2 = scanner.nextInt();


             manager.move(new Line(x1,y1,x2,y2));
         }else {
             manager.aiMove();
         }
         manager.getBoard().printBoard();
         if(manager.getBoard().isComplete()){
             manager.endGame();
         }


     }
     if(manager.getBoard().isComplete()){
         System.out.print("" +manager.getBoard().getWinner() + '\n');
         System.out.print("Player 1 score:" + manager.getBoard().getScore1() + '\n');
         System.out.print("Player 2 score:" + manager.getBoard().getScore2() + '\n');

     }

 }
}
*/