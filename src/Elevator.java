import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Elevator {

	public static double totalGearRatio = 12;
	public static double pulleyDiameter = 0; //meters
	public static double angleOfElevator = Math.toRadians(50); //degrees
	public static double lengthOfElevator = 0; //meters

	public static double weightCarriage = 0;	//Newtons
	public static double weightStage = 0;	//Newtons

	public static double efficiency = 0;

	public static double rotPerMin = 0;
	public static double stallTorque = 0;
	public static double stallCurrent = 0;

	private static final double numMotors = 2;	
	private static final double robotLength = 27;
	private static final double robotHeight = 5.5;

	private static final int scalar = 15;

	private double distanceTraveled = 0;
	private double velocity = 0;
	private double acceleration = 0;

	private double distanceTraveledInches = 0;

	public String calcData, physicsValues;

	private double speedHalfway = 0;
	private double  accelerationHalf = 0;
	private double torqueHalf = 0;

	private double timePassed = 0;
	private double halfWayTime = 0;
	private double freeSpeedTime = 0;
	private double timeInFreeSpeed = 0;
	private double timeStartDeccel = 0;

	private boolean freeSpeed = false;
	private boolean accelerating = true;
	private boolean deccelerating = false;
	private boolean halfWay = false;

	private boolean simulationIsRunning;

	private ArrayList<Double> speed = new ArrayList<Double>();
	private ArrayList<Double> time = new ArrayList<Double>();
	private ArrayList<Double> accel = new ArrayList<Double>();
	private ArrayList<Double> position = new ArrayList<Double>();


	private double distanceTraveledAccel = 0;
	private double distanceTraveledDeccel = 0;
	private double tempDistanceDeccelStart = 0;


	private int numTurnsAccel;
	private int numTurnsFree;
	
	private int deccelIterator = 0;

	public Elevator() {


		calcData = "gear ratio: " + totalGearRatio + 
				"\n pulleyDiameter: " + pulleyDiameter +
				"\n angleOfElevator: " + angleOfElevator +
				"\n lengthOfElevator: " + lengthOfElevator +
				"\n weightCarriage: " + weightCarriage +
				"\n weightStage: " + weightStage +
				"\n efficiency: " + efficiency +
				"\n rotPerMin: " + rotPerMin +
				"\n stallTorque: " + stallTorque +
				"\n stallCurrent: " + stallCurrent;

		physicsValues = "distance traveled: " + 
				"\n velocity: " +
				"\n acceleration: " + 
				"\n torque: "+ calcAcceleration() +
				"\n rpm: "+ (velocity*60/(pulleyDiameter*Math.PI));
	}

	public void updateValues() {
		calcData = "gear ratio: " + totalGearRatio + 
				"\n pulleyDiameter: " + pulleyDiameter +
				"\n angleOfElevator: " + angleOfElevator +
				"\n lengthOfElevator: " + meterToInches(lengthOfElevator) +
				"\n weightCarriage: " + weightCarriage +
				"\n weightStage: " + weightStage +
				"\n efficiency: " + efficiency +
				"\n rotPerMin: " + rotPerMin +
				"\n stallTorque: " + stallTorque +
				"\n stallCurrent: " + stallCurrent +
				"\n no load free speed: " + meterToInches(noLoadFreeSpeed()) +
				"\n 1-percentStall: " + (1-percentStall()) +
				"\n loaded free speed: " + meterToInches(loadedFreeSpeed());

	}

	public void updatePhysics(double dt) {
		if(deccelerating) {
//			System.out.println(velocity);
//			System.out.println("deccel: " + acceleration);
		}

		distanceTraveled += efficiency * velocity * dt + 1/2*acceleration*Math.pow(dt, 2);
		velocity += acceleration *  dt;
		if(simulationIsRunning) {
			timePassed +=dt;
		}

		if(distanceTraveled < lengthOfElevator/2) {
			halfWay = false;
			deccelerating = false;

			if(Math.abs(acceleration) <= 0.000001 && velocity > 0.000001) {
				if(!freeSpeed) {
					freeSpeedTime = timePassed;
					accelerating = false;
//					System.out.println("free speed (s): " +freeSpeedTime);
					distanceTraveledAccel = distanceTraveled;
					numTurnsAccel = time.size();
				}
				freeSpeed = true;
			}
			else{
				freeSpeed = false;
				accelerating = true;
			}




		}
		else {
			if(!halfWay) {
				halfWayTime = timePassed;
				timeInFreeSpeed = halfWayTime - freeSpeedTime;
				if(!freeSpeed) {
					numTurnsAccel = time.size();
					System.out.println("ac: " + numTurnsAccel);
				}

//				System.out.println("halfway (s): " +halfWayTime);
			}
			halfWay = true;
			accelerating = false;

			if(freeSpeed && timePassed < halfWayTime + timeInFreeSpeed) {
				deccelerating = false;
				freeSpeed = true;
			}
			else if (timePassed <= 2*halfWayTime){
				if(!deccelerating) {
					timeStartDeccel = timePassed;
//					System.out.println("deccelerating start (s): " +timeStartDeccel);
					tempDistanceDeccelStart = distanceTraveled;
					numTurnsFree = time.size();
				}
				deccelerating = true;
				freeSpeed = false;
			}
			else {
				deccelerating = false;
				freeSpeed = false;
//				System.out.println("end time (s): " + timePassed);
				distanceTraveledDeccel = distanceTraveled-tempDistanceDeccelStart;

//				System.out.println("time during accel: " + freeSpeedTime);
//				System.out.println("distance during deccel: " + (timePassed-timeStartDeccel));
//
//
//				System.out.println("distance during accel: " + distanceTraveledAccel);
//				System.out.println("distance during deccel: " + distanceTraveledDeccel);
//
//				System.out.println("turns accel: " + numTurnsAccel);
//				System.out.println("turns deccel: " + (time.size()-numTurnsFree));

				stopSimulation();
			}

		}

		if(accelerating) {
			acceleration = calcAcceleration();
//			System.out.println("accel: " + acceleration);
//			System.out.println(velocity);
		}
		else if(deccelerating) {

			deccelIterator++;

			if(deccelIterator <= numTurnsAccel) {
				acceleration = -accel.get(numTurnsAccel-deccelIterator);
			}
			else {
				acceleration = -accel.get(0);
			}
			//			System.out.println(velocity);
			//			System.out.println("deccel: " + acceleration);
		}
		else {
			acceleration = 0;
		}




		//		if(distanceTraveled < lengthOfElevator/2 ) {
		//			if(Math.abs(acceleration) <= 0.000001 && velocity > 0.000001) {
		//				if(!freeSpeed) {
		//					freeSpeedTime = timePassed;
		//				}
		//				acceleration = 0;
		//				freeSpeed = true;
		//			}
		//			else {
		//				acceleration = calcAcceleration();
		//				freeSpeed = false;
		//			}
		//		}
		//		else if (velocity > 0 ) {
		//			if(!halfWay) {
		//				accelerationHalf = calcAcceleration();
		//				torqueHalf = calcTorque();
		//				speedHalfway = velocity;
		//
		//				halfWayTime = timePassed;
		//			}
		//
		//			halfWay = true;
		//			if(timePassed >= 2*halfWayTime - freeSpeedTime) {
		//				acceleration = calcDecceleration();
		//				freeSpeed = false;
		//			}
		//		}
		//		else {
		//			acceleration = 0;
		//			if(!freeSpeed) {
		//				velocity = 0;
		//				stopSimulation();
		//			}
		//		}
		//System.out.println("velocity: " + velocity + "\nsecond cond: " + (timePassed >= 2*halfWayTime - freeSpeedTime));

		//		if (distanceTraveled >= lengthOfElevator) {
		//			if(Math.abs(meterToInches(velocity)) <= 2) {
		//				velocity = 0;
		//			}
		//		}


		position.add(distanceTraveled);
		speed.add(velocity);
		accel.add(acceleration);
		time.add(timePassed);
		//		System.out.println(velocity);
		//		System.out.println("net torque:" + (calcTorque() - (forceOnMotor()*pulleyDiameter/2)));
		//		
		//		System.out.println("Supposed max: " + loadedFreeSpeed());

		if(acceleration > 0) {
			physicsValues = "distance traveled: " + meterToInches(distanceTraveled) + 
					"\n velocity: " + meterToInches(velocity) +
					"\n acceleration: " + meterToInches(acceleration) + 
					"\n torque: "+ calcTorque() +
					"\n rpm: "+ (velocity*60/(pulleyDiameter*Math.PI)) +
					"\n accel half way: "+ meterToInches(accelerationHalf) +
					"\n torque half way: "+ torqueHalf +
					"\n half way time : "+ halfWayTime +
					"\n free speed time : "+ freeSpeedTime +
					"\n time passed: "+ timePassed +
					"\n freespeed?: "+ freeSpeed;

		}
		else {
			physicsValues = "distance traveled: " + meterToInches(distanceTraveled) + 
					"\n velocity: " + meterToInches(efficiency * velocity) +
					"\n acceleration: " + meterToInches(acceleration) + 
					"\n torque: "+ calcFakeTorque() +
					"\n rpm: "+ (velocity*60/(pulleyDiameter*Math.PI)) +
					"\n accel half way: "+ meterToInches(accelerationHalf) +
					"\n torque half way: "+ torqueHalf +
					"\n half way time : "+ halfWayTime +
					"\n free speed time : "+ freeSpeedTime +
					"\n time passed: "+ timePassed +
					"\n freespeed?: "+ freeSpeed;
		}


	}

	public void draw(Graphics g) {	
		distanceTraveledInches = meterToInches(distanceTraveled);

		g.fillRect(200, (int)(600 - (scalar*robotHeight)), (int)(scalar*robotLength), (int)(scalar*robotHeight));

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform old = g2d.getTransform();
		g2d.translate(200, (int)(600 - (scalar*robotHeight)));
		g2d.rotate(-angleOfElevator- Math.PI/2);
		//draw shape/image (will be rotated)

		g2d.setColor(new Color(245, 238, 218));
		g2d.fillRect(-scalar, 0, 2*scalar, 23*scalar);

		//first stage
		if(acceleration > 0) {
			g2d.setColor(Color.GREEN);
		}
		else if (acceleration == 0){
			g2d.setColor(new Color(227, 214, 177));
		}
		else {
			g2d.setColor(Color.RED);
		}
		g2d.fillRect(-10, (int)(scalar + scalar*(distanceTraveledInches)), 20, 23*scalar);
		//        System.out.println((int)(scalar + scalar*(distanceTraveledInches)) + "\n" + distanceTraveled);

		//carriage
		g2d.setColor(new Color(196, 181, 135));
		g2d.fillRect(-5, (int)(2*scalar + scalar*(2*distanceTraveledInches)), 10, 6*scalar);

		g2d.setColor(Color.MAGENTA);
		g2d.drawRect(-10, scalar + (int)(meterToInches(lengthOfElevator)/2*scalar), 20, 1);
		g2d.drawRect(-10, scalar + (int)(meterToInches(lengthOfElevator)*scalar), 20, 1);

		g2d.setTransform(old);


		g.setColor(Color.black);

		int y = 10;
		for (String line : calcData.split("\n")) {
			g.drawString(line, 10, y += g.getFontMetrics().getHeight());
		}

		y = 10;
		for (String line : physicsValues.split("\n")) {
			g.drawString(line, 500, y += g.getFontMetrics().getHeight());
		}	

		g.setColor(Color.BLACK);
		g.drawLine(100, 750, 1000, 750);
		if(halfWayTime != 0) {
			g.drawLine((int)(halfWayTime*1000/2) + 100, 650, (int)(halfWayTime*1000/2) + 100, 850);
		}

		printGraph(position, 100/lengthOfElevator, g, Color.WHITE);
		printGraph(speed, 100*efficiency/loadedFreeSpeed(), g, Color.YELLOW);
		printGraph(accel, 100/((stallTorque*totalGearRatio/(pulleyDiameter/2) - forceOnMotor())/calcMass()), g, Color.ORANGE);
	}

	public double calcAcceleration() {

		return (calcTorque()/(pulleyDiameter/2) - forceOnMotor())/calcMass();
	}

	public double calcTorque() {
		return (-stallTorque/rotPerMin*(velocity*totalGearRatio*60/(pulleyDiameter*Math.PI)) + stallTorque)*totalGearRatio;
	}


	public double calcDecceleration(){
		//	return (calcFakeTorque()/(pulleyDiameter/2) + forceOnMotor())/calcMass();
		return -calcAcceleration();
	}

	//this is not actually the torque applied when deccelerating, but it is a mirror of the accelerating toruqe since we know that we will decelerate faster than accelerate
	public double calcFakeTorque() {
		//return -stallTorque/rotPerMin*((velocity-speedHalfway)*60/(pulleyDiameter*Math.PI)) - stallTorque;
		return -(-stallTorque/rotPerMin*(velocity*totalGearRatio*60/(pulleyDiameter*Math.PI)) + stallTorque)*totalGearRatio;
	}

	public double calcMass() {
		return weightToMass((2 * weightCarriage) + weightStage);
	}

	public void resetAnim() {
		distanceTraveled = 0;
		velocity = 0;
		acceleration = 0;
		timePassed = 0;
		halfWayTime = 0;
		freeSpeedTime = 0;
		freeSpeed = false;
		halfWay = false;

		position.clear();
		speed.clear();
		accel.clear();
		time.clear();
		deccelIterator = 0;

	}

	//meters per second of one stage with no load or efficiency considered
	public double noLoadFreeSpeed() {
		return rotPerMin / totalGearRatio * pulleyDiameter * Math.PI / 60;
	}

	//the linear force the motor outputs at stall in Newtons
	public double newStallTorque() {
		return stallTorque * totalGearRatio * numMotors/(0.5 * pulleyDiameter);
	}

	//newtons
	public double forceOnMotor() {
		return ((2 * weightCarriage) + weightStage)*Math.sin(angleOfElevator);
	}

	//percent of stall torque required to counter elevator weight
	public double percentStall() {
		return forceOnMotor()/newStallTorque();
	}

	//meters per second
	public double loadedFreeSpeed() {
		return noLoadFreeSpeed()*(1-percentStall())*efficiency;
	}

	public double meterToInches(double x) {
		return x * 39.37;
	}
	public double inchToMeter(double x) {
		return x * 0.0254;
	}
	//N to kg
	public double weightToMass(double x) {
		return x /9.807;
	}

	public void startSimulation() {
		simulationIsRunning = true;
	}
	public void stopSimulation() {
		simulationIsRunning = false;
	}

	public boolean isSimulationRunning() {
		return simulationIsRunning;
	}
	public void printGraph(ArrayList<Double> dependent, double scale, Graphics g, Color col) {
		g.setColor(col);
		for(int i = 0; i < time.size()-1; i++) {
			g.drawLine((int)(time.get(i)*1000/2) + 100, (750) - (int)(dependent.get(i)*scale),(int)(time.get(i+1)*1000/2) + 100, (750) - (int)(dependent.get(i+1)*scale));
		}
	}
}


