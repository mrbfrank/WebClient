/**
 * A simple web client with a GUI.
 */

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class WebClient extends Frame {
    /* The stuff for the GUI. */
    private Button btRequest = new Button("Request");
    private Button btRefresh = new Button("Refresh");
    private Button btClear = new Button("Clear");
    private Button btQuit = new Button("Quit");
    private Label URLLabel = new Label("URL:");
    private TextField URLField = new TextField("", 40);
    private Label webpageLabel = new Label("Webpage:");
    private TextArea webpageText = new TextArea(10, 40);

    /**
     * Create a new WebClient window with fields for entering all
     * the relevant information (URL).
     */
    public WebClient() {
	super("Java WebClient");
	
	/* Create panels for holding the fields. To make it look nice,
	   create an extra panel for holding all the child panels. */
	Panel URLPanel = new Panel(new BorderLayout());
	Panel webpagePanel = new Panel(new BorderLayout());
	URLPanel.add(URLLabel, BorderLayout.WEST);
	URLPanel.add(URLField, BorderLayout.CENTER);
	webpagePanel.add(webpageLabel, BorderLayout.NORTH);	
	webpagePanel.add(webpageText, BorderLayout.CENTER);
	Panel fieldPanel = new Panel(new GridLayout(0, 1));
	fieldPanel.add(URLPanel);

	/* Create a panel for the buttons and add listeners to the
	   buttons. */
	Panel buttonPanel = new Panel(new GridLayout(1, 0));
	btRequest.addActionListener(new RequestListener());
	btRefresh.addActionListener(new RefreshListener());
	btClear.addActionListener(new ClearListener());
	btQuit.addActionListener(new QuitListener());
	buttonPanel.add(btRequest);
	buttonPanel.add(btRefresh);
	buttonPanel.add(btClear);
	buttonPanel.add(btQuit);
	
	/* Add, pack, and show. */
	add(fieldPanel, BorderLayout.NORTH);
	add(webpagePanel, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
	pack();
	show();
    }

    static public void main(String argv[]) {
	new WebClient();
    }

    /* Handler for the Request-button. */
    class RequestListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    System.out.println("Requesting webpage");

	    /* First, check that we have the URL. */
	    if((URLField.getText()).equals("")) {
		System.out.println("Need URL!");
		return;
	    }

	    /* Establish the connection and try to 
	       request the webpage. */
	    try {
		HTTPConnection connection = new HTTPConnection(URLField.getText());
		if (!connection.readyToOpen) {
			System.out.println("Connection failed!");
			return;
		}
		webpageText.setText(connection.requestWebpage());
		connection.close();
	    } catch (IOException error) {
		System.out.println("Requesting webpage failed: " + error);
		return;
	    }

	    System.out.println("Webpage requested succesfully!");
	}
    }

    /* Handler for the Refresh-button. */
    class RefreshListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    System.out.println("Refreshing webpage");

	    /* First, check that we have the URL. */
	    if((URLField.getText()).equals("")) {
		System.out.println("Need URL!");
		return;
	    }

	    /* Establish the connection and try to 
	       refresh the webpage. */
	    try {
		HTTPConnection connection = new HTTPConnection(URLField.getText());
		if (!connection.readyToOpen) {
			System.out.println("Connection failed!");
			return;
		}
		webpageText.setText(connection.refreshWebpage());
		connection.close();
	    } catch (IOException error) {
		System.out.println("Refreshing webpage failed: " + error);
		return;
	    }

	    System.out.println("Webpage refreshed succesfully!");
	}
    }

    /* Clear the fields on the GUI. */
    class ClearListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    System.out.println("Clearing fields");
	    URLField.setText("");
	    webpageText.setText("");
	}
    }
    /* Quit. */
    class QuitListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    System.exit(0);
	}
    }
}
