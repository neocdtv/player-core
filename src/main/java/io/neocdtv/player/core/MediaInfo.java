package io.neocdtv.player.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author xix
 */
public class MediaInfo {

  private static final Logger LOGGER = Logger.getLogger(MediaInfo.class.getName());
  private static final String DURATION = "ID_LENGTH=";

  private final static List<String> CMD = Arrays.asList(
      "mplayer",
      "-identify",
      "-frames",
      "0");

  private MediaInfo() {
  }

  public static String getDuration(final String mediaPath) {
    try {
      final ArrayList cmdCopy = new ArrayList(CMD);
      cmdCopy.add(mediaPath);
      ProcessBuilder pb = new ProcessBuilder(cmdCopy);
      Process process = pb.start();
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (isDurationLine(line)) {
          String duration = line.trim().substring(DURATION.length());
          LOGGER.info("Duration: " + duration);
          return duration;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static boolean isDurationLine(String line) {
    return line.startsWith(DURATION);
  }
}
