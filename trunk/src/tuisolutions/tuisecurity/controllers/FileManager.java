package tuisolutions.tuisecurity.controllers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import tuisolutions.tuisecurity.utils.AppUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FileManager {
    Context context;
    String pathFile = ""; // Sdcard: Environment.getExternalStorageDirectory() +
                          // "/sleep-away.mp3"
    String url = "";// Localhost: "http://10.0.2.2/android/upload.php"
    
    public FileManager(Context c) {
        this.context = c;
    }
    
    public FileManager(Context c, String pathFile, String url) {
        this.context = c;
        this.pathFile = pathFile;
        this.url = url;
    }
    
    @SuppressWarnings("rawtypes")
    private class UploadFile extends AsyncTask {
        @Override
        protected Object doInBackground(Object... params) {
            Looper.prepare();
            uploadFileToServer();
            return true;
        }
        
        @Override
        protected void onPostExecute(Object result) {
            if (result.equals(true)) {
                Toast.makeText(context, "Upload successfully!", Toast.LENGTH_LONG).show();
            }
        }
        
    }
    
    @SuppressWarnings("deprecation")
    private synchronized void uploadFileToServer() {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        
        String pathToOurFile = this.pathFile;
        String urlServer = this.url;
        String lineEnd = "\r\n";
        String boundary = "*****";
        String twoHyphens = "--";
        
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));
            
            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();
            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            
            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // Send multipart form data neccessary after file data...
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Close outputStream
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (MalformedURLException ex) {
            Log.e("Upload file", "error; " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Upload file", "error: " + ioe.getMessage(), ioe);
        }
        
        // Responses from the server (code and message)
        try {
            inputStream = new DataInputStream(connection.getInputStream());
            String str;
            
            while ((str = inputStream.readLine()) != null) {
                Log.d("Upload file", "Server Response " + str);
            }
            inputStream.close();
        } catch (IOException ioex) {
            Log.e("Upload file", "error: " + ioex.getMessage(), ioex);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public void uploadFile() {
        if (!this.pathFile.equals("") && !this.url.equals("")) {
            new UploadFile().execute();
        } else {
            Toast.makeText(context, "Please tell me know where source path file and url server.", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Write a ArrayList type of String to a file
     * 
     * @param src
     * @param filePath
     * @return true if success, false if has any error
     */
    public static boolean writeToFile(ArrayList<String> src, String filePath, boolean append, int offer) {
        
        try {
            // Create file
            FileWriter fstream = new FileWriter(filePath, append);
            BufferedWriter out = new BufferedWriter(fstream);
            if (offer == 1) {
                // write added lines
                out.write("###-Added apps-###");
                out.write("\n");
            }
            if (offer == 2) {
                // write deleted lines
                out.write("###-Deleted apps-###");
                out.write("\n");
            }
            if (offer == 3) {
                // write modified lines
                out.write("###-Modified apps-###");
                out.write("\n");
            }
            for (String s : src) {
                out.write(s);
                out.write("\n");
            }
            
            // Close the output stream
            out.close();
            return true;
        } catch (Exception e) {
            // Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean writeToFile(ArrayList<Integer> EA, String filePath, boolean append, boolean decryptFile) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(filePath, append);
            BufferedWriter out = new BufferedWriter(fstream);
            if (!decryptFile) {
                // Write Encrypted file
                for (int i : EA) {
                    out.write(String.valueOf(i));
                }
            } else {
                // If write decrypted File
                for (int i : EA) {
                    out.write(String.valueOf((char) i));
                }
            }
            
            // Close the output stream
            out.close();
            return true;
        } catch (Exception e) {
            // Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public static String readFile(File file) {
        String result = "";
        if (file.exists() && file.length() != 0) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = bufferReader.readLine()) != null) {
                    result += line + "\n";
                }
                in.close();
                return result;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }
    
    /**
     * Delete a file
     * 
     * @param context
     * @param pathFile
     *            : full file path to deleting
     * 
     */
    public static void delete(Context context, String pathFile) {
        if (new File(pathFile).exists()) {
            rmFile(pathFile);
        }
    }
    
    /**
     * encrypting a file follow RSA with configable key
     * 
     * @param pathFile
     */
    // TOTO configable keys
    public static boolean encrypt(String path, String plaintFile, String cypherFile) {
        Cypher c = new Cypher(path + "public.key", path + "private.key", 1024);
        return c.crypt(plaintFile, cypherFile, false);
    }
    
    /**
     * decrypting a file follow RSA with configable key
     * 
     * @param pathFile
     */
    public static boolean decrypt(String path, String cypherFile, String plaintFile) {
        Cypher c = new Cypher(path + "public.key", path + "private.key", 1024);
        return c.crypt(plaintFile, cypherFile, true);
    }
    
    /**
     * Get file path with out filename.
     * 
     * @param file
     * @return
     */
    public static String getPathFileWithoutName(File file) {
        String fullPath = file.getAbsolutePath();
        String fileName = file.getName();
        return fullPath.substring(0, fullPath.indexOf(fileName));
    }
    
    /**
     * Save pair key RSA to file
     * 
     * @param fileName
     * @param mod
     * @param exp
     * @throws IOException
     */
    public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            oout.close();
        }
    }
    
    public static void getAllFileAndFolder(File folder, Set<File> all) {
        all.add(folder);
        if (folder.isFile()) {
            return;
        }
        for (File file : folder.listFiles()) {
            all.add(file);
            if (file.isDirectory()) {
                getAllFileAndFolder(file, all);
            }
        }
    }
    
    public static void rmFile(String path_file) {
        String[] commands = { "sysrw", "/system/bin/rm " + path_file, "sysro" };
        try {
            AppUtils.runAsRoot(commands);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
