package qfm_ad;

import java.util.HashSet;

public class Taxa {
	private String name; // node name
	private byte partition; //0 = A, 1 = B
    //private int state; //0 = unmoved, 1 = moved
    //private int taxaScore;
    //private boolean locked;//dont need to lock anymore
    //Taxa tnext;
    private HashSet<SVD_Log> svdTable;//later i'll change it to hashSet cz now order doesn't matter 
    
    /////For threading
    private int val;		
	private int sat;
	private int vat;
	//private int def;
	public int partitionIndex;//it's for taxa serial number in partition
    /////For threading

	public int getPartitionIndex() {
		return partitionIndex;
	}

	public Taxa(String name) {
		this(name, (byte) -1);
	}
	
//	 public Taxa(String name, int partition) {
//		 this(name, partition, false);
//	}
	
	 
	public Taxa(String name, int partition) {
		this.name = name;
		this.partition = (byte) partition;
		//this.locked = locked;
		
		this.svdTable = new HashSet<SVD_Log>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean add(SVD_Log e) {
				if(contains(e))
		            remove(e);
				return super.add(e);
			}

			
		};
		
	}
	


	public HashSet<SVD_Log> getSvdTable() {
		return svdTable;
	}

	

//	public boolean isLocked() {
//		return locked;
//	}
//
//	public void setLocked(boolean locked) {
//		this.locked = locked;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public byte getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = (byte) partition;
	}
	

//	public int getState() {
//		return state;
//	}
//
//	public void setState(int state) {
//		this.state = state;
//	}

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

//	public int getDef() {
//		return def;
//	}
//
//	public void setDef(int def) {
//		this.def = def;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Taxa))
			return false;
		Taxa other = (Taxa) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



}