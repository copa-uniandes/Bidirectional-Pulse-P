package threads;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import dataStructures.DataHandler;
import dataStructures.PendingPulse;
import dataStructures.PulseGraph;
import dataStructures.Settings;

public class pulseTask2 implements Runnable{

	/**
	 * The pulse type
	 */
	private int type;
	
	
	/**
	 * The pulse graph
	 */
	private PulseGraph network;
	
	/**
	 * The instance
	 */
	
	private Settings instance;
	
	/**
	 * Threads list
	 */
	
	private ArrayList<pulseTask2> threads;
	
	/**
	 * This a flag to stop the task
	 */
	private AtomicBoolean running = new AtomicBoolean(false);
	
	private int source;
	
	private int sink;
	/**
	 * The main method
	 * @param ty Pulse type: 1 forward; 2 backward
	 */
	public pulseTask2(int ty, PulseGraph graph, ArrayList<pulseTask2> ths, int s, int t){
		type = ty;
		network = graph;
		threads = ths;
		source = s;
		sink = t;
	}
	
	/**
	 * This is the main method.
	 * 
	 * Runs the pulse algorithm
	 */
	public void run(){
		
		//System.out.println(PulseGraph.TimeC/3 + " - "+2*PulseGraph.TimeC/2);
		 running.set(true);
	        while (running.get()) {
	        	if(type == 1){
	        		
	        		//Initial pulse weights:
        			
	        			int[] pulseWeights = new int[2];
	        			pulseWeights[0] = 0;
	        			pulseWeights[1] = 0;
	        			
	        		//Sends the initial pulse:
		        		
	        			network.getVertexByID(source-1).pulseFWithQueues(pulseWeights, 0,0);
	        			
	        			
	        		//When the first pulse is stopped the queue is full:
	        				
	        			int pendingPulses = DataHandler.pendingQueueF.size();
	        		
	        		//While the queue has at least one element, the search must go on!
	        			
	        			
		        		while(pendingPulses > 0) {
		        			
		        			//Test:
		        			/**
		        			System.out.println("-----------");
		        			for(int i=0;i<DataHandler.pendingQueueF.size();i++) {
		        				PendingPulse p = DataHandler.pendingQueueF.get(i);
		        				System.out.println("F "+p.getNodeID()+" - "+p.getDist() + " - "+p.getTime() + " - "+(PulseGraph.getVertexByID(p.getNodeID()).getMinDist() +p.getDist()));
		        			}	
		        			System.out.println("-----------");
		        			*/
		        			
		        			//Recovers the last pulse (and removes it):
		        				
		        				PendingPulse p = DataHandler.pendingQueueF.remove(pendingPulses-1);
		        				p.setNotTreated(false);
		        				
		        			//The pendingPulse weights:
		        				
		        				pulseWeights[0] = p.getTime();
		        				pulseWeights[1] = p.getDist();
		        				
		        			 //Begins the search:
		        				if(PulseGraph.getVertexByID(p.getNodeID()).getMinDist() + pulseWeights[1] < PulseGraph.PrimalBound && p.getTime() <= 2*PulseGraph.TimeC/3) {
		        					//System.out.println(p.getDist() + " - "+p.getTime());
		        					network.getVertexByID(p.getNodeID()).pulseFWithQueues(pulseWeights, 0,p.getPredId());
		        				}
		        			//Updates the global queue size (How many are left)
			        		
		        				pendingPulses = DataHandler.pendingQueueF.size();
		        		}
		        	
		        	//Final info: Who ended first, stops the other pulse:
		        	System.out.println("TERMINO F");
	        		//network.termine = true;
	        		network.setFirst(1);
	        		this.interrupt();
	        	}
	        	if(type == 2){
	        	
	        		//Initial pulse weights:
	        				
		        		int[] pulseWeights = new int[2];
		        		pulseWeights[0] = 0;
		        		pulseWeights[1] = 0;
		        		
		        	//Sends the initial pulse:
		        		
		        		network.getVertexByID(sink-1).pulseBWithQueues(pulseWeights, 0,sink-1);
		        
		        		
		        	//When the first pulse is stopped the queue is full:
		        		
		        		int pendingPulses = DataHandler.pendingQueueB.size();
	        		
		        	//While the queue has at least one element, the search must go on!
			        	
		        		while(pendingPulses > 0) {
		        			
		        			//Test:
		        			/**
		        			if(!network.termine) {
			        			System.out.println("-----------");
			        			for(int i=0;i<DataHandler.pendingQueueB.size();i++) {
			        				PendingPulse p = DataHandler.pendingQueueB.get(i);
			        				System.out.println("B "+p.getNodeID()+" - "+p.getDist() + " - "+p.getTime() + " - "+(PulseGraph.getVertexByID(p.getNodeID()).getMinDistB() +p.getDist()));
			        			}	
			        			System.out.println("-----------");
		        			}
		        			*/
		        			//Recovers the last pulse (and removes it):
		   
			        			PendingPulse p = DataHandler.pendingQueueB.remove(pendingPulses-1);
			        			p.setNotTreated(false);
		        			
			        			
			        		//The pendingPulse weights:	
			        			
			        			pulseWeights[0] = p.getTime();
				        		pulseWeights[1] = p.getDist();
				        	
				        	//Begins the search:
				        		
				        		if(PulseGraph.getVertexByID(p.getNodeID()).getMinDistB() + pulseWeights[1] < PulseGraph.PrimalBound && p.getTime() <= PulseGraph.TimeC/3) {
				        			network.getVertexByID(p.getNodeID()).pulseBWithQueues(pulseWeights, 0,p.getPredId());
				        		}
				        		
				        	//Updates the global queue size (How many are left)	
				        
				        		pendingPulses = DataHandler.pendingQueueB.size();
		        		}
		        	//Final info: Who ended first, stops the other pulse:	
		        	System.out.println("TERMINO B");
	        		//network.termine = true;
	        		network.setFirst(2);
	        		this.interrupt();
	        	}
	        }
		
	}
	
	/**
	 * This method interrupts a thread
	 */
	public void interrupt() {
        running.set(false);
       // System.out.println("Entre por aca");
    }
	
	/**
	 * This method checks if the thread is active
	 * @return true if the thread is active
	 */
    boolean isRunning() {
        return running.get();
    }
 
  
	
}
