package tuisolutions.tuisecurity.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.ui.DemoAntiThiefActivity;
import tuisolutions.tuisecurity.ui.DemoBasicFeaturesActivity;
import tuisolutions.tuisecurity.ui.DemoPhoneFindingActivity;
import tuisolutions.tuisecurity.ui.DemoRemotePhoneActivity;
import tuisolutions.tuisecurity.ui.DemoSimChangeAlertActivity;
import tuisolutions.tuisecurity.ui.SetupHelperActivity;
import tuisolutions.tuisecurity.utils.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainPager extends AbstractPager implements View.OnClickListener {
	public static final String TAG = MainPager.class.getSimpleName();

	private SharedPreferences m_preferences;
	private boolean m_isEnable = false;
	private boolean m_isSetting = false;
	private Button m_btnActive;
	private Button m_btnDeactivate;
	private TextView m_tvInfo;
	private List<LinearLayout> m_items;
	private List<Boolean> m_service;
	private ImageView m_imgLock;
	private View m_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_view = LayoutInflater.from(getActivity()).inflate(
				R.layout.pager_main, null);
		m_preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		m_tvInfo = (TextView) m_view.findViewById(R.id.tv_tips);
		m_btnActive = (Button) m_view.findViewById(R.id.btn_activate);
		m_btnDeactivate = (Button) m_view.findViewById(R.id.btn_deactivate);
		m_imgLock = (ImageView) m_view.findViewById(R.id.img_lock);
		m_items = new ArrayList<LinearLayout>();
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item1));
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item2));
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item3));
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item4));
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item5));
		m_items.add((LinearLayout) m_view.findViewById(R.id.main_item6));
		// checking first
		m_isEnable = m_preferences.getBoolean(Parameters.IS_ACTIVATED, false);
		if (m_isEnable) {
			setCheckEnable(true);
		} else {
			setCheckEnable(false);
		}

		m_btnActive.setOnClickListener(this);
		m_btnDeactivate.setOnClickListener(this);
		return m_view;

	}

	@Override
	public boolean getUserVisibleHint() {
		System.out.println("Hidden");
		return super.getUserVisibleHint();
	}

	@Override
	public void onResume() {
		super.onResume();
		m_isEnable = m_preferences.getBoolean(Parameters.IS_ACTIVATED, false);
		if (m_isEnable) {
			setCheckEnable(true);
		} else {
			setCheckEnable(false);
		}
	}

	private void checkService() {
		m_isEnable = m_preferences.getBoolean(Parameters.IS_ACTIVATED, false);
		m_isSetting = m_preferences.getBoolean(Parameters.IS_SET_SETTINGS,
				false);
		m_service = new ArrayList<Boolean>();
		Boolean server1 = m_preferences.getBoolean(
				Parameters.SERVICE_BASIC_FEATURES, false);
		Boolean server2 = m_preferences.getBoolean(
				Parameters.SERVICE_PHONE_ALERT, false);
		Boolean server3 = m_preferences.getBoolean(
				Parameters.SERVICE_ANTI_THIEF, false);
		Boolean server4 = m_preferences.getBoolean(
				Parameters.SERVICE_PHONE_FINDING, false);
		Boolean server5 = m_preferences.getBoolean(
				Parameters.SERVICE_PHONE_SPYING, false);
		Boolean server6 = m_preferences.getBoolean(
				Parameters.SERVICE_WEB_SERVICE, false);
		m_service.add(server1);
		m_service.add(server2);
		m_service.add(server3);
		m_service.add(server4);
		m_service.add(server5);
		m_service.add(server6);
	}

	private void initView() {

		for (int i = 0; i < m_items.size(); i++) {
			TextView title = (TextView) m_items.get(i).findViewById(
					R.id.main_item_title);
			title.setText(Parameters.FEATURES.get(i));
			ImageView imgIcon = (ImageView) m_items.get(i).findViewById(
					R.id.main_item_icon);
			String strTitle = (String) title.getText();
			if (Parameters.SERVICE_BASIC_FEATURES.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_basic_feature_icon);
				m_items.get(i).setOnClickListener(this);
			} else if (Parameters.SERVICE_PHONE_ALERT.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_alarm_feature_icon);
				m_items.get(i).setOnClickListener(this);
			} else if (Parameters.SERVICE_PHONE_SPYING.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_spy_feature_icon);
				m_items.get(i).setOnClickListener(this);
			} else if (Parameters.SERVICE_WEB_SERVICE.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_web_service_icon);
				m_items.get(i).setOnClickListener(this);
			} else if (Parameters.SERVICE_PHONE_FINDING.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_finding_feature_icon);
				m_items.get(i).setOnClickListener(this);
			} else if (Parameters.SERVICE_ANTI_THIEF.equals(strTitle)) {
				imgIcon.setImageResource(R.drawable.item_anti_thief_icon);
				m_items.get(i).setOnClickListener(this);
			}

			ImageView imgStatus = (ImageView) m_items.get(i).findViewById(
					R.id.main_item_status);

			if (m_isEnable) {
				if (m_service.get(i) == true) {
					imgStatus.setImageResource(R.drawable.item_service_run);
				} else {
					imgStatus.setImageResource(R.drawable.item_service_norun);
				}
			} else {
				imgStatus.setImageResource(R.drawable.item_service_disable);
			}

		}
	}

	/**
	 * set check enable change status for button Active
	 * 
	 * @param enable
	 *            true if become activated
	 */
	public void setCheckEnable(Boolean enable) {
		String info;
		if (enable) {
			info = getResources().getString(R.string.main_pager_activated_tip);
			m_tvInfo.setText(info);
			m_btnActive.setVisibility(View.GONE);
			m_btnDeactivate.setVisibility(View.VISIBLE);
			m_imgLock.setImageResource(R.drawable.item_phone_lock);

		} else {
			info = getResources().getString(R.string.main_pager_activate_tip);
			m_tvInfo.setText(info);
			m_btnDeactivate.setVisibility(View.GONE);
			m_btnActive.setVisibility(View.VISIBLE);
			m_imgLock.setImageResource(R.drawable.item_phone_unlock);
		}
		checkService();
		initView();
		Log.v(TAG, "Commit enable apps " + enable);
	}

	@SuppressWarnings("deprecation")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_activate:
			if (m_isSetting) {
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(getActivity()).edit();
				editor.putBoolean(Parameters.IS_ACTIVATED, true);
				// Set enabled SERVICES
				editor.putBoolean(Parameters.SERVICE_BASIC_FEATURES, true);
				editor.putBoolean(Parameters.SERVICE_PHONE_ALERT, true);
				editor.putBoolean(Parameters.SERVICE_ANTI_THIEF, true);
				editor.putBoolean(Parameters.SERVICE_PHONE_FINDING, true);
				editor.commit();
			} else {
				Intent i = new Intent(getActivity(), SetupHelperActivity.class);
				startActivityForResult(i, 0);
			}
			setCheckEnable(true);
			break;
		case R.id.btn_deactivate:
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
					.create();
			alertDialog.setTitle("Deactivate...");
			alertDialog.setMessage("Your phone may be unsafe! Are you sure?");
			alertDialog.setButton("Sure",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences.Editor editor = PreferenceManager
									.getDefaultSharedPreferences(getActivity())
									.edit();
							editor.putBoolean(Parameters.IS_ACTIVATED, false);
							// Setting deactive SERVICES
							editor.putBoolean(
									Parameters.SERVICE_BASIC_FEATURES, false);
							editor.putBoolean(Parameters.SERVICE_PHONE_ALERT,
									false);
							editor.putBoolean(Parameters.SERVICE_ANTI_THIEF,
									false);
							editor.putBoolean(Parameters.SERVICE_PHONE_FINDING,
									false);
							editor.putBoolean(Parameters.SERVICE_PHONE_SPYING,
									false);
							editor.commit();
							setCheckEnable(false);
						}
					});
			alertDialog.setButton2("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Utils.showToast(getActivity(), "Cancel");
						}
					});
			// alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
			break;
		case R.id.main_item1:
			startDemoActivity(DemoBasicFeaturesActivity.class);
			break;
		case R.id.main_item2:
			startDemoActivity(DemoSimChangeAlertActivity.class);
			break;
		case R.id.main_item3:
			startDemoActivity(DemoAntiThiefActivity.class);
			break;
		case R.id.main_item4:
			startDemoActivity(DemoPhoneFindingActivity.class);
			break;
		case R.id.main_item5:
			startDemoActivity(DemoRemotePhoneActivity.class);
			break;
		/*
		 * case R.id.main_item6:
		 * startDemoActivity(RemoteDeviceDemoActivity.class); break;
		 */

		default:
			break;
		}
	}

	private void startDemoActivity(Class<?> clz) {
		if (m_isEnable) {
			Intent i = new Intent(getActivity(), clz);
			startActivity(i);
		} else {
			Utils.showToast(getActivity(), "You must active this apps");
		}
	}
}
