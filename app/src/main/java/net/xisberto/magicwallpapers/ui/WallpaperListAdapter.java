package net.xisberto.magicwallpapers.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.apps.muzei.api.Artwork;
import com.squareup.picasso.Picasso;

import net.xisberto.magicwallpapers.BuildConfig;
import net.xisberto.magicwallpapers.R;
import net.xisberto.magicwallpapers.model.ArtworkList;

/**
 * Created by xisberto on 09/09/15.
 */
public class WallpaperListAdapter extends RecyclerView.Adapter<WallpaperListAdapter.ViewHolder> {
    private Context mContext;
    private ArtworkList mList;

    public WallpaperListAdapter(Context context) {
        this.mContext = context;
        if (BuildConfig.DEBUG) {
            Picasso.with(context)
                    .setIndicatorsEnabled(true);
        }
    }

    public WallpaperListAdapter(Context context, ArtworkList list) {
        this(context);
        this.mList = list;
    }

    public void setList(ArtworkList list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artwork artwork = mList.get(position);

        Picasso.with(mContext)
                .load(artwork.getImageUri())
                .placeholder(R.drawable.ic_wallpaper)
                .resize(600, 600)
                .centerInside()
                .into(holder.image);

        holder.text.setText(artwork.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_preview);
            text = (TextView) itemView.findViewById(R.id.text_title);
        }
    }
}
