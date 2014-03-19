package tuisolutions.tuisecurity.utils.backup;

import java.util.List;

import android.content.ContentValues;

public interface AccessUriInterface {
    
    public List<Resources> getResources();
    
    public int getCount();
    
    public void insertAll(ContentValues values);
    
    public void deleteAll();
    
    public void delete(ContentValues values);
}
