package tuisolutions.tuisecurity.ui.fragment;

import tuisolutions.tuisecurity.ui.SetupHelperActivity;
import tuisolutions.tuisecurity.webservice.CallServiceInActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class AlertDialogFragment extends DialogFragment {
	static ProgressDialog progressDialog = null;

	public static AlertDialogFragment newInstance(boolean isProgressDialog,
			ProgressDialog dialog, int typeProgress) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("isProgressDialog", isProgressDialog);
		args.putInt("typeProgress", typeProgress);
		progressDialog = dialog;
		frag.setArguments(args);
		return frag;
	}

	public static AlertDialogFragment newInstance(int typeRequest, int typeAlert) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putInt("typeRequest", typeRequest);
		args.putInt("typeAlert", typeAlert);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		boolean isProgressDialog = getArguments().getBoolean("isProgressDialog");
		if (isProgressDialog) {
			int typeProgress = getArguments().getInt("typeProgress");// Send request or implementing
			switch (typeProgress) {
			case 0:
				// Sending request to server
				progressDialog.setTitle("Sending request");
			    progressDialog.setMessage("Please wait...");
				break;
			case 1:
				// Implementing
				progressDialog.setTitle("Implementing...");
			    progressDialog.setMessage("Please wait...");
				break;
			default:
				break;
			}
			// Show progress dialog while send request to get data
		    progressDialog.setIndeterminate(true);
		    progressDialog.setCancelable(true);
		    return progressDialog;
		} else {
			// Default show alert dialog
			int typeRequest = getArguments().getInt("typeRequest");
			int typeAlert = getArguments().getInt("typeAlert");
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			String title = "";
			String message = "";
			// TODO need to global all constant
			switch (typeAlert) {
			case -1:
				// Does not Internet connection
				title = "Lose connection!";
				message = "Does not connect internet.\nPlease check your internet connection.";
				break;
			case 0:
				// Request timeout
				title = "Request timout!";
				message = "Request timeout.\nPlease check your internet connection.";
				break;
			case 1:
				// 200
				switch (typeRequest) {
				case CallServiceInActivity.SERVICE_REGISTER:
					// Register Successfully
					title = "Congratulation!";
					message = "Your account has been created! \nClick OK button to setup your PIN code.";
					break;
				case CallServiceInActivity.SERVICE_LOGIN:
					// Login successfully.
					title = "Congratulation!";
					message = "Login successfully!";
					break;
				case CallServiceInActivity.SERVICE_FORGOT_PASSWORD:
					// Change password successfully
					title = "Congratulation!";
					message = "Your new password has been sent to your email. \nThank you!";
					break;
				}
				if(typeRequest != CallServiceInActivity.SERVICE_FORGOT_PASSWORD){
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// TODO Gogo input setting activity
									startActivity(new Intent(getActivity()
											.getApplicationContext(),
											SetupHelperActivity.class));
									getActivity().finish();
								}
							});
				}
				break;
			case 2:
				// 500 -- Internal Server Error
				switch (typeRequest) {
				case CallServiceInActivity.SERVICE_REGISTER:
					// Can not create account
					title = "Sorry!";
					message = "Can not create your account.\nPlease try again later!";
					break;
				case CallServiceInActivity.SERVICE_LOGIN:
					title = "Login failed!";
					message = "Your username or password incorrect";
					break;
				case CallServiceInActivity.SERVICE_FORGOT_PASSWORD:
					title = "Sorry";
					message = "Your email does not exist in our system.";
					break;
				}
				break;
			case 3:
				// 501 -- Not implemented
				switch (typeRequest) {
				case CallServiceInActivity.SERVICE_REGISTER:
					// User has existed
					title = "Sorry!";
					message = "Your account or email has existed.\nPlease try with another account or email!";
					break;
				case CallServiceInActivity.SERVICE_LOGIN:
					title = "Login failed!";
					message = "Your username or password incorrect";
					break;
				case CallServiceInActivity.SERVICE_FORGOT_PASSWORD:
					title = "Sorry";
					message = "Can not sent new password to your email. \nPlease try later.";
					break;
				}
				break;
			default:
				break;
			}
			//

			builder.setTitle(title)
					.setMessage(message)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).setCancelable(true);

			return builder.create();
		}

	}

	public static void dimissProgressDialog() {
		progressDialog.dismiss();
	}
}
