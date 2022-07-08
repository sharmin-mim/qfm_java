#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address. 
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3. 	sate_dataset_simulation_100taxa.pl script is for simulating 100 taxa
#	 	sate_dataset_simulation_500taxa.pl script is for simulating 500 taxa.
#	 	The main difference of these scripts is 
#			for 100 taxa, all possible SVDquartets are generated and 
#			for 500 taxa, in we generated 36067497 quartets.
#		Change number of quartets (-n_quartets) of line 47 if you want to generate
#		a different number of quartets over 500 taxa.
#		Use sate_dataset_simulation_100taxa.pl script in order to generate all possible SVDquartets.
#	4.	Download SATe dataset from
#		https://drive.google.com/file/d/0B0lcoFFOYQf8bEpVbHFDZFZjcTg/view?resourcekey=0-GTEuC69U2TLFk4dejtJoww
#		Unzip Dataset:
#		tar -xvf sate_datasets_final_combined.tar.bz2 --directory dataset/
#	5. 	Command: perl run_full_SATe_dataset.pl  
#............................................END OF GENERAL INSTRUCTION .......................................

use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;

my $folderName="";
my $folder_directory = "/home/user/research/wqfmProject/datasets/SATe_dataset";
my @all_folder_names=("100L1", "100L2", "100M1", "100M2", "100M3", "100S1", "100S2");
my $command;
my $now;

foreach $folderName (@all_folder_names)
{
for (my $i=0; $i < 20; $i++){
    my $command = "perl /home/user/research/wqfmProject/script/sate_dataset_simulation_100taxa.pl -folder_name $folder_directory/$folderName  -rep_no $i";
    print "$command\n";
    system $command;
  }
}

@all_folder_names=("500L1", "500L2", "500L3", "500L4", "500L5", "500M1", "500M2", "500M3", "500M4", "500M5", "500S1", "500S2", "500S3", "500S4", "500S5");
foreach $folderName (@all_folder_names)
{
for (my $i=0; $i < 20; $i++){
    my $command = "perl /home/user/research/wqfmProject/script/sate_dataset_simulation_500taxa.pl -folder_name $folder_directory/$folderName  -rep_no $i -n_quartets 36067497";
    print "$command\n";
    system $command;
  }
}
