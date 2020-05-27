package qfm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;


public class Routines {
	//I will try to eliminate these static variables later
		//public static int partSat = 0;
		//public static int partSatCount = 0;
		//public static int extraTaxa = 1000; // it should be #of taxa + 1 initially
		//public static int rtt = 1;
		public static MultyReturnType readQuartet(String fileName) {
			Quartet quartet = new Quartet();
			Quartet listOfQuartet = quartet;
			Taxa taxa = new Taxa();
			Taxa taxa1 = new Taxa(); 
			Taxa listOfTaxa = taxa;
			
			try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))){
				int count=0;
				while (scanner.hasNext()) {
					count++;
					String singleQuartet = scanner.next();				
					//String[] qq = singleQuartet.split(",|\\|");/// for quartet format q1,q2|q3,q4
					String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight
					quartet.qnext = new Quartet(qq[0], qq[1], qq[2], qq[3], count);
					quartet = quartet.qnext;
					//now taxa assigning code
					int sk[] = new int[] {0,0,0,0};
					taxa1 = listOfTaxa;
					while(taxa1!=null) {
						for (int i = 0; i < qq.length; i++) {
							if(qq[i].contentEquals(taxa1.getName()) && sk[i]==0) {
								sk[i]=1;
								break;
							}
						}
						taxa1 = taxa1.tnext;
					}
					for (int i = 0; i < 4; i++) {
						if(sk[i]==0) {
							taxa.tnext = new Taxa(qq[i]);
							taxa = taxa.tnext;
						}
					}
					
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println("*********************Before Counting***********************");
//			printQuartet(listOfQuartet);
//			listOfQuartet = countQuaret(listOfQuartet);
//			System.out.println("*********************After Counting***********************");
//			printQuartet(listOfQuartet);
//			System.out.println("*********************After Sorting***********************");
//			listOfQuartet = sortQuaret(listOfQuartet);
//			printQuartet(listOfQuartet);
			return new MultyReturnType(sortQuaret(countQuaret(listOfQuartet)), listOfTaxa);
			
		}
		public static MultyReturnType readQuartetC(String fileName) { // count will be done at the time of reading
			Quartet quartet = new Quartet();
			Quartet listOfQuartet = quartet;
			Quartet qtemp;
			Taxa taxa = new Taxa();
			Taxa taxa1 = new Taxa(); 
			Taxa listOfTaxa = taxa;
			
			try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))){
				int qcount=0, tcount = 0;
				while (scanner.hasNext()) {
					
					String singleQuartet = scanner.next();				
					//String[] qq = singleQuartet.split(",|\\|");/// for quartet format q1,q2|q3,q4
					String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight
					qtemp = listOfQuartet;
					int avail = 0;
					while(qtemp.qnext!= null)
		            {	
						avail = 0;
						qtemp = qtemp.qnext;
		               
		                if(qq[0].contentEquals(qtemp.getQ1()) && qq[1].contentEquals(qtemp.getQ2())){
		                   
		                    if(qq[2].contentEquals(qtemp.getQ3())&& qq[3].contentEquals(qtemp.getQ4())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[2].contentEquals(qtemp.getQ4())&& qq[3].contentEquals(qtemp.getQ3())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[0].contentEquals(qtemp.getQ2())&& qq[1].contentEquals(qtemp.getQ1())){
		                   
		                    if(qq[2].contentEquals(qtemp.getQ3())&& qq[3].contentEquals(qtemp.getQ4())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[2].contentEquals(qtemp.getQ4())&& qq[3].contentEquals(qtemp.getQ3())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[0].contentEquals(qtemp.getQ3())&& qq[1].contentEquals(qtemp.getQ4())){
		                    
		                    if(qq[2].contentEquals(qtemp.getQ1())&& qq[3].contentEquals(qtemp.getQ2())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[2].contentEquals(qtemp.getQ2())&& qq[3].contentEquals(qtemp.getQ1())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[0].contentEquals(qtemp.getQ4())&& qq[1].contentEquals(qtemp.getQ3())){
		                   
		                    if(qq[2].contentEquals(qtemp.getQ1())&& qq[3].contentEquals(qtemp.getQ2())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[2].contentEquals(qtemp.getQ2())&& qq[3].contentEquals(qtemp.getQ1())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }



		             
		            }
					if (avail == 0) {
						qcount++;
						quartet.qnext = new Quartet(qq[0], qq[1], qq[2], qq[3], qcount);
						quartet = quartet.qnext;
						//now taxa assigning code
						int sk[] = new int[] {0,0,0,0};
						taxa1 = listOfTaxa;
						while(taxa1!=null) {
							for (int i = 0; i < 4; i++) {
								if(qq[i].contentEquals(taxa1.getName()) && sk[i]==0) {
									sk[i]=1;
									break;
								}
							}
							taxa1 = taxa1.tnext;
						}
						for (int i = 0; i < 4; i++) {
							if(sk[i]==0) {
								taxa.tnext = new Taxa(qq[i]);
								taxa = taxa.tnext;
								tcount++;
							}
						}
					}
					
					
					
					
				}
				System.out.println("Number of unique Quartet = "+ qcount);
				System.out.println("Number of taxa = "+ tcount);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new MultyReturnType(sortQuaret((listOfQuartet)), listOfTaxa);
			
		}
		private static Quartet sortQuaret(Quartet quartetList) {
			boolean tag  = true;
			Quartet q, qtemp;
			String tq1, tq2, tq3, tq4;// tstat;
			int tqid, tqFrequency;// tmod;
	        while(tag)
	        {
	            q  = quartetList;
	            qtemp = q.qnext;
	            tag  = false;
	            while(qtemp.qnext!= null)
	            {
	                q  = q.qnext;
	                qtemp  = qtemp.qnext;

	                if(qtemp.getQFrequency() > q.getQFrequency())
	                {
	                    tqid= qtemp.getQuartet_id();
	                    tq1 =qtemp.getQ1();
	                    tq2 =qtemp.getQ2();
	                    tq3 =qtemp.getQ3();
	                    tq4 =qtemp.getQ4();
	                   // tstat=qtemp.getStatus();
	                    tqFrequency = qtemp.getQFrequency();
	                    //tmod = qtemp.getmodified;

	                    qtemp.setQuartet_id(q.getQuartet_id());
	                    qtemp.setQ1(q.getQ1());
	                    qtemp.setQ2(q.getQ2());
	                    qtemp.setQ3(q.getQ3());
	                    qtemp.setQ4(q.getQ4());
	                   // qtemp.status = q.status;
	                    qtemp.setQFrequency(q.getQFrequency());
	                    //qtemp.modified = q.modified;

	                    q.setQuartet_id(tqid);
	                    q.setQ1(tq1);
	                    q.setQ2(tq2);
	                    q.setQ3(tq3);
	                    q.setQ4(tq4);
	                    //q.status = tstat;
	                    q.setQFrequency(tqFrequency);
	                    //q.modified= tmod;
	                    tag = true;

	                }
	            }
	        }
			
			return quartetList;
		}
		public static void printQuartet(Quartet listOfQuartet) {
			while(listOfQuartet.qnext!=null) {
				listOfQuartet = listOfQuartet.qnext;
				System.out.println(listOfQuartet.getQuartet_id()+" : "+listOfQuartet.getQ1()+","+listOfQuartet.getQ2()+"|"
								+listOfQuartet.getQ3()+","+listOfQuartet.getQ4()+"->"+listOfQuartet.getQFrequency());
				
			}
			
		}
		public static Quartet countQuaret(Quartet quartetList) {
			Quartet q = quartetList;
			Quartet qtemp; int n;
			while(q.qnext!= null)
			{
	            
	            q  = q.qnext;
	            qtemp = q;
	            
	            while(qtemp.qnext!= null)
	            {	
	            	n = 0;
	                qtemp = qtemp.qnext;
	                //if(q.getQuartet_id == qtemp.getQuartet_id) continue;
	                if(q.getQ1().contentEquals(qtemp.getQ1()) && q.getQ2().contentEquals(qtemp.getQ2())){
	                 
	                    if(q.getQ3().contentEquals(qtemp.getQ3())&& q.getQ4().contentEquals(qtemp.getQ4()))n++;
	                    else if(q.getQ3().contentEquals(qtemp.getQ4())&& q.getQ4().contentEquals(qtemp.getQ3()))n++;

	                }
	                else if(q.getQ1().contentEquals(qtemp.getQ2())&& q.getQ2().contentEquals(qtemp.getQ1())){
	                    
	                    if(q.getQ3().contentEquals(qtemp.getQ3())&& q.getQ4().contentEquals(qtemp.getQ4()))n++;
	                    else if(q.getQ3().contentEquals(qtemp.getQ4())&& q.getQ4().contentEquals(qtemp.getQ3()))n++;

	                }
	                else if(q.getQ1().contentEquals(qtemp.getQ3())&& q.getQ2().contentEquals(qtemp.getQ4())){
	                   
	                    if(q.getQ3().contentEquals(qtemp.getQ1())&& q.getQ4().contentEquals(qtemp.getQ2()))n++;
	                    else if(q.getQ3().contentEquals(qtemp.getQ2())&& q.getQ4().contentEquals(qtemp.getQ1()))n++;

	                }
	                else if(q.getQ1().contentEquals(qtemp.getQ4())&& q.getQ2().contentEquals(qtemp.getQ3())){
	                   
	                    if(q.getQ3().contentEquals(qtemp.getQ1())&& q.getQ4().contentEquals(qtemp.getQ2()))n++;
	                    else if(q.getQ3().contentEquals(qtemp.getQ2())&& q.getQ4().contentEquals(qtemp.getQ1()))n++;

	                }



	                if(n>0)
	                {
	                    qtemp.increase_count();
	                 
	                    q.increase_count();
	                }


	            }


	        }
			return quartetList;
		}
		public static void printTaxa(Taxa listOfTaxa) {
			while(listOfTaxa.tnext!=null) {
				listOfTaxa = listOfTaxa.tnext;
				System.out.print(listOfTaxa.getName()+"->");
				
			}
			System.out.print("null");
			System.out.println();
		}
		//I will write this function later
		public static MultyReturnType readQuartetNewick(String fileName) {//incomplete
			Quartet quartet = new Quartet();
			Quartet listOfQuartet = quartet;
			Quartet qtemp;
			Taxa taxa = new Taxa();
			Taxa taxa1 = new Taxa(); 
			Taxa listOfTaxa = taxa;
			
			
			//Scanner scanner = null;
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
				//scanner = new Scanner(new BufferedReader(new FileReader("sample.txt")));
				int tcount=0, qcount = 0;
				String loc;
				while ((loc = br.readLine())!=null) {
					if(loc.equals("")) {
						//remove extra new line/white space
						continue;
					}
			
					String[] qq = loc.split("\\(\\(|,|\\),\\(|\\)\\)");
					qtemp = listOfQuartet;
					int avail = 0;
					while(qtemp.qnext!= null)
		            {	
						avail = 0;
						qtemp = qtemp.qnext;
		               
		                if(qq[1].contentEquals(qtemp.getQ1()) && qq[2].contentEquals(qtemp.getQ2())){
		                   
		                    if(qq[3].contentEquals(qtemp.getQ3())&& qq[4].contentEquals(qtemp.getQ4())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[3].contentEquals(qtemp.getQ4())&& qq[4].contentEquals(qtemp.getQ3())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[1].contentEquals(qtemp.getQ2())&& qq[2].contentEquals(qtemp.getQ1())){
		                   
		                    if(qq[3].contentEquals(qtemp.getQ3())&& qq[4].contentEquals(qtemp.getQ4())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[3].contentEquals(qtemp.getQ4())&& qq[4].contentEquals(qtemp.getQ3())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[1].contentEquals(qtemp.getQ3())&& qq[2].contentEquals(qtemp.getQ4())){
		                    
		                    if(qq[3].contentEquals(qtemp.getQ1())&& qq[4].contentEquals(qtemp.getQ2())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[3].contentEquals(qtemp.getQ2())&& qq[4].contentEquals(qtemp.getQ1())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }
		                else if(qq[1].contentEquals(qtemp.getQ4())&& qq[2].contentEquals(qtemp.getQ3())){
		                   
		                    if(qq[3].contentEquals(qtemp.getQ1())&& qq[4].contentEquals(qtemp.getQ2())){
		                    	avail++; qtemp.increase_count(); break;
		                    }
		                    else if(qq[3].contentEquals(qtemp.getQ2())&& qq[4].contentEquals(qtemp.getQ1())){
		                    	avail++; qtemp.increase_count(); break;
		                    }

		                }



		             
		            }
					if (avail == 0) {
						qcount++;
						quartet.qnext = new Quartet(qq[1], qq[2], qq[3], qq[4], qcount);
						quartet = quartet.qnext;
						//now taxa assigning code
						int sk[] = new int[] {0,0,0,0,0};
						taxa1 = listOfTaxa;
						while(taxa1!=null) {
							for (int i = 1; i <= 4; i++) {
								if(qq[i].contentEquals(taxa1.getName()) && sk[i]==0) {
									sk[i]=1;
									break;
								}
							}
							taxa1 = taxa1.tnext;
						}
						for (int i = 1; i <= 4; i++) {
							if(sk[i]==0) {
								taxa.tnext = new Taxa(qq[i]);
								taxa = taxa.tnext;
								tcount++;
							}
						}
					}
					
					
					
					
				}
				System.out.println("Number of unique Quartet = "+ qcount);
				System.out.println("Number of taxa = "+ tcount);
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new MultyReturnType(sortQuaret((listOfQuartet)), listOfTaxa);
			
		}
		public static String SQP(Quartet quartetList, Taxa taxaList, int extraTaxa, int partSatCount) {
			int taxacount = taxaCount(taxaList);
			String s, s1, s2, extra = "extra";
			Taxa pa, pb, partA, partB;
			Quartet qa, qb, quartetA, quartetB, qtemp;
			
			if (taxacount == 0) {
				s = ("");
				return s;
			}
			if (quartetList.qnext == null || taxacount <3) {
				s = depthOneTree(taxaList);
			} else {
				MultyReturnType mrl = FM(taxaList, quartetList);
				//Taxa p = FM(taxaList, quartetList);
				Taxa p = mrl.getTaxa();
				//printTaxa(p);
				//System.out.println("partSat = "+ partSat);
				int partSat = mrl.getAnyCount(); // It returns # of satisfied quartets
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
		        System.out.println("extra = "+ extra);
		        extraTaxa++;

		        partA = new Taxa();
		        pa = partA;
		        partB = new Taxa();
		        pb = partB;
		        
		        while(p.tnext!= null)
		        {
		            p = p.tnext;
		            if(p.getPartition() == 0){
		                pa.tnext = new Taxa(p.getName(), 0);
		                pa= pa.tnext;
		            }

		            else{
		                
		                pb.tnext = new Taxa(p.getName(), 1);
		                pb= pb.tnext;
		            }

		        }
//		        System.out.println("***************partA*****************");
//		        printTaxa(partA);
//		        System.out.println("***************partB*****************");
//		        printTaxa(partB);
		        pa.tnext = new Taxa(extra, 0); ///add extra taxa to partition A
		        pa= pa.tnext;
		        pb.tnext = new Taxa(extra, 1);// add extra taxa to parttion B
		        pb= pb.tnext;

//		        printTaxa(partA);
//		        printTaxa(partB);
		        quartetA = new Quartet();
		        qa = quartetA;
		        quartetB = new Quartet();
		        qb = quartetB;
		        String t1,t2,t3,t4;
		        char c;
		        int l,dcount=0;
		        while(quartetList.qnext !=  null)
		        {
		            quartetList = quartetList.qnext;
		            l = quartetList.getStatus().length();
		            c = quartetList.getStatus().charAt(l-1);
//		            if(debug)
//		                cout<< "Status of Quartet "<<Q->quartet_id<<"= "<<c<<endl;
		            if(c == 'b' || c == 'd' )
		            {
		                //Q = Q->qnext;
		                qtemp =  new Quartet(quartetList.getQ1(), quartetList.getQ2(), quartetList.getQ3(),
		                		quartetList.getQ4(), quartetList.getQuartet_id());
		                
		                qtemp.setStatus(quartetList.getStatus());
		                qtemp.setQFrequency(quartetList.getQFrequency());
//		                if(debug)
//		                    cout << "....q1 ="<<Q->q1<<"....q2 ="<<Q->q2<<"....q3 ="<<Q->q3<<"....q4 ="<<Q->q4<<".......\n";

		                if(c=='b')
		                {
		                    pa = partA.tnext;
		                    while(pa!= null && (pa.getName().contentEquals(quartetList.getQ1())) != true ) // logic error
		                    {
		                        pa = pa.tnext;
		                        //if(debug)
		                        //	cout<<"both side\n";
		                    }

		                    if(pa== null)
		                    {	//if(debug)
		                        //	cout<< "........else.......\n";
		                        qb.qnext = qtemp;
		                        qb = qb.qnext;
		                        //place Q to QB
		                    }
		                    else //((pa->name.compare(Q->q1))==0)// q1 on part A
		                    {
		                        //if(debug)
		                        //	cout<< "........if.......\n";
		                        qa.qnext = qtemp;
		                        qa = qa.qnext;
		                        //place Q to QA
		                    }
		                }
		                else // deferred
		                {	qtemp.setModified(1);
		                    dcount =0 ;
		                    pa = partA.tnext;
		                    while(pa!= null)
		                    {
		                        if(pa.getName().contentEquals(quartetList.getQ1()) || pa.getName().contentEquals(quartetList.getQ2())||
		                        		pa.getName().contentEquals(quartetList.getQ3())|| pa.getName().contentEquals(quartetList.getQ4()))
		                            dcount++;
		                        pa = pa.tnext;
		                        if(dcount==3)
		                        {
		                            //cout<<"dcount  =  "<<dcount<<endl;
		                            break;
		                        }
		                    }
		                    if (dcount == 1) { // find for either mathch for q1, q2, q3, q4 on partB, change it
		                    	pa = partA.tnext;
		                    	 while(pa.getName().contentEquals(quartetList.getQ1()) != true && 
			                        		pa.getName().contentEquals(quartetList.getQ2()) != true && 
			                        				pa.getName().contentEquals(quartetList.getQ3()) != true && 
			                        				pa.getName().contentEquals(quartetList.getQ4()) != true)
			                        {
			                            pa = pa.tnext;
			                        }
		                    	 if (pa.getName().contentEquals(quartetList.getQ1())) qtemp.setQ1(extra);
		                    	 else if (pa.getName().contentEquals(quartetList.getQ2())) qtemp.setQ2(extra);
		                    	 else if (pa.getName().contentEquals(quartetList.getQ3())) qtemp.setQ3(extra);
		                    	 else qtemp.setQ4(extra);
		                    	 
		                    	 qb.qnext = qtemp;
			                     qb = qb.qnext;//place Q to QB
							}
		                    else{// find for either mathch for q1, q2, q3, q4 on partB, change it

		                        pb = partB.tnext;
		                        while(pb.getName().contentEquals(quartetList.getQ1()) != true && 
		                        		pb.getName().contentEquals(quartetList.getQ2()) != true && 
		                        				pb.getName().contentEquals(quartetList.getQ3()) != true && 
		                        				pb.getName().contentEquals(quartetList.getQ4()) != true)
		                        {
		                            pb = pb.tnext;
		                        }
	                    	 if (pb.getName().contentEquals(quartetList.getQ1())) qtemp.setQ1(extra);
	                    	 else if (pb.getName().contentEquals(quartetList.getQ2())) qtemp.setQ2(extra);
	                    	 else if (pb.getName().contentEquals(quartetList.getQ3())) qtemp.setQ3(extra);
	                    	 else qtemp.setQ4(extra);
	                    	 
	                    	 qa.qnext = qtemp;
		                     qa = qa.qnext;
		                        //place Q to QA
		                    }

		                }
		                //if(debug)
		                //	cout<< "........endIF.......\n";

		            }
		            //if(debug)
		            //	cout<< "........endwhile.......\n";

		        }
		        System.out.println("****************SQP*************");
		        printTaxa(partA);
		        printTaxa(partB);
		        s1 = SQP(quartetA, partA, extraTaxa, partSatCount);
		        s2 = SQP(quartetB, partB, extraTaxa, partSatCount);
		        s = merge(s1,s2,extra);
		        System.out.println("Merged Tree = "+s);
		        //	cout<< "Merged tree\n = "<<s<<endl; //taxa list er end er ta newly added

		    }

		
			
			return s;
		}
		private static MultyReturnType FM(Taxa taxaList, Quartet quartetList) {
			// code for counting and sorting will be given in readQuartet() method
			/********************Initial Partition************** */
			Quartet quartetList1 = quartetList;
			Taxa prev, p, patemp, pbtemp, pa, pb;
			Taxa partA = new Taxa();
			Taxa partB = new Taxa();
			pa = partA;
			pb = partB;
			int a, b, c, d, ca = 0, cb = 0; String q1, q2, q3, q4;
			
			while(quartetList1.qnext != null) {
				a = -1; b = -1; c = -1; d = -1;
				quartetList1 = quartetList1.qnext;
				q1 = quartetList1.getQ1();
				q2 = quartetList1.getQ2();
				q3 = quartetList1.getQ3();
				q4 = quartetList1.getQ4();
				int pCount = 0;

				p = taxaList;

				while (p.tnext != null) {
					prev = p;
					p = p.tnext;
					if(q1.contentEquals(p.getName()) || q2.contentEquals(p.getName()) || 
						q3.contentEquals(p.getName()) || q4.contentEquals(p.getName())) {
						prev.tnext = p.tnext;
						p = prev;
						pCount ++;
					}
					if (pCount == 4) {
						break;
					}
					
				}

				if (pCount > 0) {
					
					patemp = partA;
				    pbtemp = partB;
				    while(patemp.tnext != null)
				    {
				        patemp = patemp.tnext;
				    
				        if(q1.contentEquals(patemp.getName())) a = 0;
				        else if (q2.contentEquals(patemp.getName())) b = 0;
				        else if (q3.contentEquals(patemp.getName())) c = 0;
				        else if (q4.contentEquals(patemp.getName())) d = 0;
				        
				    }
				    while(pbtemp.tnext != null)
				    {
				        pbtemp = pbtemp.tnext;
				    
				        if(q1.contentEquals(pbtemp.getName())) a = 1;
				        else if (q2.contentEquals(pbtemp.getName())) b = 1;
				        else if (q3.contentEquals(pbtemp.getName())) c = 1;
				        else if (q4.contentEquals(pbtemp.getName())) d = 1;
				        
				    }

				    if(a==-1 && b==-1 && c==-1 && d==-1) //all new
				    {
				        pa.tnext = new Taxa(q1, 0) ;
				        pa = pa.tnext;
				        a= 0; ca++;
				        pa.tnext = new Taxa(q2, 0) ;
				        pa = pa.tnext;
				        b= 0; ca++;
				        pb.tnext = new Taxa(q3, 1) ;
				        pb = pb.tnext;
				        c= 0; cb++;
				        pb.tnext = new Taxa(q4, 1) ;
				        pb = pb.tnext;
				        d= 0; cb++;
				     
				    }else {
				    	if(a==-1)
				        {	
				            if(b!=-1){
				                if(b==0) {pa.tnext = new Taxa(q1, 0) ; pa = pa.tnext; a=0;ca++;}
				                else {pb.tnext = new Taxa(q1, 1) ; pb = pb.tnext; a=1;cb++;}
				            }
				            else if(c!=-1){
				                if(c==1) {pa.tnext = new Taxa(q1, 0) ; pa = pa.tnext; a=0;ca++;}
				                else {pb.tnext = new Taxa(q1, 1) ; pb= pb.tnext; a=1;cb++;}
				            }
				            else if(d!=-1)
				            {
				                if(d==1) {pa.tnext = new Taxa(q1, 0) ; pa = pa.tnext; a=0;ca++;}
				                else {pb.tnext = new Taxa(q1, 1) ; pb = pb.tnext; a=1;cb++;}
				            }
				
				        }
				        if(b==-1)
				        {
				
				            if(a==0) {pa.tnext = new Taxa(q2, 0) ; pa = pa.tnext; b=0;ca++;}
				            else {pb.tnext = new Taxa(q2, 1) ; pb = pb.tnext; b=1;cb++;}
				
				        }
				        if(c==-1)
				        {
				            if(d!=-1)
				            {
				                if(d==0) {pa.tnext = new Taxa(q3, 0) ; pa = pa.tnext; c=0;ca++;}
				                else {pb.tnext = new Taxa(q3, 1) ; pb= pb.tnext; c=1;cb++;}
				            }
				            else{
				                if(a==1) {pa.tnext = new Taxa(q3, 0) ; pa = pa.tnext; c=0;ca++;}
				                else {pb.tnext = new Taxa(q3, 1) ; pb= pb.tnext; c=1;cb++;}
				            }
				        }
				        if(d==-1)
				        {
				            if(c==0) {pa.tnext = new Taxa(q4, 0) ; pa = pa.tnext; d=0;ca++;}
				            else {pb.tnext = new Taxa(q4, 1) ; pb= pb.tnext; d=1;cb++;}
				        }
				
					}
				    
				    if(taxaList.tnext == null) {
				    	break;
				    }
				    
				}
			}
	        c = 0;
	        p = taxaList;
	        while(p.tnext!= null)
	        {
	           
	            p = p.tnext;
	            if(ca<cb)
	                c=2;
	            else if(cb<ca)
	                c=1;
	            else c++;
	            if(c%2==0){pa.tnext = new Taxa(p.getName(), 0); pa= pa.tnext;ca++;}
	            else {pb.tnext = new Taxa(p.getName(), 1); pb= pb.tnext;cb++;}

	        }
			System.out.println("*************inside fm**********");
			printTaxa(partA);
			printTaxa(partB);
			//taxaList = FM_algo(partA, partB, quartetList);
			return FM_algo(partA, partB, quartetList);
		}
		private static MultyReturnType FM_algo(Taxa partA, Taxa partB, Quartet quartetList) {
			boolean loopAgain = true;
			Taxa pa, pb, temp, temp2, finalTaxalist, part;
			String taxaToMove = null;
			int prevScore, prevS, prevD, prevV;
			Listt gl, gainList, g, m, movedList;//, dl;
			int cumulativeGain, gainmax;
		
			//Random rand = new Random(); 
			
			
			while (loopAgain) {
				cumulativeGain = 0; gainmax  = 0; 

		        movedList = new Listt();
		        m = movedList;
				boolean iterationMore = true; int iteration = 0;
				while (iterationMore) {
					iteration++;
					int[] score= calculateScore(partB,quartetList,"null",0,0,0);
					//score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
					prevScore = score[0];//partition score
		            prevS = score[1];//noOfSat
		            prevV = score[2];//noOfVat
		            prevD = score[3];//noOfDef

					int ca = taxaCount(partA);
					int cb = taxaCount(partB);
					boolean flag = true; int tag1 = 0, tag2 = 0, alt = 0;
					gainList = new Listt();
					while (flag) {
						if(alt == 0 && tag1 ==0 ){
		                    //******** move one taxa from partA to PartB ****************//
		                    pa = partA.tnext;
		                    pb = partB.tnext;



		                    while(pa!= null && pa.getState() == 1)
		                    { //skip
		                        pa = pa.tnext;
		                    }
		                    if(pa == null)
		                    {	
		                    	tag1 = 1;	                       
		                    }// no node to move from A to B}
		                    else{

		                        taxaToMove = pa.getName();
		                        pa.setState(1);
		                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
			                      //score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
		                        gl = gainList;
		                        while(gl.next!= null)
		                            gl = gl.next;
		                        gl.next = new Listt(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], ca-1, 0);
		                    }
		                    if(tag2 == 0)
		                        alt =1;

		                }else if(alt == 1 && tag2 == 0) {
		                
		                    //******** move one taxa from partB to PartA ****************//
		                    pa = partA.tnext;
		                    pb = partB.tnext;

		                    while(pb!= null && pb.getState() == 1)
		                    { //skip
		                        pb = pb.tnext;
		                    }
		                    if(pb == null ){
		                        tag2 = 1;
		                    }// no node to move from B to A}
		                    else{
		                        taxaToMove = pb.getName();
		                        pb.setState(1);
		                        score = calculateScore(partB, quartetList, taxaToMove, prevS, prevV, prevD);
			                      //score[0] -> partition score, score[1] -> noOfSat, score[2] -> noOfVat, score[3] -> noOfDef
		                        gl = gainList;
		                        while(gl.next!= null)
		                            gl = gl.next;
		                        gl.next = new Listt(taxaToMove, score[0]-prevScore, score[1], score[2], score[3], cb-1, 1);
		     
		                    }
		                    if(tag1 == 0){
		                        alt = 0;
		                    }
		                }
						if (tag1 == 1 && tag2 == 1) {
							flag = false;
						}
						
					}
					System.out.println("**********gainlist**********");
					gl= gainList.next;
		            while (gl!= null){
		                System.out.println(gl.getTaxaToMove()+" "+gl.getVal()+" "+gl.getPart()+" "+gl.getSat()+" "+gl.getVat()+" "+
		                		gl.getDef()+" "+gl.getBel0w());
		                gl= gl.next;
		            }
		            System.out.println("**********end of gainlist**********");
		           
		            
		            gl = gainList.next;
		            int maxgain = -1000000000; //double
		            int minvio = 0;
		            int maxsat = 0;
		            int glPart = 0;
		            int randnum = 0;
		            int maxdiff = -1000000000; 
		            double maxratio = 0, tempratio = 0; // d/v
		            double maxratio1 = 0, tempratio1 = 0; // s/d
	 
		            int nextQ = 0, c1=0, c2=0;
		            taxaToMove = null;


		            while(gl != null)
		            {
		                if(gl.getPart()==0) {c1 = ca -1; c2= cb+1;}
		                else{c1 = ca + 1; c2= cb - 1;}


		                if(gl.getVal() > maxgain && gl.getBel0w() >= 2 )//&&((c1>2||c2>2)&& total!=gl->val+gl->sat))
		                {
		                    taxaToMove = gl.getTaxaToMove();
		                    maxgain = gl.getVal();
		                    minvio = gl.getVat();
		                    maxdiff = gl.getSat() - gl.getVat();
		                    if(gl.getVat() != 0)
		                        maxratio = gl.getSat() - gl.getVat();
		                    else maxratio = gl.getSat();
		                
		                    maxsat = gl.getSat();
		                    glPart = gl.getPart(); // current Partition
		                }
		                else if(gl.getVal() == maxgain && gl.getBel0w() >= 2 )//&&((c1>2||c2>2)&& total!=gl->val+gl->sat))
		                { 
		                    if(gl.getVat() != 0)
		                        tempratio = gl.getSat()/gl.getVat();
		                    else tempratio = gl.getSat();
		                   
		                    if(gl.getSat() > maxsat && gl.getBel0w() >= 2)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
		                    {
		                        taxaToMove = gl.getTaxaToMove();
		                        maxgain = gl.getVal();
		                        minvio = gl.getVat();
		                        maxsat = gl.getSat();
		                        maxdiff = gl.getSat() - gl.getVat();
		                        if(gl.getVat() != 0)
		                            maxratio = gl.getSat()/gl.getVat();
		                        else maxratio = gl.getSat();
		                       
		                        glPart = gl.getPart();
		                    }
		                    else if(gl.getSat() == maxsat && gl.getBel0w() >= 2)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
		                    {
		                       randnum = 10 + (new Random().nextInt(100));///rand()%100;
		                        if(randnum%2 == 0){
		                            taxaToMove = gl.getTaxaToMove();
		                            maxgain = gl.getVal();
		                            minvio = gl.getVat();
		                            maxdiff = gl.getSat() - gl.getVat();
		                            if(gl.getVat() != 0)
		                                maxratio = gl.getSat()/gl.getVat();
		                            else maxratio = gl.getSat();
		                           
		                            maxsat = gl.getSat();
		                            glPart = gl.getPart(); // current Partition
		                        }
		                        //}

		                    }

		                }
		                gl = gl.next;

		            } 
		            
		            if(taxaToMove != null){

		                if(glPart == 1)
		                {	
		                	pa = partA.tnext;
		                    pb = partB.tnext;
		                    temp = null;
		                    temp = partB;
		                    while(pb.getName().contentEquals(taxaToMove) == false)
		                    {	
		                    	temp = temp.tnext; // temp is prev taxa
		                        pb = pb.tnext;
		                    }
		                    temp2 = new Taxa(taxaToMove, 0, 1, 1);
		                    temp.tnext = pb.tnext; //pb->tnext;
		                    temp2.tnext = pa;
		                    partA.tnext = temp2;
		                }
		                else{
		                    pa = partA.tnext;
		                    pb = partB.tnext;
		                    temp = null;
		                    temp = partA;
		                    while(pa.getName().contentEquals(taxaToMove) == false)
		                    {
		                        temp = temp.tnext; // temp is prev taxa
		                        pa = pa.tnext;

		                    }
		                    temp2 = new Taxa(taxaToMove, 1, 1, 1);
		                    temp.tnext = pa.tnext; //pb->tnext;
		                    temp2.tnext = pb;
		                    partB.tnext = temp2;
		                }

		                //	cout<<" TaxaToMove  = "<<TaxaToMove<<endl;
		                g = new Listt(taxaToMove, maxgain, 1-glPart);
		                g.prev = m;
		                m.next = g;
		                m = m.next;

		                pa = partA.tnext;
		                iterationMore = false;
		                while(pa != null)
		                {   if(pa.getLocked() == 1) {pa.setState(1);}
		                    else
		                    {	pa.setState(0);
		                        iterationMore = true;
		                    }
		                    pa = pa.tnext;
		                }
		                pb = partB.tnext;
		                while(pb != null)
		                {   if(pb.getLocked() == 1) {pb.setState(1);}
		                    else
		                    {	pb.setState(0);
		                        iterationMore = true;
		                    }

		                    pb = pb.tnext;
		                }
		            
		            }else {

		            	iterationMore = false;
		            }

		       } 
		         // no more iteration
				
				System.out.println("***************Moved List******************");
				m= movedList.next;
	            while (m!= null){
	                System.out.println(m.getTaxaToMove()+" "+m.getVal()+" "+m.getPart()+" "+m.getSat()+" "+m.getVat()+" "+
	                		m.getDef()+" "+m.getBel0w());
	                m= m.next;
	            }
	            System.out.println("***************Moved List******************");
		            //***********Cumulative gain compute*************//
		            gainmax= 0;
		            String back = "Initial";
		            m = movedList.next;
		            while(m != null)
		            {
		                cumulativeGain = cumulativeGain + m.getVal();
		                //System.out.println("By moving taxa "+ m.getTaxaToMove() + " cummulative gain is "+ cumulativeGain);
		                if(cumulativeGain >= gainmax)
		                {
		                    gainmax = cumulativeGain;
		                    back = m.getTaxaToMove();
		                }
		                m = m.next;
		            }
		           
		            System.out.println("cumulative gain = "+ cumulativeGain);
		            System.out.println(" taxa to move = "+ taxaToMove);
		            System.out.println(" back taxa = "+ back);
		            System.out.println("partA and partB");
		            printTaxa(partA);
		            printTaxa(partB);
		   
		            //***********Update Partition*******************//
		            m = movedList.next;
		         
		            int rollback = 0;
		          

		            
		            if(taxaToMove != null){
		                while(m.next != null)
		                    {m = m.next;}   // m points the last element of moved list
		               // System.out.println("whats wrong + "+ m.getTaxaToMove());
		                while(m.prev != null || m.getVal() != -1)
		                { 
		                	//System.out.println("***********inside rollback**********");
		                    rollback++;
		                    //System.out.println("roll back taxa = "+ m.getTaxaToMove());

		                    pa = partA.tnext;
		                    pb = partB.tnext;
		                    //	cout<<m->part;
		                    //System.out.println("pa = "+ pa.getName()+" and pb = "+ pb.getName());
		                    if(back.contentEquals(m.getTaxaToMove()))
		                        break;
		                    if(m.getPart() == 0)
		                    {
		                        temp = partA;
		                        while(pa.getName().contentEquals(m.getTaxaToMove()) == false && pa!=null) //skip
		                        {	temp = temp.tnext; // temp is prev taxa
		                            pa = pa.tnext;
		                        }
		                        temp2 = new Taxa(pa.getName(), 1);
		                        temp.tnext = pa.tnext;
		                        temp2.tnext = pb;
		                        partB.tnext = temp2;
		                        
		                    }
		                    else
		                    {	
		                    	temp = partB;
		                        while(pb.getName().contentEquals(m.getTaxaToMove()) == false && pb!= null) //skip
		                        {	temp = temp.tnext; // temp is prev taxa
		                            pb = pb.tnext;
		                        }
		                        temp2 = new Taxa(pb.getName(), 1);
		                        temp.tnext = pb.tnext;
		                        temp2.tnext = pa;
		                        partA.tnext = temp2;
		                       

		                    }

		                    m = m.prev;
		                    
		                }
		            }
		            pa = partA.tnext;
		            pb = partB.tnext;
		            while(pa != null)
		            {
		                pa.setState(0); pa.setLocked(0); pa.setTaxaScore(0); pa = pa.tnext;
		            }
		            while(pb!= null)
		            {
		                pb.setState(0); pb.setLocked(0); pb.setTaxaScore(0); pb = pb.tnext;
		            }
		            //*******************************************************************//


		            //*******************************************************************//
		            if(gainmax <= 0)// || iteration>1)
		            {		
		            	loopAgain = false; //it will stop outer while loop
		            	//System.out.println("Max cumulative gain = "+ gainmax);
		            	break;
		                
		            }
		           
		            //************end of Update Partition****************//
		           // loopAgain =false;
				
		        }
		        //************end of Loop Again**********//
		        //***********Merge Two list***************//
		        int partSat = countSatisfiedQuartets(partA, partB,quartetList);
		        System.out.println("After loop again part a nd b");
		        printTaxa(partA);
		        printTaxa(partB);

		        finalTaxalist = new Taxa();
		        part = finalTaxalist;

		        while(partA.tnext!= null)
		        {

		            partA = partA.tnext;
		            part.tnext = new Taxa(partA.getName(), 0);
		            part = part.tnext;
		            //pa= pa->tnext;
		        }
		        while(partB.tnext != null)
		        {

		            partB = partB.tnext;
		            part.tnext = new Taxa(partB.getName(), 1);
		            part = part.tnext;
		            //pb= pb->tnext;
		        }
	        System.out.println("Final taxa list");
			printTaxa(finalTaxalist);
			return new MultyReturnType(finalTaxalist, partSat);
			//return null;
			
		}
		private static int countSatisfiedQuartets(Taxa partA, Taxa partB, Quartet quartetList) {
			int csat = 0;
			Quartet q;
		    
		    q = quartetList.qnext;
		    int quartetScore;
		  
		    while(q!= null){

		        //	if(q->modified == 0)

		        quartetScore = checkQuartet(partB, q, "null");
		        if(quartetScore == 6)
		        {
		           // csat++;
		        	csat += q.getQFrequency();


		            //Satisfied_quartets[q->quartet_id]=1;
		        }
		        //	}
		        q = q.qnext;
		    }
		   // Satisfied += csat;
		    return csat;
		
		}
		private static int[] calculateScore(Taxa partB, Quartet quartetList, String tempTaxa, int st, int vt,
				int df) {
			int[] scores = {0,0,0,0};
		    int  qscore;
		    
		    Quartet q = quartetList.qnext;
		    int s = 0, v = 0, d = 0;
		    char c;
		    
		   

		  

		    while(q!= null){
		        if(tempTaxa.contentEquals("null"))
		        {
		            q.setStatus("");
		            qscore  = checkQuartet(partB, q, tempTaxa);
		            if(qscore == 6) s = s + q.getQFrequency();
		            else if(qscore == 2) v = v + q.getQFrequency();
		            else if(qscore == 3) d = d + q.getQFrequency();
		        }
		        else if(q.getQ1().contentEquals(tempTaxa) || q.getQ2().contentEquals(tempTaxa) ||
		        		q.getQ3().contentEquals(tempTaxa) || q.getQ4().contentEquals(tempTaxa))
		        {	qscore  = checkQuartet(partB, q, tempTaxa);
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
		        //partitionScore += Check_Quartet(parta, partb, q, tempTaxa);
		        q = q.qnext;
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
		private static int checkQuartet(Taxa partB, Quartet q, String tempTaxa) {
			//partA is never used. Only partB is used. So later I will skip partA from method signature
			int a = 0, b = 0, c = 0, d = 0, score = 0;
			String s1,qstat;
		    partB = partB.tnext;
		    while(partB != null)
		    {
		        s1 = partB.getName();
		        if(s1.contentEquals(q.getQ1())) a = 1;
		        else if(s1.contentEquals(q.getQ2())) b = 1;
		        else if(s1.contentEquals(q.getQ3())) c = 1;
		        else if(s1.contentEquals(q.getQ4())) d = 1;
		   
		        partB = partB.tnext;

		    }
		    if(tempTaxa.contentEquals(q.getQ1())) a = 1 - a;
		    else if(tempTaxa.contentEquals(q.getQ2())) b = 1-b;
		    else if(tempTaxa.contentEquals(q.getQ3())) c = 1-c;
		    else if(tempTaxa.contentEquals(q.getQ4())) d = 1-d;

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
		private static String depthOneTree(Taxa taxaList) {
			String s = "(";
		    while(taxaList.tnext != null)
		    {
		        taxaList = taxaList.tnext;
		        s += taxaList.getName();
		        //s.append(taxa_list->name);
		        if(taxaList.tnext != null)
		        	s += ",";
		    }
		    s += ")";
		    System.out.println("Depth one tree = "+ s);
			return s;
		}
		private static int taxaCount(Taxa taxa) {
			int taxacount = 0;
		    while (taxa.tnext != null)
		    {
		        taxacount++;
		        taxa = taxa.tnext;
		    }
			return taxacount;
		}

		private static boolean balance(String s) {
			// TODO Auto-generated method stub
			int i, l, ob = 0, com = 0;
			l = s.length();
			System.out.println(" length = "+l);
			
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
		private static String merge(String s1, String s2, String extra) {
			s1 = reroot(s1, extra);
			s2 = reroot(s2, extra);
			return s1+","+s2;
		}
		private static String reroot(String s, String extra) {
			//String s = "extra1000,(9,9),((7,12,(9,8,11)),((1,5,6,(2,3,O)),4))";
//			String fileName;
//			if (rtt == 1) {
//				fileName = "reroot1a.txt";
//				rtt++;
//			}else {
//				fileName = "rerootb.txt";
//			}
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
			System.out.println(cmd);
			try {
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				p.destroy();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Problem in cmd");
			}
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
				s = br.readLine();
				System.out.println("br = "+s);
				//String extra = "extra1000";
				System.out.println(s);
				s = s.replace(":", "");
				s = s.replace(";", "");
				int pos1 = s.indexOf(extra);
				s = s.replace(extra, "");
				System.out.println("After removing extra s= "+s);
				System.out.println("pos1 = "+pos1);

				int start = 0, i= 0, ob = 0, end = 0, cb = 0; String mystring; boolean removebr = false;
				if(s.charAt(pos1-1) =='('&& s.charAt(pos1) ==',')
			     {
					
					try{

						s = s.substring(0, pos1)+s.substring(pos1+1);
						 start = pos1-1;
						System.out.println("inside s= "+ s+ " and start = "+start );

			           
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
					System.out.println("start = "+ start + " and end = "+ end);
			        mystring = s.substring(start, end+1);
			        System.out.println("mystring = "+mystring);
			        removebr = balance(mystring);
			        System.out.println(removebr);
			        if(removebr)
			        {
			            try{


			                s = s.substring(0, end) + s.substring(end+1);
			                System.out.println(s);
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

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("before return s= "+s);
			return s;
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


}
