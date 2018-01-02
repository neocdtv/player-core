package io.neocdtv.player.core.omxplayer;

import io.neocdtv.player.core.PlayerEventsHandler;
import io.neocdtv.player.core.MediaInfo;
import io.neocdtv.player.core.ModelUtil;
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
 * @author xix
 */
public class OmxPlayer {

  private final static Logger LOGGER = Logger.getLogger(OmxPlayer.class.getName());
  private Process process = null;
  private OmxPlayerErrorStreamConsumer stdOutConsumer;
  private OmxPlayerOutputStreamConsumer errOutConsumer;
  private PrintStream stdOut;
  private PlayerState playerState;
  private final PlayerEventsHandler playerEventsHandler;
  private static final String OPTION_ADJUST_FRAME_RATE = "-r";
  private static final String OPTION_BLACK_BACKGROUND = "-b";
  private static final String OPTION_PRINT_STATS = "-s";
  private static final String OPTION_PRINT_INFORMATION = "-I";
  private static final String OPTION_START_POSITION = "-l";
  private static final String COMMAND_PAUSE = "p";
  private static final String COMMAND_QUIT = "q";
  private final static List<String> CMD = Arrays.asList(
      "omxplayer",
      OPTION_PRINT_INFORMATION,
      OPTION_ADJUST_FRAME_RATE,
      OPTION_BLACK_BACKGROUND,
      OPTION_PRINT_STATS,
      OPTION_START_POSITION);

  public OmxPlayer(final PlayerEventsHandler playerEventsHandler) {
    Runtime.getRuntime().addShutdownHook(cleanupThread);
    playerState = new PlayerState();
    this.playerEventsHandler = playerEventsHandler;
  }

  public void play(final String mediaPath) {
    LOGGER.log(Level.INFO, "play " + mediaPath);
    play(mediaPath, 0);
  }

  public void play(final String mediaPath, final long startPosition) {
    stop();
    LOGGER.log(Level.INFO, "play: " + mediaPath + ", startPosition: " + startPosition);
    playerState = new PlayerState();
    playerState.setCurrentUri(mediaPath);
    playerState.setDuration(MediaInfo.getDuration(mediaPath));
    ArrayList<String> cmdCopy = new ArrayList<>(CMD);
    cmdCopy.add(ModelUtil.toTimeString(startPosition));
    cmdCopy.add(mediaPath);

    printCommand(cmdCopy);
    ProcessBuilder pb = new ProcessBuilder(cmdCopy);
    try {
      process = pb.start();
      InputStream stdIn = process.getInputStream();
      InputStream outIn = process.getErrorStream();
      stdOut = new PrintStream(process.getOutputStream());

      errOutConsumer = new OmxPlayerOutputStreamConsumer(outIn, playerState, playerEventsHandler);
      Thread two = new Thread(errOutConsumer);
      two.start();

      /*
      errOutConsumer is currently only consuming the output to avoid a deadlock
       */
      stdOutConsumer = new OmxPlayerErrorStreamConsumer(stdIn, playerState);
      Thread one = new Thread(stdOutConsumer);
      one.start();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void stop() {
    LOGGER.log(Level.INFO, "stop");
    if (isProcessAvailable()) {
      stdOutConsumer.deactivate();
      errOutConsumer.deactivate();
      sendCommand(COMMAND_QUIT);
      try {
        process.waitFor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void pause() {
    LOGGER.log(Level.INFO, "pause");
    sendCommand(COMMAND_PAUSE);
  }

  public void skip(long seconds) {
    LOGGER.log(Level.INFO, "skip: " + seconds);
    play(playerState.getCurrentUri(), seconds);
  }

  public long getPosition() {
    LOGGER.log(Level.INFO, "position");
    return playerState.getPosition();
  }

  public long getDuration() {
    LOGGER.log(Level.INFO, "duration");
    return playerState.getDuration();
  }

  public float getPositionPercentage() {
    LOGGER.log(Level.INFO, "position percentage");
    throw new RuntimeException("NOT IMPLEMENTED");
  }

  private void sendCommand(final String command) {
    LOGGER.log(Level.INFO, "send command: " + command);
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
    final StringBuffer stringBuffer = new StringBuffer();
    cmdCopy.stream().forEach(o -> stringBuffer.append(o).append(" "));
    LOGGER.info("Executing command: " + stringBuffer.toString());
  }
}
