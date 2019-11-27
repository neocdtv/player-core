package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.LoggerUtil;
import io.neocdtv.player.core.MediaInfo;
import io.neocdtv.player.core.PlayerEventsHandler;
import io.neocdtv.player.core.PlayerState;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MPlayer.
 *
 * @author xix
 * @since 03.01.18
 */
public class MPlayer {

  private final static Logger LOGGER = Logger.getLogger(MPlayer.class.getName());
  private Process process = null;
  private MPlayerOutputStreamConsumer stdOutConsumer;
  private MPlayerErrorStreamConsumer errOutConsumer;
  private PrintStream stdOut;
  private PlayerState playerState;
  private PlayerEventsHandler playerEventsHandler;
  private static final String COMMAND_PAUSE = "p";
  private static final String COMMAND_QUIT = "q";
  private static final String OPTION_MEDIA_INFO = "-identify";
  private static final String COMMAND_INCREASE_VOLUME = "0";
  private static final String COMMAND_DECREASE_VOLUME = "9";
  private static final String OPTION_NO_VIDEO = "-novideo";
  private static final String OPTION_NO_AUDIO = "-ao null";
  private static final String OPTION_START_POSITION = "-ss";
  private static final int MAX_VOLUME_IN_MILLIBELS = 400;
  private static final int MIN_VOLUME_IN_MILLIBELS = -9600;
  private final static List<String> CMD = Arrays.asList(
      "mplayer",
      OPTION_MEDIA_INFO,
      OPTION_START_POSITION);
  private final Amixer amixer;

  public MPlayer(final Amixer amixer) {
    Runtime.getRuntime().addShutdownHook(cleanupThread);
    playerState = new PlayerState();
    this.amixer = amixer;
  }

  public void play(final String mediaPath) {
    LOGGER.log(Level.INFO, mediaPath);
    play(mediaPath, 0);
  }

  public void addPlayerEvent(final PlayerEventsHandler playerEventsHandler) {
    this.playerEventsHandler = playerEventsHandler;
  }

  // TODO: check out pb.redirectErrorStream and maybe remove in future MPlayerErrorStreamConsumer
  public void play(final String mediaPath, final long startPosition) {
    stop();
    LOGGER.log(Level.INFO, mediaPath + ", startPosition: " + startPosition);
    playerState = new PlayerState();
    playerState.setPosition(mapPosition(startPosition));
    playerState.setCurrentUri(mediaPath);
    playerState.setDuration(MediaInfo.getDuration(mediaPath));
    ArrayList<String> cmdCopy = new ArrayList<>(CMD);
    cmdCopy.add(startPosition + "");
    cmdCopy.add(mediaPath);
    printCommand(cmdCopy);
    ProcessBuilder processBuilder = new ProcessBuilder(cmdCopy);
    try {
      process = processBuilder.start();
      InputStream stdIn = process.getInputStream();
      InputStream errIn = process.getErrorStream();
      stdOut = new PrintStream(process.getOutputStream());

      if (playerEventsHandler != null) {
        stdOutConsumer = new MPlayerOutputStreamConsumer(stdIn, playerState, playerEventsHandler);
        Thread stdOutThread = new Thread(stdOutConsumer);
        stdOutThread.start();
      }

      errOutConsumer = new MPlayerErrorStreamConsumer(errIn);
      Thread errOutThread = new Thread(errOutConsumer);
      errOutThread.start();

    } catch (IOException ioException) {
      LOGGER.log(Level.SEVERE, ioException.getMessage(), ioException);
    }
  }

  private String mapPosition(long startPosition) {
    return startPosition + ".0";
  }

  public void stop() {
    if (isProcessAvailable()) {
      if (stdOutConsumer != null) {
        stdOutConsumer.deactivate();
      }
      errOutConsumer.deactivate();
      execute(COMMAND_QUIT);
      try {
        process.waitFor();
      } catch (InterruptedException interruptedException) {
        LOGGER.log(Level.SEVERE, interruptedException.getMessage(), interruptedException);
      }
    }
  }

  public void pause() {
    execute(COMMAND_PAUSE);
  }

  public void skip(long seconds) throws InterruptedException {
    play(playerState.getCurrentUri(), seconds);
  }

  public long getPosition() {
    return playerState.getPosition();
  }

  public long getDuration() {
    return playerState.getDuration();
  }

  public void increaseVolume() {
    execute(COMMAND_INCREASE_VOLUME);
  }

  public void decreaseVolume() {
    execute(COMMAND_DECREASE_VOLUME);
  }

  public int getVolumeInMillibels() {
    return playerState.getVolumeInMillidels();
  }

  /**
   * Sets the audio playback volume. Its effect will be clamped to the range [0.0, 1.0]
   *
   * @param volume the volume
   */
  public void setVolume(double volume) {
    int millibels = 0;
    if (volume >= 1.0) {
      millibels = MAX_VOLUME_IN_MILLIBELS;
    } else {
      double realVolume = 1 - volume;
      millibels = (int) ((MIN_VOLUME_IN_MILLIBELS * realVolume) + MAX_VOLUME_IN_MILLIBELS);
    }

    playerState.setVolume(millibels);
    amixer.setVolume(millibels);
  }

  private void execute(final String command) {
    LOGGER.log(Level.INFO, "execute: " + command);
    stdOut.print(command);
    stdOut.flush();
  }

  private Thread cleanupThread = new Thread(this::cleanup);

  private void cleanup() {
    LOGGER.log(Level.INFO, "clean up");
    if (isProcessAvailable()) {
      process.destroy();
    }
  }

  public PlayerState getPlayerState() {
    return playerState;
  }

  private boolean isProcessAvailable() {
    return process != null;
  }

  private void printCommand(final ArrayList<String> cmdCopy) {
    LoggerUtil.printCommand(LOGGER, cmdCopy);
  }
}