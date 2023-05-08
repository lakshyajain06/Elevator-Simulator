import javax.swing.*;
import java.awt.*;

public class ElevatorSimulatorMain {
	public static void main(String[] args) {
		JFrame window = new JFrame("Elevator");
        
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        Elevator elevator = new Elevator();
        
        ElevatorGraphicsPanel graphicsPanel =  new ElevatorGraphicsPanel(elevator);
        ElevatorSimulatorEventCaller eventRunner = new ElevatorSimulatorEventCaller(elevator, graphicsPanel);
        ElevatorInput inputPanel = new ElevatorInput(elevator, eventRunner);
        
        container.add(graphicsPanel, BorderLayout.CENTER);        
        container.add(inputPanel, BorderLayout.EAST);
        
        window.setContentPane(container);
        
        window.setSize(1440, 1000);
        window.setLocation(100,100);
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setVisible(true);
	}
}
