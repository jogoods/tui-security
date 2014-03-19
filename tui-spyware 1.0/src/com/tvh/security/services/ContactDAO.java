package com.tvh.security.services;

import java.util.ArrayList;
import java.util.List;

import com.tvh.security.models.*;
import com.tvh.security.utils.ContactUtils;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactDAO implements AccessUriInterface {
    
    private Context m_context;
    private Cursor m_cursor;
    
    public ContactDAO(Context context) {
        m_context = context;
        m_cursor = m_context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }
    
    public int getCount() {
        return m_cursor.getCount();
    }
    
    /*
     * Put message to the phone
     */
    public void insertAll(ContentValues values) {
        m_context.getContentResolver().insert(ContactsContract.Contacts.CONTENT_URI, values);
    }
    
    /*
     * Delete all message on the phone
     */
    public void deleteAll() {
        m_context.getContentResolver().delete(ContactsContract.Contacts.CONTENT_URI, null, null);
    }
    
    public void delete(ContentValues values) {
        
    }
    
    public List<Resources> getResources() {
        List<Resources> res = new ArrayList<Resources>();
        while (m_cursor.moveToNext()) {
            String contactId = m_cursor.getString(m_cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Phone phoneNumber = ContactUtils.getPhoneNumbers(m_context, contactId);
            StructuredName phoneName = ContactUtils.getStructuredName(m_context, contactId);
            Address address = ContactUtils.getAddresses(m_context, contactId);
            Email email = ContactUtils.getEmails(m_context, contactId);
            
            Contact contact = new Contact(contactId, phoneNumber, phoneName, address, email);
            res.add(contact);
        }
        return res;
    }
}
