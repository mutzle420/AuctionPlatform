package de.ba.AuctionPlatform.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.ba.AuctionPlatform.dao.Auction;

/**
 * @author mbrowars
 *
 */
public class CreateAuctionServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1865643688426963594L;
	private static final Logger logger = Logger.getLogger(CreateAuctionServlet.class);

	/**
	 * 
	 */

	@Override
	public void doGet(HttpServletRequest requ, HttpServletResponse resp) throws ServletException, IOException {

		Auction auc = new Auction();
		Part filePart = requ.getPart("file");
		String fileName = filePart.getSubmittedFileName();
		InputStream fileContent = filePart.getInputStream();
		System.out.println(filePart);
		Blob blob = null;
		try {
			blob.getBinaryStream(new Long(requ.getParameter("picture")), 10);
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(requ.getParameter("picture").getBytes());
		auc.setPicture(blob);
		logger.log(Level.INFO, auc.getPicture());
		auc.setTitel(requ.getParameter("title"));
		auc.setBeschreibung(requ.getParameter("desc"));
		auc.setEnddatum(requ.getParameter("end"));
		String gebot = requ.getParameter("bid");
		auc.setGebot(Double.parseDouble(gebot));
		logger.log(Level.INFO, "Auktion :" + auc.getId() + "," + auc.getTitel() + " Wurde angelegt.");
		// TODO save Auction in db
		requ.getRequestDispatcher("/index.jsp").forward(requ, resp);

	}

	public void doPost(HttpServletRequest requ, HttpServletResponse resp) throws ServletException, IOException {
		doGet(requ, resp);
	}
}