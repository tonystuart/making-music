set -x
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo modprobe bcm2835-v4l2
java \
-Djava.library.path=/home/pi/opencv-3.1.0/build/lib:/home/pi/lib \
-cp /home/pi/mm/binary/MakingMusic.jar \
com.example.afs.makingmusic.webapp.WebApp
