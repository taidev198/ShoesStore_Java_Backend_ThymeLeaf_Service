package com.taidev198.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkAddressUtils {

    public static String GetAddress(String addressType) {
        String address = "";
        InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {

                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }
                    }
                }
            }

            if (lanIp == null) return null;

            if (addressType.equals("ip")) {

                address = lanIp.toString().replaceAll("^/+", "");

            } else if (addressType.equals("mac")) {

            } else {

                throw new Exception("Specify \"ip\" or \"mac\"");
            }

        } catch (UnknownHostException ex) {

            ex.printStackTrace();

        } catch (SocketException ex) {

            ex.printStackTrace();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if (null == mac) return false;
        byte invalidMacs[][] = {
            {0x00, 0x05, 0x69}, // VMWare
            {0x00, 0x1C, 0x14}, // VMWare
            {0x00, 0x0C, 0x29}, // VMWare
            {0x00, 0x50, 0x56}, // VMWare
            {0x08, 0x00, 0x27}, // Virtualbox
            {0x0A, 0x00, 0x27}, // Virtualbox
            {0x00, 0x03, (byte) 0xFF}, // Virtual-PC
            {0x00, 0x15, 0x5D} // Hyper-V
        };

        for (byte[] invalid : invalidMacs) {
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }

    public static List<String> GetMacAddress() { // Change return type from String to List<String>
        Enumeration<NetworkInterface> networks = null;
        try {
            networks = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        NetworkInterface inter;
        List<String> addresses = new ArrayList<>(); // Create list to store all MAC addresses
        while (networks.hasMoreElements()) {
            inter = networks.nextElement();
            byte[] mac = null;
            try {
                mac = inter.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if (mac != null) {
                StringBuilder sb = new StringBuilder(); // Rather than appending with += use a String builder
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format(
                            "%02X%s", mac[i], (i < mac.length - 1) ? "-" : "")); // Use append on the string builder
                }
                addresses.add(sb.toString()); // Add MAC address to the Collection
            }
        }
        return addresses; // Return collection rather than one String
    }
}
