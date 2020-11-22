package qfm_ad;

import java.util.Objects;

public class Quartet {
	public Taxa t1;
	public Taxa t2;
	public Taxa t3;
	public Taxa t4;
	public char status;
	//private char status;
	private int qFrequency;
	private boolean increaseFrequency;
	//private int quartetID;
	public Quartet(Taxa t1, Taxa t2, Taxa t3, Taxa t4) {
		this(t1, t2, t3, t4, 1);
	}
	
	public Quartet(Taxa t1, Taxa t2, Taxa t3, Taxa t4, int qFrequency) {
		
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
		this.qFrequency = qFrequency;
		this.increaseFrequency = true;
		//this.quartetID = -1;
	}

	
	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	

	
	public boolean isIncreaseFrequency() {
		return increaseFrequency;
	}
	public void setIncreaseFrequency(boolean increaseFrequency) {
		this.increaseFrequency = increaseFrequency;
	}

	public void fillUpSVDmapInitiallyWithRelaventQIDandScore(int quartetID) {
		int a = t1.getPartition();
		int b = t2.getPartition();
		int c = t3.getPartition();
		int d = t4.getPartition();
		this.status = Routines.iCheckQuartet2(a, b, c, d);
		
		if (this.status=='d') {
			this.t1.fillUpSVDmap(quartetID, statusWRTmovingTaxaI(1-a,b,c,d));
			this.t2.fillUpSVDmap(quartetID, statusWRTmovingTaxaI(a,1-b,c,d));
			this.t3.fillUpSVDmap(quartetID, statusWRTmovingTaxaI(a,b,1-c,d));
			this.t4.fillUpSVDmap(quartetID, statusWRTmovingTaxaI(a,b,c,1-d));
  
		} else {
			int[] sv = {0,0};
		    if(this.status=='s'){ sv[0] = - this.qFrequency;}// d =  q.getQFrequency();} // s d
	
	        else if(this.status=='v'){sv[1] = - this.qFrequency;}//d = q.getQFrequency();}  // v d

			this.t1.fillUpSVDmap(quartetID, new SVD_Log(sv[0], sv[1], 'd'));
			this.t2.fillUpSVDmap(quartetID, new SVD_Log(sv[0], sv[1], 'd'));
			this.t3.fillUpSVDmap(quartetID, new SVD_Log(sv[0], sv[1], 'd'));
			this.t4.fillUpSVDmap(quartetID, new SVD_Log(sv[0], sv[1], 'd'));
			
		}
		
		


	
	}
//	public SVD_Log cq() {
//		int[] sv = {0,0};
//	    if(this.status=='s'){ sv[0] = - this.qFrequency;}// d =  q.getQFrequency();} // s d
//
//        else if(this.status=='v'){sv[1] = - this.qFrequency;}//d = q.getQFrequency();}  // v d
//	    return new SVD_Log(sv[0], sv[1], 'd');
//	}
	public void updateSVD_for_moving_taxa(int quartetID) {
		int a = t1.getPartition();
		int b = t2.getPartition();
		int c = t3.getPartition();
		int d = t4.getPartition();
		
		
		if (!t1.locked) {
			
			t1.updateSVDmap(quartetID, statusWRTmovingTaxa(1-a,b,c,d));
		}
		if (!t2.locked) {
			t2.updateSVDmap(quartetID, statusWRTmovingTaxa(a,1-b,c,d));
		}
		if (!t3.locked) {
			t3.updateSVDmap(quartetID, statusWRTmovingTaxa(a,b,1-c,d));
		}
		if (!t4.locked) {
			t4.updateSVDmap(quartetID, statusWRTmovingTaxa(a,b,c,1-d));
		}
	
	}
	public SVD_Log statusWRTmovingTaxa(int a, int b, int c, int d) {

    	char qStat; 
    	int[] sv = {0,0};
    	//if quartet status is satisfied, violated or b, then moving taxa from its current partition will must make q status deffered
    	//But if quartet status is deffered, then we must have to check its status with respect to moving taxa
    	if (this.status=='d') {
    		qStat  = Routines.iCheckQuartet2(a, b, c, d);
    		if(qStat == 'v'){sv[1] = this.qFrequency;} // d v

            else if(qStat == 's'){sv[0] = this.qFrequency;} // d s
  
		} else {
			
			qStat  = 'd';
		    if(this.status=='s'){ sv[0] = - this.qFrequency;}// d =  q.getQFrequency();} // s d

	        else if(this.status=='v'){sv[1] = - this.qFrequency;}//d = q.getQFrequency();}  // v d
			
		}
    	return new SVD_Log(sv[0],sv[1],qStat);
	}
	
	
	public SVD_Log statusWRTmovingTaxaI(int a, int b, int c, int d) {

    	 
    	int[] sv = {0,0};
    	//if quartet status is satisfied, violated or b, then moving taxa from its current partition will must make q status deffered
    	//But if quartet status is deffered, then we must have to check its status with respect to moving taxa
    
    	char qStat  = Routines.iCheckQuartet2(a, b, c, d);
		if(qStat == 'v'){sv[1] = this.qFrequency;} // d v

        else if(qStat == 's'){sv[0] = this.qFrequency;} // d s
  
    	return new SVD_Log(sv[0],sv[1],qStat);
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(t1, t2, t3, t4);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Quartet))
			return false;
		Quartet other = (Quartet) obj;
		if (Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2)
				&& Objects.equals(t3, other.t3) && Objects.equals(t4, other.t4)) {
			if (other.isIncreaseFrequency()) {
				//other.setQFrequency(other.getQFrequency()+1);
				other.setQFrequency(other.getQFrequency()+this.qFrequency);
			}
			return true;
		} else {
			return false;

		}
		
	}
	
	
	

}