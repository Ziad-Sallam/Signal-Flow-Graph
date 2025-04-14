package SignalFlowService;

import com.example.project.Graph;
import com.example.project.NonTouchingLoops;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GraphService {
      private Graph graph;

      public GraphService(Graph graph) {
            this.graph = graph;
            graph.findPaths();
            graph.findCycles();
            graph.calculateCyclesgain();
            graph.calculatePathsgain();
      }

      public List<List<Integer>> forwardPaths() {
            return graph.getPaths();
      }

      public List<List<Integer>> cycles() {
            return graph.getCycles();
      }

      public List<List<Integer>> nonTouchingLoops() {
            NonTouchingLoops nonTouchingLoops = new NonTouchingLoops();
            return nonTouchingLoops.findNonTouchingLoops(graph.getCycles());
      }

      public List<Integer> cycleGains() {

            return graph.getCyclesgains();
      }

      public List<Integer> pathGains() {

            return graph.getPathsgains();
      }

      public float delta(List<List<Integer>> nonTouchingLoops) {
            float delta = 1;

            for (int i = 0; i < cycles().size(); i++) {
                        delta =delta- cycleGains().get(i);
            }

            for (List<Integer> group : nonTouchingLoops) {
                  int gainProduct = 1;
                  for (int index : group) {
                        gainProduct =gainProduct* cycleGains().get(index);
                  }

                  if (group.size() % 2 == 0) {
                        delta =delta+ gainProduct;
                  } else {
                        delta =delta-gainProduct;
                  }
            }
            return delta;
      }

      public float deltaOfPath(List<Integer> path, List<List<Integer>> nonTouchingLoops) {
            float delta = 1;
            List<List<Integer>> filteredLoops = graph.nontouchinwithPath(cycles(), path);

            for (int i = 0; i < cycles().size(); i++) {
                  if (i < cycleGains().size() && filteredLoops.contains(cycles().get(i))) {
                        delta =delta-cycleGains().get(i);
                  }
            }
            for (List<Integer> group : nonTouchingLoops) {
                  boolean flag = true;
                  for (int index : group) {
                     if (!filteredLoops.contains(cycles().get(index))) {
                        flag = false;
                           break;
                     }

                  }

                  if (flag) {
                        int gainProduct = 1;
                        for (int index : group) {
                          gainProduct =gainProduct*cycleGains().get(index);
                        }

                        if (group.size() % 2 == 0) {
                              delta =delta+ gainProduct;
                        } else {
                              delta =delta-gainProduct;
                        }
                  }
            }

            return delta;
      }


      public float CalculateTransferFunction() {
            float tf = 0;
            List<List<Integer>> forwardPaths = forwardPaths();
            List<List<Integer>> nonTouchingLoops = nonTouchingLoops();
            List<Integer> forwardPathGains = pathGains();
            float delta = delta(nonTouchingLoops);

            for (int i = 0; i < forwardPaths.size(); i++) {
                  float pathGain = forwardPathGains.get(i) ;
                  float pathDelta = deltaOfPath(forwardPaths.get(i), nonTouchingLoops);
                  tf =tf+ (pathGain * pathDelta);
            }

            tf = tf / delta;
            return tf;
      }

      public static void main(String[] args) {
            int[][] adjacencyMatrix = {
//                    {0, 1, 0, 0, 0, 0, 0},
//                    {0, 0, 5, 0, 0, 0, 10},
//                    {0, 0, 0, 10, 0, 0, 0},
//                    {0, 0, -1, 0, 2, 0, 0},
//                    {0, -1, 0, -2, 0, 1, 0},
//                    {0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 2, 0, -1}

                 //  0,1,2,3,4,5
                   {0,1,0,0,0,0},
                    {0,0,2,0,5,0},
                    {0,-1,0,3,0,0},
                    {0,0,-1,0,4,0},
                    {0,0,0,-1,0,1},
                    {0,0,0,0,0,0}

            };

            Graph graph = new Graph(adjacencyMatrix, 0, 5);
            GraphService graphService = new GraphService(graph);

            List<List<Integer>> forwardPaths = graphService.forwardPaths();
            List<List<Integer>> cycles = graphService.cycles();
            List<List<Integer>> nonTouchingLoops = graphService.nonTouchingLoops();
            List<Integer> cycleGains = graphService.cycleGains();
            List<Integer> pathGains = graphService.pathGains();
            float delta = graphService.delta(nonTouchingLoops);
            float transferFunction = graphService.CalculateTransferFunction();

            System.out.println("Forward Paths: " + forwardPaths);
            System.out.println("Cycles: " + cycles);
            System.out.println("Non-Touching Loops: " + nonTouchingLoops);
            System.out.println("Cycle Gains: " + cycleGains);
            System.out.println("Path Gains: " + pathGains);
            System.out.println("Delta: " + delta);
            for (int i = 0; i < forwardPaths.size(); i++) {
              System.out.println("Delta of Path "+graphService.deltaOfPath(forwardPaths.get(i), nonTouchingLoops));
            }

            System.out.println("Transfer Function: " + transferFunction);


      }
}