import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object GraphComponents {
  def main(args: Array[String]): Unit = {

    /*Creating Spark configuration and setting up the app name*/
    val conf = new SparkConf().setAppName("Connectedness of a Graph")
    conf.setMaster("local[2]")

    /*Initializing the SparkContext*/
    val sc = new SparkContext(conf)

    /*Reading the file line by line and mapping as tuples*/
    val scan = sc.textFile(args(0)).map(line => {

      /*Splitting each line using a comma as a delimiter*/
      val a = line.split(",")

      /*Initially, the group of each node is set to the node id*/
      (a(0).toLong, a(0).toLong, a.slice(1, a.length).toList.map(_.toLong))
    })

    /*Creating edges from scanned data*/
    val edgeTuples = scan.flatMap(x => x._3.flatMap(a => Seq((a, x._2))) ++ Seq((x._1, x._2)))

    /*Creating Edge RDDs*/
    val edges = edgeTuples.map(x => {

      new Edge(x._1, x._2, 1)
    })

    /*Using the graph builder Graph.fromEdges to construct a Graph from the RDD of edges*/
    val graph = Graph.fromEdges(edges, 1)

    /*Accessing the VertexRDD and change the value of each vertex to be the vertex ID (initial group number)*/
    val initialGraph = graph.mapVertices((id, _) => id)

    /*Calling the Graph.pregel method in the GraphX Pregel API to find the connected components.
    For each vertex, this method changes its group number to the minimum group number of its neighbors
    (if it is less than its current group number)*/
    val process = initialGraph.pregel(Long.MaxValue, 5)(

      (id, dist, newDist) => math.min(dist, newDist),

      edge => {

        if (edge.srcAttr < edge.dstAttr) {

          Iterator((edge.dstId, edge.srcAttr))
        } else {

          Iterator.empty
        }
      },
      (a, b) => math.min(a, b)
    )

    /*Grouping the graph vertices by their group number*/
    val result = process.vertices.map(v => (v._2, 1)).reduceByKey((x, y) => x + y)

    /*Printing the group sizes*/
    result.map(x => {

      x._1 + "\t" + x._2
    }).collect().foreach(println)

    /*Stopping the SparkContext*/
    sc.stop()
  }
}
