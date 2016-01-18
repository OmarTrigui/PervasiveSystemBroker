package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtils {

	public static List<NetworkInterface> getNetworkInterfacesNames() {

		List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();

		try {
			Enumeration<NetworkInterface> list = NetworkInterface
					.getNetworkInterfaces();

			while (list.hasMoreElements()) {
				NetworkInterface netInterface = list.nextElement();
				if (!netInterface.isLoopback())
					interfaces.add(netInterface);
			}

		} catch (SocketException ex) {
			ex.printStackTrace();
		}

		return interfaces;
	}

	public static InetAddress getMyIP() {
		List<NetworkInterface> netInterfaces = getNetworkInterfacesNames();
		NetworkInterface ni = null;

		if (netInterfaces != null && netInterfaces.size() != 0) {
			ni = netInterfaces.get(0);
		}

		return getMyIP(ni);
	}

	public static InetAddress getMyIP(NetworkInterface _interface) {
		if (_interface == null)
			return null;

		Enumeration<InetAddress> address = _interface.getInetAddresses();
		while (address.hasMoreElements()) {
			InetAddress addr = address.nextElement();
			if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
					&& !(addr.getHostAddress().indexOf(":") > -1)) {
				return addr;
			}
		}

		return null;
	}

	public static int getPingRatio(String sendToAddress) {

		System.out.println("Bandwidth Test Started (Ping)");
		try {

			String ip = sendToAddress;
			int pingCount = 3;
			String pingCmd = "ping -s 60000 -c " + pingCount + " " + ip;
			try {
				Runtime r = Runtime.getRuntime();
				Process p = r.exec(pingCmd);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));

				in.readLine();

				double sum = 0;

				for (int i = 0; i < pingCount; i++) {
					String input = in.readLine();
					sum += Double
							.parseDouble(input.split(" ")[6].split("=")[1]);
				}

				in.close();

				System.out.println("Ping : " + (int) (sum / pingCount));
				
				System.out.println("Bandwidth Test Finished");

				return (int) (sum / pingCount);

			} catch (IOException e) {
				System.out.println(e);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return -1;
	}
}
