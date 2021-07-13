package model;

import java.io.IOException;

public class Manager {

	public Manager() throws IOException, InterruptedException{

	}
	
	public PulseAlgorithm runBidirectionalPulse(int depth,int instance, String netPath)throws IOException, InterruptedException {
		PulseAlgorithm pulso = new PulseAlgorithm();
		pulso.bidirectionalPulse(depth,instance,netPath);
		return pulso;
		
	}
	
	public PulseAlgorithm runBidirectionalPulsePerimeter(int depth,int instance,String netPath)throws IOException, InterruptedException {
		PulseAlgorithm pulso = new PulseAlgorithm();
		pulso.bidirectionalPulsePerimeter(depth,instance,netPath);
		return pulso;
		
	}
}
