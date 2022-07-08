#!/usr/bin/perl  

#............................. GENERAL INSTRUCTION AND SOME INFO OF THIS SCRIPT ..........................................
##    This script is largely taken form a script (named "tree_simulation.pl") of wQMC package
##    which is available at (http://research.haifa.ac.il/~ssagi/software/wQMC.tar.gz).
##    We have slightly modified the script.
##    Use this script, if model tree (true species tree) is already generated. 
##    In order to use this script, one has to 
##    1. Change all the directories or path address of this script.
##    2. Change the values of @replicate, @correct, @qf according to your need
##	  4. COMMAND: perl data_generator_with_existing_model_tree.pl  -ntaxa n 
##	  	 where,
##	  	 n = number of taxa in the simulation.
#.........................................................................................................................

use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;
use Cwd;

my $max_cut_dir = "/home/user/research/wqfmProject/qmc";
my $orig_max_cut_dir = "/home/user/research/wqfmProject/qmc/max-cut-tree";
my $simulation_src_dir = "/home/user/research/wqfmProject/qmc";
my $local_simulation_src_dir = ".";
my $triplets_src_dir =  "/home/user/research/wqfmProject/qmc";
# my $start_step = "MXC";
my $start_step = "FILTER";
#goto $start_step;
#my $paup_dir="/home/user/research/newP/packages/paup4a166_ubuntu64";

my $ntaxa;
my $seq_len;
my $cmd;
my $now;
my $help;
my $corr;
my $w_pcorrect;
my $experiment="0";
my $weights_source = "matrix";
my $qnum_factor;

my @replicate = (1..20); #number of replicates
my @correct = (70);#, 80, 90, 95, 100); #consistency (percentage of quartet which are consistent with the model tree
my @qf = (1.5, 2, 2.8); # quartet factor. Here number of quartet = (number of taxa)^(qf)  
my $model_tree;

my $model_tree_fname;
my $non_weighted_model_tree_fname;

my $replicateNo;



GetOptions( "ntaxa:s" => \$ntaxa,
    	    "help" => \$help);


if ($help ) {
    &help;
}


if (! defined $ntaxa){
    &help;
} 

# Do I really need this s@%t?
my $testtree = "((1,8),7,(2,(4,3)));";
print "count: ", count_edges($testtree), "\n";
#exit;




#if ((! defined $corr) ){
#   $corr = 100;   # default is to take all quartets
#}


my $tr = ($ntaxa**2);

my $exec_corr = 100;


#my  $orig_qrt_file = "model_tree_${ntaxa}_100_${qnum_factor}";
#my  $qrt_file = "model_tree_${ntaxa}_${corr}_${qnum_factor}";
my  $orig_qrt_file = "";
my  $qrt_file = "";

gen_random_tree();

#clean();

####################################################################

sub gen_random_tree {
print "generating random tree...\n";

#
#             R A N D O M   T R E E   S T E P 
#
#


my $seed = int(rand(10000));
print "$seed\n";


$model_tree_fname = "modelTrees/model-tree-n${ntaxa}.dat";

foreach $replicateNo (@replicate)
{
foreach $corr (@correct)
{
	foreach $qnum_factor (@qf)
	{
	my  $orig_qrt_file = "inputs/r${replicateNo}_orig_qrt_n${ntaxa}_pc${corr}_c100_qf${qnum_factor}";
	my  $qrt_file = "inputs/r${replicateNo}_qrt_n${ntaxa}_c${corr}_qf${qnum_factor}";
	gen_quartets($model_tree_fname,$qrt_file,$ntaxa,$corr,$w_pcorrect,$qnum_factor);

	#gen_quartets($model_tree_fname,$orig_qrt_file,$ntaxa,$exec_corr,0,$qnum_factor);
	}
}
}
#gen_quartets($model_tree_fname,$qrt_file,$ntaxa,$corr,$w_pcorrect);

#gen_quartets($model_tree_fname,$orig_qrt_file,$ntaxa,$exec_corr,0);

#gen_non_weighted_quartets("nw_model_tree",$non_weighted_model_tree_fname,$tr,$corr);
#gen_non_weighted_quartets("nw_model_tree",$non_weighted_model_tree_fname,$tr,$exec_corr);

} # end gen_random_tree

#######################################################################




######################################################################

#
#
#       R O U T I N E S 
#
#


#-------------------------------------------------------------------------------
sub rmfile{
  if (check_exists($_[0])) {
    `rm $_[0]`;
  }
} 

#-------------------------------------------------------------------------------

sub count_edges{
    my $tree = $_[0];
    if ($tree =~ /\(\d+\)/){
	print "ERROR ERROR ERROR. tree contains unison $&\n";
	exit 9;
    }
    if ($tree !~ /\(([0-9,():\.]+)\)/){
	print "ERROR ERROR ERROR.  tree $tree has invalid structure\n";
	exit 9;
    }
    my $bin_root = binary_root($1);
    print "bin root is $bin_root\n";
    my $num_edges = 0;
#  print "computing num edges of tree $1\n";
    for (my  $i =0; $i < length($1); $i++){
	if (substr($tree,$i,1) eq "("){
	    $num_edges++;
#      print "found edge at $i: ". substr($1,$i,1)  ." num edges $num_edges\n";
	}
    }
    $num_edges--; # decrease one for the root

    if ($bin_root == 1){
	print "\n\ntree has binary root\n";
	$num_edges--;
    }
    print "number of edges $num_edges\n";
    return  $num_edges;
}

#-------------------------------------------------------------------------------

sub binary_root{
    my $tree = $_[0];
    my $balance = 0;
    my $count = 0;
    for (my  $i =0; $i < length($tree); $i++){
	if (substr($tree,$i,1) eq "("){
	    $balance++;
#	  print "found (. increasing balance to $balance\n";
	}
	if (substr($tree,$i,1) eq ")"){
	    $balance--;
#	  print "found ). decreasing balance to $balance\n";
	}
	if ($balance < 0){
	    die "$tree : parenthesys are imbalanced\n";
	}
	if (substr($tree,$i,1) eq ","){
#	  print "found \",\" substring \n";
	    if ( $balance eq 0){
		$count++;
#	      print "balance is 0 and count is $count\n";
		if ($count > 1){
		    print "root is not binary: $tree\n";
		    return 0;
		}
	    }
	}
    }
    if ($count < 1){
#      print " no root found for tree $tree at binary root. try to peel one parentheses\n";
	if ($tree =~ /\(([0-9,()]+)\)/){
	    return binary_root($1);
	}
	else {
	    print "ERROR ERROR ERROR. no root found for tree $tree at binary root\n";
	    exit 9;
	}
    }
#    print "root is  binary: $tree\n";
    return 1;
}

#-------------------------------------------------------------------------------




#-------------------------------------------------------------------------------




#-------------------------------------------------------------------------------





sub gen_quartets {
  my ($tree_name,$qrt_file,$ntaxa,$corr,$w_pcorrect,$qnum_factor) = @_;

  # my $command = "$triplets_src_dir/gen-quartets-from-tree-long"." $tree $tr $corr";

# qrt with weights
  my $out_qrt_file = "$qrt_file.wqrt";  

  my $weighted_pcorrect = "";

  if($w_pcorrect)
  {
      $weighted_pcorrect = "w_pcorrect=on";
  }

  my $command = "qmc/tree_quartets tree_file=$tree_name out_qrt_file=$out_qrt_file pcorrect=$corr $weighted_pcorrect select=random qnum_factor=$qnum_factor ntaxa=$ntaxa weights=on";
  cmd($command);


# qrt with no weights

 $command = "sed \"s/:[.0-9]\\+//g\" < $out_qrt_file > $qrt_file.qrt";
  cmd($command);

}




sub check_exists{
  my $file = $_[0];
# print "checkin exists of $_[0]\n";
  my $exists = -e $file;
  if ($exists){
    print "file $file  $exists\n";
  }
  return $exists;

}

sub cmd{
  my $cmd = $_[0];
  printf "time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  print "UNIX COMMAND: $cmd\n";
  `$cmd`;
  printf "After command, rc is $?. time is %d:%d:%d\n",localtime->hour(),localtime->min(), localtime->sec();
  if ($? != 0 ){
      print "rc $? after system call\n";
      exit;
  }
}



sub decrease_names{
    my $ftree = $_[0];
    print "in decrease names. file $ftree\n";
    open (IN, "$ftree") or die " could not open random-tree-non-MC.dat";
    my $new_tree = <IN>;
    while (1){
	if ($new_tree =~ /t([0-9]+)/){
	    my $sp_1 = $1;
	    if ($sp_1 < 1){
		print "ERROR ERROR. found a aspecies with number < 1: $sp_1\n";
		exit;
	    }
	    $sp_1--;
#	    print "found a species $& at ". length($`) . ". new number $sp_1\n";
	    $new_tree = $` . $sp_1 . $'; #'
#            print "tree after: $new_tree\n";
         }
       else {
        last;
        }
    }
#
  return $new_tree;
}

sub write_out_tree{
  my ($tree_out_file,$MRP_out_file)= @_;
  my $tree;
  print "hi";
  open (MRP_OUT_FILE,"<$MRP_out_file") or die "can't open file  $MRP_out_file for read\n";
  while(<MRP_OUT_FILE>) {
      chomp;
      my $line = $_;
      if ($line =~ /tree MajRule = \[\&U\] (\(.*\);)/) {
	$tree = $1;
	last;
      }
    }
  close MRP_OUT_FILE;

  if($tree){
    open (TREE_OUT_FILE,">$tree_out_file");
    print TREE_OUT_FILE $tree;
    close TREE_OUT_FILE;
  }
}

sub help{
    print "USAGE:\n\n";
    print "perl data_generator_with_existing_model_tree.pl  -ntaxa n \n\n";   
    print "where\n";
    print "\tn = number of taxa in the simulation\n";
 

    exit;

}

sub pad{
  my ($field,$len)=@_;
  return substr($field."",0,$len);
}


sub ERR{
    print "\n\nERROR ERROR ERROR: ",$_[0] , "\n\n";
    exit;
}

sub clean{
    rmfile("$qrt_file.qrt");
    rmfile("$qrt_file.wqrt");
    rmfile("$orig_qrt_file.qrt");
    rmfile("$orig_qrt_file.wqrt");
}
