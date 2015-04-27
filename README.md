IRM-SA
======

This project contains the adaptation plugin for jDEECo as explained in [http://d3s.mff.cuni.cz/projects/components_and_services/irm-sa/](http://d3s.mff.cuni.cz/projects/components_and_services/irm-sa/)

##Building the plugin
To build the plugin you also need to import the latest versions of two more Eclipse projects: [IRM tool] (https://gitlab.d3s.mff.cuni.cz/iliasg/irm-sa-tool) and [jDEECo core](https://github.com/d3scomp/JDEECo).

##Testing the plugin
You can test the plugin by running its unit tests: "run as..."->" JUnit Test".

##Experiments
The IRM-SA project comes with an application that serves as a proof of the concept. It can be found under the 'irm-sa.experimentations'. This simple experiment implements the Firefighter Scenario that deals with the firefighters reaction time on a critical situation. In particular the IRM-SA adaptation mechanism is evaluated by monitoring continuity in system "normal" operation, which is given by a sustained connectivity between components.

###Running experiments
To run the experiments import the 'irm-sa.experimentations' project into your Eclipse workspace. There are prepared few runnig configuration that facilitate the application execution. The main Java classes that act as launchers of the simulations are:

- SingleNodeSimulation - 6 components (one team) deployed on a single node - no communication involved
- MultiNodeSimulation - 14 components (three teams) deployed on seperate nodes - full distribution 
- MultiNodeSimulationOneTeam - 6 components (one team) deployed on seperate nodes - full distribution 
