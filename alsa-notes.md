## raspberry pi 3;Creative Sound Blaster X-Fi HD USB Audio System
### list alsa devices
$ aplay -l
**** List of PLAYBACK Hardware Devices ****
card 0: ALSA [bcm2835 ALSA], device 0: bcm2835 ALSA [bcm2835 ALSA]
  Subdevices: 7/7
  Subdevice #0: subdevice #0
  Subdevice #1: subdevice #1Creative Sound Blaster X-Fi HD USB Audio System
  Subdevice #2: subdevice #2
  Subdevice #3: subdevice #3
  Subdevice #4: subdevice #4
  Subdevice #5: subdevice #5
  Subdevice #6: subdevice #6
card 0: ALSA [bcm2835 ALSA], device 1: bcm2835 IEC958/HDMI [bcm2835 IEC958/HDMI]
  Subdevices: 1/1
  Subdevice #0: subdevice #0
card 0: ALSA [bcm2835 ALSA], device 2: bcm2835 IEC958/HDMI1 [bcm2835 IEC958/HDMI1]
  Subdevices: 1/1
  Subdevice #0: subdevice #0
card 1: HD [USB Sound Blaster HD], device 0: USB Audio [USB Audio]
  Subdevices: 1/1
  Subdevice #0: subdevice #0
card 1: HD [USB Sound Blaster HD], device 1: USB Audio [USB Audio #1]
  Subdevices: 1/1
  Subdevice #0: subdevice #0
card 1: HD [USB Sound Blaster HD], device 2: USB Audio [USB Audio #2]
  Subdevices: 1/1
  Subdevice #0: subdevice #0

### use alsa device X-Fi HD USB and analog/chinch output;1.0
$ mplayer -novideo -ao alsa:device=hw=1.0 video.mp4
$ omxplayer -o alsa:hw:1,0 video.mp4

### use alsa device X-Fi HD USB and digital/spdif output:1.1 also 1.2 can be used not sure about the difference
$ mplayer -novideo -ao alsa:device=hw=1.1 video.mp4
$ omxplayer -o alsa:hw:1,1 video.mp4