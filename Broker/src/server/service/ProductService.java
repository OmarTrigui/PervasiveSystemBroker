package server.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import server.model.Product;

public class ProductService {

	private static final ProductService instance = new ProductService();

	public static SessionFactory factory;

	private ProductService() {
		Configuration configuration = new Configuration()
				.configure("hibernate.cfg.xml");
		ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
		registry.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
		factory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static ProductService getInstance() {
		return instance;
	}

	public void saveProduct(Product product) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Product getProductById(int id) {
		Session session = factory.openSession();
		Product product = null;

		try {
			product = (Product) session.load(Product.class, id);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getAllProducts() {
		Session session = factory.openSession();
		return session.createCriteria(Product.class).list();
	}

}
