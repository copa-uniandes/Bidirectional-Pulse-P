package view;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

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
		System.out.println("Time limit: "+pulse.network.TimeC);
		System.out.println("Time star: "+pulse.network.TimeStar);
		System.out.println("Initial Primal Bound: "+ pulse.InitialPrimalBound);
		System.out.println("Final Primal Bound: "+pulse.network.PrimalBound);
		System.out.println("Final path:"+pulse.recoverThePath(pulse.network));
		if(pulse.network.terminePrimero == 0) {
			System.out.println("The initialization step is enough");
		}
		else if(pulse.network.terminePrimero == 1) {
			System.out.println("Ended first: Forward direction");
		}else {
			System.out.println("Ended first: Backward direction");
		}
		
		System.out.println("------------------------------------");
		
		try {
			PrintWriter pw = new PrintWriter(new File("./results/Pulse/res"+pulse.instanc+".txt"));
			pw.println("Instance:"+pulse.instanc);
			pw.println("Network:"+ pulse.networkName);
			pw.println("Destiny:"+pulse.destiny);
			pw.println("Time limit:"+pulse.network.TimeC);
			pw.println("Time star:"+pulse.network.TimeStar);
			pw.println("Initial Primal Bound:"+ pulse.InitialPrimalBound);
			pw.println("Final Primal Bound:"+pulse.network.PrimalBound);
			pw.println("Computational time:"+ pulse.computationalTime);
			pw.println("Ended first:"+pulse.network.terminePrimero);
			pw.println("Final path:"+pulse.recoverThePath(pulse.network));
			pw.close();
		}
		catch(Exception e) {
			System.out.println("Error al puro final imprimiendo");
		}
		
		
		
		
	}
	
}
