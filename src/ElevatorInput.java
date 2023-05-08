import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ElevatorInput extends JPanel implements ActionListener{
	
	//first array is motor type, second is voltage value, third is actual values (0 is rpm, 1 is stall torque, 3 is stall current
	private static final double[][][] motorValues = {{{2110, 2.15, 39},
													  {3160, 2.84, 80},
													  {4170, 3.41, 126},
													  {5180, 3.87, 185},
													  {6380, 4.69, 257}}};

	
	private JTextField totalGearRatioInput, pulleyDiameterInput, angleOfElevatorInput, lengthOfElevatorInput, weightCarriageInput, weightStageInput, efficiencyInput;
	
	private JSlider voltageSlider, simulationSpeedSlider;
	
	private Elevator elevator;
	private ElevatorSimulatorEventCaller eventRunner;
	
	
	public ElevatorInput(Elevator elevator, ElevatorSimulatorEventCaller eventRunner){
		this.elevator = elevator;
		this.eventRunner = eventRunner;
		
		this.setLayout(new GridLayout (9,1));
		
		totalGearRatioInput = new JTextField("12", 10);
		totalGearRatioInput.setBackground(Color.WHITE);
		
		pulleyDiameterInput = new JTextField("1.5", 10);
		pulleyDiameterInput.setBackground(Color.WHITE);
		
		angleOfElevatorInput = new JTextField("50", 10);
		angleOfElevatorInput.setBackground(Color.WHITE);
		
		lengthOfElevatorInput = new JTextField("15", 10);
		lengthOfElevatorInput.setBackground(Color.WHITE);
		
		weightCarriageInput = new JTextField("12", 10);
		weightCarriageInput.setBackground(Color.WHITE);
		
		weightStageInput = new JTextField("5", 10);
		weightStageInput.setBackground(Color.WHITE);
		
		efficiencyInput = new JTextField("0.64", 10);
		efficiencyInput.setBackground(Color.WHITE);
		
		JPanel totalGearRatioPanel = new JPanel();
		totalGearRatioPanel.add( new JLabel("Total Gear Ratio: "));
		totalGearRatioPanel.add(totalGearRatioInput);
		
		
		JPanel pulleyDiameterPanel = new JPanel();
		pulleyDiameterPanel.add( new JLabel("Pulley Diameter: "));
		pulleyDiameterPanel.add(pulleyDiameterInput);
		
		JPanel angleOfElevatorPanel = new JPanel();
		angleOfElevatorPanel.add( new JLabel("Angle Of Elevator: "));
		angleOfElevatorPanel.add(angleOfElevatorInput);
		
		JPanel lengthOfElevatorPanel = new JPanel();
		lengthOfElevatorPanel.add( new JLabel("Length Of Elevator: "));
		lengthOfElevatorPanel.add(lengthOfElevatorInput);
		
		
		JPanel weightCarriagePanel = new JPanel();
		weightCarriagePanel.add( new JLabel("Carriage Weight: "));
		weightCarriagePanel.add(weightCarriageInput);
		
		JPanel weightStagePanel = new JPanel();
		weightStagePanel.add( new JLabel("1st Stage Weight: "));
		weightStagePanel.add(weightStageInput);
		
		JPanel efficiencyPanel = new JPanel();
		efficiencyPanel.add( new JLabel("Efficiency: "));
		efficiencyPanel.add(efficiencyInput);
		
		voltageSlider = new JSlider(4, 12, 8);
		voltageSlider.setMajorTickSpacing(4);
		voltageSlider.setMinorTickSpacing(2);
		voltageSlider.setPaintTicks(true);
		voltageSlider.setPaintLabels(true);
		voltageSlider.setSnapToTicks(true);
		
		JPanel voltageInputPanel = new JPanel();
		
		voltageInputPanel.add(new JLabel("Voltage:"));
		voltageInputPanel.add(voltageSlider);
		
		simulationSpeedSlider = new JSlider(0, 100, 100);
		simulationSpeedSlider.setMajorTickSpacing(50);
		simulationSpeedSlider.setMinorTickSpacing(25);
		simulationSpeedSlider.setPaintTicks(true);
		simulationSpeedSlider.setPaintLabels(true);
		simulationSpeedSlider.setSnapToTicks(false);
		
		JPanel simulationSpeedInputPanel = new JPanel();
		
		simulationSpeedInputPanel.add(new JLabel("Smilation Speed %:"));
		simulationSpeedInputPanel.add(simulationSpeedSlider);
		
		
		this.add(voltageInputPanel);
		this.add(totalGearRatioPanel);
		this.add(pulleyDiameterPanel);
		this.add(angleOfElevatorPanel);
		this.add(lengthOfElevatorPanel);
		this.add(weightCarriagePanel);
		this.add(weightStagePanel);
		this.add(efficiencyPanel);
		
		JButton setValues = new JButton("Set Values");
        setValues.addActionListener(this);
        this.add(setValues);
        
        JButton resetAnim = new JButton("Play Animation");
        resetAnim.addActionListener(this);
        this.add(resetAnim);
        

		this.add(simulationSpeedInputPanel);
        
        elevator.updateValues();
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		String input;
		int voltageNum;
		
		try {
			input = totalGearRatioInput.getText();
            Elevator.totalGearRatio = Double.parseDouble(input);
        }
        catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Total gear ratio is invalid.");
            return;
        }
		
		try {
			input = pulleyDiameterInput.getText();
			Elevator.pulleyDiameter = Double.parseDouble(input) * 0.0254; //converts from in to m
        }
        catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Pulley diameter is invalid.");
            return;
        }
		
		try {
			input = angleOfElevatorInput.getText();
			Elevator.angleOfElevator = Math.toRadians(Double.parseDouble(input)); //converts from degrees to radians
        }
        catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Angle of elevator is invalid.");
            return;
        }
		try {
			input = lengthOfElevatorInput.getText();
			Elevator.lengthOfElevator = Double.parseDouble(input)* 0.0254; //converts from in to m
        }
        catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Length of elevator is invalid.");
            return;
        }
		
		try {
			input = weightCarriageInput.getText();
			Elevator.weightCarriage = Double.parseDouble(input) * 4.4482216153; //converts from lbf to N
        }
		catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Carriage Weight is invalid.");
            return;
        }
		
		try {
			input = weightStageInput.getText();
			Elevator.weightStage = Double.parseDouble(input) * 4.4482216153; //converts from lbf to N
        }
		catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "1st Stage Weight is invalid.");
            return;
        }
		
		try {
			input = efficiencyInput.getText();
			Elevator.efficiency = Double.parseDouble(input);
        }
		catch (NumberFormatException e) {
                // The string xStr is not a legal number.
        	JOptionPane.showMessageDialog(this, "Efficiency is invalid.");
            return;
        }
		
		voltageNum = (voltageSlider.getValue())/2 - 2;
		
		Elevator.rotPerMin = motorValues[0][voltageNum][0];
		Elevator.stallTorque = motorValues[0][voltageNum][1];
		Elevator.stallCurrent = motorValues[0][voltageNum][2];
		
		elevator.updateValues();
		eventRunner.setSimulationSpeed(simulationSpeedSlider.getValue());
		
		if(evt.getActionCommand() == "Play Animation") {
			elevator.resetAnim();
			eventRunner.resetSystemTimer();
			elevator.startSimulation();
		}
	}
	
	
}