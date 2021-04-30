package com.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Routes {
	
	private static HashMap<String, String> secMap = new HashMap<String, String>();
	private static HashMap<String, String> directMap = new HashMap<String, String>();

	private static void calRoutes(HashMap<String, String> mainMap) {

		Scanner frstStVal = new Scanner(System.in);
		System.out.print("What station are you getting on the train ? : ");
		String infVal = frstStVal.nextLine().toUpperCase();

		Scanner secStVal = new Scanner(System.in);
		System.out.print("What station are you getting off the train ? : ");
		String insVal = secStVal.nextLine().toUpperCase();

		if (Pattern.matches("[a-zA-Z]+", infVal) && Pattern.matches("[a-zA-Z]+", insVal)) {

			if (null != infVal && null != insVal || !insVal.isEmpty() && !infVal.isEmpty()) {
				
				Boolean directFlag = false;
				String key = infVal.concat(insVal);
				
				for (Map.Entry<String, String> entry : mainMap.entrySet()) {
					
					if (entry.getKey().equals(key)) {
						directFlag = true;
						directMap.put(entry.getKey(), entry.getValue());
						// System.out.println("directMap"+directMap);
					}
				}

				if (!directFlag) {
					// First check
					HashMap<String, String> comMap2 = new HashMap<String, String>();
					
					for (Map.Entry<String, String> entry : mainMap.entrySet()) {
						
						if (entry.getKey().substring(0, 1).equals(infVal)) {
							comMap2.put(entry.getKey(), entry.getValue());
							break;
						}
					}
					// System.out.println("First check Map" + comMap2);
					if (!comMap2.isEmpty()) {
						Boolean setFlag = false;

						Map<String, String> oneMap = checkCom(mainMap, comMap2, infVal, insVal, setFlag);
						
						if (oneMap.isEmpty()) {
							System.out.println("");
							System.out.println("No routes from " + infVal + " to " + insVal);

						} else {
							
							int mintes = 0;
							int secMintes = 0;
							
							for (Map.Entry<String, String> entryOneMap : oneMap.entrySet()) {

								mintes = mintes + Integer.parseInt(entryOneMap.getValue());

							}
							if (mintes > 0) {

								// System.out.println("First minutes "+ mintes);
								setFlag = true;

								Map<String, String> secMap = checkComTwo(mainMap, comMap2, infVal, insVal, setFlag);
								// System.out.println("sec "+ secMap);
								for (Map.Entry<String, String> entrysecValue : secMap.entrySet()) {

									secMintes = secMintes + Integer.parseInt(entrysecValue.getValue());
								}

							}

							if (mintes > secMintes) {

								int fstops = secMap.values().size() - 1;
								printResult(fstops, infVal, insVal, secMintes);

							} else {
								
								int stops = oneMap.values().size() - 1;
								printResult(stops, infVal, insVal, mintes);
							}

						}
					}
				} else {

					int directMapMintes = 0;
					int secdMintes = 0;

					HashMap<String, String> comMap2d = new HashMap<String, String>();
					
					for (Map.Entry<String, String> entry : mainMap.entrySet()) {
						
						if (entry.getKey().substring(0, 1).equals(infVal)) {
							comMap2d.put(entry.getKey(), entry.getValue());
							break;
						}
					}

					for (Map.Entry<String, String> entryOneMap : directMap.entrySet()) {

						directMapMintes = directMapMintes + Integer.parseInt(entryOneMap.getValue());

					}
					if (directMapMintes > 0) {

						// System.out.println("First minutes "+ directMapMintes);
						Boolean setDirFlag = true;

						Map<String, String> secMap = checkComTwo(mainMap, comMap2d, infVal, insVal, setDirFlag);
						// System.out.println("sec "+ secMap);
						for (Map.Entry<String, String> entrysecValue : secMap.entrySet()) {

							secdMintes = secdMintes + Integer.parseInt(entrysecValue.getValue());
						}

					}

					if (directMapMintes > secdMintes) {
						
						int fstops = secMap.values().size() - 1;
						printResult(fstops, infVal, insVal, secdMintes);
						
					} else {
						
						int stops = directMap.values().size() - 1;
						printResult(stops, infVal, insVal, directMapMintes);
						
					}

				}
			}

		} else {
			System.out.println("");
			System.out.println("Please enter correct stations Only A-Z");
			calRoutes(mainMap);
		}

	}

	private static void printResult(int stops, String infVal, String insVal, int mintes) {
		if (stops == 1) {
			System.out.println("");
			System.out.println("Your trip from " + infVal + " to " + insVal + " includes " + stops
					+ " stop and will take " + mintes + " minutes");
		} else {
			System.out.println("");
			System.out.println("Your trip from " + infVal + " to " + insVal + " includes " + stops
					+ " stops and will take " + mintes + " minutes");
		}

	}

	private static HashMap<String, String> dataLoad() throws FileNotFoundException {

		Scanner readFile = new Scanner(new File("C:\\Rajesh\\Project\\Routes\\routes.csv"));

		HashMap<String, String> comMap = new HashMap<String, String>();

		while (readFile.hasNext()) {

			String string = readFile.next();

			String firstKey = string.substring(0, 3).replace(",", "");
			String firstVal = string.substring(4, string.length());

			comMap.put(firstKey, firstVal);
			// System.out.println(string);

		}
		// System.out.println(comMap);
		calRoutes(comMap);
		return comMap;
	}

	private static Map<String, String> checkComTwo(HashMap<String, String> mainMap, HashMap<String, String> ittMap, String infVal, String insVal, Boolean setFlag) {

		HashMap<String, String> valuets = new HashMap<String, String>();
		
		for (Map.Entry<String, String> entryittMap : ittMap.entrySet()) {
			
			for (Map.Entry<String, String> entrymainMap : mainMap.entrySet()) {

				if (setFlag) {
					
					if (entryittMap.getKey().substring(1, 2).equals(entrymainMap.getKey().substring(0, 1))) {
						
						secMap.put(entryittMap.getKey(), entryittMap.getValue());
						secMap.put(entrymainMap.getKey(), entrymainMap.getValue());
						valuets.put(entrymainMap.getKey(), entrymainMap.getValue());
						
						return checkComTwo(mainMap, valuets, infVal, insVal, setFlag);
						
					}
				}
			}
		}
		
		return secMap;

	}

	private static Map<String, String> checkCom(HashMap<String, String> mainMap, HashMap<String, String> ittMap, String infVal, String insVal, Boolean setFlag) {
		
		HashMap<String, String> value = new HashMap<String, String>();
		HashMap<String, String> valuet = new HashMap<String, String>();
		Boolean flag = false;
		
		for (Map.Entry<String, String> entryittMap : ittMap.entrySet()) {
			
			for (Map.Entry<String, String> entrymainMap : mainMap.entrySet()) {

				if (setFlag) {
					if (entryittMap.getKey().substring(1, 2).equals(entrymainMap.getKey().substring(0, 1))) {
						
						secMap.put(entryittMap.getKey(), entryittMap.getValue());
						secMap.put(entrymainMap.getKey(), entrymainMap.getValue());
						valuet.put(entrymainMap.getKey(), entrymainMap.getValue());
						
						return checkComTwo(mainMap, valuet, infVal, insVal, setFlag);
					}

					// System.out.println("setFlag");
				} else {

					if (entryittMap.getKey().substring(1, 2).equals(entrymainMap.getKey().substring(0, 1))) {
						
						value.put(entrymainMap.getKey(), entrymainMap.getValue());
						
						if (entrymainMap.getKey().equals(infVal + insVal)) {
							
							for (Map.Entry<String, String> entrymsainMap : mainMap.entrySet()) {
								
								if (entrymsainMap.getKey().equals(entrymainMap.getKey())) {
									
									value.put(entrymsainMap.getKey(), entrymsainMap.getValue());
									value.put(entryittMap.getKey(), entryittMap.getValue());
									
									flag = true;
									break;
								}
							}
						} else if (entrymainMap.getKey().substring(1, 2).equals(insVal)) {

							for (Map.Entry<String, String> entrymsainMap : mainMap.entrySet()) {
								if (entrymsainMap.getKey().equals(entrymainMap.getKey())) {
									value.put(entrymsainMap.getKey(), entrymsainMap.getValue());
									value.put(entryittMap.getKey(), entryittMap.getValue());
									flag = true;
									break;
								}
							}

						}
						// System.out.println("second " + value);
						if (!flag) {
							
							String chkey = entrymainMap.getKey().substring(1, 2) + insVal;
							
							for (Map.Entry<String, String> entrymsainMap : mainMap.entrySet()) {
								
								if (entrymsainMap.getKey().equals(chkey)) {
									
									value.put(entrymsainMap.getKey(), entrymsainMap.getValue());
									value.put(entryittMap.getKey(), entryittMap.getValue());
									
									flag = true;
									break;
								}
							}
						}
						// System.out.println(value);
						if (!flag) {
							
							return checkCom(mainMap, value, infVal, insVal, setFlag);
							
						}

					}

				}

				if (flag) {
					
					break;
				}
			}
		}
		if (!value.isEmpty() && !flag) {

			return checkCom(mainMap, value, infVal, insVal, setFlag);
		}
		return value;
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		dataLoad();
		
	}

}
