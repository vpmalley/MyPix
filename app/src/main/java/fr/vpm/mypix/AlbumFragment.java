package fr.vpm.mypix;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.vpm.mypix.album.Album;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AlbumListActivity}
 * in two-pane mode (on tablets) or a {@link AlbumActivity}
 * on handsets.
 */
public class AlbumFragment extends Fragment {
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String ARG_ALBUM_ID = "album_id";
  public static final String ARG_ALBUM_SOURCE = "album_source";

  private String albumId;
  private Album.Source albumSource;

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
      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
        appBarLayout.setTitle(albumSource.name());
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);

    TextView mainLabel = rootView.findViewById(R.id.item_detail);
    mainLabel.setText(albumId);

    return rootView;
  }
}
