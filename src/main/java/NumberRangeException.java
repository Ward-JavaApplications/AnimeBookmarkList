public class NumberRangeException extends Exception{
    private int minRange;
    private int maxRange;
    public NumberRangeException(int minRange, int maxRange){
        this.maxRange = maxRange;
        this.minRange = minRange;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }
}
