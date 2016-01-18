package server.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import server.event.callback.OnConsume;
import server.model.Consumer;
import server.model.MatchingProductsRequest;
import server.model.Producer;
import server.model.Product;
import server.model.ProductRequest;
import server.service.ProductService;
import utils.CompressionUtils;
import utils.LocationUtils;
import utils.NetworkUtils;

import common.config.Configuration;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Broker {

	private final static int COVERED_DISTANCE = 50;
	private final static int PING_THRESHOLD = 200;

	private static ProductService productService = ProductService.getInstance();
	private static List<Product> productList = new ArrayList<Product>();
	private static HashMap<String, MatchingProductsRequest> requestList = new HashMap<String, MatchingProductsRequest>();
	private static Consumer consumer;
	private static Producer<List<Product>> producer;
	private static Producer<Product> uniProducer;

	public static void main(String[] args) throws Exception {

		System.out.println("------------------------------------------");
		System.out.println("----------   Server  Started   -----------");
		System.out.println("------------------------------------------");
		System.out.println("IP : " + NetworkUtils.getMyIP());
		System.out.println("------------------------------------------");

		productList = productService.getAllProducts();

		Thread c = new Thread(new Runnable() {
			@Override
			public void run() {
				consumer = new Consumer(Configuration.BROKER_RECEIVE_PORT);
				consumer.setOnConsume(new OnConsume() {
					@Override
					public void onConsume(Object objectReceived, String ipSource) {
						try {

							if (objectReceived instanceof Product) {
								Product p = (Product) objectReceived;
								productService.saveProduct(p);
								productList.add(p);
								System.out.println("[" + ipSource
										+ "] Receive product : " + p.toString());
								matchSingleProduct();
							} else if (objectReceived instanceof MatchingProductsRequest) {

								MatchingProductsRequest pr = (MatchingProductsRequest) objectReceived;
								requestList.put(ipSource, pr);
								System.out.println("[" + ipSource
										+ "] Received request : "
										+ pr.getCategory());

								matchMultipleProducts(ipSource, pr);
							} else if (objectReceived instanceof ProductRequest) {

								ProductRequest pr = (ProductRequest) objectReceived;
								System.out.println("Requested product ID : "
										+ pr.getId());
								System.out
										.println("Product has been served to the client");
								Product product = productService
										.getProductById(pr.getId());
								uniProducer = new Producer<Product>(ipSource,
										Configuration.CLIENT_RECEIVE_PORT,
										product);
								new Thread(uniProducer).start();
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				Thread consumerThread = new Thread(consumer);
				consumerThread.start();
				try {
					consumerThread.join();
				} catch (InterruptedException ex) {
					System.out.println("ERROR");
				}
			}
		});
		c.start();
	}

	public static void matchSingleProduct() {

		for (Entry<String, MatchingProductsRequest> pr : requestList.entrySet()) {
			for (Product p : productList) {
				if (p.getCategory().toLowerCase()
						.equals(pr.getValue().getCategory().toLowerCase())) {
					uniProducer = new Producer<Product>(pr.getKey(),
							Configuration.CLIENT_RECEIVE_PORT, p);
					new Thread(uniProducer).start();
					// requestList.remove(pr);
				}
			}
		}
	}

	public static void matchMultipleProducts(String sendToAddress,
			MatchingProductsRequest mpr) {

		List<Product> produceList = new ArrayList<Product>();

		for (Product p : productList) {
			if (p.getCategory().toLowerCase()
					.contains(mpr.getCategory().toLowerCase())) {

				double distance = LocationUtils.distanceKm(mpr.getLatitude(),
						mpr.getLongitude(), p.getLongitude(), p.getLatitude());

				if (((int) distance) < COVERED_DISTANCE)
					produceList.add(p);
			}
		}

		System.out.println("Client's Coords : Long=" + mpr.getLongitude()
				+ " lat=" + mpr.getLatitude());

		int pingMoy = NetworkUtils.getPingRatio(sendToAddress);

		if (pingMoy > PING_THRESHOLD) {
			System.out.println("Laaaaaaaaaaaaaaaaaaaag ! Compressing Data");
			for (int i = 0; i < produceList.size(); i++)
				produceList.get(i).setThumbnail(
						CompressionUtils.getCompressedImage(productList.get(i)
								.getThumbnail()));
		} else {
			System.out.println("Good connection");
		}

		producer = new Producer<List<Product>>(sendToAddress,
				Configuration.CLIENT_RECEIVE_PORT, produceList);
		System.out.println(produceList.toString());
		new Thread(producer).start();
	}

}
