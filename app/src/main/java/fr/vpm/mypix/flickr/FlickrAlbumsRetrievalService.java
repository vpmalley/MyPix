package fr.vpm.mypix.flickr;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import fr.vpm.mypix.AlbumListActivity;
import fr.vpm.mypix.R;
import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumsRetriever;
import retrofit2.Retrofit;

public class FlickrAlbumsRetrievalService extends IntentService implements LocalAlbumsRetriever.OnAlbumsRetrievedListener {

    public static final int FOREGROUND_SERVICE_ID = 201;
    public static final String CHANNEL_ID = FlickrAlbumsRetrievalService.class.getName() + "channel";
    private FlickrAlbumsRetriever flickrAlbumsRetriever;

    public FlickrAlbumsRetrievalService() {
        super(FlickrAlbumsRetrievalService.class.getName());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        showSynchroNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        showSynchroNotification();
        if (flickrAlbumsRetriever == null) {
            Retrofit flickrRetrofit = new FlickrRetrofit().getFlickrRetrofit(this);
            flickrAlbumsRetriever = new FlickrAlbumsRetriever(flickrRetrofit, this);
        }
        flickrAlbumsRetriever.getFlickrAlbums(this);
    }

    private void showSynchroNotification() {
        Intent notificationIntent = new Intent(this, AlbumListActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notif_synchro_flickr))
                .setContentText(getString(R.string.notif_synchro_flickr_message))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());
        startForeground(FOREGROUND_SERVICE_ID, builder.build());
    }

    @Override
    public void onAlbumsRetrieved(List<Album> albums) {
        stopForeground(true);
    }
}
