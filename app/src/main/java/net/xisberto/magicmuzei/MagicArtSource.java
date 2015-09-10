package net.xisberto.magicmuzei;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.gson.Gson;

import net.xisberto.magicmuzei.ui.Settings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
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

        try {
            InputStream inputStream = new URL("http://magic.wizards.com/see-more-wallpaper?page=1").openStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            JSONResponse response = new Gson().fromJson(reader, JSONResponse.class);

            Document document = Jsoup.parse(response.data);
            Elements list = document.select("li > div.wrap");
            for (Element element : list) {
                Element title = element.select("h3").first();
                Element author = element.select("p.author").first();
                Element uri = element.select("a:contains(1280x960), a:contains(Tablet)").first();
                Log.w("MuzeiService", "Using uri " + uri.attr("href"));

                Artwork artwork = new Artwork.Builder()
                        .title(title.html())
                        .byline(author.html())
                        .imageUri(Uri.parse(uri.attr("href")))
                        .build();
                result.add(artwork);
            }

            Log.w("WallpaperRequest", String.format("Got %s artworks", result.size()));

        } catch (IOException e) {
            e.printStackTrace();
        }



        return result;
    }

    public static final class JSONResponse {
        int status;
        String data;
        int page;
        int displaySeeMore;
    }

}
