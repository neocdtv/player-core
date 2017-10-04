What
=====
This project offers a Java API to control mplayer and omxplayer. It was tested only on linux. 

How
=====
Maven
======
    <dependency>
      <groupId>io.neocdtv</groupId>
      <artifactId>player-core</artifactId>
      <version>0.1</version>
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
        final Player player = PlayerFactory.getPlayer("omxplayer");
        // final Player player = PlayerFactory.getPlayer("mplayer");
        final String url = "http://somedomain.eu/video.mp4";
        player.play(url);
    }
