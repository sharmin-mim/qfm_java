package qfm_ad;

/*
 * This class contains  Quartet_Id and its corresponding values of satisfied, violated and deferred quartets
 * in respect to a partition
 * 
 * */

public class SVD_Log {
    private int sat;    //number of satisfied quartet
    private int vat;    //number of violated quartet
    private char qStat; //status of quartet

    public SVD_Log(int sat, int vat, char qStat) {
        super();
        this.sat = sat;
        this.vat = vat;
        this.qStat = qStat;
    }

    public char getqStat() {
        return qStat;
    }

    public void setSVD(int sat, int vat, char qStat) {
        this.sat = sat;
        this.vat = vat;
        this.qStat = qStat;
    }

    public int getSat() {
        return sat;
    }

    public int getVat() {
        return vat;
    }
}
