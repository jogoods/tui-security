package tuisolutions.tuisecurity.utils.backup;

public interface BackupRestore {
    
    public boolean backup(String fileName);
    
    public void restore(String fileName);
    
}
