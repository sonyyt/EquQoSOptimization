package equivalentProj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Calculator {
	static equAL equ;
	TreeNode equTree;
	public Calculator(String equation) {
		
		equTree = new TreeNode(equation);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @author JiangQifan
	 * @since 2012/3/19
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		equ  = new equAL(4);
		//equ.saveToFile("/data/equivalentProj/4equ");
		equ.restoreFromFile("/data/equivalentProj/4equ");
		
		
//		Calculator test = new Calculator("a*b*c");
//		equBranch testb = test.calculate();
//		System.out.println(testb);
//		
//		Calculator test1 = new Calculator("c*a*b");
//		equBranch testb1 = test1.calculate();
//		System.out.println(testb1);
//		
//		System.exit(1);
		
		double latencyThreshold = 3.3;	
		double reliabilityThreshod = 0.998;
		double minimalCost = 100;
		equBranch selected = null;
		
		double minLatency = 100;
		equBranch minLatencyBranch = null;
		double minCost = 100;
		equBranch minCostBranch = null;
		double maxReliability = 0;
		equBranch maxReliabilityBranch = null;
		
		System.out.println(equ);
		
        try {

            File f = new File("/data/equivalentProj/pySource/4.equations");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            System.out.println("Read All Equations");

            while ((readLine = b.readLine()) != null) {
            	//System.out.println(readLine);
        		Calculator ca = new Calculator(readLine.replaceAll(" ",""));
        		equBranch tmp = ca.calculate();
        		if(tmp.latency <= latencyThreshold && tmp.reliability >= reliabilityThreshod) {
        			if(tmp.cost < minimalCost) {
        				minimalCost = tmp.cost;
        				selected = tmp;
        			}
        		}
        		
        		if(tmp.latency < minLatency) {
        			minLatency = tmp.latency;
        			minLatencyBranch = tmp;
        		}
        		if(tmp.cost < minCost) {
        			minCost = tmp.cost;
        			minCostBranch = tmp;
        		}
        		
        		if(tmp.reliability > maxReliability) {
        			maxReliability = tmp.reliability;
        			maxReliabilityBranch = tmp;
        		}
            }
            if(selected == null) {
            	System.out.println("Cannot Find Orchestration that fit the requirement");
        		System.out.println("Required Latency:" + latencyThreshold + "; Best Latency:" + minLatency + " | with:" + minLatencyBranch);
        		System.out.println("Required Reliability:" + reliabilityThreshod + "; Best reliability:" + maxReliability + " | with:" + maxReliabilityBranch);
        		System.out.println("Best Cost:" + minCost + " | with:" + minCostBranch);
            }else {
            	System.out.println(selected);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
 
	public equBranch calculate() {
		return equTree.calculate();
	}
 
	class TreeNode {
 
		equBranch equNode;
		char operation;
		TreeNode left;
		TreeNode right;
		ArrayList<TreeNode> branches;
 
		/*
		 * recursively construct the tree
		 */
		public TreeNode(String expression) {
			char[] exc = toCharArrayTrimOutParenthes(expression);
			if (exc == null) {
				return;
			}
			exc = syntaxCheck(exc);
			int length = exc.length;
 
			int index = 0;
			ArrayList<Integer> indexs = new ArrayList<Integer>();
 
			/* How does it process parentheses?
			 * 1. for a given equation, first make sure that the operator is not in any paretheses. 
			 * 2. for '*', it may be the operator of the equation, or may not be; (the * on the left)
			 * 
			 * assumption: if the final operation is '*', no '-' will be spotted. 
			 * 
			 * 3. for '-', it must be the last operator.  ( the - on the right)
			 * 
			 * why from right to left?
			*/
			if (hasOperation(exc)) {
				int parenthes = 0;
 
				for (int i = length - 1; i >= 0; i--) {
 
					if (exc[i] == '(') {
						parenthes--;
					} else if (exc[i] == ')') {
						parenthes++;
					}
 
					if (parenthes > 0) {
						continue;
					}
 
					if (exc[i] == '*') {
						index = i;
						indexs.add(i);
					} else if (exc[i] == '-') {
						index = i;
						break;
					}
 
				}
				if (isOperation(exc[index])) {
					operation = exc[index];
				}
				if(operation == '-') {
					StringBuilder sbleft = new StringBuilder();
					StringBuilder sbright = new StringBuilder();
	 
					for (int i = 0; i < index; i++) {
						sbleft.append(exc[i]);
					}
					for (int i = index + 1; i < length; i++) {
						sbright.append(exc[i]);
					}
					left = new TreeNode(sbleft.toString());
					right = new TreeNode(sbright.toString());
				}else {
					branches = new ArrayList<TreeNode>();
					//indexs.add(0);
					//indexs.add(length);
					Collections.sort(indexs);
					//System.out.println(indexs);
					//System.out.println(exc);
					//System.out.println("processing first");
					StringBuilder firstSonTree = new StringBuilder();
					for (int i = 0; i < indexs.get(0); i++) {
						firstSonTree.append(exc[i]);
					}
					//System.out.println("sonTree front"+firstSonTree);
					branches.add(new TreeNode(firstSonTree.toString()));
					StringBuilder lastSonTree = new StringBuilder();
					for (int i = indexs.get(indexs.size()-1) + 1; i < length; i++) {	
						lastSonTree.append(exc[i]);
					}
					//System.out.println("sonTree end"+lastSonTree);
					branches.add(new TreeNode(lastSonTree.toString()));
					
					//System.out.println("processing indexs");
					if(indexs.size()!=1) {
						for(int i = 0; i<indexs.size()-1;i++) {
							//System.out.println("i"+i);
							StringBuilder sonTree = new StringBuilder();
							for(int j = indexs.get(i)+1; j<indexs.get(i+1);j++) {				
								//System.out.println("j"+j);
								sonTree.append(exc[j]);
							}
							// have problem here!
							//System.out.println("Loop sonTree"+sonTree);
							
							branches.add(new TreeNode(sonTree.toString()));
						}

					}
				}
 
			} else {
				StringBuilder value = new StringBuilder();
				//System.out.println(exc);
				value.append(exc);
				//System.out.println("new tree node"+value);
				equNode = equ.getNodeByEquID(value.toString());				
			}
		}
 
		/*
		 * check the expression's syntax if there is two or more continuous
		 * operations,print out syntax error add '0' before the '-' if it's a
		 * negative
		 */
		public char[] syntaxCheck(char[] cArray) {
			boolean flag = false;
			if (isOperation(cArray[0])) {
				char[] checkedArray = new char[cArray.length + 1];
				checkedArray[0] = '0';
				for (int i = 0; i < cArray.length; i++) {
					checkedArray[i + 1] = cArray[i];
				}
				cArray = checkedArray;
			}
			for (int i = 0; i < cArray.length; i++) {
				if (isOperation(cArray[i])) {
					if (flag == true) {
						System.out.println("syntaxError");
					}
					flag = true;
				} else {
					flag = false;
				}
			}
 
			return cArray;
		}
 
		/*
		 * is there operations in the char array if there is,return true. if
		 * not,return false
		 */
		public boolean hasOperation(char[] cArray) {
			for (int i = 0; i < cArray.length; i++) {
				if (isOperation(cArray[i])) {
					return true;
				}
 
			}
			return false;
		}
 
		public boolean isOperation(char c) {
			return (c == '-' || c == '*');
 
		}
 
		/*
		 * trim the out most useless parentheses and return a char array
		 */
		public char[] toCharArrayTrimOutParenthes(String src) {
 
			if (src.length() == 0) {
				return null;
			}
			String result = src;
			while (result.charAt(0) == '('
					&& result.charAt(result.length() - 1) == ')') {
				int parenthes = 0;
				for (int i = 0; i < result.length() - 1; i++) {
					if (result.charAt(i) == '(') {
						parenthes++;
					} else if (result.charAt(i) == ')') {
						parenthes--;
					}
					if (parenthes == 0) {
						return result.toCharArray();
					}
				}
				result = result.substring(1, result.length() - 1);
 
			}
 
			return result.toCharArray();
		}
 
		// recursively calculate
		public equBranch calculate() {
			equBranch result = null;
			if (operation!='-' && operation!='*') {
				result = equNode;
			} else {
				switch (operation) {
				case '-':
					equBranch leftResult = left.calculate();
					equBranch rightResult = right.calculate();
					result = leftResult.seqExecute(rightResult);
					break;
				case '*':
					equBranch[] bs = new equBranch[branches.size()];
					int i = 0;
					for(TreeNode nodes:branches) {
						bs[i] =  nodes.calculate();
						i++;
					}
					result = equBranch.paraExecute(bs);
					break;
				default:
					break;
				}
			}
			return result;
		}
	}
}