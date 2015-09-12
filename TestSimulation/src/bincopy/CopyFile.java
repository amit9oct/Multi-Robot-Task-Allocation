package bincopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CopyFile {
	
	
	public static final String PATH = "/home/amit9oct/Amitayush/SWARM/TestSimulation/bin/sample/";
	public static final String COPY_PATH = "/home/amit9oct/Amitayush/SWARM/TestSimulation/robots/sample/";
	public static void copyFile(String fileName1,String fileName2){
		File sourceFile = new File(
				PATH+fileName1);

		File destFile = new File(
				COPY_PATH+fileName2);

		/* verify whether file exist in source location */
		if (!sourceFile.exists()) {
			System.out.println("Source File Not Found!");
		}

		/* if file not exist then create one */
		if (!destFile.exists()) {
			try {
				destFile.createNewFile();
				
				System.out.println("Destination file doesn't exist. Creating one!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {

			/**
			 * getChannel() returns unique FileChannel object associated a file
			 * output stream.
			 */
			source = new FileInputStream(sourceFile).getChannel();

			destination = new FileOutputStream(destFile).getChannel();

			if (destination != null && source != null) {
				destination.transferFrom(source, 0, source.size());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (source != null) {
				try {
					source.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (destination != null) {
				try {
					destination.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
