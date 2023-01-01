#include <iostream>
using namespace std;
#include <string>
#include <sstream>
#include <fstream>
#include <time.h>
#include <cstdlib>
#include <cstdlib>
#include <exception>
#include <stdexcept>
#include <cmath>


#define MAX 50
/****************Global Variables************/
int extraTaxa = 1000; // it should be #of taxa + 1 initially
int Satisfied = 0, n;
int Satisfied_quartets[1000001] = {0};
int debug =0;
int partSat;
int partSatCount  = 0;
int noOfSat;
int noOfVat;
int noOfDef;
string inputpath;
string inputfilename;
bool doSort = true;

/*********************Class***********************/

class list   // creates a doubly linked list
{
public:
	//int val;
	double val;
	string str;
	int part;
	int sat;
	int vat;
	int def;
	int below;
	list * next;
	list * prev;
	list()
	{	sat = -1;
		vat = -1;
		val = -1;
		def = -1;
		part = -1;
		below = 0;
		str = " ";
		next = NULL;
		prev = NULL;	
	}
};

class quartet //creates a quartet ((q1,q2),(q3,q4))
{
public:
	int quartet_id; 
	string q1;
	string q2;
	string q3;
	string q4;
	string status;
	int my_count;
	int modified;
	//list *status; // 0 = both one side, 1 = satisfied, 2 = deferred, 3 = violated
	quartet *qnext;
	quartet()
	{
		quartet_id = -1;
		modified = 0;
		q1=""; q2=""; q3=""; q4="";
		//status = new list(); // 1st element dummy	
		my_count = 1;
		status = "";
		qnext = NULL;
	
	}
	quartet(int n, string a, string b, string c, string d)
	{
		quartet_id = n;
		modified = 0;
	//	status = new list(); 
		status = "";
		q1=a; q2=b; q3=c; q4=d;
		my_count = 1;
		qnext = NULL;
	
	}
};

class taxa //creates a taxa
{
public:
	//int taxa_id;
	string name; // node name
	int partition; //0 = A, 1 = B
	int state; //0 = unmoved, 1 = moved
	int taxaScore;
	int locked;
	list *quartet_list; // lists the id's of those quartets, the taxa belongs to 
	taxa *tnext;
	taxa()
	{
		name = "";
		partition = -1;
		state = 0;
		locked = 0;
		taxaScore = 0;
		quartet_list = NULL;
		tnext = NULL;
	}
	void printTaxa()
	{
		cout<<"name ="<< name;
		cout<<"\npartition = "<<partition; 
		cout<<"\nstate = "<<state; //0 = unmoved, 1 = moved
		cout<<"\n";
	}
};

/**********************Class object Declaration*************************/
quartet *listOfquartet = NULL;
taxa *listOftaxa = NULL;

/**********************Functions****************************************/

void printPartition(taxa* partA, taxa* partB)
{
	taxa *p;
	if(debug)
		cout<<"partition A: \n";
	p=partA->tnext;
	while(p!=NULL)
	{
		if(debug)
			cout<<p->name<<"-> ";
		p=p->tnext;
	}
	if(debug){
		cout<<"NULL;"<<endl;
		cout<<"partition B: \n";
	}
	p=partB->tnext;
	while(p!=NULL)
	{	if(debug)
			cout<<p->name<<"-> ";
		p=p->tnext;
	}
	if(debug)
		cout<<"NULL;"<<endl;
}
int Check_Quartet(taxa* partA, taxa* partB, quartet *q, string tempTaxa )
{
	int a, b, c, d; // corresponds to partiontion of q1, q2, q3, q4; if q1 in Partition A, a = 0, otherwise a = 1
	taxa *p;
	//list *stat, *temp;
	int score = 0;
	
	string s1,qstat;
//	cout<<"Inside Check quartet\n";
		
	p = partB->tnext;
		
	a = 0;
	b = 0;
	c = 0;
	d = 0;
	
	while(p!=NULL)
	{
		s1.assign(p->name);
		if(s1.compare(q->q1)==0) a = 1;
		else if(s1.compare(q->q2)==0) b = 1;
		else if(s1.compare(q->q3)==0) c = 1;
		else if(s1.compare(q->q4)==0) d = 1;
		else
		{
		
		}
		p = p->tnext;
		
	}
	if(!tempTaxa.compare(q->q1)) a = 1 - a;
	else if(!tempTaxa.compare(q->q2)) b = 1-b;
	else if(!tempTaxa.compare(q->q3)) c = 1-c;
	else if(!tempTaxa.compare(q->q4)) d = 1-d;
	else{}
	//stat = new list();

	if (a==b && c==d && b==c) // totally on one side
	{	//score += 2; //stat->val = 0;
		//score += 1;
		score = 1;
		qstat.assign("b");
	}
	else if( a==b && c==d) //satisfied
	{
		//score += 4; //stat->val = 1;
		//score += 1;
		
		score = 6; //6
		qstat.assign("s");
	}
	else if ((a==c && b==d) || (a==d && b==c)) // violated
	{
		//score += 0;
		//score -= 1;
		//score += 1;
		score = 2;
		//stat->val = 3;
		qstat.assign("v");
	
	}
	else //deffered
	{
		//score += 1; //stat->val = 2;
		//score += 0;
		score = 3;
		qstat.assign("d");
	}

	/*temp = q->status->next;
	while(temp!=NULL)
		temp = temp->next;
	temp = stat;*/
	q->status.append(qstat);

	
	return score;
	
}
double Calculate_Score(taxa *parta, taxa *partb, quartet *qt, string tempTaxa, int st, int vt, int df)
{
	int partitionScore, qscore;
	double dps, dps1;
	quartet *q;
	int s, v,l,d;
	s = 0; v = 0; d = 0;
	char c;
	double p;

	//p=(n-Satisfied)/n;

//	cout<<"Start of calcscore\n";
	q = qt->qnext;

	partitionScore = 0;
	
	while(q!= NULL){
		if(!tempTaxa.compare("NULL"))
		{
			q->status.assign("");
			qscore  = Check_Quartet(parta, partb, q, tempTaxa); 
			if(qscore == 6) s++;
			else if(qscore == 2) v++;
			else if(qscore == 3) d++;
		}
		else if(!(q->q1.compare(tempTaxa)&&q->q2.compare(tempTaxa)&&q->q3.compare(tempTaxa)&&q->q4.compare(tempTaxa)))
		{	qscore  = Check_Quartet(parta, partb, q, tempTaxa); 
			//l = q->status.length();		
			c = q->status[0];
			//cout<<"c = "<<c<<endl;
			//  s s,  v v,  d d, s v, s d, v s, v d, d v , d s
			if(c=='s' && qscore == 2) { s--; v++; } // s v
					
			else if(c=='s' && qscore==3){ s--; d++;} // s d
					
			else if(c=='v' && qscore == 6){v--; s++;} // v s		
					
			else if(c=='v' && qscore==3){v--;d++;}  // v d

			else if(c=='d' && qscore==2){d--;v++;} // d v 

			else if(c=='d' && qscore==6){d--;s++;} // d s

			else if(qscore==1)
			{
				if(c=='s') s--;
				else if(c=='v') v--;
				else if(c=='d') d--;
			
			}
			else if(c=='b')
			{
				if(qscore==6) s++;
				else if(qscore==2) v++;
				else if(qscore==3) d++;
			}
					
		}
		//partitionScore += Check_Quartet(parta, partb, q, tempTaxa);
		q = q->qnext;		
	}
	if(!tempTaxa.compare("NULL"))
	{
		noOfSat = s;
		noOfVat = v;
		noOfDef = d;
		
		//partitionScore = 12*s + 3*d - v;
		
		if(v!=0)
			dps = s/v;
		else dps = s;
		if(d!=0)
			dps1 = s/d;
		else dps1 = s;
		//partitionScore = (s-v )+ dps; 
		partitionScore = (s-v);
	}
	else
	{
		noOfSat = st+s;
		noOfVat = vt+v;
		noOfDef = df+d;
		//partitionScore = 12*(st+s)+3*(df+d) - (vt+v);
		
		if((vt+v)!=0)
			dps = (st+s)/(vt+v);
		else dps = (st+s);
		if((df+d)!=0)
			dps1 = (st+s)/(df+d);
		else dps1 = (st+s);
		//partitionScore = ((st+s)-(vt+v))+ dps;
		partitionScore = ((st+s)-(vt+v));
	}
	//partitionScore = ceil(dps);
	
	//dps = dps;	
//	return dps;
	//;
	return partitionScore;
}
int Count_Satisfied_Quartets(taxa *parta, taxa *partb, quartet *qt)
{
	int csat = 0;
	quartet *q;	
	q = qt->qnext;
	int quartet_score;
	if(debug)
		printPartition(parta,partb);
	while(q!= NULL){
		
	//	if(q->modified == 0)
		
			quartet_score = Check_Quartet(parta, partb, q, "NULL");
			if(quartet_score == 6)
			{
				csat++;
				
				
				Satisfied_quartets[q->quartet_id]=1; 
			}
	//	}
		q = q->qnext;		
	}
	Satisfied += csat;
	return csat;
}
int CountPart(taxa* partA)
{
	taxa *t;
	t = partA;
	int count  = 0;
	while(t->tnext!=NULL)
	{
		count++;
		t=t->tnext;
	}
	return count;
}
taxa* FM_Algo(taxa* partA, taxa* partB, quartet* q)
{
	quartet *qt;
	taxa *part, *temp, *pa, *pb ,*temp2, *ftemp, *FinalTaxalist;	
	list *moved_list,*m, *g, *gainList, *gl;
	int  pass = 0, iteration = 0, loop = 0, total=0;
	int ca = 0, cb = 0;
	double score, prevscore; //int
	bool flag = true, iterationMore = true;
	double gainmax  = 0, cumulativeGain = 0, prevCumulativeGain = 0; //int
	int alt = 0, tag1 = 0, tag2 = 0;
	int prevs, prevd, prevv;
	string qtemp;
	string TaxaToMove;
	//int minTaxaScore;
	bool dontmove = false;
	bool LoopAgain = true;
	while(LoopAgain)
	{   loop++;
		if(debug)
			cout<<"\n*********LOOP "<<loop<<" *********\n";
		cumulativeGain = 0; gainmax  = 0; iterationMore = true; iteration = 0;

		moved_list = new list();
		m = moved_list;
		while(iterationMore){
			//**********Start of Outer while (FM ITERATION)******************//
			iteration++;
			if(debug){
				cout<<"\nInside FM Iteration and Iteration = "<<iteration<<"\n";
				cout<<"Initial ";
			}
			score = Calculate_Score(partA, partB,q,"NULL",0,0,0); // sending just 1 partition is OK to find score , initial score //Changed
			ca = CountPart(partA);
			cb = CountPart(partB);
			total  = ca+cb;
			prevs = noOfSat; prevd = noOfDef;prevv = noOfVat;
			prevscore = score;
			//prevscore = 0; // new score e gain according to modified code
			//***Initialization****//
			flag =true; alt = 0; tag1 = 0; tag2 = 0; pass = 0;
			gainList = new list();
			gl = gainList;

			//*************Start of FM PASS****************************//
			pass = 0;
			while(flag)
			{
				
				pass++;
				if(debug)
					cout<<"\nInside FM Pass and Pass = "<<pass<<"\n";
				
				if(alt == 0 && tag1 ==0 ){
					//******** move one taxa from partA to PartB ****************//
					pa = partA->tnext;
					pb = partB->tnext;
					temp = NULL;
					temp = partA;
				
					
					while(pa!= NULL && pa->state == 1)
					{ //skip
						pa = pa->tnext;
					}
					if(pa == NULL)
					{	tag1 = 1;
						if(debug)
							cout<<"*******alt0*********\n";
					}// no node to move from A to B}
					else{

						TaxaToMove.assign(pa->name);
						pa->state = 1;
						
						score = Calculate_Score(partA,partB,q, TaxaToMove,prevs,prevv,prevd);
						gl = gainList;
						while(gl->next!=NULL)
							gl = gl->next;
						list *gtemp;
						gtemp = new list();
						gtemp->str = TaxaToMove;
						gtemp->val = score - prevscore;
						gtemp->below =  ca - 1; 
						gtemp->sat = noOfSat;
						gtemp->vat = noOfVat;
						gtemp->def = noOfDef;

						if(debug)
							cout<<"Move "<<TaxaToMove<<" , Gain = "<<score<<"-"<<prevscore<<" = "<<gtemp->val<<endl;
							//cout<<"Move "<<TaxaToMove<<" , Gain = "<<gtemp->val<<endl;
						gtemp->part = 0; //current partition
						gl->next = gtemp;
					}
					if(tag2 == 0)
						alt =1;
					
				}
				else if(alt == 1 && tag2 == 0)
				{
					//******** move one taxa from partB to PartA ****************//
					pa = partA->tnext;
					pb = partB->tnext;
					
					while(pb!= NULL && pb->state == 1)
					{ //skip
						pb = pb->tnext;
					}
					if(pb == NULL){
						if(debug)
							cout<<"*******alt1*********\n";
						tag2 = 1;
					}// no node to move from B to A}
					else{
						TaxaToMove.assign(pb->name);  
						pb->state = 1;
						score = Calculate_Score(partA,partB,q, TaxaToMove,prevs,prevv,prevd);
						gl = gainList;
						while(gl->next!=NULL)
							gl = gl->next;
						list *gtemp;
						gtemp = new list();
						gtemp->str = TaxaToMove;
						gtemp->val = score - prevscore;
						gtemp->below =  cb - 1; 
						gtemp->sat = noOfSat;
						gtemp->vat = noOfVat;
						gtemp->def = noOfDef;
						if(debug)
							cout<<"Move "<<TaxaToMove<<" , Gain = "<<score<<"-"<<prevscore<<" = "<<gtemp->val<<endl;
							//cout<<"Move "<<TaxaToMove<<" , Gain = "<<gtemp->val<<endl;
						gtemp->part = 1;// currentpartition
						gl->next = gtemp;
					}
					if(tag1 == 0){
						alt = 0;
					}
				}
				 
				
				if(tag1 == 1 && tag2 == 1)
				{ 
					flag = false; // no more iteration is required
					
				}
				
			
			}
			//*************End of FM PASS****************************//
			//Move taxa with highest gain
			gl = gainList->next;
			double maxgain = -10000000000; //int
			int minvio = 0;
			int maxsat = 0;
			int glPart = 0;
			int randnum = 0;
			long int maxdiff = -100000000000, tempdiff;
			double maxratio = 0, tempratio = 0; // d/v
			double maxratio1 = 0, tempratio1 = 0; // s/d
			int nextQ = 0, c1=0, c2=0;
			TaxaToMove.assign("NULL");
		

			while(gl!=NULL)
			{
				if(gl->part==0) {c1 = ca -1; c2= cb+1;}
				else{c1 = ca + 1; c2= cb - 1;}
				
			
				if(gl->val>maxgain && gl->below>=2 )//&&((c1>2||c2>2)&& total!=gl->val+gl->sat))
				{ 
					TaxaToMove = gl->str;
					maxgain = gl->val;
					minvio = gl->vat;
					maxdiff = gl->sat - gl->vat;
					if(gl->vat!=0)
						maxratio = gl->sat-gl->vat;
					else maxratio = gl->sat;
					/*if(gl->def!=0)
						maxratio1 = gl->sat/gl->def;
					else maxratio1 = gl->sat;*/
					
					maxsat = gl->sat;
					glPart = gl->part; // current Partition
				}
				else if(gl->val==maxgain && gl->below>=2 )//&&((c1>2||c2>2)&& total!=gl->val+gl->sat))
				{ /*
					tempdiff = gl->sat - gl->vat;
					tempdiff = gl->sat - gl->vat;*/
					if(gl->vat!=0)
						tempratio = gl->sat/gl->vat;
					else tempratio = gl->sat;
					/*if(gl->def!=0)
						tempratio1 = gl->sat/gl->def;
					else tempratio1 = gl->sat;*/
									
					if(gl->sat > maxsat && gl->below>=2)// && ((c1>2||c2>2)&& total!=gl->val+gl->sat)) //(tempratio1>maxratio1)
					{
						TaxaToMove = gl->str;
						maxgain = gl->val;
						minvio = gl->vat;
						maxsat = gl->sat;
						maxdiff = gl->sat - gl->vat;
						if(gl->vat!=0)
							maxratio = gl->sat/gl->vat;
						else maxratio = gl->sat;
						/*if(gl->def!=0)
							maxratio1 = gl->sat/gl->def;
						else maxratio1 = gl->sat;*/
						glPart = gl->part; 
					}
					else if(gl->sat == maxsat && gl->below>=2)// &&((c1>2||c2>2)&& total!=gl->val+gl->sat))//(tempratio1==maxratio1)
					{	
						/*if(tempratio>maxratio)//(gl->sat > maxsat)
						{
							TaxaToMove = gl->str;
							maxgain = gl->val;
							minvio = gl->vat;
							maxsat = gl->sat;
							maxdiff = gl->sat - gl->vat;
							if(gl->vat!=0)
								maxratio = gl->sat/gl->vat;
							else maxratio = gl->sat;
							/*if(gl->def!=0)
								maxratio1 = gl->sat/gl->def;
							else maxratio1 = gl->sat;*/
						/*	glPart = gl->part; 
						}
						else if(tempratio==maxratio){//(gl->sat==maxsat){					
							*/randnum = 10 + rand()%100;
							if(randnum%2==0){
								TaxaToMove = gl->str;
								maxgain = gl->val;
								minvio = gl->vat;
								maxdiff = gl->sat - gl->vat;
								if(gl->vat!=0)
									maxratio = gl->sat/gl->vat;
								else maxratio = gl->sat;
								/*if(gl->def!=0)
									maxratio1 = gl->sat/gl->def;
								else maxratio1 = gl->sat;*/
								maxsat = gl->sat;
								glPart = gl->part; // current Partition
							}
						//}
					
					}
					
				}
				gl = gl->next;
				
			}
			if(TaxaToMove.compare("NULL")){
				if(glPart == 1)
				{	pa = partA->tnext;
					pb = partB->tnext;
					temp = NULL;
					temp = partB;
					while(pb->name.compare(TaxaToMove))
					{	temp = temp->tnext; // temp is prev taxa 
						pb = pb->tnext;
					}
					temp2 = new taxa();
					temp2->name = TaxaToMove; //pb->name;
					temp2->partition = 0;
					temp2->state = 1;
					temp2->locked = 1;		
					temp->tnext = pb->tnext; //pb->tnext;

					temp2->tnext = pa;
					partA->tnext = temp2;
				}
				else{
					pa = partA->tnext;
					pb = partB->tnext;
					temp = NULL;
					temp = partA;
					while(pa->name.compare(TaxaToMove))
					{
						temp = temp->tnext; // temp is prev taxa 
						pa = pa->tnext;
					
					}
					temp2 = new taxa();
					temp2->name = TaxaToMove; //pa->name;
					temp2->partition = 1;
					temp2->state = 1;
					temp2->locked = 1;
					temp->tnext = pa->tnext; //pa->tnext;

					temp2->tnext = pb;
					partB->tnext = temp2;
				}
					
			//	cout<<" TaxaToMove  = "<<TaxaToMove<<endl;
				g = new list();
				g->val = maxgain;
				g->str.assign(TaxaToMove);// taxa moved
				g->part = 1-glPart; // current partition of the taxa
				g->prev = m;
				m->next = g;
				m = m->next;
			
				pa = partA->tnext;
				iterationMore = false;
				while(pa!=NULL)
				{   if(pa->locked == 1) {pa->state = 1;}
					else
					{	pa->state =0;
						iterationMore = true;
					}
					pa = pa->tnext;
				}
				pb = partB->tnext;
				while(pb!=NULL)
				{   if(pb->locked == 1) {pb->state = 1;}
					else 
					{	pb->state =0;
						iterationMore = true;
					}
					
					pb = pb->tnext;
				}
			}
			else iterationMore = false;
		}// no more iteration
		
		//***********Cumulative gain compute*************//
		gainmax= 0;
		string back = "Initial";
		m = moved_list->next;
		while(m!=NULL)
		{
			cumulativeGain = cumulativeGain + m->val;
			if(debug)
				cout<<"By moving taxa "<<m->str<<" the gain is "<<m->val<<endl;
			if(cumulativeGain>=gainmax)
			{
				gainmax = cumulativeGain;
				back.assign(m->str);
			}
			m = m->next;
		}
		if(debug)
		{
			cout<<"\nMax Cumulative Gain  = "<<gainmax<<endl;
			cout<<"Roll back the moves after moving "<< back <<endl;
		}
		//***********Update Partition*******************//
		m = moved_list->next;
		int rollback = 0;
		if(TaxaToMove.compare("NULL")){
			while(m->next!=NULL)
				m = m->next;   // m points the last element of moved list
			while(m->prev!=NULL || m->val != -1)
			{ // rollback loop
			//	cout<<"Inside rollback\n";
				rollback++;

				pa = partA->tnext;
				pb = partB->tnext;
			//	cout<<m->part;
				if(back.compare(m->str)==0)
					break;
				if(m->part == 0)
				{
					temp = partA; 
					while(pa->name.compare(m->str)&& pa!=NULL) //skip
					{	temp = temp->tnext; // temp is prev taxa 
						pa = pa->tnext;				
					}
					temp2 = new taxa();
					temp2->name = pa->name;
					temp2->partition = 1;
					temp2->state = 0;	
					temp2->taxaScore = 0;
					temp->tnext = pa->tnext;
					temp2->tnext = pb;
					partB->tnext = temp2;
				//	cout<<"123\n";
				}
				else
				{	temp = partB; 
					while(pb->name.compare(m->str) && pb!=NULL) //skip
					{	temp = temp->tnext; // temp is prev taxa 
						pb = pb->tnext;				
					}
					temp2 = new taxa();
					temp2->name = pb->name;
					temp2->partition = 1;
					temp2->state = 0;
					temp2->taxaScore = 0;
					temp->tnext = pb->tnext;
					temp2->tnext = pa;
					partA->tnext = temp2;
				//	cout<<"345\n";
				
				}
				
				m = m->prev;
			//	cout<<"Rollback\n";
			}
		}
		pa = partA->tnext;
		pb = partB->tnext;
		while(pa!=NULL)
		{
			pa->state = 0;pa->locked = 0; pa->taxaScore = 0; pa = pa->tnext;
		}
		while(pb!=NULL)
		{
			pb->state = 0; pb->locked = 0; pb->taxaScore = 0; pb = pb->tnext;
		}
		//*******************************************************************//
		
	
		//*******************************************************************//	
		if(debug)
			cout<<"\n*********************************************\n";
		printPartition(partA, partB);
		if(debug)
			cout<<"\n*********************************************\n";
		//if(cumulativeGain<=prevCumulativeGain)
		if(gainmax <= 0) //|| iteration>1)
		{		LoopAgain = false; //it will stop outer while loop 
				if(debug)
					cout<<"Max Cumulative gain = "<<gainmax<<" ,so no loop further\n";
		}
		//************end of Update Partition****************//
		
	}
	//************end of Loop Again**********//
	//***********Merge Two list***************//
	partSat = Count_Satisfied_Quartets(partA, partB,q);
	
	FinalTaxalist = new taxa();
	part = FinalTaxalist;
	
	while(partA->tnext!=NULL)
	{
	
		partA = partA->tnext;
		ftemp = new taxa();
		ftemp->name.assign(partA->name);
		ftemp->partition = 0;
		ftemp->state = 0;
	//	temp2->taxaScore = 0;
		ftemp->tnext = NULL;
		part->tnext = ftemp; 
		part = part->tnext;
		//pa= pa->tnext;
	}
	while(partB->tnext!=NULL)
	{
	
		partB = partB->tnext;
		ftemp = new taxa();
		ftemp->name.assign(partB->name);
		ftemp->partition = 1;
		ftemp->state = 0;
	//	temp2->taxaScore = 0;
		ftemp->tnext = NULL;
		part->tnext = ftemp; 	
		part = part->tnext;
		//pb= pb->tnext;
	}
	
	return FinalTaxalist;

}

taxa* FM(taxa* t, quartet* q)
{
	taxa *partA, *partB, *P, *temp, *pa, *pb, *taxaList,*patemp,*pbtemp,*Prev;
	quartet *Qt, *Qtemp, *prev1, *prev2;
	int ca=0,cb=0;
	int a, b, c, d;
	int flag = 0;
	string qq1,qq2,qq3,qq4,ex;
	string tq1,tq2, tq3, tq4, tstat;
	int tqid, tcount, tmod;
				
	int m,n, tag = 0;
	if(doSort){
	
		Qt = q;
		while(Qt->qnext!=NULL)
		{	
			Qt  = Qt->qnext;
			Qtemp = q;
			tag = 0;
			while(Qtemp->qnext!=NULL)
			{	m = 0; n = 0;
				Qtemp = Qtemp->qnext;
				if(Qt->quartet_id == Qtemp->quartet_id) continue;
				if(!(Qt->q1.compare(Qtemp->q1)||Qt->q2.compare(Qtemp->q2)))m++;
				else if(!(Qt->q1.compare(Qtemp->q2)||Qt->q2.compare(Qtemp->q1)))m++;
				else if(!(Qt->q1.compare(Qtemp->q3)||Qt->q2.compare(Qtemp->q4)))m++;
				else if(!(Qt->q1.compare(Qtemp->q4)||Qt->q2.compare(Qtemp->q3)))m++;

			
				if(!(Qt->q3.compare(Qtemp->q1)||Qt->q4.compare(Qtemp->q2)))n++;
				else if(!(Qt->q3.compare(Qtemp->q2)||Qt->q4.compare(Qtemp->q1)))n++;
				else if(!(Qt->q3.compare(Qtemp->q3)||Qt->q4.compare(Qtemp->q4)))n++;
				else if(!(Qt->q3.compare(Qtemp->q4)||Qt->q4.compare(Qtemp->q3)))n++;

				if(n>0&&m>0)
				{
					Qtemp->my_count++;
					tag++;
					//Qt->my_count++;
				}
			
		
			}
			if(tag!=0)Qt->my_count++;
		}
		// sort q by count
		//Qt = q;
		tag  = 1;
		while(tag)
		{			
			Qt  = q;
			Qtemp = Qt->qnext;
			tag  = 0;
			while(Qtemp->qnext!=NULL)
			{
				Qt  = Qt->qnext;
				Qtemp  = Qtemp->qnext;

				if(Qtemp->my_count>Qt->my_count)
				{
					tqid= Qtemp->quartet_id; 
					tq1 =Qtemp->q1;
					tq2 =Qtemp->q2;
					tq3 =Qtemp->q3;
					tq4 =Qtemp->q4;
					tstat=Qtemp->status;
					tcount = Qtemp->my_count;
					tmod = Qtemp->modified;
				
					Qtemp->quartet_id = Qt->quartet_id;
					Qtemp->q1 = Qt->q1;
					Qtemp->q2 = Qt->q2;
					Qtemp->q3 = Qt->q3;
					Qtemp->q4 = Qt->q4;
					Qtemp->status = Qt->status;
					Qtemp->my_count =  Qt->my_count;
					Qtemp->modified = Qt->modified;

					Qt->quartet_id = tqid;
					Qt->q1 = tq1;
					Qt->q2 = tq2;
					Qt->q3 = tq3;
					Qt->q4 = tq4;
					Qt->status = tstat;
					Qt->my_count =  tcount;
					Qt->modified= tmod;
					tag++;
				
				}
			}
		}
	
	}
	doSort = false;
	if(debug)
	{
		cout<<".......FM........ \n";
		cout<<"Initial Partition :\n";
	}
	//******************** Initial Partition*****************//
	partA = new taxa();
	pa = partA;
	partB = new taxa();
	pb = partB;
	P = t;
	Qt = q;

	while(Qt->qnext!=NULL)
	{
		a=-1;b=-1;c=-1;d=-1;
		Qt=Qt->qnext;
		qq1.assign(Qt->q1);
		qq2.assign(Qt->q2);
		qq3.assign(Qt->q3);
		qq4.assign(Qt->q4);
		P=t;
		while(P->tnext!=NULL)
		{	Prev = P;
			P=P->tnext;
			if(!(Qt->q1.compare(P->name)&&Qt->q2.compare(P->name)&&Qt->q3.compare(P->name)&&Qt->q4.compare(P->name)))
			{
				Prev->tnext=P->tnext;
				P=Prev;
			}

			//flag = 1;

		}
		patemp = partA;
		pbtemp = partB;
		while(patemp->tnext!=NULL)
		{
			patemp = patemp->tnext;
			if(!(qq1.compare(patemp->name)&&qq2.compare(patemp->name)&&qq3.compare(patemp->name)&&qq4.compare(patemp->name)))
			{
				if(!qq1.compare(patemp->name)) a = 0;
				else if (!qq2.compare(patemp->name)) b = 0;
				else if (!qq3.compare(patemp->name)) c = 0;
				else if (!qq4.compare(patemp->name)) d = 0;
			}
		}
		while(pbtemp->tnext!=NULL)
		{
			pbtemp = pbtemp->tnext;
			if(!(qq1.compare(pbtemp->name)&&qq2.compare(pbtemp->name)&&qq3.compare(pbtemp->name)&&qq4.compare(pbtemp->name)))
			{
				if(!qq1.compare(pbtemp->name)) a = 1;
				else if (!qq2.compare(pbtemp->name)) b = 1;
				else if (!qq3.compare(pbtemp->name)) c = 1;
				else if (!qq4.compare(pbtemp->name)) d = 1;
			}
		}
		if(a==-1 && b==-1 && c==-1 && d==-1) //all new
		{
			temp = new taxa();
			temp->name = Qt->q1;
			temp->partition = 0;
			pa->tnext = temp; 
			pa= pa->tnext;
			a= 0; ca++;
			temp = new taxa();
			temp->name = Qt->q2;
			temp->partition = 0;
			pa->tnext = temp; 
			pa= pa->tnext;
			b=0; ca++;
			temp = new taxa();
			temp->name = Qt->q3;
			temp->partition = 1;
			pb->tnext = temp; 
			pb= pb->tnext;
			c= 0; cb++;
			temp = new taxa();
			temp->name = Qt->q4;
			temp->partition = 1;
			pb->tnext = temp; 
			pb= pb->tnext;
			d=0; cb++;
		}
		else		
		{
			if(a==-1)
			{	temp = new taxa();
				temp->name = Qt->q1;
				if(b!=-1){
					if(b==0) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; a=0;ca++;}
					else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; a=1;cb++;}
				}
				else if(c!=-1){
					if(c==1) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; a=0;ca++;}
					else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; a=1;cb++;}
				}
				else if(d!=-1)
				{
					if(d==1) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; a=0;ca++;}
					else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; a=1;cb++;}
				}
				
			}
			if(b==-1)
			{
				temp = new taxa();
				temp->name = Qt->q2;
				
				if(a==0) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; b=0;ca++;}
				else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; b=1;cb++;}
				
			}
			if(c==-1)
			{
				temp = new taxa();
				temp->name = Qt->q3;
				if(d!=-1)
				{
					if(d==0) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; c=0;ca++;}
					else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; c=1;cb++;}
				}
				else{
					if(a==1) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; c=0;ca++;}
					else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; c=1;cb++;}
				}
			}
			if(d==-1)
			{
				temp = new taxa();
				temp->name = Qt->q4;
				if(c==0) {temp->partition = 0; pa->tnext = temp; pa= pa->tnext; d=0;ca++;}
				else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext; d=1;cb++;}
			}
		
		}


	
	}
	P = t;
	//pa = partA;
	//pb = partB;
	c = 0;
	while(P->tnext!=NULL)
	{	P = P->tnext;
		if(ca<cb)
			c=2;
		else if(cb<ca)
			c=1;
		else c++;

		ex.assign(P->name);
		temp = new taxa();
		temp->name = ex;
		if(c%2==0){temp->partition = 0; pa->tnext = temp; pa= pa->tnext;ca++;}
		else {temp->partition = 1; pb->tnext = temp; pb= pb->tnext;cb++;}
		
	}
	
	printPartition(partA, partB);
	//*****************End of Random Initial Partition*****************//
	taxaList = FM_Algo(partA, partB, q);

	return taxaList;
}
//string ParseString(strin tempstring, string sdel, string edel)
//{

//}
void readQuartets()
{
	int count = 0, sk1,sk2,sk3,sk4, tcount=0;
	quartet *temp, *loq;
	taxa *tax,*lot, *l;
	lot = listOftaxa;
	string qt;
	int found;
	char buffer[100];
	int length, len, start = 2;

	
	loq = listOfquartet; // first element dummy listOfquartet->qnext == 1st quartet
	//cin>>n; // no. of input quartets
	
	cin >> qt;
	while(!qt.empty()){
		count++;
		start = 2;
	//	cout<<"Quartet "<<count<<endl;
		temp = new quartet();
		temp->quartet_id = count;
		//cin >> qt;
		found=qt.find(',');
		len = found - start;
		length=qt.copy(buffer,len,start);
		buffer[length]='\0';
		temp->q1.assign(buffer);
		if(!temp->q1.compare("0"))
			temp->q1.assign("O");
		
		start = found + 1;
		found=qt.find(')');
		len = found - start;
		length=qt.copy(buffer,len,start);
		buffer[length]='\0';
		temp->q2.assign(buffer);
		if(!temp->q2.compare("0"))
			temp->q2.assign("O");
		try{
		qt.assign(qt.replace(0,found+2,""));
		}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
		//cout << qt;

		start = 1;
		found = qt.find(',');
		len = found - start;
		length=qt.copy(buffer,len,start);
		buffer[length]='\0';
		temp->q3.assign(buffer);
		if(!temp->q3.compare("0"))
			temp->q3.assign("O");

		start = found + 1;
		found=qt.find(')');
		len = found - start;
		length=qt.copy(buffer,len,start);
		buffer[length]='\0';
		temp->q4.assign(buffer);
		if(!temp->q4.compare("0"))
			temp->q4.assign("O");

		loq->qnext = temp;
		loq = loq->qnext;

		sk1 = 0; sk2 = 0; sk3 = 0; sk4 = 0;

		l = listOftaxa;	
		while(l!=NULL)  
		{		
			if(l->name.compare(temp->q1)==0) sk1 = 1;
			else if(l->name.compare(temp->q2)==0) sk2 = 1;
			else if(l->name.compare(temp->q3)==0) sk3 = 1;
			else if(l->name.compare(temp->q4)==0) sk4 = 1;
			else{;}
			l = l->tnext;
		}
		
		if(sk1 == 0)
		{	tax = new taxa();
			tcount++;
			tax->name.assign(temp->q1); 
		/*	list *ql, *qlp;
			ql = new list();
			ql->val = temp->quartet_id;
			qlp = tax->quartet_list;
			while(qlp->next!=NULL)qlp = qlp->next;
			qlp->next = ql;

			tax->quartet_list = */
			lot->tnext = tax;
			lot = lot->tnext;
		}
		if(sk2 == 0)
		{	tax = new taxa();
			tcount++;
			tax->name.assign(temp->q2); 
			lot->tnext = tax;
			lot = lot->tnext;
		}
		if(sk3 == 0)
		{	tax = new taxa();
			tcount++;
			tax->name.assign(temp->q3); 
			lot->tnext = tax;
			lot = lot->tnext;
		}
		if(sk4 == 0)
		{	tax = new taxa();
			tcount++;
			tax->name.assign(temp->q4); 
			lot->tnext = tax;
			lot = lot->tnext;
		}

		qt = "";
		cin >> qt;
	}
	printPartition(listOftaxa,lot);
	/*freopen( "tcount.txt", "w", stdout ); 
	cout<<tcount;
	fclose(stdout);*/
	

	//populate taxa list here
}
void readTaxa()
{
	int count=0, n;
	taxa *temp,*lot;
	lot = listOftaxa; // first element dummy listOfquartet->qnext == 1st quartet
	cin>>n; // no. of input taxa
	while(count<n)//while !eof
	{
		count++;
		cout<<"Taxa "<<count<<endl;
		temp = new taxa();
		cin >> temp->name; 
		lot->tnext = temp;
		lot = lot->tnext;
	}
}
/*string merge(string s1, string s2, string extra)
{
	string s;
	int pos1, pos2, l;

	pos1 = s1.find(extra);
	pos2 = s2.find(extra);
	l = extra.length();

	pos1--;
	pos2--;

	s1.assign(s1.replace(pos1,l+1,""));
	s2.assign(s2.replace(pos2,l+1,""));

	s.assign("(");
	s.append(s1);
	s.append(",");
	s.append(s2);
	s.append(")");


	return s;

}*/

bool bracket(string s)
{
	int flag = 0;
	int l;
	l=s.length();
	if(s[0]!='(')
		return false;
	if(s[l-1]!=')')
		return false;

	for(int i = 1; i<l-1;i++ )
	{
		if(s[i]=='(' || s[i]==')')
			return false;
	}
	return true;

}
string remove_bracket(string s)
{
	int l,i,tag, t = 0;
	bool flag = false;
	l = s.length();
	i = 1;
	int match = 0;
	while(s[i])
	{
		if(!flag)
		{
			if(s[i]=='(' && s[i-1]=='(')
				flag = true;
				tag = i;
		}
		else if(s[i]==')' && s[i+1]==')' )
		{
			if(match==0)
				{
					s.replace(tag,1,"");
					s.replace(i,1,"");
					i-=3;
					flag =false;
					t = 0;
					match = 0;
					
				}
			else {
				if (s[i+2]==')') //cholbe advance
					match--;
				else //no match so do not remve bracket.
				{
					flag =false;
					t = 0;
					match = 0;
				
				}
			
			}
				
			
		}
		else // flag true
		{
			if(t==0 && s[i]==')')
			{
				flag = false;
			}
			else if (t==0 && s[i]=='(')
			{
				t = 1;
				match = 1;
			}
			else if(t==1 && s[i]=='(')
				match++;
			else if(t==1 && s[i]==')')
				match--;
		}
		i++;
	}
	return s;

}
bool balance(string s)
{
	int i, l, ob = 0, com = 0;
	l = s.length();
	
	for(i = 1; i<l-1; i++)
	{
		
		if(s[i]=='(') ob++;
		else if(s[i]==')') ob--;
		else if(s[i]==',') com = 1;
		if(ob<0) return false; // don't remove bracket;
	} 
	if((s[1]=='('&& s[l-2]==')' && ob==0) || com==0)return true; //remove unnecessary bracket
	else return false;
	
}
string merge(string s1, string s2, string extra)
{
	string s, t1, t2, t3, str1, str2, str3, temp, br;
	int pos1, pos2, l, i, p,q,randnum;
	char *temp2, *temp3;
	char *buff;
	bool brkt;
	
	if(debug)
	cout<<"Unrooted s1 (before) "<<s1<<endl;
	brkt = bracket(s1);
	if(!brkt)
	{
		br.assign("(");
		br.append(s1);
		br.append(")");
		s1.assign(br);
		
	}

	char buffer [L_tmpnam];
	
	tmpnam (buffer);
	//ostringstream convert;
	/*
	randnum = 35 + rand()%5000000;
	ostringstream convert;   // stream used for the conversion
	convert << randnum;      // insert the textual representation of 'Number' in the characters in the stream
	f1.assign("reroot1");
	f1.append(convert.str());
	f1.append(".txt");

	
	l = f1.length();
	buff = new char[l+1];
	for(i = 0; i<l; i++)
		buff[i] = f1[i];
	buff[i]='\0';*/


	//cout<<"f1 = "<<f1<<endl;
	string f1(buffer);

	if(debug)
	cout<<"Unrooted s1 (after)"<<s1<<" & extra ="<<extra<<endl;
	str1 = "\"";
	str1.append(s1);
	str1.append(";\"");
	str2 = "\"";
	str2.append(extra);
	str2.append("\"");
	str3 = " -o \"";
	str3.append(f1); str3.append("\"");
	temp.assign("perl reroot_tree_new.pl -t ");
	temp.append(str1); temp.append(" -r "); temp.append(str2); temp.append(str3); 

	l = temp.length();
	
	temp2 = new char[l+1];
	for(i = 0; i<l; i++)
		temp2[i] = temp[i];
	temp2[i]='\0';

	int rt = -1;
	rt = system((char *)temp2);

	FILE *pFile;
	pFile = fopen(buffer, "r");
	
	while(pFile==NULL){ fclose(pFile);  pFile = fopen(buffer, "r");  if(debug) cout <<"inside fopen s1"<<endl;}
	char c;
	fseek(pFile, 0, SEEK_SET);
	c = fgetc(pFile);
	while(c == ' ') {
		if(debug)
		cout <<"inside fseek s1"<<endl;
		fseek(pFile, 0, SEEK_SET);
		c = fgetc(pFile);
	}
	

	//freopen( "reroot1.txt", "r", stdin );
	string s1n;
	fseek(pFile, 0, SEEK_SET);
	std::ifstream filein(buffer);
	std::getline(filein, s1n);
	
	fclose(pFile);
	
	//cin >> s1n;	
	s1.assign(s1n);
	if(debug)
	cout<<"rerooted s1 "<<s1<<endl;
	
	//fclose(stdin);
	//remove(buffer);

	p = s1.find(":");
	if(p>=0){
		l = s1.length();
		q = l-2;
		try{
		s1.assign(s1.replace(p,q-p,""));
		}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
		//cout<<s<<endl;
	}
	l = s1.length();
	try{
		if(s1[l-1]==';')
			s1.assign(s1.replace(l-1,1,""));  //removes semicolon
	}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
	//s1.assign(s1.replace(p,l-p+1,""));	
	if(debug)
	cout<<"rerooted s1 bracket removed "<<s1<<endl;
	
	if(debug)
	cout<<"Unrooted s2 (before)"<<s2<<endl;
	brkt = bracket(s2);
	if(!brkt)
	{
		br.assign("(");
		br.append(s2);
		br.append(")");
		s2.assign(br);
		
	}
	/*randnum = 55 + rand()%500;
	//sprintf(buff,"%d",randnum);
	convert << randnum;      // insert the textual representation of 'Number' in the characters in the stream
	f2.assign("reroot2");
	f2.append(convert.str());
	f2.append(".txt");

	l = f2.length();
	buff = new char[l+1];
	for(i = 0; i<l; i++)
		buff[i] = f2[i];
	buff[i]='\0';*/

	tmpnam (buffer);
	
	string f2(buffer);

	//cout<<"f2 = "<<f2<<endl;	

	if(debug)
	cout<<"Unrooted s2 (after)"<<s2<<" & extra ="<<extra<<endl;
	str1 = "\""; 
	str1.append(s2);
	str1.append(";\"");
	str3 = " -o \"";
	str3.append(f2); str3.append("\"");
	temp.assign("perl reroot_tree_new.pl -t ");
	temp.append(str1); temp.append(" -r "); temp.append(str2);temp.append(str3); 

	l = temp.length();
	
	temp3 = new char[l+1];
	for(i = 0; i<l; i++)
		temp3[i] = temp[i];
	temp3[i]='\0';


	system((char *)temp3);

	
	pFile = NULL;
	pFile = fopen (buffer, "r");
	
	while(pFile==NULL){ fclose(pFile); pFile = fopen (buffer, "r"); if(debug) cout <<"inside fopen s2"<<endl;}
	
	fseek(pFile, 0, SEEK_SET);
	c = fgetc(pFile); 
	while(c == ' ') {
		if(debug)
		cout <<"inside fseek s2"<<endl;
		fseek(pFile, 0, SEEK_SET);
		c = fgetc(pFile);
	}
	
	
	//freopen( "reroot2.txt", "r", stdin );
	string s2n = "";

	fseek(pFile, 0, SEEK_SET);
	std::ifstream filein2(buffer);
	std::getline(filein2, s2n);
	//cin>>s2n;
	s2.assign(s2n);
	if(debug)
	cout<<"rerooted s2 "<<s2<<endl;
	fclose(pFile);

	remove(buffer);


	p = s2.find(":");
	if(p>=0){
		l = s2.length();
		q = l-2;
		try{
			s2.assign(s2.replace(p,q-p,""));
			}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
		//cout<<s<<endl;
	}
	l = s2.length();
	try{
		if(s2[l-1]==';')
		s2.assign(s2.replace(l-1,1,""));
		}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
	//s2.assign(s2.replace(p,l-p+1,""));
	if(debug)
	cout<<"rerooted s2 bracket removed"<<s2<<endl;
	
	
	//***************************************************//

	pos1 = s1.find(extra);
	pos2 = s2.find(extra);
	l = extra.length();
	int start, end, ob, cb;
	string mystring;
	bool removebr = false;
	try{
		s1.assign(s1.replace(pos1,l,""));
		s2.assign(s2.replace(pos2,l,""));
		if(debug)
		cout << "After removing extra\n s1: "<<s1<<"\n s2: "<<s2<<endl;
	}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
	
	if(s1[pos1-1]=='('&& s1[pos1]==',')
	{	try{
		s1.assign(s1.replace(pos1,1,""));
		start = pos1-1;
		i = start; ob = 0;
		while(1)
		{
			if(s1[i]=='(')
				ob++;
			else if(s1[i]==')')
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
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
		mystring = s1.substr(start, end-start+1);
		removebr = balance(mystring);
		if(removebr)
		{ 
			try{
			if(debug)
			cout<< "s1: (, end = "<<end<< " and start = "<<start<<endl;

			s1.assign(s1.replace(end,1,""));
			s1.assign(s1.replace(start,1,""));
			}
			catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
			}

		}
		
	}
	else if(s1[pos1-1]==','&& s1[pos1]==')')
	{
		try{
		s1.assign(s1.replace(pos1-1,1,""));
		end = pos1-1; 
		i = end; cb = 0;
		while(1)
		{
			if(s1[i]==')')
				cb++;
			else if(s1[i]=='(')
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
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
		mystring = s1.substr(start, end-start+1);
		removebr = balance(mystring);
		if(removebr)
		{ 
			try{
			if(debug)
			cout<< "s1: ,) end = "<<end<< " and start = "<<start<<endl;

			s1.assign(s1.replace(end,1,""));
			s1.assign(s1.replace(start,1,""));
			}
			catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
			}

		}

		
	
	}
	else if(s1[pos1-1]==','&& s1[pos1]==',')
	{
		try{
		s1.assign(s1.replace(pos1-1,1,""));
		}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
		if(s2[pos1-2]=='('&& s2[pos1]==')')
		{
			try{
				s2.assign(s2.replace(pos1,1,""));
				s2.assign(s2.replace(pos1-2,1,""));
				
			}
			catch (out_of_range &oor) {
					cerr << "Out of Range error: " << oor.what() << endl;
				}
		}
	
	
	
	}
	
	
	if(s2[pos2-1]=='('&& s2[pos2]==',')
	{
		try{
		s2.assign(s2.replace(pos2,1,""));
		start = pos2-1;
		i = start; ob = 0;
		while(1)
		{
			if(s2[i]=='(')
				ob++;
			else if(s2[i]==')')
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
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
		mystring = s2.substr(start, end-start+1);
		removebr = balance(mystring);
		if(removebr)
		{ 
			try{
			if(debug)
			cout<< "s2: (, end = "<<end<< " and start = "<<start<<endl;

			s2.assign(s2.replace(end,1,""));
			s2.assign(s2.replace(start,1,""));
			}
			catch (out_of_range &oor) {
			cerr << "Out of Range error: here1" << oor.what() << endl;
			}

		}
		
		
	
	}
	else if(s2[pos2-1]==','&& s2[pos2]==')')
	{
		try{
		s2.assign(s2.replace(pos2-1,1,""));
		end = pos2-1; 
		i = end; cb = 0;
		while(1)
		{
			if(s2[i]==')')
				cb++;
			else if(s2[i]=='(')
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
		catch (out_of_range &oor) {
			cerr << "Out of Range error: hh" << oor.what() << endl;
		}
		if(debug)
		cout<< "end = "<<end<< " and start = "<<start<<endl;

		mystring = s2.substr(start, end-start+1);
		removebr = balance(mystring);
		if(removebr)
		{ 
			try{
			if(debug)
			cout<< "s2: ,) end = "<<end<< " and start = "<<start<<endl;
			s2.assign(s2.replace(end,1,""));
			s2.assign(s2.replace(start,1,""));
			}
			catch (out_of_range &oor) {
			cerr << "Out of Range error: hereh" << oor.what() << endl;
			}

		}

		
	

	}
	else if(s2[pos2-1]==','&& s2[pos2]==',')
	{
		try{
			s2.assign(s2.replace(pos2-1,1,""));
		}
		catch (out_of_range &oor) {
			cerr << "Out of Range error: " << oor.what() << endl;
		}
		if(s2[pos2-2]=='('&& s2[pos2]==')')
		{
			try{
				s2.assign(s2.replace(pos2,1,""));
				s2.assign(s2.replace(pos2-2,1,""));
				
			}
			catch (out_of_range &oor) {
					cerr << "Out of Range error: " << oor.what() << endl;
				}
		}
	
	
	}
	
	if(debug)
	 cout<< "before retrun  "<<s1<<"  &  "<<s2<<endl;

//	s.assign("(");
	s.append(s1);
	s.append(",");
	s.append(s2);
//	s.append(")");
	

	return s;

}


string DepthOneTree(taxa* taxa_list)
{
	string s;
	int numberOfTaxa = 0, flag = 1;
	taxa* pt;
	pt = taxa_list->tnext;
	while(pt!=NULL)
	{
		numberOfTaxa++;
		pt = pt->tnext;
	}
//	if(numberOfTaxa<=3){
		s.assign("(");
		while(taxa_list->tnext!=NULL)
		{
			taxa_list = taxa_list->tnext;
			s.append(taxa_list->name);
			if(taxa_list->tnext!=NULL)
				s.append(",");

		}
		s.append(")");
//	}
	/*else
	{
		s.assign("(");
		while(taxa_list->tnext!=NULL)
		{
			if(flag==1 || flag == numberOfTaxa -1)
				s.append("(");

			taxa_list = taxa_list->tnext;
			s.append(taxa_list->name);

			if(flag==2 || flag == numberOfTaxa)
				s.append(")");

			if(taxa_list->tnext!=NULL)
				s.append(",");	
			flag++;
		}
		s.append(")");
	
	}*/
	return s;
}
string SQP(taxa* taxa_list, quartet* Q)
{
	taxa *partA, *partB, *P, *temp, *pa, *pb, *tcount; 
	quartet *QuartetA, *QuartetB, *qa, * qb, *qtemp;
	
	string extra = "extra";
	char buff[200];

	//TRY TO GENERATE RANDOM STRING
	//extra = extraTaxa;
	int taxacount = 0;
	tcount = taxa_list;
	while (tcount->tnext!=NULL)
	{
		taxacount++;
		tcount = tcount->tnext;
	}

	
	string s1, s2, s;
	if(taxacount==0)
	{	s=("");
		return s;
	}
	if(Q->qnext==NULL || taxacount < 3)
	{
		
		
		s = DepthOneTree(taxa_list);
		//cout<< "\nreturn a tree of depth 1 with all taxa\n = "<< s<<endl; //taxa list er end er ta newly added
	//	s = strcat(s2,s2);
	}
	else
	{
		//cout<<"TaxaCount"<<taxacount<<endl;
		
		P = FM(taxa_list, Q);
		if(partSat==0){

			partSatCount++;
			if(partSatCount>100) //mc dependant step. No. of sat quartets = 0 in successive 20 iterations
			{	partSatCount = 0;
				s = DepthOneTree(taxa_list);
				return s;
			}
		}
		else partSatCount=0;
	//	itoa(extraTaxa,buff,10);
		sprintf(buff,"%d",extraTaxa);
		extraTaxa++;
		extra.append(buff);
	//	cout<<"\nback from FM\n";
		partA = new taxa();
		pa = partA;
		partB = new taxa();
		pb = partB;

		while(P->tnext!=NULL)
		{ 
			P = P->tnext;
			
			temp = new taxa();
			temp->name = P->name;
			if(P->partition == 0){
				temp->partition = 0;
				pa->tnext = temp; 
				pa= pa->tnext;
			}
				
			else{			
				temp->partition = 1;
				pb->tnext = temp; 
				pb= pb->tnext;
			}
			
		}
		temp = new taxa();
		temp->name.assign(extra);
		temp->partition = 0;  // add extra taxa to parttion A
		pa->tnext = temp;
		pa= pa->tnext;

		temp = new taxa();
		temp->name.assign(extra);
		temp->partition = 1; // add extra taxa to parttion B
		pb->tnext = temp;
		pb= pb->tnext;

		printPartition(partA, partB);


		QuartetA = new quartet();
		qa = QuartetA;
		QuartetB = new quartet();
		qb = QuartetB;
		string t1,t2,t3,t4;
		char c;
		int l,dcount=0;
		while(Q->qnext!=NULL)
		{
			Q = Q->qnext;
			l = Q->status.length();		
			c = Q->status[l-1];
			if(debug)
				cout<< "Status of Quartet "<<Q->quartet_id<<"= "<<c<<endl;
			if(c == 'b' || c == 'd' )
			{
				//Q = Q->qnext;
				qtemp =  new quartet();
				qtemp->quartet_id =Q->quartet_id;		
				qtemp->q1.assign(Q->q1);
				qtemp->q2.assign(Q->q2);
				qtemp->q3.assign(Q->q3);
				qtemp->q4.assign(Q->q4);
				qtemp->status.assign(Q->status);
				if(debug)
					cout << "....q1 ="<<Q->q1<<"....q2 ="<<Q->q2<<"....q3 ="<<Q->q3<<"....q4 ="<<Q->q4<<".......\n";
				
				if(c=='b')
				{	
					pa = partA->tnext;
					while(pa!=NULL && (pa->name.compare(Q->q1))!=0 ) // logic error
					{ 
						pa = pa->tnext;
						//if(debug)
						//	cout<<"both side\n";
					}
	
					if(pa==NULL)
					{	//if(debug)
						//	cout<< "........else.......\n";
						qb->qnext = qtemp;
						qb = qb->qnext;
						//place Q to QB
					}
					else //((pa->name.compare(Q->q1))==0)// q1 on part A
					{
						//if(debug)
						//	cout<< "........if.......\n";
						qa->qnext = qtemp;
						qa = qa->qnext;
						//place Q to QA	
					}
				}
				else // deferred
				{	qtemp->modified = 1;
					dcount =0 ;
					pa = partA->tnext;
					while(pa!=NULL)  
					{
						if(pa->name.compare(Q->q1)==0 || pa->name.compare(Q->q2)==0||pa->name.compare(Q->q3)==0||pa->name.compare(Q->q4)==0)						
						dcount++;
						pa = pa->tnext;
						if(dcount==3)
						{
							//cout<<"dcount  =  "<<dcount<<endl;
							break;
						}
					}
					if(dcount ==1) // find for either mathch for q1, q2, q3, q4 on partB, change it
					{
						pa = partA->tnext;
						while(pa->name.compare(Q->q1)!=0 && pa->name.compare(Q->q2)!=0 && pa->name.compare(Q->q3)!=0 && pa->name.compare(Q->q4)!=0)  
						{
							pa = pa->tnext;
						}
						if(pa->name.compare(Q->q1)==0)qtemp->q1.assign(extra);
						else if(pa->name.compare(Q->q2)==0)qtemp->q2.assign(extra);
						else if(pa->name.compare(Q->q3)==0)qtemp->q3.assign(extra);
						else qtemp->q4.assign(extra);

						qb->qnext = qtemp;
						qb = qb->qnext;
						//place Q to QB
					}
					else{// find for either mathch for q1, q2, q3, q4 on partB, change it

						pb = partB->tnext;
						while(pb->name.compare(Q->q1)!=0 && pb->name.compare(Q->q2)!=0 && pb->name.compare(Q->q3)!=0 && pb->name.compare(Q->q4)!=0)  
						{
							pb = pb->tnext;
						}
						if(pb->name.compare(Q->q1)==0)qtemp->q1.assign(extra);
						else if(pb->name.compare(Q->q2)==0)qtemp->q2.assign(extra);
						else if(pb->name.compare(Q->q3)==0)qtemp->q3.assign(extra);
						else qtemp->q4.assign(extra);

						qa->qnext = qtemp;
						qa = qa->qnext;
						//place Q to QA
					}
				
				}
				//if(debug)
				//	cout<< "........endIF.......\n";
				
			}
			//if(debug)
			//	cout<< "........endwhile.......\n";
			
		}
		s1 = SQP(partA, QuartetA);
		s2 = SQP(partB, QuartetB);
		s = merge(s1,s2,extra);
	//	cout<< "Merged tree\n = "<<s<<endl; //taxa list er end er ta newly added

	}	
	return s;
}
/*int main()
{
	string s;
	char inputfile[30], outputfile[30];
	
	quartet *q;
	list *Quartet_id, *temp, *qid;
	listOfquartet = new quartet();
	listOftaxa = new taxa();

	cin>>inputfile;
	cin>>outputfile;
	time_t now = time(0);
	char* dt = ctime(&now);

	freopen( inputfile, "r", stdin );
	//freopen( "input_3.txt", "r", stdin );
	

	readQuartets();
	fclose(stdin);
	
	
	freopen( outputfile, "w", stdout );
	//freopen( "output_3.txt", "w", stdout );
	
	clock_t start = clock();
	
	s = SQP(listOftaxa, listOfquartet);

	clock_t ends = clock();
	
	cout<<"Total No. of Satisfied Quartets = "<<Satisfied<<endl;
	cout<<"Consistent quartets are:  "<<endl;
	int l=0;
	for(int i=1;i<=n;i++)
	{
		if(Satisfied_quartets[i]==1)
		{
			cout<<i;
			cout<<", ";
			l++;
		}
	}
	cout<<"\n";
	cout<<"Total No. of Satisfied Quartets = "<<l<<endl;
	cout << "Running Time : " << (double) (ends - start) / CLOCKS_PER_SEC <<" seconds" <<endl;
	
	
	return 0;
}*/
int main(int argc, char *argv[])
{
	string s;
	long double diff;
	int i;
	quartet *q;
	list *Quartet_id, *temp, *qid;
	listOfquartet = new quartet();
	listOftaxa = new taxa();

	char *pFilename = argv[1];
	cout << argv[1];  // name of the input file
	freopen( argv[1], "r", stdin );
	//freopen( "input_3.txt", "r", stdin );


	

	
	
	readQuartets();
	fclose(stdin);
	

	//freopen( "output_t100_q10000_m_13-2-13.txt", "w", stdout );
	freopen( argv[2], "w", stdout );  // argv[2] is the name of the output file
	//freopen( "output_3.txt", "w", stdout );
	
	//clock_t start = clock();
	time_t t1 = time(NULL);
	
	s = SQP(listOftaxa, listOfquartet);

	time_t t2 = time(NULL);

	int f = s.find('O');
	if(f>=0)
	s.replace(f,1,"0");

	cout<<"("<<s<<");\n"<<endl;
	diff = difftime(t2,t1);

	//clock_t ends = clock();
	
	//cout<<"Total No. of Satisfied Quartets = "<<Satisfied<<endl;
	/*cout<<"Consistent quartets are:  "<<endl;
	int l=0;
	for(int i=1;i<=n;i++)
	{
		if(Satisfied_quartets[i]==1)
		{
			cout<<i;
			cout<<", ";
			l++;
		}
	}
	cout<<"\n";
	cout<<"Total No. of Satisfied Quartets = "<<l<<endl;*/
	//cout << "Running Time : " << (double) (ends - start) / CLOCKS_PER_SEC <<" seconds" <<endl;
	//cout<< "Running Time: "<<diff<<" seconds "<<endl;
	
	
	return 0;
}
