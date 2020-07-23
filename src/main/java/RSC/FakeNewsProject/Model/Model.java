package RSC.FakeNewsProject.Model;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.generate.BarabasiAlbertGraphGenerator;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.util.SupplierUtil;

import RSC.FakeNewsProject.Simulator.Simulator;

public class Model {
	
	Graph<Nodo, DefaultEdge> grafo;
	GraphGenerator<Nodo, DefaultEdge, Nodo> generatore;
	Simulator sim;
	
	public void creaBarabasiAlbertGrafo(int dimensione, int gradoMedio) {
		// Create the VertexFactory so the generator can create vertices
        Supplier<Nodo> creatoreNodi = new Supplier<Nodo>()
        {
            @Override
            public Nodo get()
            {
                return new Nodo();
            }
        };
        
		// Create the graph object
        this.grafo = new SimpleGraph<>(creatoreNodi, SupplierUtil.createDefaultEdgeSupplier(), false);
        this.generatore = new BarabasiAlbertGraphGenerator<>(dimensione, gradoMedio, dimensione);
        this.generatore.generateGraph(grafo);

	}
	
	public void creaErdosRenyiGrafo(int dimensione, int gradoMedio) {
		// Create the VertexFactory so the generator can create vertices
        Supplier<Nodo> creatoreNodi = new Supplier<Nodo>()
        {
            @Override
            public Nodo get()
            {
                return new Nodo();
            }
        };
        
		// Create the graph object
        this.grafo = new SimpleGraph<>(creatoreNodi, SupplierUtil.createDefaultEdgeSupplier(), false);
        this.generatore = new GnmRandomGraphGenerator<Nodo, DefaultEdge>(dimensione, gradoMedio * dimensione);
        this.generatore.generateGraph(grafo);

	}
	
	public Graph<Nodo, DefaultEdge> getGrafo() {
		return this.grafo;
	}
	
	// int conclusionTime, double alpha, double beta, double pVerify, double pForget
	public void simulate(int initialBelievers, int conclusionTime, double alpha, double beta, double pVerify, double pForget) {
		sim = new Simulator(conclusionTime, alpha, beta, pVerify, pForget);
		sim.setGrafo(grafo);
		sim.init(initialBelievers);
		sim.run();		
	}
	
	public List<Map<String, Integer>> getSimulatorResults() {
		if (sim == null)
			throw new NullPointerException();
		return sim.getResultScore();
	}

	
}
