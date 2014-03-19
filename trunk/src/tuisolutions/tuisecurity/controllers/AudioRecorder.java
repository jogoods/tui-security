package tuisolutions.tuisecurity.controllers;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;

public class AudioRecorder {

	public static final int CALL_MODE = 1;
	public static final int MIC_MODE = 2;

	private MediaRecorder m_recorder = new MediaRecorder();
	private String m_path = "";

	/**
	 * Creates a new audio recording at the given path (relative to root of SD
	 * card).
	 */
	public AudioRecorder(String path) {
		if(path != null && !path.equals(""))
			m_path = path + ".3gp";
	}

	/**
	 * Starts a new recording.
	 * 
	 * @param mode
	 *            mode of service: 1 is record phone call 2 is record from
	 *            microphone
	 * @throws IOException
	 */
	public void start(int mode) throws IOException {
		if(!m_path.equals("")){
			String state = android.os.Environment.getExternalStorageState();
			if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
				throw new IOException("SD Card is not mounted.  It is " + state
						+ ".");
			}

			if (new File(m_path).getParentFile().exists()){
				m_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				m_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				m_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				File fOut = new File(m_path);
				m_recorder.setOutputFile(fOut.getAbsolutePath());
				try {
					m_recorder.prepare();
					m_recorder.start();
					System.out.println("Started Recording");
				} catch (Exception e) {
					return;
				}
			}
		}
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() throws IOException {
		try {
			m_recorder.stop();
		} catch (Exception e) {
			return;
		}

	}
}
