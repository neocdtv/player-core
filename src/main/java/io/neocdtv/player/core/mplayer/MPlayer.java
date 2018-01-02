package io.neocdtv.player.core.mplayer;

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
 * @author xix
 */
public class MPlayer {

  private final static Logger LOGGER = Logger.getLogger(MPlayer.class.getName());
  private Process process = null;
  private MPlayerOutputStreamConsumer stdOutConsumer;
  private MPlayerErrorStreamConsumer errOutConsumer;
  private PrintStream stdOut;
  private PlayerState playerState;
  private final PlayerEventsHandler playerEventsHandler;
  private static final String COMMAND_PAUSE = "p";
  private static final String COMMAND_QUIT = "q";
  private static final String OPTION_MEDIA_INFO = "-identify";
  private static final String OPTION_NO_VIDEO = "-novideo";
  private static final String OPTION_NO_AUDIO = "-ao null";
  private static final String OPTION_START_POSITION = "-ss";
  private final static List<String> CMD = Arrays.asList(
      "mplayer",
      OPTION_MEDIA_INFO,
      OPTION_START_POSITION);

  public MPlayer(final PlayerEventsHandler playerEventsHandler) {
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
    playerState.setPosition(startPosition + ".0");
    playerState.setCurrentUri(mediaPath);
    playerState.setDuration(MediaInfo.getDuration(mediaPath));
    ArrayList<String> cmdCopy = new ArrayList<>(CMD);
    cmdCopy.add(startPosition + "");
    cmdCopy.add(mediaPath);

    printCommand(cmdCopy);
    ProcessBuilder pb = new ProcessBuilder(cmdCopy);
    try {
      process = pb.start();
      InputStream stdIn = process.getInputStream();
      InputStream errIn = process.getErrorStream();
      stdOut = new PrintStream(process.getOutputStream());

      stdOutConsumer = new MPlayerOutputStreamConsumer(stdIn, playerState, playerEventsHandler);
      Thread one = new Thread(stdOutConsumer);
      one.start();

      errOutConsumer = new MPlayerErrorStreamConsumer(errIn, playerState);
      Thread two = new Thread(errOutConsumer);
      two.start();

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

  private void printCommand(ArrayList<String> cmdCopy) {
    final StringBuffer stringBuffer = new StringBuffer();
    cmdCopy.stream().forEach(o -> stringBuffer.append(o).append(" "));
    LOGGER.info("Executing command: " + stringBuffer.toString());
  }
}