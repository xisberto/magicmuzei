package net.xisberto.magicmuzei.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.apps.muzei.api.Artwork;
import com.orm.SugarRecord;

/**
 * Represents information about a Wallpaper
 */
public class WallpaperInfo extends SugarRecord<WallpaperInfo> implements Parcelable {
    public static final Creator<WallpaperInfo> CREATOR = new Creator<WallpaperInfo>() {
        @Override
        public WallpaperInfo createFromParcel(Parcel in) {
            return new WallpaperInfo(in);
        }

        @Override
        public WallpaperInfo[] newArray(int size) {
            return new WallpaperInfo[size];
        }
    };
    public String title;
    public String author;
    public String url;

    public WallpaperInfo() {
    }

    public WallpaperInfo(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }

    protected WallpaperInfo(Parcel in) {
        title = in.readString();
        author = in.readString();
        url = in.readString();
    }

    public static WallpaperInfo fromArtwork(@NonNull Artwork artwork) {
        return new WallpaperInfo(
                artwork.getTitle(),
                artwork.getByline(),
                artwork.getImageUri().toString());
    }

    public Artwork toArtwork() {
        return new Artwork.Builder()
                .title(title)
                .byline(author)
                .imageUri(Uri.parse(url))
                .build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(url);
    }
}
