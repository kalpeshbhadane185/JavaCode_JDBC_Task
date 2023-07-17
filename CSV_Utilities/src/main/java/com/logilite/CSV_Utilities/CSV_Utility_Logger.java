package com.logilite.CSV_Utilities;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CSV_Utility_Logger
{

	public static Logger logger = LogManager.getLogger(CSV_Utility_Logger.class);

	static
	{
		PropertyConfigurator.configure(
				System.getProperty("user.home") + System.getProperty("file.separator") 
				+ "/log4j.properties"); 
	}
}
