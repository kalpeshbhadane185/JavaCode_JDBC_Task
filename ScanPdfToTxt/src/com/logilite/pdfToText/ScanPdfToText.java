package com.logilite.pdfToText;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ScanPdfToText
{

	static Scanner				scanner				= new Scanner(System.in);
	public static Properties	properties			= new Properties();
	public static String		all_files_path		= null;
	public static String		pngExtentension		= ".png";
	public static String		pdfExtentension		= ".pdf";
	public static String		txtExtension		= ".txt";
	public static String		bothExtensionName	= "pdf and txt";
	public static String		defaultOutPutPath	= null;
	public static List<String>	fileList			= new ArrayList<>();
	public static int			maxDepthFromUser	= 0;
	public static Set<String>	uniqueFiles			= new HashSet<>();
	public static List<String>	allFileList			= new ArrayList<>();
	public static List<String>	pdfFileList			= new ArrayList<>();
	public static List<String>	pngFileList			= new ArrayList<>();

	public static void main(String[] args)
	{
		boolean loadProperties = loadProperties();
		if (loadProperties == true)
		{
			choiceForUser();
		}
	}

	private static void choiceForUser()
	{
		try
		{
			MyLogger.myLoggerr.info("Select any Option from following list\n" + "1. Convert PDF to txt \n"
					+ "2. Convert png to txt\n" + "3. Convert all");

			int choice = scanner.nextInt();
			switch (choice)
			{
				case 1:
					pdfToText();
					break;
				case 2:
					pngToText();
					break;
				case 3:
					allConversion();
					break;
				default:
					MyLogger.myLoggerr.info("Wrong Input Please Enter Correct Number");
					break;
			}
		}
		catch (Exception e)
		{
			MyLogger.myLoggerr.log(Level.WARN, "Exception ::", e);
		}
	}

	public static void allConversion()
	{
		try
		{
			fileList = setFileNameFromUser(fileList, bothExtensionName);

			boolean checkFilesInDirectory = checkFilesInDirectory(bothExtensionName);

			if (checkFilesInDirectory == true)
			{
				if (fileList.size() > 0)
				{
					convertUserEnteredFiles(fileList, allFileList, bothExtensionName);
				}
				else
				{
					convertAllFiles(allFileList);
				}
			}
		}
		catch (Exception e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "IOException ::", e);
		}

	}

	public static boolean loadProperties()
	{
		try
		{
			properties.load(new FileInputStream("config.properties"));

			all_files_path = properties.getProperty("all_files_path");

			if (!all_files_path.endsWith(System.getProperty("file.separator")))
			{
				all_files_path = all_files_path + System.getProperty("file.separator");
			}

			File folderCheck = new File(all_files_path);

			if (folderCheck.isDirectory() == false)
			{
				Scanner sc = new Scanner(System.in);
				MyLogger.myLoggerr.info("Invalid Folder Path Enter 0 To Continue Used For Default Path");
				if (sc.nextInt() == 0)
				{
					all_files_path = properties.getProperty("default_all_files_path");
				}
				else
				{
					MyLogger.myLoggerr.info("Please Provide Correct Path");
					return false;
				}
			}
			String depth = properties.getProperty("depthFromUser");

			maxDepthFromUser = Integer.parseInt(depth);
		}
		catch (IOException e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "IOException ::", e);
		}
		return true;
	}

	@SuppressWarnings("resource")
	public static void pdfToText()
	{
		try
		{
			fileList = setFileNameFromUser(fileList, pdfExtentension);

			boolean checkFilesInDirectory = checkFilesInDirectory(pdfExtentension);

			if (checkFilesInDirectory == true)
			{
				if (fileList.size() > 0)
				{
					convertUserEnteredFiles(fileList, pdfFileList, pdfExtentension);
				}
				else
				{
					convertAllFiles(pdfFileList);
				}
			}
		}
		catch (Exception e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "Exception ::", e);
		}
	}

	public static void pngToText()
	{
		try
		{
			fileList = setFileNameFromUser(fileList, pngExtentension);

			boolean checkFilesInDirectory = checkFilesInDirectory(pngExtentension);

			if (checkFilesInDirectory == true)
			{
				if (fileList.size() > 0)
				{
					convertUserEnteredFiles(fileList, pngFileList, pngExtentension);
				}
				else
				{
					convertAllFiles(pngFileList);
				}
			}
		}
		catch (Exception e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "Exception ::", e);
		}
	}

	@SuppressWarnings("resource")
	public static List<String> setFileNameFromUser(List<String> fileList, String fileExtension)
	{
		MyLogger.myLoggerr.info("Please Enter The File Name or 0 to convert all files and also for Exit");

		String fileNameFromUser = "";

		while (true)
		{
			Scanner sc = new Scanner(System.in);
			fileNameFromUser = sc.next();

			if (fileNameFromUser.equalsIgnoreCase("0"))
			{
				break;
			}
			else if (fileNameFromUser.length() > 0)
			{
				if (fileNameFromUser.contains(System.getProperty("file.separator")))
				{
					all_files_path = setFolderPath(fileNameFromUser) + System.getProperty("file.separator");

					for (int i = fileNameFromUser.length() - 1; i >= 0; i--)
					{
						if (fileNameFromUser.charAt(i) == System.getProperty("file.separator").charAt(0))
						{
							fileNameFromUser = fileNameFromUser.substring(i + 1, fileNameFromUser.length());
							break;
						}
					}
				}

				if ((fileNameFromUser.toLowerCase().endsWith(pdfExtentension)) && fileExtension == pdfExtentension)
				{
					fileList.add(fileNameFromUser);
					return fileList;
				}
				else if ((fileNameFromUser.toLowerCase().endsWith(pngExtentension)) && fileExtension == pngExtentension)
				{
					fileList.add(fileNameFromUser);
					return fileList;
				}
				else if ((fileNameFromUser.toLowerCase().endsWith(pdfExtentension)
						|| fileNameFromUser.toLowerCase().endsWith(pngExtentension))
						&& fileExtension == bothExtensionName)
				{
					fileList.add(fileNameFromUser);
					return fileList;
				}
				else
				{
					MyLogger.myLoggerr.info("Please Enter Valid File Name");
					continue;
				}
			}
			sc.close();
		}
		return fileList;
	}

	private static String setFolderPath(String fileNameFromUser)
	{

		for (int i = fileNameFromUser.length() - 1; i >= 0; i--)
		{
			if (fileNameFromUser.charAt(i) == System.getProperty("file.separator").charAt(0))
			{
				all_files_path = all_files_path + fileNameFromUser.substring(0, i);
				break;
			}
		}
		return all_files_path;
	}

	private static String createFileName(String fileNameFromUser)
	{
		for (int i = fileNameFromUser.length() - 1; i >= 0; i--)
		{
			if (fileNameFromUser.charAt(i) == System.getProperty("file.separator").charAt(0))
			{
				fileNameFromUser = fileNameFromUser.substring(i + 1, fileNameFromUser.length());
				break;
			}
		}
		return fileNameFromUser;
	}

	public static void convertUserEnteredFiles(List<String> filesList, List<String> listpdf, String fileExtension)
			throws Exception
	{
		String fileName = "";
		int validFilePresentOrNot = 0;

		for (String fileNameFromList : filesList)
		{
			for (String listname : listpdf)
			{
				fileName = createFileName(listname);
				if (fileName.equalsIgnoreCase(fileNameFromList))
				{
					validFilePresentOrNot++;

					if (fileName.endsWith(pdfExtentension))
					{
						pdfFileProcessing(fileName, listname);
					}
					else if (fileName.endsWith(pngExtentension))
					{
						pngFileProcessing(fileName, listname);
					}
				}
			}
		}

		if (validFilePresentOrNot == 0)
		{
			MyLogger.myLoggerr
					.info("This File Are Not Present Please Enter Valid File Which Are Present in Directory ");
			if (fileExtension == pdfExtentension)
			{
				pdfToText();
			}
			else if (fileExtension == pngExtentension)
			{
				pngToText();
			}
			else
			{
				allConversion();
			}
		}
	}

	public static void convertAllFiles(List<String> listpdf)
	{
		String fileName = "";
		for (String fileNameFromFilelist : listpdf)
		{
			fileName = createFileName(fileNameFromFilelist);
			if (fileName.endsWith(pdfExtentension))
			{
				pdfFileProcessing(fileName, fileNameFromFilelist);
			}
			else if (fileName.endsWith(pngExtentension))
			{
				pngFileProcessing(fileName, fileNameFromFilelist);
			}
		}
	}

	public static void addFilesInList(File folder, int depth, int calMaxDepth, String fileExtension)
	{
		if (depth > maxDepthFromUser)
		{
			return;
		}

		File[] files = folder.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				if (file.isDirectory())
				{
					addFilesInList(file, depth + 1, calMaxDepth, fileExtension);
				}
				else
				{
					String fileName = file.getName().toLowerCase();
					if (fileName.endsWith(pdfExtentension) && fileExtension == pdfExtentension)
					{
						pdfFileList.add(file.getAbsolutePath());
					}
					else if (fileName.endsWith(pngExtentension) && fileExtension == pngExtentension)
					{
						pngFileList.add(file.getAbsolutePath());
					}
					else if ((fileName.endsWith(pdfExtentension) || fileName.endsWith(pngExtentension))
							&& fileExtension == bothExtensionName)
					{
						allFileList.add(file.getAbsolutePath());
					}
				}
			}
		}

	}

	public static int getMaxFolderDepth(File folder)
	{
		int maxDepth = 0;
		File[] files = folder.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				if (file.isDirectory())
				{
					int depth = getMaxFolderDepth(file) + 1;
					if (depth > maxDepth)
					{
						maxDepth = depth;
					}
				}
			}
		}
		return maxDepth;
	}

	public static boolean checkFilesInDirectory(String fileExtension)
	{

		File folder = new File(all_files_path);
		int calMaxDepth = 0;

		calMaxDepth = getMaxFolderDepth(folder);
		if (maxDepthFromUser == -1)
		{
			maxDepthFromUser = calMaxDepth;
		}

		if (calMaxDepth >= maxDepthFromUser)
		{
			if (folder.exists() && folder.isDirectory())
			{
				addFilesInList(folder, 0, calMaxDepth, fileExtension);
			}
		}
		else
		{
			MyLogger.myLoggerr.info("You Are Provided Depth Are Not Under Maximum Folder Depth");
			return false;
		}

		if (pdfFileList.size() <= 0 && fileExtension == pdfExtentension)
		{
			MyLogger.myLoggerr.info("PDF Files Are Not There In Your Directory");
			return false;
		}
		else if (pngFileList.size() <= 0 && fileExtension == pngExtentension)
		{
			MyLogger.myLoggerr.info("PNG Files Are Not There In Your Directory");
			return false;
		}
		else if (allFileList.size() <= 0 && fileExtension == bothExtensionName)
		{
			MyLogger.myLoggerr.info("Both Files Are Not There In Your Directory");
			return false;
		}
		return true;
	}

	private static void pdfFileProcessing(String fileName, String fileNameFromMainList)
	{
		try
		{
			boolean fileAdd = uniqueFiles.add(fileName);

			if (fileAdd == true)
			{
				String outPutFilePath = "";

				String tesseractFilePath = properties.getProperty("tesseract_data_path");

				Tesseract tesseract = new Tesseract();

				tesseract.setDatapath(tesseractFilePath);

				File inputFile = new File(fileNameFromMainList);

				String result = tesseract.doOCR(inputFile);

				MyLogger.myLoggerr.info(fileName + " File Conversion Is Started...");

				String inputPath = properties.getProperty("all_files_path");

				String outFilePath = properties.getProperty("OutPut_File_Path");

				String replacePath = fileNameFromMainList.replace(inputPath, outFilePath);

				String replaceName = replacePath.replace(fileName, "");

				outPutFilePath = replaceName;

				File folder = new File(outPutFilePath);
				folder.mkdirs();

				File outputFile = new File(outPutFilePath + fileName.replaceAll(pdfExtentension, txtExtension));

				FileWriter txtFileWriter = new FileWriter(outputFile);

				txtFileWriter.write(result);

				txtFileWriter.close();

				MyLogger.myLoggerr.info(fileName + " To " + fileName.replaceAll(pdfExtentension, txtExtension)
						+ " File Conversion Is Success!!!");
			}
		}
		catch (TesseractException | IOException e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "IOException || TesseractException :: ", e);
		}
	}

	private static void pngFileProcessing(String fileName, String fileNameFromMainList)
	{
		try
		{
			boolean fileAdd = uniqueFiles.add(fileName);

			if (fileAdd == true)
			{
				String inputPath = properties.getProperty("all_files_path");

				String outFilePath = properties.getProperty("OutPut_File_Path");

				String replacePath = fileNameFromMainList.replace(inputPath, outFilePath);
				String replaceName = replacePath.replace(fileName, "");
				String tesseractFilePath = properties.getProperty("tesseract_data_path");

				Tesseract tesseract = new Tesseract();

				tesseract.setDatapath(tesseractFilePath);

				String outPutFilePath = "";

				File inputImageFile = new File(fileNameFromMainList);

				BufferedImage image = ImageIO.read(inputImageFile);

				String result = tesseract.doOCR(image);

				MyLogger.myLoggerr.info(fileName + " File Conversion Is Started...");

				outPutFilePath = replaceName;

				File folder = new File(outPutFilePath);
				folder.mkdirs();

				File outputFile = new File(outPutFilePath + fileName.replaceAll(pngExtentension, txtExtension));

				FileWriter writer = new FileWriter(outputFile);

				writer.write(result);

				writer.close();

				MyLogger.myLoggerr.info(fileName + " To " + fileName.replaceAll(pngExtentension, txtExtension)
						+ " File Conversion Is Success!!!");
			}
		}
		catch (IOException | TesseractException e)
		{
			MyLogger.myLoggerr.log(Level.ERROR, "IOException || TesseractException :: ", e);
		}
	}
}
