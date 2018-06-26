package com.company.Model;

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

    //Constructor

    public Square(int x, int y) {
        this.owner = 0;
        this.top = new Line(x,y,x+1,y);
        this.bottom= new Line(x,y+1,x+1,y+1);
        this.left = new Line(x,y,x,y+1);
        this.right = new Line(x+1,y,x+1,y+1);
        this.x = x;
        this.y = y;
    }

    /*
    *Used after adding a new line, it  returns if the square is completed.
     */

    public boolean checkComplete(){
        return (top.isPainted() && bottom.isPainted() && left.isPainted() && right.isPainted());
    }

    /*
    * Returns how many of the square's lines are available for painting
     */

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

    //Getters and Setters

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

    /*
    Creates a new instance of this Square, used for saving the old Board in the Stack for UNDO.
     */

    public Square cloneSquare(){
        Square aux = new Square(x, y);
        aux.owner = owner;
        aux.bottom = bottom.cloneLine();
        aux.left = left.cloneLine();
        aux.top = top.cloneLine();
        aux.right = right.cloneLine();
        return aux;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (owner != square.owner) return false;
        if (x != square.x) return false;
        if (y != square.y) return false;
        if (top != null ? !top.equals(square.top) : square.top != null) return false;
        if (bottom != null ? !bottom.equals(square.bottom) : square.bottom != null) return false;
        if (left != null ? !left.equals(square.left) : square.left != null) return false;
        return right != null ? right.equals(square.right) : square.right == null;
    }

    @Override
    public int hashCode() {
        int result = owner;
        result = 31 * result + (top != null ? top.hashCode() : 0);
        result = 31 * result + (bottom != null ? bottom.hashCode() : 0);
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    //Used for loading and saving

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
