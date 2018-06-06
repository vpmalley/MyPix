package fr.vpm.mypix.flickr.services;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import fr.vpm.mypix.R;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vince on 31/03/18.
 */

public class FlickrInterceptor implements Interceptor {

  private final Context context;

  public FlickrInterceptor(Context context) {
    this.context = context;
  }

  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();

    HttpUrl urlWithApiKey = request.url().newBuilder()
        .addEncodedQueryParameter("format", "json")
        .addEncodedQueryParameter("nojsoncallback", "1")
        .addEncodedQueryParameter("api_key", context.getString(R.string.flickr_api_key))
        .build();
    Request newRequest = request.newBuilder().url(urlWithApiKey).build();

    return chain.proceed(newRequest);
  }
}
