package io.neocdtv.player.core.omxplayer;

import io.neocdtv.player.core.PlayerEventsHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
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
  private final PlayerEventsHandler playerEventsHandler;
  private boolean active = true;

  OmxPlayerOutputStreamConsumer(
      final InputStream in,
      final PlayerEventsHandler playerEventsHandler) {
    this.in = in;
    this.playerEventsHandler = playerEventsHandler;
  }

  public void run() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(in));
      String line;
      while (active && (line = bufferedReader.readLine()) != null) {
        LOGGER.log(Level.FINE, line);
        handleStreamEnded(line);
      }
    } catch (Exception exception) {
      LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
    } finally {
      try {
        bufferedReader.close();
      } catch (IOException ioException) {
        LOGGER.log(Level.SEVERE, ioException.getMessage(), ioException);
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
