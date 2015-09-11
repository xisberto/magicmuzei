package net.xisberto.magicmuzei.service;

import android.net.ConnectivityManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import net.xisberto.magicmuzei.model.ArtworkList;
import net.xisberto.magicmuzei.network.JSONListParser;
import net.xisberto.magicmuzei.ui.Settings;

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

        ArtworkList artworks = new JSONListParser().getNextPage();
        if (artworks.size() == 0) {
            throw new RetryException();
        }

        int index = 0;
        if (!Settings.getInstance(this).showMostRecent()) {
            index = new Random().nextInt(artworks.size());
        }
        Log.w("MuzeiService", String.format("Publishing arwtork #%s", index));
        publishArtwork(artworks.get(index));
    }

}
