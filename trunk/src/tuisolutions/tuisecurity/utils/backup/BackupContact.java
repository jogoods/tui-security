package tuisolutions.tuisecurity.utils.backup;

import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.JsonMarker;

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
