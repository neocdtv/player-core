package io.neocdtv.player.core;

import io.neocdtv.player.core.omxplayer.OmxPlayer;

public class OmxPlayerExampleMain {
  public static void main(String[] args) {
    final OmxPlayer player = new OmxPlayer();
    player.addPlayerEvent(new SimplePlayerEventsHandler());
    player.play("add media path here!");
  }
}
