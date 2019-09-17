package data.model;

import data.Line;

import java.util.Random;

public class TrendLine {
    public static Line Linear(final int dots, final double angle, final double offset){
        final Line line = new Line(dots);
        for (int i = 0; i < dots; i++) {
            line.setX(i,i);
            line.setY(i,angle*i+offset);
        }
        return line;
    }

    public static Line Exponential(final int dots, final double koef, final double degree){
        final Line line = new Line(dots);
        for (int i = 0; i < dots; i++) {
            line.setX(i,i);
            line.setY(i,koef*Math.exp(degree*i));
        }
        return line;
    }

    public static Line Piecewise(final int dots, final double stepSize){
        final Line line = new Line(dots);
        for (int i = 0; i <dots; i++) {
            double si = stepSize*i;
            line.setX(i,si);
            double value;
            if (si < 20) value = (si-10)*(si-10);
            else if (si < 40) value = -5*((si - 30)*(si-30)-120);
            else value = 2.5*si;
            line.setY(i,value);
        }

        return line;
    }

    public static Line Random(final int dots, final int seed) {
        final Line line = new Line(dots);
        Random rnd = new Random(seed);
        for (int i = 0; i < dots; i++) {
            line.setX(i,i);
            line.setY(i,rnd.nextDouble());
        }
        return line;
    }
    public static Line MyRandom(final int dots, final int seed) {
        final Line line = new Line(dots);
        MyRandom rnd = new MyRandom(seed);
        for (int i = 0; i < dots; i++) {
            line.setX(i, i);
            line.setY(i, rnd.nextDouble());
        }
        return line;
    }
}
