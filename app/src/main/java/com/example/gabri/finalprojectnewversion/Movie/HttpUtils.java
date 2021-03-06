package com.example.gabri.finalprojectnewversion.Movie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * class that gets image from a specified URL
 */
class HttpUtils {
  /**
   * getter for image via url
   * @param url url object to retrieve image from
   * @return bitmap image
   */
  public static Bitmap getImage(URL url) {
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();
      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        return BitmapFactory.decodeStream(connection.getInputStream());
      } else
        return null;
    } catch (Exception e) {
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * getter for image via provided string
   * @param urlString string used to make url object
   * @return bitmap image
   */
  public static Bitmap getImage(String urlString) {
    try {
      URL url = new URL(urlString);
      return getImage(url);
    } catch (MalformedURLException e) {
      return null;
    }
  }
}