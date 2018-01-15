package traffic.diy;

import java.util.Observer;

import traffic.core.Intersection;
import traffic.core.Phase;
import traffic.core.TrafficStream;
import traffic.misc.Detector;
import traffic.misc.RandomDetector;
import traffic.phaseplan.FullyActuatedPhasePlan;
import traffic.phaseplan.PhasePlan;
import traffic.phaseplan.PretimedPhasePlan;
import traffic.signal.SignalFace;
import traffic.util.State;
import traffic.util.TrafficDirection;

/**
 * "Manually" create an intersection by assembling the various elements. Use the
 * classes provided in the traffic packages to construct an intersection which
 * can be displayed in the monitor.
 *
 */
public class ModelIntersection {

	/**
	 * A demo intersection made fusing the packages provided.  It has 
	 * one or more pre-timed phase plans.
	 * @return the intersection I made.
	 */
	public static Intersection preTimedIntersection() {
		PretimedPhasePlan plan = new PretimedPhasePlan();
        Intersection intersect =  new Intersection("Papanui Road & Harewood Road", "Note Left turn onto Harewood during second phase (Pre-timed)");
       
		TrafficStream strmNSE = 		new TrafficStream("N->S|E", "North on Papanui, continuing south or turning east");
        TrafficStream strmNWArrow =		new TrafficStream("N->W", "North on Papanui, turning west");
        TrafficStream strmSNEW = 		new TrafficStream("S->N|E|W", "South on Papanui, continuing North or turning East or West");
		TrafficStream strmSWArrow = 	new TrafficStream("S->W", "South on Papanui, turning West");
		TrafficStream strmWNES = 		new TrafficStream("W->N|E|S", "West on Creyke, continuing east or turning north or south");
        
		SignalFace nw1 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.NORTH, 3);
        SignalFace sw1 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.NORTH, 3);
		SignalFace ne1 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.NORTH, 3);
        SignalFace se1 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.NORTH, 3);
        
        SignalFace nw2 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.WEST, 3);
        SignalFace sw2 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.WEST, 3);
		SignalFace ne2 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.WEST, 3);
		SignalFace se2 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.WEST, 3);
		
		SignalFace nw3 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.SOUTH, 3);
		SignalFace sw3 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.SOUTH, 3);
		SignalFace ne3 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.SOUTH, 3);
		SignalFace se3 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.SOUTH, 3);
		
        SignalFace swar = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.NORTH, 33);
        SignalFace nwar = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.NORTH, 33);
		SignalFace swar2 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.SOUTH, 23);
        SignalFace nwar2 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.SOUTH, 23);
		
		strmNSE.addObserver((Observer)nw1);
        strmNSE.addObserver((Observer)sw1);
        strmNSE.addObserver((Observer)ne1);
		strmNSE.addObserver((Observer)se1);       
		strmSNEW.addObserver((Observer)nw3);
        strmSNEW.addObserver((Observer)sw3);
        strmSNEW.addObserver((Observer)ne3);
        strmSNEW.addObserver((Observer)se3);
		strmWNES.addObserver((Observer)nw2);
        strmWNES.addObserver((Observer)sw2);
        strmWNES.addObserver((Observer)ne2);
		strmWNES.addObserver((Observer)se2);
		strmNWArrow.addObserver((Observer)swar);
		strmNWArrow.addObserver((Observer)nwar);
		strmSWArrow.addObserver((Observer)swar2);
        strmSWArrow.addObserver((Observer)nwar2);
        
		Phase p1 = new Phase("NS-NR", "Right turn prohibited");
        Phase p2 = new Phase("NS", "includes all turning streams");
        Phase p3 = new Phase("NS-Y", "Left turn arrow Green");
        Phase p4 = new Phase("ALL RED", "All red, except arrow");
        Phase p5 = new Phase("W", "includes all turning streams");
        Phase p6 = new Phase("W-Y", "includes LH arrow");
        Phase p7 = new Phase("ALL RED", "All red");
        
        p1.addStream(strmNWArrow, State.RED);
        p1.addStream(strmNSE, State.GREEN);
        p1.addStream(strmSNEW, State.GREEN);
        p1.addStream(strmSWArrow, State.OFF);
        p1.addStream(strmWNES, State.RED);
        
        p2.addStream(strmNWArrow, State.OFF);
        p2.addStream(strmNSE, State.GREEN);
        p2.addStream(strmSNEW, State.GREEN);
        p2.addStream(strmSWArrow, State.OFF);
        p2.addStream(strmWNES, State.RED);
        
        p3.addStream(strmNWArrow, State.OFF);
        p3.addStream(strmNSE, State.YELLOW);
        p3.addStream(strmSNEW, State.YELLOW);
        p3.addStream(strmSWArrow, State.GREEN);
        p3.addStream(strmWNES, State.RED);
        
        p4.addStream(strmNWArrow, State.RED);
        p4.addStream(strmNSE, State.RED);
        p4.addStream(strmSNEW, State.RED);
        p4.addStream(strmSWArrow, State.GREEN);
        p4.addStream(strmWNES, State.RED);
        
        p5.addStream(strmNWArrow, State.RED);
        p5.addStream(strmNSE, State.RED);
        p5.addStream(strmSNEW, State.RED);
        p5.addStream(strmSWArrow, State.GREEN);
        p5.addStream(strmWNES, State.GREEN);
        
        p6.addStream(strmNWArrow, State.RED);
        p6.addStream(strmNSE, State.RED);
        p6.addStream(strmSNEW, State.RED);
        p6.addStream(strmSWArrow, State.YELLOW);
        p6.addStream(strmWNES, State.YELLOW);
        
        p7.addStream(strmNWArrow, State.RED);
        p7.addStream(strmNSE, State.RED);
        p7.addStream(strmSNEW, State.RED);
        p7.addStream(strmSWArrow, State.RED);
        p7.addStream(strmWNES, State.RED);
        
        plan.add(p1);
        plan.add(p2);
        plan.add(p3);
        plan.add(p4);
        plan.add(p5);
        plan.add(p6);
        plan.add(p7);
        
        intersect.addPlan((PhasePlan)plan);
        return intersect;
	}
	
	/**
	 * A demo intersection made fusing the packages provided.  It has 
	 * one or more fully-actuated phase plans.
	 * @return the intersection I made.
	 */
	public static Intersection fullyActivatedIntersection() {
		FullyActuatedPhasePlan plan = new FullyActuatedPhasePlan();
		Intersection intersect =  new Intersection("Papanui Road & Harewood Road", "Note Left turn onto Harewood during second phase (Pre-timed)");
	    
		//Build Streams
		TrafficStream strmNSE = 		new TrafficStream("N->S|E", "North on Papanui, continuing south or turning east");
        TrafficStream strmNWArrow =		new TrafficStream("N->W", "North on Papanui, turning west");
        TrafficStream strmSNEW = 		new TrafficStream("S->N|E|W", "South on Papanui, continuing North or turning East or West");
		TrafficStream strmSWArrow = 	new TrafficStream("S->W", "South on Papanui, turning West");
		TrafficStream strmWNES = 		new TrafficStream("W->N|E|S", "West on Creyke, continuing east or turning north or south");
        
		//Build SignalFaces
		SignalFace nw1 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.NORTH, 3);
        SignalFace sw1 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.NORTH, 3);
		SignalFace ne1 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.NORTH, 3);
        SignalFace se1 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.NORTH, 3);
        
        SignalFace nw2 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.WEST, 3);
        SignalFace sw2 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.WEST, 3);
		SignalFace ne2 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.WEST, 3);
		SignalFace se2 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.WEST, 3);
		
		SignalFace nw3 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.SOUTH, 3);
		SignalFace sw3 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.SOUTH, 3);
		SignalFace ne3 = intersect.addSignalFace(TrafficDirection.NORTHEAST, TrafficDirection.SOUTH, 3);
		SignalFace se3 = intersect.addSignalFace(TrafficDirection.SOUTHEAST, TrafficDirection.SOUTH, 3);
		
        SignalFace swar = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.NORTH, 33);
        SignalFace nwar = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.NORTH, 33);
		SignalFace swar2 = intersect.addSignalFace(TrafficDirection.SOUTHWEST, TrafficDirection.SOUTH, 23);
        SignalFace nwar2 = intersect.addSignalFace(TrafficDirection.NORTHWEST, TrafficDirection.SOUTH, 23);
		
        //Add Observers
		strmNSE.addObserver((Observer)nw1);
        strmNSE.addObserver((Observer)sw1);
        strmNSE.addObserver((Observer)ne1);
		strmNSE.addObserver((Observer)se1);       
		strmSNEW.addObserver((Observer)nw3);
        strmSNEW.addObserver((Observer)sw3);
        strmSNEW.addObserver((Observer)ne3);
        strmSNEW.addObserver((Observer)se3);
		strmWNES.addObserver((Observer)nw2);
        strmWNES.addObserver((Observer)sw2);
        strmWNES.addObserver((Observer)ne2);
		strmWNES.addObserver((Observer)se2);
		strmNWArrow.addObserver((Observer)swar);
		strmNWArrow.addObserver((Observer)nwar);
		strmSWArrow.addObserver((Observer)swar2);
        strmSWArrow.addObserver((Observer)nwar2);
        
        //Add Detectors
        strmNSE.addDetector((Detector)new RandomDetector());
        strmSNEW.addDetector((Detector)new RandomDetector());
        strmWNES.addDetector((Detector)new RandomDetector());
        strmNWArrow.addDetector((Detector)new RandomDetector());
        strmSWArrow.addDetector((Detector)new RandomDetector());
        
        //Phases
		Phase p1 = new Phase("NS-NR", "Right turn prohibited");
        Phase p2 = new Phase("NS", "includes all turning streams");
        Phase p3 = new Phase("NS-Y", "Left turn arrow Green");
        Phase p4 = new Phase("ALL RED", "All red, except arrow");
        Phase p5 = new Phase("W", "includes all turning streams");
        Phase p6 = new Phase("W-Y", "includes LH arrow");
        Phase p7 = new Phase("ALL RED", "All red");
        
        p1.addStream(strmNWArrow, State.RED);
        p1.addStream(strmNSE, State.GREEN);
        p1.addStream(strmSNEW, State.GREEN);
        p1.addStream(strmSWArrow, State.OFF);
        p1.addStream(strmWNES, State.RED);
        
        p2.addStream(strmNWArrow, State.OFF);
        p2.addStream(strmNSE, State.GREEN);
        p2.addStream(strmSNEW, State.GREEN);
        p2.addStream(strmSWArrow, State.OFF);
        p2.addStream(strmWNES, State.RED);
        
        p3.addStream(strmNWArrow, State.OFF);
        p3.addStream(strmNSE, State.YELLOW);
        p3.addStream(strmSNEW, State.YELLOW);
        p3.addStream(strmSWArrow, State.GREEN);
        p3.addStream(strmWNES, State.RED);
        
        p4.addStream(strmNWArrow, State.RED);
        p4.addStream(strmNSE, State.RED);
        p4.addStream(strmSNEW, State.RED);
        p4.addStream(strmSWArrow, State.GREEN);
        p4.addStream(strmWNES, State.RED);
        
        p5.addStream(strmNWArrow, State.RED);
        p5.addStream(strmNSE, State.RED);
        p5.addStream(strmSNEW, State.RED);
        p5.addStream(strmSWArrow, State.GREEN);
        p5.addStream(strmWNES, State.GREEN);
        
        p6.addStream(strmNWArrow, State.RED);
        p6.addStream(strmNSE, State.RED);
        p6.addStream(strmSNEW, State.RED);
        p6.addStream(strmSWArrow, State.YELLOW);
        p6.addStream(strmWNES, State.YELLOW);
        
        p7.addStream(strmNWArrow, State.RED);
        p7.addStream(strmNSE, State.RED);
        p7.addStream(strmSNEW, State.RED);
        p7.addStream(strmSWArrow, State.RED);
        p7.addStream(strmWNES, State.RED);
        
        plan.add(p1);
        plan.add(p2);
        plan.add(p3);
        plan.add(p4);
        plan.add(p5);
        plan.add(p6);
        plan.add(p7);
        
        intersect.addPlan((PhasePlan)plan);
        return intersect;
	}
}
