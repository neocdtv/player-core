package io.neocdtv.player.core;

import io.neocdtv.player.core.omxplayer.OmxPlayer;

import java.util.logging.Logger;

/**
 * PlayerEventsHandlerForTests.
 *
 * @author xix
 * @since 25.02.18
 */
public class SimplePlayerEventsHandler implements PlayerEventsHandler {

  private final static Logger LOGGER = Logger.getLogger(SimplePlayerEventsHandler.class.getName());


  @Override
  public void onTrackEnded() {
    LOGGER.info("empty handle for onTrackEnded");
  }

  @Override
  public void onStaringPlayback() {
    LOGGER.info("empty handle for onStaringPlayback");
  }
}
