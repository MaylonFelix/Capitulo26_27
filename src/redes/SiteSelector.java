package redes;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SiteSelector extends JApplet{
	private HashMap<String, URL> sites;
	private ArrayList<String> siteNames;
	private JList siteChooser;
	
	public void init(){
		sites = new HashMap<String, URL>();
		siteNames = new ArrayList<String>();
		getSitesFromHTMLParameters();
		add(new JLabel("Choose a site to browser"), BorderLayout.NORTH);
		siteChooser = new JList(siteNames.toArray());
		siteChooser.addListSelectionListener(
				new ListSelectionListener() {
			
			
			public void valueChanged(ListSelectionEvent event) {
				Object object = siteChooser.getSelectedValue();
				URL newDocument = sites.get(object);
				AppletContext browser = getAppletContext();
				browser.showDocument(newDocument);
				
			}
		});
		
		add(new JScrollPane(siteChooser));
	}

	private void getSitesFromHTMLParameters() {
		String title;
		String location;
		URL url;
		int counter = 0;
		
		title = getParameter("title"+counter);
		while(title!=null){
			location = getParameter("location"+counter);
			try {
				url = new URL(location);
				sites.put(title, url);
				siteNames.add(title);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			++counter;
			title = getParameter("title"+counter);
			
		}
		
	}
	

}
