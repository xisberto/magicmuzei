package net.xisberto.magicmuzei.ui;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.apps.muzei.api.Artwork;
import com.squareup.picasso.Picasso;

import net.xisberto.magicmuzei.R;
import net.xisberto.magicmuzei.model.ArtworkList;
import net.xisberto.magicmuzei.model.WallpaperInfo;

/**
 * Represents an {@link ArtworkList} into a {@link android.support.v7.widget.RecyclerView.Adapter}.
 */
public class WallpaperListAdapter extends RecyclerView.Adapter<WallpaperListAdapter.ViewHolder> {
    private ArtworkList artworks;
    private SparseArray<WallpaperInfo> selected_items;

    public WallpaperListAdapter(ArtworkList artworks) {
        this.artworks = artworks;
        this.selected_items = new SparseArray<>(artworks.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Artwork artwork = artworks.get(position);

        holder.text_preview.setText(artwork.getTitle());

        Picasso.with(holder.image_preview.getContext())
                .load(artwork.getImageUri())
                .placeholder(R.drawable.ic_wallpaper_placeholder)
                .into(holder.image_preview);

        holder.itemView.setActivated(isItemSelected(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(
                        !v.isActivated());
                setItemSelected((int) v.getTag(), v.isActivated());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    public ArtworkList getArtworks() {
        return artworks;
    }

    public void setArtworks(ArtworkList artworks) {
        this.artworks = artworks;
        notifyDataSetChanged();
    }

    public void addArtworks(ArtworkList artworks) {
        this.artworks.addAll(artworks);
        notifyDataSetChanged();
    }

    public SparseArray<WallpaperInfo> getSelectedItems() {
        return selected_items;
    }

    public void setSelectedItems(SparseArray<WallpaperInfo> selected_items) {
        this.selected_items = selected_items;
    }

    public void unselectAll() {
        selected_items.clear();
    }

    public void setItemSelected(int position, boolean selected) {
        if (selected) {
            selected_items.put(position,
                    WallpaperInfo.fromArtwork(getItem(position)));
        } else {
            selected_items.remove(position);
        }
    }

    public boolean isItemSelected(int position) {
        return selected_items.get(position) != null;
    }

    public Artwork getItem(int position) {
        return artworks.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_preview;
        TextView text_preview;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            image_preview = (ImageView) itemView.findViewById(R.id.image_preview);
            text_preview = (TextView) itemView.findViewById(R.id.text_preview);
        }
    }

}
