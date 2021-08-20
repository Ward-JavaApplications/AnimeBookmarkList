public class StatsContainerPriority {
    private int p0;
    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;

    public StatsContainerPriority(int p0, int p1, int p2, int p3, int p4, int p5) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
    }
    public StatsContainerPriority(int[] p){
        this.p0 = p[0];
        this.p1 = p[1];
        this.p2 = p[2];
        this.p3 = p[3];
        this.p4 = p[4];
        this.p5 = p[5];
    }
    public int[] getAsArray(){
        return new int[]{p0,p1,p2,p3,p4,p5};
    }

    public int getP0() {
        return p0;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getP3() {
        return p3;
    }

    public int getP4() {
        return p4;
    }

    public int getP5() {
        return p5;
    }
}
