#!/usr/bin/perl  

#.............................................. GENERAL INSTRUCTION ..........................................
#	1. 	Change path or directories of reproduce-result-dataset-I.pl script 
#	2. 	Change @numberOfTaxa, @qf, @consistency according to your need
#	3.	Download simulated Dataset-I from
#		https://drive.google.com/drive/folders/1yUUR8mHz7d20ckUKe7Q326JjouYGNf3p
#	4. 	Command: perl run_20_replicates.pl 
#............................................END OF GENERAL INSTRUCTION .......................................
use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;

my $ntaxa=5;

my $correct=100;

my $qnum_factor=2;

my $cmd;
my $now;

#my $outName = "";


#GetOptions("ntaxa:s" => \$ntaxa,
	   #"qnum_factor:s"  =>  \$qnum_factor, 
	   #"correct:s" => \$correct
#);


my @numberOfTaxa=(1000, 2000);
my @qf=(1.5, 2);
my @consistency=(70, 80, 90);

foreach $correct (@consistency)
{
foreach $ntaxa (@numberOfTaxa)
{
foreach $qnum_factor (@qf)
{
for (my $i=1; $i <= 20; $i++){
    my $cmd = "perl reproduce-result-dataset-I.pl --ntaxa $ntaxa --experiment $i --corr $correct --qnum_factor $qnum_factor";
    print "$cmd\n";
    system $cmd;
  }
}
}
}





