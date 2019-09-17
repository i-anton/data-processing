package data.analysis;

import data.Line;

import java.util.ArrayList;

public class Stationary {
    public static double avg(final Line line, int startIdx, int endIdx){
        var result = 0.0;
        for (int i = startIdx; i < endIdx; i++) {
            result += line.getY(i);
        }
        return result/(endIdx-startIdx);
    }
    public static double dispersion(final Line line, int startIdx, int endIdx){
        var result = 0.0;
        var avg = avg(line,startIdx,endIdx);
        for (int i = startIdx; i < endIdx; i++) {
            var value = line.getY(i);
            result += (value-avg)*(value-avg);
        }
        return result/(endIdx-startIdx);
    }
    public static double amplitude(final Line line, int startIdx, int endIdx){
        double minVal = line.getY(startIdx);
        double maxVal = line.getY(startIdx);
        for (int i = startIdx + 1; i < endIdx; i++) {
            double curr = line.getY(i);
            if (curr > maxVal) maxVal = curr;
            if (curr < minVal) minVal = curr;
        }
        return maxVal - minVal;
    }
    public static ArrayList<Line> dataPerInterval(final Line line, int intervalsCount, double error){
        Line dispResult = new Line(intervalsCount);
        Line avgResult = new Line(intervalsCount);
        Line errorResult = new Line(intervalsCount);
        final var size = line.getSize();
        final int pointsInInterval = size / intervalsCount;

        int startIdx = 0, endIdx;
        double ampl = amplitude(line,0,size);
        for (int i = 0; i < intervalsCount; i++) {
            endIdx = Math.min(startIdx+pointsInInterval,size);
            dispResult.setX(i,dispersion(line,startIdx,endIdx));
            avgResult.setX(i,avg(line,startIdx,endIdx));
            errorResult.setX(i, error*ampl);
            startIdx+=pointsInInterval;
        }
        var list = new ArrayList<Line>();
        list.add(dispResult);
        list.add(avgResult);
        list.add(errorResult);

        return list;
    }
}
