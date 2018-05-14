package fr.vpm.mypix;

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

import fr.vpm.mypix.album.FlickrPicture;
import fr.vpm.mypix.album.LocalPicture;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;
import fr.vpm.mypix.album.PictureWithUrl;

public class PicturesRecyclerViewAdapter extends RecyclerView.Adapter<PicturesRecyclerViewAdapter.ViewHolder> {

  private final SharePicturesWithDialog sharePicturesWithDialog;
  private final View.OnClickListener mOnPictureClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Picture picture = (Picture) view.getTag();
      sharePicturesWithDialog.sharePicture(view, picture);
    }
  };
  private List<Picture> mPictures;

  PicturesRecyclerViewAdapter() {
    mPictures = new ArrayList<>();
    sharePicturesWithDialog = new SharePicturesWithDialog();
  }

  void setPictures(List<Picture> pictures) {
    this.mPictures = pictures;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_detail_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    Picture picture = mPictures.get(position);
    if (picture instanceof PictureWithUri) {
      Glide.with(holder.pictureDisplay)
          .load(((PictureWithUri) picture).getUri())
          .apply(RequestOptions.centerCropTransform())
          .into(holder.pictureDisplay);
    } else if (picture instanceof PictureWithUrl) {
      Glide.with(holder.pictureDisplay)
          .load(((PictureWithUrl) picture).getUrl())
          .apply(RequestOptions.centerCropTransform())
          .into(holder.pictureDisplay);
    }
    if (picture.getExtension().isEmpty()) {
      holder.pictureExtension.setVisibility(View.INVISIBLE);
    } else {
      holder.pictureExtension.setVisibility(View.VISIBLE);
      holder.pictureExtension.setText(picture.getExtension().toUpperCase());
    }
    if (picture instanceof FlickrPicture) {
      holder.pictureSourceFlickr.setVisibility(View.VISIBLE);
    } else {
      holder.pictureSourceFlickr.setVisibility(View.GONE);
    }
    if (picture instanceof LocalPicture) {
      holder.pictureSourceLocal.setVisibility(View.VISIBLE);
    } else {
      holder.pictureSourceLocal.setVisibility(View.GONE);
    }
    holder.itemView.setTag(mPictures.get(position));
    holder.itemView.setOnClickListener(mOnPictureClickListener);
  }

  @Override
  public int getItemCount() {
    return mPictures.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final ImageView pictureDisplay;
    final TextView pictureExtension;
    final ImageView pictureSourceFlickr;
    final ImageView pictureSourceLocal;

    ViewHolder(View view) {
      super(view);
      pictureDisplay = view.findViewById(R.id.picture_display);
      pictureExtension = view.findViewById(R.id.picture_extension);
      pictureSourceFlickr = view.findViewById(R.id.picture_source_flickr);
      pictureSourceLocal = view.findViewById(R.id.picture_source_local);
    }
  }
}
