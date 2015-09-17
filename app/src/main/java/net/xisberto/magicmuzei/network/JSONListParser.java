package net.xisberto.magicmuzei.network;

import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.xisberto.magicmuzei.model.ArtworkList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by xisberto on 11/09/15.
 */
public class JSONListParser {
    private int nextPage = 1;
    private boolean hasMorePages = true;

    public JSONListParser() {

    }

    public JSONListParser(int page) {
        this.nextPage = page;
    }

    public ArtworkList getNextPage() {
        ArtworkList result = new ArtworkList();

        if (!hasMorePages) {
            return result;
        }

        try {
            JSONResponse response = load(nextPage);
            nextPage = response.page;
            hasMorePages = response.displaySeeMore != 0;
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

    public JSONResponse load(int page) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String sPage = Integer.toString(page);

        Request request = new Request.Builder()
                .url("http://magic.wizards.com/see-more-wallpaper?page=" + sPage)
                .build();
        Log.w("JSONList.load", request.urlString());

        Response response = client.newCall(request).execute();
        String content = response.body().string();

        JSONResponse result = new Gson().fromJson(content, JSONResponse.class);

        return result;
    }

    public static final class JSONResponse {
        public String data;
        int status;
        int page;
        int displaySeeMore;

    }
}
