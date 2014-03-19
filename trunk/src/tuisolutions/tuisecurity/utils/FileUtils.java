package tuisolutions.tuisecurity.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileUtils {
	private static final String TAG = FileUtils.class.getName();

	public static boolean DeleleFileOnSDCard(String filename) {
		if (isSdcardMounted()) {
			File file = new File(filename);
			if (file.exists()) {
				return file.delete();
			}
		}
		return false;
	}

	public static String loadBackupFile(String filename) throws IOException {
		File file = new File(Environment.getExternalStorageDirectory(),
				filename);
		FileReader fileIn = null;
		try {
			fileIn = new FileReader(file);
		} catch (FileNotFoundException e) {
			Log.e("File Reading Exception",
					"Error in reading file " + e.getMessage());
			return null;
		}
		BufferedReader buffer = new BufferedReader(fileIn);
		String jsonContent = "";
		String s = "";
		while ((s = buffer.readLine()) != null) {
			jsonContent += s;
		}
		Log.v("check", "Read file Successfully");
		buffer.close();
		fileIn.close();

		return jsonContent;
	}

	public static boolean createBackupFile(String path, String json) {
		File file = createNewFile(path);
		if (file == null) {
			Log.e(TAG, "Something went wrong. Can not create new File");
			return false;
		}
		FileWriter fileOut = null;
		try {
			fileOut = new FileWriter(file);
			fileOut.write(json);
			fileOut.flush();
			fileOut.close();
			Log.v(TAG, "Create File Successfully");
		} catch (IOException e) {
			Log.e("IOException", "Can not read file " + e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean mkdir(File path) {
		if (isSdcardMounted()) {
			if (path.exists() || path.mkdirs()) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSdcardMounted() {
		String state = Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static File createNewFile(String path) {
		File directory = new File(path).getParentFile();
		if (!mkdir(directory)) {
			return null;
		}
		File file = new File(path);
		try {
			file.delete();
			file.createNewFile();
		} catch (IOException e) {
			Log.e("IOException", "Can not create file " + e.getMessage());
			return null;
		}
		return file;
	}

	/**
	 * Format SD card function
	 */
	public static void formatDSCard() {
		File deleteMatchingFile = new File(Environment
				.getExternalStorageDirectory().toString()); //
		Log.i("External Path", deleteMatchingFile.getPath().toString());
		try {
			File[] filenames = deleteMatchingFile.listFiles();
			if (filenames != null && filenames.length > 0) {
				for (File tempFile : filenames) {
					if (tempFile.isDirectory()) {
						deleteDirectory(tempFile.toString());
						tempFile.delete();
					} else {
						tempFile.delete();
					}
				}
			} else {
				deleteMatchingFile.delete();
			}
			Log.v(TAG, "Format SD Card successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deleteDirectory(String name) {
		File directoryFile = new File(name);
		File[] filenames = directoryFile.listFiles();
		if (filenames != null && filenames.length > 0) {
			for (File tempFile : filenames) {
				if (tempFile.isDirectory()) {
					deleteDirectory(tempFile.toString());
					tempFile.delete();
				} else {
					tempFile.delete();
				}
			}
		} else {
			directoryFile.delete();
		}
	}
	
}
