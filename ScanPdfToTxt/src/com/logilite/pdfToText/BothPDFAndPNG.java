package com.logilite.pdfToText;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BothPDFAndPNG {

	public void bothConversion(int num3, int numForCallSetFileListMethod) {

		try {
			Scanner sc = new Scanner(System.in);

			List<String> allFilesList = new ArrayList<>();

			String fileNameFromUser = "";

			ScanPdfToText.properties.load(new FileInputStream("config.properties"));

			ScanPdfToText.all_files_path = ScanPdfToText.properties.getProperty("all_files_path");

			if (!ScanPdfToText.all_files_path.endsWith("/")) {
				ScanPdfToText.all_files_path = ScanPdfToText.all_files_path + "/";
			}

			String depth = ScanPdfToText.properties.getProperty("depthFromUser");

			File folderCheck = new File(ScanPdfToText.all_files_path);

			if (folderCheck.isDirectory() == false) {
				MyLogger.myLoggerr.info("Invalid Folder Path Please Check Once Again Or U Can "
						+ "Put 0 To Continue Used For Default Path");
				if (sc.nextInt() == 0) {
					ScanPdfToText.all_files_path = ScanPdfToText.properties.getProperty("default_all_files_path");
				} else {
					return;
				}
			}

			int depthFromUser = Integer.parseInt(depth);
			ScanPdfToText.maxDepthFromUser = depthFromUser;

			if (numForCallSetFileListMethod == 0) {
				MyLogger.myLoggerr.info("Please Enter The File Name or 0 to convert all files and also for Exit");
			}

			allFilesList = ScanPdfToText.setFileList(fileNameFromUser, allFilesList, numForCallSetFileListMethod, num3);

			if (ScanPdfToText.maxDepthFromUser == -1) {
				File folder = new File(ScanPdfToText.all_files_path);
				int maxFolderDepth = ScanPdfToText.getMaxFolderDepth(folder);
				ScanPdfToText.maxDepthFromUser = maxFolderDepth;
			}

			String txtFilePath = ScanPdfToText.properties.getProperty("defaultOutPutPath");
			if (txtFilePath == null || txtFilePath.length() <= 0) {
				txtFilePath = ScanPdfToText.properties.getProperty("defaultOutPutPath");
			}

			String fileName = "";

			ScanPdfToText.CheckFiles(allFilesList, fileName, txtFilePath, num3, numForCallSetFileListMethod);
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
