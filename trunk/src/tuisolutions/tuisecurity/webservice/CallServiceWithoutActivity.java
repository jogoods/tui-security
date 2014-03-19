package tuisolutions.tuisecurity.webservice;

import java.util.Map;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;

import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class CallServiceWithoutActivity extends AsyncTask<Void, Void, Boolean>
		implements Parameters {
	Context context;
	private int typeRequest;
	private Map<String, String> paramsList;
	HttpResponse httpResponse = null;
	private boolean hasInternet = true;

	public CallServiceWithoutActivity(Context context, int typeRequest,
			Map<String, String> params) {
		this.context = context;
		this.typeRequest = typeRequest;
		this.paramsList = params;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		// With
		// Slepp 30s to device enable internet service and get SIM information,
		// then check internet
		// connection and send request
		try {
			Thread.sleep(30000);
			if (Utils.checkInternetConnection(context)) {
				try {
					call();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			} else {
				this.hasInternet = false;
				return false;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	protected void onPostExecute(Boolean result) {
		if (result && httpResponse != null
				&& !httpResponse.getBodyAsString().equals(null)) {
			switch (httpResponse.getStatus()) {
			case 200:
				// OK
				break;
			case 500:
				// Server SQL error
				break;
			case 501:
				break;
			default:
				break;
			}
		} else {
			if (!this.hasInternet) {
				// Internet not connect
				// Not implement
			} else {
				// Request timeout
				// Send request again
			}
		}
	}

	private void call() throws Exception {
		AndroidHttpClient httpClient = new AndroidHttpClient(
				"http://www.tuisolutions.com.vn/index.php/");
		httpClient.setConnectionTimeout(10000);
		ParameterMap paramsList = httpClient.newParams();

		for (Map.Entry<String, String> e : this.paramsList.entrySet()) {
			paramsList.add(e.getKey(), e.getValue());
		}

		switch (this.typeRequest) {
		case SERVICE_SEND_SMS:
			/**
			 * Service request server send SMS when SIM change. Param list
			 * contain: 1. Username: used to login 2. Buddy number. 3. Contain
			 * of sms. 4. MD5 password
			 */
			System.out.println("Sending request to server!");
			httpResponse = httpClient.post("api/users/sendsms", paramsList);
			break;
		default:
			break;
		}
	}
}
