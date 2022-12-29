package fr.vpm.mypix;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.ParcelableAlbum;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.flickr.FlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.FocalLengthLogger;
import fr.vpm.mypix.local.LocalAlbumRetriever;
import fr.vpm.mypix.utils.Zoom;
import retrofit2.Retrofit;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AlbumListActivity}.
 */
public class AlbumActivity extends AppCompatActivity implements LocalAlbumRetriever.OnAlbumRetrievedListener, ActionModeManager {

    public static final String ARG_ALBUMS = "albums";

    private RecyclerView recyclerView;
    private List<Picture> allPictures = new ArrayList<>();
    private List<Album> allAlbums = new ArrayList<>();
    private LocalAlbumRetriever localAlbumRetriever;
    private FlickrAlbumRetriever flickrAlbumRetriever;
    private CollapsingToolbarLayout appBarLayout;
    private ActionMode actionMode;

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

        if (getIntent().hasExtra(ARG_ALBUMS)) {
            List<ParcelableAlbum> albums = getIntent().getParcelableArrayListExtra(ARG_ALBUMS);
            loadAlbum(albums);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.container);
        ImageView expandedImageView = findViewById(R.id.expanded_image_view);
        recyclerView.setAdapter(new PicturesRecyclerViewAdapter(
                (view, picture) -> new Zoom().zoomImageFromThumb(view, expandedImageView, coordinatorLayout, picture), null, null));
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
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(album);
        new FocalLengthLogger().extractExifInfo(this, albums);
    }

    public void startActionMode() {
        actionMode = startSupportActionMode(new PicturesActionModeCallback(this, (PicturesRecyclerViewAdapter) recyclerView.getAdapter(), recyclerView));
    }

    public void endActionMode() {
        actionMode.finish();
    }

    @Override
    public void onSelectedItemsChanged(List<Picture> selectedPictures) {
        if (actionMode != null) {
            final String selectedItemsCountLabel = getResources().getQuantityString(R.plurals.selected_items_count, selectedPictures.size(), selectedPictures.size());
            this.actionMode.setTitle(selectedItemsCountLabel);
        }
    }

    public static class PicturesActionModeCallback implements ActionMode.Callback {

        private SharePicturesWithDialog sharePicturesWithDialog;
        private DeletePicturesWithDialog deletePicturesWithDialog;

        private final Context context;
        private final PicturesRecyclerViewAdapter adapter;
        private View snackbarContainerView;

        public PicturesActionModeCallback(Context context, PicturesRecyclerViewAdapter adapter, View snackbarContainerView) {
            this.context = context;
            this.adapter = adapter;
            this.snackbarContainerView = snackbarContainerView;
            this.sharePicturesWithDialog = new SharePicturesWithDialog();
            this.deletePicturesWithDialog = new DeletePicturesWithDialog();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_album, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO : handle the compare action
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final List<Picture> selectedPictures = adapter.getSelectedPictures();
            switch (item.getItemId()) {
                case R.id.compare:
                    if (selectedPictures.size() == 2) {
                        PicturesComparisonActivity.start(context, selectedPictures.get(0), selectedPictures.get(1));
                    }
                    return true;
                case R.id.share:
                    if (selectedPictures.size() == 1) {
                        sharePicturesWithDialog.sharePicture(snackbarContainerView, context, selectedPictures.get(0));
                    }
                    return true;
                case R.id.delete:
                    if (!selectedPictures.isEmpty()) {
                        deletePicturesWithDialog.deletePictures(snackbarContainerView, selectedPictures, null);
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode.finish();
            adapter.clearSelectedPictures();
        }
    }
}
