package qfm_ad;
/*
 * This class contains  Quartet_Id and its corresponding values of satisfied, violated and deferred quartets
 * in respect to a partition
 * 
 * */

import java.util.Objects;

public class SVD_Log {
	private Quartet quartet;
	private int sat;//number of satisfied quartet
	private int vat;//number of violated quartet
	private int def;//number of deferred quartet
	private char qStat;
	
	public SVD_Log(Quartet quartet, int sat, int vat, int def, char qStat) {

		this.quartet = quartet;
		this.sat = sat;
		this.vat = vat;
		this.def = def;
		this.qStat = qStat;
	}

	public char getqStat() {
		return qStat;
	}

	public void setqStat(char qStat) {
		this.qStat = qStat;
	}

	public Quartet getQuartet() {
		return quartet;
	}
	public int getSat() {
		return sat;
	}
	public int getVat() {
		return vat;
	}
	public int getDef() {
		return def;
	}
	@Override
	public int hashCode() {
		return Objects.hash(quartet);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SVD_Log))
			return false;
		SVD_Log other = (SVD_Log) obj;
		return Objects.equals(quartet, other.quartet);
	}
	
	/*
	private int QuartetID;
	private int sat;//number of satisfied quartet
	private int vat;//number of violated quartet
	private int def;//number of deferred quartet
	public SVD_Log(int quartetID, int sat, int vat, int def) {
		
		QuartetID = quartetID;
		this.sat = sat;
		this.vat = vat;
		this.def = def;
	}
	public int getQuartetID() {
		return QuartetID;
	}
	public int getSat() {
		return sat;
	}
	public int getVat() {
		return vat;
	}
	public int getDef() {
		return def;
	}
	@Override
	public int hashCode() {
		return Objects.hash(QuartetID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SVD_Log))
			return false;
		SVD_Log other = (SVD_Log) obj;
		return QuartetID == other.QuartetID;
	}
	
	*/
}