set -x 

sudo service hostapd stop
sudo service dnsmasq stop

sudo cp -v /home/pi/mm/config/etc/dnsmasq.conf.0 /etc/dnsmasq.conf
sudo cp -v /home/pi/mm/config/etc/udhcpd.conf.0 /etc/udhcpd.conf
sudo cp -v /home/pi/mm/config/etc/default/udhcpd.0 /etc/default/udhcpd
sudo cp -v /home/pi/mm/config/etc/default/hostapd.0 /etc/default/hostapd
sudo cp -v /home/pi/mm/config/etc/network/interfaces.0 /etc/network/interfaces
sudo cp -v /home/pi/mm/config/etc/hostapd/hostapd.conf.0 /etc/hostapd/hostapd.conf

sudo update-rc.d hostapd disable
sudo update-rc.d dnsmasq disable

sudo iptables -t nat -D PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
