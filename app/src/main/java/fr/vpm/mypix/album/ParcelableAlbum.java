package fr.vpm.mypix.album;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableAlbum implements Parcelable {

  public static final Creator<ParcelableAlbum> CREATOR = new Creator<ParcelableAlbum>() {
    @Override
    public ParcelableAlbum createFromParcel(Parcel in) {
      return new ParcelableAlbum(in);
    }

    @Override
    public ParcelableAlbum[] newArray(int size) {
      return new ParcelableAlbum[size];
    }
  };
  private final Album.Source source;
  private final String id;

  public ParcelableAlbum(Album.Source source, String id) {
    this.source = source;
    this.id = id;
  }

  protected ParcelableAlbum(Parcel in) {
    id = in.readString();
    source = Album.Source.valueOf(in.readString());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(source.name());
  }

  public Album.Source getSource() {
    return source;
  }

  public String getId() {
    return id;
  }
}
