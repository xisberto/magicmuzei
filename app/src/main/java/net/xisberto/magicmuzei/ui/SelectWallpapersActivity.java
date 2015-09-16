package net.xisberto.magicmuzei.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.xisberto.magicmuzei.R;
import net.xisberto.magicmuzei.model.ArtworkList;
import net.xisberto.magicmuzei.model.WallpaperInfo;
import net.xisberto.magicmuzei.network.WallpaperListRequest;
import net.xisberto.magicmuzei.network.WallpaperSpiceService;

import java.util.List;

public class SelectWallpapersActivity extends AppCompatActivity {
    private WallpaperListAdapter adapter;
    private SpiceManager spiceManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallpapers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spiceManager = new SpiceManager(WallpaperSpiceService.class);

        recyclerView = (RecyclerView) findViewById(R.id.list_wallpapers);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacing));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("artworks")) {
                ArtworkList artworks = (ArtworkList) savedInstanceState.getSerializable("artworks");
                adapter = new WallpaperListAdapter(artworks);
                SparseArray<WallpaperInfo> selected_items = savedInstanceState.getSparseParcelableArray("selected_items");
                adapter.setSelectedItems(selected_items);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("artworks", adapter.getArtworks());
        outState.putSparseParcelableArray("selected_items", adapter.getSelectedItems());
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);

        if (adapter == null) {
            spiceManager.execute(new WallpaperListRequest(), new RequestListener<ArtworkList>() {
                @Override
                public void onRequestSuccess(ArtworkList artworks) {
                    if (adapter == null) {
                        adapter = new WallpaperListAdapter(artworks);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setArtworks(artworks);
                    }
                    //Search in saved wallpapers for the wallpapers received from web
                    //Uses the url to compare, and marks them as selected
                    List<WallpaperInfo> wallpapers = WallpaperInfo.listAll(WallpaperInfo.class);
                    for (WallpaperInfo wallpaper : wallpapers) {
                        Log.w("SelectWallpapers", String.format("Wallpaper index %s", wallpapers.indexOf(wallpaper)));
                        int index;
                        for (index = 0; index < artworks.size(); index++) {
                            Log.w("SelectWallpapers", String.format("Artwork index %s", index));
                            if (wallpaper.url.equals(artworks.get(index).getImageUri().toString())) {
                                adapter.setItemSelected(index, true);
                                break;
                            }
                        }
                        if (index == artworks.size()
                                && ! adapter.isItemSelected(index)) {
                            Log.w("SelectWallpapers", String.format("Adding wallpaper %s", wallpaper.title));
                            //The artworks loop went until the end and it was not added. This means
                            //the wallpaper's url matched none of the artworks' urls
                            artworks.add(wallpaper.toArtwork());
                            adapter.setItemSelected(index, true);
                        }
                    }
                }

                @Override
                public void onRequestFailure(SpiceException spiceException) {

                }
            });
        }
    }

    @Override
    protected void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_wallpapers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm:
                saveSelectedWallpapers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveSelectedWallpapers() {
        WallpaperInfo.deleteAll(WallpaperInfo.class);
        for (int i = 0; i < adapter.getSelectedItems().size(); i++) {
            int key = adapter.getSelectedItems().keyAt(i);
            adapter.getSelectedItems().get(key).save();
        }
    }

}
