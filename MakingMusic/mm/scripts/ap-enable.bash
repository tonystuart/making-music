set -x 

sudo cp -v /home/pi/mm/config/etc/dnsmasq.conf.1 /etc/dnsmasq.conf
sudo cp -v /home/pi/mm/config/etc/default/hostapd.1 /etc/default/hostapd
sudo cp -v /home/pi/mm/config/etc/network/interfaces.1 /etc/network/interfaces
sudo cp -v /home/pi/mm/config/etc/hostapd/hostapd.conf.1 /etc/hostapd/hostapd.conf

sudo update-rc.d hostapd enable
sudo update-rc.d dnsmasq enable

sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
