package karrus.server.os;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileTools {

	private static Logger logger = Logger.getLogger(FileTools.class);
	
	public static void copy(String inFileName, String outFileName) throws IOException{
		File f = new File(inFileName);
		File ff = new File(outFileName);
		FileUtils.copyFile(f, ff);
	}

	public static void move(String inFileName, String outFileName) throws IOException{
		File f = new File(inFileName);
		File ff = new File(outFileName);
		FileUtils.moveFile(f, ff);
	}

	public static void delete(String fileName) throws IOException{
		File f = new File(fileName);
		FileUtils.forceDelete(f);
	}

	public static void makeZip(String zipFileName, List<String> fileNamesToAddToZip) throws IOException{	
		logger.info("Creating '" + zipFileName + "'...");
		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		for(String s : fileNamesToAddToZip)
			addToZipFile(s, zos);
		zos.close();
		fos.close();
		logger.info("'" + zipFileName + "' created !");
	}


	private static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		String tmp = fileName.substring(fileName.lastIndexOf("/")+1);
		logger.info("Writing '" + fileName + "' to zip file : '"+tmp+"' ");

		ZipEntry zipEntry = new ZipEntry(tmp);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}
