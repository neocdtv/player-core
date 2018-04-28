package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.PlayerState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * MPlayerErrorStreamConsumer.
 *
 * @author xix
 * @since 03.01.18
 */
public class MPlayerErrorStreamConsumer implements Runnable {

  private final static Logger LOGGER = Logger.getLogger(MPlayerErrorStreamConsumer.class.getName());

  private final InputStream in;
  private boolean active = true;

  public MPlayerErrorStreamConsumer(InputStream in) {
    this.in = in;
  }

  public void run() {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(in));
      String line = null;
      while (active && (line = br.readLine()) != null) {
        LOGGER.info(line);
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


}
