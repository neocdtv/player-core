package io.neocdtv.player.core.omxplayer;

import io.neocdtv.player.core.LoggerUtil;
import io.neocdtv.player.core.ModelUtil;
import io.neocdtv.player.core.Player;
import io.neocdtv.player.core.PlayerEventsHandler;
import io.neocdtv.player.core.PlayerState;
import io.neocdtv.player.core.mplayer.Amixer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OmxPlayer.
 *
 * @author xix
 * @since 03.01.18
 */
public class OmxPlayer implements Player {

  private final static Logger LOGGER = Logger.getLogger(OmxPlayer.class.getName());
  private Process process = null;
  private OmxPlayerOutputStreamConsumer stdOutConsumer;
  private OmxPlayerErrorStreamConsumer errOutConsumer;
  private PrintStream stdOut;
  private PlayerState playerState;
  private int volume = -3000;
  private PlayerEventsHandler playerEventsHandler;
  private List<String> additionalParameters = new ArrayList<>();
  private static final String OPTION_ADJUST_FRAME_RATE = "-r";
  private static final String OPTION_BLACK_BACKGROUND = "-b";
  private static final String OPTION_PRINT_STATS = "-s";
  private static final String OPTION_PRINT_INFORMATION = "-I";
  private static final String OPTION_START_POSITION = "-l";
  private static final String OPTION_LIVE = "--live";
  private static final String OPTION_INITIAL_VOLUME = "--vol";
  private static final String COMMAND_PAUSE = "p";
  private static final String COMMAND_QUIT = "q";
  private static final String COMMAND_INCREASE_VOLUME = "+";
  private static final String COMMAND_DECREASE_VOLUME = "-";
  private static final int MAX_VOLUME_IN_MILLIBELS = 600;
  private static final int MIN_VOLUME_IN_MILLIBELS = -6000;
  private static List<String> PLAYER_PARAMS = Arrays.asList(
      OPTION_PRINT_INFORMATION,
      OPTION_ADJUST_FRAME_RATE,
      OPTION_LIVE,
      OPTION_BLACK_BACKGROUND,
      OPTION_PRINT_STATS,
      OPTION_START_POSITION);

  public OmxPlayer() {
    Runtime.getRuntime().addShutdownHook(cleanupThread);
    playerState = new PlayerState();
  }

  public void setPlayerEventHandler(final PlayerEventsHandler playerEventsHandler) {
    this.playerEventsHandler = playerEventsHandler;
  }

  public void setAmixer(Amixer amixer) {
    // dummy to make to interface work, TODO: switch to alsa with omxplayer
  }

  public void play(final String mediaPath) {
    LOGGER.log(Level.INFO, mediaPath);
    play(mediaPath, 0);
  }

  public void play(final String mediaPath, final long startPosition) {
    stop();
    LOGGER.log(Level.INFO, mediaPath + ", startPosition: " + startPosition);
    playerState = new PlayerState();
    playerState.setCurrentUri(mediaPath);
    playerState.setVolume(volume);
    List<String> cmd = new ArrayList<>(Arrays.asList(getPlayerCommand()));
    cmd.addAll(additionalParameters);
    cmd.addAll(PLAYER_PARAMS);
    cmd.add(ModelUtil.toTimeString(startPosition));
    cmd.add(OPTION_INITIAL_VOLUME);
    cmd.add(String.valueOf(volume));
    cmd.add(mediaPath);

    printCommand(cmd);
    ProcessBuilder processBuilder = new ProcessBuilder(cmd);
    try {
      process = processBuilder.start();
      InputStream stdIn = process.getInputStream();
      InputStream errIn = process.getErrorStream();
      stdOut = new PrintStream(process.getOutputStream());

      if (playerEventsHandler != null) {
        stdOutConsumer = new OmxPlayerOutputStreamConsumer(stdIn, playerEventsHandler);
        Thread stdOutThread = new Thread(stdOutConsumer);
        stdOutThread.start();
      }

      errOutConsumer = new OmxPlayerErrorStreamConsumer(errIn);
      Thread errOutThread = new Thread(errOutConsumer);
      errOutThread.start();

    } catch (IOException ioException) {
      LOGGER.log(Level.SEVERE, ioException.getMessage(), ioException);
    }
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
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void pause() {
    execute(COMMAND_PAUSE);
  }

  public void skip(long seconds) {
    play(playerState.getCurrentUri(), seconds);
  }

  public long getPosition() {
    return playerState.getPosition();
  }

  public long getDuration() {
    return playerState.getDuration();
  }

  public int getVolumeInMillibels() {
    return playerState.getVolumeInMillidels();
  }

  @Override
  public void setVolume(double volume) {
    throw new RuntimeException("NOT IMPLEMENTED");
  }

  public float getPositionPercentage() {
    throw new RuntimeException("NOT IMPLEMENTED");
  }

  public void increaseVolume() {
    if (volume < MAX_VOLUME_IN_MILLIBELS) {
      execute(COMMAND_INCREASE_VOLUME);
      volume += 300;
      playerState.setVolume(volume);
    }
  }

  public void decreaseVolume() {
    if (volume > MIN_VOLUME_IN_MILLIBELS) {
      execute(COMMAND_DECREASE_VOLUME);
      volume -= 300;
      playerState.setVolume(volume);
    }
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

  public String getPlayerCommand() {
    return "omxplayer";
  }

  public void setAdditionalParameters(List<String> additionalParameters) {
    this.additionalParameters = additionalParameters;
  }

  public PlayerState getPlayerState() {
    return playerState;
  }

  private boolean isProcessAvailable() {
    return process != null;
  }

  private void printCommand(final List<String> cmdCopy) {
    LoggerUtil.printCommand(LOGGER, cmdCopy);
  }
}
