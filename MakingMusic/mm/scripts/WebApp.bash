set -x
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo modprobe bcm2835-v4l2
java \
-Djava.library.path=/home/pi/mm/lib/arm7l \
-cp /home/pi/mm/binary/MakingMusic.jar \
com.example.afs.makingmusic.webapp.WebApp
