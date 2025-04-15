package com.example.project;

import SignalFlowService.GraphService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {

    @PostMapping("/routh")
    public Routh index(@RequestBody double[] coefficients) {
        Routh routh = new Routh();
        Routh result1 = routh.stable(coefficients);
        result1.getRoots(coefficients);
        return result1;
    }

    @PostMapping("/signal-flow-graph")
    public DataToSend signalFlowGraph(@RequestBody DataToReceive dataToReceive) {
        Graph graph = new Graph(dataToReceive.adjacencyMatrix, dataToReceive.start, dataToReceive.end);
        GraphService graphService = new GraphService(graph);
        DataToSend dataToSend = new DataToSend();
        dataToSend.forwardPaths = graphService.forwardPaths();
        dataToSend.cycles = graphService.cycles();
        dataToSend.nonTouchingLoops = graphService.nonTouchingLoops();
        dataToSend.cycleGains = graphService.cycleGains();
        dataToSend.pathGains = graphService.pathGains();
        dataToSend.delta = graphService.delta(dataToSend.nonTouchingLoops);
        dataToSend.transferFunction = graphService.CalculateTransferFunction();
        dataToSend.deltaOfPaths = new ArrayList<>();
        for (int i = 0; i < dataToSend.forwardPaths.size(); i++) {
            dataToSend.deltaOfPaths.addLast(graphService.deltaOfPath(dataToSend.forwardPaths.get(i), dataToSend.nonTouchingLoops));
        }
        return dataToSend;
    }





}

