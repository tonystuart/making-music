set -x
sudo modprobe bcm2835-v4l2
java \
-Djava.library.path=/home/pi/mm/lib/arm7l \
-cp /home/pi/mm/binary/MakingMusic.jar \
com.example.afs.makingmusic.Player
