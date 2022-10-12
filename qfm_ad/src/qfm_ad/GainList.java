package qfm_ad;

public class GainList {
    //If I give Taxa instead of taxaToMove, space complexity becomes high. That's why I am rolling back to previous implementation

    public Taxa taxa;
    public int val;

    public GainList(Taxa taxa, int val) {

        this.taxa = taxa;
        this.val = val;
    }
}
