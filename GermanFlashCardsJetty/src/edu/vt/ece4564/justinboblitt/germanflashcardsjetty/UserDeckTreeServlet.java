package edu.vt.ece4564.justinboblitt.germanflashcardsjetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserDeckTreeServlet extends HttpServlet {

	private static final String JSON_KEY_ALL_USER_DECK_TREE = "JSON_KEY_ALL_USER_DECK_TREE";
	private static final String JSON_KEY_USERNAME = "JSON_KEY_USERNAME";
	private static final String JSON_KEY_SINGLE_USER_DECK_TREE = "JSON_KEY_SINGLE_USER_DECK_TREE";
	private Hashtable<String, String> m_userDecks = new Hashtable<String, String>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");

		String strResp = "";
		String username = req.getParameter("username");
		if (username == null) {
			// String addr = req.getRemoteAddr();
			// String host = req.getRemoteHost();
			// String url = req.getRequestURL().toString();
			//
			// strResp = "UserDeckTreeServlet username: " + username + "\n" +
			// "addr: " + addr + "\n"
			// + "host: " + host + "\n" + "url: " + url + "\n";

			JSONObject json = new JSONObject();
			JSONArray jsonAllUsers = new JSONArray();
			Iterator it = m_userDecks.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pairs = (Entry<String, String>) it.next();
				JSONObject singleUser = new JSONObject();
				singleUser.put(JSON_KEY_USERNAME, pairs.getKey());
				singleUser.put(JSON_KEY_SINGLE_USER_DECK_TREE, pairs.getValue());
				jsonAllUsers.add(singleUser);
			}
			json.put(JSON_KEY_ALL_USER_DECK_TREE, jsonAllUsers);
			strResp += json.toString();

		} else {
			strResp += m_userDecks.get(username);
		}
		resp.getWriter().write(strResp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username = req.getParameter("username");
		if (username != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					req.getInputStream()));
			StringBuilder jsonTree = new StringBuilder();
			String read;

			try {
				while ((read = br.readLine()) != null) {
					jsonTree.append(read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	

			m_userDecks.put(username, jsonTree.toString());
		}
	}
}
