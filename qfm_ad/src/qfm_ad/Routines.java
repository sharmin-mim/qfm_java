package qfm_ad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;



public class Routines {
	public static MultiReturnType readQuartetQMC(String fileName) { // count will be done at the time of reading

		LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>();
		HashSet<Quartet> quartetList = new HashSet<Quartet>();
		double startTime = System.currentTimeMillis();
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))){
			int count=0, qc = 0;//count-> only counts unique quartet. qc-> counts all quartet
			while (scanner.hasNext()) {
				
				String singleQuartet = scanner.next();	
				//String[] qq = singleQuartet.split(",|\\|");/// for quartet format q1,q2|q3,q4
				String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight
				//Taxa t1, t2, t3, t4;
				Taxa t1 = new Taxa(qq[0]);
				Taxa t2 = new Taxa(qq[1]);
				Taxa t3 = new Taxa(qq[2]);
				Taxa t4 = new Taxa(qq[3]);
				taxaList.add(t1);
				taxaList.add(t2);
				taxaList.add(t3);
				taxaList.add(t4);
				
				if(quartetList.contains(new Quartet(t1, t2, t3, t4))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t2, t1, t3, t4))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t1, t2, t4, t3))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t2, t1, t4, t3))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t3, t4, t1, t2))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t3, t4, t2, t1))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t4, t3, t1, t2))) {
					qc++;
				}else if (quartetList.contains(new Quartet(t4, t3, t2, t1))) {
					qc++;
				}else {
					quartetList.add(new Quartet(t1, t2, t3, t4));
					count++;
					qc++;
				}
				
								
				
				
				
			}
			System.out.println("number of quartet = "+ qc);
			System.out.println("number of unique quartet = "+count);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ql.sort(Comparator.comparing(Quartet::getQFrequency).reversed());
//		List<Quartet> sortedList = ql.stream()
//				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList());
		//LinkedHashSet<Quartet> nql = new LinkedHashSet<Quartet>(sortedList);
		LinkedHashSet<Quartet> countedSortedQL = new LinkedHashSet<Quartet>(quartetList.stream()
				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList()));
//		for (Quartet quartet : sortedList) {
//			nql.add(quartet);
//		}
		double estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed Time : "+ estimatedTime/1000 + " seconds");
		for (Taxa taxa : taxaList) {
			System.out.print(taxa.getName()+"->");
		}
		
		//ql.sort(Comparator.comparing(Quartet::getQFrequency).reversed());
		System.out.println();
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"))) {//args[1] is output file
//			for(Quartet quartets : countedSortedQL) {
//				
//				bw.write(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
//						+","+quartets.getT4().getName()+":"+quartets.getQFrequency()+"->"+quartets.isIncreaseFrequency()+"\n");
//				
////				System.out.println(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
////						+","+quartets.getT4().getName()+":"+quartets.getQFrequency());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		for (Quartet quartet : countedSortedQL) {
			quartet.setIncreaseFrequency(false);;
		}
		
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter("out1.txt"))) {//args[1] is output file
//			for(Quartet quartets : countedSortedQL) {
//				
//				bw.write(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
//						+","+quartets.getT4().getName()+":"+quartets.getQFrequency()+"->"+quartets.isIncreaseFrequency()+"\n");
//				
////				System.out.println(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
////						+","+quartets.getT4().getName()+":"+quartets.getQFrequency());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		System.out.println("\n elements : "+taxaList.size());
		
		FM(taxaList, countedSortedQL);
		return new MultiReturnType(countedSortedQL, taxaList);
		
	}
	
	public static String SQP(LinkedHashSet<Quartet> quartetList, LinkedHashSet<Taxa> taxaList, int extraTaxa, int partSatCount) {
		int taxacount = taxaList.size();
		String s, s1, s2, extra = "extra";
		Taxa pa, pb, partA, partB;
		Quartet qa, qb, quartetA, quartetB, qtemp;
		
		if (taxacount == 0) {
			s = ("");
			return s;
		}
		//quartetList.size() == 0 diye check korte hobe
		if (quartetList.isEmpty() || taxacount <3) {
			s = depthOneTree(taxaList);
		} else {
			MultiReturnType mrl = FM(taxaList, quartetList);
//			//Taxa p = FM(taxaList, quartetList);
//			Taxa p = mrl.getTaxa();
//			//printTaxa(p);
//			//System.out.println("partSat = "+ partSat);
//			int partSat = mrl.getAnyCount(); // It returns # of satisfied quartets
//			if(partSat==0){
//
//	            partSatCount++;
//	            if(partSatCount>100) //mc dependant step. No. of sat quartets = 0 in successive 20 iterations
//	            {	
//	            	System.out.println("partSatCount value = "+ partSatCount);
//	            	partSatCount = 0;
//	                s = depthOneTree(taxaList);
//	                return s;
//	            }
//	        }
//	        else partSatCount=0;
	        
			extra = "extra"+ extraTaxa;
	        System.out.println("extra = "+ extra);
	        extraTaxa++;

//	        partA = new Taxa();
//	        pa = partA;
//	        partB = new Taxa();
//	        pb = partB;
//	        
//	        while(p.tnext!= null)
//	        {
//	            p = p.tnext;
//	            if(p.getPartition() == 0){
//	                pa.tnext = new Taxa(p.getName(), 0);
//	                pa= pa.tnext;
//	            }
//
//	            else{
//	                
//	                pb.tnext = new Taxa(p.getName(), 1);
//	                pb= pb.tnext;
//	            }
//
//	        }
////	        System.out.println("***************partA*****************");
////	        printTaxa(partA);
////	        System.out.println("***************partB*****************");
////	        printTaxa(partB);
//	        pa.tnext = new Taxa(extra, 0); ///add extra taxa to partition A
//	        pa= pa.tnext;
//	        pb.tnext = new Taxa(extra, 1);// add extra taxa to parttion B
//	        pb= pb.tnext;
//
////	        printTaxa(partA);
////	        printTaxa(partB);
//	        quartetA = new Quartet();
//	        qa = quartetA;
//	        quartetB = new Quartet();
//	        qb = quartetB;
//	        String t1,t2,t3,t4;
//	        char c;
//	        int l,dcount=0;
//	        while(quartetList.qnext !=  null)
//	        {
//	            quartetList = quartetList.qnext;
//	            l = quartetList.getStatus().length();
//	            c = quartetList.getStatus().charAt(l-1);
////	            if(debug)
////	                cout<< "Status of Quartet "<<Q->quartet_id<<"= "<<c<<endl;
//	            if(c == 'b' || c == 'd' )
//	            {
//	                //Q = Q->qnext;
//	                qtemp =  new Quartet(quartetList.getQ1(), quartetList.getQ2(), quartetList.getQ3(),
//	                		quartetList.getQ4(), quartetList.getQuartet_id());
//	                
//	                qtemp.setStatus(quartetList.getStatus());
//	                qtemp.setQFrequency(quartetList.getQFrequency());
////	                if(debug)
////	                    cout << "....q1 ="<<Q->q1<<"....q2 ="<<Q->q2<<"....q3 ="<<Q->q3<<"....q4 ="<<Q->q4<<".......\n";
//
//	                if(c=='b')
//	                {
//	                    pa = partA.tnext;
//	                    while(pa!= null && (pa.getName().contentEquals(quartetList.getQ1())) != true ) // logic error
//	                    {
//	                        pa = pa.tnext;
//	                        //if(debug)
//	                        //	cout<<"both side\n";
//	                    }
//
//	                    if(pa== null)
//	                    {	//if(debug)
//	                        //	cout<< "........else.......\n";
//	                        qb.qnext = qtemp;
//	                        qb = qb.qnext;
//	                        //place Q to QB
//	                    }
//	                    else //((pa->name.compare(Q->q1))==0)// q1 on part A
//	                    {
//	                        //if(debug)
//	                        //	cout<< "........if.......\n";
//	                        qa.qnext = qtemp;
//	                        qa = qa.qnext;
//	                        //place Q to QA
//	                    }
//	                }
//	                else // deferred
//	                {	qtemp.setModified(1);
//	                    dcount =0 ;
//	                    pa = partA.tnext;
//	                    while(pa!= null)
//	                    {
//	                        if(pa.getName().contentEquals(quartetList.getQ1()) || pa.getName().contentEquals(quartetList.getQ2())||
//	                        		pa.getName().contentEquals(quartetList.getQ3())|| pa.getName().contentEquals(quartetList.getQ4()))
//	                            dcount++;
//	                        pa = pa.tnext;
//	                        if(dcount==3)
//	                        {
//	                            //cout<<"dcount  =  "<<dcount<<endl;
//	                            break;
//	                        }
//	                    }
//	                    if (dcount == 1) { // find for either mathch for q1, q2, q3, q4 on partB, change it
//	                    	pa = partA.tnext;
//	                    	 while(pa.getName().contentEquals(quartetList.getQ1()) != true && 
//		                        		pa.getName().contentEquals(quartetList.getQ2()) != true && 
//		                        				pa.getName().contentEquals(quartetList.getQ3()) != true && 
//		                        				pa.getName().contentEquals(quartetList.getQ4()) != true)
//		                        {
//		                            pa = pa.tnext;
//		                        }
//	                    	 if (pa.getName().contentEquals(quartetList.getQ1())) qtemp.setQ1(extra);
//	                    	 else if (pa.getName().contentEquals(quartetList.getQ2())) qtemp.setQ2(extra);
//	                    	 else if (pa.getName().contentEquals(quartetList.getQ3())) qtemp.setQ3(extra);
//	                    	 else qtemp.setQ4(extra);
//	                    	 
//	                    	 qb.qnext = qtemp;
//		                     qb = qb.qnext;//place Q to QB
//						}
//	                    else{// find for either mathch for q1, q2, q3, q4 on partB, change it
//
//	                        pb = partB.tnext;
//	                        while(pb.getName().contentEquals(quartetList.getQ1()) != true && 
//	                        		pb.getName().contentEquals(quartetList.getQ2()) != true && 
//	                        				pb.getName().contentEquals(quartetList.getQ3()) != true && 
//	                        				pb.getName().contentEquals(quartetList.getQ4()) != true)
//	                        {
//	                            pb = pb.tnext;
//	                        }
//                    	 if (pb.getName().contentEquals(quartetList.getQ1())) qtemp.setQ1(extra);
//                    	 else if (pb.getName().contentEquals(quartetList.getQ2())) qtemp.setQ2(extra);
//                    	 else if (pb.getName().contentEquals(quartetList.getQ3())) qtemp.setQ3(extra);
//                    	 else qtemp.setQ4(extra);
//                    	 
//                    	 qa.qnext = qtemp;
//	                     qa = qa.qnext;
//	                        //place Q to QA
//	                    }
//
//	                }
//	                //if(debug)
//	                //	cout<< "........endIF.......\n";
//
//	            }
//	            //if(debug)
//	            //	cout<< "........endwhile.......\n";
//
//	        }
//	        System.out.println("****************SQP*************");
//	        printTaxa(partA);
//	        printTaxa(partB);
//	        s1 = SQP(quartetA, partA, extraTaxa, partSatCount);
//	        s2 = SQP(quartetB, partB, extraTaxa, partSatCount);
//	        s = merge(s1,s2,extra);
//	        System.out.println("Merged Tree = "+s);
//	        //	cout<< "Merged tree\n = "<<s<<endl; //taxa list er end er ta newly added

	    }

	
		
		//return s;
		return null;
	}

	private static MultiReturnType FM(LinkedHashSet<Taxa> taxaList, LinkedHashSet<Quartet> quartetList) {
		LinkedHashSet<Taxa> partA = new LinkedHashSet<Taxa>();
		LinkedHashSet<Taxa> partB = new LinkedHashSet<Taxa>();
		int ca = 0, cb = 0;
		for (Quartet quartet : quartetList) {
			int tcount = 0;
			if (taxaList.isEmpty()) {
				System.out.println("Finished");
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
				if (partA.contains(quartet.getT1())) a = 0;
				if (partA.contains(quartet.getT2())) b = 0;
				if (partA.contains(quartet.getT3())) c = 0;
				if (partA.contains(quartet.getT4())) d = 0;
				
				if (partB.contains(quartet.getT1())) a = 1;
				if (partB.contains(quartet.getT2())) b = 1;
				if (partB.contains(quartet.getT3())) c = 1;
				if (partB.contains(quartet.getT4())) d = 1;
		
				
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
		for (Taxa taxa : taxaList) {
			if(ca<cb)
                c=2;
            else if(cb<ca)
                c=1;
            else c++;
            if(c%2==0){
            	taxa.setPartition(0);
            	partA.add(taxa);
            	taxaList.remove(taxa);
            	ca++;
            }else {
            	taxa.setPartition(1);
            	partB.add(taxa);
            	taxaList.remove(taxa);
            	cb++;
            }

		}
        
		//printQuartet(quartetList);
		printTaxa(partA);
		printTaxa(partB);
		return FM_algo(partA, partB, quartetList);
	}

	private static MultiReturnType FM_algo(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB,
			LinkedHashSet<Quartet> quartetList) {
		String taxaToMove = null;
		boolean loopAgain = true;
		while (loopAgain) {
			boolean iterationMore = true;
			LinkedHashSet<GainList> movedList = new LinkedHashSet<GainList>();
			while (iterationMore) {
				int[] score= calculateScore(partB,quartetList,"null",0,0,0);
				//score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
				int prevScore = score[0];//partition score
	            int prevS = score[1];//noOfSat
	            int prevV = score[2];//noOfVat
	            int prevD = score[3];//noOfDef
	            int ca = partA.size();
				int cb = partB.size();
				boolean flag = true; int tag1 = 0, tag2 = 0, alt = 0;
//				gainList = new Listt();
				LinkedHashSet<GainList> gainList = new LinkedHashSet<GainList>();
				Iterator iteratorA = partA.iterator();
				Iterator iteratorB = partB.iterator();
				while (flag) {
					if (iteratorA.hasNext() && alt == 0) {
						
						Taxa taxaA = (Taxa) iteratorA.next();
						if (!taxaA.isLocked()) {
							taxaToMove = taxaA.getName();
	                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
							gainList.add(new GainList(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], ca-1, 0));
							
						}
						if (tag2 == 0) {
							alt = 1;
						}
						
						
					} else if (iteratorB.hasNext() && alt == 1) {
						Taxa taxaB = (Taxa) iteratorB.next();
						if (!taxaB.isLocked()) {
							taxaToMove = taxaB.getName();
	                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
							gainList.add(new GainList(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], cb-1, 1));
							
						}
						if (tag1 == 0) {
							alt = 0;
						}
						
						
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
				}
				System.out.println("********************Gain List*************");
				for (GainList gl : gainList) {
					 System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
		                		gl.getDef()+" "+gl.getBel0w());
				}
				
//				quartetList.stream()
//				.sorted(Comparator.comparing(Quartet::getQFrequency).reversed()).collect(Collectors.toList())
				///////Moving Taxa which have highest gain
				GainList movedTaxa = new GainList();
				movedTaxa = gainList.stream().max(Comparator.comparing(GainList::getVal).thenComparing(GainList::getSat)).get();
				taxaToMove = movedTaxa.getTaxaToMove();
				final String taxaMove = taxaToMove;
				if (movedTaxa.getPart() == 1) {
					partB.removeIf(i -> i.getName().contentEquals(taxaMove));
					partA.add(new Taxa(taxaToMove, 0, true));
				} else {
					partA.removeIf(i -> i.getName().contentEquals(taxaMove));
					partB.add(new Taxa(taxaToMove, 1, true));

				}
				movedTaxa.setPart(1-movedTaxa.getPart());
				movedList.add(movedTaxa);
				
				if (gainList.size() == 1) {
					iterationMore = false;
				}
				
				
			}///no more iteration
			System.out.println("********************Moved List*************");
			for (GainList gl : movedList) {
				 System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
	                		gl.getDef()+" "+gl.getBel0w());
			}
			int cumulativeGain = 0, gainMax = 0;
			String back = "Initial";
			for (GainList ml : movedList) {
				cumulativeGain += ml.getVal();
				if (cumulativeGain >= gainMax) {
					gainMax = cumulativeGain;
					back = ml.getTaxaToMove();
				}
			}
			System.out.println("cumulative gain = "+ cumulativeGain);
            System.out.println(" taxa to move = "+ taxaToMove);
            System.out.println(" back taxa = "+ back);
            System.out.println("partA and partB");
            printTaxa(partA);
            printTaxa(partB);
            boolean isMove = false;
            for (GainList ml : movedList) {
            	String moveTaxa = ml.getTaxaToMove();
            	if (isMove) {
            		if (ml.getPart() == 1) {
    					partB.removeIf(i -> i.getName().contentEquals(moveTaxa));
    					partA.add(new Taxa(moveTaxa, 0));
    				} else {
    					partA.removeIf(i -> i.getName().contentEquals(moveTaxa));
    					partB.add(new Taxa(moveTaxa, 1));

    				}
				}
				if (moveTaxa.contentEquals(back)) {
					isMove = true;
				}
			}
            System.out.println("After moving partA and partB");
            printTaxa(partA);
            printTaxa(partB);
            for (Taxa taxa : partA) {
				taxa.setLocked(false);
			}
            for (Taxa taxa : partB) {
				taxa.setLocked(false);
			}
            if (gainMax <= 0) {
            	System.out.println("Looop again is finished");
            	loopAgain = false;
			}
			
		}//no more loop
		 //************end of Loop Again**********//
        //***********Merge Two list***************//
        int partSat = countSatisfiedQuartets(partA, partB,quartetList);
        System.out.println("PartSat = "+partSat);
        System.out.println("After loop again part a nd b");
        printTaxa(partA);
        printTaxa(partB);
        LinkedHashSet<Taxa> finalTaxaList = new LinkedHashSet<Taxa>(partA);
        finalTaxaList.addAll(partB);
        printTaxa(finalTaxaList);

		return null;
	}

	private static int countSatisfiedQuartets(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB,
			LinkedHashSet<Quartet> quartetList) {
		int csat = 0;
	    int quartetScore;
	    for (Quartet quartet : quartetList) {
			quartetScore = checkQuartet(partB, quartet, "null");
			if(quartetScore == 6)
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
	            //System.out.println("c = "+ c);

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
		String qstat;
		//following code should work. will investigate later
//		if(q.getT1().getPartition() == 1) a = 1;
//		if(q.getT2().getPartition() == 1) a = 1;
//		if(q.getT3().getPartition() == 1) a = 1;
//		if(q.getT4().getPartition() == 1) a = 1;
		// i think parB na dileo hobe
		if(partB.contains(q.getT1())) a = 1;
		if(partB.contains(q.getT2())) b = 1;
		if(partB.contains(q.getT3())) c = 1;
		if(partB.contains(q.getT4())) d = 1;
		////////will change following block of code later
		if(tempTaxa.contentEquals(q.getT1().getName())) a = 1 - a;
	    else if(tempTaxa.contentEquals(q.getT2().getName())) b = 1-b;
	    else if(tempTaxa.contentEquals(q.getT3().getName())) c = 1-c;
	    else if(tempTaxa.contentEquals(q.getT4().getName())) d = 1-d;
		
		if (a==b && c==d && b==c) // totally on one side
	    {	
	        score = 1;
	        qstat = "b";
	    }
	    else if( a==b && c==d) //satisfied
	    {
	        score = 6; //6
	        qstat = "s";
	    }
	    else if ((a==c && b==d) || (a==d && b==c)) // violated
	    {
	        score = 2;
	        qstat = "v";

	    }
	    else //deffered
	    {
	        score = 3;
	        qstat = "d";
	    }

	    q.setStatus(q.getStatus()+qstat);
		return score;

	}

	private static void printQuartet(LinkedHashSet<Quartet> quartetList) {
		System.out.println("**********************Quartet List********************************");
		for(Quartet quartets : quartetList) {
			
			System.out.println(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
					+","+quartets.getT4().getName()+":"+quartets.getQFrequency()+"->"+quartets.isIncreaseFrequency()+"\n");
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
	    
	    System.out.println("Depth one tree = "+ s);
		return s;
	}
	

}
