package fr.vpm.mypix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.album.AlbumDisplay;
import io.realm.Realm;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlbumActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AlbumListActivity extends AppCompatActivity {

  private static final int READ_STORAGE_PERMISSION_REQUEST = 101;
  private AlbumsPresenter albumsPresenter;
  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_list);
    albumsPresenter = new AlbumsPresenter(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(() -> albumsPresenter.refreshFlickrAlbums(AlbumListActivity.this));
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

    if (findViewById(R.id.item_detail_container) != null) {
      mTwoPane = true;
    }

    Realm.init(this);
    recyclerView = findViewById(R.id.item_list);
    setupRecyclerView(recyclerView);
    loadAlbums();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_album_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.refresh:
        swipeRefreshLayout.setRefreshing(true);
        albumsPresenter.refreshFlickrAlbums(this);
        return true;
      case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new AlbumRecyclerViewAdapter(this, new ArrayList<>(), mTwoPane));
  }

  private void loadAlbums() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.album_list_permission_title)
            .setMessage(R.string.album_list_permission_message)
            .setPositiveButton(R.string.all_ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_STORAGE_PERMISSION_REQUEST));
        builder.create().show();
      } else {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
            READ_STORAGE_PERMISSION_REQUEST);
      }
    } else {
      albumsPresenter.loadAlbums(this);
    }
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
    switch (requestCode) {
      case READ_STORAGE_PERMISSION_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          albumsPresenter.loadAlbums(this);
        } else {
          albumsPresenter.refreshFlickrAlbums(this);
          Snackbar.make(recyclerView, R.string.snackbar_album_list_permission_denied, Snackbar.LENGTH_SHORT).show();
        }
      }
    }
  }

  public void onAlbumsLoaded(final List<AlbumDisplay> albums) {
    swipeRefreshLayout.setRefreshing(false);
    AlbumRecyclerViewAdapter adapter = (AlbumRecyclerViewAdapter) recyclerView.getAdapter();
    adapter.setAlbums(albums);
    adapter.notifyDataSetChanged();
  }
}
