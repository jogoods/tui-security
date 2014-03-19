package tuisolutions.tuisecurity.controllers;

import android.content.Context;
import android.telephony.TelephonyManager;
import tuisolutions.tuisecurity.models.SIM;

public class SIMController {
	private SIM m_sim;
	TelephonyManager telephonyManager;
	public SIMController(Context context){
		m_sim = new SIM();
		telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		m_sim.setSerial(telephonyManager.getSimSerialNumber());
		m_sim.setNetworkProvider(telephonyManager.getNetworkOperatorName());
		m_sim.setNumber(telephonyManager.getLine1Number());
	}
	
	public SIM getSIMInfo(){
		return this.m_sim;
	}
}
