package tuisolutions.tuisecurity.controllers;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Mailer extends Authenticator {
	private Context context;
	private String _user;
	private String _pass;

	private String[] _to;
	private String _from;

	private String _port;
	private String _sport;

	private String _host;

	private String _subject;
	private String _body;

	private boolean _auth;

	private boolean _debugable;

	private Multipart _multipart;

	private ArrayList<String> _attachFiles;

	public Mailer() {
		_host = "smtp.gmail.com"; // default smtp server
		_port = "465"; // default smtp port
		_sport = "465";// default socketfactory port

		_user = "";// username
		_pass = "";// password
		_from = "";// email sent from
		_subject = "";// email sub
		_body = "";// email body

		_debugable = false;// debug mode on or off - default off
		_auth = true; // smtp authentication - default on

		_multipart = new MimeMultipart();

		_attachFiles = new ArrayList<String>();

		// There is something wrong with MailCap, javamail can not find a
		// handler for the multipart/mixed part, so this bit needs to be added
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}

	public Mailer(Context c, String username, String pass) {
		this();
		this.context = c;
		_user = username;
		_pass = pass;
	}

	private boolean actionSend() throws Exception {
		Properties props = _setProperties();
		if (!_user.equals("") && !_pass.equals("") && _to.length > 0
				&& !_from.equals("") && !_subject.equals("")
				&& !_body.equals("")) {
			Session session = Session.getInstance(props, this);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(_from));

			InternetAddress[] addressTo = new InternetAddress[_to.length];
			for (int i = 0; i < _to.length; i++) {
				addressTo[i] = new InternetAddress(_to[i]);
			}

			msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

			msg.setSubject(_subject);
			msg.setSentDate(new java.util.Date());

			// setup message body
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(_body);
			_multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			msg.setContent(_multipart);

			// set the message content here
			Transport t = session.getTransport("smtps");
			try {
				t.connect(_host, _user, _pass);
				t.sendMessage(msg, msg.getAllRecipients());
			} finally {
				t.close();
			}
			// Send email
			// Transport.send(msg);
			return true;
		} else {
			return false;
		}
	}

	private Properties _setProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", _host);
		if (_debugable) {
			props.put("mail.debug", "true");
		}
		if (_auth) {
			props.put("mail.smtps.auth", "true");
		}

		props.put("mail.smtp.port", _port);
		props.put("mail.smtp.socketFactory.port", _sport);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		return props;
	}

	public void addAttachment(String filename) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		javax.activation.DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);

		_multipart.addBodyPart(messageBodyPart);
		_attachFiles.add(filename);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(_user, _pass);
	}

	// The getters and setter
	public String getBody() {
		return _body;
	}

	public void setBody(String _body) {
		this._body = _body;
	}

	public void setTo(String[] toArr) {
		this._to = toArr;
	}

	public void setFrom(String from) {
		this._from = from;
	}

	public void setSubject(String subject) {
		this._subject = subject;
	}

	public Context getContext() {
		return this.context;
	}

	public ArrayList<String> getListAttachFile() {
		if (this._attachFiles.size() > 0)
			return this._attachFiles;
		return null;
	}

	/**
	 * Send email and delete file if sent successfully, delete if
	 * unsuccessfully.
	 * 
	 * @param deleteAttachFiles
	 */
	@SuppressWarnings("unchecked")
	public void send(boolean deleteAttachFiles) {
		SendEmail sendEmail;
		if (this._attachFiles != null && this._attachFiles.size() > 0) {
			sendEmail = new SendEmail(deleteAttachFiles);
		} else {
			sendEmail = new SendEmail(false);
		}
		sendEmail.execute();
	}

	//
	// Class synchronize
	@SuppressWarnings("rawtypes")
	private class SendEmail extends AsyncTask {
		private boolean deleteAttachFiles = false;

		public SendEmail(boolean deleteAttachFiles) {
			this.deleteAttachFiles = deleteAttachFiles;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				Log.d("SendEmail", "Sending email.....");
				return actionSend();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result.equals(true)) {
				Toast.makeText(context, "Mail was sent successfully",
						Toast.LENGTH_LONG).show();
				Log.d("SendEmail", "Email was sent successfully.");

				// Delete sent files
				if (this.deleteAttachFiles) {
					ArrayList<String> attFiles = getListAttachFile();
					for (String s : attFiles) {
						System.out.println("Deleting file: " + s);
						if (FileUtils.DeleleFileOnSDCard(s)) {
							System.out.println("File " + s + " was deleted!");
						} else {
							System.out.println("File " + s
									+ " was not deleted!");
							// TODO Encrypt file
						}
					}
				}
			} else {
				Toast.makeText(context, "Mail was not sent.",
						Toast.LENGTH_SHORT).show();
				Log.d("SendEmail", "Email was not sent.");
				/*
				 * // Encrypt unsuccess sent file if (this.deleteAttachFiles) {
				 * ArrayList<String> attFiles = getListAttachFile(); for (String
				 * s : attFiles) { System.out.println("encrypt file " + s);
				 * String path = PreferencesUtils.getPathSaveFile(context);
				 * FileManager.encrypt(path, s, s + "c"); } }
				 */

				// DELETE
				ArrayList<String> attFiles = getListAttachFile();
				if (!attFiles.equals(null) && attFiles.size() > 0) {
					for (String s : attFiles) {
						System.out.println("Deleting file: " + s);
						if (FileUtils.DeleleFileOnSDCard(s)) {
							System.out.println("File " + s + " was deleted!");
						} else {
							System.out.println("File " + s
									+ " was not deleted!");
							// TODO Encrypt file
						}
					}
				}
			}
		}

	}
}