package de.ba.AuctionPlatform.dao;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;

import de.ba.AuctionPlatform.controller.CreateAuctionServlet;
import de.ba.AuctionPlatform.dao.Auction;

public class AuctionDAO {

	private static final Logger logger = Logger.getLogger(AuctionDAO.class);

	/* Auktion anlegen */

	/**
	 * @param auctionid
	 * @param titel
	 * @param gebot
	 * @param enddatum
	 * @param beschreibung
	 * @param hoechstbietenderid
	 * @param picture
	 * @return auctionid
	 */
	public static int addAuction(Auction auction) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			
			session.save(auction);
			tx.commit();
			logger.log(Level.INFO, "Auktion: " + auction.getId() + "," + auction.getTitel() + " wurde angelegt.");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR,
					"Auktion: " + auction.getId() + "," + auction.getTitel() + " konnte nicht angelegt werden " + e);
		} finally {
			session.close();

		}
		return auction.getId();
	}

	/* Auktion l�schen */
	/**
	 * @param auction
	 */
	public void removeAuction(Auction auction) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(auction);
			tx.commit();
			logger.log(Level.INFO, "Auktion: " + auction + " wurde angel�scht.");

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR, "Auktion: " + auction + " konnte nicht gel�scht werden." + e);
		} finally {
			session.close();
		}
	}

	/* Auktion �ndern */
	/**
	 * @param auction
	 */
	public void updateAuction(Auction auction) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(auction);
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR, "Auktion: " + auction + " konnte nicht aktualisiert werden." + e);
		} finally {
			session.close();
		}
	}

	/**
	 * @return
	 */
	public static List getAllAuctions() {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;
		List auctions = new ArrayList();
		try {
			tx = session.beginTransaction();
			auctions = (List) session.createQuery("FROM Auction").list();
			tx.commit();
			logger.log(Level.INFO, "Index.jsp wurde aktualisiert.");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.log(Level.ERROR, "Es konnten keine Auktionen geladen werden." + e);
		} finally {
			session.close();
		}
		return auctions;
	}
}
