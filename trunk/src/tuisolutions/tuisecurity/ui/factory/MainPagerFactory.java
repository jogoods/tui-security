package tuisolutions.tuisecurity.ui.factory;

import tuisolutions.tuisecurity.ui.fragment.AbstractPager;
import tuisolutions.tuisecurity.ui.fragment.MainPager;

public class MainPagerFactory implements IPagerFactory {

	public AbstractPager createPager() {
		return new MainPager();
	}
}
