set -x 

sudo cp -v /home/pi/mm/src/system/etc/udhcpd.conf.1 /etc/udhcpd.conf
sudo cp -v /home/pi/mm/src/system/etc/default/udhcpd.1 /etc/default/udhcpd
sudo cp -v /home/pi/mm/src/system/etc/default/hostapd.1 /etc/default/hostapd
sudo cp -v /home/pi/mm/src/system/etc/network/interfaces.1 /etc/network/interfaces
sudo cp -v /home/pi/mm/src/system/etc/hostapd/hostapd.conf.1 /etc/hostapd/hostapd.conf

sudo update-rc.d hostapd enable
sudo update-rc.d udhcpd enable

