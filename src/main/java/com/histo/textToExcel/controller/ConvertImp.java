package com.histo.textToExcel.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ConvertImp {
	public static void main(String arg[]) {
		try {
			File checkTemplateXLSXPath = new File(arg[0]);
			File countsPath = new File(arg[1]);
			if (checkTemplateXLSXPath.exists()) {
			  if (countsPath.exists()) {
				if (checkTemplateXLSXPath.getName().endsWith("xlsx")) {
//			FileInputStream sfinderOutputtemplate = new FileInputStream("C:\\Users\\ManivasagamMarichamy\\Documents\\ProjectDocuments\\TextToExcel\\sfinderOutput.template.xlsx");
					FileInputStream sfinderOutputtemplate = new FileInputStream(arg[0]);
					XSSFWorkbook studentsSheet = new XSSFWorkbook(sfinderOutputtemplate);
					XSSFSheet worksheet = studentsSheet.getSheetAt(0);
					int rowCount = worksheet.getPhysicalNumberOfRows();
//					System.out.println(rowCount);
					HashMap<String, Integer> templateColumnIndexValue = new LinkedHashMap<String, Integer>();
					for (int i = 0; i < rowCount; i++) {
						Row row = worksheet.getRow(i);
						Cell cell = row.getCell(0);
						String cellValueAsString = cell.getStringCellValue();
//				System.out.println(cellValueAsString);
						templateColumnIndexValue.put(cellValueAsString.trim(), i);
					}
//			int columnCount = 1;
					int numberOfTextFiles = 1;
//			File sourcePathDir = new File("C:\\Users\\ManivasagamMarichamy\\Documents\\ProjectDocuments\\TextToExcel");
					File sourcePathDir = new File(arg[1]);
					File[] files = sourcePathDir.listFiles(filelist -> filelist.getName().contains("counts"));
					if (files.length > 0) {
						for (File file : files) {
							Scanner sc = new Scanner(file);
							boolean isHeaderSetAlready = false;
							Row rowHeader = null;
							while (sc.hasNext()) {
								String readLine = sc.nextLine();
								String[] splitValues = readLine.split(" ");
								String appendingName = splitValues[0].trim();
								String appendingValue = splitValues[1];
								int getNameValueRow = templateColumnIndexValue.get(appendingName);
								if (!isHeaderSetAlready) {
									// Header
									rowHeader = worksheet.getRow(0);
									Cell cellHeader = rowHeader.createCell(numberOfTextFiles);
									cellHeader.setCellValue("Column" + numberOfTextFiles);
									worksheet.autoSizeColumn(numberOfTextFiles);
									isHeaderSetAlready = true;
								}
								// append values
								Row row = worksheet.getRow(getNameValueRow);
								Cell cell = row.createCell(numberOfTextFiles);
//				System.out.println(row.getCell(0).getCellStyle().getFillForegroundColor());
//				System.out.println(row.getCell(0).getStringCellValue());
								CellStyle cStyle = row.getCell(0).getCellStyle();
								cell.setCellStyle(cStyle);
								cell.setCellValue(new Double(appendingValue));
							}
							numberOfTextFiles++;
						}
//			System.out.println(lastRow);
//			Row row = worksheet.createRow(++lastRow);
//			row.createCell(1).setCellValue("Dr.Hola");
						sfinderOutputtemplate.close();
						worksheet.setAutoFilter(new CellRangeAddress(0, 0, 1, numberOfTextFiles - 1));
						worksheet.createFreezePane(0, 1);
//			FileOutputStream output_file = new FileOutputStream(new File("C:\\\\Users\\\\ManivasagamMarichamy\\\\Documents\\\\ProjectDocuments\\\\TextToExcel\\\\sfinderOutput.xlsx"));
						FileOutputStream output_file = new FileOutputStream(
								new File(arg[2] + File.separator + "sfinderOutput.xlsx"));
//			// write changes
						studentsSheet.write(output_file);
						output_file.close();
						System.out.println("Successfully written the file in " + arg[2] + "sfinderOutput.xlsx");
					} else {
						System.out.println("No Files contains name of 'counts'");
					}
				} else {
					System.err.println("Template file is not xlsx format");
				}
			  }
				else {
					System.out.println("Path not found ->"+arg[1]);
				}
			} else {
				System.err.println("File Not Found at location " + arg[0]);
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getMessage());
		}
	}

}
