import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DTree {
	private static int numVars;
	private static ArrayList<ArrayList<Character>> data = new ArrayList<ArrayList<Character>>();
	private static double dependentEntropy;
	public static Set<Integer> used = new HashSet<Integer>();

	public static void main(String[] args) {
		readData(args[0]);
		System.out.println("Number of Independent variables: " + numVars);
		buildTree(data, 1);
	}

	/**
	 * 
	 * @param data
	 * @param it
	 * 
	 *            Recursively splits the node based on entropy gained and builds
	 *            a decision tree
	 */
	private static void buildTree(ArrayList<ArrayList<Character>> data, int it) {
		dependentEntropy = calculateYEntropy(data);
		int varToSplit = determineBestSplit(data, dependentEntropy);
		used.add(varToSplit);
		ArrayList<ArrayList<Character>> zData = new ArrayList<ArrayList<Character>>();
		ArrayList<ArrayList<Character>> oData = new ArrayList<ArrayList<Character>>();
		Iterator<ArrayList<Character>> dIt = data.iterator();
		double ooCount = 0, ozCount = 0, zzCount = 0, zoCount = 0;
		while (dIt.hasNext()) {
			ArrayList<Character> record = dIt.next();
			if (record.get(varToSplit) == '0') {
				if (record.get(numVars) == '0') {
					zzCount++;
				} else {
					zoCount++;
				}
				zData.add(record);
			} else {
				if (record.get(numVars) == '0') {
					ozCount++;
				} else {
					ooCount++;
				}
				oData.add(record);
			}

		}
		char zDom = '1', oDom = '1';
		if (zzCount > zoCount) {
			zDom = '0';
		}
		if (ozCount > ooCount) {
			oDom = '0';
		}
		System.out.println("Node at level: " + it
				+ " ******************************");
		System.out.println("\t Split by variable: " + varToSplit);
		System.out.println("\t Left subnode has " + zData.size()
				+ " elements and the most common result is " + zDom);
		System.out.println("\t Right subnode has " + oData.size()
				+ " elements and the most common result is " + oDom);
		if (used.size() >= numVars || zData.size() == 0 || oData.size() == 0) {
			return;
		}
		buildTree(zData, it + 1);
		buildTree(oData, it + 1);

	}

	/**
	 * 
	 * @param dataset
	 * @return entropy of the dependent variable in the dataset
	 */
	private static double calculateYEntropy(
			ArrayList<ArrayList<Character>> dataset) {
		Iterator<ArrayList<Character>> depIt = dataset.iterator();
		double totCount = 0, zCount = 0, oCount = 0;
		while (depIt.hasNext()) {
			ArrayList<Character> temp = depIt.next();
			Character character = temp.get(numVars);
			if (character == '0') {
				zCount++;
			} else {
				oCount++;
			}
			totCount++;
		}
		double toReturn = (-1)
				* (zCount * Math.log(zCount / totCount) + oCount
						* Math.log(oCount / totCount)) / totCount;
		return toReturn;
	}

	/**
	 * 
	 * @param data
	 * @param dependentEntropy
	 * @return the variable which gives maximum information gain
	 */
	private static int determineBestSplit(ArrayList<ArrayList<Character>> data,
			double dependentEntropy) {
		double curMaxEntropyGain = Double.MIN_VALUE;
		int toReturn = 0;
		for (int i = 0; i < numVars; i++) {
			if (!used.contains(i)) {
				double ooCount = 0, ozCount = 0, zzCount = 0, zoCount = 0, oCount = 0, zCount = 0, totCount = 0;
				Iterator<ArrayList<Character>> dIt = data.iterator();
				while (dIt.hasNext()) {
					ArrayList<Character> record = dIt.next();
					Character curValue = record.get(i);
					Character dep = record.get(numVars);
					if (curValue == '0') {
						if (dep == '0') {
							zzCount++;
						} else {
							zoCount++;
						}
						zCount++;
					} else {
						if (dep == '0') {
							ozCount++;
						} else {
							ooCount++;
						}
						oCount++;
					}
					totCount++;
				}
				double zEntropy = (-1)
						* (zzCount * Math.log(zzCount / zCount) + zoCount
								* Math.log(zoCount / zCount)) / zCount;
				double oEntropy = (-1)
						* (ozCount * Math.log(ozCount / oCount) + ooCount
								* Math.log(ooCount / oCount)) / oCount;
				double curEntropy = (zCount * zEntropy + oCount * oEntropy)
						/ totCount;
				double curEntropyGain = dependentEntropy - curEntropy;
				if (curEntropyGain > curMaxEntropyGain) {
					curMaxEntropyGain = curEntropyGain;
					toReturn = i;
				}
			}
		}

		return toReturn;
	}

	/**
	 * 
	 * @param input
	 *            Read data and store in local data structures
	 */
	private static void readData(String input) {
		try {
			File f = new File(input);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				String[] parts = line.split(";");
				numVars = parts.length - 1;
				ArrayList<Character> temp = new ArrayList<Character>();
				for (int i = 0; i < parts.length; i++) {
					temp.add(parts[i].charAt(0));
				}
				data.add(temp);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
