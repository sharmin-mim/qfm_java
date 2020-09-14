package qfm_ad;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.LinkedHashMap;
//import java.util.LinkedHashSet;
import java.util.List;

public class Taxa {
	private String name; // node name
	private byte partition; //0 = A, 1 = B
    //private int state; //0 = unmoved, 1 = moved
    //private int taxaScore;
    //private boolean locked;//dont need to lock anymore
    //Taxa tnext;
    //private HashSet<SVD_Log> svdTable;//later i'll change it to hashSet cz now order doesn't matter
    public HashMap<Integer, SVD_Log> svdTableMap = new HashMap<Integer, SVD_Log>();
    
    
    //public List<Integer> initialRelaventQuartetID = new ArrayList<Integer>();
    public List<Integer> relaventQuartetIDOfCorrespondingMovedTaxa = new ArrayList<Integer>();

    
    /////For threading
    private int val;		
	private int sat;
	private int vat;
	//private int def;
	public int partitionIndex;//it's for taxa serial number in partition
	private int sumOfSatOfSVDmap;
	private int sumOfVatOfSVDmap;
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
		
//		this.svdTable = new HashSet<SVD_Log>() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public boolean add(SVD_Log e) {
//				if(contains(e))
//		            remove(e);
//				return super.add(e);
//			}
//
//			
//		};
		
	}
	


//	public HashSet<SVD_Log> getSvdTable() {
//		return svdTable;
//	}

	

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
	public void mCalculateScore(LinkedHashMap<Integer, Quartet> quartetMap, int prevS, int prevV, int prevScore) {
		
		int satisfied = 0, violated = 0;
	    for (int index : svdTableMap.keySet()) {
	    	Quartet q = quartetMap.get(index);
	    	char qStat  = q.mCheckQuartet(name);
    		int[] sv = svScore(q, qStat);//sv[0] = s, sv[1] = 1;
    		SVD_Log svd = svdTableMap.get(index);
    		svd.setSVD(sv[0], sv[1], qStat);
            satisfied += sv[0]; violated += sv[1];
		}

	    sumOfSatOfSVDmap = satisfied;
	    sumOfVatOfSVDmap = violated;
        sat = prevS + satisfied;
        vat = prevV + violated;
        val = (sat - vat) - prevScore;
	 
	}
	
	public void mCalculateScore2(LinkedHashMap<Integer, Quartet> quartetMap, int prevS, int prevV, int prevScore) {

	
	    int satisfied = sumOfSatOfSVDmap, violated = sumOfVatOfSVDmap;//, d = 0;
	
	    for (int index : relaventQuartetIDOfCorrespondingMovedTaxa) {
	    	
	    	Quartet q = quartetMap.get(index);
    		char qStat  = q.mCheckQuartet(name);
    		int[] sv = svScore(q, qStat);//sv[0] = s, sv[1] = 1;
    		SVD_Log svd = svdTableMap.get(index);
    		satisfied = satisfied + sv[0] - svd.getSat();
    		violated = violated + sv[1] - svd.getVat();
            svd.setSVD(sv[0], sv[1], qStat);
            
		}
	    relaventQuartetIDOfCorrespondingMovedTaxa.clear();
	    sumOfSatOfSVDmap = satisfied;
	    sumOfVatOfSVDmap = violated;
        sat = prevS + satisfied;
        vat = prevV + violated;
        val = (sat - vat) - prevScore;

	
	}
//	public void mCalculateScore3(LinkedHashMap<Integer, Quartet> quartetMap, HashMap<Integer, SVD_Log> svdTableMap1, int prevS, int prevV, int prevScore) {
//
//		
//	    int satisfied = sumOfSatOfSVDmap, violated = sumOfVatOfSVDmap;//, d = 0;
//	
//	    for (int id : svdTableMap1.keySet()) {
//	    	
//	    	if (!svdTableMap.containsKey(id)) {
//				continue;
//			}
//	    	
//	    	Quartet q = quartetMap.get(id);
//    		char qStat  = q.mCheckQuartet(name);
//    		int[] sv = svScore(q, qStat);//sv[0] = s, sv[1] = 1;
//    		SVD_Log svd = svdTableMap.get(id);
//    		satisfied = satisfied + sv[0] - svd.getSat();
//    		violated = violated + sv[1] - svd.getVat();
//            svd.setSVD(sv[0], sv[1], qStat);
//            
//		}
//	    relaventQuartetIDOfCorrespondingMovedTaxa.clear();
//	    sumOfSatOfSVDmap = satisfied;
//	    sumOfVatOfSVDmap = violated;
//        sat = prevS + satisfied;
//        vat = prevV + violated;
//        val = (sat - vat) - prevScore;
//
//	
//	}
	public int[] svScore(Quartet q, char qStat ) {
		char c = q.getStatus();
		int[] sv = {0,0}; // sv[0] for satisfied, sv[1] for violated


        if(c=='s' && qStat == 'v') { sv[0] = - q.getQFrequency(); sv[1] =  q.getQFrequency();} // s v

        else if(c=='s' && qStat == 'd'){ sv[0] = - q.getQFrequency();}// d =  q.getQFrequency();} // s d

        else if(c=='v' && qStat == 's'){sv[1] = - q.getQFrequency(); sv[0] = q.getQFrequency();} // v s

        else if(c=='v' && qStat == 'd'){sv[1] = - q.getQFrequency();}//d = q.getQFrequency();}  // v d

        else if(c=='d' && qStat == 'v'){sv[1] = q.getQFrequency();} // d v

        else if(c=='d' && qStat == 's'){sv[0] = q.getQFrequency();} // d s

        else if(qStat == 'b')
        {
            if(c=='s') { sv[0] = - q.getQFrequency();}
            else if(c=='v') {sv[1] = - q.getQFrequency();}
            //else if(c=='d') { d = - q.getQFrequency();}

        }
        else if(c=='b')
        {
            if(qStat == 's') { sv[0] = q.getQFrequency();}//s
            else if(qStat == 'v') {sv[1] = q.getQFrequency();}//v
            //else if(qStat == 'd') { d = q.getQFrequency();}
        }
		return sv;
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