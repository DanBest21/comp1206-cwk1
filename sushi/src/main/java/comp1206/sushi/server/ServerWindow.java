package comp1206.sushi.server;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import comp1206.sushi.common.*;
import comp1206.sushi.server.components.ButtonPanel;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.table.TableView;

/**
 * Provides the Sushi Server user interface
 *
 */
public class ServerWindow extends JFrame implements UpdateListener {

	private static final long serialVersionUID = -4661566573959270000L;
	private ServerInterface server;
	private TableView view;
	
	/**
	 * Create a new server window
	 * @param server instance of server to interact with
	 */
	public ServerWindow(ServerInterface server) {
		super("Sushi Server");
		this.server = server;
		this.setTitle(server.getRestaurantName() + " Server");
		server.addUpdateListener(this);
		
		//Display window
		setSize(800,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupWindow();

		setVisible(true);
		
		//Start timed updates
		startTimer();
	}
	
	/**
	 * Start the timer which updates the user interface based on the given interval to update all panels
	 */
	public void startTimer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);     
        int timeInterval = 5;
        
        scheduler.scheduleAtFixedRate(() -> refreshAll(), 0, timeInterval, TimeUnit.SECONDS);
	}
	
	/**
	 * Refresh all parts of the server application based on receiving new data, calling the server afresh
	 */
	public void refreshAll()
	{
		// TODO: Figure out why this is causing the row to be deselected.
		// view.updateTables();
	}
	
	@Override
	/**
	 * Respond to the model being updated by refreshing all data displays
	 */
	public void updated(UpdateEvent updateEvent) {
		refreshAll();
	}

	public void setupWindow()
	{
		setIconImage(ServerConfiguration.getImg());

		JPanel panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE);

		view = new TableView(ServerConfiguration.getTabs(), server, this);
		panel.add(view, BorderLayout.CENTER);

		JPanel btnPanel = new ButtonPanel(view, server);
		panel.add(btnPanel, BorderLayout.SOUTH);

		view.addChangeListener(e -> ((ButtonPanel) btnPanel).generateButtons());
	}

	public void setupForMapView()
	{
		setSize(ServerConfiguration.getMapWidth() + 15, ServerConfiguration.getMapHeight() + 75);
		setResizable(false);
	}
}
