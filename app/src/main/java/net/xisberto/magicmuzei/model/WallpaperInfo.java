package net.xisberto.magicmuzei.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.apps.muzei.api.Artwork;
import com.orm.SugarRecord;

/**
 * Represents information about a Wallpaper
 */
public class WallpaperInfo extends SugarRecord<WallpaperInfo> {
    String title;
    String author;
    String url;

    public WallpaperInfo() {
    }

    public WallpaperInfo(String author, String title, String url) {
        this.author = author;
        this.title = title;
        this.url = url;
    }

    public static WallpaperInfo fromArtwork(@NonNull Artwork artwork) {
        return new WallpaperInfo(
                artwork.getByline(),
                artwork.getTitle(),
                artwork.getImageUri().toString());
    }

    public Artwork toArtwork() {
        return new Artwork.Builder()
                .title(title)
                .byline(author)
                .imageUri(Uri.parse(url))
                .build();
    }
}
