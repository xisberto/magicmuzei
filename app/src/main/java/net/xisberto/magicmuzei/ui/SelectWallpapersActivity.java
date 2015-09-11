package net.xisberto.magicmuzei.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.xisberto.magicmuzei.R;
import net.xisberto.magicmuzei.model.ArtworkList;
import net.xisberto.magicmuzei.network.WallpaperListRequest;
import net.xisberto.magicmuzei.network.WallpaperSpiceService;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);

        spiceManager.execute(new WallpaperListRequest(), new RequestListener<ArtworkList>() {
            @Override
            public void onRequestSuccess(ArtworkList artworks) {
                if (adapter == null) {
                    adapter = new WallpaperListAdapter(artworks);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setArtworks(artworks);
                }
            }

            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
