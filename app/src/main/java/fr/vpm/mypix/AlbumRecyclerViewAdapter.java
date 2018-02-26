package fr.vpm.mypix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.vpm.mypix.album.Album;

/**
 * Created by vince on 26/02/18.
 */
public class AlbumRecyclerViewAdapter
    extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

  private final AlbumListActivity mParentActivity;
  private final boolean mTwoPane;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Album item = (Album) view.getTag();
      if (mTwoPane) {
        Bundle arguments = new Bundle();
        arguments.putString(AlbumFragment.ARG_ITEM_ID, item.getId());
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit();
      } else {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(AlbumFragment.ARG_ITEM_ID, item.getId());

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

  public void setAlbums(List<Album> mValues) {
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
    holder.mIdView.setText(mValues.get(position).getId());
    holder.mContentView.setText(mValues.get(position).getName());

    holder.itemView.setTag(mValues.get(position));
    holder.itemView.setOnClickListener(mOnClickListener);
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final TextView mIdView;
    final TextView mContentView;

    ViewHolder(View view) {
      super(view);
      mIdView = (TextView) view.findViewById(R.id.id_text);
      mContentView = (TextView) view.findViewById(R.id.name);
    }
  }
}
