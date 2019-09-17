package data.model;

import data.Line;

import java.util.Random;

public class SimpleTransforms {
    public static void normalize(Line line, double scale){
        double minVal = line.getY(0);
        double maxVal = line.getY(0);
        final int size = line.getSize();
        for (int i = 1; i < size; i++) {
            double curr = line.getY(i);
            if (curr > maxVal) maxVal = curr;
            if (curr < minVal) minVal = curr;
        }
        for (int i = 0; i < size; i++) {
            double curr = line.getY(i);
            curr = scale*((curr-minVal)/(maxVal-minVal)-0.5);
            line.setY(i,curr);
        }
    }

    public static void spikes(Line line, int seed, int spikeNum, double scale){
        final Random rnd = new Random(seed);
        final int size = line.getSize();
        final double halfScale = scale/2.0;
        for (int i = spikeNum; i > 0; i--) {
            final int spikeIdx = rnd.nextInt(size);
            final double value = line.getY(spikeIdx);
            if (spikeIdx != 0) {
                final double y = line.getY(spikeIdx-1);
                line.setY(spikeIdx - 1,y+y*halfScale);
            }
            if (spikeIdx != size) {
                final double y = line.getY(spikeIdx+1);
                line.setY(spikeIdx + 1,y+y*halfScale);
            }
            line.setY(spikeIdx, value+value*scale);
        }
    }

    public static void shift(Line line, double start, double end, double shift, double scale){
        final int size = line.getSize();
        int i = 0;
        double curr = line.getX(i);
        while (i < size && curr < start){
            i++;
            curr = line.getX(i);
        }
        while (i < size && curr < end){
            final double val = line.getY(i);
            curr = line.getX(i);
            line.setY(i, (val+shift)*scale);
            i++;
        }
    }
}
