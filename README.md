# JavaUtils
# XMLValidaion:
XmlValidation program used to find what are the changes between two version of XML files. It is used for PAcbio Autotyper output comparision.
# Inputs
com.histo.xmlvalidation.controller.XMLComparator_SMBFile
//10.201.20.198/Volume1Backup/pbvol2/mani/smrtlink/userdata/074016/Reanalysis/AutoType/
//10.201.20.198/Volume1Backup/pbvol2/mani/smrtlink_new/userdata/074016/Reanalysis/AutoType/
//10.201.20.198/Volume1Backup/pbvol2/mani/autoTyperValidator/
# --------------------
# PacbioSNPPreAutotyping:
It is used for SNP pre autotyping program.
Remove the "_noise" string from amplicon_analysis.fasta if present(\074064\Reanalysis\S105--S105\amplicon_analysis.fasta).
create a new json file based on the data from exec PacBioSNPGetBarcodeData " + barCodeId + "," +jobId+ ""
# Inputs
com.histo.pacbiosnp.process.PacBioSNPEntryPoint

//10.201.20.198/Volume1Backup/pbvol2/mani/074064/

S105--S105
# --------------------
# CSVtoJson:
Read .csv file from the given path and convert into json file.
# Inputs
C:\csvFiles\651512481801.csv
# -------------------
# TextoExcel:
This is used for R&D WGS pipeline.
Read the .counts files from source path and convert into .xlsx format.
# Input
com.histo.textToExcel.process.TextFileToExcel

C:\test\sfinderOutput.template.xlsx

C:\test\

C:\test\output
