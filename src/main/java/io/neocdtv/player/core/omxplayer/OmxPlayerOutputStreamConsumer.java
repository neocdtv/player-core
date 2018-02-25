package io.neocdtv.player.core.omxplayer;

import io.neocdtv.player.core.PlayerEventsHandler;
import io.neocdtv.player.core.PlayerState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * OmxPlayerOutputStreamConsumer.
 *
 * @author xix
 * @since 03.01.18
 */
public class OmxPlayerOutputStreamConsumer implements Runnable {

  private final static Logger LOGGER = Logger.getLogger(OmxPlayerOutputStreamConsumer.class.getName());

  private final InputStream in;
  private final PlayerState playerState;
  private final PlayerEventsHandler playerEventsHandler;
  private boolean active = true;

  OmxPlayerOutputStreamConsumer(
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
      String line;
      while (active && (line = br.readLine()) != null) {
        LOGGER.info(line);
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

  private void handleStreamEnded(final String line) {
    if (isTrackEndedLine(line)) {
      playerEventsHandler.onTrackEnded();
    }
  }

  private boolean isTrackEndedLine(final String line) {
    return line.matches(".*have a nice day.*");
  }

  void deactivate() {
    active = false;
  }
}
