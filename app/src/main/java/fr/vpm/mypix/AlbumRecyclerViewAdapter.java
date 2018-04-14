package fr.vpm.mypix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.AlbumDisplay;
import fr.vpm.mypix.album.ParcelableAlbum;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;
import fr.vpm.mypix.album.PictureWithUrl;

/**
 * Created by vince on 26/02/18.
 */
public class AlbumRecyclerViewAdapter
    extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

  private final AlbumListActivity mParentActivity;
  private final boolean mTwoPane;
  private final View.OnClickListener mOnAlbumCardClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      AlbumDisplay album = (AlbumDisplay) view.getTag();
      ArrayList<ParcelableAlbum> albumsToPass = parcelAlbums(album);
      if (mTwoPane) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(AlbumFragment.ARG_ALBUMS, albumsToPass);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit();
      } else {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putParcelableArrayListExtra(AlbumFragment.ARG_ALBUMS, albumsToPass);
        context.startActivity(intent);
      }
    }
  };
  private List<AlbumDisplay> mValues;

  AlbumRecyclerViewAdapter(AlbumListActivity parent,
                           List<AlbumDisplay> items,
                           boolean twoPane) {
    mValues = items;
    mParentActivity = parent;
    mTwoPane = twoPane;
  }

  @NonNull
  private ArrayList<ParcelableAlbum> parcelAlbums(AlbumDisplay album) {
    ArrayList<ParcelableAlbum> albumsToPass = new ArrayList<>();
    for (Album albumToPass : album.getAlbums()) {
      albumsToPass.add(new ParcelableAlbum(albumToPass.getSource(), albumToPass.getId()));
    }
    return albumsToPass;
  }

  void setAlbums(List<AlbumDisplay> mValues) {
    this.mValues = mValues;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_list_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.albumNameView.setText(mValues.get(position).getName());
    if (mValues.get(position).getPicture() != null) {
      Picture albumCover = mValues.get(position).getPicture();
      if (albumCover instanceof PictureWithUri) {
        Glide.with(holder.albumPictureView)
            .load(((PictureWithUri) albumCover).getUri())
            .apply(RequestOptions.centerCropTransform())
            .into(holder.albumPictureView);
      } else if (albumCover instanceof PictureWithUrl) {
        Glide.with(holder.albumPictureView)
            .load(((PictureWithUrl) albumCover).getUrl())
            .apply(RequestOptions.centerCropTransform())
            .into(holder.albumPictureView);
      }
    } else {
      Glide.with(holder.albumPictureView)
          .load(R.drawable.ic_photo_album_black_24dp)
          .apply(RequestOptions.centerInsideTransform())
          .into(holder.albumPictureView);
    }
    holder.itemView.setTag(mValues.get(position));
    holder.itemView.setOnClickListener(mOnAlbumCardClickListener);
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final TextView albumNameView;
    final ImageView albumPictureView;

    ViewHolder(View view) {
      super(view);
      albumNameView = view.findViewById(R.id.album_name);
      albumPictureView = view.findViewById(R.id.album_picture);
    }
  }
}
