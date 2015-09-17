package net.xisberto.magicmuzei.network;

import com.octo.android.robospice.request.SpiceRequest;

import net.xisberto.magicmuzei.model.ArtworkList;

/**
 * Created by xisberto on 11/09/15.
 */
public class WallpaperListRequest extends SpiceRequest<ArtworkList> {
    private int page;

    public WallpaperListRequest() {
        super(ArtworkList.class);
        page = 1;
    }

    public WallpaperListRequest(int page) {
        super(ArtworkList.class);
        this.page = page;
    }

    @Override
    public ArtworkList loadDataFromNetwork() throws Exception {
        return new JSONListParser(page).getNextPage();
    }
}
