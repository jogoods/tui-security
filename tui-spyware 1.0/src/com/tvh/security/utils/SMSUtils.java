package com.tvh.security.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tvh.security.models.CommandMessage;


public class SMSUtils {

    private static final String TAG = SMSUtils.class.getName();

    /**
     * Delete all SMS in phone number
     *
     * @param context context of programs
     */
    public static void deleteSMS(Context context) {
        new DeleteSMS(context).execute();
    }

    public static void sendSMS(String number, String content) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, content, null, null);
    }

    /**
     * Get List of Message in Intent
     *
     * @param intent input
     * @return List of Message
     */
    public static List<CommandMessage> getMessages(Intent intent) {
        List<CommandMessage> smses = new ArrayList<CommandMessage>();
        Bundle bdl = intent.getExtras();
        try {
            Object pdus[] = (Object[]) bdl.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                byte[] byteData = (byte[]) pdus[i];
                SmsMessage msg = SmsMessage.createFromPdu(byteData);
                String content = msg.getMessageBody().trim().toLowerCase();
                String sender = msg.getOriginatingAddress();
                CommandMessage sms = new CommandMessage(sender, content);
                smses.add(sms);
            }
        } catch (Exception e) {
        }
        return smses;
    }

    @SuppressWarnings("rawtypes")
    private static class DeleteSMS extends AsyncTask {
        Context context;

        public DeleteSMS(Context context) {
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Log.d("Delete SMS", "Deleting.....");
                return delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean delete() {
            Uri deleteUri = Uri.parse("content://sms/");
            Cursor c = context.getContentResolver().query(deleteUri, null, null, null, null);
            while (c.moveToNext()) {
                Uri uri = Uri.parse("content://sms/" + c.getInt(c.getColumnIndex("_id")));
                context.getContentResolver().delete(uri, null, null);

            }
            return true;
        }

        @Override
        protected void onPostExecute(Object result) {
            Log.d("Delete SMS", "SMS deleted.");
        }

    }
}
