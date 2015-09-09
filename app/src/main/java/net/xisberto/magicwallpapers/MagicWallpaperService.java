package net.xisberto.magicwallpapers;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import net.xisberto.magicwallpapers.ui.Settings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MagicWallpaperService extends RemoteMuzeiArtSource {

    /**
     * Remember to call this constructor from an empty constructor!
     */
    public MagicWallpaperService() {
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

        ArrayList<Artwork> artworks = loadDataFromNetwork();
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

    private ArrayList<Artwork> loadDataFromNetwork() {
        ArrayList<Artwork> result = new ArrayList<>();
        Connection connection = Jsoup.connect("http://magic.wizards.com/en/articles/media/wallpapers");
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            return result;
        }
        Elements list = document.select("ul.wallpaper-wrap > li > div.wrap");
        for (Element element : list) {
            Element title = element.select("h3").first();
            Element author = element.select("p.author").first();
            Element uri = element.select("a:contains(Tablet)").first();
            Artwork artwork = new Artwork.Builder()
                    .title(title.html())
                    .byline(author.html())
                    .imageUri(Uri.parse(uri.attr("href")))
                    .build();
            result.add(artwork);
        }

        Log.w("WallpaperRequest", String.format("Got %s artworks", result.size()));

        return result;
    }

}
