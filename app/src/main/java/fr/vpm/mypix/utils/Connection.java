package fr.vpm.mypix.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.Nullable;

public class Connection {

  private final Context context;

  public Connection(Context context) {
    this.context = context;
  }

  public boolean isOnline() {
    NetworkInfo activeNetwork = getNetworkInfo();
    return activeNetwork != null &&
        activeNetwork.isConnectedOrConnecting();
  }

  public boolean isOnlineThroughWifi() {
    NetworkInfo activeNetwork = getNetworkInfo();
    return activeNetwork != null &&
        activeNetwork.isConnectedOrConnecting() &&
        activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
  }

  public boolean isOffline() {
    return !isOnline();
  }

  @Nullable
  private NetworkInfo getNetworkInfo() {
    ConnectivityManager cm =
        (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    return cm != null ? cm.getActiveNetworkInfo() : null;
  }
}
