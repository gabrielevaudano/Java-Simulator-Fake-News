package RSC.FakeNewsProject.Model;

public abstract class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		
		m.creaErdosRenyiGrafo(1000,6);
		System.out.println(m.getGrafo());
		
		m.simulate(20, 100, 0.7, 0.7, 0.5, 0.2);
		System.out.print(m.getSimulatorResults());
	}

}
