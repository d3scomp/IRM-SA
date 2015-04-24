jDEECo meta adaptation strategies 
======

This project contains the implementation of the meta-adaptation strategies (MAS) as plugins to the jDEECo framework, as specified in our paper submitted to ECSA 2015, and a demo that shows their combined execution. 

It is comprised of 
* 2 Eclipse projects with the 3 MAS plugins: "cz.cuni.mff.d3s.jdeeco.irm-sa.correlation" (containing the implementation of the 1st MAS) and "cz.cuni.mff.d3s.irm-sa.strategies" (containing the implementation of the 2nd and 3rd MAS)
* 1 Eclipse project with the main IRM-SA plugin: "cz.cuni.mff.d3s.irm-sa"
* 1 Eclipse project with the demo: "cz.cuni.mff.d3s.jdeeco.irm-sa.strategies.demo"

##Building
To build the demo you just need to checkout the sources and run a maven update. 
This will download (among others) the main dependency, which is [jDEECo core v. 3.0](https://github.com/d3scomp/JDEECo/tree/jDEECo-3).

##Testing
You can run the combined demo by running the "Run" class inside the "combined" package of the demo project. 
