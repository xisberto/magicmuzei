package net.xisberto.magicwallpapers;

import android.net.ConnectivityManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

import net.xisberto.magicwallpapers.model.ArtworkList;
import net.xisberto.magicwallpapers.network.WallpaperListRequest;
import net.xisberto.magicwallpapers.ui.Settings;

import java.util.Random;

public class MagicWallpaperService extends RemoteMuzeiArtSource {

    private SpiceManager spiceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);
        spiceManager.start(this);
    }

    @Override
    public void onDestroy() {
        Log.w("MuzeiService", "Destroying service");
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onDestroy();
    }

    /**
     * Remember to call this constructor from an empty constructor!
     */
    public MagicWallpaperService() {
        super("MagicWallpaperService");
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

        WallpaperListRequest request = new WallpaperListRequest();
        try {
            ArtworkList artworks = request.loadDataFromNetwork();
            int index = 0;
            String most_recent = getResources().getStringArray(R.array.entryvalues_which_show)[0];
            if (!most_recent.equals(Settings.getInstance(this).whichShow())) {
                index = new Random().nextInt(artworks.size());
            }
            Log.w("MuzeiService", String.format("Publishing arwtork #%s", index));
            publishArtwork(artworks.get(index));
        } catch (Exception e) {
            throw new RetryException();
        }
    }

}
