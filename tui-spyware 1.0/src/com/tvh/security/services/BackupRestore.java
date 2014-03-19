package com.tvh.security.services;

public interface BackupRestore {
    
    public boolean backup(String fileName);
    
    public void restore(String fileName);
    
}
