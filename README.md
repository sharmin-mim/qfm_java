
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
## Acknowledgements

 - For rerooting a tree with respect to an outgroup, both QFM-F and QFM-FI uses some methods in the [PhyloNet](https://bioinfocs.rice.edu/phylonet) package. 
    
    C. Than, D. Ruths, L. Nakhleh (2008) PhyloNet: A software package for analyzing and reconstructing reticulate evolutionary histories, BMC Bioinformatics 9:322.
  
## License

The contents of this repository are licensed under the MIT License.

See [LICENSE](https://github.com/sharmin-mim/qfm_java/blob/master/LICENSE) for the full license text.



## Bugs or Issues
We are constantly searching for ways to improve our codebase.

For any bugs, please post on [issues](https://github.com/sharmin-mim/qfm_java/issues) page.

Alternatively, you can email at `sharmin133mim@gmail.com`.