
# QFM 

## QFMstr.cpp
It is the original code that we have got from Reaz et al.
## qfm_rm_randomization_off.cpp
It is the slightly modified version. 
At the time of moving a taxon from one partition to another, if gain and number of satified quartets become equal for two taxa, in that case to break the tie, QFM algorithm moves one of them randomly.
"qfm_rm_randomization_off.cpp" script choses the first one from those two taxa to break the tie.
This is the only difference between these two scripts.
## System Requirement
Linux OS with basic perl installation.
## Dependency
- Depends on bioPerl (bioPerl-1.5.2.zip is used). Extract the bioperl package and make all the files of bioPerl-1.5.2/bin directory executable.
- Depends on reroot_tree_new.pl script. Change the bioperl path address of this script (Line 3)

## How to run
Compile QFMstr.cpp or qfm_rm_randomization_off.cpp
```
g++ QFMstr.cpp -o QFM
```
or
```
g++ qfm_rm_randomization_off.cpp -o QFM
```
Use following command
```
./QFM <input-file> <output-file>
```
For example run following command
```
./QFM input_t25_q125_70.txt output_qfm_t25_q125_70.txt
```

