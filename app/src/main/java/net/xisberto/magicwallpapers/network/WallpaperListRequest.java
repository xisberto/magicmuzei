package net.xisberto.magicwallpapers.network;

import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import net.xisberto.magicwallpapers.model.ArtworkList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by xisberto on 08/09/15.
 */
public class WallpaperListRequest extends GoogleHttpClientSpiceRequest<ArtworkList> {

    public WallpaperListRequest() {
        super(ArtworkList.class);
    }

    @Override
    public ArtworkList loadDataFromNetwork() throws Exception {
        ArtworkList result = new ArtworkList();
        Connection connection = Jsoup.connect("http://magic.wizards.com/en/articles/media/wallpapers");
        Document document = connection.get();
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
