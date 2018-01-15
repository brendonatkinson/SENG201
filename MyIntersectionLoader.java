package traffic.diy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Scanner;
import traffic.core.Intersection;
import traffic.core.Phase;
import traffic.core.TrafficStream;
import traffic.load.TrafficException;
import traffic.load.TrafficSyntaxException;
import traffic.misc.Detector;
import traffic.misc.RandomDetector;
import traffic.phaseplan.FullyActuatedPhasePlan;
import traffic.phaseplan.PhasePlan;
import traffic.phaseplan.PretimedPhasePlan;
import traffic.signal.SignalFace;
import traffic.util.State;
import traffic.util.TrafficDirection;


/**
 * Read an intersection description file and build an intersection from the data
 * it contains.
 *@author bja90 46376139
 *@since 27/05/2016
 */
public class MyIntersectionLoader {
    private BufferedReader buffReader;
    private Scanner scan;
    private String currentLine;
    private Intersection intersect;
    private List<TrafficStream> tstream;
    public static final String ACTUATED = "<Actuated>";
    public static final String PRETIMED = "<PreTimed>";
    
    public MyIntersectionLoader(BufferedReader br) {
        this.intersect = null;
        this.tstream = new ArrayList<TrafficStream>();
        this.buffReader = br;
    }
    
    /**
     * Builds the intersection.
     *
     * @return Intersection	the Intersection constructed from data file
     */
    public Intersection buildIntersection() {
    	try {
            this.currentLine = this.parseNextLine();
            if (!this.currentLine.equals(traffic.load.Tag.INTERSECTION)) {
                throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.INTERSECTION + " Received: " + this.currentLine);
            }
            this.currentLine = this.parseNextLine();
            this.scan = new Scanner(this.currentLine);
            this.scan.useDelimiter("\t");
            this.intersect = new Intersection(this.scan.next(), this.scan.next());
            this.currentLine = this.parseNextLine();
            if (!this.currentLine.equals(traffic.load.Tag.END_INTERSECTION)) 
            {
                throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.END_INTERSECTION + " Received: " + this.currentLine);
            }
            this.buildStreams();
            this.buildPP();
            this.buildSignals();
        	
        }
        catch (TrafficSyntaxException e) 
    	{
            e.printStackTrace();
            intersect = null;
        } 
    	return this.intersect;

    }
    
    /**
     * Builds the TrafficStream.
     */
    private void buildStreams() {
        try {
            this.currentLine = this.parseNextLine();
            if (!this.currentLine.equals(traffic.load.Tag.TRAFFIC_STREAMS))
            {
                throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.TRAFFIC_STREAMS + " Received: " + this.currentLine);
            }
            
            this.currentLine = this.parseNextLine();
            
            while (!this.currentLine.equals(traffic.load.Tag.END_TRAFFIC_STREAMS))
            {
            	this.scan = new Scanner(this.currentLine);
            	this.scan.useDelimiter("\t");
                this.tstream.add(new TrafficStream(this.scan.next(), this.scan.next()));
                this.currentLine = this.parseNextLine();
            }
        }
        catch (TrafficSyntaxException e) {
        	e.printStackTrace();
            intersect = null;
        }
    }
    
    /**
     * Builds the PhasePlans and adds to Intersection.
     */
    private void buildPP()
    {
    	{
    		try
    		{
    			PhasePlan plan;
    			this.currentLine = this.parseNextLine();
    			if (!this.currentLine.equals(traffic.load.Tag.PHASEPLAN)) 
    			{
    				throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.PHASEPLAN + " Received: " + this.currentLine);
    			}
    			this.currentLine = this.parseNextLine();
    			do
    			{
    				if (!this.currentLine.equals(traffic.load.Tag.PHASES)) 
    				{
    					throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.PHASES + " Received: " + this.currentLine);
    				}
    			
    				this.currentLine = this.parseNextLine();
    			
    				switch (this.currentLine)
    				{
    				case (ACTUATED):
    					//Consume token, build PhasePlan
    					this.currentLine = this.parseNextLine();
    					plan = this.buildPhases(true);
    					break;
    				case (PRETIMED):
    					//Consume token, build PhasePlan
    					this.currentLine = this.parseNextLine();
    					plan = this.buildPhases(false);
    					break;
    				default:
    					plan = this.buildPhases(false);
    					break;
    				}
    				
    			//Add to intersection
    			this.intersect.addPlan(plan);
    			this.currentLine = this.parseNextLine();
    			}
    			while (!this.currentLine.equals(traffic.load.Tag.END_PHASEPLAN));
    		}
    		catch (TrafficSyntaxException e)
    		{
    			e.printStackTrace();
    			intersect = null;
    		}
    	}
    	}

	/**
	 * Builds a fully actuated PhasePlan.
	 *
	 * @param boolean actuated, The type of actuation, true = actuated, false = timed
	 * @return PhasePlan the completed phase plan
	 */
	private PhasePlan buildPhases(Boolean actuated){
		PhasePlan plan;
		if (actuated.equals(true))
		{
			plan = new FullyActuatedPhasePlan();
		}
		else
		{
			plan = new PretimedPhasePlan();
		}
    	try {
    	while (!this.currentLine.equals(traffic.load.Tag.END_PHASES)) 
		{
    		this.scan = new Scanner(this.currentLine);
    		this.scan.useDelimiter("\t");
			Phase currPhase = new Phase(this.scan.next(), this.scan.next());
			String states = this.scan.next();
			//Need to check there is a state for each stream
			if (!(this.tstream.size() == states.length()))
			{
				throw new TrafficSyntaxException("Phase error. Not enough States");
			}
			int count = 0;
			
			//Match State to Stream
			for (TrafficStream trafstream : this.tstream)
			{
				if (actuated.equals(true))
				{
					trafstream.addDetector((Detector)new RandomDetector());
				}
				currPhase.addStream(trafstream, State.stateFor(states.charAt(count++)));
			}
			
			if (this.scan.hasNextInt()) 
			{
				currPhase.setMinGreenInterval(this.scan.nextInt());
			}
			
			plan.add(currPhase);
			this.currentLine = this.parseNextLine();
		}
    }
    	catch (TrafficSyntaxException e)
    	{
			e.printStackTrace();
			intersect = null;
			
		} 
    	catch (TrafficException e)
    	{
			e.printStackTrace();
			intersect = null;
		}
    
    	return plan;
    }
    
    /**
     * Builds the Intersection signal faces.
     */
    private void buildSignals() {
        SignalFace currFace = null;
        try {
            this.currentLine = this.parseNextLine();
            if (!this.currentLine.equals(traffic.load.Tag.SIGNAL_FACES)) {
            	 throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.SIGNAL_FACES + " Received: " + this.currentLine);
            }
            this.currentLine = this.parseNextLine();
            
            while (!this.currentLine.equals(traffic.load.Tag.END_SIGNAL_FACES)) 
            {
            	this.scan = new Scanner(this.currentLine);
                this.scan.useDelimiter("\t");
                TrafficDirection loc = TrafficDirection.directionFor(this.scan.next());
                TrafficDirection orient = TrafficDirection.directionFor(this.scan.next());
                int intkind = 0;
                switch (this.scan.next())
                {
                	case "STANDARD": 
                	{
                		intkind = SignalFace.STANDARD;
                		break;
                	}
                	case "LEFT_ARROW": 
                	{
                		intkind = SignalFace.LEFT_ARROW;
                    	break;
                	}
                	case "RIGHT_ARROW":
                	{
                		intkind = SignalFace.RIGHT_ARROW;
                		break;
                	}
                	default:
                	{
                		throw new TrafficSyntaxException("Invalid SignalFace");
                	}
                }
                
                currFace = new SignalFace(loc, orient, intkind);
                while (this.scan.hasNext()) 
                {
                	
                	//Find TrafficStream in ArrayList, add Observer
                	
                	String streamName = this.scan.next();
                	
                    for(int n = 0; n < tstream.size(); n++)
                   	    {
                        if(tstream.get(n).getname().equalsIgnoreCase(streamName))
                        { 
                   	       	 tstream.get(n).addObserver((Observer)currFace);
                    	         }
                    	    }
                }
                this.intersect.addSignalFace(currFace);
                this.currentLine = this.parseNextLine();
            }
            if (!this.currentLine.equals(traffic.load.Tag.END_SIGNAL_FACES)) {
                throw new TrafficSyntaxException("Invalid Tag. Expected: " + traffic.load.Tag.END_SIGNAL_FACES + " Received: " + this.currentLine);
            }
        
        }
        catch (TrafficSyntaxException e) {
        	e.printStackTrace();
            intersect = null;
        } catch (TrafficException e) {
			e.printStackTrace();
			intersect = null;
		}
    }
    
    /**
     * Reads the next line from the buffered reader, discarding 
     * whitespace and comments.
     *
     * @return String the parsed string
     */
    private String parseNextLine() {
        String inputLine = null;
        try {
            do {
                inputLine = this.buffReader.readLine();
                
                //Check for EOF
                if (inputLine == null) 
                {
                    return inputLine;
                }
                //Clean up whitespace
                inputLine = inputLine.trim(); 
            } while (inputLine.length() == 0 || inputLine.startsWith("//"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return inputLine;
    }
}
