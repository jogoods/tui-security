package tuisolutions.tuisecurity.services;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.utils.AppUtils;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.ContactUtils;
import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.SMSUtils;
import tuisolutions.tuisecurity.utils.Utils;
import tuisolutions.tuisecurity.utils.backup.BackupContact;
import tuisolutions.tuisecurity.utils.backup.BackupSMS;
import tuisolutions.tuisecurity.utils.backup.ContactDAO;
import tuisolutions.tuisecurity.utils.backup.SmsDAO;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


//this is the service which runs by receiving the correct SMS
public class BackupDeleteService extends Service {
    private static final String TAG = BackupDeleteService.class.getName();
    public static final String FILE_EXTENSION = ".txt";
    public static final String SERVICE_TYPE = "type";
    public static final String PHONE_SENDER = "phone sender";
    public static final String DELETE_AFTER_BACKUP = "delete after backup";
    
    public static final int URGENT_CASE = 1;
    public static final int BACKUP_DELETE_MESSAGE = 2;
    public static final int BACKUP_DELETE_CONTACT = 3;
    public static final int FORMAT_SD_CARD = 4;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
    }
    
    @SuppressWarnings("deprecation")
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        int type = intent.getIntExtra(SERVICE_TYPE, -1);
        boolean deleteAfterBackup = intent.getBooleanExtra(DELETE_AFTER_BACKUP, false);
        String sender = intent.getStringExtra(PHONE_SENDER);
        Log.v(TAG, "OnStart Backup Delete Service");
        String filename;
        switch (type) {
            case URGENT_CASE:
            	new UrgentCase(getApplicationContext()).execute();
                break;
            case BACKUP_DELETE_MESSAGE:
                Utils.showToast(getApplicationContext(), "BackupSMS");
                SmsDAO sms = new SmsDAO(getApplicationContext());
                filename = PreferencesUtils.getPathSaveFile(getApplicationContext()) + "backupSMS" + Utils.getDateNow() + FILE_EXTENSION;
                boolean backupsms = new BackupSMS(sms).backup(filename);
                if (backupsms) {
                    Utils.sendEmail(getApplicationContext(), filename, true);
                } else {
                    CommandUtils.sendCommand(sender, "Backup message not successfully");
                }
                if(deleteAfterBackup){
                	// Delete sms
                	SMSUtils.deleteSMS(getApplicationContext());
                }
                break;
            case BACKUP_DELETE_CONTACT:
                Utils.showToast(getApplicationContext(), "Backup Contact");
                ContactDAO contact = new ContactDAO(getApplicationContext());
                filename = PreferencesUtils.getPathSaveFile(getApplicationContext()) + "backupContact" + Utils.getDateNow() + FILE_EXTENSION;
                boolean backup = new BackupContact(contact).backup(filename);
                if (backup) {
                    Utils.sendEmail(getApplicationContext(), filename, true);
                    // TODO: Delete Contact
                } else {
                    CommandUtils.sendCommand(sender, "Backup contact not successfully");
                }
                if(deleteAfterBackup){
                	// Delete contact
                	ContactUtils.deleteContact(getApplicationContext());
                }
                break;
            case FORMAT_SD_CARD:
                FileUtils.formatDSCard();
                break;
            
            default:
                break;
        }
        stopSelf();
    }
    private class UrgentCase extends AsyncTask<Void, Void, Void>{
    	Context context;
    	public UrgentCase(Context context) {
    		this.context = context;
    	}
		@Override
		protected Void doInBackground(Void... params) {
			// Delete SD card
        	FileUtils.formatDSCard();
        	// Delete appications data
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_CONTACT, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_MESSAGE, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_MAP_HISTORY, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_YOUTUBE_HISTORY, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_YAHOO_HISTORY, context);
        	AppUtils.deleteDatabaseFile(Parameters.DElETE_FACEBOOK_DB, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_ACCOUNT, context);
        	AppUtils.deleteDatabaseFile(Parameters.DELETE_CALENDAR, context);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("Urgent case delete all successfully.");
		}
    	
    }
}
