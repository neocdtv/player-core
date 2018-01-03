What
=====
This project offers a Java API to control mplayer and omxplayer. It was tested only on Linux. 

How
=====
Maven
======
    <dependency>
      <groupId>io.neocdtv</groupId>
      <artifactId>player-core</artifactId>
      <version>0.2</version>
    </dependency>
    
Ubuntu/Debian
======
    # you always need to install mplayer, even if you want to use omxplayer
    # the mplayer is used to get info about the multimedia file i.e. duration
    sudo apt-get install mplayer
    
    # omxplayer on raspberry pi 
    sudo apt-get install omxplayer

Java
======
    public static void main(String[] args) {
        final EventsHandler eventsHandler = new MPlayerEventsHandler();
        final Player player = new MPlayer(eventsHandler)
        //final Player player = new OmxPlayer(eventsHandler);
        final String url = "http://somedomain.eu/video.mp4";
        player.play(url);
    }

EventsHandler
======
You can find a example of a EventsHandler implementation in my other project: https://github.com/neocdtv/leanplayer-renderer/blob/master/src/main/java/io/neocdtv/leanplayer/renderer/control/MPlayerEventsHandler.java
