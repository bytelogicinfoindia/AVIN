package com.avin.avin.server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.avin.avin.allactivity.MainActivity;
import com.avin.avin.allactivity.SplashActivity;

import java.util.HashMap;

public class SessionManager {
    public static final String KEY_PREFS_SMS_BODY = "sms_body";
    public static final String APP_SHARED_PREFS = SessionManager.class.getSimpleName();
    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";
    private static final String PREF_NAME = "CPref";


    /// key


    public static final String KEY_ID = "id";
    public static final String KEY_full_name = "name";
    public static final String KEY_email = "email";
    public static final String KEY_mobile = "phone";
    public static final String KEY_user_profile = "user_profile";
    public static final String KEY_password = "password";
    public static final String KEY_address = "address";
    public static final String KEY_zip_code = "zip_code";





    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id,String name,String email,String phone,String user_profile,String password, String address,String zip_code) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_full_name, name);
        editor.putString(KEY_email, email);
        editor.putString(KEY_mobile, phone);
        editor.putString(KEY_user_profile, user_profile);
        editor.putString(KEY_password, password);
        editor.putString(KEY_address, address);
        editor.putString(KEY_zip_code, zip_code);

        editor.commit();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        } else {
            Intent ii = new Intent(_context, SplashActivity.class);
            /*
            ii.putExtra("id", KEY_USERID);*/
            // Closing all the Activities
            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(ii);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_full_name, pref.getString(KEY_full_name, null));
        user.put(KEY_email, pref.getString(KEY_email, null));
        user.put(KEY_mobile, pref.getString(KEY_mobile, null));
        user.put(KEY_user_profile, pref.getString(KEY_user_profile, null));
        user.put(KEY_password, pref.getString(KEY_password, null));
        user.put(KEY_address, pref.getString(KEY_address, null));
        user.put(KEY_zip_code, pref.getString(KEY_zip_code, null));
        System.out.println("headerdatauser"+user);
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
        Toast.makeText(_context, "Logout Success", Toast.LENGTH_LONG).show();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
     public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
