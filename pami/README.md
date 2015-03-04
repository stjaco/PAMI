# PAMI

Requires the apache commons lang, math, and io library paths to be set.

things this code needs (ordered by priority):

1- make a parametric main in PAMI.java (so we can then run the jar from scripts)
the GraphBuilder (usage examples is commented in the current main) object creates the graphs from the raw data and prints them to files (step 1). Then, a static aggregateFeats() method takes care of step 3.

The parameters to be managed include: paths, output filenames, whether to create dynamic or static graphs, whether the graphs created should be directed or not, thresholds for slice duration (e.g. 120) and data selection (e.g. "float th = .95f;" currently hardcoded in AudioGraph.java)

2- a bash script that uses the parametric jar to create and process the various kind of files

3- (optional) minor refactoring: DynamicGraph objects should not have a to override a fill method.. they should just call the fill() method of Graph objects they contain (the latter should be rewritten in order to do so); using the static aggregateFeats() method is a quick and dirt solution, could be done in better ways...

4- (optional) XML output might be provided by external libraries, through XML annotations etc.
