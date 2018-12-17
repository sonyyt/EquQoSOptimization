package equivalentProj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

class equAL{
	ArrayList<equBranch> equNodes = null;
	public equAL(int numberOfNodes){
		equNodes = new ArrayList<equBranch>();
		for(int i = 0; i<numberOfNodes; i++ ) {
			char tmp = (char)(97+i);
			String str = Character.toString(tmp);
			double cost = generateCost();
			double latency = generateLatency();
			double reliability = generateReliability();
			equNodes.add(new equBranch(reliability, cost, latency, str));
		}
	}
	
	
	public equBranch getNodeByEquID(String targetEquID) {
		targetEquID = targetEquID.trim();
		//System.out.println("targetEquID"+targetEquID);
		for(equBranch node : equNodes) {
			//System.out.println("nodes ids"+node.equID);
			if(node.equID.equals(targetEquID)) {
				return node;
			}
		}
		return null;
	}
	
	public boolean saveToFile(String fileName) {
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(equNodes);
			oos.close();
			return true;
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean restoreFromFile(String fileName) {
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			this.equNodes = (ArrayList) ois.readObject();
			ois.close();
			return true;
		} catch ( IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String toString() {
		String ret = "";
		for(equBranch node : equNodes) {
			ret += node.toString()+"\n";
		}
		return ret;
	}
	
	private double generateCost() {
		Random rand = new Random(); 
		return rand.nextInt(20); 
	}
	
	private double generateLatency() {
		Random rand = new Random(); 
		return rand.nextInt(20); 
	}
	
	private double generateReliability() {
		Random rand = new Random(); 
		return (double)rand.nextInt(1000)/1000; 
	}
}