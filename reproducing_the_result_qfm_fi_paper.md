
# Reproducing the Result of QFM-FI paper
All the scripts and tools to re-generate the result of QFM-FI paper are available [here]().
Download all the datasets before reproducing the results. 
The information of datasets are given [here](https://github.com/sharmin-mim/qfm_java/blob/master/README.md).
1. A script named "getFpFn.py" is used to calculate Robinson-Foulds distance. In order to use this script, install two packages which are in newick_modified-1.3.1_Bayzid.zip and spruce-1.0.tar.gz.
The detail description of installing these two packages are given inside these compressed files.

2. In order to access "time" command install "time" package using following command.
```bash
sudo apt install time
```
3. Before running every scripts, check and change (if it is needed) the path/directory of input datasets, jars and binary files. Make all the binary files executable.
4. Some general instructions are given inside all the scripts. Read them carefully before running any of them.
## Generating and Reproducing Result on Dataset-I
### Generating Dataset-I:
Dataset-I is available [here](https://drive.google.com/drive/folders/1yUUR8mHz7d20ckUKe7Q326JjouYGNf3p).
We could not provide some quartet files as their size is very large.
We have provided the model trees at [here](https://drive.google.com/drive/folders/1GyZO_vqAlfq4zUqPqDW4A29crS4rQYzv).
In order to generate set of quartets from these already generated model tree, run following command.
```bash
perl data_generator_with_existing_model_tree.pl -ntaxa n 
```
Here, 
- n = number of taxa in the simulation
For example, in order to generate quartet for 500 taxa, run following command.
```bash
perl data_generator_with_existing_model_tree.pl -ntaxa 500 
```

In order to generate new model tree (true tree) as well as set of quartets, run following command.
```bash
perl data_generator.pl -ntaxa n 
```
Here, 
- n = number of taxa in the simulation
For example, in order to generate quartet for 400 taxa, run following command.
```bash
perl data_generator.pl -ntaxa 400 
```
### Reproducing Result on Dataset-I
The script named "reproduce-result-dataset-I.pl" only gives the result of one replicate of all methods which includes QFM-F, QFM-FI, QMC and wQMC.
1. Edit all the directory or path address.
2. Run the script using following command  
```bash
perl reproduce-result-dataset-I.pl --ntaxa n --experiment r --corr c --qnum_factor q
```

Here, 
- n = number of taxa in the simulation
- q = factor number of qrts related to ntaxa
- c = percentage of input quartet that are consistent with model tree  
- r = replicate number

For example, run following command for number of taxa n=100, q=2.8, c=100%, replicate number r=1       
```bash
perl reproduce-result-dataset-I.pl --ntaxa 100 --experiment 1 --corr 100 --qnum_factor 2.8
``` 

The script named "run_20_replicates.pl" gives the result of 20 replicate of all methods which includes QFM-F, QFM-FI, QMC and wQMC
Change number of taxa, qnum_factor and consistency inside this script.
Run the script using following command.
```bash
perl run_20_replicates.pl
```


## Reproducing Result on SATe Dataset

1. Download SATe dataset from [here](https://sites.google.com/eng.ucsd.edu/datasets/alignment/sate-i?authuser=0).In this experiment we have only used the datasets where number of taxa is either 100 or 500 ( only download 100L1.tar.bz2, 100L2.tar.bz2, 100M1.tar.bz2, 100M2.tar.bz2, 100M3.tar.bz2, 100S1.tar.bz2, 100S2.tar.bz2, 500L1.tar.bz2, 500L2.tar.bz2, 500L3.tar.bz2, 500L4.tar.bz2, 500L5.tar.bz2, 500M1.tar.bz2, 500M2.tar.bz2, 500M3.tar.bz2, 500M4.tar.bz2, 500M5.tar.bz2, 500S1.tar.bz2, 500S2.tar.bz2, 500S3.tar.bz2, 500S4.tar.bz2, 500S5.tar.bz2). Then unzip them.
2. Download and install [PAUP*](https://paup.phylosolutions.com/). 
3. Run following command (make sure you have a large storage in your hard drive because the size of generated quartet files are large)
```bash
perl run_full_SATe_dataset.pl
``` 

## Reproducing Result on 37-taxon Dataset
1.	We have downloaded 37-taxon simulated dataset from
		https://drive.google.com/drive/folders/1X52MOo2lObw3tiZ6GJSCR86je4nbUrdx
2. 	Run following command 
```bash
perl 37-taxon-simulation.pl 
```
## Reproducing Result on 100-taxon Dataset
In this dataset, there are 100 taxa and 1 outgroup
1.	We have downloaded 100-taxon simulated dataset from
		https://drive.google.com/drive/folders/1r3nk_t4Lkwi7ZMktWHhXCqirrSnQOCtv
2. 	Run following command 
```bash
perl 100-taxon-simulation.pl 
```
## Reproducing Result of all Biological Dataset
1.	We have taken biological datasets from different sources. In QFM-FI paper, we have provided all the information of these sources. All the biological datasets are also available at [here](https://drive.google.com/drive/folders/18spjLcCqLDN6vY1UVUkUtvpxBmq0E6TJ).
2. 	Run following command 
```bash
perl biological-simulation.pl 
```

## Acknowledgements

 - For rerooting a tree with respect to an outgroup, both QFM-F and QFM-FI uses some methods in the [PhyloNet](https://bioinfocs.rice.edu/phylonet) package. 
    
	C. Than, D. Ruths, L. Nakhleh (2008). PhyloNet: A software package for analyzing and reconstructing reticulate evolutionary histories, BMC Bioinformatics 9:322. 
	
 - To generate model species tree for Dataset-I, [r8s](https://doi.org/10.1093/bioinformatics/19.2.301) software is used. 
    
	Sanderson, M. J. (2003). r8s: inferring absolute rates of molecular evolution and divergence times in the absence of a molecular clock. Bioinformatics, 19(2):301–302.
	
 - To generate quartets (both weighted and unweighted) for Dataset-I, some scripts of [wQMC](http://research.haifa.ac.il/~ssagi/software/wQMC.tar.gz) package is used. 
    
    Avni, E., Cohen, R., and Snir, S. (2014). Weighted Quartets Phylogenetics. Systematic Biology, 64(2):233–242.

 - Two scripts (convertForWQMC_BufferedReader.jar and convertBack.jar) of [wQFM](https://github.com/Mahim1997/wQFM-2020) package is used. We have also used some of their pre-processed datasets. 
    
    Mahbub, M., Wahab, Z., Reaz, R., Rahman, M. S., and Bayzid, M. S. (2021). wQFM: highly accurate genome-scale species tree estimation from weighted quartets. Bioinformatics, 37(21):3734–3743.

