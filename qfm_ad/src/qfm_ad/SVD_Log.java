package qfm_ad;
/*
 * This class contains  Quartet_Id and its corresponding values of satisfied, violated and deferred quartets
 * in respect to a partition
 * 
 * */

import java.util.Objects;

public class SVD_Log {
	//private Quartet quartet;
	private int quertetID;
	private int sat;//number of satisfied quartet
	private int vat;//number of violated quartet
	//private int def;//number of deferred quartet
	private char qStat;//status of quartet
	
	//public SVD_Log(Quartet quartet, int sat, int vat, int def, char qStat) {
	public SVD_Log(int quertetID, int sat, int vat, char qStat) {
		this.quertetID = quertetID;
		this.sat = sat;
		this.vat = vat;
		//this.def = def;
		this.qStat = qStat;
	}

	public char getqStat() {
		return qStat;
	}

	public void setqStat(char qStat) {
		this.qStat = qStat;
	}

	public int getQuartetID() {
		return quertetID;
	}
	public int getSat() {
		return sat;
	}
	public int getVat() {
		return vat;
	}
	
	public void setSat(int sat) {
		this.sat = sat;
	}

	public void setVat(int vat) {
		this.vat = vat;
	}

	//	public int getDef() {
//		return def;
//	}
	@Override
	public int hashCode() {
		return Objects.hash(quertetID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SVD_Log))
			return false;
		SVD_Log other = (SVD_Log) obj;
		return Objects.equals(quertetID, other.quertetID);
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