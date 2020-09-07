package qfm_ad;

import java.util.Objects;

public class Quartet {
	private Taxa t1;
	private Taxa t2;
	private Taxa t3;
	private Taxa t4;
	private char status;
	//private char status;
	private int qFrequency;
	private boolean increaseFrequency;
	private int quartetID;
	public Quartet(Taxa t1, Taxa t2, Taxa t3, Taxa t4) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
		this.qFrequency = 1;
		this.increaseFrequency = true;
		//this.status = '';
		this.quartetID = -1;
	}
	public Taxa getT1() {
		return t1;
	}

	public Taxa getT2() {
		return t2;
	}

	public Taxa getT3() {
		return t3;
	}

	public Taxa getT4() {
		return t4;
	}
	
	public void setT1(Taxa t1) {
		this.t1 = t1;
	}
	public void setT2(Taxa t2) {
		this.t2 = t2;
	}
	public void setT3(Taxa t3) {
		this.t3 = t3;
	}
	public void setT4(Taxa t4) {
		this.t4 = t4;
	}
	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	

	public int getQuartetID() {
		return quartetID;
	}
	public void setQuartetID(int quartetID) {
		this.quartetID = quartetID;
	}
//	public char getStatus() {
//		return status;
//	}
//	public void setStatus(char status) {
//		this.status = status;
//	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char initStatus) {
		this.status = initStatus;
	}
	public boolean isIncreaseFrequency() {
		return increaseFrequency;
	}
	public void setIncreaseFrequency(boolean increaseFrequency) {
		this.increaseFrequency = increaseFrequency;
	}
	public void fillUpRelaventQuartetIDOfCorrespondingMovedTaxa() {
		t1.relaventQuartetIDOfCorrespondingMovedTaxa.add(quartetID);
		t2.relaventQuartetIDOfCorrespondingMovedTaxa.add(quartetID);
		t3.relaventQuartetIDOfCorrespondingMovedTaxa.add(quartetID);
		t4.relaventQuartetIDOfCorrespondingMovedTaxa.add(quartetID);
	}
	public void fillUpInitialRelaventQuartetID() {
		t1.initialRelaventQuartetID.add(quartetID);
		t2.initialRelaventQuartetID.add(quartetID);
		t3.initialRelaventQuartetID.add(quartetID);
		t4.initialRelaventQuartetID.add(quartetID);
	}
	public char mCheckQuartet(String tempTaxa) {
		//int a = 0, b = 0, c = 0, d = 0;
		char quartet_state;

		int a = t1.getPartition();
		int b = t2.getPartition();
		int c = t3.getPartition();
		int d = t4.getPartition();

		if(tempTaxa.contentEquals(t1.getName())) a = 1 - a;
	    else if(tempTaxa.contentEquals(t2.getName())) b = 1-b;
	    else if(tempTaxa.contentEquals(t3.getName())) c = 1-c;
	    else if(tempTaxa.contentEquals(t4.getName())) d = 1-d;

		
		if (a==b && c==d && b==c) // totally on one side
	    {	
			quartet_state = 'b';
	    }
	    else if( a==b && c==d) //satisfied
	    {
	    	quartet_state = 's';
	    }
	    else if ((a==c && b==d) || (a==d && b==c)) // violated
	    {
	    	quartet_state = 'v';

	    }
	    else //deffered
	    {
	    	quartet_state = 'd';
	    }

	    //q.setStatus(qstat);
		return quartet_state;

	}
	
//	@Override
//	public int hashCode() {
//		return Objects.hash(t1, t2, t3, t4);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			//increase_count();
//			//System.out.println("increased");
//			return true;
//		}
//		if (!(obj instanceof Quartet))
//			return false;
//		Quartet other = (Quartet) obj;
//		if (Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2) && Objects.equals(t3, other.t3)
//				&& Objects.equals(t4, other.t4)) {
//			if (other.isIncreaseFrequency()) {
//				other.setQFrequency(other.getQFrequency()+1);
//			}
//			return true;
//		} else {
//			return false;
//
//		}
//	
//	}
//	
	/////////////////////////////////////////

	@Override
	public int hashCode() {
		return Objects.hash(quartetID, t1, t2, t3, t4);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Quartet))
			return false;
		Quartet other = (Quartet) obj;
		if (quartetID == other.quartetID && Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2)
				&& Objects.equals(t3, other.t3) && Objects.equals(t4, other.t4)) {
			if (other.isIncreaseFrequency()) {
				other.setQFrequency(other.getQFrequency()+1);
			}
			return true;
		} else {
			return false;

		}
		
	}
	
	
	

}