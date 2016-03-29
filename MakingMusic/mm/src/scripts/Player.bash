set -x
sudo modprobe bcm2835-v4l2
java \
-Djava.library.path=/home/pi/opencv-3.1.0/build/lib:/home/pi/lib \
-cp /home/pi/mm/bin/MakingMusic.jar \
com.example.afs.makingmusic.Player
