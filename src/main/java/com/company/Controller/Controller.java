package com.company.Controller;


import com.company.Main.AppLauncher;
import com.company.Model.Board;
import com.company.Model.GameManager;
import com.company.Model.Line;
import com.company.Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Controller implements Serializable{
    private GameManager gm;
    private AppLauncher appLauncher;

    public Controller(GameManager gameManager, AppLauncher appLauncher){
        this.gm = gameManager;
        this.appLauncher = appLauncher;
    }

    public void initialize(GameManager gameManager, AppLauncher appLauncher){
        this.gm = gameManager;
        this.appLauncher = appLauncher;
    }

    //Actions on Manager

    public void undo(){
        gm.undo();
    }

    public void save(){
        appLauncher.save();
    }

    public Line aiMove() throws IOException {
        return gm.aiMove();
    }

    public void move(Line line) {
        gm.move(line);
    }


    //Getters and Setters

    public Player getPlayer1() {
        return gm.getPlayer1();
    }


    public Player getPlayer2() {
        return gm.getPlayer2();
    }

    public Player getCurrent() {
        return gm.getCurrent();
    }

    public int getSize() {
        return gm.getSize();
    }

    public int getState() {
        return gm.getState();
    }

    public Board getBoard() {
        return gm.getBoard();
    }

    public Player getWinningPlayer() {
        return gm.getWinningPlayer();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException{
        out.writeObject(gm);
        out.writeObject(appLauncher);
    }

    private void readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
        gm = (GameManager) ois.readObject();
        appLauncher = (AppLauncher) ois.readObject();
    }
}