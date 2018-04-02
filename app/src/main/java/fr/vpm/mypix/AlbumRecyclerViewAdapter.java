package fr.vpm.mypix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fr.vpm.mypix.album.Album;
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
      Album album = (Album) view.getTag();
      if (mTwoPane) {
        Bundle arguments = new Bundle();
        arguments.putString(AlbumFragment.ARG_ALBUM_ID, album.getId());
        arguments.putSerializable(AlbumFragment.ARG_ALBUM_SOURCE, album.getSource());
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit();
      } else {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(AlbumFragment.ARG_ALBUM_ID, album.getId());
        intent.putExtra(AlbumFragment.ARG_ALBUM_SOURCE, album.getSource());
        context.startActivity(intent);
      }
    }
  };
  private List<Album> mValues;

  AlbumRecyclerViewAdapter(AlbumListActivity parent,
                           List<Album> items,
                           boolean twoPane) {
    mValues = items;
    mParentActivity = parent;
    mTwoPane = twoPane;
  }

  void setAlbums(List<Album> mValues) {
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
    if (!mValues.get(position).getPictures().isEmpty()) {
      Picture firstPicture = mValues.get(position).getPictures().get(0);
      if (firstPicture instanceof PictureWithUri) {
        Glide.with(holder.albumPictureView)
            .load(((PictureWithUri) firstPicture).getUri())
            .apply(RequestOptions.centerCropTransform())
            .into(holder.albumPictureView);
      } else if (firstPicture instanceof PictureWithUrl) {
        Glide.with(holder.albumPictureView)
            .load(((PictureWithUrl) firstPicture).getUrl())
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
