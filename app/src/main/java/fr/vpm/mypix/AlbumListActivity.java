package fr.vpm.mypix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.MediaContent;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlbumActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AlbumListActivity extends AppCompatActivity {

  private final AlbumsPresenter albumsPresenter = new AlbumsPresenter();
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

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    if (findViewById(R.id.item_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }

    recyclerView = findViewById(R.id.item_list);
    setupRecyclerView(recyclerView);
    albumsPresenter.loadAlbums(this);
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new AlbumRecyclerViewAdapter(this, MediaContent.ITEMS, mTwoPane));
  }

  public void onAlbumsLoaded(final List<Album> albums) {
    AlbumRecyclerViewAdapter adapter = (AlbumRecyclerViewAdapter) recyclerView.getAdapter();
    adapter.setAlbums(albums);
    adapter.notifyDataSetChanged();
  }
}
