package io.neocdtv.player.core.example;

import io.neocdtv.player.core.PlayerEventsHandler;

import java.util.logging.Logger;

/**
 * PlayerEventsHandlerForTests.
 *
 * @author xix
 * @since 25.02.18
 */
public class ExamplePlayerEventsHandler implements PlayerEventsHandler {

  private final static Logger LOGGER = Logger.getLogger(ExamplePlayerEventsHandler.class.getName());


  @Override
  public void onTrackEnded() {
    LOGGER.info("empty handle for onTrackEnded");
  }

  @Override
  public void onStaringPlayback() {
    LOGGER.info("empty handle for onStaringPlayback");
  }
}
