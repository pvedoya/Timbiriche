package main.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Line implements Serializable{
    private boolean painted;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Line(int x1, int y1, int x2, int y2) {
        this.painted = false;
        if(x1<0 || x2 < 0 || y1 < 0 || y2 < 0  ){
            throw new IllegalArgumentException("ERROR: coordinates must be positive or 0");
        }
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public boolean isPainted() {
        return painted;
    }

    public void paint(){
        this.painted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (painted != line.painted) return false;
        if (x1 != line.x1) return false;
        if (y1 != line.y1) return false;
        return x2 == line.x2 && y2 == line.y2;
    }

    @Override
    public int hashCode() {
        int result = (painted ? 1 : 0);
        result = 31 * result + x1;
        result = 31 * result + y1;
        result = 31 * result + x2;
        result = 31 * result + y2;
        return result;
    }

    public Line cloneLine(){
        Line clone = new Line(x1,y1,x2,y2);
        clone.painted = painted;
        return clone;
    }

    @Override
    public String toString() {
        return "Line{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }

    public void saveObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(painted);
        out.writeObject(x1);
        out.writeObject(y1);
        out.writeObject(x2);
        out.writeObject(y2);
    }

    public void loadObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        painted = (Boolean) ois.readObject();
        x1 = (Integer) ois.readObject();
        y1 = (Integer) ois.readObject();
        x2 = (Integer)ois.readObject();
        y2 = (Integer)ois.readObject();

    }
}
