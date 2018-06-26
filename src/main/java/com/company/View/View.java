package com.company.View;

import com.company.Controller.Controller;
import com.company.Main.AppLauncher;
import com.company.Model.Board;
import com.company.Model.GameManager;
import com.company.Model.Line;
import com.company.Model.Player;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class View {

    private Controller controller;

    //Used to make art

    @FXML
    private TextField Player1Score,Player1Tag,Player2Tag,Player2Score,CurrentPlayer;

    @FXML
    private AnchorPane pane;

    private GridPane grid;

    private static final int SPACE = 5;

    //colours
    private static final Paint circleColor = Color.BLACK;
    private static final Paint emptyLineColor = Color.WHEAT;
    private static final Paint usedLineColor = Color.BLACK;
    private static final Paint squareNotComplete = Color.WHITE;
    private static final Paint squareComplete = Color.BLUE;

    //assets
    private Player player1,player2;


    /*
    *Sets up the visual environment, paints the board, initial scores, etc..
     */

    public void loadView(){
        setBoard();
        setNames();
        updateFields();
        updateBoard();
        pane.getChildren().add(grid);
    }

    /*
    *Paint the board, by creating a matrix of squares, circles and rectangles, representing the different parts of the board.
     */

    private void setBoard() {
        grid = new GridPane();
        grid.setPadding(new Insets(SPACE,SPACE,SPACE,SPACE));

        int size = controller.getSize();
        int cols = size + size - 1;
        double width = pane.getWidth();
        double height = pane.getHeight();
        double max = (height > width)? height : width;

        double radius = (max - SPACE) / (2 * (size + 3 * (size - 1)));
        double length = 6 * radius;
        double tall = radius * 2;

        for(int i = 0; i < cols; i++){
            for(int j = 0; j < cols; j++){
                if(i % 2 == 0 && j % 2 == 0) {
                    grid.add(new Circle(radius, circleColor), i, j);
                }else if (i % 2 == 1 && j % 2 == 0 ){
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

    //Setters



    private void setNames() {
        player1 = controller.getPlayer1();
        player2 = controller.getPlayer2();

        String name = player1.isHuman()? "Player 1" : "Cortana";
        Player1Tag.textProperty().setValue(name);
        name = player2.isHuman()? "Player 2" : "Siri";
        Player2Tag.textProperty().setValue(name);

        CurrentPlayer.textProperty().setValue("Current : " + controller.getCurrent().getColour());
    }


    //Button Handlers

    @FXML
    private void saveHandler(MouseEvent mouseEvent) {
        controller.save();
    }

    @FXML
    private void undoHandler(MouseEvent mouseEvent) {
        if(controller.getState() == 1) {
            controller.undo();
            updateFields();
            updateBoard();
        }
    }

    private void updateFields() {
        Player1Score.textProperty().setValue(String.valueOf(controller.getPlayer1().getScore()));
        Player2Score.textProperty().setValue(String.valueOf(controller.getPlayer2().getScore()));
        CurrentPlayer.textProperty().setValue("Current: " + controller.getCurrent().getColour());
    }

    @FXML
    private void AIMoveHandler() throws IOException {
        if(!controller.getCurrent().isHuman()){
            Line line = controller.aiMove();
            controller.move(line);
            updateFields();
            updateBoard();
        }
    }

    @FXML
    private void exitHandler(MouseEvent mouseEvent) {
        exit();
    }

    private class RectangleEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if(controller.getCurrent().isHuman()) {
                Node source = (Node) event.getSource();
                Integer col = GridPane.getColumnIndex(source);
                Integer row = GridPane.getRowIndex(source);
                boolean horizontal = row % 2 == 0;
                if (horizontal) {
                    controller.move(new Line(col / 2, row / 2, (col / 2) + 1, row / 2));
                } else {
                    controller.move(new Line(col / 2, row / 2, (col / 2), (row / 2) + 1));
                }
                updateFields();
                updateBoard();
            }
        }
    }

    //Changes the colours of the board assets to identify them as selected or not.

    private void updateBoard(){
        Board board = controller.getBoard();
        ObservableList<Node> assets = grid.getChildren();

        int col,row;
        for(Node node : assets){
            col = GridPane.getColumnIndex(node);
            row = GridPane.getRowIndex(node);
            if(col % 2 == 1 && row % 2 == 0 ){
                Line line = new Line(col/2,row/2,(col/2) + 1,row/2);
                if(!board.getAvailable().containsKey(line)){
                    ((Rectangle) node).setFill(usedLineColor);
                }else{
                    ((Rectangle) node).setFill(emptyLineColor);
                }
            }else if(col % 2 == 0 && row % 2 == 1){
                Line line = new Line(col/2,row/2,col/2,row/2+1);
                if(!board.getAvailable().containsKey(line)){
                    ((Rectangle) node).setFill(usedLineColor);
                }else{
                    ((Rectangle) node).setFill(emptyLineColor);
                }
            } else if( col % 2 == 1 && row % 2 == 1){
                if(board.getBoard()[row/2][col/2].checkComplete()){
                    ((Rectangle) node).setFill(squareComplete);
                }else{
                    ((Rectangle) node).setFill(squareNotComplete);
                }
            }
        }

        if(controller.getState() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Player player = controller.getWinningPlayer();
            alert.setContentText("GAME FINISHED");
            if(player != null) {
                alert.setContentText("GAME FINISHED, " + (player.isHuman() ? "Player " + player.getColour() : (player.getColour() == 1 ? "Cortana" : "Siri")) + " Wins");
            }else{
                alert.setContentText("GAME FINISHED, Its a Tie!" );
            }
            alert.showAndWait();
        }
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
