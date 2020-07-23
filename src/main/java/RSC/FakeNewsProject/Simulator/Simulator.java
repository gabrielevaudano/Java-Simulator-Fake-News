package RSC.FakeNewsProject.Simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import RSC.FakeNewsProject.Model.Nodo;

public class Simulator {
	// Imposto le variabili di simulazione
	private double alpha; // parametro costante indicante la credibilità della notizia falsa
	private double beta; // paramtro costante indicante il tasso di diffusione della notizia
	
	private double pVerify; // parametro che indica la probabilità di verifica della notizia da parte di un nodo
	private double pForget; // probabilità che un nodo dimentichi la notizia, nel bene o nel male
	
	private int conclusionTime; // tempo di esecuzione della diffusione (step di diffusione)
	private int currentTime;
	
	private Graph<Nodo, DefaultEdge> referenceGraph;
	private List<List<Nodo>> evolution;
	
	public Simulator(int conclusionTime, double alpha, double beta, double pVerify, double pForget) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		this.pVerify = pVerify;
		this.pForget = pForget;
		this.conclusionTime = conclusionTime;
	}
	
	public void setGrafo(Graph<Nodo, DefaultEdge> grafo) {
		this.referenceGraph = grafo;
	}
	
	
	public List<List<Nodo>> getResult() {
		return this.evolution;
	}
	
	public List<Map<String, Integer>> getResultScore() { 
		List<Map<String, Integer>> map = new ArrayList<Map<String,Integer>>();
		
		for (List<Nodo> l : this.evolution) {
			int susceptible = 0;
			int factChecker = 0;
			int believer = 0;
			
			for (Nodo nodo : l)
			{
				if (nodo.isSusceptible())
					susceptible++;
				if (nodo.isFactChecker())
					factChecker++;
				if (nodo.isBeliever())
					believer++;				
			}
			
			Map<String, Integer> re = new HashMap<String, Integer>();
			
			re.put("Susceptible", susceptible);
			re.put("Believer", believer);
			re.put("Fact Checker", factChecker);
			
			map.add(re);
		}
		
	return map;
	
	}
	
	public void init(int initialBelievers) {
		// Initialize process machine
			this.currentTime = 0;
			this.evolution = new ArrayList<List<Nodo>>();
			
			List<Nodo> startingPoint = new ArrayList<Nodo>(referenceGraph.vertexSet());
			
			for (int bel = 0; bel < initialBelievers; bel++)
			{
				startingPoint.get(bel).setBeliever(true);
				startingPoint.get(bel).setSusceptible(false);
			}
			
			this.evolution.add(new ArrayList<Nodo>());
			
			for (Nodo n : startingPoint)
				evolution.get(evolution.size()-1).add(new Nodo(n, currentTime));
			
	}
	
	public void run() {
		do {
			this.currentTime++; // Proceed time
			
			// New time -> next step
			List<Nodo> nextEvolutionStep = new ArrayList<>();
		
			for (Nodo nodo : this.referenceGraph.vertexSet()) {
				Nodo result = processEvent(nodo);
				nextEvolutionStep.add(new Nodo(result, this.currentTime));
			}
			
			this.evolution.add(new ArrayList<>(nextEvolutionStep));
		} while (currentTime < conclusionTime);
	}

	
	public Nodo processEvent(Nodo nodo) {
		// the probability mass function pi(t+1) is defined by
		double pBeliever = functionF(nodo) * parsingIntBool(nodo.isSusceptible()) + (1 - pForget - pVerify) * parsingIntBool(nodo.isBeliever());
		double pFactChecker = functionG(nodo) * parsingIntBool(nodo.isSusceptible()) + pVerify * parsingIntBool(nodo.isBeliever()) + (1 - pForget) * parsingIntBool(nodo.isFactChecker());
		double pSusceptible = pForget * parsingIntBool(nodo.isSusceptible()) + parsingIntBool(nodo.isFactChecker()) + (1 - functionF(nodo)- functionG(nodo)) * parsingIntBool(nodo.isSusceptible());
		
		// Its mechanism is not commented on paper -> suppose it works randomly
		if ((pBeliever >= pFactChecker) && (pBeliever >= pSusceptible)) {
			nodo.setBeliever(true);
			nodo.setFactChecker(false);
			nodo.setSusceptible(false);
		}
		else if ((pFactChecker > pBeliever) && (pFactChecker > pSusceptible)){
			nodo.setBeliever(false);
			nodo.setFactChecker(true);
			nodo.setSusceptible(false);
		}
		else if ((pSusceptible > pBeliever) && (pSusceptible > pFactChecker)) {
			nodo.setBeliever(false);
			nodo.setFactChecker(false);
			nodo.setSusceptible(true);
		}

		return nodo;
	}
	
	// function of fact checking 
	private double functionG(Nodo nodo) {
		double result;
		
		result = this.beta * (numBelieverNeighbours(nodo)* (1 - this.alpha));
		result = result / ((numFactCheckerNeighbours(nodo) * (1 - this.alpha)) + numBelieverNeighbours(nodo) * (1 + this.alpha));  

		if (Double.isNaN(result))
			result = Double.POSITIVE_INFINITY;
		System.out.println(String.format("Believers: %d, FactCheckers: %d", numBelieverNeighbours(nodo), numFactCheckerNeighbours(nodo)));
		return result;
	}
	
	// function of diffusion of fake news
	private double functionF(Nodo nodo) {
		double result;
		result = this.beta * (numBelieverNeighbours(nodo) * (1 + this.alpha));
		result = result / ((numFactCheckerNeighbours(nodo) * (1 - this.alpha)) + numBelieverNeighbours(nodo) * (1 + this.alpha));  
	
		if (Double.isNaN(result))
			result = Double.POSITIVE_INFINITY;
		return result;
	}
	
	private int numFactCheckerNeighbours(Nodo nodo) {
		List<Nodo> neighbours = new ArrayList<Nodo>();
		
		for (Nodo n : Graphs.neighborListOf(this.referenceGraph, nodo))
			if (n.isFactChecker())
				neighbours.add(n);
	
		return neighbours.size();
	}
	
	private int numBelieverNeighbours(Nodo nodo) {
		List<Nodo> neighbours = new ArrayList<Nodo>();
		for (Nodo n : Graphs.neighborListOf(this.referenceGraph, nodo))
			if (nodo.isBeliever())
				neighbours.add(n);
		
		return neighbours.size();
	}

	private int parsingIntBool(boolean bool) {
		if (bool==true)
			return 1;
		else
			return 0;
	}

	
}

