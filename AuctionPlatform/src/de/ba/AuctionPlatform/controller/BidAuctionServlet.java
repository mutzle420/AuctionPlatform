package de.ba.AuctionPlatform.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.ba.AuctionPlatform.codegen.Codegenerator;
import de.ba.AuctionPlatform.codegen.Codevalidator;
import de.ba.AuctionPlatform.dao.Admin;
import de.ba.AuctionPlatform.dao.Auction;
import de.ba.AuctionPlatform.dao.AuctionDAO;
import de.ba.AuctionPlatform.dao.User;
import de.ba.AuctionPlatform.dao.UserDAO;
import de.ba.AuctionPlatform.emailservice.SendMail;

/**
 * @author Matthias Browarski
 *
 */
public class BidAuctionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8982518104080193013L;
	private static final Logger logger = Logger.getLogger(BidAuctionServlet.class);
	private int code = 0;
	private Double bid;
	private String mail;
	private int saved;

	@Override
	public void doGet(HttpServletRequest requ, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = requ.getSession(true);
		SendMail em = new SendMail();
		BidHandler bi = new BidHandler();
		Codegenerator gen = new Codegenerator();
		UserDAO userd = new UserDAO();
		User user = new User();
		Auction auc = new Auction();
		AuctionDAO aucd = new AuctionDAO();
		Codevalidator valid = new Codevalidator();

		// Angebot mit der Datenbank vergleichen / Email senden /
		// Sicherkeitscode testen
		if ((requ.getParameter("mail") != null) && (requ.getParameter("bid") != null)) {
			code = gen.auctionSecurity();
			System.out.println(code);
			bid = Double.parseDouble(requ.getParameter("bid"));
			if (bid < 0) {
				resp.getWriter().write("Bitte geben Sie ein positives Gebot ein.");
				logger.log(Level.WARN, "Negatives Gebot wurde eingetragen.");
			}

			// mail versenden
			mail = requ.getParameter("mail");
			int id = Integer.parseInt(requ.getParameter("id"));
			if (bi.checkBid(id, bid) == true) {

				try {

					em.send(mail, "Auktionsbestaetigung fuer Artikel (plus id)",
							"Bitte geben Sie diesen Code auf der Website ein: " + code);
					user.setEmail(mail);
					user.setCode(code);
					userd.addUser(user);
					session.setAttribute("user", user);
					saved = user.getId();
					resp.getWriter().write("null");

				} catch (MessagingException e) {

					// TODO Auto-generated catch block
					resp.getWriter().write("Fehler bei Emailuebertragung");
					userd.removeUser(user);
				}
			} else {
				resp.getWriter().write("Biete mehr als den aktuellen Preis");
			}
		}

		// übergebener emailcode
		if (requ.getParameter("code") != null) {
			String usercode = requ.getParameter("code");

			boolean rightcode = valid.validate(usercode, code);

			if (rightcode) {
				// Angebot speichern und status und code an view uebergeben
				User user1 = (User) session.getAttribute("user");
				if (bi.saveBid(user1.getId(), bid) == true) {
					auc.setGebot(bid);
					aucd.updateAuction(auc);
					resp.getWriter().write("null");
				} else {
					resp.getWriter().write("Gebot konnte nicht gespeichert werden!");
				}
			} else {
				resp.getWriter().write("Falscher Code wurde eingegeben!");
			}

		}

	}

	public void doPost(HttpServletRequest requ, HttpServletResponse resp) throws ServletException, IOException {
		doGet(requ, resp);
	}
}
