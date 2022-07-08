#!/usr/bin/perl  

#............................. GENERAL INSTRUCTION AND SOME INFO OF THIS SCRIPT ..........................................
##    This script is largely taken form a script (named "tree_simulation.pl") of wQMC package
##    which is available at (http://research.haifa.ac.il/~ssagi/software/wQMC.tar.gz).
##    We have also done a lots of modifications on this script.
##
##    Another script named "getFpFn.py" is used here to calculate Robinson-Foulds distance.
##
##    In order to use this script, at first, one has to 
##    1. Install two packages which are in newick_modified-1.3.1_Bayzid.zip and spruce-1.0.tar.gz
##    2. Intall time package using command "sudo apt install time"
##    3. Change all the directories or path address of this script.
##	  4. COMMAND: perl reproduce-result-dataset-I.pl --ntaxa n --experiment r --corr c --qnum_factor
##	  	 where,
##	  	 n = number of taxa in the simulation,
##	  	 q = factor number of qrts related to ntaxa,
##	  	 c = percentage of input quartet that are consistent with model tree,
##	  	 r = replicate number.
#.........................................................................................................................

use strict;
use warnings;
use Getopt::Long;
use Time::HiRes qw[gettimeofday tv_interval];
use File::stat;
use Cwd;

my $ntaxa;
my $cmd;
my $now;
my $help;
my $corr=100;
my $w_pcorrect;
my $experiment="1";
my $qnum_factor = 2;
my ($QFM_FI_time, $QFM_FI_RF_dist_nSim, $QFM_FI_memory);
my ($QFM_F_time, $QFM_F_RF_dist_nSim, $QFM_F_memory);
my ($wMXC_time, $wMXC_RF_dist_nSim, $wMXC_memory);
my ($MXC_time, $MXC_RF_dist_nSim, $MXC_memory);




GetOptions( "ntaxa:s" => \$ntaxa,
	    "qnum_factor:s" => \$qnum_factor,
	    "experiment:s" => \$experiment,
	    "corr:s" => \$corr,
    	    "help" => \$help);
    	    

if ($help ) {
    &help;
}


if (! defined $ntaxa){
    &help;
} 


#.......................EDIT PATH................................
my $input_file_dir = "/home/user/research/wqfmProject/datasets/myData20Replicate";
#my $input_file_dir = "/home/user/research/wqfmProject/script/dataGenerationSimulation/200to400";
my $output_file_dir = "output";
my $model_tree_fname =  "modelTrees/nw_model-tree-n${ntaxa}.dat";
my $statistics_fname =  "resultingStatistics/mydataset_memory_simulation_22feb2022.txt";
my $max_cut_dir = "/home/user/research/wqfmProject/qmc";
#.................................................................

#----------------------QUARTET FILE-------------------------------
my $MXC_wquartet = "$input_file_dir/r${experiment}_qrt_n${ntaxa}_c${corr}_qf${qnum_factor}.wqrt";
my $newick_wquartet = "newick_wq/r${experiment}_qrt_n${ntaxa}_c${corr}_qf${qnum_factor}_newick.wqrt";

#cmd("java -jar /home/user/research/wqfmProject/script/WQMC_to_newick_format.jar $MXC_wquartet $newick_wquartet" );
#.....................................................................


#.................... RUN FUNCTIONS...............................
run_qfmFI();
run_qfmF();
run_weighted_qmc();
run_qmc();
simulation_summary ();
#.................................................................
sub run_qfmFI {

print "Running QFM-FI step ...\n";


#
#                QFM-FI
#
#


QFM:


my $QFMFItree_fname = "$output_file_dir/r${experiment}_qfmFI_output_t${ntaxa}_q${qnum_factor}_${corr}_22feb2022.txt";
$now = [gettimeofday()];#time;

cmd("/usr/bin/time -o time.log -v java -Xmx6500M -jar /home/user/research/wqfmProject/qfm/qfm_v2.jar $MXC_wquartet $QFMFItree_fname > QFM_FI.log" );

$QFM_FI_time = tv_interval($now)*1000;#time - $now;
$QFM_FI_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $QFMFItree_fname);
my $memory_line = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line;
my @fields = split / /, $memory_line;
print "$fields[5]";
$QFM_FI_memory = $fields[5];


rmfile("time.log");
rmfile("QFM_FI.log");



} # end QFM-FI

#...........................................................................................
sub run_qfmF {

print "Running QFM-F step ...\n";


#
#                QFM-F
#
#


QFMF:


my $QFMFtree_fname = "$output_file_dir/r${experiment}_qfmF_output_t${ntaxa}_q${qnum_factor}_${corr}_22feb2022.txt";
$now = [gettimeofday()];#time;

cmd("/usr/bin/time -o time.log -v java -Xmx6500M -jar /home/user/research/wqfmProject/qfm/qfm_v1.jar $MXC_wquartet $QFMFtree_fname > QFM_F.log" );

$QFM_F_time = tv_interval($now)*1000;#time - $now;
$QFM_F_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $QFMFtree_fname);
my $memory_line2 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line2;
my @fields2 = split / /, $memory_line2;
print "$fields2[5]";
$QFM_F_memory = $fields2[5];


rmfile("time.log");
rmfile("QFM_F.log");



} # end QFM-F
#...........................................................................................
sub run_weighted_qmc {

print "Running wQMC ...\n";


#
#             WEIGHTED QMC V3
#
#


wMXC:
my $wMXCtree_fname = "outputs/r${experiment}_wqmc_output_t${ntaxa}_q${qnum_factor}_${corr}_22feb22.txt";
$now =[gettimeofday()];#time;


cmd("/usr/bin/time -o time.log -v $max_cut_dir/max-cut-tree qrtt=$MXC_wquartet otre=$wMXCtree_fname weights=on > WQMCN.log" );
$wMXC_time = tv_interval($now)*1000;#time - $now;
$wMXC_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $wMXCtree_fname);#newick_modified
my $memory_line3 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line3;
my @fields3 = split / /, $memory_line3;
print "$fields3[5]";
$wMXC_memory = $fields3[5];

rmfile("time.log");
rmfile("WQMCN.log");


}
#..........................................................................
sub run_qmc {

print "Running QMC ...\n";


#
#             QMC V3
#
#


wMXC:
my $MXCtree_fname = "outputs/r${experiment}_qmc_output_t${ntaxa}_q${qnum_factor}_${corr}_22feb2022.txt";
$now =[gettimeofday()];#time;

cmd("/usr/bin/time -o time.log -v $max_cut_dir/max-cut-tree qrtt=$MXC_wquartet otre=$MXCtree_fname weights=off > QMCN.log" );
$MXC_time = tv_interval($now)*1000;#time - $now;
$MXC_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $MXCtree_fname);#newick_modified
my $memory_line4 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line4;
my @fields4 = split / /, $memory_line4;
print "$fields4[5]";
$MXC_memory = $fields4[5];

rmfile("time.log");
rmfile("QMCN.log");

}

#..........................................................................
sub simulation_summary {
print "Writing simulation_summary ...\n";
#
#             W R I T I N G   S I M U L A T I O N  S U M M A R Y
#
#
print "ntaxa=$ntaxa  qnum_factor=$qnum_factor correct=$corr \n\n";

#my $stime = sprintf("%d:%d:%d",localtime->hour(),localtime->min(), localtime->sec());
#my $stime1 = substr($stime."    ",0,9);
  print "iteration=$experiment n=$ntaxa corr=$corr qnumf=$qnum_factor\n ";

if (check_exists( $statistics_fname))
  {
    open (RESULT_TABLE,">>$statistics_fname");
  }
else
  {
    open (RESULT_TABLE,">$statistics_fname");
	print RESULT_TABLE "Here the unit of time is milisecond and that of memory is kbytes.   \n\n";	
    print RESULT_TABLE "replicateN0 ntaxa pcorrect qnumf";
    printf RESULT_TABLE  " wqmc_time wqmc_RF_dist_nSim wqmc_memory";
	printf RESULT_TABLE  " qmc_time qmc_RF_dist_nSim qmc_memory";
	printf RESULT_TABLE  " qfmF_time qfmF_RF_dist_nSim qfmF_memory";
	printf RESULT_TABLE  " qfmFI_time qfmFI_RF_dist_nSim qfmFI_memory";
    print RESULT_TABLE  "\n";
  }

printf RESULT_TABLE "%d %d %d %.1f",$experiment,$ntaxa,$corr,$qnum_factor;

printf RESULT_TABLE  " %.2f %.5f %d",
$wMXC_time,$wMXC_RF_dist_nSim,$wMXC_memory;

printf RESULT_TABLE  " %.2f %.5f %d",
$MXC_time,$MXC_RF_dist_nSim,$MXC_memory;

printf RESULT_TABLE  " %.2f %.5f %d",
$QFM_F_time,$QFM_F_RF_dist_nSim,$QFM_F_memory;

printf RESULT_TABLE  " %.2f %.5f %d",
$QFM_FI_time,$QFM_FI_RF_dist_nSim,$QFM_FI_memory;

print RESULT_TABLE  "\n";

close RESULT_TABLE;   

} # end of simulation_summary

#............................................................................
sub obtain_Real_RF_dist{
  my ($true_tree, $estimate_tree) = @_;
  

  rmfile("rf.txt");
  $cmd = "python3 getFpFn.py -t $true_tree -e $estimate_tree > rf1.txt";  
  cmd($cmd);
  open (IN1,"rf1.txt") or die "rf1.txt  does not exist";
  my $res_line = <IN1>;
  #close IN1;
  print "rf output: $res_line\n";
  #my $RFdist =  $1;
  #print "rfdist=$RFdist\n";
  return $res_line;
}
#............................................................................

sub rmfile{
  if (check_exists($_[0])) {
    `rm $_[0]`;
  }
}
#..............................................................................

sub check_exists{
  my $file = $_[0];
# print "checkin exists of $_[0]\n";
  my $exists = -e $file;
  if ($exists){
    print "file $file  $exists\n";
  }
  return $exists;

}
#.................................................................................

sub cmd{
  my $cmd = $_[0];
  #printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  print "UNIX COMMAND: $cmd\n";
  `$cmd`;
  #printf "After command, rc is $?. time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  if ($? != 0 ){
      print "rc $? after system call\n";
      exit;
  }
}
#.................................................................................
sub help{
    print "USAGE:\n\n";
    print "reproduce-result-dataset-I.pl --ntaxa n --experiment r --corr c --qnum_factor q \n\n";   
    print "where\n";
    print "\t n = number of taxa in the simulation\n";
    print "\t q = factor number of qrts related to ntaxa\n";
    print "\t c = percentage of input quartet that are consistent with model tree \n"; 
    print "\t r = replicate number\n"; 

    exit;

}


















