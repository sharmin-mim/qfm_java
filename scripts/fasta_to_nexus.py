#! /usr/bin/env python

#This file is taken from "https://gist.github.com/jeetsukumaran/2225546". We have slightly modified it.

import os
import sys

if ("--help" in sys.argv) or ("-?" in sys.argv):
    sys.stderr.write("usage: fasta-to-nexus.py [<fasta-file-path>] [<nexus-file-path>]\n")
    sys.exit(1)
    
if len(sys.argv) < 2:
    src = sys.stdin
else:
    src_fpath = os.path.expanduser(os.path.expandvars(sys.argv[1]))
    if not os.path.exists(src_fpath):
        sys.stderr.write('Not found: "%s"' % src_fpath)
    src = open(src_fpath)        
        
if len(sys.argv) < 3:
    dest = sys.stdout # os.path.splitext(src_fpath)[0] + ".nex"
else:
    dest_fpath = os.path.expanduser(os.path.expandvars(sys.argv[2]))
    dest = open(dest_fpath, "w")

seqs = {}
cur_seq = None
lines = src.readlines()
for i in lines:
    i = i.replace("\n", "").replace("\r", "")
    if i:
        if i.startswith(">"):
            label = i[1:]
            seqs[label] = []
            cur_seq = seqs[label]
        else:
            if cur_seq is None:
                raise Exception("Sequence data found before label")
            cur_seq.extend(i.replace(" ", ""))
        
taxlabels = seqs.keys()
#taxlabels.sort()

dest.write("#NEXUS\n\n")

dest.write("BEGIN TAXA;\n")
dest.write("\tDIMENSIONS ntax=%d;\n" % len(seqs))
dest.write("\tTAXLABELS ")
for taxlabel in taxlabels:
    dest.write(" %s" % taxlabel)
dest.write(";\n")
dest.write("END;\n\n")

nchar = max([len(s) for s in seqs.values()])
dest.write("BEGIN CHARACTERS;\n")
dest.write("\tDIMENSIONS nchar=%d;\n" % nchar)
dest.write("\tFORMAT datatype=dna missing=? gap=-;\n")
dest.write("\tMATRIX\n")
for taxlabel in taxlabels:
    dest.write("\t%s\t%s\n" % (taxlabel, "".join(seqs[taxlabel])))
dest.write("\t;\n")
dest.write("END;\n")

dest1 = open("data.properties", "w")
count = 0
dest1.write("#property\n")
for taxlabel in taxlabels:
    count = count+1    
    dest1.write("%d" % count)
    dest1.write("=%s\n" % taxlabel)
