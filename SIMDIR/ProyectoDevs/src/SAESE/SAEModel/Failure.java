/*
*  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
*  Version    : DEVSJAVA 2.7 
*  Date       : 08-15-02 
*/
package SAESE.SAEModel;


import GenCol.*;


public class Failure extends entity{
	
	private int failureId;
	
	//Generator of random numbers
	private double downtime;
	
	//Constructors
	//by default:
	public Failure(){
		this("failure",0, 10);
	}
	
	//With parameters:
	public Failure(String name,int failureId, double downtime){
	   super(name);
	   this.setFailureId(failureId);
	   this.setDowntime(downtime);
	}

	//Accessors
	public int getFailureId() {
		return failureId;
	}
	
	public void setFailureId(int failureId) {
		this.failureId = failureId;
	}
	
	public double getDowntime() {
		return downtime;
	}

	public void setDowntime(double downtime) {
		this.downtime = downtime;
	}
	
	//Equal operator for the instances of this class
	/** equal operator: 
	 * compares if an entity of this class is equal than this,
	 * having as reference the identifier.
	 * 
	 * @param entityToCmp
	 * @return boolean
	 */
	public boolean equal( entity  entityToCmp){
		Failure failureEntity = (Failure)entityToCmp;
		return this.getFailureId() == failureEntity.getFailureId();
	}

	/** greaterThan operator: 
	 * compares if an entity of this class is greater than this,
	 * having as reference the identifier.
	 * 
	 * @param entityToCmp
	 * @return boolean
	 */
	public boolean greaterThan( entity  entityToCmp){
		Failure failureEntity = (Failure)entityToCmp;
		return this.getFailureId() < failureEntity.getFailureId();
	}

	/** print function: 
	 * shows the name and id of this entity
	 *
	 */
	public void print(){
	   System.out.println("Failure: "+ this.getName() +
	        " id: " + this.getFailureId());
	}

}