package net.xisberto.magicwallpapers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.xisberto.magicwallpapers.model.ArtworkList;
import net.xisberto.magicwallpapers.network.WallpaperListRequest;
import net.xisberto.magicwallpapers.ui.WallpaperListAdapter;

public class MainActivity extends AppCompatActivity {

    private SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);
    private WallpaperListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new WallpaperListAdapter(this);
        RecyclerView list = (RecyclerView) findViewById(R.id.list_wallpapers);
        list.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        list.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);

        WallpaperListRequest request = new WallpaperListRequest();
        spiceManager.execute(request, new RequestListener<ArtworkList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(ArtworkList artworks) {
                Log.w("RequestListener", String.format("Received %s artworks", artworks.size()));
                mAdapter.setList(artworks);
            }
        });
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
