package io.neocdtv.player.core;

/**
 * PlayerState.
 *
 * @author xix
 * @since 03.01.18
 */
public class PlayerState {
  private String currentUri;
  private String position;
  private String duration;

  public void setCurrentUri(String currentUri) {
    this.currentUri = currentUri;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public long getPosition() {
    String[] split = position.split("\\.");
    return new Long(split[0]);
  }

  public long getDuration() {
    String[] split = duration.split("\\.");
    return new Long(split[0]);
  }

  public String getCurrentUri() {
    return currentUri;
  }

  @Override
  public String toString() {
    return "PlayerState{" +
        "currentUri='" + currentUri + '\'' +
        ", position='" + position + '\'' +
        ", duration='" + duration + '\'' +
        '}';
  }
}
