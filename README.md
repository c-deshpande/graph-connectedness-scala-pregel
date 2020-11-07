# graph-connectedness-scala-pregel
Generate Histogram for each RGB color in the given dataset using Scala and Spark and GraphX Pregel API.

Project done as a part of CSE-6331 Cloud Computing Course at UTA.

<a href="https://lambda.uta.edu/cse6331/spring20/project8.html">Project Description</a>

<p>An undirected graph is represented in the input text file using one line per graph vertex. For example, the line</p>

<p>1,2,3,4,5,6,7</p>
</p>represents the vertex with ID 1, which is connected to the vertices with IDs 2, 3, 4, 5, 6, and 7. For example, the following graph:</p>

<img src="https://raw.githubusercontent.com/c-deshpande/graph-connectedness/master/img/p2.png"/>

<p>is represented in the input file as follows:</p>
3,2,1
<br>
2,4,3
<br>
1,3,4,6
<br>
5,6
<br>
6,5,7,1
<br>
0,8,9
<br>
4,2,1
<br>
8,0
<br>
9,0
<br>
7,6
<br>
<br>
<p align=justify>
The task is to write a Map-Reduce program that finds the connected components of any undirected graph and prints the size of these connected components. A connected component of a graph is a subgraph of the graph in which there is a path from any two vertices in the subgraph. For the above graph, there are two connected components: one 0,8,9 and another 1,2,3,4,5,6,7. The program should print the sizes of these connected components: 3 and 7.
</p>

<p align=justify>
The following pseudo-code finds the connected components. It assigns a unique group number to each vertex (we are using the vertex ID as the group number), and for each graph edge between Vi and Vj, it changes the group number of these vertices to the minimum group number of Vi and Vj. That way, vertices connected together will eventually get the same minimum group number, which is the minimum vertex ID among all vertices in the connected component. First you need a class to represent a vertex:
</p>

<p align=justify>
Re-implementing Project #5 (Graph Processing) using Pregel on Spark GraphX. That is, the program must find the connected components of any undirected graph and print the size of these connected components. Have to use the pregel method from the GraphX Pregel API only to write your code. The main program should take the text file that contains the graph (small-graph.txt or large-graph.txt) as an argument and print the results to the output. The stopping condition is when the number of repetition reaches 5.
<p>

Psuedo-code:

```
1. Read the input graph and construct the RDD of edges
2. Use the graph builder Graph.fromEdges to construct a Graph from the RDD of edges
3. Access the VertexRDD and change the value of each vertex to be the vertex ID (initial group number)
4. Call the Graph.pregel method in the GraphX Pregel API to find the connected components. For each vertex, this method changes its group number to the minimum group number of its neighbors (if it is less than its current group number)
5. Group the graph vertices by their group number and print the group sizes. 
```
