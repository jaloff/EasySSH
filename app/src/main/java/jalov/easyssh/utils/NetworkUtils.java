package jalov.easyssh.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;

/**
 * Created by jalov on 2018-01-24.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    private NetworkUtils(){}

    public static Optional<String> getIPAddress() {
        Optional<String> wlan;
        Optional<String> rmnet = Optional.empty();
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface networkInterface : Collections.list(ni)){
                if(networkInterface.getName().compareTo("wlan0") == 0) {
                    wlan = getIPFromInetAddresses(networkInterface.getInetAddresses());
                    if(wlan.isPresent()) {
                        return wlan;
                    }
                } else if (networkInterface.getName().compareTo("rmnet0") == 0) {
                    rmnet = getIPFromInetAddresses(networkInterface.getInetAddresses());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return rmnet;
    }

    private static Optional<String> getIPFromInetAddresses(Enumeration<InetAddress> addresses) {
        for(InetAddress address : Collections.list(addresses)) {
            if(address instanceof Inet4Address) {
                return Optional.of(address.getHostAddress());
            }
        }
        return Optional.empty();
    }
}
