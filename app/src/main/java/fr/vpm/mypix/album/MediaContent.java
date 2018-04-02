package fr.vpm.mypix.album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MediaContent {

  /**
   * An array of sample (dummy) items.
   */
  public static final List<Album> ITEMS = new ArrayList<Album>();

  /**
   * A map of sample (dummy) items, by ID.
   */
  public static final Map<String, Album> ITEM_MAP = new HashMap<String, Album>();

  private static final int COUNT = 25;

  static {
    // Add some sample items.
    for (int i = 1; i <= COUNT; i++) {
      addItem(createDummyItem(i));
    }
  }

  private static void addItem(Album item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.getId(), item);
  }

  private static Album createDummyItem(int position) {
    return new Album(String.valueOf(position), "Item " + position, makeDetails(position), Album.Source.LOCAL);
  }

  private static String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position);
    for (int i = 0; i < position; i++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }

}
