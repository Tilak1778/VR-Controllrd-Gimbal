# VR Controlled Gimbal 
In software part this project includes one android application(RPi camera Viewer) and a python file(Raspberry_code.py) for rpi to controll two servos  





# RPi Camera Viewer

This app plays the raw H.264 video from a Raspberry Pi.


# Introduction
The RPi Camera Viewer (RPiC) is an open source app for Android that plays the raw H.264 video stream that is produced by the Raspberry Pi‘s camera software.


# Overview
The raspivid program generates a raw H.264 video stream, which is then sent out over the network (streamed) using one of the following methods:

TCP/IP using netcat
HTTP using UV4L
Multicast using multicat (not fully supported)
RPiC plays these streams on an Android device.

The first time you run the app, it automatically scans the network for cameras. It looks for TCP/IP cameras on port 5001 and it looks for UV4L HTTP cameras on port 8080. Whatever it finds it adds to the list of cameras, which is displayed as the main screen of the app. You can do a manual scan any time via the main menu, and you can change the ports that are scanned by going to the program settings, again via the main menu.

Tapping on a camera in the main list will display the video from that camera in VR mode. It’s a very simple viewer and the only supported operations are:

pinch to zoom the image in and out
pan around a zoomed image by dragging it with one finger
double tap the image to clear the zoom
press the Back button to return to the list of cameras
press the Snapshot button to save an image of the current video to your photos



The app really doesn’t do much more than that. No play or pause, rewind or fast forward, no recording. It just plays the live stream from one of the cameras.
# Streaming Raw H.264 From A Raspberry Pi
direct terminal input for video streaming from rpi 
"raspivid -n -ih -t 0 -rot 0 -w 1280 -h 720 -fps 15 -b 1000000 -o - | ncat -lkv4 5001"

[for more information check this out](http://frozen.ca/streaming-raw-h-264-from-a-raspberry-pi/)

