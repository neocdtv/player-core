package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.PlayerEventsHandlerForTests;
import org.junit.Test;

import java.net.URL;

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

  PlayerEventsHandlerForTests eventsHandler = spy(new PlayerEventsHandlerForTests());
  private MPlayer mplayer = new MPlayer(eventsHandler);

  @Test
  public void startingPlayback() throws InterruptedException {
    // given
    URL resource = Thread.currentThread().getContextClassLoader().getResource("audio/1.mp3");
    // when
    mplayer.play(resource.getPath());
    // then
    verify(eventsHandler).onStaringPlayback();
    verify(eventsHandler).onTrackEnded();
  }

  @Test
  public void startingPlayback_not_possible_wrong_path() throws InterruptedException {
    // when
    mplayer.play("non_existent_file");
    // then
    verify(eventsHandler, never()).onStaringPlayback();
    verify(eventsHandler).onTrackEnded();
  }

  @Test
  public void startingPlayback_not_possible_unsupported_format() throws InterruptedException {
    // given
    URL resource = Thread.currentThread().getContextClassLoader().getResource("text/1.txt");
    // when
    mplayer.play(resource.getPath());
    // then
    verify(eventsHandler, never()).onStaringPlayback();
    verify(eventsHandler).onTrackEnded();
  }
}