package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.PlayerEventsHandler;
import io.neocdtv.player.core.PlayerState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * MPlayerOutputStreamConsumer.
 *
 * @author xix
 * @since 03.01.18
 */
public class MPlayerOutputStreamConsumer implements Runnable {

  private final static Logger LOGGER = Logger.getLogger(MPlayerOutputStreamConsumer.class.getName());

  private final static String EXIT = "ID_EXIT=";
  private final static String END_OF_FILE = "EOF";
  private final static String QUIT = "QUIT";
  private final InputStream in;
  private final PlayerState playerState;
  private final PlayerEventsHandler playerEventsHandler;
  private boolean active = true;

  public MPlayerOutputStreamConsumer(
      final InputStream in,
      final PlayerState playerState,
      final PlayerEventsHandler playerEventsHandler) {
    this.in = in;
    this.playerState = playerState;
    this.playerEventsHandler = playerEventsHandler;
  }

  public void run() {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(in));
      String line = null;
      while (active && (line = br.readLine()) != null) {
        LOGGER.info(line);
        handlePosition(line);
        handleStreamEnded(line);
      }
    } catch (Exception e) {
      LOGGER.info("Exception: " + e.getMessage());
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        LOGGER.info(e.getMessage());
      }
    }
  }

  private void handleStreamEnded(String line) {
    if (isTrackEndedLine(line)) {
      playerEventsHandler.onTrackEnded();
    }
  }

  public void deactivate() {
    active = false;
  }

  private void handlePosition(final String line) {
    if (isPositionLine(line)) {
      String[] split = line.trim().replaceAll("\\s{2,}", " ").split("\\s");
      String position = split[1];
      playerState.setPosition(position);
    }
  }

  private boolean isPositionLine(String line) {
    return line.startsWith("A:");
  }

  private boolean isTrackEndedLine(String line) {
    if (isExitLine(line)) {
      final String exitValue = line.trim().substring(EXIT.length());
      return exitValue.equals(END_OF_FILE);
    }
    return false;
  }

  private static boolean isExitLine(String line) {
    return line.startsWith(EXIT);
  }
}
