package net.xisberto.magicwallpapers.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.xisberto.magicwallpapers.R;

/**
 * Created by xisberto on 09/09/15.
 */
public class Settings {
    private static Settings instance;

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private Settings(Context context) {
        this.mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }

    public boolean wifiOnly() {
        return mSharedPreferences.getBoolean(
                mContext.getString(R.string.key_wifi_only),
                true
        );
    }

    public String whichShow() {
        return mSharedPreferences.getString(
                mContext.getString(R.string.key_which_show),
                mContext.getResources().getStringArray(R.array.entryvalues_which_show)[0]
        );
    }
}
