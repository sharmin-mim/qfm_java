#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address of this script. 
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3.	We have downloaded "avian", "angiosperm" and "aminota-aa" biological dataset from
#		https://drive.google.com/drive/folders/1BpKjYZggAiaeaH72zlS8u14Ou3M-N567
#		"plant" and "Mammal mtDNA" biological datasets are available at
#		https://drive.google.com/drive/folders/18spjLcCqLDN6vY1UVUkUtvpxBmq0E6TJ
#	4. 	Command: perl biological-simulation.pl 
#............................................END OF GENERAL INSTRUCTION .......................................

use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;


my @folderNamesBio=("mammal-mtDNA/fastTrees/tCoffee", "mammal-mtDNA/fastTrees/pasta", "mammal-mtDNA/fastTrees/fsa", "avian", "angiosperm", "aminota-aa", "plant/quarets-induced-from-gene-tree");
my $folderName;
my $command;

my $input_file_dir = "/home/user/research/wqfmProject/datasets/WQFM-2020-Datasets/Datasets-Biological/plant/quarets-induced-from-gene-tree";
$command = "./quartet-controller.sh $input_file_dir/all_gt.tre $input_file_dir/weighted_quartets";
system $command;

foreach $folderName (@folderNamesBio)
{
	my $command = "perl /home/user/research/wqfmProject/script/all_method_simulation_bio.pl --folderName $folderName";
    print "$command\n";
    system $command;
  
}

#we have done another experiment on plant dataset using PAUP*+SVDquartet method
$folderName = "plant/quartets-induced-using-svdquartet";
$command = "perl /home/user/research/wqfmProject/script/bio_plant_svd.pl --folderName $folderName";
system $command;


