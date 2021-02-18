package qfm_ad;



import java.util.Objects;

public class Quartet {
	public Taxa t1;
	public Taxa t2;
	public Taxa t3;
	public Taxa t4;
	public char status;
	public char t1_status;
	public char t2_status;
	public char t3_status;
	public char t4_status;
	//private char status;
	private int qFrequency;
	//private boolean increaseFrequency;
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
		//this.increaseFrequency = true;
		//this.quartetID = -1;
	}

	
	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	

	
//	public boolean isIncreaseFrequency() {
//		return increaseFrequency;
//	}
//	public void setIncreaseFrequency(boolean increaseFrequency) {
//		this.increaseFrequency = increaseFrequency;
//	}
	public void initiallyFillUpRelaventQIDs(int QID) {
		t1.relaventQIDs.add(QID);
		t2.relaventQIDs.add(QID);
		t3.relaventQIDs.add(QID);
		t4.relaventQIDs.add(QID);
	}
	public void initialSatVatCalculation() {
		int a = t1.getPartition();
		int b = t2.getPartition();
		int c = t3.getPartition();
		int d = t4.getPartition();
		this.status = Routines.iCheckQuartet2(a, b, c, d);
		
		if (this.status=='d') {
			t1_status = statusWRTmovingTaxaI(t1, 1-a,b,c,d);
			t2_status = statusWRTmovingTaxaI(t2, a,1-b,c,d);
			t3_status = statusWRTmovingTaxaI(t3, a,b,1-c,d);
			t4_status = statusWRTmovingTaxaI(t4, a,b,c,1-d);
  
		} else {
		    if(this.status=='s'){ 
		    	t1.addSatScoresWRTmovingTaxa(-this.qFrequency);
		    	t2.addSatScoresWRTmovingTaxa(-this.qFrequency);
		    	t3.addSatScoresWRTmovingTaxa(-this.qFrequency);
		    	t4.addSatScoresWRTmovingTaxa(-this.qFrequency);
		    }// d =  q.getQFrequency();} // s d
	
	        else if(this.status=='v'){
		    	t1.addVatScoresWRTmovingTaxa(-this.qFrequency);
		    	t2.addVatScoresWRTmovingTaxa(-this.qFrequency);
		    	t3.addVatScoresWRTmovingTaxa(-this.qFrequency);
		    	t4.addVatScoresWRTmovingTaxa(-this.qFrequency);
	        }//d = q.getQFrequency();}  // v d

			t1_status = 'd';
			t2_status = 'd';
			t3_status = 'd';
			t4_status = 'd';

		}
		
		

	}
	public void updateSatVatCalculation() {
		int a = t1.getPartition();
		int b = t2.getPartition();
		int c = t3.getPartition();
		int d = t4.getPartition();
	
		if (this.status=='d') {

			this.status = Routines.iCheckQuartet2(a, b, c, d);
			//System.out.println("q status "+ this.status);
			if (!t1.locked) {
				//System.out.println("prev t1_status = "+t1_status);
				statusComparison(t1, t1_status, -qFrequency);
				t1_status = statusWRTmovingTaxa(t1, 1-a,b,c,d);
				//System.out.println("new t1_status = "+t1_status);
			}
			if (!t2.locked) {
				statusComparison(t2, t2_status, -qFrequency);
				t2_status = statusWRTmovingTaxa(t2, a,1-b,c,d);
			}
			if (!t3.locked) {
				statusComparison(t3, t3_status, -qFrequency);
				t3_status = statusWRTmovingTaxa(t3, a,b,1-c,d);
			}
			if (!t4.locked) {
				statusComparison(t4, t4_status, -qFrequency);
				t4_status = statusWRTmovingTaxa(t4, a,b,c,1-d);
			}

  
		} else if(this.status=='s'){ 
			this.status = 'd';
	    	if (!t1.locked) {
	    		t1.addSatScoresWRTmovingTaxa(this.qFrequency);
	    		t1_status = statusWRTmovingTaxaI(t1, 1-a,b,c,d);
	    	}
	    	if (!t2.locked) {
	    		t2.addSatScoresWRTmovingTaxa(this.qFrequency);
	    		t2_status = statusWRTmovingTaxaI(t2, a,1-b,c,d);
	    	}
	    	if (!t3.locked) {
	    		t3.addSatScoresWRTmovingTaxa(this.qFrequency);
	    		t3_status = statusWRTmovingTaxaI(t3, a,b,1-c,d);
	    	}
	    	if (!t4.locked) {
	    		t4.addSatScoresWRTmovingTaxa(this.qFrequency);
	    		t4_status = statusWRTmovingTaxaI(t4, a,b,c,1-d);
	    	}
		    }// d =  q.getQFrequency();} // s d
	
        else if(this.status=='v'){
        	this.status = 'd';
	    	if (!t1.locked) {
	    		t1.addVatScoresWRTmovingTaxa(this.qFrequency);
	    		t1_status = statusWRTmovingTaxaI(t1, 1-a,b,c,d);
	    	}
	    	if (!t2.locked) {
	    		t2.addVatScoresWRTmovingTaxa(this.qFrequency);
	    		t2_status = statusWRTmovingTaxaI(t2, a,1-b,c,d);
	    	}
	    	if (!t3.locked) {
	    		t3.addVatScoresWRTmovingTaxa(this.qFrequency);
	    		t3_status = statusWRTmovingTaxaI(t3, a,b,1-c,d);
	    	}
	    	if (!t4.locked) {
	    		t4.addVatScoresWRTmovingTaxa(this.qFrequency);
	    		t4_status = statusWRTmovingTaxaI(t4, a,b,c,1-d);
	    	}

        }//d = q.getQFrequency();}  // v d
		    

        else if (this.status=='b') {

			this.status = 'd';
			//System.out.println("q status "+ this.status);
			if (!t1.locked) {
				//System.out.println("prev t1_status = "+t1_status);
				//statusComparison(t1, t1_status, -qFrequency);
				t1_status = statusWRTmovingTaxaI(t1, 1-a,b,c,d);
				//System.out.println("new t1_status = "+t1_status);
			}
			if (!t2.locked) {
				//statusComparison(t2, t2_status, -qFrequency);
				t2_status = statusWRTmovingTaxaI(t2, a,1-b,c,d);
			}
			if (!t3.locked) {
				//statusComparison(t3, t3_status, -qFrequency);
				t3_status = statusWRTmovingTaxaI(t3, a,b,1-c,d);
			}
			if (!t4.locked) {
				//statusComparison(t4, t4_status, -qFrequency);
				t4_status = statusWRTmovingTaxaI(t4, a,b,c,1-d);
			}

	  
		} 
		    

		
		
	}
//	public void afterMovingTaxaQStatusCalculator() {
//		subtractPreviousScores();
//	System.out.println("After Subtraction ");
//		int a = t1.getPartition();
//		int b = t2.getPartition();
//		int c = t3.getPartition();
//		int d = t4.getPartition();
//	
//		
//		this.status = Routines.iCheckQuartet2(a, b, c, d);
//
//		if (!t1.locked) {
//			
//			t1_status = statusWRTmovingTaxa(t1, 1-a,b,c,d);
//		}
//		if (!t2.locked) {
//			t2_status = statusWRTmovingTaxa(t2, a,1-b,c,d);
//		}
//		if (!t3.locked) {
//			t3_status = statusWRTmovingTaxa(t3, a,b,1-c,d);
//		}
//		if (!t4.locked) {
//			t4_status = statusWRTmovingTaxa(t4, a,b,c,1-d);
//		}
//  
//
//	
//	}
//	private void subtractPreviousScores() {
//		
//		if (this.status=='d') {
//			statusComparison(t1, t1_status, -qFrequency);
//			statusComparison(t2, t2_status, -qFrequency);
//			statusComparison(t3, t3_status, -qFrequency);
//			statusComparison(t4, t4_status, -qFrequency);
//  
//		} else {
//		    if(this.status=='s'){ 
//		    	t1.addSatScoresWRTmovingTaxa(this.qFrequency);
//		    	t2.addSatScoresWRTmovingTaxa(this.qFrequency);
//		    	t3.addSatScoresWRTmovingTaxa(this.qFrequency);
//		    	t4.addSatScoresWRTmovingTaxa(this.qFrequency);
//		    }// d =  q.getQFrequency();} // s d
//	
//	        else if(this.status=='v'){
//		    	t1.addVatScoresWRTmovingTaxa(this.qFrequency);
//		    	t2.addVatScoresWRTmovingTaxa(this.qFrequency);
//		    	t3.addVatScoresWRTmovingTaxa(this.qFrequency);
//		    	t4.addVatScoresWRTmovingTaxa(this.qFrequency);
//	        }//d = q.getQFrequency();}  // v d
//
//
//		}
//		
//		
//		
//	}

	public char statusWRTmovingTaxaI(Taxa taxa, int a, int b, int c, int d) {
    	//if quartet status is satisfied, violated or b, then moving taxa from its current partition will must make q status deffered
    	//But if quartet status is deffered, then we must have to check its status with respect to moving taxa
    
    	char qStat  = Routines.iCheckQuartet2(a, b, c, d);
    	statusComparison(taxa, qStat, this.qFrequency);
    	return qStat;
	}
	public char statusWRTmovingTaxa(Taxa taxa, int a, int b, int c, int d) {

    	char qStat; 
    	//if quartet status is satisfied, violated or b, then moving taxa from its current partition will must make q status deffered
    	//But if quartet status is deffered, then we must have to check its status with respect to moving taxa
    	if (this.status=='d') {
    		qStat  = statusWRTmovingTaxaI(taxa, a, b, c, d);
    		
//    		qStat  = Routines.iCheckQuartet2(a, b, c, d);
//    		if(qStat == 'v'){sv[1] = this.qFrequency;} // d v
//
//            else if(qStat == 's'){sv[0] = this.qFrequency;} // d s
		} else {
			qStat  = 'd';
			statusComparison(taxa, this.status, -qFrequency);
			
		}
    	return qStat;
	}
	public void statusComparison(Taxa taxa, char qStat, int frequency) {
		if(qStat == 'v'){taxa.addVatScoresWRTmovingTaxa(frequency);} // d v

        else if(qStat == 's'){taxa.addSatScoresWRTmovingTaxa(frequency);} // d s
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
			//if (other.isIncreaseFrequency()) {
				//other.setQFrequency(other.getQFrequency()+1);
				other.setQFrequency(other.getQFrequency()+this.qFrequency);
			//}
			return true;
		} else {
			return false;

		}
		
	}
	
	
	

}