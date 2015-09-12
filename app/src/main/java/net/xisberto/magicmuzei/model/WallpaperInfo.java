package net.xisberto.magicmuzei.model;

import com.orm.SugarRecord;

/**
 * Created by xisberto on 12/09/15.
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
}
