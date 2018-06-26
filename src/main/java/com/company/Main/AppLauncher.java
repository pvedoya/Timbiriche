package com.company.Main;

import com.company.Controller.Controller;
import com.company.Model.GameManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import static javafx.application.Platform.exit;

public class AppLauncher extends Application implements Serializable{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private ArrayList parameters;
    private Stage primaryStage;
    private GameManager manager;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        parameters = getInput(getParameters());
        if(parameters == null){
            exit();
            throw new IllegalStateException();
        }

        FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
        Parent root = paneLoader.load();
        Controller controller = paneLoader.getController();
        controller.setAppLauncher(this);

        if(parameters.size() == 6){
            load();
        }else{
            restart();
        }

        Scene scene = new Scene(root, WIDTH,HEIGHT);
        if(primaryStage != null) this.primaryStage = primaryStage;
        else this.primaryStage = new Stage();

        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        controller.initialize(manager);
    }

    /*
    * Parses the parameters from the terminal's input by iterating through the arguments and adding each case to an arrayList containing said arguments.
     */

    private ArrayList getInput(Parameters parameters) {
        ArrayList aux = new ArrayList();
        String arg, value;
        for(int i = 0; i < parameters.getRaw().size(); i++){
            arg = parameters.getRaw().get(i);
            if(i < parameters.getRaw().size() - 1){
                value = parameters.getRaw().get(++i);
            }else{
                System.out.println("Missing parameters");
                return null;
            }
            try{
                switch(arg){
                    case"-size":
                        int num = Integer.parseInt(value);
                        if(num < 3) throw new IllegalArgumentException("Size must be bigger than 2");
                        aux.add(0,num);
                        break;
                    case "-ai":
                        num = Integer.parseInt(value);
                        if (num < 0 || num > 3){
                            throw new IllegalArgumentException("Ai integer must be between 0 and 3.");
                        }
                        aux.add(1, num);
                        break;
                    case "-mode":
                        switch (value) {
                            case "time":
                                aux.add(2, 0);
                                break;
                            case "depth":
                                aux.add(2, 1);
                                break;
                            default:
                                throw new IllegalArgumentException("Mode parameter must be time or depth.");
                        }
                        break;
                    case "-param":
                        aux.add(3, Integer.parseInt(value));
                        break;
                    case "-prune":
                        switch (value) {
                            case "on":
                                aux.add(4, true);
                                break;
                            case "off":
                                aux.add(4, false);
                                break;
                            default:
                                throw new IllegalArgumentException("Prune parameter must on or off");
                        }
                        break;
                    case "-load":
                        aux.add(5,value);
                        break;
                    default:
                        System.out.println("Illegal Argument: \""+arg+"\".");
                        return null;
                }
            }catch (Exception e){
                System.out.println("Illegal parameters found");
                return null;
            }
        }
        if (aux.size() < 5) {
            System.out.println("Missing Parameters.");
            return null;
        }
        return aux;
    }

    //Contructor-ish

    public GameManager restart(){
        manager = new GameManager((int)parameters.get(0),(int)parameters.get(1),(int)parameters.get(2),(int)parameters.get(3),(boolean)parameters.get(4));
        return manager;
    }

    //Loading and saving methods - File management methods

    public void load(){
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try{
            fileInputStream = new FileInputStream((String) parameters.get(5));
            objectInputStream = new ObjectInputStream(fileInputStream);
            AppLauncher aux = (AppLauncher) objectInputStream.readObject();
            manager = aux.manager;
            parameters = aux.parameters;
            primaryStage = aux.primaryStage;
            objectInputStream.close();
        }catch(IOException | ClassNotFoundException e){
            System.out.print(e.getMessage());
            System.out.println("Failed to load file, game will restart");
            restart();
        }
    }

    public void save(){
        File file = getFile();
        writeToFile(this,file);
    }

    private void writeToFile(Object o, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch(IOException e){
            System.out.println(e.getMessage() + " " + "Save file corrupted and didn't save properly");

        }
    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("SaveName.txt");
        return fileChooser.showSaveDialog(new Stage());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException{
        out.writeObject(parameters);
        out.writeObject(manager);
    }

    private void readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
        parameters = (ArrayList) ois.readObject();
        manager = (GameManager) ois.readObject();
    }

}
