package fr.vpm.mypix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
  public static final int MAX_OPACITY_PROGRESS = 255;
  public static final int MIN_OPACITY_PROGRESS = 0;
  public static final int MID_OPACITY = 120;

  private RelativeLayout contentView;
  private ImageView imageViewPicture1;
  private ImageView imageViewPicture2;
  private SeekBar visibilitySeekbar;

  public static void start(Context context, Picture picture1, Picture picture2) {
    Intent intent = new Intent(context, PicturesComparisonActivity.class);
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

    contentView = findViewById(R.id.contentView);
    imageViewPicture1 = findViewById(R.id.picture1);
    imageViewPicture2 = findViewById(R.id.picture2);
    visibilitySeekbar = findViewById(R.id.pictures_visibility_seekbar);
    contentView.setOnClickListener(v -> togglePicture());
    fillPicture1(getIntent());
    fillPicture2(getIntent());
    setupSeekbar();
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
    imageViewPicture1.setImageAlpha(MAX_OPACITY_PROGRESS);
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
    imageViewPicture2.setImageAlpha(MIN_OPACITY_PROGRESS);
  }

  private void togglePicture() {
    if (imageViewPicture1.getImageAlpha() > MID_OPACITY) {
      imageViewPicture1.setImageAlpha(MIN_OPACITY_PROGRESS);
      imageViewPicture2.setImageAlpha(MAX_OPACITY_PROGRESS);
      visibilitySeekbar.setProgress(MAX_OPACITY_PROGRESS);
      visibilitySeekbar.refreshDrawableState();
    } else {
      imageViewPicture1.setImageAlpha(MAX_OPACITY_PROGRESS);
      imageViewPicture2.setImageAlpha(MIN_OPACITY_PROGRESS);
      visibilitySeekbar.setProgress(MIN_OPACITY_PROGRESS);
      visibilitySeekbar.refreshDrawableState();
    }
  }

  private void setupSeekbar() {
    visibilitySeekbar.setMax(MAX_OPACITY_PROGRESS);
    visibilitySeekbar.setProgress(MIN_OPACITY_PROGRESS);
    visibilitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        imageViewPicture1.setImageAlpha(MAX_OPACITY_PROGRESS - progress);
        imageViewPicture2.setImageAlpha(progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
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
}
