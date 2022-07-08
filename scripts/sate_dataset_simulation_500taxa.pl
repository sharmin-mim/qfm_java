#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address. 
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3. 	sate_dataset_simulation_500taxa.pl script is for simulating 500 taxa.
#	4.	Download SATe dataset from
#		https://drive.google.com/file/d/0B0lcoFFOYQf8bEpVbHFDZFZjcTg/view?resourcekey=0-GTEuC69U2TLFk4dejtJoww
#		Unzip Dataset:
#		tar -xvf sate_datasets_final_combined.tar.bz2 --directory dataset/
#	5. 	Command: perl sate_dataset_simulation_500taxa.pl -folder_name name  -rep_no r -n_quartets
#		where,
#		name = folder name in the simulation (provide file name with full directory address),
#		r = replicate number in the simulation,
#		n = number of quartets in the simulation.
#............................................END OF GENERAL INSTRUCTION .......................................

use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;
use Cwd;

my $rep_no;
my $folder_name;
my $usage_and_help;
my $now;
my ($QFM_F_RF, $QFM_F_time);
my ($QFM_FI_RF, $QFM_FI_time);
#my ($QFM_v3_RF, $QFM_v3_time);
my ($MXC_RF, $MXC_time);
my ($SVD_QFM_RF, $SVD_QFM_time);

GetOptions( "folder_name:s" => \$folder_name,
    	    "rep_no:s" => \$rep_no,
    	    "usage_and_help" => \$usage_and_help);


if ($usage_and_help ) {
    &usage_and_help;
}


if (! defined $rep_no){
    &usage_and_help;
} 

my $input_directory = "$folder_name/R${rep_no}";
my $script_directory = "/home/user/research/wqfmProject/script";
my $qfm_directory = "/home/user/research/wqfmProject/qfm";
my $qmc_directory = "/home/user/research/wqfmProject/qmc";
my $translator =  "$input_directory/svd_translate.tre";
my $svd_qfm_newick_tree = "$input_directory/svd_qfm_newick.tre";
my $quartet_file = "$input_directory/quartets.qrt";
my $fasta_file = "$input_directory/rose.aln.true.fasta";
my $nexus_file = "$input_directory/rose.aln.true.nex";
my $qmc_tree = "$input_directory/qmc.tre";
my $qfm_f_tree = "$input_directory/qfm_f.tre";
my $qfm_fi_tree = "$input_directory/qfm_fi.tre";
#my $qfm_v3_tree = "$input_directory/qfm_v3.tre";
my $qmc_tree_trans = "$input_directory/qmc_translated.tre";
my $qfm_f_tree_trans = "$input_directory/qfm_f_translated.tre";
my $qfm_fi_tree_trans = "$input_directory/qfm_fi_translated.tre";
#my $qfm_v3_tree_trans = "$input_directory/qfm_v3_translated.tre";
my $true_tree = "$input_directory/rose.tt";
my $statistics_table =  "/home/user/research/wqfmProject/table/SATE_statistics.txt";



############# FASTA TO NEXUS FILE CONVERSION ##############################
command("python $script_directory/fasta_to_nexus.py $fasta_file $nexus_file");

################## RUNNING SIMULATION #####################################
generate_svdQuartet_QFMtree();
run_qfm_F();
run_qfm_FI();
#run_qfm_v3();
run_qmc();
summarize_simulation_result();
######################### SVD QUARTETS AND TREE GENERATION #################
sub generate_svdQuartet_QFMtree {
open (COMF,">$input_directory/command.nex");
print COMF "#NEXUS\n".
           "BEGIN PAUP;\n".
           "\tEXECUTE $nexus_file;\n".
		   "\tsvdQuartets evalQuartets = random nquartets = $n_quartets qfile=$quartet_file seed=1234 treeInf=QFM;\n".
           "\tSaveTrees file=$translator;\n".
           "\tSaveTrees file= $svd_qfm_newick_tree format = newick;\n".
           "\tQUIT;\n".
           "END;";
close COMF; 
command("$script_directory/paup4a168_ubuntu64 $input_directory/command.nex -L $input_directory/SVD.log");

my $qfm_time_line = `grep "Time used for QFM" $input_directory/SVD.log`;

my @fields = split / /, $qfm_time_line;
if( $fields[8] eq  "sec" ) {
   $SVD_QFM_time = $fields[7];
} elsif( $fields[8] eq  "(CPU") {
   my ($h, $m, $s) = split /:/, $fields[7];
   $SVD_QFM_time = ($h*3600)+($m*60)+$s;
} else {
   $SVD_QFM_time = ($fields[7]*60);
}

$SVD_QFM_RF = obtain_RF_dist_newickModified($true_tree, $svd_qfm_newick_tree);


}
######################### QFM TREE GENERATION #############################
sub run_qfm_F {

print "Executing QFM-F step ...\n";


#
#                QFM-F   S T E P (RE-COUNTING AND RE-SORTING IS NOT DONE AFTER EACH DIVIDE STEP) 
#
#


QFM_F:
$now = time;
printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
 $now = time;
command("java -Xmx6144M -jar $qfm_directory/QFM-F.jar $quartet_file $qfm_f_tree 3 > $input_directory/QFM-F.log");
$QFM_F_time = time - $now;
command("java -jar $script_directory/convertBack.jar $qfm_f_tree $qfm_f_tree_trans");
$QFM_F_RF = obtain_RF_dist_newickModified($true_tree, $qfm_f_tree_trans);


} # end
sub run_qfm_FI {

print "Executing QFM-FI step ...\n";


#
#                QFM-FI   S T E P (VERSION 2, RE-COUNTING AND RE-SORTING IS DONE AFTER EACH DIVIDE STEP) 
#
#


QFM_FI:
$now = time;
printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
 $now = time;
command("java -Xmx6144M -jar $qfm_directory/QFM-FI.jar $quartet_file $qfm_fi_tree 3 > $input_directory/QFM-FI.log");
$QFM_FI_time = time - $now;
command("java -jar $script_directory/convertBack.jar $qfm_fi_tree $qfm_fi_tree_trans");
$QFM_FI_RF =obtain_RF_dist_newickModified($true_tree, $qfm_fi_tree_trans);


} # end

######################### QMC TREE GENERATION #############################
sub run_qmc {

print "Executing QMC V3 step ...\n";


#
#             QMC V3
#
#


QMC:

$now = time;
printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
$now = time;
command("$qmc_directory/max-cut-tree qrtt=$quartet_file otre=$qmc_tree weights=off > $input_directory/QMCN.log" );
$MXC_time = time - $now;
command("java -jar $script_directory/convertBack.jar $qmc_tree $qmc_tree_trans");
$MXC_RF =obtain_RF_dist_newickModified($true_tree, $qmc_tree_trans);


} # end 

#################################################################################

####################### WRITING STATISTICS ######################################
sub summarize_simulation_result {
print "Writing the summary of simulation ...\n";
#
#             SUMMARIZING SIMULATION RESULT 
#
#


if (check_existance_of_file($statistics_table))
  {
    open (RESULT_TABLE,">>$statistics_table");
  }
else
  {
    open (RESULT_TABLE,">$statistics_table");
	print RESULT_TABLE "Here the unit of time is second.   \n\n";
    print RESULT_TABLE "replicateN0 folder_name pcorrect qnumf";

    printf RESULT_TABLE  " MXCtime MXC_RF";

    printf RESULT_TABLE  " QFM_v1_time QFM_F_RF";

    printf RESULT_TABLE  " QFM_v2_time QFM_FI_RF";
	
    printf RESULT_TABLE  " SVD_QFM_time SVD_QFM_RF";

    print RESULT_TABLE  "\n";
  }

printf RESULT_TABLE "%d %s",$rep_no,$folder_name;

printf RESULT_TABLE  " %.2f %.5f",
$MXC_time,$MXC_RF;


printf RESULT_TABLE  " %.2f %.5f",
$QFM_F_time,$QFM_F_RF;

printf RESULT_TABLE  " %.2f %.5f",
$QFM_FI_time,$QFM_FI_RF;

printf RESULT_TABLE  " %.2f %.5f",
$SVD_QFM_time,$SVD_QFM_RF;



print RESULT_TABLE  "\n";

close RESULT_TABLE;   

} # end summarize_simulation_result

#################################################################################

######################## SUB ROUTINES ##############################################
#-------------------------------------------------------------------------------
sub obtain_RF_dist_newickModified{
  my ($true_tree, $estimate_tree) = @_;
  my $rf_dist = `$script_directory/getFpFn.py -t $true_tree -e $estimate_tree`;
  return $rf_dist;
}
#------------------------------------------------------------------------------
sub command{
  my $command = $_[0];
  printf "Before running command, time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  print "COMMAND: $command\n";
  `$command`;
  printf "After running command, rc is $?. time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  if ($? != 0 ){
      print "rc $? after system call\n";
      exit;
  }
}

#------------------------------------------------------------------------------

sub check_existance_of_file{
  my $file = $_[0];
  my $existance = -e $file;
  if ($existance){
    print "file $file  $existance\n";
  }
  return $existance;

}
#------------------------------------------------------------------------------
sub remove_file{
  if (check_existance_of_file($_[0])) {
    `rm $_[0]`;
  }
}

#------------------------------------------------------------------------------
sub usage_and_help{
    print "USAGE:\n\n";
    print "sate_dataset_simulation_500taxa.pl -folder_name name  -rep_no r -n_quartets \n\n";   
    print "where\n";
    print "\tname = folder name in the simulation\n";
    print "\tr = replicate number in the simulation\n";
    print "\tn = number of quartets in the simulation\n";	


    exit;

}
