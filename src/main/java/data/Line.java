package data;

import de.gsi.dataset.spi.DoubleDataSet;

import java.util.Arrays;

public class Line {
    private double[] xs;
    private double[] ys;

    public Line(Line other){
        int size = other.xs.length;
        this.xs = Arrays.copyOf(other.xs, size);
        this.ys = Arrays.copyOf(other.ys, size);
    }

    public Line(int size){
        assert size >= 0;
        this.xs = new double[size];
        this.ys = new double[size];
    }

    public int getSize(){
        return xs.length;
    }
    public double getX(int index){
        assert index < xs.length && index >= 0;
        return xs[index];
    }
    public double getY(int index){
        assert index < xs.length && index >= 0;
        return ys[index];
    }
    public void setX(int index, double value){
        assert index < xs.length && index >= 0;
        xs[index] = value;
    }
    public void setY(int index, double value){
        assert index < xs.length && index >= 0;
        ys[index] = value;
    }
    public void addToDataset(DoubleDataSet dataSet){
        dataSet.setAutoNotifaction(false);
        for (int i = 0; i < xs.length; i++) {
            dataSet.add(xs[i],ys[i]);
        }
        dataSet.setAutoNotifaction(true);
    }
}
