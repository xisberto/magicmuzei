package net.xisberto.magicmuzei.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.apps.muzei.api.Artwork;
import com.squareup.picasso.Picasso;

import net.xisberto.magicmuzei.R;
import net.xisberto.magicmuzei.model.ArtworkList;

/**
 * Represents an {@link ArtworkList} into a {@link android.support.v7.widget.RecyclerView.Adapter}.
 */
public class WallpaperListAdapter extends RecyclerView.Adapter<WallpaperListAdapter.ViewHolder> {
    protected ArtworkList artworks;

    public WallpaperListAdapter(ArtworkList artworks) {
        this.artworks = artworks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artwork artwork = artworks.get(position);
        holder.text_preview.setText(artwork.getTitle());

        Picasso.with(holder.image_preview.getContext())
                .load(artwork.getImageUri())
                .placeholder(R.drawable.ic_wallpaper_placeholder)
                .into(holder.image_preview);
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    public void setArtworks(ArtworkList mArtworks) {
        this.artworks = mArtworks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_preview;
        TextView text_preview;

        public ViewHolder(View itemView) {
            super(itemView);
            image_preview = (ImageView) itemView.findViewById(R.id.image_preview);
            text_preview = (TextView) itemView.findViewById(R.id.text_preview);
        }
    }
}
