package tuisolutions.tuisecurity.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import tuisolutions.tuisecurity.controllers.AccelerationManager.AccelerationCallback;
import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.Fusion;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorEvent;
import android.util.FloatMath;

public class RecordingListener implements AccelerationCallback {
	public static int INTERACTION_STOP_MODE = 1;
	public static int TIME_STOP_MODE = 2;
	private static AudioRecorder m_recorder;
	private static AccelerationManager accelerationManagger = null;
	public static boolean isRecording = false;
	private Fusion lastFusion, nowFusion;
	private long lastTime, currentTime;
	private static String audio_path = "";
	private Context context;
	long tempTime, startRecordingTime;
	private static int stopType = INTERACTION_STOP_MODE;

	public RecordingListener(Context context, int stopType) {
		this.context = context;
		RecordingListener.stopType = stopType;
	}

	public void startListener() {
		if (isRecording == false) {
			lastFusion = new Fusion();
			nowFusion = new Fusion();
		}
		if (accelerationManagger == null) {
			System.out.println("Create listener");
			accelerationManagger = new AccelerationManager(this.context);
			accelerationManagger.setAccelerationCallback(this);
		}
		accelerationManagger.startListening();
	}

	public void stopListener() {
		accelerationManagger.stopListening();
		accelerationManagger = null;
		isRecording = false;

		if (m_recorder != null) {
			try {
				m_recorder.stop();
				System.out.println("Stop recording successfully.");
				Utils.sendEmail(context, audio_path + ".3gp", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("FloatMath")
	public void onAccelerationUpdate(Fusion fusion, SensorEvent event) {
		float x = fusion.getX();
		float y = fusion.getY();
		float z = fusion.getZ();
		nowFusion.setX(FloatMath.floor(x));
		nowFusion.setY(FloatMath.floor(y));
		nowFusion.setZ(FloatMath.floor(z));
		currentTime = new Date().getTime();

		//
		// Get last fusion and time of last change
		if (!isRecording && !Utils.compareFusion(lastFusion, nowFusion)) {
			System.out.println("changing.............");
			lastFusion.setX(FloatMath.floor(x));
			lastFusion.setY(FloatMath.floor(y));
			lastFusion.setZ(FloatMath.floor(z));
			lastTime = currentTime;
		}
		//
		// Get setting delta time, compare it with delta time (between current
		// time and last time)
		// Turn on recording if delta time over setting time, and not recording
		// before.
		if (isRecording == false
				&& currentTime - lastTime > PreferencesUtils
						.getDeltaTime(context)) {
			//
			// Get and Check path directory exist, if not will create path
			// directory
			audio_path = PreferencesUtils.getPathSaveFile(context);
			if(FileUtils.mkdir(new File(audio_path))){
				isRecording = true;
				tempTime = currentTime;
				startRecordingTime = currentTime;
				audio_path += System.currentTimeMillis();
				m_recorder = new AudioRecorder(audio_path);
				try {
					m_recorder.start(AudioRecorder.MIC_MODE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//
		//
		// Send email after delta sending email time (if in recording)
		//
		if (isRecording
				&& currentTime - lastTime > PreferencesUtils
						.getSendEmailDelay(context)) {
			lastTime = currentTime;

			try {
				m_recorder.stop();
				String oldFile = audio_path + ".3gp";
				//
				// Re-create new record file
				audio_path = PreferencesUtils.getPathSaveFile(context);
				if(FileUtils.mkdir(new File(audio_path))){
					audio_path += System.currentTimeMillis(); 
					m_recorder = new AudioRecorder(audio_path);
					m_recorder.start(AudioRecorder.MIC_MODE);
				}
				// 
				// Send email
				//
				Utils.sendEmail(context,oldFile, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//
		// Reset tempTime to stop if has interaction
		//
		if (Utils.compareFusion(lastFusion, nowFusion)) {
			tempTime = currentTime;
		}
		//
		//
		// Stop when has interaction on device
		// Check if delta delay time (between current time and temp time) >
		// limit time ==> has interaction on device ==> stop
		//
		if (isRecording && stopType == INTERACTION_STOP_MODE) {
			if (!Utils.compareFusion(lastFusion, nowFusion)
					&& (currentTime - tempTime) > PreferencesUtils
							.getLimitTime(context)) {
				stopListener();
			}
		}
		
		//
		// Stop if over limit recording time
		if(isRecording && stopType == TIME_STOP_MODE){
			if(currentTime - startRecordingTime > PreferencesUtils.getRecordingLimitTime(context)){
				stopListener();
			}
		}
	}

}
