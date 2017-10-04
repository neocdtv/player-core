package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.PlayerState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by xix on 18.09.17.
 */
public class MPlayerOutputStreamConsumer implements Runnable {

  private final static Logger LOGGER = Logger.getLogger(MPlayerOutputStreamConsumer.class.getName());

  private final InputStream in;
  private final PlayerState playerState;
  private boolean active = true;

  public MPlayerOutputStreamConsumer(InputStream in, PlayerState playerState) {
    this.in = in;
    this.playerState = playerState;
  }

  public void run() {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(in));
      String line = null;
      while (active && (line = br.readLine()) != null) {
        LOGGER.info(line);
        handlePosition(line);
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
}
