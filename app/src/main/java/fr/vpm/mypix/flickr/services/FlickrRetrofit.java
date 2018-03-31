package fr.vpm.mypix.flickr.services;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by vince on 31/03/18.
 */

public class FlickrRetrofit {

  public Retrofit getFlickrRetrofit(final Context context) {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new FlickrInterceptor(context.getApplicationContext()))
        .build();
    return new Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.flickr.com/services/rest/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }

}
