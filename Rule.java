package cis262TM;

public class Rule {
	
	private int initState;
	private int initC;
	private int nextC;
	private String dir;
	private int nextState;
	
	public Rule(int initState, int initC, int nextC, String dir, int nextState) {
		super();
		this.initState = initState;
		this.initC = initC;
		this.nextC = nextC;
		this.dir = dir;
		this.nextState = nextState;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + initState + ", " + initC + ", " + nextC + ", " + dir
				+ ", " + nextState + ")";
	}

	/**
	 * @return the initState
	 */
	public int getInitState() {
		return initState;
	}
	/**
	 * @return the initC
	 */
	public int getInitC() {
		return initC;
	}
	/**
	 * @return the nextC
	 */
	public int getNextC() {
		return nextC;
	}
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @return the nextState
	 */
	public int getNextState() {
		return nextState;
	}
	
}
