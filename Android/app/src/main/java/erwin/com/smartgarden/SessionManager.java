package erwin.com.smartgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String MYPREFERENCES = "MyPrefs" ;
    public static final String NAME = "nameKey";
    public static final String TELP = "telpKey";
    public static final String EMAIL = "emailKey";
    public static final String PASSWORD = "passwordKey";
    public static final String SERVER = "serverKey";
    public static final String JAM_JADWALSIRAM = "jam_jadwalSiramKey";
    public static final String MENIT_JADWALSIRAM = "menit_jadwalSiramKey";

    private static final String IS_LOGIN = "IsLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MYPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String password, String email, String telp, String server,
                                   String jam_jadwalSiram, String menit_jadwalSiram){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(PASSWORD, password);
        editor.putString(EMAIL, email);
        editor.putString(TELP, telp);
        editor.putString(SERVER, server);
        editor.putString(JAM_JADWALSIRAM, jam_jadwalSiram);
        editor.putString(MENIT_JADWALSIRAM, menit_jadwalSiram);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }else {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(NAME, pref.getString(NAME, null));
        user.put(PASSWORD, pref.getString(PASSWORD, null));
        user.put(EMAIL, pref.getString(EMAIL, null));
        user.put(TELP, pref.getString(TELP, null));
        user.put(SERVER, pref.getString(SERVER, null));
        user.put(JAM_JADWALSIRAM, pref.getString(JAM_JADWALSIRAM, null));
        user.put(MENIT_JADWALSIRAM, pref.getString(MENIT_JADWALSIRAM, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
