package com.logilite.pdfToText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;

public class PNG {

	public void pngToText(int num2, int numForCallSetFileListMethod) throws FileNotFoundException {
		try {
			Scanner sc = new Scanner(System.in);

			List<String> pngList = new ArrayList<>();
			String fileNameFromUser = "";
			String pngFileName = "";

			ScanPdfToText.properties.load(new FileInputStream("config.properties"));

			ScanPdfToText.all_files_path = ScanPdfToText.properties.getProperty("all_files_path");

			if (!ScanPdfToText.all_files_path.endsWith("/")) {
				ScanPdfToText.all_files_path = ScanPdfToText.all_files_path + "/";
			}

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

			String depth = ScanPdfToText.properties.getProperty("depthFromUser");

			int depthFromUser = Integer.parseInt(depth);
			ScanPdfToText.maxDepthFromUser = depthFromUser;

			if (numForCallSetFileListMethod == 0) {
				MyLogger.myLoggerr.info("Please Enter The File Name or 0 to convert all file and also for Exit");
			}

			pngList = ScanPdfToText.setFileList(fileNameFromUser, pngList, numForCallSetFileListMethod, num2);

			if (ScanPdfToText.maxDepthFromUser == -1) {
				File folder = new File(ScanPdfToText.all_files_path);
				int maxFolderDepth = ScanPdfToText.getMaxFolderDepth(folder);
				ScanPdfToText.maxDepthFromUser = maxFolderDepth;
			}

			String outputxtFilePath = ScanPdfToText.properties.getProperty("txtfile_output");

			ScanPdfToText.CheckFiles(pngList, pngFileName, outputxtFilePath, num2, numForCallSetFileListMethod);
			sc.close();
		} catch (Exception e) {
			MyLogger.myLoggerr.log(Level.ERROR, "Exception ::", e);
		}
	}

}
