set -x 

sudo service hostapd stop
sudo service udhcpd stop

sudo cp -v /home/pi/mm/src/system/etc/udhcpd.conf.0 /etc/udhcpd.conf
sudo cp -v /home/pi/mm/src/system/etc/default/udhcpd.0 /etc/default/udhcpd
sudo cp -v /home/pi/mm/src/system/etc/default/hostapd.0 /etc/default/hostapd
sudo cp -v /home/pi/mm/src/system/etc/network/interfaces.0 /etc/network/interfaces
sudo cp -v /home/pi/mm/src/system/etc/hostapd/hostapd.conf.0 /etc/hostapd/hostapd.conf

sudo update-rc.d hostapd disable
sudo update-rc.d udhcpd disable

