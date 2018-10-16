package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.PlayerEventsHandlerForTests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.WantedButNotInvoked;

import java.net.URL;
import java.util.logging.Logger;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * MPlayerTest.
 *
 * @author xix
 * @since 25.02.18
 */
public class MPlayerTest {

  private static final Logger LOGGER = Logger.getLogger(MPlayerTest.class.getName());
  private static final int MILLIS = 250;
  private PlayerEventsHandlerForTests eventsHandler = spy(new PlayerEventsHandlerForTests());
  private MPlayer mplayer = new MPlayer(new Amixer("mono"));

  @Before
  public void setup() {
    mplayer.addPlayerEvent(eventsHandler);
  }

  @Test
  public void startingPlayback() throws InterruptedException {
    // given
    URL resource = Thread.currentThread().getContextClassLoader().getResource("audio/1.mp3");
    // when
    mplayer.play(resource.getPath());
    // then
    boolean checkAgain = true;
    int currentCheck = 0;
    final int maxChecks = 10;
    while (checkAgain) {
      try {
        verify(eventsHandler).onStaringPlayback();
        verify(eventsHandler).onTrackEnded();
        checkAgain = false;
      } catch (WantedButNotInvoked e) {
        currentCheck++;
        if (currentCheck <= maxChecks) {
          LOGGER.info("Verification didn't work, sleeping " + MILLIS + "ms and trying again");
          Thread.sleep(MILLIS);
        } else {
          throw e;
        }
      }
    }
  }

  @Test
  public void startingPlayback_not_possible_wrong_path() throws InterruptedException {
    // when
    mplayer.play("non_existent_file");
    // then
    verifyBehaviour();
  }

  @Test
  public void startingPlayback_not_possible_unsupported_format() throws InterruptedException {
    // given
    URL resource = Thread.currentThread().getContextClassLoader().getResource("text/1.txt");
    // when
    mplayer.play(resource.getPath());
    // then
    verifyBehaviour();
  }

  private void verifyBehaviour() throws InterruptedException {
    boolean checkAgain = true;
    int currentCheck = 0;
    final int maxChecks = 10;
    while (checkAgain) {
      try {
        verify(eventsHandler, never()).onStaringPlayback();
        verify(eventsHandler).onTrackEnded();
        checkAgain = false;
      } catch (WantedButNotInvoked e) {
        currentCheck++;
        if (currentCheck <= maxChecks) {
          LOGGER.info("Verification didn't work, sleeping " + MILLIS + "ms and trying again");
          Thread.sleep(MILLIS);
        } else {
          throw e;
        }
      }
    }
  }
}