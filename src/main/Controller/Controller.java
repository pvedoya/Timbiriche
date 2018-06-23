package main.Controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.Main;
import main.Model.GameManager;
import main.Model.Line;
import main.Model.Player;
import main.Model.Square;


public class Controller {
    private TextField Player1Score,Player1Tag,Player2Tag,Player2Score;
    private AnchorPane maxPane;
    private GridPane grid;
    private static final int SPACE = 5;

    //colours
    private static final Paint circleColor = Color.BLACK;
    private static final Paint emptyLineColor = Color.WHITE;
    private static final Paint usedLineColor = Color.BLACK;
    private static final Paint squareNotComplete = Color.BROWN;
    private static final Paint squareComplete = Color.BLUE;

    //assets
    private GameManager gm;
    private Main main;
    private Player player1,player2;

    public void initialize(GameManager gameManager){
        setGameManager(gameManager);
        setBoard();
        setNames();
        updateScore();
        updateBoard();
        maxPane.getChildren().add(grid);
    }

    private void setBoard() {
        grid = new GridPane();
        grid.setPadding(new Insets(SPACE,SPACE,SPACE,SPACE));

        int size = gm.getSize();
        double width = maxPane.getWidth();
        double height = maxPane.getHeight();
        double max = (height > width)? height : width;

        double circleRaius = (max - SPACE) / (2 * (size + 3 * (size - 1)));
        double length = 6 * circleRaius;
        double tall = circleRaius * 2;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(i % 2 == 0 && j % 2 == 0) {
                    grid.add(new Circle(circleRaius, circleColor), i, j);
                }else if (i % 2 == 1 && j % 2 == 0){
                    Rectangle hLine = new Rectangle(length,tall,emptyLineColor);
                    hLine.setOnMouseClicked(new RectangleEvent());
                    grid.add(hLine,i,j);
                }else if (i % 2 == 0 && j % 2 == 1){
                    Rectangle vLine = new Rectangle(tall,length,emptyLineColor);
                    vLine.setOnMouseClicked(new RectangleEvent());
                    grid.add(vLine,i,j);
                }else{
                    Rectangle square = new Rectangle(length,length,squareNotComplete);
                    grid.add(square,i,j);
                }
            }
        }

    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void setNames() {
        player1 = gm.getPlayer1();
        player2 = gm.getPlayer2();

        String name = player1.isHuman()? "Player 1" : "Cortana";
        Player1Tag.textProperty().setValue(name);
        name = player2.isHuman()? "Player 2" : "Siri";
        Player2Tag.textProperty().setValue(name);
    }


    private void setGameManager(GameManager gameManager){
        this.gm = gameManager;
    }

    @FXML
    private void saveHandler(MouseEvent mouseEvent) {
        main.save();
    }

    @FXML
    private void loadHandler(MouseEvent mouseEvent) {
        main.load();
    }

    @FXML
    private void undoHandler(MouseEvent mouseEvent) {
        gm.undo();
        updateBoard();
        updateScore();
    }

    private void updateScore() {
        Player1Score.textProperty().setValue(String.valueOf(gm.getPlayer1().getScore()));
        Player2Score.textProperty().setValue(String.valueOf(gm.getPlayer2().getScore()));
    }

    @FXML
    private void exitHandler(MouseEvent mouseEvent) {
        gm.endGame();
    }

    @FXML
    private void dotFileHandler(MouseEvent mouseEvent) {
        main.generateDotFile();
    }


    private class RectangleEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Node source = (Node) event.getSource();
            Integer col = GridPane.getColumnIndex(source);
            Integer row = GridPane.getRowIndex(source);
            boolean horizontal = row % 2 == 0;
            if(horizontal){
                gm.move(new Line(col/2,row/2,(col/2) + 1,col/2));
            }else{
                gm.move(new Line(col/2,row/2,(col/2),(col/2)+ 1));
            }
            updateBoard();
            updateScore();
        }
    }


    /*corregir*/

    private void updateBoard(){
        Square[][] board = gm.getBoard().getBoard();
        ObservableList<Node> assets = grid.getChildren();
        int i,j;
        for(Node node : assets){
            i = GridPane.getColumnIndex(node);
            j = GridPane.getRowIndex(node);
            if(i % 2== 1 && j % 2 == 0){
                if(board[j/2][i/2].getTop().isPainted()){
                    ((Rectangle) node).setFill(usedLineColor);
                }else{
                    ((Rectangle) node).setFill(emptyLineColor);
                }
            }else if(i % 2 == 0 && j % 2 == 1){
                if(board[j/2][i/2].getLeft().isPainted()){
                    ((Rectangle) node).setFill(usedLineColor);
                }else{
                    ((Rectangle) node).setFill(emptyLineColor);
                }
            } else if( i % 2 == 1 && j % 2 == 1){
                if(board[j/2][i/2].checkComplete()){
                    ((Rectangle) node).setFill(squareComplete);
                }else{
                    ((Rectangle) node).setFill(squareNotComplete);
                }
            }
        }
    }

}
