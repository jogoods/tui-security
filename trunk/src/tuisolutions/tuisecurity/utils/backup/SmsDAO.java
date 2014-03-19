package tuisolutions.tuisecurity.utils.backup;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsDAO implements AccessUriInterface {
    // Content for SMS , these may change in future SDK.
    private static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    private Context m_context;
    
    public SmsDAO(Context context) {
        m_context = context;
    }
    
    /*
     * get sum of message on the phone
     * 
     * @return: sum of message
     */
    public int getCount() {
        Cursor cursor = m_context.getContentResolver().query(SMS_CONTENT_URI, null, null, null, null);
        return cursor.getCount();
    }
    
    /*
     * Get Cursor of Message
     */
    public Cursor getCursor() {
        return m_context.getContentResolver().query(SMS_CONTENT_URI, null, null, null, null);
    }
    
    /*
     * Put message to the phone
     */
    public void insertAll(ContentValues values) {
        m_context.getContentResolver().insert(SMS_CONTENT_URI, values);
    }
    
    /*
     * Delete all message on the phone
     */
    public void deleteAll() {
        m_context.getContentResolver().delete(SMS_CONTENT_URI, null, null);
    }
    
    public void delete(ContentValues values) {
        
    }
    
    public List<Resources> getResources() {
        List<Resources> res = new ArrayList<Resources>();
        Cursor c = getCursor();
        while (c.moveToNext()) {
            String address = c.getString(c.getColumnIndex("address"));
            Long date = c.getLong(c.getColumnIndex("date"));
            String content = c.getString(c.getColumnIndex("body"));
            int read = c.getInt(c.getColumnIndex("read"));
            int locked = c.getInt(c.getColumnIndex("locked"));
            int status = c.getInt(c.getColumnIndex("status"));
            String type = c.getString(c.getColumnIndex("type"));
            Message msg = new Message(address, date, content, read, locked, status, type);
            res.add(msg);
        }
        return res;
    }
}
