package fr.vpm.mypix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;

import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;
import fr.vpm.mypix.album.PictureWithUrl;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PicturesComparisonActivity extends AppCompatActivity {
  public static final String ARG_PICTURE1_URI = PicturesComparisonActivity.class.getName() + "picture1_uri";
  public static final String ARG_PICTURE1_URL = PicturesComparisonActivity.class.getName() + "picture1_url";
  public static final String ARG_PICTURE2_URI = PicturesComparisonActivity.class.getName() + "picture2_uri";
  public static final String ARG_PICTURE2_URL = PicturesComparisonActivity.class.getName() + "picture2_url";
  /**
   * Some older devices needs a small delay between UI widget updates
   * and a change of the status and navigation bar.
   */
  private static final int UI_ANIMATION_DELAY = 300;
  private final Handler mHideHandler = new Handler();
  private View mContentView;
  private final Runnable mHidePart2Runnable = new Runnable() {
    @SuppressLint("InlinedApi")
    @Override
    public void run() {
      mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
          | View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
  };
  private ImageView imageViewPicture1;
  private ImageView imageViewPicture2;
  private SeekBar visibilitySeekbar;
  private final Runnable mShowPart2Runnable = new Runnable() {
    @Override
    public void run() {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.show();
      }
      visibilitySeekbar.setVisibility(View.VISIBLE);
    }
  };
  private boolean mVisible;
  private final Runnable mHideRunnable = new Runnable() {
    @Override
    public void run() {
      hide();
    }
  };

  public void start(Context context, Picture picture1, Picture picture2) {
    Intent intent = new Intent(context, AlbumActivity.class);
    if (picture1 instanceof PictureWithUri) {
      intent.putExtra(ARG_PICTURE1_URI, ((PictureWithUri) picture1).getUri());
    } else if (picture1 instanceof PictureWithUrl) {
      intent.putExtra(ARG_PICTURE1_URL, ((PictureWithUrl) picture1).getUrl());
    }
    if (picture2 instanceof PictureWithUri) {
      intent.putExtra(ARG_PICTURE2_URI, ((PictureWithUri) picture2).getUri());
    } else if (picture2 instanceof PictureWithUrl) {
      intent.putExtra(ARG_PICTURE2_URL, ((PictureWithUrl) picture2).getUrl());
    }
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pictures_comparison);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    mVisible = true;
    mContentView = findViewById(R.id.contentView);
    imageViewPicture1 = findViewById(R.id.picture1);
    imageViewPicture2 = findViewById(R.id.picture2);
    visibilitySeekbar = findViewById(R.id.pictures_visibility_seekbar);
    fillPicture1(getIntent());
    fillPicture2(getIntent());

    mContentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        toggleVisibility();
      }
    });
  }

  private void fillPicture1(Intent intent) {
    if (intent.hasExtra(ARG_PICTURE1_URI)) {
      Uri pictureUri = intent.getParcelableExtra(ARG_PICTURE1_URI);
      Glide.with(imageViewPicture1)
          .load(pictureUri)
          .into(imageViewPicture1);
    } else if (intent.hasExtra(ARG_PICTURE1_URL)) {
      String pictureUrl = intent.getStringExtra(ARG_PICTURE1_URL);
      Glide.with(imageViewPicture1)
          .load(pictureUrl)
          .into(imageViewPicture1);
    }
  }

  private void fillPicture2(Intent intent) {
    if (intent.hasExtra(ARG_PICTURE2_URI)) {
      Uri pictureUri = intent.getParcelableExtra(ARG_PICTURE2_URI);
      Glide.with(imageViewPicture2)
          .load(pictureUri)
          .into(imageViewPicture2);
    } else if (intent.hasExtra(ARG_PICTURE2_URL)) {
      String pictureUrl = intent.getStringExtra(ARG_PICTURE2_URL);
      Glide.with(imageViewPicture2)
          .load(pictureUrl)
          .into(imageViewPicture2);
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    delayedHide(100);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void toggleVisibility() {
    if (mVisible) {
      hide();
    } else {
      show();
    }
  }

  private void hide() {
    // Hide UI first
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
    visibilitySeekbar.setVisibility(View.GONE);
    mVisible = false;

    mHideHandler.removeCallbacks(mShowPart2Runnable);
    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
  }

  @SuppressLint("InlinedApi")
  private void show() {
    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    mVisible = true;

    mHideHandler.removeCallbacks(mHidePart2Runnable);
    mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
  }

  /**
   * Schedules a call to hide() in delay milliseconds, canceling any
   * previously scheduled calls.
   */
  private void delayedHide(int delayMillis) {
    mHideHandler.removeCallbacks(mHideRunnable);
    mHideHandler.postDelayed(mHideRunnable, delayMillis);
  }
}
