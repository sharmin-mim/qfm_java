#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change all the directories or path address of this script. 
#	2. 	Make sure you have enough memory in your computer because the size
#		of generated quartet files are very large.
#	3.	We have downloaded 37-taxon simulated dataset from
#		https://drive.google.com/drive/folders/1X52MOo2lObw3tiZ6GJSCR86je4nbUrdx
#	4. 	Command: perl 37-taxon-simulation.pl 
#............................................END OF GENERAL INSTRUCTION .......................................

use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;


#my $nTaxa= 37;
my @folderNames37=("noscale.25g.500b", "noscale.50g.500b", "noscale.100g.500b", "noscale.200g.250b", "noscale.200g.500b","noscale.200g.1000b","noscale.200g.1500b","noscale.200g.true","noscale.400g.500b", "noscale.800g.500b", "scale2d.200g.500b", "scale2u.200g.500b");
my $folderName;
my $command;
my $nTaxa= 37;
foreach $folderName (@folderNames37)
{
for (my $i=1; $i <= 20; $i++){
    #my $outName = "log-${i}-${folderName}-${qnum_factor}-${correct}";
	my $command = "perl /home/user/research/wqfmProject/script/all_method_simulation.pl --nTaxa $nTaxa --folderName $folderName --experiment $i";
    print "$command\n";
    system $command;
  }
}


