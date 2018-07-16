package fr.vpm.mypix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

  private AlbumsPresenter albumsPresenter;
  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_list);
    albumsPresenter = new AlbumsPresenter(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    if (findViewById(R.id.item_detail_container) != null) {
      mTwoPane = true;
    }

    Realm.init(this);
    recyclerView = findViewById(R.id.item_list);
    setupRecyclerView(recyclerView);
    albumsPresenter.loadAlbums(this);
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
        albumsPresenter.refreshFlickrAlbums(this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new AlbumRecyclerViewAdapter(this, new ArrayList<>(), mTwoPane));
  }

  public void onAlbumsLoaded(final List<AlbumDisplay> albums) {
    AlbumRecyclerViewAdapter adapter = (AlbumRecyclerViewAdapter) recyclerView.getAdapter();
    adapter.setAlbums(albums);
    adapter.notifyDataSetChanged();
  }
}
