ants-samplebot
==============
Sample Bot to get started with coding a bot for the (no longer current) Ants AI Challenge. (http://aichallenge.org)

### Setting up
To get started, import all projects in this repository into an Eclipse installation. 
We recommend to use the eGit plugin (comes preinstalled with most current Eclipse distributions).
Use Import... -> Projects from Git to import the projects into your workspace.

### Launching a game
The sample bot will simply issue random orders for the ants. 
To see it in action, run the tutorial target of the included ANT build file (Ants/build.xml). 
The build.xml file contains a few additional targets for more interesting matches.

### Coding your own
The Ants project contains the basis for the Bot. Take a look at ants.bot.SampleBot.java to get started.
The other projects contain useful algorithms and classes, such as A* and BFS for pathfinding and searching, or a simple InfluenceMap implementation.
To get a feel for the game, you may want to try the tutorial at http://aichallenge.org/ants_tutorial.php
 
