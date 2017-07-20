package cis262TM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static String l = "L";
	public static String r = "R";
	
	static List<String> states;
	static List<String> alphabet;
	
	static String output = "";
	
	//maps a state to a map of its rules from each alphabet
	static Map<Integer, HashMap<Integer, Rule>> map = new HashMap<Integer, HashMap<Integer, Rule>>();
	
	static String input;
	
	public static void main(String[] args) {
		readTM();
		readInput();
		printTM();
		output += "\n\n\n";
		runTM();
		try{
			Writer out = new FileWriter("output.txt");
			out.write(output);
			out.flush();
			out.close();
		} catch (Exception e){
			System.out.println("An exception occurred while writing the file");
		}
		
	}
	
	public static void readTM() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("inputTM.txt"));
			String line = in.readLine();
			while (line != null) {
				String arrParts[] = line.split(",");
				if (arrParts[0].equals("states")) {
					//update states
					states = new ArrayList<String>();
					for (int i = 1; i < arrParts.length; i++) {
						states.add(arrParts[i]);
					}
					
				} else if (arrParts[0].equals("alphabet")) {
					//create an ArrayList and add each of the parts of the line to it
					alphabet = new ArrayList<String>();
					for (int i = 1; i < arrParts.length; i++) {
						if (!arrParts[i].equals("delta")) {
							alphabet.add(arrParts[i]);
						} else {
							alphabet.add("D");
						}
					}
					
				} else {
					//create a new Rule and add it to the appropriate place in the map
					int initState = states.indexOf(arrParts[0]);
					String initChar = arrParts[1];
					if (initChar.equals("delta")) {
							initChar = "D";
					}
					int initC = alphabet.indexOf(initChar);
					int nextState = states.indexOf(arrParts[4]);
					String nxtChar = arrParts[2];
					if (nxtChar.equals("delta")) {
							nxtChar = "D";
					}
					int nextC = alphabet.indexOf(nxtChar);
					
					Rule r = new Rule(initState, initC, nextC, arrParts[3], nextState);
					if (map.containsKey(r.getInitState())) {
						HashMap<Integer, Rule> h = map.get(r.getInitState());
						h.put(r.getInitC(), r);
					} else {
						HashMap<Integer, Rule> h = new HashMap<Integer, Rule>();
						h.put(r.getInitC(), r);
						map.put(r.getInitState(), h);
					}
				}
				line = in.readLine();
			}
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("inputTM.txt not found!");
		} catch (IOException e) {
			System.out.println("An error occurred while reading inputTM.txt");
		} catch (Exception e) {
			System.out.println("Something went wrong when reading inputTM.txt");
		}
	}
	
	public static void readInput() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("input.txt"));
			String line = in.readLine();
			if (line != null) {
				input = line;
			} else {
				in.close();
				throw new IOException();
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("input.txt not found!");
		} catch (IOException e) {
			System.out.println("An error occurred while reading input.txt");
		} catch (Exception e) {
			System.out.println("Something went wrong when reading input.txt");
		}
	}
	
	public static void printTM() {
		output += "Input Turing Machine: \n";
		for (Integer state : map.keySet()) {
			HashMap<Integer, Rule> hm = map.get(state);
			for (Integer alpha : hm.keySet()) {
				Rule r = hm.get(alpha);
				String rule = "(" + states.get(r.getInitState()) + ", " + alphabet.get(r.getInitC()) + ", " + 
						alphabet.get(r.getNextC()) + ", " + r.getDir() + ", " + states.get(r.getNextState()) + ")";
				//rule = rule.replace("delta", "\u0394");
				output += "\n" + rule;
			}
		}
	}

	public static String removeB() {
		char[] chars = input.toCharArray();
		String in2 = "";
		for (char c : chars) {
			if (c != 'B') {
				in2 += c;
			}
		}
		return in2;
	}
	
	public static void editInput(String str, int ind) {
		char[] chars = input.toCharArray();
		input = "";
		if (str == "delta") {
			str = "D";
		}
		for (int i = 0; i< chars.length; i++) {
			if (i != ind) {
				input += chars[i];
			} else {
				input += str;
			}
		}
	}
	
	public static void runTM() {
		int currState = 0;
		int index = 0;
		boolean finished = false;
		output += "Starting TM.... \nOriginal input\n" + input;
		
		while (!finished) {
			char c; 
			if (index >= input.length()) {
				input += "B";
				index = input.length() - 1;
			} else if (index < 0){
				input = "B" + input;
				index = 0;
			}
			c = input.charAt(index);
			
			int alphaC = alphabet.indexOf(String.valueOf(c));
			Rule r = map.get(currState).get(alphaC);
			currState = r.getNextState();
			editInput(alphabet.get(r.getNextC()), index);
			if (r.getDir().equals("R")) {
				index++;
			} else {
				index--;
			}
			output += "\n" + input;
			String input2 = removeB();
			if ((input2.indexOf('0') == 0 || input2.indexOf('1') == 0)) {
				finished = true;
			}
		}
		input = removeB();
		if (input.equals("1")) {
			output += "\n" + "This string was accepted!";
		} else {
			output += "\n" + "This string was rejected :(";
		}
	}
}
