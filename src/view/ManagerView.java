package view;

import dataStructures.PulseGraph;
import model.PulseAlgorithm;

public class ManagerView {

	/**
	 * Constant to manage the number of data shown  in console
	 */
	public static final int N = 20;

	/**
	 * Manager of the view
	 */
	public ManagerView() {

	}

	/**
	 * Method to print in console a message
	 * @param message to print
	 */
	public void printMessage(String mensaje) {
		System.out.println(mensaje);
	}
	
	/**
	 * Method to print Pulse Algorithm Results. 
	 * @param  pulse runned.
	 */
	public void printResults(PulseAlgorithm pulse){
		
		System.out.println("-----------Main results------------");		
		System.out.println("Instance: "+pulse.instanc);
		System.out.println("Network: "+ pulse.networkName);
		System.out.println("Destiny: "+pulse.destiny);
		System.out.println("Time limit: "+PulseGraph.TimeC);
		System.out.println("Time star: "+PulseGraph.TimeStar);
		System.out.println("Initial Primal Bound: "+ pulse.InitialPrimalBound);
		System.out.println("Final Primal Bound: "+PulseGraph.PrimalBound);
		System.out.println("Computational time:"+ pulse.computationalTime);
		System.out.println("Final path:"+PulseAlgorithm.recoverThePath(pulse.network));
		if(PulseGraph.terminePrimero == 0) {
			System.out.println("The initialization step is enough");
		}
		else if(PulseGraph.terminePrimero == 1) {
			System.out.println("Ended first: Forward direction");
		}else {
			System.out.println("Ended first: Backward direction");
		}
		System.out.println("------------------------------------");	
		
		
		
		
		
	}
	
}
