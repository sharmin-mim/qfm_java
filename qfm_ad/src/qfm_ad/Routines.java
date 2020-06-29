package qfm_ad;

import java.io.BufferedReader;

//import java.io.BufferedWriter;
import java.io.FileReader;

//import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;



public class Routines {
	public static String readQuartetQMC(String fileName) { // count will be done at the time of reading

		LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>();
		
		
		HashSet<Quartet> quartetList = new HashSet<Quartet>();
		double startTime = System.currentTimeMillis();
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
					t[i] = new Taxa(qq[i]);
					//taxaList.add(t[i]);
					///////////recent change
					if (!taxaList.add(t[i])) {
						final String taxaName = t[i].getName();
						t[i] = taxaList.stream().filter(j -> j.getName().contentEquals(taxaName)).findAny().get();
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
				}
				
								
				
				
				
			}
			System.out.println("number of quartet = "+ qc);
			System.out.println("number of unique quartet = "+quartetList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ql.sort(Comparator.comparing(Quartet::getQFrequency).reversed());
//		List<Quartet> sortedList = ql.stream()
//				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList());
		//LinkedHashSet<Quartet> nql = new LinkedHashSet<Quartet>(sortedList);
		LinkedHashSet<Quartet> countedSortedQL = new LinkedHashSet<Quartet>(quartetList.stream()
				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList()));

		double estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed Time : "+ estimatedTime/1000 + " seconds");
		for (Taxa taxa : taxaList) {
			System.out.print(taxa.getName()+"->");
		}
		
		
		System.out.println();
		////////////I will investigate this block later. whether it is really nrcessary or not
		int qID = 0;
		for (Quartet quartet : countedSortedQL) {
			qID++;
			quartet.setIncreaseFrequency(false);
			quartet.setQuartetID(qID);;
		}
		///////////////////////////////////////////////////////////////////////////////////
		//printQuartet(countedSortedQL);
		System.out.println("\n Number of taxa : "+taxaList.size());
		///////////////////////////////will delete
//		FM(taxaList, countedSortedQL);
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter("sample27.txt"))) {//args[1] is output file
//			//bw.write("("+s+")"+";");
//			for(Quartet quartets : countedSortedQL) {
//				
//				bw.write("("+quartets.getT1().getName()+","+quartets.getT1().getPartition()+")"+","
//						+"("+quartets.getT2().getName()+","+quartets.getT1().getPartition()+")"+"|"
//						+"("+quartets.getT3().getName()+","+quartets.getT1().getPartition()+")"+","
//						+"("+quartets.getT4().getName()+","+quartets.getT1().getPartition()+")"+":"
//						+quartets.getQFrequency()+"\n");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
		
		//////////////////////////////////

		String s = SQP(countedSortedQL, taxaList, 1000, 0);
		s = s.replace("(O,", "(0,");
		s = s.replace(",O,", ",0,");
		s = s.replace(",O)", ",0)");
		return s;
		//return null;
	
		
	}
	
	public static String SQP(LinkedHashSet<Quartet> quartetList, LinkedHashSet<Taxa> taxaList, int extraTaxa, int partSatCount) {
		int taxacount = taxaList.size();
//		System.out.println("******************SQP*****************");
//		System.out.println("Taxa Count = "+ taxacount);
//		System.out.println("Quartet Count = "+ quartetList.size());
		String s, extra = "extra";

		
		if (taxacount == 0) {
			s = ("");
			return s;
		}
		//quartetList.size() == 0 diye check korte hobe
		if (quartetList.isEmpty() || taxacount <3) {
//			System.out.println("Quartet list is empty");
			s = depthOneTree(taxaList);
		} else {
			MultiReturnType mrt = FM(taxaList, quartetList);
			LinkedHashSet<Taxa> partA = mrt.getPartA();
			LinkedHashSet<Taxa> partB = mrt.getPartB();
//			System.out.println("****************************SQP****************");
//			System.out.println("*****************PartA**************************");
//			printTaxa(partA);
//			System.out.println("*****************PartB**************************");
//			printTaxa(partB);
			//printQuartet(quartetList);
			int partSat = countSatisfiedQuartets(quartetList); // It returns # of satisfied quartets
			//System.out.println("partSat = "+ partSat);

			if(partSat==0){

	            partSatCount++;
	            if(partSatCount>100) //mc dependant step. No. of sat quartets = 0 in successive 20 iterations
	            {	
	            	System.out.println("partSatCount value = "+ partSatCount);
	            	partSatCount = 0;
	                s = depthOneTree(taxaList);
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
	        LinkedHashSet<Quartet> quartetA = new LinkedHashSet<Quartet>();
	        LinkedHashSet<Quartet> quartetB = new LinkedHashSet<Quartet>();
	        
//	        LinkedHashSet<Quartet> quartetAA = new LinkedHashSet<Quartet>();
//	        LinkedHashSet<Quartet> quartetBB = new LinkedHashSet<Quartet>();
//	        
//	        ArrayList<Quartet> quartetAAA = new ArrayList<Quartet>();
//	        ArrayList<Quartet> quartetBBB = new ArrayList<Quartet>();
	        
	        int numOfBDQ = 0; //number of b and deferred quartet
//	        int numOfB = 0;
//	        int numOfD = 0;
//	        int numOfBA = 0;
//	        int numOfBB = 0;
//	        int numOfDA = 0;
//	        int numOfDB = 0;
//	        int numOfDAA = 0;
//	        int numOfDBB= 0;
	        for (Quartet q : quartetList) {
				int l = q.getStatus().length();
				char c = q.getStatus().charAt(l-1);
//				Quartet qtemp = new Quartet(q.getT1(), q.getT2(), q.getT3(), q.getT4());
//				qtemp.setIncreaseFrequency(false);
//				qtemp.setStatus(q.getStatus());
//				qtemp.setQFrequency(q.getQFrequency());
				
				if (c == 'b' || c == 'd' ) {
					numOfBDQ++;
					q.setQuartetID(numOfBDQ);
					if (c == 'b') {
						//numOfB++;
						//if (partA.contains(q.getT1())) {
						if (q.getT1().getPartition() == 0) {	
							quartetA.add(q);
//							quartetAA.add(q);
//							quartetAAA.add(qtemp);
//							numOfBA++;
						} else {
							quartetB.add(q);
//							quartetBB.add(q);
//							quartetBBB.add(qtemp);
//							numOfBB++;
						}
					} else {
						//numOfD++;
						//int dcount = 0;
						int dcount = q.getT1().getPartition() + q.getT2().getPartition() + q.getT3().getPartition();
//						if (partA.contains(q.getT1()))dcount++;
//						if (partA.contains(q.getT2()))dcount++;
//						if (partA.contains(q.getT3()))dcount++;
						//if (partA.contains(q.getT4()))dcount++;
//						System.out.println("Quartet = "+ q.getT1().getName()+","+q.getT2().getName()
//								+"|"+q.getT3().getName()+","+q.getT4().getName()+":"+q.getQFrequency()+"->"+q.getStatus());
//						System.out.println("dcount = "+ dcount);
						
						if (dcount > 1) {
							if(q.getT1().getPartition() == 0)q.setT1(extraB);
							else if(q.getT2().getPartition() == 0)q.setT2(extraB);
							else if(q.getT3().getPartition() == 0)q.setT3(extraB);
							else q.setT4(extraB);
							quartetB.add(q);
//							if (partB.contains(q.getT1()))q.setT1(extraA);
//							else if (partB.contains(q.getT2()))q.setT2(extraA);
//							else if (partB.contains(q.getT3()))q.setT3(extraA);
//							else q.setT4(extraA);
//							/*if (partB.contains(q.getT1()))q.setT1(new Taxa(extra));
//							else if (partB.contains(q.getT2()))q.setT2(new Taxa(extra));
//							else if (partB.contains(q.getT3()))q.setT3(new Taxa(extra));
//							else q.setT4(new Taxa(extra));*/
							//quartetA.add(q);
//							quartetAAA.add(qtemp);
//							numOfDA++;
						} else {
							if(q.getT1().getPartition() == 1)q.setT1(extraA);
							else if(q.getT2().getPartition() == 1)q.setT2(extraA);
							else if(q.getT3().getPartition() == 1)q.setT3(extraA);
							else q.setT4(extraA);
							quartetA.add(q);
//							if (partA.contains(q.getT1()))q.setT1(extraB);
//							else if (partA.contains(q.getT2()))q.setT2(extraB);
//							else if (partA.contains(q.getT3()))q.setT3(extraB);
//							else q.setT4(extraB);
//							/*if (partA.contains(q.getT1()))q.setT1(new Taxa(extra));
//							else if (partA.contains(q.getT2()))q.setT2(new Taxa(extra));
//							else if (partA.contains(q.getT3()))q.setT3(new Taxa(extra));
//							else q.setT4(new Taxa(extra));*/
							//quartetB.add(q);
//							quartetBBB.add(qtemp);
//							numOfDB++;

						}
//						if (dcount == 1) {
//							if (partA.contains(q.getT1()))q.setT1(new Taxa(extra));
//							else if (partA.contains(q.getT2()))q.setT2(new Taxa(extra));
//							else if (partA.contains(q.getT3()))q.setT3(new Taxa(extra));
//							else q.setT4(new Taxa(extra));
//							
//							quartetBB.add(q);
//							numOfDBB++;
//						} else {
//							if (partB.contains(q.getT1()))q.setT1(new Taxa(extra));
//							else if (partB.contains(q.getT2()))q.setT2(new Taxa(extra));
//							else if (partB.contains(q.getT3()))q.setT3(new Taxa(extra));
//							else q.setT4(new Taxa(extra));
//							
//							quartetAA.add(q);
//							numOfDAA++;
//						}
					}
				}
			}
	       // System.out.println("numOfBDQ = "+numOfBDQ+"  QuartetA+B = "+(quartetA.size()+quartetB.size()));
//	        System.out.println("NumOfD = "+numOfD+"  numOfB = "+numOfB+ "  numOfD+B = "+(numOfD+numOfB));
//	        System.out.println("QuartetA = "+quartetA.size()+"  numOfDA = "+ numOfDA + "  numOfBA = "+numOfBA);
//	        System.out.println("QuartetB = "+quartetB.size()+"  numOfDB = "+ numOfDB + "  numOfBB = "+numOfBB);
//	        System.out.println("NumOfDAA = "+numOfDAA+"  numOfDBB = "+numOfDBB+ "  numOfDAA+DBB = "+(numOfDAA+numOfDBB));
	       // System.out.println("QuartetA = "+quartetA.size());
	        //System.out.println("QuartetB = "+quartetB.size());
//	        System.out.println("QuartetAAA = "+quartetAAA.size());
//	        System.out.println("QuartetBBB = "+quartetBBB.size());
	        
//	        System.out.println("**************SQP After partitioning quartet****************");
//	        printTaxa(partA);
//	        System.out.println("**************SQP After partitioning quartetA****************");
//	        printQuartet(quartetA);
//	        printTaxa(partB);
//	        System.out.println("**************SQP After partitioning quartetB****************");
//	        printQuartet(quartetB);
	        System.out.println("One SQP divide step is completed");
	        String s1 = SQP(quartetA, partA, extraTaxa, partSatCount);
	        String s2 = SQP(quartetB, partB, extraTaxa, partSatCount);
	        s = merge(s1,s2,extra);
	        //System.out.println("Merged Tree = "+s);

	       

	    }

	
		
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

		String fileName = "reroot.txt";
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
		String cmd = "perl reroot_tree_new.pl -t "+ s+";"+ " -r "+extra+ " -o "+ fileName;
		//System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Problem in cmd");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
			s = br.readLine();
//			System.out.println("br = "+s);
//			//String extra = "extra1000";
//			System.out.println(s);
			if (s != null) {
				s = s.replace(":", "");
				s = s.replace(";", "");
				int pos1 = s.indexOf(extra);
				s = s.replace(extra, "");
//				System.out.println("After removing extra s= "+s);
//				System.out.println("pos1 = "+pos1);

				int start = 0, i= 0, ob = 0, end = 0, cb = 0; String mystring; boolean removebr = false;
				if(s.charAt(pos1-1) =='('&& s.charAt(pos1) ==',')
			     {
					
					try{

						s = s.substring(0, pos1)+s.substring(pos1+1);
						 start = pos1-1;
//						System.out.println("inside s= "+ s+ " and start = "+start );

			           
			            i = start; ob = 0;
			            while(true)
			            {
			                if(s.charAt(i) == '(')
			                    ob++;
			                else if(s.charAt(i) == ')')
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
			        mystring = s.substring(start, end+1);
			        //System.out.println("mystring = "+mystring);
			        removebr = balance(mystring);
			        //System.out.println(removebr);
			        if(removebr)
			        {
			            try{


			                s = s.substring(0, end) + s.substring(end+1);
			                //System.out.println(s);
			                s = s.substring(0,start) + s.substring(start+1);

			            }
			            catch (Exception e) {

			            }

			        }

			    
			     }
			    else if(s.charAt(pos1-1) == ',' && s.charAt(pos1) == ')')
			    {

			        try{
			        	s = s.substring(0, pos1-1) + s.substring(pos1);
		
			            end = pos1-1;
			            i = end; cb = 0;
			            while(true)
			            {
			                if(s.charAt(i) == ')')
			                    cb++;
			                else if(s.charAt(i) == '(')
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
			        mystring = s.substring(start, end+1);
			        removebr = balance(mystring);
			        if(removebr)
			        {
			            try{

			            	s = s.substring(0, end) + s.substring(end + 1);
			                s = s.substring(0,start) + s.substring(start+1);

			            }
			            catch (Exception e) {

			            }

			        }



			    
			    }
			    else if(s.charAt(pos1-1) == ',' && s.charAt(pos1) == ',')
			    {

			        try{
			            s = s.substring(0, pos1-1) + s.substring(pos1);
			        	//s1.assign(s1.replace(pos1-1,1,""));
			        }
			        catch (Exception e) {
			            //cerr << "Out of Range error: " << oor.what() << endl;
			        }
			        if(s.charAt(pos1-2) == '(' && s.charAt(pos1) == ')')
			        {
			            try{
			                s = s.substring(0, pos1) + s.substring(pos1 + 1);
			                s = s.substring(0, pos1 - 2) + s.substring(pos1 - 1);
//			            	s2.assign(s2.replace(pos1,1,""));
//			                s2.assign(s2.replace(pos1-2,1,""));

			            }
			            catch (Exception e) {
			               // cerr << "Out of Range error: " << oor.what() << endl;
			            }
			        }



			    
			    }
			}
		

		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("before return s= "+s);
		return s;
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


	private static MultiReturnType FM(LinkedHashSet<Taxa> taxaList, LinkedHashSet<Quartet> quartetList) {
		LinkedHashSet<Taxa> partA = new LinkedHashSet<Taxa>();
		LinkedHashSet<Taxa> partB = new LinkedHashSet<Taxa>();
		int ca = 0, cb = 0;
		for (Quartet quartet : quartetList) {
			int tcount = 0;
			if (taxaList.isEmpty()) {
				//System.out.println("Finished");
				break;
			} else {
				if(taxaList.contains(quartet.getT1())) {
					taxaList.remove(quartet.getT1());
					tcount ++;
				}
				if(taxaList.contains(quartet.getT2())) {
					taxaList.remove(quartet.getT2());
					tcount ++;
				}
				if(taxaList.contains(quartet.getT3())) {
					taxaList.remove(quartet.getT3());
					tcount ++;
				}
				if(taxaList.contains(quartet.getT4())) {
					taxaList.remove(quartet.getT4());
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
				if (partA.contains(quartet.getT1())) a = 0;
				else if (partB.contains(quartet.getT1())) a = 1;
				
				if (partA.contains(quartet.getT2())) b = 0;
				else if (partB.contains(quartet.getT2())) b = 1;
				
				if (partA.contains(quartet.getT3())) c = 0;
				else if (partB.contains(quartet.getT3())) c = 1;
				
				if (partA.contains(quartet.getT4())) d = 0;
				else if (partB.contains(quartet.getT4())) d = 1;
				
		
				
				if(a==-1 && b==-1 && c==-1 && d==-1) //all new
			    {
			        ///This section can create problem in multithreading
					quartet.getT1().setPartition(0);
					partA.add(quartet.getT1());
			        a= 0; ca++;
			        quartet.getT2().setPartition(0);
					partA.add(quartet.getT2());
			        b= 0; ca++;
			        quartet.getT3().setPartition(1);
					partB.add(quartet.getT3());
			        c= 0; cb++;
			        quartet.getT4().setPartition(1);
					partB.add(quartet.getT4());
			        d= 0; cb++;
			     
			    }else {
			    	if(a==-1)
			        {	
			            if(b!=-1){
			                if(b==0) {quartet.getT1().setPartition(0); partA.add(quartet.getT1()); a=0; ca++;}
			                else {quartet.getT1().setPartition(1); partB.add(quartet.getT1()); a=1; cb++;}
			            }
			            else if(c!=-1){
			                if(c==1) {quartet.getT1().setPartition(0); partA.add(quartet.getT1()); a=0; ca++;}
			                else {quartet.getT1().setPartition(1); partB.add(quartet.getT1()); a=1; cb++;}
			            }
			            else if(d!=-1)
			            {
			                if(d==1) {quartet.getT1().setPartition(0); partA.add(quartet.getT1()); a=0; ca++;}
			                else {quartet.getT1().setPartition(1); partB.add(quartet.getT1()); a=1; cb++;}
			            }
			
			        }
			        if(b==-1)
			        {
			
			            if(a==0) {quartet.getT2().setPartition(0); partA.add(quartet.getT2()); b=0; ca++;}
			            else {quartet.getT2().setPartition(1); partB.add(quartet.getT2()); b=1; cb++;}
			
			        }
			        if(c==-1)
			        {
			            if(d!=-1)
			            {
			                if(d==0) {quartet.getT3().setPartition(0); partA.add(quartet.getT3()); c=0; ca++;}
			                else {quartet.getT3().setPartition(1); partB.add(quartet.getT3()); c=1; cb++;}
			            }
			            else{
			                if(a==1) {quartet.getT3().setPartition(0); partA.add(quartet.getT3()); c=0; ca++;}
			                else {quartet.getT3().setPartition(1); partB.add(quartet.getT3()); c=1;cb++;}
			            }
			        }
			        if(d==-1)
			        {
			            if(c==0) {quartet.getT4().setPartition(0); partA.add(quartet.getT4()); d=0;ca++;}
			            else {quartet.getT4().setPartition(1); partB.add(quartet.getT4()); d=1; cb++;}
			        }
			
				}
			}
				
		}
		int c = 0;
//		System.out.println("*********FM taxalist*********");
//		printTaxa(taxaList);
		if (!taxaList.isEmpty()) {
			
			for (Taxa taxa : taxaList) {
				
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
		return MFM_algo(partA, partB, quartetList);
		//return FM_algo(partA, partB, quartetList);
	}

//	private static MultiReturnType FM_algo(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB,
//			LinkedHashSet<Quartet> quartetList) {
//		String taxaToMove = null;
//		boolean loopAgain = true;
//		while (loopAgain) {
//			boolean iterationMore = true;
//			LinkedHashSet<GainList> movedList = new LinkedHashSet<GainList>();
//			while (iterationMore) {
//				System.out.println("****************Before Score and Gain**************");
//				printQuartet(quartetList);
//				int[] score= calculateScore(partB,quartetList,"null",0,0,0);
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
////				gainList = new Listt();
//				LinkedHashSet<GainList> gainList = new LinkedHashSet<GainList>();
//				Iterator<Taxa> iteratorA = partA.iterator();
//				Iterator<Taxa> iteratorB = partB.iterator();
//				//System.out.println("New Iteration");
//				while (flag) {
//					if (iteratorA.hasNext() && alt == 0) {
//						
//						Taxa taxaA = iteratorA.next();
//						if (!taxaA.isLocked()) {
//							taxaToMove = taxaA.getName();
//							System.out.println("Flag taxa = "+ taxaToMove);
//	                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
//	                        System.out.println("Inside Flag: score[0]-prevScore = "+(score[0]-prevScore)
//	                        		+"  Score[0] = "+score[0]+"  score[1] = "+score[1]
//	                        		+"  score[2] = "+score[2]+"  score[3] = "+score[3]);
//							gainList.add(new GainList(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], ca-1, 0));
//							
//						}
////						if (tag2 == 0 && iteratorB.hasNext()) {
////							alt = 1;
////						}
//						
//						
//					} else if (iteratorB.hasNext() && alt == 1) {
//						Taxa taxaB = iteratorB.next();
//						if (!taxaB.isLocked()) {
//							taxaToMove = taxaB.getName();
//							System.out.println("Flag taxa = "+ taxaToMove);
//	                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
//	                        System.out.println("Inside Flag: score[0]-prevScore = "+(score[0]-prevScore)
//	                        		+"  Score[0] = "+score[0]+"  score[1] = "+score[1]
//	                        		+"  score[2] = "+score[2]+"  score[3] = "+score[3]);
//							gainList.add(new GainList(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], cb-1, 1));
//							
//						}
////						if (tag1 == 0 && iteratorA.hasNext()) {
////							alt = 0;
////						}
//						
//						
//					} 
//					
//					if (!iteratorA.hasNext()) {
//						tag1 = 1;
//					}
//					if (!iteratorB.hasNext()) {
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
//				}
//				System.out.println("********************Gain List*************");
//				for (GainList gl : gainList) {
//					 System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
//		                		gl.getDef()+" "+gl.getBel0w());
//				}
//				printQuartet(quartetList);
//				
////				quartetList.stream()
////				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList())
//				///////Moving Taxa which have highest gain
//				int maxgain = -1000000000; //double
//				int glPart = 0;
//				taxaToMove = null;
//				
//				
//				/////////////////////
//
////				//maxgain = -1000000000; //double
////				int maxsat = 0;
////	            //glPart = 0;
////	            int randnum = 0;
////				
////				for (GainList gl : gainList) {
////					if (gl.getVal() > maxgain && gl.getBel0w() >= 2 ) {
////						taxaToMove = gl.getTaxaToMove();
////	                    maxgain = gl.getVal();               
////	                    maxsat = gl.getSat();
////	                    glPart = gl.getPart(); // current Partition
////					} else if(gl.getVal() == maxgain && gl.getBel0w() >= 2 ){
////						 
////	                   
////	                    if(gl.getSat() > maxsat && gl.getBel0w() >= 2)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
////	                    {
////	                        taxaToMove = gl.getTaxaToMove();
////	                        maxgain = gl.getVal();
////	                        maxsat = gl.getSat();
////	                        glPart = gl.getPart();
////	                    }
////	                    else if(gl.getSat() == maxsat && gl.getBel0w() >= 2)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
////	                    {
////	                       randnum = 10 + (new Random().nextInt(100));///rand()%100;
////	                        if(randnum%2 == 0){
////	                            taxaToMove = gl.getTaxaToMove();
////	                            maxgain = gl.getVal();                       
////	                            maxsat = gl.getSat();
////	                            glPart = gl.getPart(); // current Partition
////	                        }
////	                        //}
////
////	                    }
////
////	                
////					}
////				}
//			
//				
//				
//				//////////////////////
//				
//				GainList movedTaxa = new GainList();
//				movedTaxa = gainList.stream().max(Comparator.comparing(GainList::getVal).thenComparing(GainList::getSat)).get();
//				
//				if (movedTaxa.getBel0w() >= 2) {
//					taxaToMove = movedTaxa.getTaxaToMove();
//					maxgain = movedTaxa.getVal();
//					glPart = movedTaxa.getPart();
//				} else {
//					maxgain = -1000000000; //double
//					int maxsat = 0;
//		            glPart = 0;
//		            int randnum = 0;
//					
//					for (GainList gl : gainList) {
//						if (gl.getVal() > maxgain && gl.getBel0w() >= 2 ) {
//							taxaToMove = gl.getTaxaToMove();
//		                    maxgain = gl.getVal();               
//		                    maxsat = gl.getSat();
//		                    glPart = gl.getPart(); // current Partition
//						} else if(gl.getVal() == maxgain && gl.getBel0w() >= 2 ){
//							 
//		                   
//		                    if(gl.getSat() > maxsat && gl.getBel0w() >= 2)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
//		                    {
//		                        taxaToMove = gl.getTaxaToMove();
//		                        maxgain = gl.getVal();
//		                        maxsat = gl.getSat();
//		                        glPart = gl.getPart();
//		                    }
//		                    else if(gl.getSat() == maxsat && gl.getBel0w() >= 2)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
//		                    {
//		                       randnum = 10 + (new Random().nextInt(100));///rand()%100;
//		                        if(randnum%2 == 0){
//		                            taxaToMove = gl.getTaxaToMove();
//		                            maxgain = gl.getVal();                       
//		                            maxsat = gl.getSat();
//		                            glPart = gl.getPart(); // current Partition
//		                        }
//		                        //}
//
//		                    }
//
//		                
//						}
//					}
//				}
//				
//			
//				if (taxaToMove != null) {
//					final String taxaMove = taxaToMove;
//					if (glPart == 1) {
//						partB.removeIf(i -> i.getName().contentEquals(taxaMove));
//						partA.add(new Taxa(taxaToMove, 0, true));
//					} else {
//						partA.removeIf(i -> i.getName().contentEquals(taxaMove));
//						partB.add(new Taxa(taxaToMove, 1, true));
//					}
//					movedList.add(new GainList(taxaToMove, maxgain, 1-glPart));
//					if (gainList.size() == 1)
//						iterationMore = false;
//				} else {
//					iterationMore = false;
//				}
//				
//				/*
//				 * I will check this portion of code later. This part is efficient. 
//				 * But gl.below() must be checked 
//				 * GainList movedTaxa = new GainList();
//				movedTaxa = gainList.stream().max(Comparator.comparing(GainList::getVal).thenComparing(GainList::getSat).).get();
//				taxaToMove = movedTaxa.getTaxaToMove();
//				System.out.println("Taxa to move = "+taxaToMove);
//				final String taxaMove = taxaToMove;
//				if (movedTaxa.getPart() == 1) {
//					partB.removeIf(i -> i.getName().contentEquals(taxaMove));
//					partA.add(new Taxa(taxaToMove, 0, true));
//				} else {
//					partA.removeIf(i -> i.getName().contentEquals(taxaMove));
//					partB.add(new Taxa(taxaToMove, 1, true));
//				}
//				movedTaxa.setPart(1-movedTaxa.getPart());
//				movedList.add(movedTaxa);
//				
//				if (gainList.size() == 1) {
//					iterationMore = false;
//				}else {
//					System.out.println("Next Iteration");
//				}
//				*/
//				
//				
//			}///no more iteration
//			System.out.println("********************Moved List*************");
//			for (GainList gl : movedList) {
//				 System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
//	                		gl.getDef()+" "+gl.getBel0w());
//			}
//			int cumulativeGain = 0, gainMax = 0;
//			String back = "Initial";
//			for (GainList ml : movedList) {
//				cumulativeGain += ml.getVal();
//				if (cumulativeGain >= gainMax) {
//					gainMax = cumulativeGain;
//					back = ml.getTaxaToMove();
//				}
//			}
//			System.out.println("cumulative gain = "+ cumulativeGain);
//            System.out.println(" taxa to move = "+ taxaToMove);
//            System.out.println(" back taxa = "+ back);
//            System.out.println("partA and partB");
//            printTaxa(partA);
//            printTaxa(partB);
//            boolean isMove = false;
//            LinkedHashSet<Taxa> pa = new LinkedHashSet<Taxa>();
//            LinkedHashSet<Taxa> pb = new LinkedHashSet<Taxa>();
//            for (GainList ml : movedList) {
//            	String moveTaxa = ml.getTaxaToMove();
//            	if (isMove) {
//            		if (ml.getPart() == 1) {
//    					partB.removeIf(i -> i.getName().contentEquals(moveTaxa));
//    					pa.add(new Taxa(moveTaxa, 0));
//    					//partA.add(new Taxa(moveTaxa, 0));
//    				} else {
//    					partA.removeIf(i -> i.getName().contentEquals(moveTaxa));
//    					pb.add(new Taxa(moveTaxa, 1));
//    					//partB.add(new Taxa(moveTaxa, 1));
//
//    				}
//				}
//				if (moveTaxa.contentEquals(back)) {
//					isMove = true;
//				}
//			}
//            ///PartA
//            if (partA.size() == 1) {
//				pa.addAll(partA);
//			} else if (!partA.isEmpty()) {
//				ArrayList<Taxa> reverseTaxaList = new ArrayList<Taxa>(partA);
//	            Collections.reverse(reverseTaxaList);
//	            pa.addAll(reverseTaxaList);
//			}
//            //PartB
//            if (partB.size() == 1) {
//				pb.addAll(partB);
//			} else if (!partB.isEmpty()) {
//				ArrayList<Taxa> reverseTaxaList = new ArrayList<Taxa>(partB);
//	            Collections.reverse(reverseTaxaList);
//	            pb.addAll(reverseTaxaList);
//			}
//            ////
//            partA = new LinkedHashSet<Taxa>(pa);
//            partB = new LinkedHashSet<Taxa>(pb);
//            System.out.println("After moving partA and partB");
//            printTaxa(partA);
//            printTaxa(partB);
//            for (Taxa taxa : partA) {
//				taxa.setLocked(false);
//			}
//            for (Taxa taxa : partB) {
//				taxa.setLocked(false);
//			}
//            
//            
//          
//            if (gainMax <= 0) {
//            	System.out.println("Looop again is finished");
//            	loopAgain = false;
//			}
//			
//		}//no more loop
//		 //************end of Loop Again**********//
//        //***********Merge Two list***************//
//        /*
//         * int partSat = countSatisfiedQuartets(partA, partB,quartetList);
//        System.out.println("PartSat = "+partSat);
//        System.out.println("After loop again part a nd b");
//        printTaxa(partA);
//        printTaxa(partB);
//        LinkedHashSet<Taxa> finalTaxaList = new LinkedHashSet<Taxa>(partA);
//        finalTaxaList.addAll(partB);
//        printTaxa(finalTaxaList);
//        */
//		System.out.println("************************FM_algo Quartet List*************************");
//		printQuartet(quartetList);
//		return new MultiReturnType(partA, partB);
//	}
	
	
	private static int countSatisfiedQuartets(LinkedHashSet<Quartet> quartetList) {
		int csat = 0;
	    char quartetScore;
	    for (Quartet quartet : quartetList) {
			quartetScore = mCheckQuartet(quartet, "null");
			if(quartetScore == 's')
	        {
	        	csat += quartet.getQFrequency();
	        }
		}
	    return csat;
	}

	private static int[] calculateScore(LinkedHashSet<Taxa> partB, LinkedHashSet<Quartet> quartetList, String tempTaxa,
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

	private static void printQuartet(LinkedHashSet<Quartet> quartetList) {
		System.out.println("**********************Quartet List********************************");
		for(Quartet quartets : quartetList) {
			
			System.out.println(quartets.getQuartetID()+" : "+quartets.getT1().getName()+","+quartets.getT2().getName()+"|"
					+quartets.getT3().getName()+","+quartets.getT4().getName()+":"+quartets.getQFrequency()+"->"
					+quartets.isIncreaseFrequency()+" -> "+ quartets.getStatus()+"\n");
		}
		System.out.println("*********************End of Printing Quartet**********************");
		
	}

	private static void printTaxa(LinkedHashSet<Taxa> taxaList) {
		System.out.println("**********************Taxa List********************************");
		for (Taxa taxa : taxaList) {
			System.out.print(taxa.getName()+"->");
		}
		System.out.println("end");
	
		
	}

	private static String depthOneTree(LinkedHashSet<Taxa> taxaList) {
		String s = "(";
		int taxaCount = taxaList.size(), count = 0;
		for (Taxa taxa : taxaList) {
			 s += taxa.getName();
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
			LinkedHashSet<Quartet> quartetList) {
		//String taxaToMove = null;
		boolean loopAgain = true;
		while (loopAgain) {
			boolean iterationMore = true; int iteration = 0;
			int ca = partA.size();
			int cb = partB.size();
			int arraysize = ca + cb;
			List<GainList> movedList;
			if (arraysize > 0) {
				movedList = new ArrayList<GainList>(arraysize);
			} else {
				movedList = new ArrayList<GainList>();
			}
			
			LinkedHashSet<Quartet> rQuartetList = new LinkedHashSet<Quartet>();//reduced quartetList(quartets with movedTaxa);
			int prevScore =0, prevS = 0, prevV = 0, prevD = 0;
			while (iterationMore) {
				iteration++;
				int[] score;
				if (iteration == 1) {
					score= iCalculateScore(quartetList);
					//score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
					prevScore = score[0];//partition score
		            prevS = score[1];//noOfSat
		            prevV = score[2];//noOfVat
		            prevD = score[3];//noOfDef
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
				boolean flag = true; int tag1 = 0, tag2 = 0, alt = 0;
//				gainList = new Listt();
				//LinkedHashSet<GainList> gainList = new LinkedHashSet<GainList>();
				List<GainList> gainList = new LinkedList<GainList>();
					
				Iterator<Taxa> iteratorA = partA.iterator();
				Iterator<Taxa> iteratorB = partB.iterator();
				//System.out.println("New Iteration");
				while (flag) {
					if (iteratorA.hasNext() && alt == 0) {
						
						Taxa taxaA = iteratorA.next();
						//if (!taxaA.isLocked()) {
							//taxaToMove = taxaA.getName();
							//System.out.println("Flag taxa = "+ taxaToMove);
							if (iteration == 1) {
								//taxaA.getSvdTable().clear();
								score = mCalculateScore(taxaA.getSvdTable(), quartetList, taxaA.getName(), prevS, prevV, prevD);
							} else {
								score = mCalculateScore(taxaA.getSvdTable(), rQuartetList, taxaA.getName(), prevS, prevV, prevD);
							}
							
//	                        System.out.println("Inside Flag: score[0]-prevScore = "+(score[0]-prevScore)
//	                        		+"  Score[0] = "+score[0]+"  score[1] = "+score[1]
//	                        		+"  score[2] = "+score[2]+"  score[3] = "+score[3]);
							gainList.add(new GainList(taxaA, score[0]-prevScore, score[1], score[2], score[3], ca-1));
							
						//}
//						if (tag2 == 0 && iteratorB.hasNext()) {
//							alt = 1;
//						}
						
						
					} else if (iteratorB.hasNext() && alt == 1) {
						Taxa taxaB = iteratorB.next();
						//if (!taxaB.isLocked()) {
							//taxaToMove = taxaB.getName();
							//System.out.println("Flag taxa = "+ taxaToMove);
							if (iteration == 1) {
								//taxaB.getSvdTable().clear();
								 score = mCalculateScore(taxaB.getSvdTable(), quartetList, taxaB.getName(), prevS, prevV, prevD);
							} else {
								 score = mCalculateScore(taxaB.getSvdTable(), rQuartetList, taxaB.getName(), prevS, prevV, prevD);
							}
	                      
//	                        System.out.println("Inside Flag: score[0]-prevScore = "+(score[0]-prevScore)
//	                        		+"  Score[0] = "+score[0]+"  score[1] = "+score[1]
//	                        		+"  score[2] = "+score[2]+"  score[3] = "+score[3]);
							gainList.add(new GainList(taxaB, score[0]-prevScore, score[1], score[2], score[3], cb-1));
							
						//}
//						if (tag1 == 0 && iteratorA.hasNext()) {
//							alt = 0;
//						}
						
						
					} 
					
					if (!iteratorA.hasNext()) {
						tag1 = 1;
					}
					if (!iteratorB.hasNext()) {
						tag2 = 1;
					}
					if (tag1 == 1 && tag2 == 1) {
						flag = false;
					}
					if (tag2 == 0 && alt == 0) {
						alt = 1;
					}else if (tag1 == 0 && alt == 1) {
						alt= 0;
					}
				}
//				System.out.println("********************Gain List*************");
//				for (GainList gl : gainList) {
//					 System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
//		                		gl.getDef()+" "+gl.getBel0w());
//				}
//				printQuartet(quartetList);
				
//				quartetList.stream()
//				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList())
				///////Moving Taxa which have highest gain
				int maxgain = -1000000000; //double
				//int glPart = 0;
				//taxaToMove = null;

				
				GainList movedTaxa = gainList.stream().max(Comparator.comparing(GainList::getVal).thenComparing(GainList::getSat)).get();
				Taxa taxa_to_move = new Taxa("", -1);
				if (movedTaxa.getBel0w() >= 2) {
					taxa_to_move = movedTaxa.getTaxa();
					//taxaToMove = taxa_to_move.getName();
					maxgain = movedTaxa.getVal();
					//glPart = taxa_to_move.getPartition();
	    	        prevS = movedTaxa.getSat() ;//score[1];//noOfSat
	    	        prevV = movedTaxa.getVat();//score[2];//noOfVat
	    	        prevD = movedTaxa.getDef();//score[3];//noOfDef
				} else {
					maxgain = -1000000000; //double
					int maxsat = 0;
		            //glPart = 0;
		            int randnum = 0;
		            int mVat = 0;
		            int mDef = 0;
					
					for (GainList gl : gainList) {
						if (gl.getVal() > maxgain && gl.getBel0w() >= 2 ) {
							taxa_to_move = gl.getTaxa();
							//taxaToMove = taxa_to_move.getName();
		                    maxgain = gl.getVal();               
		                    maxsat = gl.getSat();
		                    //glPart = taxa_to_move.getPartition(); // current Partition
		                    mVat = gl.getVat();
		                    mDef = gl.getDef();
		                 
						} else if(gl.getVal() == maxgain && gl.getBel0w() >= 2 ){
							 
		                   
		                    if(gl.getSat() > maxsat && gl.getBel0w() >= 2)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
		                    {
		                    	taxa_to_move = gl.getTaxa();
								//taxaToMove = taxa_to_move.getName();
			                    maxgain = gl.getVal();               
			                    maxsat = gl.getSat();
			                   // glPart = taxa_to_move.getPartition(); // current Partition
			                    mVat = gl.getVat();
			                    mDef = gl.getDef();
		                    }
		                    else if(gl.getSat() == maxsat && gl.getBel0w() >= 2)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
		                    {
		                       randnum = 10 + (new Random().nextInt(100));///rand()%100;
		                        if(randnum%2 == 0){
		                        	taxa_to_move = gl.getTaxa();
									//taxaToMove = taxa_to_move.getName();
				                    maxgain = gl.getVal();               
				                    maxsat = gl.getSat();
				                    //glPart = taxa_to_move.getPartition(); // current Partition
				                    mVat = gl.getVat();
				                    mDef = gl.getDef();
		                        }
		                        //}

		                    }

		                
						}
					}
					prevS = maxsat;//score[1];//noOfSat
	    	        prevV = mVat;//score[2];//noOfVat
	    	        prevD = mDef;//score[3];//noOfDef
	    	        
					
				}
				
				int glPart = taxa_to_move.getPartition();
				prevScore = prevS - prevV;//partition score
				if (glPart != -1) {
					if (gainList.size() == 1) {
//						mCalculateScore(null,partB,quartetList,"null",0,0,0);
//						System.out.println("gainlist.size == 1");
						iterationMore = false;
					}
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
					movedList.add(new GainList(taxa_to_move, maxgain));
					
					////////////Extracting quartets which have moved_taxa
					
					rQuartetList.clear();
					
					
					for (SVD_Log svd : taxa_to_move.getSvdTable()) {
						Quartet q = svd.getQuartet();
						q.setStatus(svd.getqStat()+"");
						rQuartetList.add(q);
					}
					taxa_to_move.getSvdTable().clear();
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
			//boolean isMove = false;
			for (GainList ml : movedList) {
				cumulativeGain += ml.getVal();
				if (cumulativeGain >= gainMax) {
					gainMax = cumulativeGain;
					//back = ml.getTaxa().getName();
					backIndex = bi;
					//isMove = true;
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
			if (backIndex != -1) {
				for (int i = backIndex + 1; i < movedList.size(); i++) {
					Taxa moveTaxa = movedList.get(i).getTaxa();
	            
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
					Taxa moveTaxa = movedList.get(i).getTaxa();
	            
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
	private static int[] mCalculateScore(HashSet<SVD_Log> svdTable, LinkedHashSet<Quartet> quartetList, String tempTaxa,
			int st, int vt, int df) {
		
		int[] scores = {0,0,0,0};
	    char  qStat;
	    int s = 0, v = 0, d = 0;
	    char c;
	    //only tempTaxa ta jei shob quartet a ase, oi gulo niye new Qlist korte hobe
	    for (Quartet q : quartetList) {
	    	if(q.getT1().getName().contentEquals(tempTaxa) || q.getT2().getName().contentEquals(tempTaxa) ||
	        		q.getT3().getName().contentEquals(tempTaxa) || q.getT4().getName().contentEquals(tempTaxa))
	        {	
	    		s = 0; v = 0; d = 0;
	    		qStat  = mCheckQuartet(q, tempTaxa);
	            c = q.getStatus().charAt(0);//status[0];
//	            System.out.println(q.getT1().getName()+","+q.getT2().getName()
//	            		+"|"+q.getT3().getName()+","+q.getT4().getName()+":"+q.getQFrequency()+"->"+q.getStatus());
//	            System.out.println("tempTaxa = "+tempTaxa+ "  qscore = "+qscore+"  c = "+ c);

	            if(c=='s' && qStat == 'v') { s = - q.getQFrequency(); v =  q.getQFrequency();} // s v

	            else if(c=='s' && qStat == 'd'){ s = - q.getQFrequency(); d =  q.getQFrequency();} // s d

	            else if(c=='v' && qStat == 's'){v = - q.getQFrequency(); s = q.getQFrequency();} // v s

	            else if(c=='v' && qStat == 'd'){v = - q.getQFrequency();d = q.getQFrequency();}  // v d

	            else if(c=='d' && qStat == 'v'){d = - q.getQFrequency();v = q.getQFrequency();} // d v

	            else if(c=='d' && qStat == 's'){d = - q.getQFrequency();s = q.getQFrequency();} // d s

	            else if(qStat == 'b')
	            {
	                if(c=='s') { s = - q.getQFrequency(); c = 'b';}
	                else if(c=='v') { v = - q.getQFrequency(); c = 'b';}
	                else if(c=='d') { d = - q.getQFrequency(); c = 'b';}

	            }
	            else if(c=='b')
	            {
	                if(qStat == 's') { s = q.getQFrequency(); c = 's';}
	                else if(qStat == 'v') { v = q.getQFrequency(); c = 'v';}
	                else if(qStat == 'd') { d = q.getQFrequency();c = 'd';}
	            }
	            
	            svdTable.add(new SVD_Log(q, s, v, d, qStat));
	            

	        }
		}
	    s = 0; v = 0; d = 0;
    	for (SVD_Log svd : svdTable) {
			s += svd.getSat();
			v += svd.getVat();
			d += svd.getDef();
		}
    	scores[1] = st+s ;//noOfSat = st+s;
        scores[2] = vt+v;//noOfVat = vt+v;
        scores[3] = df+d;//noOfDef = df+d;

        scores[0]= scores[1]-scores[2];//partitionScore = ((st+s)-(vt+v));
	    
	    //System.out.println("s = "+s+"  v = "+v+"  d = "+d);
		return scores;
	 
	}
	
	private static int[] iCalculateScore(LinkedHashSet<Quartet> quartetList) {
		//initial_calculate_score
		int[] scores = {0,0,0,0};
	    char  qStat;
	    	 
	    for (Quartet q : quartetList) {
            q.setStatus("");
            qStat  = mCheckQuartet(q, "null");
            if(qStat == 's') scores[1] = scores[1] + q.getQFrequency();//number of satisfied quartet, s
            else if(qStat == 'v') scores[2] = scores[2] + q.getQFrequency();//number of violated quartet, v
            else if(qStat == 'd') scores[3] = scores[3] + q.getQFrequency();//number of deferred quartet, d
	        
	    	
		}
	    scores[0]=(scores[1] - scores[2]);//partitionScore = (s-v);
	
		return scores;
	 
	}
	private static char mCheckQuartet(Quartet q, String tempTaxa) {
		//int a = 0, b = 0, c = 0, d = 0;
		char qstat;
		///////will delete
		//following code should work. will investigate later
//		if(q.getT1().getPartition() == 1) a = 1;
//		if(q.getT2().getPartition() == 1) b = 1;
//		if(q.getT3().getPartition() == 1) c = 1;
//		if(q.getT4().getPartition() == 1) d = 1;
		int a = q.getT1().getPartition();
		int b = q.getT2().getPartition();
		int c = q.getT3().getPartition();
		int d = q.getT4().getPartition();
		/////////////
		/*
		if(partB.contains(q.getT1())) a = 1;
		if(partB.contains(q.getT2())) b = 1;
		if(partB.contains(q.getT3())) c = 1;
		if(partB.contains(q.getT4())) d = 1;
		*/
		////////will change above block of code later
//		if (!tempTaxa.contentEquals("null")) {
			if(tempTaxa.contentEquals(q.getT1().getName())) a = 1 - a;
		    else if(tempTaxa.contentEquals(q.getT2().getName())) b = 1-b;
		    else if(tempTaxa.contentEquals(q.getT3().getName())) c = 1-c;
		    else if(tempTaxa.contentEquals(q.getT4().getName())) d = 1-d;
//		}	
		
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

	    q.setStatus(q.getStatus()+qstat);
		return qstat;

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