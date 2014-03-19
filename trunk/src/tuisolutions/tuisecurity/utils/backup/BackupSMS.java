package tuisolutions.tuisecurity.utils.backup;

import java.util.ArrayList;

import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.JsonMarker;


public class BackupSMS extends BackupRestoreFactory {
    
    /**
     * 
     * @param sms
     * @param phoneContact
     * @param filename
     */
    public BackupSMS(AccessUriInterface sms, ArrayList<String> phone) {
        super(sms, phone);
    }
    
    /**
     * 
     * @param sms
     * @param filename
     */
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
