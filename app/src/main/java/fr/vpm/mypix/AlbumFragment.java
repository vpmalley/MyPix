package fr.vpm.mypix;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.LocalDisplayPicture;
import fr.vpm.mypix.album.ParcelableAlbum;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;
import fr.vpm.mypix.flickr.FlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumRetriever;
import retrofit2.Retrofit;

public class AlbumFragment extends Fragment {

  public static final String ARG_ALBUMS = "albums";
  private LocalAlbumRetriever localAlbumRetriever;
  private FlickrAlbumRetriever flickrAlbumRetriever;
  private SharePicturesWithDialog sharePicturesWithDialog;

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
    setHasOptionsMenu(true);
    if (getArguments().containsKey(ARG_ALBUMS)) {
      albums = getArguments().getParcelableArrayList(ARG_ALBUMS);

      Activity activity = this.getActivity();
      appBarLayout = activity.findViewById(R.id.toolbar_layout);
    } else {
      albums = new ArrayList<>();
    }
    sharePicturesWithDialog = new SharePicturesWithDialog();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);
    picturesRecyclerView = rootView.findViewById(R.id.item_detail);
    picturesRecyclerView.setAdapter(new PicturesRecyclerViewAdapter(() -> {
      if (getActivity() != null) {
        getActivity().invalidateOptionsMenu();
      }
    }));
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
      menu.findItem(R.id.delete).setVisible(false);
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
      default:
        return super.onOptionsItemSelected(item);
    }
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
    Map<String, List<Picture>> picturesByName = mapPicturesByName(allPictures);
    List<Picture> displayPictures = mapPicturesToDisplays(picturesByName);
    adapter.setPictures(displayPictures);
    adapter.notifyDataSetChanged();
  }

  @NonNull
  private Map<String, List<Picture>> mapPicturesByName(List<Picture> allPictures) {
    Map<String, List<Picture>> picturesByName = new HashMap<>();
    for (Picture newPicture : allPictures) {
      List<Picture> picturesWithThisName = picturesByName.get(newPicture.getFileName());
      if (picturesWithThisName == null) {
        picturesWithThisName = new ArrayList<>();
      }
      picturesWithThisName.add(newPicture);
      picturesByName.put(newPicture.getFileName(), picturesWithThisName);
    }
    return picturesByName;
  }

  private List<Picture> mapPicturesToDisplays(Map<String, List<Picture>> picturesByName) {
    List<Picture> pictures = new ArrayList<>();
    for (List<Picture> picturesWithThisName : picturesByName.values()) {
      PictureWithUri firstPictureWithUri = getFirstPictureWithUri(picturesWithThisName);
      if (firstPictureWithUri == null) {
        pictures.addAll(picturesWithThisName);
      } else {
        LocalDisplayPicture displayPicture = new LocalDisplayPicture(firstPictureWithUri);
        displayPicture.addPictures(picturesWithThisName);
        pictures.add(displayPicture);
      }
    }
    return pictures;
  }

  private PictureWithUri getFirstPictureWithUri(List<Picture> pictures) {
    for (Picture picture : pictures) {
      if (picture instanceof PictureWithUri) {
        return (PictureWithUri) picture;
      }
    }
    return null;
  }


}
