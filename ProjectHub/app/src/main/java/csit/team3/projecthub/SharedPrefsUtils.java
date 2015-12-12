package csit.team3.projecthub;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Chris on 12/9/2015.
 */
public class SharedPrefsUtils {
    public static final String EMAIL_KEY = "EMAIL";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static void save(Context context, String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String get(Context context, String key, String defaultValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            return prefs.getString(key, defaultValue);
        }catch(Exception e){
            return defaultValue;
        }
    }
}
