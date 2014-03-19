package tuisolutions.tuisecurity.ui.factory;

import tuisolutions.tuisecurity.ui.fragment.AbstractPager;
import tuisolutions.tuisecurity.ui.fragment.CommandPager;

public class CommandPagerFactory implements IPagerFactory{

    public AbstractPager createPager() {
        return new CommandPager();
    }
}
