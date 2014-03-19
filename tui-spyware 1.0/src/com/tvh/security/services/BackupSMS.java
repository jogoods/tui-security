package com.tvh.security.services;

import java.util.ArrayList;

import com.tvh.security.utils.FileUtils;


public class BackupSMS extends BackupRestoreFactory {

    public BackupSMS(AccessUriInterface sms, ArrayList<String> phone) {
        super(sms, phone);
    }

    public BackupSMS(AccessUriInterface sms) {
        super(sms);
    }

    public boolean backup(String filename) {
        String json = JsonMarker.createJSONString(m_statistics).toString();
        // write backup file
        return (FileUtils.createBackupFile(filename, json));

    }

    public void restore(String filename) {
        return;
    }
}
