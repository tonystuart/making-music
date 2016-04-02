set -x
sudo modprobe bcm2835-v4l2
java \
-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y \
-Djava.library.path=/home/pi/opencv-3.1.0/build/lib:/home/pi/lib \
-cp /home/pi/mm/binary/MakingMusic.jar \
com.example.afs.makingmusic.webapp.WebApp
