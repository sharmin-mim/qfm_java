
# QFM-FI

This repository contains the official implementation of our paper "Quartet Fiduccia-Mattheyses Revisited for Larger Phylogenetic Studies" submitted in Bioinformatics, 2022.

## Brief Description
We have modified the [QFM algorithm](https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0104008) which was implemented by Reaz et al.
 
### QFM-F (QFM Fast)
We have modified the QFM algorithm to make it scalable to larger dataset. 
We named this faster version as QFM Fast (in short QFM-F). 
### QFM-FI (QFM Fast and Improved)
We have also enhanced the tree quality of resultant species tree. 
This version is named as QFM Fast and Improved (in short QFM-FI).
The source code for this version are in [qfm_ad](https://github.com/sharmin-mim/qfm_java/tree/master/qfm_ad) directory.

## Input and Output
Sample input and output files are given in [sample IO](https://github.com/sharmin-mim/qfm_java/tree/master/sample%20IO) folder.
### Input
This application takes set of quartets as input.
The default format of input quartet is `a,b|c,d`. 
Here a, b, c and d are four species. 
### Output
A phylogenetic tree in newick format.
## Execution Dependencies
- Java (required to run the QFM-F and QFM-FI application).
## Instruction to Run the Application
### To run QFM-F
At first, unzip the [QFM-F.zip](https://github.com/sharmin-mim/qfm_java/blob/master/QFM-F.zip). Then run following code.
```bash
java -jar QFM-F.jar "input-file-name" "output-file-name"
```

For large number of taxa, increasing the memory available to Java is recommended.

```bash
# Example: If RAM is 8GB and free memory is 6GB, use following command

java -Xmx6000M -jar QFM-F.jar "input-file-name" "output-file-name"
```

### To run QFM-FI
At first, unzip the [QFM-F.zip](https://github.com/sharmin-mim/qfm_java/blob/master/QFM-FI.zip). Then run following code.
```bash
java -jar QFM-FI.jar "input-file-name" "output-file-name"
```

For large number of taxa, increasing the memory available to Java is recommended.

```bash
# Example: If RAM is 8GB and free memory is 6GB, use following command

java -Xmx6000M -jar QFM-FI.jar "input-file-name" "output-file-name"
```

## Datasets
| Simulated Dataset |       Total Model Conditions       | Number of Replicates |
|:-----------------:|:----------------------------------:|:--------------------:|
|     Dataset-I     |   150 (120 noisy + 30 noiseless)   |          20          |
|        SATe       |        22 out of 37 are used       |          20          |
|      37-taxon     |                 14                 |          20          |
|     100-taxon     | 1 (1000 true gene trees are used)  |          10          |

| Biological Dataset | Taken from | Number of Taxon |
|:------------------:|:----------:|:---------------:|
|        Plant       |  [Wickett et al.](https://doi.org/10.1073/pnas.1323926111)  |       103       |
|       Amniota      |  [Chiari et al.](https://doi.org/10.1186/1741-7007-10-65)  |        16       |
|        Avian       |  [Mahbub et al.](https://doi.org/10.1093/bioinformatics/btab428)  |        48       |
|     Angiosperm     |  [Xi et al.](https://doi.org/10.1093/sysbio/syu055)  |        42       |
|    Mammal mtDNA    |  [Anjum et al.](https://doi.org/10.1109/TCBB.2021.3136792) |        41       |

All the biological datasets and dataset-I are availabe [here](https://drive.google.com/drive/folders/1-hqakFYh5J5qQv2WJgcOakkapsY8Tiu4?usp=sharing).
SATe dataset is available [here](https://sites.google.com/eng.ucsd.edu/datasets/alignment/sate-i?authuser=0).
37-taxon and 100-taxon dataset are available [here](https://drive.google.com/drive/folders/1IYKYWG81Sld8QwzZNO5D71mOulGVd7ax?usp=sharing).

## Reproducing the result of QFM-FI paper
To reproduce the result of QFM-FI paper, follow the instruction of [reproducing_the_result_qfm_fi_paper.md](https://github.com/sharmin-mim/qfm_java/blob/master/reproducing_the_result_qfm_fi_paper.md).


## Acknowledgements

 - For rerooting a tree with respect to an outgroup, both QFM-F and QFM-FI uses some methods in the [PhyloNet](https://bioinfocs.rice.edu/phylonet) package. 
    
	C. Than, D. Ruths, L. Nakhleh (2008). PhyloNet: A software package for analyzing and reconstructing reticulate evolutionary histories, BMC Bioinformatics 9:322. 
	
 - To generate model species tree for Dataset-I, [r8s](https://doi.org/10.1093/bioinformatics/19.2.301) software is used. 
    
	Sanderson, M. J. (2003). r8s: inferring absolute rates of molecular evolution and divergence times in the absence of a molecular clock. Bioinformatics, 19(2):301–302.
	
 - To generate quartets (both weighted and unweighted) for Dataset-I, some scripts of [wQMC](http://research.haifa.ac.il/~ssagi/software/wQMC.tar.gz) package is used. 
    
    Avni, E., Cohen, R., and Snir, S. (2014). Weighted Quartets Phylogenetics. Systematic Biology, 64(2):233–242.

 - Two scripts (convertForWQMC_BufferedReader.jar and convertBack.jar) of [wQFM](https://github.com/Mahim1997/wQFM-2020) package is used. 
    
    Mahbub, M., Wahab, Z., Reaz, R., Rahman, M. S., and Bayzid, M. S. (2021). wQFM: highly accurate genome-scale species tree estimation from weighted quartets. Bioinformatics, 37(21):3734–3743.
	
  
## License

The contents of this repository are licensed under the MIT License.

See [LICENSE](https://github.com/sharmin-mim/qfm_java/blob/master/LICENSE) for the full license text.



## Bugs or Issues
We are constantly searching for ways to improve our codebase.

For any bugs, please post on [issues](https://github.com/sharmin-mim/qfm_java/issues) page.

Alternatively, you can email at `sharmin133mim@gmail.com`. 
