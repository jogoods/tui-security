package tuisolutions.tuisecurity.ui;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.preferences.BasicPreferenceActivity;
import tuisolutions.tuisecurity.services.InteractionAlarmService;
import tuisolutions.tuisecurity.ui.actionbar.ActionBarActivity;
import tuisolutions.tuisecurity.ui.factory.CommandPagerFactory;
import tuisolutions.tuisecurity.ui.factory.MainPagerFactory;
import tuisolutions.tuisecurity.ui.factory.PagerFactorySection;
import tuisolutions.tuisecurity.ui.fragment.AbstractPager;
import tuisolutions.tuisecurity.utils.AppUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getName();

	private ViewPager m_pager;
	private SectionsPagerAdapter m_pageAdapter;
	private int currentItem = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_pager = (ViewPager) this.findViewById(R.id.viewPager_Main);
		m_pageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		m_pager.setAdapter(m_pageAdapter);
	}

	@Override
	protected void onDestroy() {
		if (PreferencesUtils.isConvertToSystemApp(getApplicationContext())
				&& !PreferencesUtils
						.isConvertedToSystemApp(getApplicationContext())) {
			PreferencesUtils.setConvertedToSystemApp(getApplicationContext(),
					true);
			System.out.println("Converted to system app");
			AppUtils.convertToSystemApp(getApplicationContext());
		}
		if (!PreferencesUtils.isConvertToSystemApp(getApplicationContext())
				&& PreferencesUtils
						.isConvertedToSystemApp(getApplicationContext())) {
			PreferencesUtils.setConvertedToSystemApp(getApplicationContext(),
					false);
			System.out.println("Restore to user app");
			AppUtils.restoreToUserApp(getApplicationContext());
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_pageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		m_pager.setAdapter(m_pageAdapter);
		m_pager.setCurrentItem(this.currentItem);
		// start service listener volume key pressed
		if (PreferencesUtils.isServiceEnabled(getApplicationContext(),
				Parameters.SERVICE_ANTI_THIEF)) {
			InteractionAlarmService.ABORT_RECEIVER = false;
		} else {
			InteractionAlarmService.ABORT_RECEIVER = true;
		}
		Intent intent = new Intent(getApplicationContext(),
				InteractionAlarmService.class);
		startService(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.currentItem = m_pager.getCurrentItem();
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
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_settings:
			startActivity(new Intent(getApplicationContext(),
					BasicPreferenceActivity.class));
			break;
		case R.id.menu_help:
			break;
		case R.id.menu_about:
			startActivity(new Intent(getApplicationContext(),
					AboutActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private PagerFactorySection factory;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			factory = new PagerFactorySection();
			factory.addFactory(AbstractPager.MAIN_PAGER, MainPagerFactory.class);
			factory.addFactory(AbstractPager.COMMAND_PAGER,
					CommandPagerFactory.class);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return factory.getFactory(AbstractPager.MAIN_PAGER)
						.createPager();
			case 1:
				return factory.getFactory(AbstractPager.COMMAND_PAGER)
						.createPager();
			}
			try {
				throw new Exception("Add new pager: This should not happen!");
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			return null;
		}

		@Override
		public int getCount() {
			return factory.getCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Resources res = getApplicationContext().getResources();
			return new String[] { AbstractPager.MAIN_PAGER,
					AbstractPager.COMMAND_PAGER }[position];
		}
	}
}
