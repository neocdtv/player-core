package io.neocdtv.player.core;

/**
 * @author xix
 */
public interface Player {

  public void play(final String mediaPath);

  public void play(final String mediaPath, final long startPosition);

  public void stop();

  public void pause();

  public void skip(long seconds);

  public long getPosition();

  public long getDuration();

  public PlayerState getPlayerState();
}
