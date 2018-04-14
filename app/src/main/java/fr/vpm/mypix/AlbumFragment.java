package fr.vpm.mypix;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.ParcelableAlbum;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.flickr.FlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumRetriever;
import retrofit2.Retrofit;

public class AlbumFragment extends Fragment {

  public static final String ARG_ALBUMS = "albums";
  private LocalAlbumRetriever localAlbumRetriever;
  private FlickrAlbumRetriever flickrAlbumRetriever;

  private RecyclerView picturesRecyclerView;
  private CollapsingToolbarLayout appBarLayout;
  private List<ParcelableAlbum> albums;
  private List<Picture> allPictures = new ArrayList<>();
  private List<Album> allAlbums = new ArrayList<>();


  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public AlbumFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ALBUMS)) {
      albums = getArguments().getParcelableArrayList(ARG_ALBUMS);

      Activity activity = this.getActivity();
      appBarLayout = activity.findViewById(R.id.toolbar_layout);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);
    picturesRecyclerView = rootView.findViewById(R.id.item_detail);
    picturesRecyclerView.setAdapter(new PicturesRecyclerViewAdapter());
    picturesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    loadAlbum();
    return rootView;
  }

  private void loadAlbum() {
    localAlbumRetriever = new LocalAlbumRetriever();
    Retrofit flickrRetrofit = new FlickrRetrofit().getFlickrRetrofit(getContext());
    flickrAlbumRetriever = new FlickrAlbumRetriever(flickrRetrofit);
    for (ParcelableAlbum album : albums) {
      if (Album.Source.FLICKR == album.getSource()) {
        flickrAlbumRetriever.getFlickrAlbum(this, album.getId());
      }
      if (Album.Source.LOCAL == album.getSource()) {
        localAlbumRetriever.getLocalAlbum(getContext(), this, album.getId());
      }
    }
  }

  public void onAlbumRetrieved(final Album album) {
    if (appBarLayout != null) {
      appBarLayout.setTitle(album.getName());
    }
    allAlbums.add(album);
    PicturesRecyclerViewAdapter adapter = (PicturesRecyclerViewAdapter) picturesRecyclerView.getAdapter();
    allPictures.addAll(album.getPictures());
    adapter.setPictures(allPictures);
    adapter.notifyDataSetChanged();
  }

}
