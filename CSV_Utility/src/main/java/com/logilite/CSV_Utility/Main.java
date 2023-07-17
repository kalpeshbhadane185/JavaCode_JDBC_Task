package com.logilite.CSV_Utility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class Main
{
	public static Map<Object, String> map = new LinkedHashMap<>();

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			for (String string : args)
			{
				String[] keyValue = string.split("=");
				String key = keyValue[0];
				String value = keyValue[1];
				map.put(key, value);
			}
			String utilityType = map.get("utility_type");
			switch (utilityType)
			{
				case "insert":
					InsertRecrdsInDB.insertRecords();
					break;
				case "merge":
					CsvMerge.csvMerge();
					break;
				default:
					System.out.println("Please provide correct utilityType, you are provided {" + utilityType + "}");
					break;
			}
		}
		else
		{
			System.out.println("Please follow this commands to run java Jar file");
			System.out.println("1. Insert records : java -jar filename.jar utility_type=insert filepath=path");
			System.out.println(
					"2. Simple merge : java -jar filename.jar utility_type=merge filepath1=path filepath2=path....");
			System.out.println(
					"3. Advanced Merge : java -jar filename.jar utility_type=merge filepath1=path filepath2=path... column_name=colname");
			return;

		}
	}
}
