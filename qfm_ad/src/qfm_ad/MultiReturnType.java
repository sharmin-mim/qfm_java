package qfm_ad;

import java.util.LinkedHashSet;

public class MultiReturnType {
	private LinkedHashSet<Quartet> liQuartets;
	private LinkedHashSet<Taxa> liTaxa;
	public MultiReturnType(LinkedHashSet<Quartet> liQuartets, LinkedHashSet<Taxa> liTaxa) {
		super();
		this.liQuartets = liQuartets;
		this.liTaxa = liTaxa;
	}
	public LinkedHashSet<Quartet> getLiQuartets() {
		return liQuartets;
	}
	public void setLiQuartets(LinkedHashSet<Quartet> liQuartets) {
		this.liQuartets = liQuartets;
	}
	public LinkedHashSet<Taxa> getLiTaxa() {
		return liTaxa;
	}
	public void setLiTaxa(LinkedHashSet<Taxa> liTaxa) {
		this.liTaxa = liTaxa;
	}
	
	

	
	
	

}
