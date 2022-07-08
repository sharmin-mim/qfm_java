#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address of this script. 
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3. 	There is a file named "data.properties". Keep that file in the present working directory. 
#	4.	Plant dataset is available at
#		https://drive.google.com/drive/folders/1lox-8M4oaDh8mLnvx_Yylu1pLel2nTD3
#	5. 	Command: perl bio_plant_svd.pl --folderName f
#		Here, f = folderName
#............................................END OF GENERAL INSTRUCTION .......................................

use strict;
use warnings;
use Getopt::Long;
use Time::HiRes qw[gettimeofday tv_interval];
use File::stat;
use Cwd;

my $qmc_dir = "/home/user/research/wqfmProject/qmc";
my $orig_qmc_dir = "/home/user/research/wqfmProject/qmc/max-cut-tree";
my $simulation_src_dir = "/home/user/research/wqfmProject/qmc";
my $paup_dir="/home/user/software/packages/paup4a166_ubuntu64";


my $folderName;
my $seq_len;
my $command;
my $now;
my $usage_and_help;
my $corr=100;
my $w_pcorrect;
my $experiment="1";
my $weights_source = "matrix";
my $qnum_factor = 2;


my ($QFM_F_RF, $QFM_F_memory,$QFM_F_time,$QFM_F_RF_dist_nSim);
my ($QFM_FI_RF, $QFM_FI_memory,$QFM_FI_time,$QFM_FI_RF_dist_nSim);
my ($wQFM_RF, $wQFM_memory,$wQFM_time,$wQFM_RF_dist_nSim);
my ($QMC_RF, $QMC_memory,$QMC_time, $QMC_RF_dist_nSim);
my ($wQMC_RF, $wQMC_memory,$wQMC_time, $wQMC_RF_dist_nSim);
my ($astral_RF, $astral_memory,$astral_time, $astral_RF_dist_nSim);


my $model_tree;
my $model_tree_fname;
my $non_weighted_model_tree_fname;

my $i;
my $nTaxa;


GetOptions( "nTaxa:s" => \$nTaxa,
	    "folderName:s" => \$folderName,
    	"usage_and_help" => \$usage_and_help);


if ($usage_and_help ) {
    &usage_and_help;
}


if (! defined $folderName){
    &usage_and_help;
} 







####################### Input Files For QFM, wQFM, ASTRAL and wQMC #####################
my $parents_input_dir = "/home/user/research/wqfmProject/datasets/WQFM-2020-Datasets/Datasets-Biological/$folderName";
my $input_file_dir = $parents_input_dir;
my $output_file_dir = $parents_input_dir;

#model tree is the true tree. For biological dataset, there is no true tree
#$model_tree_fname =  "$parents_input_dir/true_tree_trimmed";
#command("sed -i 's/O/0/g' $model_tree_fname");
#qfm and wqfm input
my $newick_quartet = "$input_file_dir/weighted_quartets";
#input gene tree for astral
my $input_gene_tree = "$input_file_dir/all_gt.tre";
#preparing newick quartet for QMC input
my $QMC_input= "unweightedQuartets.txt";
#preparing newick quartet for wQMC input
my $wQMC_input= "wqmcQuartet.txt";
#command("java -jar convertForWQMC_BufferedReader.jar $newick_quartet $wQMC_input" );



################################### PAUP*+SVDquartet ##########################
command("$paup_dir $input_directory/command.nex -L $input_directory/SVD.log");
my $qfm_time_line = `grep "Time used for QFM" $input_directory/SVD.log`;
my $SVD_QFM_time;

my @fields = split / /, $qfm_time_line;
if( $fields[8] eq  "sec" ) {
   $SVD_QFM_time = ($fields[7]*1000);
} elsif( $fields[8] eq  "(CPU") {
   my ($h, $m, $s) = split /:/, $fields[7];
   $SVD_QFM_time = ((($h*3600)+($m*60)+$s)*1000);
} else {
   $SVD_QFM_time = (($fields[7]*60)*1000);
}
#..............................................................................


my $non_weighted_qmc_quartet = "unweightedQuartets.txt";
command("cp $input_directory/data.properties data.properties");
#########################################################################################



####################### OUTPUT Files For QFM, wQFM, ASTRAL and wQMC #####################
#my $QFMtree_fname = "$output_file_dir/QFM-16-SEP.tre";
#my $QFM_PAUP_tree_fname = "$output_file_dir/QFM-PAUP-20-FEB-2022.tre";
my $QFM_F_tree_fname = "$output_file_dir/QFM-F-20-FEB-2022.tre";
my $QFM_FI_tree_fname = "$output_file_dir/QFM-FI-20-FEB-2022.tre";
my $wQFMtree_fname = "$output_file_dir/wQFM-20-FEB-2022.tre";
my $wQMCtree_fname = "$output_file_dir/wQMC-20-FEB-2022.tre";
my $QMCtree_fname = "$output_file_dir/QMC-20-FEB-2022.tre";
my $ASTRALtree_fname = "$output_file_dir/astral5.7.3-20-FEB-2022.tre";


run_qfm_f();
run_qfm_fi();
#run_wqfm();
#run_astral();
run_max_cut_v3();
#run_wmax_cut_v3();

summarize_simulation_result();
#    }

####################################################################


#######################################################################





#######################################################################
#######################################################################
sub run_qfm_f {

print "Executing QFM-F step ...\n";


#
#                QFM-F   S T E P (RE-COUNTING AND RE-SORTING IS NOT DONE AFTER EACH DIVIDE STEP) 
#
#


QFM_F:

my $QFM_F_tree_fname_temp = "qfm_f_temp_output.txt";
$now = [gettimeofday()];#time;
command("/usr/bin/time -o time.log -v java -jar /home/user/research/wqfmProject/qfm/QFM-F.jar $non_weighted_qmc_quartet $QFM_F_tree_fname_temp > $output_file_dir/QFM_F.log" );

$QFM_F_time = tv_interval($now)*1000;#time - $now;
printf "time = %d",$QFM_F_time;
command("java -jar convertBack.jar $QFM_F_tree_fname_temp $QFM_F_tree_fname" );
remove_file($QFM_F_tree_fname_temp);
#$QFM_F_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $QFM_F_tree_fname);
my $memory_line = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line;
my @fields = split / /, $memory_line;
print "$fields[5]\n";
$QFM_F_memory = $fields[5];
print "$QFM_F_memory\n";

remove_file("time.log");
#remove_file("wQFM.log");
#remove_file($newick_wquartet);

} # end QFM


#######################################################################
#######################################################################
sub run_qfm_fi {

print "Executing QFM-FI step ...\n";


#
#                QFM-FI   S T E P (VERSION 2, RE-COUNTING AND RE-SORTING IS DONE AFTER EACH DIVIDE STEP) 
#
#


QFM_FI:

my $QFM_FI_tree_fname_temp = "qfm_fi_temp_output.txt";
$now = [gettimeofday()];#time;
command("/usr/bin/time -o time.log -v java -jar /home/user/research/wqfmProject/qfm/QFM-FI.jar $non_weighted_qmc_quartet $QFM_FI_tree_fname_temp > $output_file_dir/QFM_FI.log");

$QFM_FI_time = tv_interval($now)*1000;#time - $now;
command("java -jar convertBack.jar $QFM_FI_tree_fname_temp $QFM_FI_tree_fname");
remove_file($QFM_FI_tree_fname_temp);
#$QFM_FI_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $QFM_FI_tree_fname);
my $memory_line2 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line2;
my @fields2 = split / /, $memory_line2;
print "$fields2[5]\n";
$QFM_FI_memory = $fields2[5];
print "$QFM_FI_memory\n";

remove_file("time.log");


} # end QFM


#######################################################################
#######################################################################
sub run_wqfm {

print "Running wqfm step ...\n";


#
#                WEIGHTED QFM   S T E P 
#
#


wQFM:

$now = [gettimeofday()];#time;
command("/usr/bin/time -o time.log -v java -Xmx6500M -jar /home/user/research/wqfmProject/wQFM2/wQFM-v1.3.jar -i $newick_quartet -o $wQFMtree_fname > $output_file_dir/wQFM-22-feb.log" );
#command("java -jar /home/user/research/wqfmProject/wQFM/wQFM.jar $newick_quartet $wQFMtree_fname > wQFM.log" );
$wQFM_time = tv_interval($now)*1000;#time - $now;
#$wQFM_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $wQFMtree_fname);

my $memory_line3 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line3;
my @fields3 = split / /, $memory_line3;
print "$fields3[5]\n";
$wQFM_memory = $fields3[5];
print "$wQFM_memory\n";
remove_file("time.log");

} # end weighted qfm


#######################################################################
#######################################################################
sub run_astral {

print "Running AASTRAL step ...\n";


#
#                ASTRAL   S T E P 
#
#


ASTRAL:


$now = [gettimeofday()];#time;

command("/usr/bin/time -o time.log -v java -jar astral.5.7.3.jar -i $input_gene_tree -o $ASTRALtree_fname 2> $output_file_dir/astral.log" );
$astral_time = tv_interval($now)*1000;#time - $now;
#$astral_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $ASTRALtree_fname);

my $memory_line4 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line4;
my @fields4 = split / /, $memory_line4;
print "$fields4[5]\n";
$astral_memory = $fields4[5];
print "$astral_memory\n";
remove_file("time.log");

} # end ASTRAL



#######################################################################
sub run_max_cut_v3 {

print "Executing QMC V3 step ...\n";


#
#             QMC V3
#
#


QMC:


my $QMCtree_fname_temp = "qmc_temp_output.txt";

$now = [gettimeofday()];#time;

command("/usr/bin/time -o time.log -v $qmc_dir/max-cut-tree qrtt=$non_weighted_qmc_quartet weights=off otre=$QMCtree_fname_temp  > $output_file_dir/QMCN.log" );
$QMC_time = tv_interval($now)*1000;#time - $now;
command("java -jar convertBack.jar $QMCtree_fname_temp $QMCtree_fname" );
remove_file($QMCtree_fname_temp);
remove_file($non_weighted_qmc_quartet);
#$QMC_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $QMCtree_fname);
my $memory_line5 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line5;
my @fields5 = split / /, $memory_line5;
print "$fields5[5]\n";
$QMC_memory = $fields5[5];
print "$QMC_memory\n";
remove_file("time.log");
} # end  max cut

#..................................................................
sub run_wmax_cut_v3 {

print "Executing wQMC step ...\n";


#
#             wQMC
#
#


wQMC:


my $wQMCtree_fname_temp = "qmc_temp_output.txt";
#preparing newick quartet for wQMC input
my $wQMC_input= "wqmcQuartet.txt";
command("java -jar convertForWQMC_BufferedReader.jar $newick_quartet $wQMC_input" );
$now = [gettimeofday()];#time;

command("/usr/bin/time -o time.log -v $qmc_dir/max-cut-tree qrtt=$wQMC_input weights=on otre=$wQMCtree_fname_temp  > $output_file_dir/wQMCN.log" );
$wQMC_time = tv_interval($now)*1000;#time - $now;
command("java -jar convertBack.jar $wQMCtree_fname_temp $wQMCtree_fname" );
remove_file($wQMCtree_fname_temp);
remove_file($wQMC_input);
#$wQMC_RF_dist_nSim =obtain_Real_RF_dist($model_tree_fname, $wQMCtree_fname);
my $memory_line6 = `grep "Maximum resident set size (kbytes):" time.log`;
print $memory_line6;
my @fields6 = split / /, $memory_line6;
print "$fields6[5]\n";
$wQMC_memory = $fields6[5];
print "$wQMC_memory\n";
remove_file("time.log");
} # end weighted max cut


######################################################################
sub summarize_simulation_result {

print "Writing the summary of simulation ...\n";
#
#             SUMMARIZING RESULT 
#
#
print "folderName=$folderName  \n\n";

#my $stat_table_fname =  "/home/user/research/wqfmProject/table/wQFM/Taxa${nTaxa}simulated_qmc_qfm_8dec.txt";
#my $stat_table_fname =  "/home/user/research/wqfmProject/table/wQFM/Taxa${nTaxa}simulated_qmc_qfm_1sept_2021.txt";
my $stat_table_fname =  "/home/user/research/wqfmProject/table/wQFM/only_svdPAUP_statistics_plant_dataset.txt";
if (check_existance_of_file( $stat_table_fname))
  {
    open (RESULT_TABLE,">>$stat_table_fname");
  }
else
  {
    open (RESULT_TABLE,">$stat_table_fname");
	print RESULT_TABLE "Time is in milisecond and memory is in Kbytes\n\n";
    print RESULT_TABLE "replicateN0 folderName";
    printf RESULT_TABLE  " QMCtime QMC_memory";
    printf RESULT_TABLE  " QFM_F_time QFM_F_memory";
    printf RESULT_TABLE  " QFM_FI_time QFM_FI_memory";
	printf RESULT_TABLE  " QFM_PAUP_time";


    print RESULT_TABLE  "\n";
  }

printf RESULT_TABLE "%d %s",$experiment,$folderName;


printf RESULT_TABLE  " %.2f %d",
$QMC_time, $QMC_memory;

printf RESULT_TABLE  " %.2f %d",
$QFM_F_time, $QFM_F_memory;

printf RESULT_TABLE  " %.2f %d",
$QFM_FI_time, $QFM_FI_memory;

printf RESULT_TABLE  " %.2f",
$SVD_QFM_time;

print RESULT_TABLE  "\n";

close RESULT_TABLE;   

} # end summarize_simulation_result

#
#
#       ########################  SUB ROUTINES ############################################## 
#
#


#-------------------------------------------------------------------------------
    

sub obtain_Real_RF_dist{
  my ($true_tree, $estimate_tree) = @_;
  

  remove_file("rf.txt");
  $command = "python3 /home/user/research/wqfmProject/script/getFpFn.py -t $true_tree -e $estimate_tree > rf.txt";  
  command($command);
  open (IN1,"rf.txt") or die "rf.txt  does not exist";
  my $res_line = <IN1>;
  #close IN1;
  print "rf output: $res_line\n";
  #my $RFdist =  $1;
  #print "rfdist=$RFdist\n";
  return $res_line;
}
#------------------------------------------------------------------------------

sub remove_file{
  if (check_existance_of_file($_[0])) {
    `rm $_[0]`;
  }
}

sub check_existance_of_file{
  my $file = $_[0];
# print "checkin exists of $_[0]\n";
  my $exists = -e $file;
  if ($exists){
    print "file $file  $exists\n";
  }
  return $exists;

}

sub command{
  my $command = $_[0];
  #printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  print "UNIX COMMAND: $command\n";
  `$command`;
  #printf "After command, rc is $?. time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  if ($? != 0 ){
      print "rc $? after system call\n";
      exit;
  }
}



sub usage_and_help{
    print "USAGE:\n\n";
    print "bio_plant_svd.pl --nTaxa n --folderName f --experiment r \n\n";   
    print "where\n";
    print "\tn = number of taxa in the simulation\n";
    print "\tf = folderName\n";
    print "\tr = replicate number\n"; 
 

    exit;

}

