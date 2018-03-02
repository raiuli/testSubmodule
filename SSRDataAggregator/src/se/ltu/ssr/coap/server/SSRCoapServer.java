package se.ltu.ssr.coap.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;

import se.ltu.ssr.adapter.interfaces.DataPacket;


public class SSRCoapServer extends CoapServer {
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
	//private static final int COAP_PORT=80;
	Logger log=Logger.getLogger(SSRCoapServer.class);
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	
	public SSRCoapServer(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}
	public void StartServer(){
		addEndpoints();
		add(new EnviormentalDataResource("S0",this.dataQueue));
		add(new EnviormentalDataResource("S1",this.dataQueue));
		start();
	}
	/**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     */
    private void addEndpoints() {
    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    }
}
