package qfm_ad;

import java.util.LinkedHashSet;

public class MultiReturnType {
	private LinkedHashSet<Taxa> partA;
	private LinkedHashSet<Taxa> partB;
	public MultiReturnType(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB) {
		this.partA = partA;
		this.partB = partB;
	}
	public LinkedHashSet<Taxa> getPartA() {
		return partA;
	}
	public LinkedHashSet<Taxa> getPartB() {
		return partB;
	}
	
	
//	private LinkedHashSet<Quartet> liQuartets;
//	private LinkedHashSet<Taxa> liTaxa;
//	public MultiReturnType(LinkedHashSet<Quartet> liQuartets, LinkedHashSet<Taxa> liTaxa) {
//		super();
//		this.liQuartets = liQuartets;
//		this.liTaxa = liTaxa;
//	}
//	public LinkedHashSet<Quartet> getLiQuartets() {
//		return liQuartets;
//	}
//	public void setLiQuartets(LinkedHashSet<Quartet> liQuartets) {
//		this.liQuartets = liQuartets;
//	}
//	public LinkedHashSet<Taxa> getLiTaxa() {
//		return liTaxa;
//	}
//	public void setLiTaxa(LinkedHashSet<Taxa> liTaxa) {
//		this.liTaxa = liTaxa;
//	}
	
	

	
	
	

}
