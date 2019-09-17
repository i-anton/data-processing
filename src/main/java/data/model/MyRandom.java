package data.model;

class MyRandom {
    private long prevValue;

    MyRandom(int seed) {
        prevValue = seed* 25923;
    }
    double nextDouble(){
        long value = prevValue * 6364136223846723L;
        long shift = ((value >>> 16L) ^ prevValue) >>> 27L;
        long rot = prevValue >>> 59L;
        value =  (shift >>> rot) | (shift << ((-rot) & 31L));
        prevValue = value;
        return value;
    }
}
