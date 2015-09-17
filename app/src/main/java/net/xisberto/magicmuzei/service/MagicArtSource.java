package net.xisberto.magicmuzei.service;

import android.net.ConnectivityManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import net.xisberto.magicmuzei.model.ArtworkList;
import net.xisberto.magicmuzei.model.WallpaperInfo;
import net.xisberto.magicmuzei.network.JSONListParser;
import net.xisberto.magicmuzei.ui.Settings;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MagicArtSource extends RemoteMuzeiArtSource {

    /**
     * Remember to call this constructor from an empty constructor!
     */
    public MagicArtSource() {
        super("MagicWallpaperService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        Log.w("MuzeiService", "Trying to update");

        if (Settings.getInstance(this).wifiOnly()) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (ConnectivityManagerCompat.isActiveNetworkMetered(cm)) {
                return;
            }
        }

        List<WallpaperInfo> wallpapers = WallpaperInfo.listAll(WallpaperInfo.class);
        if (wallpapers.size() == 0) {
            ArtworkList artworks = new JSONListParser().getNextPage();
            if (artworks.size() == 0) {
                throw new RetryException();
            } else {
                publishArtwork(artworks.get(0));
            }
        } else {
            int index = 0;
            if (!Settings.getInstance(this).showMostRecent()) {
                index = new Random().nextInt(wallpapers.size());
            }
            Log.w("MuzeiService", String.format("Publishing arwtork #%s", index));
            publishArtwork(wallpapers.get(index).toArtwork());
        }

        Calendar next_schedule = Calendar.getInstance();
        next_schedule.set(Calendar.MILLISECOND, 0);
        next_schedule.set(Calendar.SECOND, 0);
        next_schedule.set(Calendar.MINUTE, 0);
        next_schedule.set(Calendar.HOUR_OF_DAY, 0);
        next_schedule.add(Calendar.DAY_OF_MONTH, 1);
        scheduleUpdate(next_schedule.getTimeInMillis());
    }

}
