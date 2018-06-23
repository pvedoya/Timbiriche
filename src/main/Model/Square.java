package main.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Square implements Serializable{
    private int owner;
    private Line top;
    private Line bottom;
    private Line left;
    private Line right;
    private int x;
    private int y;

    public Square(int x, int y) {
        this.owner = 0;
        this.top = new Line(x,y,x+1,y);
        this.bottom= new Line(x,y+1,x+1,y+1);
        this.left = new Line(x,y,x,y+1);
        this.right = new Line(x+1,y,x+1,y+1);
        this.x = x;
        this.y = y;
    }

    public boolean checkComplete(){
        return (top.isPainted() && bottom.isPainted() && left.isPainted() && right.isPainted());
    }

    public double getX() {
        return x;
    }

    public int getAvailable(){
        int counter = 0;
        if(!top.isPainted()){
            counter++;
        }
        if(!right.isPainted()){
            counter++;
        }
        if(!left.isPainted()){
            counter++;
        }
        if(!bottom.isPainted()){
            counter++;
        }
        return counter;
    }

    public Line getTop() {
        return top;
    }

    public Line getBottom() {
        return bottom;
    }

    public Line getLeft() {
        return left;
    }

    public Line getRight() {
        return right;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Square{" +
                "top=" + top +
                ", bottom=" + bottom +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public Square cloneSquare(){
        Square aux = new Square(x, y);
        aux.owner = owner;

        aux.bottom = bottom.cloneLine();
        aux.left = left.cloneLine();
        aux.top = top.cloneLine();
        aux.right = right.cloneLine();

        return aux;
    }

    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(owner);
        out.writeObject(top);
        out.writeObject(bottom);
        out.writeObject(left);
        out.writeObject(right);
        out.writeObject(x);
        out.writeObject(y);
            }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        owner = (Integer) ois.readObject();
        top = (Line) ois.readObject();
        bottom= (Line) ois.readObject();
        left = (Line)ois.readObject();
        right= (Line)ois.readObject();
        x = (Integer) ois.readObject();
        y = (Integer) ois.readObject();
    }
}
