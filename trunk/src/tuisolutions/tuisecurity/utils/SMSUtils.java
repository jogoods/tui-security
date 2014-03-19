package tuisolutions.tuisecurity.utils;

import java.util.ArrayList;
import java.util.List;

import tuisolutions.tuisecurity.models.CommandMessage;
import tuisolutions.tuisecurity.models.Parameters;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;


public class SMSUtils {
    
    private static final String TAG = SMSUtils.class.getName();
    
    /**
     * compose SMS
     * 
     * @param command
     *            list of command to embed
     * @param adStr
     *            adversity string be embed
     * @return content of SMS
     */
    @SuppressLint("DefaultLocale")
	public static String composeCommandSMS(String command, String SMSCode, String adStr) {
        String content = adStr;
        String preCommand = Parameters.DK_STRING;
        String postCommand = Parameters.TO_SMS_PHONE;
        if (command != null && !command.isEmpty()) {
            content = content + " " + preCommand + " " + command.toUpperCase() + " " + SMSCode + " " + postCommand;
        }
        return content;
    }
    
    /**
     * Delete all SMS in phone number
     * 
     * @param context
     *            context of programs
     */
    public static void deleteSMS(Context context) {
        new DeleteSMS(context).execute();
    }
    
    /**
     * Get List of Message in Intent
     * 
     * @param intent
     *            input
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
