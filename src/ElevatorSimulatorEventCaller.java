import java.awt.event.*;
import javax.swing.Timer;

public class ElevatorSimulatorEventCaller implements ActionListener{

	private final int FPS = 100;
	private final int subdivisions = 15;

	private Elevator elevator;
	private ElevatorGraphicsPanel graphicsPanel;

	private Timer systemTimer;

	private int secondsRunning;
	private double simulationSpeed;

	public ElevatorSimulatorEventCaller(Elevator elevator, ElevatorGraphicsPanel graphicsPanel) {
		this.elevator = elevator;
		this.graphicsPanel = graphicsPanel;

		systemTimer = new Timer(1000/FPS, this);
		systemTimer.start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		double dt = (double)systemTimer.getDelay()/1000 * simulationSpeed/100;

		for(int i = 0; i < subdivisions; i++){
			if(elevator.isSimulationRunning()) {
				elevator.updatePhysics(dt/subdivisions);
			}
		 }
		

			graphicsPanel.repaint();
	}

	public void resetSystemTimer() {
		systemTimer.restart();
		secondsRunning = 0;
	}

	public void setSimulationSpeed(double x) {
		simulationSpeed = x;
	}
}
