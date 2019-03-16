package comp1206.sushi.server;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

/**
 * Provides the Sushi Server user interface
 *
 */
public class ServerWindow extends JFrame implements UpdateListener {

	private static final long serialVersionUID = -4661566573959270000L;
	private ServerInterface server;

	private static final String[] TABLES = {"Orders", "Dishes", "Ingredients", "Suppliers", "Staff", "Users", "Drones", "Postcodes", "Configuration"};
	
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
	public void refreshAll() {
		
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
		JPanel panel = new JPanel();
		this.setContentPane(panel);
		panel.setLayout(new BorderLayout());

		TableView view = new TableView(TABLES);
		panel.add(view, BorderLayout.CENTER);

		// TODO: Refactor into GridBagLayout or BoxLayout.
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout());

		JButton btnAdd = new JButton("Add");
		btnPanel.add(btnAdd);

		JButton btnDelete = new JButton("Delete");
		btnPanel.add(btnDelete);

		panel.add(btnPanel, BorderLayout.SOUTH);
	}
}
