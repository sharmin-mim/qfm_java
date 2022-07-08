#!/usr/bin/perl
print "\n\n\nin modify_edge_lengths ...\n\n\n";
use strict;
use warnings;
use Getopt::Long;
use Time::localtime;
use File::stat;


if (@ARGV != 1){
    die "must supply a tree\n\n";
}

open (FTREE, "$ARGV[0]") or die "tree $ARGV[0] not exists\n";
my $tree = <FTREE>;

print "\n\ntree $tree\n\n";
my $orig_len = length($tree);

for (my $i =0; $i < length($tree);){
    my $pref = substr($tree,0,$i);
    my $suff = substr($tree,$i);
    print "\ni: $i. char at $i:". substr($tree,$i,1). "\n";
    print "pref: $pref\n";
    print "suff: $suff\n";
    if ($suff =~ /:([0-9]+.[0-9]+)[,)]/){
	my $len = $1;
	my $place = length($`);
	print "found length $len at place $place\n ";
	my $rand = rand(10);
	$rand -= 5;
	$rand /= 20;
	my $new_len = (1 + $rand) * ( $len/6);
	my $new_len_c = substr ($new_len,0,length($len));
	print "rand $rand, new len $new_len,  new len_c  $new_len_c\n";
	$i += $place + length($len) +1;
	$tree =~ s/$len/$new_len_c/;
	print "new tree $tree\n";
	if (length($tree) != $orig_len){
	    print "ERROR ERROR. length chaged from $orig_len to ".length($tree)."\n\n";
	    exit;
	}
    }
    else {last;}
}

print "\n\nfinal tree $tree\n\n";
my $outtree;
if ($ARGV[0] =~ /\./){
    $outtree = $` . "-non-MC." . $';   #'
}
else {
    $outtree = $ARGV[0] . "-non-MC.dat";
}
print "out file $outtree\n";

open (FOUT,">$outtree");
print FOUT $tree ;
close FOUT;
