/**
 *  SE-PROJEKT_AuctionPlatform
 * 
 *  team:	Markus Fr�hlich, Max G�ppert, Matthias Browarski
 *
 */
package de.ba.auctionPlatform.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import de.ba.auctionPlatform.dao.Auction;

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
			logger.log(Level.INFO,
					"Auktion: " + auction.getAuctionid() + "," + auction.getTitel() + " wurde angelegt.");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR, "Auktion: " + auction.getAuctionid() + "," + auction.getTitel()
					+ " konnte nicht angelegt werden " + e);
		} finally {
			session.close();

		}
		return auction.getAuctionid();
	}

	/* Auktion l�schen */
	/**
	 * @param auction
	 */
	public static void removeAuction(int auctionid) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query remove = session.createQuery("DELETE Auction where AUCTIONID= " + auctionid);

			remove.executeUpdate();
			tx.commit();
			logger.log(Level.INFO, "Auktion: " + auctionid + " wurde gel�scht.");

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR, "Auktion: " + auctionid + " konnte nicht gel�scht werden." + e);
		} finally {
			session.close();
		}
	}

	/* Auktion �ndern */
	/**
	 * @param auction
	 */
	public static void updateAuction(Auction auction) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Auction auctionUpdate = session.get(Auction.class, auction.getAuctionid());

			if (auction.getTitel() != null) {
				auctionUpdate.setTitel(auction.getTitel());
			}
			if (auction.getGebot() != null) {
				auctionUpdate.setGebot(auction.getGebot());
			}
			if (auction.getLaufzeit() != null) {
				auctionUpdate.setLaufzeit(auction.getLaufzeit());
			}
			if (auction.getBeschreibung() != null) {
				auctionUpdate.setBeschreibung(auction.getBeschreibung());
			}
			if (auction.getHoechstbietenderid() != 0) {
				auctionUpdate.setHoechstbietenderid(auction.getHoechstbietenderid());
			}
			if (auction.getPicture() != null) {
				auctionUpdate.setPicture(auction.getPicture());
			}
			session.update(auctionUpdate);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			logger.log(Level.ERROR, "Auktion: " + auction.getAuctionid() + " konnte nicht aktualisiert werden." + e);
		} finally {
			session.close();
		}
	}

	/* Auktion auslesen */
	public static Auction getAuction(int auctionid) {
		Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
		Transaction tx = null;
		Auction auction = new Auction();

		try {
			tx = session.beginTransaction();
			Query wantedauction = session.createQuery("FROM Auction WHERE auctionid= :auctionid");
			wantedauction.setInteger("auctionid", auctionid);
			Object queryResult = wantedauction.uniqueResult();
			auction = (Auction) queryResult;
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.log(Level.ERROR, "Auktion konnte nicht ausgelesen werden." + e);
			session.close();
		}

		return auction;
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
