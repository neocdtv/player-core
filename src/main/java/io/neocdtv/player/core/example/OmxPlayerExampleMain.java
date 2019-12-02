package io.neocdtv.player.core.example;

import io.neocdtv.player.core.omxplayer.OmxPlayer;

public class OmxPlayerExampleMain {
  public static void main(String[] args) {
    final OmxPlayer player = new OmxPlayer();
    player.setPlayerEventHandler(new ExamplePlayerEventsHandler());
    player.play("add media path here!");
  }
}
