package fr.vpm.mypix.album;

public interface PictureWithUrl extends Picture {

  String getThumbnailUrl();

  String getAlbumThumbnailUrl();

  String getOriginalUrl();
}
