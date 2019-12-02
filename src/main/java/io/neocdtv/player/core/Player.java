package io.neocdtv.player.core;

import io.neocdtv.player.core.mplayer.Amixer;

import java.util.List;

public interface Player {
  void play(String mediaPath);

  void setPlayerEventHandler(PlayerEventsHandler playerEventsHandler);

  void setAmixer(Amixer amixer);

  // TODO: check out pb.redirectErrorStream and maybe remove in future MPlayerErrorStreamConsumer
  void play(String mediaPath, long startPosition);

  void stop();

  void pause();

  void skip(long seconds) throws InterruptedException;

  long getPosition();

  long getDuration();

  void increaseVolume();

  void decreaseVolume();

  int getVolumeInMillibels();

  void setVolume(double volume);

  PlayerState getPlayerState();

  String getPlayerCommand();

  void setAdditionalParameters(List<String> additionalParameters);
}
