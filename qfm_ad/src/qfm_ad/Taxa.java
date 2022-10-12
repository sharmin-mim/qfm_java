package qfm_ad;

import java.util.ArrayList;
import java.util.List;

public class Taxa {

    public String name;         // node name
    private byte partition;     // 0 = A, 1 = B
    public boolean locked;      // dont need to lock anymore
    public List<Integer> relaventQIDs = new ArrayList<>();

    /////For threading
    private int val;
    private int sat;
    private int vat;
    //private int def;
    public int partitionIndex;//it's for taxa serial number in partition
    public int sumOfSatOfSVDmap = 0;
    public int sumOfVatOfSVDmap = 0;
    /////For threading

    public int getPartitionIndex() {
        return partitionIndex;
    }

    public Taxa(String name) {
        this(name, (byte) -1);
    }

    public Taxa(String name, int partition) {
        this.name = name;
        this.partition = (byte) partition;
        this.locked = false;

    }

    public byte getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = (byte) partition;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public synchronized void addSatScoresWRTmovingTaxa(int satisfied) {
        sumOfSatOfSVDmap += satisfied;
    }

    public synchronized void addVatScoresWRTmovingTaxa(int violated) {
        sumOfVatOfSVDmap += violated;
    }

    public synchronized void mCalculateScore(int prevS, int prevV, int prevScore) {

        sat = prevS + sumOfSatOfSVDmap;
        vat = prevV + sumOfVatOfSVDmap;
        val = (sat - vat) - prevScore;

    }

    public void resetTaxa() {
        locked = false;
        sumOfSatOfSVDmap = 0;
        sumOfVatOfSVDmap = 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Taxa)) {
            return false;
        }
        Taxa other = (Taxa) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
