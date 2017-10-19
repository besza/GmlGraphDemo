package jgrapht.demo;


import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.io.*;

import java.io.*;
import java.util.Map;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        SimpleDirectedWeightedGraph<CapacityVertex, DefaultWeightedEdge> graph =
                new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        CapacityVertex A = new CapacityVertex("A");
        CapacityVertex B = new CapacityVertex("B");
        CapacityVertex C = new CapacityVertex("C");

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);

        graph.addEdge(A, B);
        graph.addEdge(A, C);
        graph.addEdge(B, C);


        // adjunk random élsúlyokat
        Random generator = new Random();
        for (DefaultWeightedEdge e : graph.edgeSet()) {
            graph.setEdgeWeight(e, generator.nextInt(100));
        }

        GmlExporter<CapacityVertex, DefaultWeightedEdge> exporter =
                new GmlExporter<>(
                        CapacityVertex::getName, //a vertex id-ja legyen a neve
                        (v) -> String.valueOf(v.getCapacity()), //a vertex label-je legyen a kapacitása
                        new IntegerComponentNameProvider<>(), //az él id-ja legyen a soron következő egész
                        null);

        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS, true);
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        exporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, true);

        try (Writer writer = new PrintWriter(new File("out.gml"))){
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------------------------------------------
        
        VertexProvider<CapacityVertex> vertexProvider = new VertexProvider<CapacityVertex>() {
            @Override
            public CapacityVertex buildVertex(String s, Map<String, String> map) {
                CapacityVertex vertex = new CapacityVertex(s);
                
                //tákoljuk össze a vertex objektumot, itt fontos tudni azt, hogy mit tettünk a csúcs label-jébe
                String capacity = map.get("label");
                if (capacity != null) {
                    vertex.setCapacity(Integer.valueOf(capacity));
                }
                
                return vertex;
            }
        };
        
        EdgeProvider<CapacityVertex, DefaultWeightedEdge> edgeProvider = new EdgeProvider<CapacityVertex, DefaultWeightedEdge>() {
            @Override
            public DefaultWeightedEdge buildEdge(CapacityVertex capacityVertex, CapacityVertex v1, String s, Map<String, String> map) {
                return new DefaultWeightedEdge();
            }
        };
        
        GmlImporter<CapacityVertex, DefaultWeightedEdge> importer = 
                new GmlImporter<>(vertexProvider, edgeProvider);

        SimpleDirectedWeightedGraph<CapacityVertex, DefaultWeightedEdge> importedGraph =
                new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        try {
            SimpleWeightedGraph<CapacityVertex, DefaultWeightedEdge> graph2 =
                    new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
            importer.importGraph(graph2, new File("out.gml"));

        } catch (ImportException e) {
            e.printStackTrace();
        }


    }

}
