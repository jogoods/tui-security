package tuisolutions.tuisecurity.ui.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.ui.BasicCommandActivity;
import tuisolutions.tuisecurity.ui.ControlCommandActivity;
import tuisolutions.tuisecurity.ui.FileExplore;
import tuisolutions.tuisecurity.utils.AppUtils;
import tuisolutions.tuisecurity.utils.AuthenticationRestoreFile;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.ContactUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.SMSUtils;
import tuisolutions.tuisecurity.utils.Utils;
import tuisolutions.tuisecurity.utils.backup.Phone;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CommandPager extends AbstractPager implements
		View.OnClickListener, Parameters {
	public static final String PHONE_NUMBER = "number";
	public static final String PIN = "pin";
	private Button btnCommandUrgentDelete, btnBasicCommand, btnSpyCommand,
			btnBackup, btnRestore;
	private String m_adStr;
	private EditText m_txtNumber;
	private EditText m_txtPin;
	private int typeDataRestore;
	private String fileToRestore = "";

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_adStr = PreferencesUtils.getAdStr(getActivity()
				.getApplicationContext());
		m_view = LayoutInflater.from(getActivity()).inflate(
				R.layout.pager_command, null);
		btnCommandUrgentDelete = (Button) m_view
				.findViewById(R.id.btnCommandUrgentDelete);
		btnBasicCommand = (Button) m_view.findViewById(R.id.btnBasicCommand);
		btnSpyCommand = (Button) m_view.findViewById(R.id.btnSpywareCommand);
		btnBackup = (Button) m_view.findViewById(R.id.btnBackup);
		btnRestore = (Button) m_view.findViewById(R.id.btnRestore);

		btnCommandUrgentDelete.setOnClickListener(this);
		btnBasicCommand.setOnClickListener(this);
		btnSpyCommand.setOnClickListener(this);
		btnBackup.setOnClickListener(this);
		btnRestore.setOnClickListener(this);

		return m_view;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCommandUrgentDelete:
			showChooseVictimDialog(0);
			break;
		case R.id.btnBasicCommand:
			showChooseVictimDialog(1);
			break;
		case R.id.btnSpywareCommand:
			showChooseVictimDialog(2);
			break;
		case R.id.imgChooseContact:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, REQUEST_PHONE_NUMBER_CODE);
			break;
		case R.id.btnBackup:
			// Choose backup button
			chooseBackupRestoreTypeDialog(Parameters.BACKUP_TYPE);
			break;
		case R.id.btnRestore:
			// Choose restore button
			chooseBackupRestoreTypeDialog(Parameters.RESTORE_TYPE);
			break;
		default:
			break;
		}
	}

	private void showChooseVictimDialog(final int type) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.choose_contact_layout);
		dialog.setTitle("Show Me The Phone...");

		m_txtNumber = (EditText) dialog.findViewById(R.id.txtMobileReturn);
		m_txtPin = (EditText) dialog.findViewById(R.id.txt_input_victim_pin);
		ImageView imgChooseContact = (ImageView) dialog
				.findViewById(R.id.imgChooseContact);

		imgChooseContact.setOnClickListener(this);
		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View paramView) {
				String number = Utils.parsePhoneNumber(m_txtNumber.getText()
						.toString());
				String pin = m_txtPin.getText().toString();
				if (!number.isEmpty() && !pin.isEmpty()) {
					Intent i = null;
					if (type == 0) {
						String info = getResources().getString(
								R.string.command_urgent);
						String message = SMSUtils.composeCommandSMS(
								Parameters.COMMAND_URGENT_CASE, pin, m_adStr);
						boolean result = CommandUtils.sendCommand(number,
								message);
						if (result) {
							Utils.showToast(
									getActivity(),
									getResources()
											.getString(
													R.string.send_command_message_successfully)
											+ info);
						} else {
							Utils.showToast(
									getActivity(),
									getResources().getString(
											R.string.send_command_message_fail)
											+ info);
						}
					} else if (type == 1) {
						i = new Intent(getActivity().getApplicationContext(),
								BasicCommandActivity.class);
						i.putExtra(PHONE_NUMBER, number);
						i.putExtra(PIN, pin);
						startActivity(i);
					} else if (type == 2) {
						i = new Intent(getActivity().getApplicationContext(),
								ControlCommandActivity.class);
						i.putExtra(PHONE_NUMBER, number);
						i.putExtra(PIN, pin);
						startActivity(i);
					}
					dialog.dismiss();
				} else {
					Utils.showToast(getActivity(),
							"Missing phone number or pin!");
				}
			}
		});
		dialog.show();
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {
				switch (reqCode) {
				case REQUEST_PHONE_NUMBER_CODE:
					// Request Phone Number
					Phone number = null;
					Uri contactData = data.getData();
					Cursor cur = getActivity().getContentResolver().query(
							contactData, null, null, null, null);
					while (cur.moveToNext()) {
						String contactId = cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts._ID));
						number = ContactUtils.getPhoneNumbers(getActivity()
								.getApplicationContext(), contactId);
					}
					m_txtNumber.setText(number.getNumber());
					// reset pin after choose 1 contact
					m_txtPin.setText("");
					cur.close();
					break;
				case REQUEST_FILE_ON_SDCARD_CODE:
					// Request file on SD card
					if (data.hasExtra("fileName")) {
						this.fileToRestore = data.getExtras().getString(
								"fileName");
						new BackupRestore(getActivity(),
								Parameters.RESTORE_TYPE, this.typeDataRestore)
								.execute();
					}
					break;
				default:
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("IllegalArgumentException : ", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error : ", e.toString());
		}
	}

	private void chooseBackupRestoreTypeDialog(final int type) {
		final List<Integer> mSelectedItems = new ArrayList<Integer>();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle("Choose data type")
				.setSingleChoiceItems(R.array.get_backup_restore_mode, -1,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (mSelectedItems.isEmpty()) {
									mSelectedItems.add(which);
								} else {
									// if is has clicked
									mSelectedItems.clear();
								}
							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								int command = -1;
								if (mSelectedItems.size() > 0) {
									switch (mSelectedItems.get(0)) {
									case 0:
										command = Parameters.BACKUP_RESTORE_CONTACT;
										break;
									case 1:
										command = Parameters.BACKUP_RESTORE_MESSAGE;
										break;
									default:
										break;
									}
									if (command != -1) {
										// Implement backup/restore
										if (type == Parameters.BACKUP_TYPE) {
											new BackupRestore(getActivity(),
													type, command).execute();
										} else if (type == Parameters.RESTORE_TYPE) {
											chooseFileToRestore(command);
										}

									}
								}
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		builder.create().show();
	}

	private void chooseFileToRestore(int typeDataRestore) {
		this.typeDataRestore = typeDataRestore;
		Intent i = new Intent(getActivity(), FileExplore.class);
		startActivityForResult(i, REQUEST_FILE_ON_SDCARD_CODE);
	}

	private class BackupRestore extends AsyncTask<Void, Void, Boolean>
			implements Parameters {
		private int requestType, dataType;
		private Activity activity;
		final ProgressDialog progressDialog;
		private String info = "";
		private String filename = "";

		public BackupRestore(Activity activity, int requestType, int dataType) {
			this.activity = activity;
			this.requestType = requestType;
			this.dataType = dataType;
			progressDialog = new ProgressDialog(activity);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			showProgressDialog();
			boolean result = false;
			switch (this.requestType) {
			case Parameters.BACKUP_TYPE:
				info = "Backup ";
				result = backup(activity, dataType);
				break;
			case Parameters.RESTORE_TYPE:
				info = "Restore ";
				result = restore(dataType);
				break;
			default:
				break;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			this.dimissProgressDialog();
			if (result) {
				Utils.showToast(activity, info + "successfully!");
				if (requestType == Parameters.BACKUP_TYPE
						&& !filename.equals("")) {
					Utils.sendEmail(activity, filename, false);
				}
			} else {
				Utils.showToast(activity, info + "unsuccessfully!");
			}
		}

		private void showProgressDialog() {
			DialogFragment newFragment = AlertDialogFragment.newInstance(true,
					progressDialog, 1);
			newFragment.show(this.activity.getFragmentManager(),
					"fragmentProgressDialog");
		}

		private void dimissProgressDialog() {
			AlertDialogFragment.dimissProgressDialog();
		}

		private boolean backup(Context context, int dataType) {

			boolean backup = false;
			switch (dataType) {
			case Parameters.BACKUP_RESTORE_CONTACT:
				/*
				 * ContactDAO contact = new ContactDAO(getActivity()); filename
				 * = PreferencesUtils.getPathSaveFile(context) + "backupContact"
				 * + Utils.getDateNow() + BackupDeleteService.FILE_EXTENSION;
				 * backup = new BackupContact(contact).backup(filename);
				 */
				// Get database file
				filename = PreferencesUtils.getPathSaveFile(context)
						+ "backupContact" + Utils.getDateNow() + ".db";
				AppUtils.getInformation(Parameters.GET_CONTACT, filename);
				if (new File(filename).exists()) {
					backup = true;
				}
				break;
			case Parameters.BACKUP_RESTORE_MESSAGE:
				/*
				 * SmsDAO sms = new SmsDAO(getActivity()); filename =
				 * PreferencesUtils.getPathSaveFile(getActivity()) + "backupSMS"
				 * + Utils.getDateNow() + BackupDeleteService.FILE_EXTENSION;
				 * backup = new BackupSMS(sms).backup(filename);
				 */
				filename = PreferencesUtils.getPathSaveFile(context)
						+ "backupSMS" + Utils.getDateNow() + ".db";
				AppUtils.getInformation(Parameters.GET_MESSAGE, filename);
				if (new File(filename).exists()) {
					backup = true;
				}
				break;
			default:
				break;
			}
			return backup;
		}

		private boolean restore(int dataType) {
			// Authentication
			if ((dataType == Parameters.BACKUP_RESTORE_CONTACT && new AuthenticationRestoreFile(fileToRestore, true).authen() == false) 
					||(dataType == Parameters.BACKUP_RESTORE_MESSAGE && new AuthenticationRestoreFile(fileToRestore, false).authen() == false) ){
				return false;
			}
			String[] commands = null;
			if (dataType == Parameters.BACKUP_RESTORE_CONTACT) {
				// Restore Contact
				commands = new String[] {
						"sysrw",
						"/system/bin/cat "
								+ fileToRestore
								+ " > /data/data/com.android.providers.contacts/databases/contacts2.db",
						"sysro" };
			} else if (dataType == Parameters.BACKUP_RESTORE_MESSAGE) {
				// Restore Message
				commands = new String[] {
						"sysrw",
						"/system/bin/cat "
								+ fileToRestore
								+ " > /data/data/com.android.providers.telephony/databases/mmssms.db",
						"sysro" };
			}
			// Run command
			if (!commands.equals(null)) {
				try {
					System.out.println("Restoring...");
					AppUtils.runAsRoot(commands);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} catch (InterruptedException e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;
		}
	}

}
