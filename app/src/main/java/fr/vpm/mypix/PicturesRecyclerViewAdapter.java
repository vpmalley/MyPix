package fr.vpm.mypix;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

  private final OnLongClick onLongClick;
  private final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
      Picture picture = (Picture) view.getTag();
      if (selectedPictures.isEmpty()) {
        actionModeManager.startActionMode();
        selectedPictures.add(picture);
        notifyDataSetChanged();
        actionModeManager.onSelectedItemsChanged(selectedPictures);
        return true;
      } else if (onLongClick != null) {
        onLongClick.onLongClick(view, picture);
        return true;
      }
      return false;
    }
  };
  private final OnClick onClick;
  private final View.OnClickListener onPictureClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Picture picture = (Picture) view.getTag();
      if (!selectedPictures.isEmpty()) {
        if (selectedPictures.contains(picture)) {
          selectedPictures.remove(picture);
          if (selectedPictures.isEmpty()) {
            actionModeManager.endActionMode();
          }
        } else {
          selectedPictures.add(picture);
        }
        notifyDataSetChanged();
        actionModeManager.onSelectedItemsChanged(selectedPictures);
      } else if (onClick != null) {
        onClick.onClick(view, picture);
      }
    }
  };
  private final ActionModeManager actionModeManager;
  private List<Picture> mPictures;
  private List<Picture> selectedPictures;

  PicturesRecyclerViewAdapter(OnClick onClickListener, OnLongClick onLongClickListener, ActionModeManager actionModeManager) {
    this.onClick = onClickListener;
    this.onLongClick = onLongClickListener;
    this.actionModeManager = actionModeManager;
    this.mPictures = new ArrayList<>();
    this.selectedPictures = new ArrayList<>();
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
          .load(((PictureWithUrl) picture).getThumbnailUrl())
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
    if (selectedPictures.contains(picture)) {
      holder.pictureSelected.setVisibility(View.VISIBLE);
    } else {
      holder.pictureSelected.setVisibility(View.GONE);
    }
    if (picture.getFileName().length() > 4) {
      holder.pictureName.setText(picture.getFileName().substring(picture.getFileName().length() - 4));
    } else {
      holder.pictureName.setText(picture.getFileName());
    }
    holder.itemView.setTag(mPictures.get(position));
    holder.itemView.setOnClickListener(onPictureClickListener);
    holder.itemView.setOnLongClickListener(onLongClickListener);
  }

  @Override
  public int getItemCount() {
    return mPictures.size();
  }

  public List<Picture> getSelectedPictures() {
    return selectedPictures;
  }

  public void clearSelectedPictures() {
    selectedPictures.clear();
    notifyDataSetChanged();
  }

  public interface OnLongClick {
    void onLongClick(View view, Picture picture);
  }

  interface OnClick {
    void onClick(View view, Picture picture);
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final ImageView pictureDisplay;
    final TextView pictureExtension;
    final TextView pictureName;
    final ImageView pictureSourceFlickr;
    final ImageView pictureSourceLocal;
    final ImageView pictureSelected;

    ViewHolder(View view) {
      super(view);
      pictureDisplay = view.findViewById(R.id.picture_display);
      pictureExtension = view.findViewById(R.id.picture_extension);
      pictureName = view.findViewById(R.id.picture_name);
      pictureSourceFlickr = view.findViewById(R.id.picture_source_flickr);
      pictureSourceLocal = view.findViewById(R.id.picture_source_local);
      pictureSelected = view.findViewById(R.id.picture_selected);
    }
  }
}
