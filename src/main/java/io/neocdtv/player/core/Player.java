package io.neocdtv.player.core;

/**
 * @author xix
 */
public interface Player {

  void play(final String mediaPath);

  void play(final String mediaPath, final long startPosition);

  void stop();

  void pause();

  void skip(long seconds);

  long getPosition();

  long getDuration();

  PlayerState getPlayerState();
}
