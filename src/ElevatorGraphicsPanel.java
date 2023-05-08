import javax.swing.JPanel;
import java.awt.*;

public class ElevatorGraphicsPanel extends JPanel{
	
	private Elevator elevator;
	
	public ElevatorGraphicsPanel(Elevator elevator) {
		this.elevator = elevator;
		this.setBackground(new Color(122, 122, 122));
	}
	
	public void paintComponent(Graphics g) {
		
		g.drawLine(0,600,1200,600);
		elevator.draw(g);
	}
	
	
}
