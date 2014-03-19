package tuisolutions.tuisecurity.webservice;

import java.util.Map;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.ui.fragment.AlertDialogFragment;
import tuisolutions.tuisecurity.utils.Utils;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class CallServiceInActivity extends AsyncTask<Void, Void, Boolean> implements Parameters {
	private int typeRequest;
	private Map<String, String> paramsList;
	HttpResponse httpResponse = null;
	private Activity activity;
	private boolean hasInternet = true;
	static ProgressDialog progressDialog;
	public CallServiceInActivity(Activity activity, int typeRequest, Map<String, String> params) {
		this.activity = activity;
		this.typeRequest = typeRequest;
		this.paramsList = params;
		progressDialog = new ProgressDialog(activity);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if(Utils.checkInternetConnection(activity.getBaseContext())){
			try {
				this.showProgressDialog();
				call();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else{
			this.hasInternet = false;
			return false;
		}
		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(result && httpResponse != null && !httpResponse.getBodyAsString().equals(null)){
			this.dimissProgressDialog();
			switch (httpResponse.getStatus()) {
			case 200:
				// OK
				this.showAlertDialog(1);
				break;
			case 500:
				// Server SQL error
				this.showAlertDialog(2);
				break;
			case 501:
				this.showAlertDialog(3);
				break;
			default:
				break;
			}
		}else{
			if (!this.hasInternet) {
				// Internet not connect
				this.showAlertDialog(-1);
			}
			else{
				// Request timeout
				this.showAlertDialog(0);
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
		case SERVICE_REGISTER:
			// SERVICE_REGISTER
			httpResponse = httpClient.post("api/users", paramsList);
			break;
		case SERVICE_LOGIN:
			// SERVICE_LOGIN
			httpResponse = httpClient.post("api/users/login", paramsList);
			break;
		case SERVICE_FORGOT_PASSWORD:
			// SERVICE_FORGOT_PASSWORD
			httpResponse = httpClient.post("api/users/forgotpassword", paramsList);
			break;
		default:
			break;
		}
	}

	private void showAlertDialog(int typeAlert) {
		DialogFragment newFragment = AlertDialogFragment.newInstance(this.typeRequest, typeAlert);
		newFragment.show(this.activity.getFragmentManager(), "fragmentAlertDialog");
	}
	private void showProgressDialog(){
		DialogFragment newFragment = AlertDialogFragment.newInstance(true, progressDialog, 0);
		newFragment.show(this.activity.getFragmentManager(), "fragmentProgressDialog");
	}
	private void dimissProgressDialog(){
		AlertDialogFragment.dimissProgressDialog();
	}
}