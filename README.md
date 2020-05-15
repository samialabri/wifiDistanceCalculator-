# wifiDistanceCalculator-
This solution was developed in JAVA. the main purpose of it is to calculate the location of a device inside a room. The solution is divided into a server and a client. The clients gets the readings, calculate the distance by subtitling in this equation:\
**distance = 10 ^ ((k - (20 * log10(frequency)) + signal level)/20)**\
the constant k needs to be calculated for every device. To do so the client first gets calibrated by placing a device 1m from the station. Then it finds the average signal level and substitute it in following equation:\ 
**k = ((20 * (ln(distance)/ln(10))) + 20 * ln(frequency) – signal level)**\
Then it sends them to the server. In order to so; the frequency and signal level of the device is required. To get the readings the solution depends on the Aircrack-ng suite. To do so the following steps needs to be executed:\ 

1.	If not installed, java run time environment needs to be installed in both the stations and server:
```
$sudo apt update
$sudo apt install default-jdk
```
2.	If not installed,aircrack-ng suite needs to be installed in the stations: 
```
$sudo apt install aircrack-ng
```
3.	Connect all the stations and the server and note their ip address by typing the following command:
```
$ifconfig
```
4.	Access each station using ssh and first configure the correct Wi-Fi interface to be in Peromyscus mode by:
```
$sudo airmon-ng start “interface name”
```
5.	Then start recording the values of the Wi-Fi devices around and writing them into a csv file: 
```
$sudo airodump-ng -w “file name” –output-format csv “interface name + mon”
```
-**Note:** if you type for example ‘b’ as a file name, the output file will be b-01.csv.\
-**Note:** if you start the airmon-ng on interface wlan1 in step 4, the interface name will be wlan1mon.\
\
6.	Once airodump-ng starts, ssh again in a different terminal to the same Wi-Fi station to run the solution, to run it type the following command and follow the instruction:
```
$java -jar wifiDistanceCalculator-.jar
```
7.	At the same time log in to the server and run the server module by typing:
```
$java -jar Server.jar
```
The client is programmed to utilize TCP sockets to send the data to the server. The server was programmed to multithreaded in order for it to get multiple reading from different devices at the same time. 

