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

public class ScanPdfToText {

	static Scanner scanner = new Scanner(System.in);
	public static Properties properties = new Properties();
	public static String all_files_path = null;
	public static String pngExtentension = ".png";
	public static String pdfExtentension = ".pdf";
	public static String jpegExtentension = ".jpeg | jpg";
	public static String txtExtension = ".txt";
	public static int filefoundCount = 0;
	public static String defaultOutPutPath = null;
	public static List<String> fileList = new ArrayList<>();
	public static int maxDepthFromUser = 0;
	public static int folderCount = 0;
	public static int FolderAvailableCount = 0;
	public static Set<String> uniqueFiles = new HashSet<>();
	public static int folderIterationNum = 20;

	public static BothPDFAndPNG both_PDF_and_PNG = new BothPDFAndPNG();
	public static PDF pdf = new PDF();
	public static PNG png = new PNG();
	private static String txtFilePath2;

	public static void main(String[] args) {
		takenInfoFromUser();
	}

	private static void takenInfoFromUser() {
		try {
			MyLogger.myLoggerr.info("Option {1. PDF to txt }, {2. png to txt}, {3. for both (png & pdf to txt)}");

			int uNum = scanner.nextInt();
			int numForCallSetFileListMethod = 0;

			switch (uNum) {
			case 1:
				int onlyPdfConv = 1;
				pdf.pdfToText(onlyPdfConv, numForCallSetFileListMethod);
				break;
			case 2:
				int onlyPNGConv = 2;
				png.pngToText(onlyPNGConv, numForCallSetFileListMethod);
				break;
			case 3:
				int bothConv = 3;
				both_PDF_and_PNG.bothConversion(bothConv, numForCallSetFileListMethod);
				break;
			default:
				MyLogger.myLoggerr.info("Wrong Input Please Enter Correct Number");
				break;
			}
		} catch (Exception e) {
			MyLogger.myLoggerr.log(Level.WARN, "Exception ::", e);
		}
	}

	public static List<String> setFileList(String fileNameFromUser, List<String> list, int numForCallSetFileListMethod,
			int num) {

		String fileExtension = "";
		boolean bValue = false;
		int l = 0;

		if (numForCallSetFileListMethod != 0) {
			MyLogger.myLoggerr.info("Please Enter Correct File Name");
		}
		while (true) {

			fileNameFromUser = scanner.next();
			all_files_path = folderPath(fileNameFromUser) + "/";

			if (fileNameFromUser.equalsIgnoreCase("0")) {
				break;
			}

			if (num == 1 || num == 2 || num == 3) {
				if (num == 1) {
					fileExtension = pdfExtentension;
					if (fileNameFromUser.length() > 0 && (fileNameFromUser.toLowerCase().endsWith(pdfExtentension))) {
						bValue = true;
						if (bValue == true && l <= 0) {
						}
						break;
					}
				}
				if (num == 2) {
					fileExtension = pngExtentension;
					if (fileNameFromUser.length() > 0 && (fileNameFromUser.toLowerCase().endsWith(pngExtentension))) {
						bValue = true;
						if (bValue == true && l <= 0) {
						}
						break;
					}
				}
				if (num == 3) {
					fileExtension = pdfExtentension + " File Or " + pngExtentension;
					if (fileNameFromUser.length() > 0 && (fileNameFromUser.toLowerCase().endsWith(pdfExtentension)
							|| fileNameFromUser.toLowerCase().endsWith(pngExtentension))) {
						bValue = true;
						if (bValue == true && l <= 0) {
						}
						break;
					}
				}
			}
			if (bValue == false) {
				MyLogger.myLoggerr.info("Please Enter Valid " + fileExtension + " File Name And 0 For Next Process");
				l++;
				setFileList(fileNameFromUser, list, numForCallSetFileListMethod, num);
			}
		}
		for (int i = fileNameFromUser.length() - 1; i >= 0; i--) {

			if (fileNameFromUser.charAt(i) == '/') {
				fileNameFromUser = fileNameFromUser.substring(i + 1, fileNameFromUser.length());
				break;
			}
		}
		if (bValue == true) {
			list.add(fileNameFromUser);
		}
		return list;

	}

	private static String folderPath(String fileNameFromUser) {

		for (int i = fileNameFromUser.length() - 1; i >= 0; i--) {

			if (fileNameFromUser.charAt(i) == '/') {
				all_files_path = all_files_path + fileNameFromUser.substring(0, i);
				break;
			}
		}
		return all_files_path;
	}

	private static String createFileName(String fileNameFromUser) {

		for (int i = fileNameFromUser.length() - 1; i >= 0; i--) {

			if (fileNameFromUser.charAt(i) == '/') {
				fileNameFromUser = fileNameFromUser.substring(i + 1, fileNameFromUser.length());
				break;
			}
		}
		return fileNameFromUser;
	}

	public static void CheckFiles(List<String> filesList, String fileName, String outputxtFilePath, int num,
			int numForCallSetFileListMethod) {
		try {
			File folder = new File(all_files_path);
			int calMaxDepth = 0;

			if (folder.exists() && folder.isDirectory()) {
				calMaxDepth = getMaxFolderDepth(folder);
			} else {
				return;
			}

			if (calMaxDepth >= maxDepthFromUser) {
				if (folder.exists() && folder.isDirectory()) {
					iterateFolder(folder, 0, maxDepthFromUser, calMaxDepth);
				} else {
					MyLogger.myLoggerr.info("Invalid folder path.");
					return;
				}

			} else {
				MyLogger.myLoggerr.info("You Are Provided Depth Are Not Under Maximum Folder Depth");
				return;
			}

			if (fileList.size() > 0) {
				int count = 0;
				for (String filenamefileList : fileList) {
					if (num == 1 && filenamefileList.endsWith(pdfExtentension)) {
						count++;
					} else if (num == 2 && filenamefileList.endsWith(pngExtentension)) {
						count++;
					} else if (num == 3 && (filenamefileList.endsWith(pngExtentension)
							|| filenamefileList.endsWith(pdfExtentension))) {
						count++;
					}
				}
				if (count == 0) {
					if (num == 1) {
						MyLogger.myLoggerr.info("No PDF Files Are There");
					} else if (num == 2) {
						MyLogger.myLoggerr.info("No PNG Files Are There");
					} else if (num == 3) {
						MyLogger.myLoggerr.info("Both Files Are Not There");
					}
					return;
				}
			} else if (fileList == null) {
				MyLogger.myLoggerr.info("No Files Are There");
				return;
			}

			File[] allFiles = folder.listFiles();
			int validFilePresentOrNot = 0;
			numForCallSetFileListMethod = 0;
			int numComeFrom = 0;
			String fileNameFromMainList = "";

			if (allFiles != null && allFiles.length > 0) {
				for (File file : allFiles) {
					if (filesList.size() > 0) {
						for (String fileNameFroUserList : filesList) {
							fileName = createFileName(file.getName());
							if (fileName.equalsIgnoreCase(fileNameFroUserList)) {
								fileName = file.getName();
								fileNameFromMainList = file.getAbsolutePath();
								validFilePresentOrNot++;
								checkFileFoundCount(fileName, outputxtFilePath, num, numComeFrom, fileNameFromMainList);
								return;
							} else {
								for (String listname : fileList) {
									fileName = createFileName(listname);
									if (fileName.equalsIgnoreCase(fileNameFroUserList)) {
										validFilePresentOrNot++;
										fileNameFromMainList = listname;
										checkFileFoundCount(fileName, outputxtFilePath, num, numComeFrom,
												fileNameFromMainList);
									}
								}
								if (validFilePresentOrNot == 0) {
									numForCallSetFileListMethod++;
									MyLogger.myLoggerr.info("This File Are Not Present Please Enter Valid File Which "
											+ "Are Present in Directory ");
									if (num == 1) {
										pdf.pdfToText(num, numForCallSetFileListMethod);
									} else if (num == 2) {
										png.pngToText(num, numForCallSetFileListMethod);
									} else {
										both_PDF_and_PNG.bothConversion(num, numForCallSetFileListMethod);
									}
								}
								return;
							}
						}
					} else if (fileList.size() > 0 && fileList != null) {
						for (String fileNameFromFilelist : fileList) {
							fileName = createFileName(fileNameFromFilelist);
							fileNameFromMainList = fileNameFromFilelist;
							validFilePresentOrNot++;
							checkFileFoundCount(fileName, outputxtFilePath, num, numComeFrom, fileNameFromMainList);
						}
						return;
					} else {
						fileName = file.getName();
						checkFileFoundCount(fileName, outputxtFilePath, num, numComeFrom, fileNameFromMainList);
					}
				}
			}
			if (filefoundCount == 0) {
				if (num == 1) {
					MyLogger.myLoggerr.info("Pdf Files Are Not There");
				} else if (num == 2) {
					MyLogger.myLoggerr.info("Images Files Are Not There");
				} else if (num == 3) {
					MyLogger.myLoggerr.info("Both (Pdf and Images) Files Are Not There");
				}
			} else {
				if (filefoundCount == 0) {
					MyLogger.myLoggerr.info("In that path No files Are There");
				}
			}
		} catch (Exception e) {
			MyLogger.myLoggerr.log(Level.WARN, "Exception :: ", e);
		}
	}

	private static void pdfFileProcessing(String fileName, String txtFilePath, int numComeFrom,
			String fileNameFromMainList) {

		try {

			boolean fileAdd = uniqueFiles.add(fileName);

			if (fileAdd == true) {

				String property = properties.getProperty("all_files_path");

				String tesseractFilePath = properties.getProperty("tesseract_data_path");

				Tesseract tesseract = new Tesseract();

				tesseract.setDatapath(tesseractFilePath);

				String replacePath = fileNameFromMainList.replace(property, "/home/kalpesh/Outputfiles");
				String replaceName = replacePath.replace(fileName, "");

				File inputFile = null;

				if (numComeFrom == 10) {
					inputFile = new File(all_files_path + "/" + fileName);
				} else {
					inputFile = new File(fileNameFromMainList);
				}

				String result = tesseract.doOCR(inputFile);

				MyLogger.myLoggerr.info(fileName + " File Conversion Is Started...");

				if (txtFilePath == null || txtFilePath.length() <= 0) {
					txtFilePath = properties.getProperty("default_OutPut_File_Path");
				}

				txtFilePath = replaceName;
				File folder = new File(txtFilePath);
				folder.mkdirs();

				File outputFile = new File(txtFilePath + fileName.replaceAll(pdfExtentension, txtExtension));

				FileWriter txtFileWriter = new FileWriter(outputFile);

				txtFileWriter.write(result);

				txtFileWriter.close();

				MyLogger.myLoggerr.info(fileName + " To " + fileName.replaceAll(pdfExtentension, txtExtension)
						+ " File Conversion Is Success!!!");
			}
		} catch (TesseractException e) {
			MyLogger.myLoggerr.log(Level.ERROR, "TesseractException ::", e);
		} catch (IOException e) {
			MyLogger.myLoggerr.log(Level.ERROR, "IOException ::", e);
		}
	}

	private static void pngFileProcessing(String pngFileName, String outputxtFilePath, int numComeFrom,
			String fileNameFromMainList) {
		try {

			boolean fileAdd = uniqueFiles.add(pngFileName);

			if (fileAdd == true) {

				String pngDirective = properties.getProperty("all_files_path");

				String outputDirective = properties.getProperty("OutPut_File_Path");

				String tesseractFilePath = properties.getProperty("tesseract_data_path");

				Tesseract tesseract = new Tesseract();

				tesseract.setDatapath(tesseractFilePath);

				tesseract.setLanguage("eng");

				String replacePath = fileNameFromMainList.replace(pngDirective, outputDirective);
				String replaceName = replacePath.replace(pngFileName, "");

				File inputImageFile = null;

				if (numComeFrom == 10) {
					inputImageFile = new File(all_files_path + "/" + pngFileName);
				} else {
					inputImageFile = new File(fileNameFromMainList);
				}

				BufferedImage image = ImageIO.read(inputImageFile);

				String result = tesseract.doOCR(image);

				MyLogger.myLoggerr.info(pngFileName + " File Conversion Is Started...");

				if (outputxtFilePath == null || outputxtFilePath.length() <= 0) {
					outputxtFilePath = properties.getProperty("default_OutPut_File_Path");
				}

				outputxtFilePath = replaceName;
				File folder = new File(outputxtFilePath);
				boolean mkdirs = folder.mkdirs();

				if (mkdirs == false) {

				}

				File outputFile = new File(outputxtFilePath + pngFileName.replaceAll(pngExtentension, txtExtension));

				FileWriter writer = new FileWriter(outputFile);

				writer.write(result);

				writer.close();

				MyLogger.myLoggerr.info(pngFileName + " To " + pngFileName.replaceAll(pngExtentension, txtExtension)
						+ " File Conversion Is Success!!!");
			}
		} catch (IOException | TesseractException e) {
			MyLogger.myLoggerr.log(Level.ERROR, "IOException || TesseractException :: ", e);
		}
	}

	static void checkFileFoundCount(String fileName, String outputxtFilePath, int num, int numComeFrom,
			String fileNameFromMainList) {

		if (num == 1) {
			if (fileName.endsWith(pdfExtentension)) {
				filefoundCount++;
				pdfFileProcessing(fileName, outputxtFilePath, numComeFrom, fileNameFromMainList);
			}
		}

		else if (num == 2) {
			if (fileName.endsWith(pngExtentension)) {
				filefoundCount++;
				pngFileProcessing(fileName, outputxtFilePath, numComeFrom, fileNameFromMainList);
			}
		}

		else if (num == 3) {
			if (fileName.endsWith(pdfExtentension)) {
				filefoundCount++;
				pdfFileProcessing(fileName, outputxtFilePath, numComeFrom, fileNameFromMainList);
			} else if (fileName.endsWith(pngExtentension)) {
				filefoundCount++;
				pngFileProcessing(fileName, outputxtFilePath, numComeFrom, fileNameFromMainList);
			}
		} else {
			MyLogger.myLoggerr.info("Wrong File From User We Can't Convert This File");
		}
	}

	public static int countFolders(String directoryPath) {
		int count = 0;
		File directory = new File(directoryPath);

		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						count++;
						count += countFolders(file.getAbsolutePath());
					}
				}
			}
		}
		return count;
	}

	public static void iterateFolder(File folder, int depth, int maxDepth, int calMaxDepth) {

		if (maxDepth < maxDepthFromUser) {
			MyLogger.myLoggerr.info("Not allowed");
			return;
		}

		if (depth > maxDepth) {
			return;
		}

		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					iterateFolder(file, depth + 1, maxDepth, calMaxDepth); // Recursive call to iterate subfolder
				} else {
					String fileName = file.getName().toLowerCase();
					if (fileName.endsWith(".pdf") || fileName.endsWith(".png")) {
						fileList.add(file.getAbsolutePath());
					}
				}
			}
		}
	}

	public static int getMaxFolderDepth(File folder) {
		int maxDepth1 = 0;
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					int depth = getMaxFolderDepth(file) + 1;
					if (depth > maxDepth1) {
						maxDepth1 = depth;
					}
				}
			}
		}
		return maxDepth1;
	}

}
