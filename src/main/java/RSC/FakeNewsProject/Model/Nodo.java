package RSC.FakeNewsProject.Model;

public class Nodo {
	private static int idGenerator = 0;
	private int id;
	private int time;
	
	private boolean believer;
	private boolean factChecker;
	private boolean susceptible;
	
	public Nodo(Nodo nodo) {
		this.id = nodo.id;
		
		this.factChecker = false;
		this.susceptible = false;
		this.believer = false;
	}
	
	public Nodo(Nodo nodo, int time) {
		this.id = nodo.id;
		
		this.factChecker = nodo.factChecker;
		this.susceptible = nodo.susceptible;
		this.believer = nodo.believer;
		this.time = time;
	}
	
	public Nodo() {
		this.id = idGenerator++;
		
		this.susceptible = true;
		this.factChecker = false;
		this.believer = false;
	}
	

	public int getId() {
		return this.id;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public boolean isBeliever() {
		return believer;
	}
	
	public void setBeliever(boolean believer) {
		this.believer = believer;
	}
	
	public boolean isFactChecker() {
		return factChecker;
	}
	
	public void setFactChecker(boolean factChecker) {
		this.factChecker = factChecker;
	}
	
	public boolean isSusceptible() {
		return susceptible;
	}
	
	public void setSusceptible(boolean susceptible) {
		this.susceptible = susceptible;
	}

	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + time;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nodo other = (Nodo) obj;
		if (id != other.id)
			return false;
		if (time != other.time)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Nodo [id=" + id + "]";
	}


	
	
}
