package tuisolutions.tuisecurity.ui.fragment;

import tuisolutions.tuisecurity.ui.MainActivity;
import android.support.v4.app.Fragment;
import android.view.View;


public abstract class AbstractPager extends Fragment {

	public static final String MAIN_PAGER = "Main Security";
	public static final String COMMAND_PAGER = "Commander";

	protected MainActivity m_activity;
	protected View m_view;

	public AbstractPager() {
		super();
	}

	public void initFragment(MainActivity activity, View view) {
		m_activity = activity;
		m_view = view;
	}
}
