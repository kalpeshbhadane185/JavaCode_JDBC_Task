package com.logilite.CSV_Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Level;

public class CsvMerge
{
	public static BufferedReader			reader;
	public static BufferedWriter			writer;
	public static Map<Object, List<Object>>	mergedData			= new TreeMap<Object, List<Object>>();
	public static int						columnIndex			= -1;
	public static List<String>				commonRowHeader		= new ArrayList<String>();
	public static String					mergedColName		= null;
	public static String					outputFileName		= null;
	public static int						headerTotalLength	= 0;
	public static List<String>				filesList			= new ArrayList<String>();

	public static void csvMerge()
	{
		try
		{
			int filePathNumber = 1;
			while (true)
			{
				try
				{
					String filePath = App.map.get("filepath" + filePathNumber);
					File file = new File(filePath);
					if (!file.isFile())
					{
						throw new FileNotFoundException(
								"Please Provide correct FilePath " + filePathNumber + " path is " + filePath);
					}
					filesList.add(filePath);
					filePathNumber++;
				}
				catch (Exception e)
				{
					break;
				}
			}
			mergedColName = App.map.get("column_name"); 

			for (String filePath : filesList)
			{
				reader = new BufferedReader(new FileReader(filePath));
				readCSV();
			}
			writeDataInFile();
		}
		catch (IOException e)
		{
			CSV_Utility_Logger.logger.log(Level.ERROR, "IOException :: ", e);
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
				if (writer != null)
				{
					writer.close();
				}
			}
			catch (IOException e1)
			{
				CSV_Utility_Logger.logger.log(Level.ERROR, "IOException :: ", e1);
			}
		}
	}

	public static void readCSV()
	{
		try
		{
			findColumnIndex(reader.readLine().split(","));
			int keyCounter = 0;
			String row;
			while ((row = reader.readLine()) != null)
			{
				keyCounter += 1;
				String[] rowData = null;
				rowData = row.split(",");
				Object mergeColumnData = keyCounter;
				List<Object> existingData = new ArrayList<Object>();

				if (mergedColName != null)
				{
					mergeColumnData = rowData[columnIndex];
					mergeColumnData = dataTypeChecker(mergeColumnData);
				}

				existingData = mergedData.get(mergeColumnData);

				if (existingData == null)
				{
					existingData = new ArrayList<>();
					mergedData.put(mergeColumnData, existingData);
				}

				existingData = listInitialize(rowData, existingData);
			}
			headerTotalLength = commonRowHeader.size();
		}
		catch (IOException e)
		{
			CSV_Utility_Logger.logger.log(Level.ERROR, "IOException :: ", e);
		}
	}

	private static Object dataTypeChecker(Object mergeColumnData)
	{
		try
		{
			mergeColumnData = Double.parseDouble(mergeColumnData.toString());
		}
		catch (Exception e)
		{
			try
			{
				mergeColumnData = Integer.parseInt(mergeColumnData.toString());
			}
			catch (Exception e2)
			{
				return mergeColumnData;
			}
		}
		return mergeColumnData;
	}

	private static List<Object> listInitialize(String[] rowData, List<Object> listData)
	{
		for (int i = listData.size(); i < headerTotalLength; i++)
		{
			listData.add(null);
		}

		for (int i = 0; i < rowData.length; i++)
		{
			if (i != columnIndex)
			{
				listData.add(rowData[i]);
			}
		}
		return listData;
	}

	private static void findColumnIndex(String[] headers)
	{
		if (mergedColName != null)
		{
			boolean findColumn = Arrays.asList(headers).contains(mergedColName);
			if (!findColumn)
			{
				CSV_Utility_Logger.logger.info("Please provide correct column name which is present in all" + "file");
				System.exit(0);
			}
		}
		for (int i = 0; i < headers.length; i++)
		{
			if (headers[i].equalsIgnoreCase(mergedColName))
			{
				columnIndex = i;
			}
			else
			{
				commonRowHeader.add(headers[i]);
			}
		}
	}

	private static void writeDataInFile() throws IOException
	{
		outputFileName = filesList.get(0).replace(".csv", "_Csv_utilityMerge_2.0.csv");

		if (outputFileName != null)
		{
			writer = new BufferedWriter(new FileWriter(outputFileName));
			if (mergedColName != null)
			{
				writer.write(mergedColName + ",");
			}
			for (String string : commonRowHeader)
			{
				writer.write(string + ",");
			}
			writer.newLine();
			for (Entry<Object, List<Object>> data : mergedData.entrySet())
			{
				if (mergedColName != null)
				{
					Object key = data.getKey();
					writer.write(key + ",");
				}
				List<Object> values = data.getValue();
				for (Object value : values)
				{
					if (value == null)
					{
						writer.write("" + ",");
					}
					else
					{
						writer.write(value + ",");
					}
				}
				writer.newLine();
			}
			CSV_Utility_Logger.logger.info(" file has successfully merge at this path\n" + outputFileName);
		}
		else
		{
			CSV_Utility_Logger.logger.info("outputfilename is null please check it first");
		}
	}
	// BigDecimal bigDecimal = new BigDecimal(columnIndex);
}
