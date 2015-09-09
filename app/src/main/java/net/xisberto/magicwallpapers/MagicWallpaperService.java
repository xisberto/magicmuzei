package net.xisberto.magicwallpapers;

import android.content.ComponentName;
import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.xisberto.magicwallpapers.model.ArtworkList;
import net.xisberto.magicwallpapers.network.WallpaperListRequest;

import java.util.Calendar;
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

        WallpaperListRequest request = new WallpaperListRequest();
        try {
            ArtworkList artworks = request.loadDataFromNetwork();
            int index = new Random().nextInt(artworks.size());
            Log.w("MuzeiService", String.format("Publishing arwtork #%s", index));
            publishArtwork(artworks.get(index));
        } catch (Exception e) {
            throw new RetryException();
        }
    }

}
