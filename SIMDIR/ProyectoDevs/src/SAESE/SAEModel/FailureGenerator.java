/*     
 *    
 *  Author     : 
 *  Version    : DEVSJAVA 2.7, ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Date       : 01-09-2011
 */
package SAESE.SAEModel;

import GenCol.*;
import model.modeling.*;
import util.rand;
import view.modeling.ViewableAtomic;

public class FailureGenerator extends ViewableAtomic{
	private double refFailsTimeInterval; //reference value: mean in exponential distribution or upper limit in uniform distribution
	private double refDowntime; 
	private int count; //fault number
	
	//initialize the generators (given seed)
	private rand rndDowntimeGen; //time that responsibility is no available due to the fail sent from here
	private rand rndIntTimeGen;  //random interval of time generator (between faults)
	
	
	//Constructors:
	
	//By default
	public FailureGenerator() {this("FailureGenerator", 1000,10);}
	
	//With parameters
	public FailureGenerator(String name,double intMeanTimeBF, double intDowntimeMean){
		super(name);
		
		//Defining ports
		//Input ports:
		addInport("rstateip");
		
		//Output ports:
		addOutport("failop");
		
		//testing input ports
		addTestInput("rstateip",new entity("activated"));
		addTestInput("rstateip",new entity("finished"));
		
		//Initializing the period of time (exponential: mean time between faults, other distribution: needed parameter)	
		this.refFailsTimeInterval = intMeanTimeBF;
		this.refDowntime=intDowntimeMean;
		
		//defining an initial time for the next fault (Creates a new random number generator using a single long seed)
		
		rndIntTimeGen = new rand(65);
		rndDowntimeGen = new rand(98);
		
		//initializing variables of the model 
		initialize();
	}
	
	public void initialize(){
		//initializing the state
		passivateIn("inactive");

		//initializing the counter for the generated faults
		count = 0;
		
		//initializing by default other variables of this model (atomic)
		super.initialize();
	}
	
	public void  deltext(double e,message x){
		//computing the time of the simulation
		Continue(e);
		
		//Phase= inactive
		if(phaseIs("inactive")){
			for (int i=0; i< x.getLength();i++) {
				//reading value from input port
				if (messageOnPort(x,"rstateip",i)){
					//taking event from the port
					entity rstateEvent = x.getValOnPort("rstateip",i);
					if (rstateEvent.getName().equals("activated")){
						//changing the state to active and assign the sigma value (corresponding to the time to the next fault)
						holdIn("active",rndIntTimeGen.expon(this.refFailsTimeInterval));
					}
				}
			}
		}
		/*else if (phaseIs("active")){
			for (int i=0; i< x.getLength();i++){
				//reading value from input port
				if (messageOnPort(x,"rstateip",i)){
					//taking event from the port
					entity rstateEvent = x.getValOnPort("rstateip",i);
					if (rstateEvent.getName().equals("finished")){
						passivateIn("inactive");
					}
				}
			}
		}*/
	}
	
	public void  deltint( ){
		if(phaseIs("active")){
			count = count +1;
			holdIn("active",rndIntTimeGen.expon(this.refFailsTimeInterval));
		}
		
	}

	//confluence function
	public void deltcon(double e,message x)
	{
	   deltint();
	   deltext(0,x);
	}
	
	public message  out( ){
		//System.out.println(name+" out count "+count);
		message  m = new message();
		Failure fail= new Failure("failure"+count, count, this.rndDowntimeGen.uniform(0.000000000000001,this.refDowntime*2)); //Registrar en una lista las fallas: asi se puede recurer el tiempo despues
		m.add(makeContent("failop",fail));
		
		return m;
	}
	
	public void showState(){
		super.showState();
		
	}
	
	public String getTooltipText(){
		return super.getTooltipText()
		    +"\n"+" Mean time between faults: " + this.refFailsTimeInterval
		     +"\n"+" Count of faults: " + count;
	}

}

