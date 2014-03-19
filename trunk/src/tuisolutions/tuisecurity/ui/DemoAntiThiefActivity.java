package tuisolutions.tuisecurity.ui;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.models.Parameters;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

public class DemoAntiThiefActivity extends Activity {

	private SharedPreferences m_shared;
	private Switch m_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_demo_anti_thief);
		m_switch = (Switch) findViewById(R.id.feature_active_switch);
		m_shared = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isActive = m_shared.getBoolean(
				Parameters.SERVICE_ANTI_THIEF, false);
		m_switch.setChecked(isActive);

		m_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SharedPreferences.Editor editor = m_shared.edit();
				editor.putBoolean(Parameters.SERVICE_ANTI_THIEF, isChecked);
				editor.commit();
			}
		});
	}
}
