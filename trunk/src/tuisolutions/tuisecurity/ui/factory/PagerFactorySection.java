package tuisolutions.tuisecurity.ui.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class PagerFactorySection {
	private static final String TAG = "PageFactorySection";

	@SuppressWarnings("rawtypes")
	private Map<String, Class> classMap;

	@SuppressWarnings("rawtypes")
	public PagerFactorySection() {
		classMap = new HashMap<String, Class>();
	}

	@SuppressWarnings("rawtypes")
	public void addFactory(String name, Class factory) {
		classMap.put(name, factory);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPagerFactory getFactory(String name) {
		Class fac = classMap.get(name);
		if (fac != null) {

			try {
				Constructor constructor = fac
						.getDeclaredConstructor(new Class[] {});
				return (IPagerFactory) constructor.newInstance(new Object[] {});
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return null;
	}

	public int getCount() {
		return classMap.size();
	}
}
