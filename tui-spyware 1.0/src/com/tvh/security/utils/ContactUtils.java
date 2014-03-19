package com.tvh.security.utils;

import com.tvh.security.models.Phone;
import com.tvh.security.services.Address;
import com.tvh.security.services.Email;
import com.tvh.security.services.StructuredName;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactUtils {
    private static final String TAG = SMSUtils.class.getName();

    //prevent instance
    private ContactUtils() {/*cannot be instance*/}

    public static Phone getPhoneNumbers(Context context, String contactId) {
        Phone phone = new Phone();
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] args = new String[]{contactId};
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                where, args, null);
        while (c.moveToNext()) {
            phone.setNumber(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            phone.setType(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
        }
        c.close();
        return phone;
    }

    public static Address getAddresses(Context context, String contactId) {
        Address address = new Address();
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                + "= ?";
        String[] args = new String[]{contactId};
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                null, where, args, null);
        while (c.moveToNext()) {
            address.setCity(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY)));
            address.setCountry(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)));
            address.setState(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)));
            address.setStreet(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)));
        }
        c.close();
        return address;
    }

    public static Email getEmails(Context context, String contactId) {
        Email email = new Email();
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                + "= ?";
        String[] args = new String[]{contactId};
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                where, args, null);
        while (c.moveToNext()) {
            email.setEmail(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
            email.setType(c.getString(c
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));

        }
        c.close();
        return email;
    }

    public static StructuredName getStructuredName(Context context,
                                                   String contactId) {
        StructuredName structuredName = new StructuredName();
        String where = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID
                + "=?";
        String[] args = new String[]{contactId};
        Cursor c = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, null, where, args,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (c.moveToNext()) {
            String display = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            String family = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String given = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            String middle = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
            String prefix = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
            String suffix = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));

            structuredName.setDisplayName(display);
            structuredName.setFamilyName(family);
            structuredName.setGivenName(given);
            structuredName.setMiddleName(middle);
            structuredName.setNamePrefix(prefix);
            structuredName.setNameSuffix(suffix);
        }
        c.close();
        return structuredName;
    }

    public static void deleteContact(Context context) {
        new DeleteContact(context).execute();
    }

    @SuppressWarnings("rawtypes")
    private static class DeleteContact extends AsyncTask {
        Context context;

        public DeleteContact(Context context) {
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Log.d("Delete Contact", "Deleting.....");
                return delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean delete() {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);
            while (cur.moveToNext()) {
                try {
                    String lookupKey = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri
                            .withAppendedPath(
                                    ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                    lookupKey);
                    cr.delete(uri, null, null);
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object result) {
            Log.d("Delete Contact", "Contact deleted.");
        }

    }
}
