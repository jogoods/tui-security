package com.tvh.security.utils;

import java.io.*;

public class AuthenticationRestoreFile {
    String smsBackupFile = "", contactBackupFile = "";

    public AuthenticationRestoreFile(String backupFile, boolean isContactRestore) {
        if (isContactRestore) {
            this.contactBackupFile = backupFile;
        } else {
            this.smsBackupFile = backupFile;
        }
    }

    public boolean authen() {
        if (!this.contactBackupFile.equals(null) && new File(contactBackupFile).exists()) {
            try {
                FileInputStream fstream = new FileInputStream(new File(
                        this.contactBackupFile));
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String strLine;
                // Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    if (strLine.contains("raw_contacts")) {
                        return true;
                    }
                }
                // Close the input stream
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else if (!this.smsBackupFile.equals(null) && new File(smsBackupFile).exists()) {
            try {
                FileInputStream fstream = new FileInputStream(new File(
                        this.smsBackupFile));
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String strLine;
                // Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    if (strLine.contains("pdu_recipient_threads")) {
                        return true;
                    }
                }
                // Close the input stream
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
