#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address of this script. Make a directory named "output"
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3.	We have downloaded 100-taxon simulated dataset from
#		https://drive.google.com/drive/folders/1r3nk_t4Lkwi7ZMktWHhXCqirrSnQOCtv
#	4. 	Command: perl 100-taxon-simulation.pl 
#	5. 	We have only generate result for 10 replicates.  
#............................................END OF GENERAL INSTRUCTION .......................................
use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;

my $ntaxa=101;



my $cmd;
my $now;




for (my $i=1; $i <= 10; $i++){
 
    my $cmd = "perl /home/user/research/wqfmProject/script/all_method_simulation_100_taxon.pl --ntaxa $ntaxa --experiment $i";
    print "$cmd\n";
    system $cmd;
  }

