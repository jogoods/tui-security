package com.tvh.security.services;

import java.util.ArrayList;

public abstract class BackupRestoreFactory implements BackupRestore {
    
    protected AccessUriInterface m_statistics;
    
    public BackupRestoreFactory(AccessUriInterface sms) {
        m_statistics = sms;
    }
    
    public BackupRestoreFactory(AccessUriInterface sms, ArrayList<String> phoneContact) {
        m_statistics = sms;
    }
    
    public AccessUriInterface getSms() {
        return m_statistics;
    }
    
    public void setSms(SmsDAO sms) {
        this.m_statistics = sms;
    }
}
