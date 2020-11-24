package qfm_ad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.stream.Collectors;

import phylonet.tree.io.ParseException;
import phylonet.tree.model.sti.STITree;



public class Routines {
	
	
	

	public static String readQuartetQMC(String fileName) { // count will be done at the time of reading

		
		Map<String, Taxa> taxList = new HashMap<String, Taxa>();
		
		
		
		
		LinkedHashSet<Quartet> quartetList = new LinkedHashSet<Quartet>();
		//double startTime = System.currentTimeMillis();
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))){
			//int count=0, qc = 0;//count-> only counts unique quartet. qc-> counts all quartet
			int qc = 0;
			while (scanner.hasNext()) {
				
				String singleQuartet = scanner.next();	
				//String[] qq = singleQuartet.split(",|\\|");/// for quartet format q1,q2|q3,q4
				String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight
				//Taxa t1, t2, t3, t4;
				
				Taxa[] t = new Taxa[4];
				for (int i = 0; i < 4; i++) {
//					t[i] = new Taxa(qq[i]);
//					//taxaList.add(t[i]);
//					///////////recent change
//					if (!taxaList.add(t[i])) {
//						final String taxaName = t[i].name;
//						t[i] = taxaList.stream().filter(j -> j.name.contentEquals(taxaName)).findAny().get();
//					}
					if (taxList.containsKey(qq[i])) {
						t[i] = taxList.get(qq[i]);
					} else {
						t[i] = new Taxa(qq[i]);
						taxList.put(qq[i], t[i]);
					}
					////recent change/////////
				}

				
//				if(quartetList.contains(new Quartet(t1, t2, t3, t4))) {
//					qc++;
//				}else 
				if (quartetList.contains(new Quartet(t[1], t[0], t[2], t[3]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[0], t[1], t[3], t[2]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[1], t[0], t[3], t[2]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[2], t[3], t[0], t[1]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[2], t[3], t[1], t[0]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[3], t[2], t[0], t[1]))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t[3], t[2], t[1], t[0]))) {
					qc++;
				}else {
					quartetList.add(new Quartet(t[0], t[1], t[2], t[3]));
					//count++;
					qc++;
					//System.out.println("qc = "+qc);
				}
				
								
				
				
				
			}
			System.out.println("number of quartet = "+ qc);
			System.out.println("number of unique quartet = "+quartetList.size());
			System.out.println("number of Taxa = "+taxList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Quartet> qr = new ArrayList<Quartet>(quartetList);
		quartetList.clear();
		qr.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));
//		LinkedHashSet<Quartet> countedSortedQL = new LinkedHashSet<Quartet>(quartetList.stream()
//				.sorted(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder())).collect(Collectors.toList()));
		
		LinkedHashMap<Integer, Quartet> quartetMap = new LinkedHashMap<Integer, Quartet>();
		int qID = 0;
		for (Quartet quartet : qr) {
			qID++;
			quartet.setIncreaseFrequency(false);
			//quartet.setQuartetID(qID);;
			quartetMap.put(qID, quartet);
		}
		qr.clear();
		
		
		//long startTime1 = System.currentTimeMillis();
		LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>(taxList.values());
		String s = SQP(quartetMap, taxaList, 1000, 0);
		//long estimatedTime1 = System.currentTimeMillis() - startTime1;
		//System.out.println("SQP function Total Time : "+ estimatedTime1 + " miliseconds");
//		if (s != null) {
//			s = s.replace("(O,", "(0,");
//			s = s.replace(",O,", ",0,");
//			s = s.replace(",O)", ",0)");
//			//s = "("+s+")"+";"; //for rerooting with jar, this line is not needed
//		}else
//			s = "null";
		if (s == null) {
			s = "null";
		}
		
		return s;
		//return null;
	
		
	}
	
	public static String newickQuartetWeightAsFrequency(String fileName) {
		//Quartet Format: ((a,b),(c,d)); Frequency
		// If frequency is already counted or weight represents frequency 
		//and quartet is in newick format i.e ((a,b),(c,d)); Frequency
		//Then use this function.
		
		
		ArrayList<Quartet> qr = new ArrayList<Quartet>();
		Map<String, Taxa> taxList = new HashMap<String, Taxa>();
			
		//LinkedHashSet<Quartet> quartetList = new LinkedHashSet<Quartet>();
		long startTime = System.currentTimeMillis();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
			//scanner = new Scanner(new BufferedReader(new FileReader("sample.txt")));
			//int tcount=0, qcount = 0;
			String loc;// int qc = 0;
			while ((loc = br.readLine())!=null) {
				if(loc.equals("")) {
					//remove extra new line/white space
					continue;
				}
				//System.out.println(loc);
				String[] qq = loc.split("\\(\\(|,|\\),\\(|\\)\\)|; ");
			
				int frequency = Integer.parseInt(qq[6]); //for qfm, frequency means number of quartet. Frequency must be integer
				Taxa[] t = new Taxa[4];
//				for (int i = 0; i < 4; i++) {
//					t[i] = new Taxa(qq[i+1]);
//					//taxaList.add(t[i]);
//					///////////recent change
//					if (!taxaList.add(t[i])) {
//						final String taxaName = t[i].name;
//						t[i] = taxaList.stream().filter(j -> j.name.contentEquals(taxaName)).findAny().get();
//					}
//				}
				for (int i = 0; i < 4; i++) {
					int newIndex = i+1;
					if (taxList.containsKey(qq[newIndex])) {
						t[i] = taxList.get(qq[newIndex]);
					} else {
						t[i] = new Taxa(qq[newIndex]);
						taxList.put(qq[newIndex], t[i]);
					}
					////recent change/////////
				}

				
//				if(quartetList.contains(new Quartet(t1, t2, t3, t4))) {
//					qc++;
//				}else 
//				if (quartetList.contains(new Quartet(t[1], t[0], t[2], t[3], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[0], t[1], t[3], t[2], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[1], t[0], t[3], t[2], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[2], t[3], t[0], t[1], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[2], t[3], t[1], t[0], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[3], t[2], t[0], t[1], frequency))) {
//					qc++;
//				}else if (quartetList.contains(new Quartet(t[3], t[2], t[1], t[0], frequency))) {
//					qc++;
//				}else {
//					quartetList.add(new Quartet(t[0], t[1], t[2], t[3], frequency));
					//count++;
//					qc++;
//				}
//				
				
				qr.add(new Quartet(t[0], t[1], t[2], t[3], frequency));
			}	
				
			//System.out.println("number of quartet = "+ qc);
			System.out.println("number of quartet = "+qr.size());
			System.out.println("number of Taxa = "+taxList.size());
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Quartet Reading : "+ estimatedTime/1000 + " seconds");
		//printQuartet(quartetList);
//		ArrayList<Quartet> qr = new ArrayList<Quartet>(quartetList);
//		quartetList.clear();
		qr.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));
		long sortingTime = System.currentTimeMillis() - startTime - estimatedTime;
		System.out.println("Sorting Time : "+ sortingTime/1000 + " seconds");
		
		LinkedHashMap<Integer, Quartet> quartetMap = new LinkedHashMap<Integer, Quartet>();
		int qID = 0;
		for (Quartet quartet : qr) {
			qID++;
			quartet.setIncreaseFrequency(false);
			//quartet.setQuartetID(qID);;
			quartetMap.put(qID, quartet);
		}
		qr.clear();
		
		LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>(taxList.values());
		String s = SQP(quartetMap, taxaList, 1000, 0);

		if (s == null) {
			s = "null";
		}
		
		return s;

		
	}
	
	public static String SQP(LinkedHashMap<Integer, Quartet> quartetMap, LinkedHashSet<Taxa> taxaList, int extraTaxa, int partSatCount) {
		//long t0 = System.currentTimeMillis();
		int taxacount = taxaList.size();
//		System.out.println("******************SQP*****************");
//		System.out.println("Taxa Count = "+ taxacount);
//		System.out.println("Quartet Count = "+ quartetList.size());
		String s , extra = "extra";

		
		if (taxacount == 0) {
			s = ("");
			
			return s;
		}
		//quartetList.size() == 0 diye check korte hobe
		if (quartetMap.isEmpty() || taxacount <3) {
//			System.out.println("Quartet list is empty");
			s = depthOneTree(taxaList);
		} else {
			MultiReturnType mrt = FM(taxaList, quartetMap);
			LinkedHashSet<Taxa> partA = mrt.getPartA();
			LinkedHashSet<Taxa> partB = mrt.getPartB();
			
//			System.out.println("****************************SQP****************");
//			System.out.println("*****************PartA**************************");
//			printTaxa(partA);
//			System.out.println("*****************PartB**************************");
//			printTaxa(partB);
			//printQuartet(quartetList);
			int partSat = countSatisfiedQuartets(quartetMap); // It returns # of satisfied quartets
			//System.out.println("partSat = "+ partSat);

			if(partSat==0){

	            partSatCount++;
	            if(partSatCount>100) //mc dependant step. No. of sat quartets = 0 in successive 20 iterations
	            {	
	            	//System.out.println("partSatCount value = "+ partSatCount);
	            	partSatCount = 0;
	                s = depthOneTree(taxaList);
	                //long t2 = System.currentTimeMillis() - t0;
	        		//System.out.println(taxacount+","+ quartetList.size()+","+t2);
	                return s;
	            }
	        }
	        else partSatCount=0;
	        
			extra = "extra"+ extraTaxa;
//	        System.out.println("extra = "+ extra);
	        extraTaxa++;
	        Taxa extraA = new Taxa(extra, 0);
	        Taxa extraB = new Taxa(extra, 1);
	        partA.add(extraA);// add extra taxa to partition A
	        partB.add(extraB);// add extra taxa to parttion B
//	        System.out.println("**************After Adding extra taxa****************");
//			System.out.println("*****************PartA**************************");
//			printTaxa(partA);
//			System.out.println("*****************PartB**************************");
//			printTaxa(partB);
	        LinkedHashMap<Integer, Quartet> quartetA = new LinkedHashMap<Integer, Quartet>();
	        LinkedHashMap<Integer, Quartet> quartetB = new LinkedHashMap<Integer, Quartet>();
	        

	        
	        int numOfBDQ = 0; //number of b and deferred quartet

	        for (Quartet q : quartetMap.values()) {
	        	char c = q.status;

				
				if (c == 'b' || c == 'd' ) {
					numOfBDQ++;
					//q.setQuartetID(numOfBDQ);
					if (c == 'b') {
						//numOfB++;
						//if (partA.contains(q.getT1())) {
						if (q.t1.getPartition() == 0) {
							quartetA.put(numOfBDQ, q);
							
						} else {
							quartetB.put(numOfBDQ, q);

						}
					} else {
						//numOfD++;
						//int dcount = 0;
						int dcount = q.t1.getPartition() + q.t2.getPartition() + q.t3.getPartition();
//						if (partA.contains(q.getT1()))dcount++;
//						if (partA.contains(q.getT2()))dcount++;
//						if (partA.contains(q.getT3()))dcount++;
						//if (partA.contains(q.getT4()))dcount++;
//						System.out.println("Quartet = "+ q.getT1().getName()+","+q.getT2().getName()
//								+"|"+q.getT3().getName()+","+q.getT4().getName()+":"+q.getQFrequency()+"->"+q.getStatus());
//						System.out.println("dcount = "+ dcount);
						
						if (dcount > 1) {
							if(q.t1.getPartition() == 0)q.t1 =extraB;
							else if(q.t2.getPartition() == 0)q.t2 = extraB;
							else if(q.t3.getPartition() == 0)q.t3 = extraB;
							else q.t4 = extraB;
							quartetB.put(numOfBDQ, q);

						} else {
							if(q.t1.getPartition() == 1)q.t1 = extraA;
							else if(q.t2.getPartition() == 1)q.t2 = extraA;
							else if(q.t3.getPartition() == 1)q.t3 = extraA;
							else q.t4 = extraA;
							quartetA.put(numOfBDQ, q);


						}

					}
				}
			}
	       
	        
	       // System.out.println("One divide step complete");
	        quartetMap.clear();
	        if (extraTaxa == 1001) {
//	        //if (estimatedTime > 50000) {//if estimatedTime > 50000 miliseconds
//	        //if (estimatedTime > 40000) {//if estimatedTime > 40000 miliseconds
//	        //if (estimatedTime > 30000) {//if estimatedTime > 30000 miliseconds
//	        //if (estimatedTime > 20000) {//if estimatedTime > 20000 miliseconds
//	        //if (estimatedTime > 10000) {//if estimatedTime > 10000 miliseconds
//	        //if (estimatedTime > 5000) {//if estimatedTime > 5000 miliseconds
//	        //if (estimatedTime > 1000) {//if estimatedTime > 1000 miliseconds
//	        //if (estimatedTime > 500) {//if estimatedTime > 500 miliseconds
//	        //if (estimatedTime > 100) {//if estimatedTime > 100 miliseconds
	        	final int ext = extraTaxa, psc = partSatCount;
	   	        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> SQP(quartetA, partA, ext, psc));
	   	        String s2 = SQP(quartetB, partB, extraTaxa, partSatCount);
	   	        String s1 = null;
	   			try {
	   				s1 = cf.get();
	   			} catch (InterruptedException e) {
	   				//e.printStackTrace();
	   			} catch (ExecutionException e) {
	   				//e.printStackTrace();
	   			}

	   			//s = merge(s1,s2,extra);
	   		    s = mergeUsingJAR(s1,s2,extra);
			} else {
				String s1 = SQP(quartetA, partA, extraTaxa, partSatCount);
		        String s2 = SQP(quartetB, partB, extraTaxa, partSatCount);  
		       // s = merge(s1,s2,extra);
		        //s = mergeUnrootedTrees(s1,s2,extra);
		        s = mergeUsingJAR(s1,s2,extra);
		        
			}
	       
//	        //String s1 = SQP(quartetA, partA, extraTaxa, partSatCount);
//	        String s2 = SQP(quartetB, partB, extraTaxa, partSatCount);
//	        s = merge(s1,s2,extra);
	        //System.out.println("Merged Tree = "+s);

	       

	    }

	
		//long t2 = System.currentTimeMillis() - t0;
		//System.out.println(taxacount+","+ quartetList.size()+","+t2);
		return s;
		//return null;
	
	}

	private static String merge(String s1, String s2, String extra) {
		
		s1 = reroot(s1, extra);
		s2 = reroot(s2, extra);
		if (s1 != null && s2 != null) {
			return s1+","+s2;
		} else if (s1 == null && s2 != null) {
			return s2;
		} else if (s1 != null && s2 == null) {
			return s1;
		} else
			return null;
	}

    
	private static String reroot(String s, String extra) {
		

		//String fileName = "reroot.txt";
		boolean brkt;
		brkt = bracket(s);
		if(!brkt)
	    {
	        s = "(" + s + ")";

	    }
		//perl script consider 0 as false. so we have to replace zero
		s = s.replace("(0,", "(O,");
		s = s.replace(",0,", ",O,");
		s = s.replace(",0)", ",O)");
		//s = '"'+s+";"+'"';
		//System.out.println("**************** s= "+s);
		//String cmd = "perl reroot_tree_new.pl -t "+ s+";"+ " -r "+extra+ " -o "+ fileName;
		//System.out.println(cmd);
		//synchronized block
		String fileName = "qfm11011.tmp";
//		System.out.println("s = "+s);
//		System.out.println("extra = "+ extra);
		String ss = null;
	
		
		synchronized (fileName) {
			try {
				String cmd = "perl reroot_tree_new.pl -t "+ s +";"+ " -r "+extra+ " -o "+ fileName;
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				p.destroy();
			} catch (Exception e) {
				
				e.printStackTrace();
				System.out.println("Problem in cmd");
			}
			//reading from temp file
			
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
				ss = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		
		///////////////////
		/*File tempFile = null;

		try {
			tempFile = File.createTempFile("prefix-", "-suffix");
			String cmd = "perl reroot_tree_new.pl -t "+ s +";"+ " -r "+extra+ " -o "+ tempFile;
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Problem in cmd");
		}
		//reading from temp file
		String ss = null;
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile))){
			ss = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//deleting temp file
		try {
			if(!tempFile.delete())
				tempFile.deleteOnExit();
		} catch (Exception e) {
			
		}
		*/
		if (ss != null) {
			ss = ss.replace(":", "");
			ss = ss.replace(";", "");
			int pos1 = ss.indexOf(extra);
			ss = ss.replace(extra, "");
//			System.out.println("After removing extra s= "+s);
//			System.out.println("pos1 = "+pos1);

			int start = 0, i= 0, ob = 0, end = 0, cb = 0; String mystring; boolean removebr = false;
			if(ss.charAt(pos1-1) =='('&& ss.charAt(pos1) ==',')
		     {
				
				try{

					ss = ss.substring(0, pos1)+ss.substring(pos1+1);
					 start = pos1-1;
//					System.out.println("inside s= "+ s+ " and start = "+start );

		           
		            i = start; ob = 0;
		            while(true)
		            {
		                if(ss.charAt(i) == '(')
		                    ob++;
		                else if(ss.charAt(i) == ')')
		                {
		                    ob--;
		                    if(ob==0)
		                    {	end = i;
		                        break;
		                    }


		                }
		                i++;
		            }
		        }
		        catch (Exception e) {

		        }
				//System.out.println("start = "+ start + " and end = "+ end);
		        mystring = ss.substring(start, end+1);
		        //System.out.println("mystring = "+mystring);
		        removebr = balance(mystring);
		        //System.out.println(removebr);
		        if(removebr)
		        {
		            try{


		                ss = ss.substring(0, end) + ss.substring(end+1);
		                //System.out.println(s);
		                ss = ss.substring(0,start) + ss.substring(start+1);

		            }
		            catch (Exception e) {

		            }

		        }

		    
		     }
		    else if(ss.charAt(pos1-1) == ',' && ss.charAt(pos1) == ')')
		    {

		        try{
		        	ss = ss.substring(0, pos1-1) + ss.substring(pos1);
	
		            end = pos1-1;
		            i = end; cb = 0;
		            while(true)
		            {
		                if(ss.charAt(i) == ')')
		                    cb++;
		                else if(ss.charAt(i) == '(')
		                {
		                    cb--;
		                    if(cb==0)
		                    { 	start = i;
		                        break;
		                    }
		                }
		                i--;
		            }
		        }
		        catch (Exception e) {
		 
		        }
		        mystring = ss.substring(start, end+1);
		        removebr = balance(mystring);
		        if(removebr)
		        {
		            try{

		            	ss = ss.substring(0, end) + ss.substring(end + 1);
		                ss = ss.substring(0,start) + ss.substring(start+1);

		            }
		            catch (Exception e) {

		            }

		        }



		    
		    }
		    else if(ss.charAt(pos1-1) == ',' && ss.charAt(pos1) == ',')
		    {

		        try{
		            ss = ss.substring(0, pos1-1) + ss.substring(pos1);
		        }
		        catch (Exception e) {
		        }
		        if(ss.charAt(pos1-2) == '(' && ss.charAt(pos1) == ')')
		        {
		            try{
		                ss = ss.substring(0, pos1) + ss.substring(pos1 + 1);
		                ss = ss.substring(0, pos1 - 2) + ss.substring(pos1 - 1);
		            }
		            catch (Exception e) {
		            }
		        }



		    
		    }
		}
		
//		System.out.println("before return s= "+s);
		return ss;
	}
	private static boolean balance(String s) {
		
		int i, l, ob = 0, com = 0;
		l = s.length();
		//System.out.println(" length = "+l);
		
		for(i = 1; i<l-1; i++)
		{
			
			if(s.charAt(i) == '(') ob++;
			else if(s.charAt(i) == ')') ob--;
			else if(s.charAt(i) == ',') com = 1;
			if(ob<0) return false; // don't remove bracket;
		} 
		if((s.charAt(1)=='('&& s.charAt(l-2) ==')' && ob==0) || com==0)return true; //remove unnecessary bracket
		else return false;
		
	}
	private static boolean bracket(String s) {
	    int l;
	    l=s.length();
	    if(s.charAt(0)!='(')
	        return false;
	    if(s.charAt(l-1)!=')')
	        return false;

	    for(int i = 1; i<l-1;i++ )
	    {
	        if(s.charAt(i) == '(' || s.charAt(i) == ')')
	            return false;
	    }
	    return true;
		
	}


	private static MultiReturnType FM(LinkedHashSet<Taxa> taxaList, LinkedHashMap<Integer, Quartet> quartetMap) {
		LinkedHashSet<Taxa> partA = new LinkedHashSet<Taxa>();
		LinkedHashSet<Taxa> partB = new LinkedHashSet<Taxa>();
		
		//int partitionIndexNumber = 0;
		for (Quartet quartet : quartetMap.values()) {
			int tcount = 0;
			if (taxaList.isEmpty()) {
				//System.out.println("Finished");
				break;
			} else {
				if(taxaList.contains(quartet.t1)) {
					taxaList.remove(quartet.t1);
					tcount ++;
				}
				if(taxaList.contains(quartet.t2)) {
					taxaList.remove(quartet.t2);
					tcount ++;
				}
				if(taxaList.contains(quartet.t3)) {
					taxaList.remove(quartet.t3);
					tcount ++;
				}
				if(taxaList.contains(quartet.t4)) {
					taxaList.remove(quartet.t4);
					tcount ++;
				}
			}
			if (tcount > 0) {
				int a = -1, b = -1, c = -1, d = -1;
//				if (partA.contains(quartet.getT1())) a = 0;
//				if (partA.contains(quartet.getT2())) b = 0;
//				if (partA.contains(quartet.getT3())) c = 0;
//				if (partA.contains(quartet.getT4())) d = 0;
//				
//				if (partB.contains(quartet.getT1())) a = 1;
//				if (partB.contains(quartet.getT2())) b = 1;
//				if (partB.contains(quartet.getT3())) c = 1;
//				if (partB.contains(quartet.getT4())) d = 1;
		///ei khane ekta else if condition use kora jay. if partA contains, then we wont have to chk partB
				//I will chk this later
				if (partA.contains(quartet.t1)) a = 0;
				else if (partB.contains(quartet.t1)) a = 1;
				
				if (partA.contains(quartet.t2)) b = 0;
				else if (partB.contains(quartet.t2)) b = 1;
				
				if (partA.contains(quartet.t3)) c = 0;
				else if (partB.contains(quartet.t3)) c = 1;
				
				if (partA.contains(quartet.t4)) d = 0;
				else if (partB.contains(quartet.t4)) d = 1;
				
		
				
				if(a==-1 && b==-1 && c==-1 && d==-1) //all new
			    {
			        ///This section can create problem in multithreading
					quartet.t1.setPartition(0);
					partA.add(quartet.t1);
			        //a= 0; //ca++;
			        quartet.t2.setPartition(0);
					partA.add(quartet.t2);
			        //b= 0; 
					//ca += 2;
			        quartet.t3.setPartition(1);
					partB.add(quartet.t3);
			        //c= 0; //cb++;
			        quartet.t4.setPartition(1);
					partB.add(quartet.t4);
			        //d= 0; 
					//cb += 2;
			        
//			        partitionIndexNumber++;
//			        quartet.t1.partitionIndex = partitionIndexNumber;
//			        partitionIndexNumber++;
//			        quartet.t2.partitionIndex = partitionIndexNumber;
//			        partitionIndexNumber++;
//			        quartet.t3.partitionIndex = partitionIndexNumber;
//			        partitionIndexNumber++;
//			        quartet.t4.partitionIndex = partitionIndexNumber;
			     
			    }else {
			    	if(a==-1)
			        {	
//			    		partitionIndexNumber++;
//			    		quartet.t1.partitionIndex = partitionIndexNumber;				       
			    		if(b!=-1){
			                if(b==0) {quartet.t1.setPartition(0); partA.add(quartet.t1); a=0;// ca++;
			                }
			                else {quartet.t1.setPartition(1); partB.add(quartet.t1); a=1; //cb++;
			                }
			            }
			            else if(c!=-1){
			                if(c==1) {
			                	quartet.t1.setPartition(0); partA.add(quartet.t1); a=0;// ca++;
			                	quartet.t2.setPartition(0); partA.add(quartet.t2); b=0; 
			                	//a += 2;
			                }
			                else {
			                	quartet.t1.setPartition(1); partB.add(quartet.t1); a=1;// cb++;
			                	quartet.t2.setPartition(1); partB.add(quartet.t2); b=1; 
			                	//cb+=2;
			                }
			            }
			            else if(d!=-1)
			            {
			                if(d==1) {
			                	quartet.t1.setPartition(0); partA.add(quartet.t1); a=0; //ca++;
			                	quartet.t2.setPartition(0); partA.add(quartet.t2); b=0; 
			                	//ca +=2;
			                }
			                else {
			                	quartet.t1.setPartition(1); partB.add(quartet.t1); a=1;// cb++;
			                	quartet.t2.setPartition(1); partB.add(quartet.t2); b=1;
			                	//cb+=2;
			              
			                }
			            }
			
			        }
			        if(b==-1)
			        {
			
//			        	partitionIndexNumber++;
//				        quartet.t2.partitionIndex = partitionIndexNumber;
			        	if(a==0) {
			        		quartet.t2.setPartition(0); partA.add(quartet.t2);// b=0; 
			        		//ca++;
			        	}
			            else {
			            	quartet.t2.setPartition(1); partB.add(quartet.t2); //b=1; 
			            	//cb++;
			            }
			
			        }
			        if(c==-1)
			        {
//			        	partitionIndexNumber++;
//				        quartet.t3.partitionIndex = partitionIndexNumber;
			        	if(d!=-1)
			            {
			                if(d==0) {
			                	quartet.t3.setPartition(0); partA.add(quartet.t3); //c=0; 
			                	//ca++;
			                	
			                }
			                else {
			                	quartet.t3.setPartition(1); partB.add(quartet.t3); //c=1; 
			                	//cb++;
			                }
			            }
			            else{
			                if(a==1) {
			                	quartet.t3.setPartition(0); partA.add(quartet.t3);// c=0;
			                	quartet.t4.setPartition(0); partA.add(quartet.t4); d=0; 
			                	//ca += 2;
			                	}
			                else {
			                	quartet.t3.setPartition(1); partB.add(quartet.t3);// c=1;
			                	quartet.t4.setPartition(1); partB.add(quartet.t4); d=1;
			                	//cb++;
			                	
			                }
			            }
			        }
			        if(d==-1)
			        {
//			        	partitionIndexNumber++;
//				        quartet.t4.partitionIndex = partitionIndexNumber;
			        	if(c==0) {quartet.t4.setPartition(0); partA.add(quartet.t4); //ca++;
			        	}
			            else {quartet.t4.setPartition(1); partB.add(quartet.t4); //cb++;
			            }
			        }
			
				}
			}
				
		}
		int c = 0;
		int ca = partA.size(), cb = partB.size();
//		System.out.println("*********FM taxalist*********");
//		printTaxa(taxaList);
		if (!taxaList.isEmpty()) {
			
			for (Taxa taxa : taxaList) {
//				partitionIndexNumber++;
//		        taxa.partitionIndex = partitionIndexNumber;
				if(ca<cb)
	                c=2;
	            else if(cb<ca)
	                c=1;
	            else c++;
				
	            if(c%2==0){
	            	taxa.setPartition(0);
	            	partA.add(taxa);
	            	//taxaList.remove(taxa);
	            	ca++;
	            }else {
	            	taxa.setPartition(1);
	            	partB.add(taxa);
	            	//taxaList.remove(taxa);
	            	cb++;
	            }

			}
		}
		
        
		//printQuartet(quartetList);
//		System.out.println("*********************FM*****************");
//		printTaxa(partA);
//		printTaxa(partB);
		//return null;
		return MFM_algo(partA, partB, quartetMap);
		//return FM_algo(partA, partB, quartetList);
	}


	
	
	private static int countSatisfiedQuartets(LinkedHashMap<Integer, Quartet> quartetMap) {
		//int csat = 0;
		int[] sat = {0};
	    //char quartetScore;
//	    for (Quartet quartet : quartetMap.values()) {
//			//quartetScore = iCheckQuartet(quartet);
//			//if(quartetScore == 's')
//			if(quartet.status == 's')
//	        {
//	        	csat += quartet.getQFrequency();
//	        }
//		}
	    quartetMap.values().parallelStream().forEach(quartet -> {
	    	if(quartet.status == 's')
	        {
	        	
	        	sat[0] += quartet.getQFrequency();
	      
	        }
	    });
	   
	    return sat[0];
	}

	/*private static int[] calculateScore(LinkedHashSet<Taxa> partB, LinkedHashSet<Quartet> quartetList, String tempTaxa,
			int st, int vt, int df) {
		int[] scores = {0,0,0,0};
	    int  qscore;
	    int s = 0, v = 0, d = 0;
	    char c;
	    //only tempTaxa ta jei shob quartet a ase, oi gulo niye new Qlist korte hobe
	    for (Quartet q : quartetList) {
	    	if(tempTaxa.contentEquals("null"))
	        {
	            q.setStatus("");
	            qscore  = checkQuartet(partB, q, tempTaxa);
	            if(qscore == 6) s = s + q.getQFrequency();
	            else if(qscore == 2) v = v + q.getQFrequency();
	            else if(qscore == 3) d = d + q.getQFrequency();
	        }
	    	else if(q.getT1().getName().contentEquals(tempTaxa) || q.getT2().getName().contentEquals(tempTaxa) ||
	        		q.getT3().getName().contentEquals(tempTaxa) || q.getT4().getName().contentEquals(tempTaxa))
	        {	
	    		qscore  = checkQuartet(partB, q, tempTaxa);
	            c = q.getStatus().charAt(0);//status[0];
	            System.out.println(q.getT1().getName()+","+q.getT2().getName()
	            		+"|"+q.getT3().getName()+","+q.getT4().getName()+":"+q.getQFrequency()+"->"+q.getStatus());
	            System.out.println("tempTaxa = "+tempTaxa+ "  qscore = "+qscore+"  c = "+ c);

	            if(c=='s' && qscore == 2) { s = s - q.getQFrequency(); v = v + q.getQFrequency(); } // s v

	            else if(c=='s' && qscore==3){ s = s - q.getQFrequency(); d = d + q.getQFrequency();} // s d

	            else if(c=='v' && qscore == 6){v = v - q.getQFrequency(); s = s + q.getQFrequency();} // v s

	            else if(c=='v' && qscore==3){v = v - q.getQFrequency();d = d + q.getQFrequency();}  // v d

	            else if(c=='d' && qscore==2){d = d - q.getQFrequency();v = v + q.getQFrequency();} // d v

	            else if(c=='d' && qscore==6){d = d - q.getQFrequency();s = s + q.getQFrequency();} // d s

	            else if(qscore==1)
	            {
	                if(c=='s') s = s - q.getQFrequency();
	                else if(c=='v') v = v - q.getQFrequency();
	                else if(c=='d') d = d - q.getQFrequency();

	            }
	            else if(c=='b')
	            {
	                if(qscore==6) s = s + q.getQFrequency();
	                else if(qscore==2) v = v + q.getQFrequency();
	                else if(qscore==3) d = d + q.getQFrequency();
	            }

	        }
		}
	    System.out.println("s = "+s+"  v = "+v+"  d = "+d);
	    if(tempTaxa.contentEquals("null"))
	    {
	        scores[1]=s;//noOfSat = s;
	        scores[2]=v;//noOfVat = v;
	        scores[3]=d;//noOfDef = d;


	        scores[0]=(s-v);//partitionScore = (s-v);
	    }
	    else
	    {
	        scores[1] = st+s ;//noOfSat = st+s;
	        scores[2] = vt+v;//noOfVat = vt+v;
	        scores[3] = df+d;//noOfDef = df+d;

	        scores[0]= ((st+s)-(vt+v));//partitionScore = ((st+s)-(vt+v));
	    }

		return scores;
	 
	}

	private static int checkQuartet(LinkedHashSet<Taxa> partB, Quartet q, String tempTaxa) {
		int a = 0, b = 0, c = 0, d = 0, score = 0;
		char qstat;
		//following code should work. will investigate later
//		if(q.getT1().getPartition() == 1) a = 1;
//		if(q.getT2().getPartition() == 1) b = 1;
//		if(q.getT3().getPartition() == 1) c = 1;
//		if(q.getT4().getPartition() == 1) d = 1;
		
		// i think parB na dileo hobe
		if(partB.contains(q.getT1())) a = 1;
		if(partB.contains(q.getT2())) b = 1;
		if(partB.contains(q.getT3())) c = 1;
		if(partB.contains(q.getT4())) d = 1;
		////////will change above block of code later
		if(tempTaxa.contentEquals(q.getT1().getName())) a = 1 - a;
	    else if(tempTaxa.contentEquals(q.getT2().getName())) b = 1-b;
	    else if(tempTaxa.contentEquals(q.getT3().getName())) c = 1-c;
	    else if(tempTaxa.contentEquals(q.getT4().getName())) d = 1-d;
		
		if (a==b && c==d && b==c) // totally on one side
	    {	
	        score = 1;
	        qstat = 'b';
	    }
	    else if( a==b && c==d) //satisfied
	    {
	        score = 6; //6
	        qstat = 's';
	    }
	    else if ((a==c && b==d) || (a==d && b==c)) // violated
	    {
	        score = 2;
	        qstat = 'v';

	    }
	    else //deffered
	    {
	        score = 3;
	        qstat = 'd';
	    }

	    q.setStatus(q.getStatus()+qstat);
		return score;

	}
	*/

	private static void printQuartet(LinkedHashSet<Quartet> quartetList) {
		System.out.println("**********************Quartet List********************************");
		for(Quartet quartets : quartetList) {
			
			System.out.println("quartet"+" : "+quartets.t1.name+","+quartets.t2.name+"|"
					+quartets.t3.name+","+quartets.t4.name+":"+quartets.getQFrequency()+"->"
					+quartets.isIncreaseFrequency()+" -> "+ quartets.status+"\n");
		}
		System.out.println("*********************End of Printing Quartet**********************");
		
	}

	private static void printTaxa(LinkedHashSet<Taxa> taxaList) {
		System.out.println("**********************Taxa List********************************");
		for (Taxa taxa : taxaList) {
			System.out.print(taxa.name+"->");
		}
		System.out.println("end");
	
		
	}

	private static String depthOneTree(LinkedHashSet<Taxa> taxaList) {
		String s = "(";
		int taxaCount = taxaList.size(), count = 0;
		for (Taxa taxa : taxaList) {
			 s += taxa.name;
			 count++;
			 if (count == taxaCount) {
				 s += ")";
			}else {
				s += ",";
			}
		}
	    
	    //System.out.println("Depth one tree = "+ s);
		return s;
	}
	/////////////Modifying Gain//////////////////
	private static MultiReturnType MFM_algo(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB,
			LinkedHashMap<Integer, Quartet> quartetMap) {
		//String taxaToMove = null;
		boolean loopAgain = true;

//		for (int quartetID : quartetMap.keySet()) {
//			Quartet quartet = quartetMap.get(quartetID);
//			quartet.fillUpSVDtableMapWithInitialRelaventQuartetID(quartetID);
//		}
		while (loopAgain) {
			boolean iterationMore = true; int iteration = 0;
			int ca = partA.size();
			int cb = partB.size();
			// at the begining of this loop(loopAgain) ca and cb are size of partA and part. But later it is hypothetically true.
			// because I only remove taxa from these partitions but I dont add them in opposite partition. I add the removed taxa in movedList
			
			int arraysize = ca + cb;
			List<GainList> movedList;
			if (arraysize > 0) {
				movedList = new ArrayList<GainList>(arraysize);
			} else {
				movedList = new ArrayList<GainList>();
			}
			
			//LinkedHashSet<Quartet> rQuartetList = new LinkedHashSet<Quartet>();//reduced quartetList(quartets with movedTaxa);
			int prevScore =0, prevS = 0, prevV = 0;//, prevD = 0;
			
//			for (Quartet quartet : quartetList) {
//				quartet.fillUpRelaventQuartetListOfCorrespondingTaxa();
//			}
			//Taxa taxa_to_move = new Taxa("", -1);
			
			while (iterationMore) {
				//final HashMap<Integer, SVD_Log> svdTableMap = taxa_to_move.svdTableMap;
				iteration++;
				//System.out.println("iteration "+ iteration);
				int[] score;
				if (iteration == 1) {
					score= iCalculateScore(quartetMap);
					//score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
					prevScore = score[0];//partition score
		            prevS = score[1];//noOfSat
		            prevV = score[2];//noOfVat
		            //prevD = score[3];//noOfDef
				}
//				else {
//					score= iCalculateScore(partB,rQuartetList);
//				}
//				int[] score= mCalculateScore(null,partB,quartetList,"null",0,0,0);
//				//score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
//				int prevScore = score[0];//partition score
//	            int prevS = score[1];//noOfSat
//	            int prevV = score[2];//noOfVat
//	            int prevD = score[3];//noOfDef

//	            int ca = partA.size();
//				int cb = partB.size();
//				System.out.println("Before Flag: prevScore = "+prevScore+"  prevS = "+ prevS+"  prevV = "+prevV+"  prevD = "+ prevD);
//				printQuartet(quartetList);
//				boolean flag = true; int tag1 = 0, tag2 = 0, alt = 0;
//				gainList = new Listt();
				//LinkedHashSet<GainList> gainList = new LinkedHashSet<GainList>();
				//List<GainList> gainList = new LinkedList<GainList>();
					
//				Iterator<Taxa> iteratorA = partA.iterator();
//				Iterator<Taxa> iteratorB = partB.iterator();
				//System.out.println("New Iteration");
				
				
//				final int iterationF = iteration;
//				final int prevSF = prevS;
//				final int prevVF = prevV;
//				//final int prevDF = prevD;
//				final int prevScoreF = prevScore;
//				
//		
//				Thread tA = new Thread() {
//
//					@Override
//					public void run() {
//						//int[] score;
//						int partitionIndex = 0;
//						for (Taxa taxaA : partA) {
//							
//							if (iterationF == 1) {
//								//taxaA.getSvdTable().clear();
//								//System.out.println("Taxa A = "+taxaA.getName());
//								taxaA.relaventQuartetIDOfCorrespondingMovedTaxa.clear();
//								taxaA.mCalculateScore(quartetMap, prevSF, prevVF, prevScoreF);
//							} else {
//								taxaA.mCalculateScore2(quartetMap, prevSF, prevVF, prevScoreF);
//							}
//							//I am keeping following values as attributes of taxa. If I keep them in a treemap, i think it
//							//will be fasster. but we may not get similar result
//							//System.out.println("Taxa : "+ taxaA.getName() +" , number of relavantQuartet = "+ taxaA.relaventQuartet.size());
////							taxaA.setVal(score[0] - prevScoreF);
////							taxaA.setSat(score[1]);
////							taxaA.setVat(score[2]);
//							//taxaA.setDef(score[3]);
//							partitionIndex += 1;
//							taxaA.partitionIndex = partitionIndex;
//						}
//						
//					}
//					
//				};
//				tA.start();
				//////////////////parrallel stream///////////////////
				final int prevSF = prevS;
				final int prevVF = prevV;
				final int prevScoreF = prevScore;
				partA.parallelStream().forEach(taxa -> {
					taxa.mCalculateScore(prevSF, prevVF, prevScoreF);
				});
				partB.parallelStream().forEach(taxa -> {
					taxa.mCalculateScore(prevSF, prevVF, prevScoreF);
				});
				/////////////////////////////////////////////////////
				//////////Folowing chunk of code is useless. But um doing this to match java's result with c++ result
				int partitionIndex = 0;
				for (Taxa taxa : partA) {
					partitionIndex++;
					taxa.partitionIndex = partitionIndex;
				}
				partitionIndex = 0;
				for (Taxa taxa : partB) {
					partitionIndex++;
					taxa.partitionIndex = partitionIndex;
				}
				////////////////////////////Parallel Steam //////////////////////
//				int partitionIndex = 0;
//				for (Taxa taxaA : partA) {
////					if (iteration == 1) {
////						//taxaA.getSvdTable().clear();
////						//taxaA.relaventQuartetIDOfCorrespondingMovedTaxa.clear();
////						taxaA.mCalculateScore(quartetMap, prevS, prevV, prevScore);
////					} else {
//						taxaA.mCalculateScore(prevS, prevV, prevScore);
//					//}
////					partitionIndex += 1;
////					taxaA.partitionIndex = partitionIndex;
//				}
				////////////////////////////Parallel Steam //////////////////////
//				 partitionIndex = 0;
//				for (Taxa taxaB : partB) {
////					if (iteration == 1) {
////						//taxaA.getSvdTable().clear();
////						//System.out.println("Taxa B = "+taxaB.getName());
////						//taxaB.relaventQuartetIDOfCorrespondingMovedTaxa.clear();
////						taxaB.mCalculateScore(quartetMap, prevS, prevV, prevScore);
////					} else {
//						taxaB.mCalculateScore(prevS, prevV, prevScore);
//					//}
////					partitionIndex += 1;
////					taxaB.partitionIndex = partitionIndex;
//				}
				////////////////////////////Parallel Steam //////////////////////
				
//				Taxa maxGainTaxaPartB = null;
//				if (!partB.isEmpty() && cb > 2) {
//					maxGainTaxaPartB = partB.stream().max(Comparator.comparing(Taxa::getVal).thenComparing(Taxa::getSat)).get();
//				}
				
//				try {
//					tA.join();
//				} catch (InterruptedException e) {
//					
//				}
//				
				Taxa taxa_to_move = new Taxa("", -1);
				
				//I have tried Three ways for finding taxa which has maximum gain
				//1. If we keep all taxa in a treeset in descending order on the basis of val, sat and partitionIndex,
				// then first element will be taxa of maximum gain. But its not memory efficient
				//2. Finding max taxa for partA and partB separately, then compare them. It can be done inside uppper thread
				//3. Using iteration , then comparing one by one
				// Three approaches gives almost same running time. Method two is slightly better. 
				
				
//				//method 1: Using treeset
//				TreeSet<Taxa> tr = new TreeSet<Taxa>(Comparator.comparing(Taxa::getVal, Collections.reverseOrder())
//						.thenComparing(Taxa::getSat, Collections.reverseOrder())
//						.thenComparing(Taxa::getPartitionIndex));
//				if (!partA.isEmpty() && ca > 2) {
//					tr.addAll(partA);
//				}
//			
//				if (!partB.isEmpty() && cb > 2) {
//					tr.addAll(partB);
//				}
//				if (!tr.isEmpty()) {
//					taxa_to_move = tr.first();
//				}
				
				
				
				// Method 2: Finding max taxa for partA and partB separately, then compare them. 
				
				Taxa maxGainTaxaPartA = null;
				if (!partA.isEmpty() && ca > 2) {
					maxGainTaxaPartA = partA.stream().max(Comparator.comparing(Taxa::getVal).thenComparing(Taxa::getSat)).get();
				}
				// adding below code after calculating gain of all taxa of partB
				Taxa maxGainTaxaPartB = null;
				if (!partB.isEmpty() && cb > 2) {
					maxGainTaxaPartB = partB.stream().max(Comparator.comparing(Taxa::getVal).thenComparing(Taxa::getSat)).get();
				}
				
	            if (maxGainTaxaPartA != null && maxGainTaxaPartB != null) {
	            	if (maxGainTaxaPartA.getVal() > maxGainTaxaPartB.getVal()) {
	            		taxa_to_move = maxGainTaxaPartA;						
					}else if(maxGainTaxaPartA.getVal() < maxGainTaxaPartB.getVal()) {
						taxa_to_move = maxGainTaxaPartB;
					}else { //in that case maxGainTaxaPartA.getVal() == maxGainTaxaPartB.getVal()
						if (maxGainTaxaPartA.getSat() > maxGainTaxaPartB.getSat()) {
							taxa_to_move = maxGainTaxaPartA;
						}else if (maxGainTaxaPartA.getSat() < maxGainTaxaPartB.getSat()) {
							taxa_to_move = maxGainTaxaPartB;
						}else {//in that case (maxGainTaxaPartA.getSat() == maxGainTaxaPartB.getSat()) {
							if (maxGainTaxaPartA.partitionIndex <= maxGainTaxaPartB.partitionIndex) {
								taxa_to_move = maxGainTaxaPartA;
							}else {
								taxa_to_move = maxGainTaxaPartB;
							}
						}
					}
	            	
				} else if (maxGainTaxaPartA != null) {
					taxa_to_move = maxGainTaxaPartA;
				} else if (maxGainTaxaPartB != null) {
					taxa_to_move = maxGainTaxaPartB;
				}
				
				
			
             // Method 3: comparing one by one
//				int maxgain = -1000000000; //double
//				int maxsat = 0;
//	            //glPart = 0;
//	            int randnum = 0;
//	            int mVat = 0;
//	            int mDef = 0;   
//				while (flag) {
//					Taxa taxa = new Taxa("", 5);
//					boolean access = false;
//					if (iteratorA.hasNext() && alt == 0 && ca > 2) {
//						taxa = iteratorA.next();
//						access = true;
//						
//					} else if (iteratorB.hasNext() && alt == 1 && cb > 2) {
//						taxa = iteratorB.next();
//						access = true;
//					} 
//					
//					if (!iteratorA.hasNext() || ca<3) {
//						tag1 = 1;
//					}
//					if (!iteratorB.hasNext() || cb<3) {
//						tag2 = 1;
//					}
//					if (tag1 == 1 && tag2 == 1) {
//						flag = false;
//					}
//					if (tag2 == 0 && alt == 0) {
//						alt = 1;
//					}else if (tag1 == 0 && alt == 1) {
//						alt= 0;
//					}
//					////Gain Calculation
//					if (access) {
//						if (taxa.getVal() > maxgain) {
//							taxa_to_move = taxa;
//							//taxaToMove = taxa_to_move.getName();
//		                    maxgain = taxa.getVal();               
//		                    maxsat = taxa.getSat();
//		                    //glPart = taxa_to_move.getPartition(); // current Partition
//		                    mVat = taxa.getVat();
//		                    mDef = taxa.getDef();
//		                 
//						} else if(taxa.getVal() == maxgain){
//							 
//		                   
//		                    if(taxa.getSat() > maxsat)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
//		                    {
//		                    	taxa_to_move = taxa;
//								//taxaToMove = taxa_to_move.getName();
//			                    maxgain = taxa.getVal();               
//			                    maxsat = taxa.getSat();
//			                   // glPart = taxa_to_move.getPartition(); // current Partition
//			                    mVat = taxa.getVat();
//			                    mDef = taxa.getDef();
//		                    }
//		                    //below code is for randomly pick a taxa. I m commenting this block to turn off randomization. It is not necessary
////		                    else if(taxa.getSat() == maxsat)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
////		                    {
////		                       randnum = 10 + (new Random().nextInt(100));///rand()%100;
////		                        if(randnum%2 == 0){
////		                        	taxa_to_move = taxa;
////									//taxaToMove = taxa_to_move.getName();
////				                    maxgain = taxa.getVal();               
////				                    maxsat = taxa.getSat();
////				                    //glPart = taxa_to_move.getPartition(); // current Partition
////				                    mVat = taxa.getVat();
////				                    mDef = taxa.getDef();
////		                        }
////		                        //}
////
////		                    }
//
//		                
//						}
//					}
//
//					
//				
//					////////////
//				}

				///////Moving Taxa which have highest gain
//	            maxgain = taxa_to_move.getVal();               
//                maxsat = taxa_to_move.getSat();
//                //glPart = taxa_to_move.getPartition(); // current Partition
//                mVat = taxa_to_move.getVat();
//                mDef = taxa_to_move.getDef();
//				prevS = maxsat;//score[1];//noOfSat
//    	        prevV = mVat;//score[2];//noOfVat
//    	        prevD = mDef;//score[3];//noOfDef
    	        
    	        
    	        ///////////////
    	        prevS = taxa_to_move.getSat();//score[1];//noOfSat
    	        prevV = taxa_to_move.getVat();//score[2];//noOfVat
    	       // prevD = taxa_to_move.getDef();//score[3];//noOfDef
    	        ////////////////
				int glPart = taxa_to_move.getPartition();
				prevScore = prevS - prevV;//partition score
				if (glPart != -1) {
					taxa_to_move.locked = true;
					//System.out.println("Moved Taxa = "+taxa_to_move.getName());
//					if (gainList.size() == 1) {
////						mCalculateScore(null,partB,quartetList,"null",0,0,0);
////						System.out.println("gainlist.size == 1");
//						iterationMore = false;
//					}
					//final String taxaMove = taxaToMove;
					//Taxa t;
					if (glPart == 1) {
						taxa_to_move.setPartition(0);
						partB.remove(taxa_to_move);
						cb--; ca++;
						//partA.add(taxa_to_move);
					} else {
						taxa_to_move.setPartition(1);
						partA.remove(taxa_to_move);
						ca--; cb++;
						//partB.add(taxa_to_move);
					}
					//taxa_to_move.setLocked(true);
//					t.setLocked(true);
					//movedList.add(new GainList(taxa_to_move, maxgain));
					movedList.add(new GainList(taxa_to_move,  taxa_to_move.getVal()));
					////////////Extracting quartets which have moved_taxa
					
					//rQuartetList.clear();
					
					
					////////////////Parallel Stream//////////////
					final Taxa final_taxa_to_move = taxa_to_move;				
					taxa_to_move.svdTableMap.keySet().parallelStream().forEach(qid -> {
						Quartet q = quartetMap.get(qid);
						SVD_Log svd = final_taxa_to_move.svdTableMap.get(qid);
						q.status=svd.getqStat();
						//q.fillUpRelaventQuartetIDOfCorrespondingMovedTaxa(qid);
						q.updateSVD_for_moving_taxa(qid);
				    });
					////////////////Parallel Stream//////////////
					
//					for (int qid : taxa_to_move.svdTableMap.keySet()) {
//						Quartet q = quartetMap.get(qid);
//						SVD_Log svd = taxa_to_move.svdTableMap.get(qid);
//						q.status=svd.getqStat();
//						//q.fillUpRelaventQuartetIDOfCorrespondingMovedTaxa(qid);
//						q.updateSVD_for_moving_taxa(qid);
//						//rQuartetList.add(q);
//					}
					
					//taxa_to_move.svdTableMap.clear();
					//taxa_to_move.relaventQuartet.clear();
//					for (Quartet q : quartetList) {
//						if(q.getT1().getName().contentEquals(taxaToMove) ||
//								q.getT2().getName().contentEquals(taxaToMove) ||
//								q.getT3().getName().contentEquals(taxaToMove) ||
//								q.getT4().getName().contentEquals(taxaToMove)) {
//							rQuartetList.add(q);
//						}
//					}
//					System.out.println("Taxa to move = "+taxaToMove+" Size of rQlist = "+rQuartetList.size());
//					System.out.println("************rQlist**********");
//					printQuartet(rQuartetList);
//					printQuartet(quartetList);
					
						
				} else {
					iterationMore = false;
//					mCalculateScore(null,partB,quartetList,"null",0,0,0);
//					System.out.println("Accessed");
				}
				
			
				
				
			}///no more iteration
			//mCalculateScore(null,partB,quartetList,"null",0,0,0);
//			System.out.println("********************Moved List*************");
//			for (GainList gl : movedList) {
//				 System.out.println(gl.getTaxa().getName()+" "+gl.getVal()+" "+gl.getTaxa().getPartition()+" "+gl.getSat()+" "+gl.getVat()+" "+
//	                		gl.getDef()+" "+gl.getBel0w());
//			}
			int cumulativeGain = 0, gainMax = 0;
			//String back = "Initial"; 
			int backIndex = -1, bi = 0;
			boolean isMove = false;
			for (GainList ml : movedList) {
				cumulativeGain += ml.val;
				if (cumulativeGain >= gainMax) {
					gainMax = cumulativeGain;
					//back = ml.getTaxa().getName();
					backIndex = bi;
					isMove = true;
				}
				bi++;
			}
//			System.out.println("cumulative gain = "+ cumulativeGain);
//         //   System.out.println(" taxa to move = "+ taxaToMove);
//            System.out.println(" back taxa = "+ back);
//            System.out.println("partA and partB");
//            printTaxa(partA);
//            printTaxa(partB);
//			LinkedList<Taxa> pa = new LinkedList<Taxa>();
//			LinkedList<Taxa> pb = new LinkedList<Taxa>();
           // boolean isMove = false; bi = 0;
//            boolean revrs = true;
			if (isMove) {
				for (int i = backIndex + 1; i < movedList.size(); i++) {
					Taxa moveTaxa = movedList.get(i).taxa;
					moveTaxa.resetTaxa();
	            
					if (moveTaxa.getPartition() == 1) {
	        			moveTaxa.setPartition(0);
	        			partA.add(moveTaxa);
	        			//pa.addFirst(moveTaxa);
	        			//pa.add(moveTaxa);
					} else {
						moveTaxa.setPartition(1);
						partB.add(moveTaxa);
						//pb.addFirst(moveTaxa);
	        			//pb.add(moveTaxa);
					}
					
					
				}
				for (int i = backIndex; i >= 0; i--) {
					Taxa moveTaxa = movedList.get(i).taxa;
					moveTaxa.resetTaxa();
	            
					if (moveTaxa.getPartition() == 1) {
	        			partB.add(moveTaxa);
					} else {
	        			partA.add(moveTaxa);
					}
					
					
				}
			}
            
			
			 
           /* for (int i = movedList.size()-1; i >= 0; i--) {
				Taxa moveTaxa = movedList.get(i).getTaxa();
            	String moveTaxaName = moveTaxa.getName();
            	if (moveTaxaName.contentEquals(back)) {
					isMove = true;
				}
				if (isMove) {
					if (revrs) {
						Collections.reverse(pa);
						Collections.reverse(pb);
						//partA.clear();
						//partB.clear();
						partA.addAll(pa);
						partB.addAll(pb);
						revrs = false;
					}
					if (moveTaxa.getPartition() == 1) {
            			partB.add(moveTaxa);
    				} else {
            			partA.add(moveTaxa);
    				}
				} else {
					if (moveTaxa.getPartition() == 1) {
            			moveTaxa.setPartition(0);
            			pa.add(moveTaxa);
    				} else {
    					moveTaxa.setPartition(1);
            			pb.add(moveTaxa);
    				}
				}
				
			}*/
      
            
//            System.out.println("After moving partA and partB");
//            printTaxa(partA);
//            printTaxa(partB);
		
//            for (Taxa taxa : partA) {
//				taxa.setLocked(false);
//			}
//            for (Taxa taxa : partB) {
//				taxa.setLocked(false);
//			}
            
            
          
            if (gainMax <= 0) {
            	//System.out.println("Looop again is finished");
            	loopAgain = false;
			}
			
		}//no more loop
		 //************end of Loop Again**********//
        //***********Merge Two list***************//
        /*
         * int partSat = countSatisfiedQuartets(partA, partB,quartetList);
        System.out.println("PartSat = "+partSat);
        System.out.println("After loop again part a nd b");
        printTaxa(partA);
        printTaxa(partB);
        LinkedHashSet<Taxa> finalTaxaList = new LinkedHashSet<Taxa>(partA);
        finalTaxaList.addAll(partB);
        printTaxa(finalTaxaList);
        */
//		System.out.println("************************FM_algo Quartet List*************************");
//		printQuartet(quartetList);
//		mCalculateScore(null,partB,quartetList,"null",0,0,0);
		return new MultiReturnType(partA, partB);
	}
	
	
	private static int[] iCalculateScore(LinkedHashMap<Integer, Quartet> quartetMap) {
		//initial_calculate_score
		int[] scores = {0,0,0,0};
	    
		quartetMap.keySet().parallelStream().forEach(quartetID -> {
	
			Quartet q = quartetMap.get(quartetID);
			q.fillUpSVDmapInitiallyWithRelaventQIDandScore(quartetID);
			
			char qStat  = q.status;
            if(qStat == 's') scores[1] = scores[1] + q.getQFrequency();//number of satisfied quartet, s
            else if(qStat == 'v') scores[2] = scores[2] + q.getQFrequency();//number of violated quartet, v
            else if(qStat == 'd') scores[3] = scores[3] + q.getQFrequency();//number of deferred quartet, d
	    });
		
//	    for (int quartetID : quartetMap.keySet()) {
//			Quartet q = quartetMap.get(quartetID);
////			q.fillUpSVDmapInitiallyWithRelaventQIDandScore(quartetID);
//			char qStat  = q.status;
//            if(qStat == 's') scores[1] = scores[1] + q.getQFrequency();//number of satisfied quartet, s
//            else if(qStat == 'v') scores[2] = scores[2] + q.getQFrequency();//number of violated quartet, v
//            else if(qStat == 'd') scores[3] = scores[3] + q.getQFrequency();//number of deferred quartet, d
//            
//            
//			
//		}
	    
	    
	    
	    scores[0]=(scores[1] - scores[2]);//partitionScore = (s-v);
	    
	    
	    
	
		return scores;
	 
	}
//	public static char mCheckQuartet(Quartet q, String tempTaxa) {
//		//int a = 0, b = 0, c = 0, d = 0;
//		char qstat;
//
//		int a = q.getT1().getPartition();
//		int b = q.getT2().getPartition();
//		int c = q.getT3().getPartition();
//		int d = q.getT4().getPartition();
//
//		if(tempTaxa.contentEquals(q.getT1().getName())) a = 1 - a;
//	    else if(tempTaxa.contentEquals(q.getT2().getName())) b = 1-b;
//	    else if(tempTaxa.contentEquals(q.getT3().getName())) c = 1-c;
//	    else if(tempTaxa.contentEquals(q.getT4().getName())) d = 1-d;
//	
//		
//		if (a==b && c==d && b==c) // totally on one side
//	    {	
//	        qstat = 'b';
//	    }
//	    else if( a==b && c==d) //satisfied
//	    {
//	        qstat = 's';
//	    }
//	    else if ((a==c && b==d) || (a==d && b==c)) // violated
//	    {
//	        qstat = 'v';
//
//	    }
//	    else //deffered
//	    {
//	        qstat = 'd';
//	    }
//
//	    //q.setStatus(qstat);
//		return qstat;
//
//	}
	private static char iCheckQuartet(Quartet q) {
		char qstat= iCheckQuartet2(q.t1.getPartition(), q.t2.getPartition(), q.t3.getPartition(), q.t4.getPartition());
	    q.status=qstat;
		return qstat;

	}
	public static char iCheckQuartet2(int a, int b, int c, int d) {
		char qstat;

		if (a==b && c==d && b==c) // totally on one side
	    {	
	        qstat = 'b';
	    }
	    else if( a==b && c==d) //satisfied
	    {
	        qstat = 's';
	    }
	    else if ((a==c && b==d) || (a==d && b==c)) // violated
	    {
	        qstat = 'v';

	    }
	    else //deffered
	    {
	        qstat = 'd';
	    }

		return qstat;

	}
	//////////////////////////////

    private static String mergeUsingJAR(String s1, String s2, String extra) {
       
        if (s1.equals("")) {
            return s2;
        }
        if (s2.equals("")) {
            return s1;
        }

        String rootedS1 = rerootTreeUsingJARAndProcessing(s1, extra);
        String rootedS2 = rerootTreeUsingJARAndProcessing(s2, extra);
        
        String mergedTree = "("+rootedS1 + "," + rootedS2+");";

        return mergedTree;

	}

	private static String rerootTreeUsingJARAndProcessing(String newickTree, String outGroupNode) {
		STITree tree = null;
		  try {
	            tree = new STITree(newickTree);
	            tree.rerootTreeAtNode(tree.getNode(outGroupNode));
	        } catch (IOException | ParseException ex) {
	            System.out.println("Error in rerootTree.JAR ... check if jar main.jar exists. Exiting.");
	            System.exit(-1);
	        }

	      String rootedTree = tree.toNewick();
	      
	      rootedTree = rootedTree.replace(";", ""); // remove semi-colon
	      rootedTree = rootedTree.substring(1, rootedTree.length() - 1); // remove first and last brackets
	      rootedTree = rootedTree.replace(outGroupNode, ""); //remove outGroup Node
	      rootedTree = rootedTree.substring(1); //From left, so remove first comma
	 
	      return rootedTree;
		
	}


	////////////////////////////////////////////
	/*
	 *Most of the time is consumed by FM_algo() method
	 *After moving one taxa, I think we can reduce size of quartet list
	 *reverse na korle, RF score better ashe naki dekhte hobe
	 *adding maxgain in iterator loop may work. will ve to check
	 *multithreading is remaining
	 **/
	

}