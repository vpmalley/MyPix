package fr.vpm.mypix;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.ParcelableAlbum;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.flickr.FlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumRetriever;
import fr.vpm.mypix.utils.Zoom;
import retrofit2.Retrofit;

public class AlbumFragment extends Fragment implements LocalAlbumRetriever.OnAlbumRetrievedListener {

  public static final String ARG_ALBUMS = "albums";
  private LocalAlbumRetriever localAlbumRetriever;
  private FlickrAlbumRetriever flickrAlbumRetriever;
  private SharePicturesWithDialog sharePicturesWithDialog;

  private RecyclerView picturesRecyclerView;
  private CollapsingToolbarLayout appBarLayout;
  private List<ParcelableAlbum> albums;
  private List<Picture> allPictures = new ArrayList<>();
  private List<Album> allAlbums = new ArrayList<>();
  private DeletePicturesWithDialog deletePicturesWithDialog;


  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public AlbumFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    if (getArguments().containsKey(ARG_ALBUMS)) {
      albums = getArguments().getParcelableArrayList(ARG_ALBUMS);

      Activity activity = this.getActivity();
      appBarLayout = activity.findViewById(R.id.toolbar_layout);
    } else {
      albums = new ArrayList<>();
    }
    sharePicturesWithDialog = new SharePicturesWithDialog();
    deletePicturesWithDialog = new DeletePicturesWithDialog();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);
    picturesRecyclerView = rootView.findViewById(R.id.item_detail);
    ImageView expandedImageView = ((ViewGroup) container.getParent()).findViewById(R.id.expanded_image_view);
    picturesRecyclerView.setAdapter(new PicturesRecyclerViewAdapter(() -> {
      if (getActivity() != null) {
        getActivity().invalidateOptionsMenu();
      }
    },
        (view, picture) -> new Zoom().zoomImageFromThumb(view, expandedImageView, container, picture)));
    picturesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    loadAlbum();
    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_album, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    int selectedPicturesCount = ((PicturesRecyclerViewAdapter) picturesRecyclerView.getAdapter()).getSelectedPictures().size();
    if (selectedPicturesCount == 2) {
      menu.findItem(R.id.compare).setVisible(true);
    } else {
      menu.findItem(R.id.compare).setVisible(false);
    }
    if (selectedPicturesCount > 0) {
      menu.findItem(R.id.delete).setVisible(true);
      menu.findItem(R.id.share).setVisible(true);
    } else {
      menu.findItem(R.id.delete).setVisible(false);
      menu.findItem(R.id.share).setVisible(false);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    List<Picture> selectedPictures = ((PicturesRecyclerViewAdapter) picturesRecyclerView.getAdapter()).getSelectedPictures();
    switch (item.getItemId()) {
      case R.id.compare:
        if (selectedPictures.size() == 2) {
          PicturesComparisonActivity.start(getContext(), selectedPictures.get(0), selectedPictures.get(1));
        }
        return true;
      case R.id.share:
        if (selectedPictures.size() == 1) {
          sharePicturesWithDialog.sharePicture(getContext(), selectedPictures.get(0));
        }
        return true;
      case R.id.delete:
        if (!selectedPictures.isEmpty()) {
          deletePicturesWithDialog.deletePictures(picturesRecyclerView, selectedPictures, this::loadAlbum);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void loadAlbum() {
    allAlbums.clear();
    allPictures.clear();
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
    Collections.sort(allPictures, (pic1, pic2) -> pic1.getFileName().compareTo(pic2.getFileName()));
    adapter.setPictures(allPictures);
    adapter.notifyDataSetChanged();
  }

}
