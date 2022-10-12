package qfm_ad;

/*
 * This class contains  Quartet_Id and its corresponding values of satisfied, violated and deferred quartets
 * in respect to a partition
 * 
 * */

//import java.util.Objects;
public class SVD_Log {
    //private Quartet quartet;
    //private int quertetID;

    private int sat;//number of satisfied quartet
    private int vat;//number of violated quartet
    //private int def;//number of deferred quartet
    private char qStat;//status of quartet

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

//	public int getQuartetID() {
//		return quertetID;
//	}
    public int getSat() {
        return sat;
    }

    public int getVat() {
        return vat;
    }

}
