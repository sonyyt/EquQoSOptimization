package equivalentProj;
import java.io.Serializable;
import java.util.Arrays;

public class equBranch implements Serializable, Comparable<equBranch>{
	String equID = "";
	double reliability;
	double cost; 
	double latency;

	public equBranch(double reli, double cost, double latency, String equID) {
		this.equID = equID;
		this.reliability = reli;
		this.cost = cost; 
		this.latency = latency;
	}
	
	public equBranch(double reli, double cost, double latency) {
		this.equID = "";
		this.reliability = reli;
		this.cost = cost; 
		this.latency = latency;
	}
	
	@Override
	public String toString() {
		return equID+": reliability = " + reliability + "; cost = "+ cost + "; latency = " + latency;
	}
	
	public equBranch seqExecute(equBranch node) {
		double resultReliability  = 1-(1-this.reliability)*(1-node.reliability);
		double resultCost = this.cost + (1-this.reliability)*node.cost;
		double resultLatency = this.latency+(1-this.reliability)*node.latency;
		return new equBranch(resultReliability, resultCost, resultLatency, "("+this.equID+"-"+node.equID+")");
	}

	public static equBranch paraExecute(equBranch nodes[]) {
		Arrays.sort(nodes);
		double errorRate = 1;
		double resultCost = 0;
		String resultID = nodes[0].equID;
		double resultLatency = nodes[0].reliability * nodes[0].latency;
		for(int i=0; i<nodes.length;i++) {
			errorRate *= 1-nodes[i].reliability;
			resultCost += nodes[i].cost;
			if(i!=0) {
				double possibility = 1;
				for(int j=0; j<i; j++) {
					possibility = possibility*(1-nodes[j].reliability);
				}
				resultLatency += possibility * nodes[i].latency;
				resultID += "*" + nodes[i].equID;
			}
		}
		
		return new equBranch(1-errorRate, resultCost, resultLatency, resultID);	
	}

	@Override
	public int compareTo(equBranch o) {
		// TODO Auto-generated method stub
	    if(this.latency > o.latency)
	        return 1;
	    else if (this.latency == o.latency)
	        return 0 ;
	    return -1 ;
		
	}
}