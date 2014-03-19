package tuisolutions.tuisecurity.ui;

import java.util.ArrayList;
import java.util.List;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.preferences.BasicPreferenceActivity;
import tuisolutions.tuisecurity.ui.actionbar.ActionBarActivity;
import tuisolutions.tuisecurity.ui.fragment.CommandPager;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.SMSUtils;
import tuisolutions.tuisecurity.utils.Utils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

@SuppressLint("ShowToast")
public class BasicCommandActivity extends ActionBarActivity implements View.OnClickListener {
    private Button btnPhoneRing, btnDeleteSMS, btnDeleteContact, btnFormatSDCard;
    private String m_number;
    private String m_adStr;
    private String m_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_command_include_basic);
        m_number = getIntent().getStringExtra(CommandPager.PHONE_NUMBER);
        m_pin = getIntent().getStringExtra(CommandPager.PIN);
        m_adStr = PreferencesUtils.getAdStr(getApplicationContext());

        btnPhoneRing = (Button) findViewById(R.id.btnPhoneRing);
        btnDeleteSMS = (Button) findViewById(R.id.btnDeleteSMS);
        btnDeleteContact = (Button) findViewById(R.id.btnDeleteContact);
        btnFormatSDCard = (Button) findViewById(R.id.btnFormatSDCard);
        btnPhoneRing.setOnClickListener(this);
        btnFormatSDCard.setOnClickListener(this);

        btnDeleteContact.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ChooseDeleteTypeDialog dialog = new ChooseDeleteTypeDialog(Parameters.COMMAND_BACKUP_DELETE_CONTACT);
                dialog.show(getFragmentManager(), "DataTypeDialog");
            }
        });
        btnDeleteSMS.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ChooseDeleteTypeDialog dialog = new ChooseDeleteTypeDialog(Parameters.COMMAND_BACKUP_DELETE_MESSAGE);
                dialog.show(getFragmentManager(), "DataTypeDialog");
            }
        });
    }

    public void onClick(View v) {
        String command = null;
        String info = " ";
        switch (v.getId()) {
        case R.id.btnPhoneRing:
            command = Parameters.COMMAND_ALARM;
            info += "Alarm";
            break;
        case R.id.btnFormatSDCard:
            command = Parameters.COMMAND_FORMAT_SD_CARD;
            info += "Format SD Card";
            break;
        default:
            break;
        }
        String message = SMSUtils.composeCommandSMS(command, m_pin, m_adStr);
        boolean result = CommandUtils.sendCommand(m_number, message);
        if (result) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_successfully) + ": " + info);
        } else {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_fail) + ": " + info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Utils.showToast(getApplicationContext(), "Tapped home");
            finish();
            break;
        case R.id.menu_settings:
            startActivity(new Intent(getApplicationContext(), BasicPreferenceActivity.class));
        case R.id.menu_help:
            break;
        case R.id.menu_about:
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChooseDeleteTypeDialog extends DialogFragment {
        private List<Integer> mSelectedItems = new ArrayList<Integer>();
        String backupType = "";

        public ChooseDeleteTypeDialog(String backupType) {
            super();
            this.backupType = backupType;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Delete after backup").setSingleChoiceItems(R.array.get_backup_delete_mode, 1, new DialogInterface.OnClickListener() {

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
                    String command = backupType;
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                        case 0:
                            // Delete after backup
                            if (backupType.equals(Parameters.COMMAND_BACKUP_DELETE_MESSAGE)) {
                                command = Parameters.COMMAND_BACKUP_DELETE_MESSAGE_DELETE;
                            } else if (backupType.equals(Parameters.COMMAND_BACKUP_DELETE_CONTACT)) {
                                command = Parameters.COMMAND_BACKUP_DELETE_CONTACT_DELETE;
                            }
                            break;
                        case 1:
                            // Not delete after backup
                            command = backupType;
                            break;
                        default:
                            break;
                        }
                    }
                    // Implement backup/restore
                    System.out.println(command);

                    String message = SMSUtils.composeCommandSMS(command, m_pin, m_adStr);
                    boolean result = CommandUtils.sendCommand(m_number, message);
                    String info = backupType.equals(Parameters.COMMAND_BACKUP_DELETE_CONTACT) ? "Backup Contact" : "Backup Message";
                    if (result) {
                        Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_successfully) + ": " + info);
                    } else {
                        Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_fail) + ": " + info);
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
