package com.histo.textToExcel.process;

import com.histo.textToExcel.controller.ConvertImp;

public class TextFileToExcel {
	//com.histo.textToExcel.process.TextFileToExcel
	public static void main(String[] arg) {
		// arg[0] -> C:\\Users\\ManivasagamMarichamy\\Documents\\ProjectDocuments\\TextToExcel\\sfinderOutput.template.xlsx
		// arg[1] -> C:\\Users\\ManivasagamMarichamy\\Documents\\ProjectDocuments\\TextToExcel
		// arg[2] -> C:\\Users\\ManivasagamMarichamy\\Documents\\ProjectDocuments\\TextToExcel\\MergedOutputFiles\\
		if(arg.length == 3) {
		//calling implementation 
		ConvertImp.main(arg);
		}
		else {
			System.err.println("pass arguments -> /path/sfinderOutput.template.xlsx /path/counts_files_path /path/sfinderOutputmergedPath");
		}
	}
}
