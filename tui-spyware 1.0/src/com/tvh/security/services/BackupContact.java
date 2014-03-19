package com.tvh.security.services;

import com.tvh.security.services.JsonMarker;
import com.tvh.security.utils.FileUtils;

public class BackupContact extends BackupRestoreFactory {
    
    public BackupContact(ContactDAO contact) {
        super(contact);
    }
    
    public boolean backup(String fileName) {
        String json = JsonMarker.createJSONString(m_statistics).toString();
        // write backup file
        return (FileUtils.createBackupFile(fileName, json));
    }
    
    public void restore(String fileName) {
        
    }
}
