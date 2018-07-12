package fr.vpm.mypix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AlbumListActivity}.
 */
public class AlbumActivity extends AppCompatActivity implements LocalAlbumRetriever.OnAlbumRetrievedListener {

  public static final String ARG_ALBUMS = "albums";

  private RecyclerView recyclerView;
  private List<Picture> allPictures = new ArrayList<>();
  private List<Album> allAlbums = new ArrayList<>();
  private LocalAlbumRetriever localAlbumRetriever;
  private FlickrAlbumRetriever flickrAlbumRetriever;

  private SharePicturesWithDialog sharePicturesWithDialog;
  private DeletePicturesWithDialog deletePicturesWithDialog;
  private CollapsingToolbarLayout appBarLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);
    Toolbar toolbar = findViewById(R.id.detail_toolbar);
    appBarLayout = findViewById(R.id.toolbar_layout);
    setSupportActionBar(toolbar);

    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    recyclerView = findViewById(R.id.item_detail);
    setupRecyclerView(recyclerView);

    sharePicturesWithDialog = new SharePicturesWithDialog();
    deletePicturesWithDialog = new DeletePicturesWithDialog();

    if (getIntent().hasExtra(ARG_ALBUMS)) {
      List<ParcelableAlbum> albums = getIntent().getParcelableArrayListExtra(ARG_ALBUMS);
      loadAlbum(albums);
    }

  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    CoordinatorLayout coordinatorLayout = findViewById(R.id.container);
    ImageView expandedImageView = findViewById(R.id.expanded_image_view);
    recyclerView.setAdapter(new PicturesRecyclerViewAdapter(this::invalidateOptionsMenu,
        (view, picture) -> new Zoom().zoomImageFromThumb(view, expandedImageView, coordinatorLayout, picture)));
  }

  private void loadAlbum(List<ParcelableAlbum> albums) {
    allAlbums.clear();
    allPictures.clear();
    localAlbumRetriever = new LocalAlbumRetriever();
    Retrofit flickrRetrofit = new FlickrRetrofit().getFlickrRetrofit(this);
    flickrAlbumRetriever = new FlickrAlbumRetriever(flickrRetrofit);
    for (ParcelableAlbum album : albums) {
      if (Album.Source.FLICKR == album.getSource()) {
        flickrAlbumRetriever.getFlickrAlbum(this, album.getId());
      }
      if (Album.Source.LOCAL == album.getSource()) {
        localAlbumRetriever.getLocalAlbum(this, this, album.getId());
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_album, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    int selectedPicturesCount = ((PicturesRecyclerViewAdapter) recyclerView.getAdapter()).getSelectedPictures().size();
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
    return false;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    List<Picture> selectedPictures = ((PicturesRecyclerViewAdapter) recyclerView.getAdapter()).getSelectedPictures();
    switch (item.getItemId()) {
      case R.id.compare:
        if (selectedPictures.size() == 2) {
          PicturesComparisonActivity.start(this, selectedPictures.get(0), selectedPictures.get(1));
        }
        return true;
      case R.id.share:
        if (selectedPictures.size() == 1) {
          sharePicturesWithDialog.sharePicture(this, selectedPictures.get(0));
        }
        return true;
      case R.id.delete:
        if (!selectedPictures.isEmpty()) {
          deletePicturesWithDialog.deletePictures(recyclerView, selectedPictures, null);
        }
        return true;
      case R.id.home:
        navigateUpTo(new Intent(this, AlbumListActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void onAlbumRetrieved(final Album album) {
    if (appBarLayout != null) {
      appBarLayout.setTitle(album.getName());
    }
    allAlbums.add(album);
    PicturesRecyclerViewAdapter adapter = (PicturesRecyclerViewAdapter) recyclerView.getAdapter();
    allPictures.addAll(album.getPictures());
    Collections.sort(allPictures, (pic1, pic2) -> pic1.getFileName().compareTo(pic2.getFileName()));
    adapter.setPictures(allPictures);
    adapter.notifyDataSetChanged();
  }
}
