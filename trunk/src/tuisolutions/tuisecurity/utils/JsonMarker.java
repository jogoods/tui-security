package tuisolutions.tuisecurity.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tuisolutions.tuisecurity.utils.backup.AccessUriInterface;
import tuisolutions.tuisecurity.utils.backup.Contact;
import tuisolutions.tuisecurity.utils.backup.ContactDAO;
import tuisolutions.tuisecurity.utils.backup.Message;
import tuisolutions.tuisecurity.utils.backup.Resources;
import tuisolutions.tuisecurity.utils.backup.SmsDAO;

import android.util.Log;


public class JsonMarker {
    
    private static final String TAG = JsonMarker.class.getName();
    
    public static JSONObject createJSONString(AccessUriInterface dao) {
        JSONArray jsonArray = null;
        JSONObject jsonObject = new JSONObject();
        String arrayText = "";
        String objText = "";
        if (dao instanceof SmsDAO) {
            objText = "smses";
            arrayText = "sms";
        } else if (dao instanceof ContactDAO) {
            objText = "contacts";
            arrayText = "contact";
        }
        List<Resources> res = dao.getResources();
        try {
            // put all message content to backup file
            jsonObject.put(arrayText, getMessageArray(jsonArray, res));
            // put sum of message information to backup file
            jsonObject.put(objText, res.size());
            // put version information to backup file
            jsonObject.put("backupTime", Utils.getDateNow());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return jsonObject;
    }
    
    private static JSONArray getMessageArray(JSONArray json, List<Resources> listRes) throws JSONException {
        int threadID = 1;
        JSONArray jsonArray = new JSONArray();
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    // put message content to JSONArray
                    jsonArray.put(json.getJSONObject(i));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        // if all phone contact is selected
        JSONObject jsonObject = new JSONObject();
        for (Resources res : listRes) {
            jsonObject = putToJSONSms(res, threadID);
            jsonArray.put(jsonObject);
            threadID++;
        }
        return jsonArray;
    }
    
    public static JSONObject putToJSONSms(Resources res, int id) throws JSONException {
        
        if (res.getClass().isAssignableFrom(Message.class)) {
            return createMessageObject(res, id);
        } else if (res.getClass().isAssignableFrom(tuisolutions.tuisecurity.utils.backup.Contact.class)) {
            return createContactObject(res, id);
        }
        return null;
    }
    
    private static JSONObject createContactObject(Resources res, int id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Contact contact = (Contact) res;
        try {
            jsonObject.put("id", id);
            jsonObject.put("address", contact.getContactId());
            jsonObject.put("phoneNumber", contact.getPhoneNumber().getNumber());
            jsonObject.put("phoneNumberType", contact.getPhoneNumber().getType());
            jsonObject.put("displayName", contact.getPhoneName().getDisplayName());
            jsonObject.put("familyName", contact.getPhoneName().getFamilyName());
            jsonObject.put("givenName", contact.getPhoneName().getGivenName());
            jsonObject.put("middleName", contact.getPhoneName().getMiddleName());
            jsonObject.put("namePrefix", contact.getPhoneName().getNamePrefix());
            jsonObject.put("nameSuffix", contact.getPhoneName().getNameSuffix());
            jsonObject.put("emailAddress", contact.getEmail().getEmail());
            jsonObject.put("emailType", contact.getEmail().getType());
            jsonObject.put("streetAddress", contact.getAddress().getStreet());
            jsonObject.put("city", contact.getAddress().getCity());
            jsonObject.put("state", contact.getAddress().getState());
            jsonObject.put("country", contact.getAddress().getCountry());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return jsonObject.put("error", "true");
        }
        return jsonObject;
    }
    
    private static JSONObject createMessageObject(Resources res, int id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Message msg = (Message) res;
        try {
            jsonObject.put("id", id);
            jsonObject.put("contactId", msg.getAddress());
            jsonObject.put("date", msg.getDate());
            jsonObject.put("content", msg.getContent());
            jsonObject.put("read", msg.getRead());
            jsonObject.put("locked", msg.getLocked());
            jsonObject.put("status", msg.getStatus());
            jsonObject.put("type", msg.getType());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return jsonObject.put("error", "true");
        }
        return jsonObject;
    }
}
