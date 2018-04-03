package fr.vpm.mypix;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.flickr.FlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumRetriever;
import retrofit2.Retrofit;

public class AlbumFragment extends Fragment {

  public static final String ARG_ALBUM_ID = "album_id";
  public static final String ARG_ALBUM_SOURCE = "album_source";
  private LocalAlbumRetriever localAlbumRetriever;
  private FlickrAlbumRetriever flickrAlbumRetriever;

  private String albumId;
  private Album.Source albumSource;

  private RecyclerView picturesRecyclerView;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public AlbumFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ALBUM_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      albumId = getArguments().getString(ARG_ALBUM_ID);
      albumSource = (Album.Source) getArguments().getSerializable(ARG_ALBUM_SOURCE);

      Activity activity = this.getActivity();
      CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
        appBarLayout.setTitle(albumSource.name());
      }
      localAlbumRetriever = new LocalAlbumRetriever();
      Retrofit flickrRetrofit = new FlickrRetrofit().getFlickrRetrofit(getContext());
      flickrAlbumRetriever = new FlickrAlbumRetriever(flickrRetrofit);

      if (Album.Source.FLICKR == albumSource) {
        flickrAlbumRetriever.getFlickrAlbum(this, albumId);
      }
      if (Album.Source.LOCAL == albumSource) {
        localAlbumRetriever.getLocalAlbum(getContext(), this, albumId);
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);
    picturesRecyclerView = rootView.findViewById(R.id.item_detail);
    return rootView;
  }

  public void onAlbumRetrieved(final Album album) {
    // display the album in the recycler view
  }

}
