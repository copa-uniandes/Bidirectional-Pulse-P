/**
 * This is the main class for the pulse algorithm.
 * 
 * Ref.: Lozano, L. and Medaglia, A. L. (2013). 
 * On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 * DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 * 
 * 
 * @author L. Lozano & D. Duque
 * @affiliation Universidad de los Andes - Centro para la Optimizaci�n y Probabilidad Aplicada (COPA)
 * @url http://copa.uniandes.edu.co/
 * 
 */

package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import dataStructures.DataHandler;
import dataStructures.DukqstraDist;
import dataStructures.DukqstraTime;
import dataStructures.PendingPulse;
import dataStructures.PulseGraph;
import dataStructures.VertexPulse;
import threads.ShortestPathTask;
import threads.pulseTask;
import threads.pulseTask2;



public class PulseAlgorithm {

	/**
	 * The name of the file (network)
	 */
	public String fileName;

	/**
	 * The network where the Original pulse will be running
	 */
	public PulseGraph network;

	/**
	 * Name of the network
	 */
	public String networkName;


	/**
	 * Initial Primal Bound
	 */
	public int InitialPrimalBound;


	/**
	 * Computational Time
	 */
	public double computationalTime;
	
	
	/**
	 * Instance id
	 */
	public int instanc;
	
	/**
	 * Last node id
	 */
	public int destiny;
	
	/**
	 * Initialize the attributes of the pulse
	 */
	public PulseAlgorithm(){

		fileName = "";
		network = null;
		networkName = "";
		InitialPrimalBound = 0;
		computationalTime = 0;
	}
	
	
	
	/**
	 * This method runs the pulse
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 public void bidirectionalPulse(int depth, int instance, String netPath) throws IOException, InterruptedException{


		File testFile = new File("./instances/Config"+instance+".txt");
		instanc = instance;
		
		BufferedReader bufRedr = new BufferedReader(new FileReader(testFile));
		
		String actLine = null;
		
		String [] information = new String [6];
		
		int rowA = 0;
		int colA = 0;
		
		while((actLine = bufRedr.readLine()) != null && rowA < 6){	
			String [] info = actLine.split(":");
			information[rowA] = info[1];
			rowA++;
		}

		//Modifies the instance tightness
		
		DataHandler data = new DataHandler(Integer.parseInt(information[2]),Integer.parseInt(information[1]),Integer.parseInt(information[5]),1,2,information[0]);
		PulseGraph network = null;
		destiny = Integer.parseInt(information[5]);
		data.ReadDimacs(netPath);
	
		//Backward direction network:
						
		//Creates the graph:
			
			network = createGraphB(data);
			network.SetConstraint(Integer.parseInt(information[3]));	
						
		//Finds the bounds to reach the source node:
						
			SPB(data,network);

		//Stores information for the backward pulse:
						
			int numNodesAct = network.getNumNodes();
			Integer[][] lista = new Integer[numNodesAct][4];
			for (int i = 0; i < numNodesAct; i++) {
				VertexPulse actVertex = network.getVertexes()[i];
				lista[i][0] = actVertex.getMaxDistB();
				lista[i][1] = actVertex.getMaxTimeB();
				lista[i][2] = actVertex.getMinDistB();
				lista[i][3] = actVertex.getMinTimeB();
			}
						
		//Forward direction network:
						
			//Creates the graph:
						
			network = createGraphF(data);
			network.SetConstraint(Integer.parseInt(information[3]));
			
			//Finds the bounds to reach the sink node:
				
			Double ITime2 = (double) System.nanoTime();
			SPF(data,network);
			Double FTime2 = (double) System.nanoTime();
			Double iniTime = (FTime2-ITime2)/1000000000;
					
			// Set the first primal bound
						
			int MD=network.getVertexByID(data.getLastNode()-1).getMaxDist();
			network.setDestiny(data.getSource()-1);
			network.setPrimalBound(MD);
			InitialPrimalBound = MD;
			network.setTimeStar(network.getVertexByID(data.getLastNode()-1).getMinTime());
						
		// Recovers information for the backward pulse
						
			for (int i = 0; i < numNodesAct; i++) {
				VertexPulse actVertex = network.getVertexes()[i];
				actVertex.setEveryBound(lista[i]);	
			}
						
		// This is the pulse procedure
						
				network.depth = depth; //The queue depth
						
		//Starts the clock
						
				Double ITime = (double) System.nanoTime();
				
		//Bidirectional Pulse !
						
				//Check if we already have found the optimal solution
				if(network.getVertexByID(data.getLastNode()-1).getMaxTime() <= network.TimeC) {

					//Set the primal bound and the time star
					network.setPrimalBound(network.getVertexByID(data.getLastNode()-1).getMinDist());
					network.TimeStar = network.getVertexByID(data.getLastNode()-1).getMaxTime();

				}else {
					runPulses(data,network);
				}
							
		//Ends the clock
							
				Double FTime = (double) System.nanoTime();
				

		/*******************************************************************
		 ************************ RESULTS ******************************
		 *******************************************************************
		 */

		networkName = information[0];
		computationalTime = (FTime-ITime)/1000000000 + iniTime*2;
		
		
	}
	 
	 
	 /**
		 * This method runs the pulse
		 * @param args
		 * @throws IOException
		 * @throws InterruptedException
		 */
		 public void bidirectionalPulsePerimeter(int depth, int instance, String netPath) throws IOException, InterruptedException{


			File testFile = new File("./instances/Config"+instance+".txt");
			instanc = instance;
			
			BufferedReader bufRedr = new BufferedReader(new FileReader(testFile));
			
			String actLine = null;
			
			String [] information = new String [6];
			
			int rowA = 0;
			int colA = 0;
			
			while((actLine = bufRedr.readLine()) != null && rowA < 6){	
				String [] info = actLine.split(":");
				information[rowA] = info[1];
				rowA++;
			}

			//Modifies the instance tightness
			
			DataHandler data = new DataHandler(Integer.parseInt(information[2]),Integer.parseInt(information[1]),Integer.parseInt(information[5]),1,2,information[0]);
			PulseGraph network = null;
			destiny = Integer.parseInt(information[5]);
			data.ReadDimacs(netPath);
		
			//Backward direction network:
							
			//Creates the graph:
				
				network = createGraphB(data);
				network.SetConstraint(Integer.parseInt(information[3]));	
							
			//Finds the bounds to reach the source node:
							
				SPB(data,network);

			//Stores information for the backward pulse:
							
				int numNodesAct = network.getNumNodes();
				Integer[][] lista = new Integer[numNodesAct][4];
				for (int i = 0; i < numNodesAct; i++) {
					VertexPulse actVertex = network.getVertexes()[i];
					lista[i][0] = actVertex.getMaxDistB();
					lista[i][1] = actVertex.getMaxTimeB();
					lista[i][2] = actVertex.getMinDistB();
					lista[i][3] = actVertex.getMinTimeB();
				}
							
			//Forward direction network:
							
				//Creates the graph:
							
				network = createGraphF(data);
				network.SetConstraint(Integer.parseInt(information[3]));
				
				//Finds the bounds to reach the sink node:
							
				SPF(data,network);
					
				// Set the first primal bound
							
				int MD=network.getVertexByID(data.getLastNode()-1).getMaxDist();
				network.setDestiny(data.getSource()-1);
				network.setPrimalBound(MD);
				InitialPrimalBound = MD;
				network.setTimeStar(network.getVertexByID(data.getLastNode()-1).getMinTime());
							
			// Recovers information for the backward pulse
							
				for (int i = 0; i < numNodesAct; i++) {
					VertexPulse actVertex = network.getVertexes()[i];
					actVertex.setEveryBound(lista[i]);	
				}
							
			// This is the pulse procedure
							
					network.depth = depth; //The queue depth
							
			//Starts the clock
							
					Double ITime = (double) System.nanoTime();
							
			//Bidirectional Pulse !
								
					runPulsesPerimeter(data,network);
								
			//Ends the clock
								
					Double FTime = (double) System.nanoTime();
										
			
			/*******************************************************************
			 ************************ RESULTS ******************************
			 *******************************************************************
			 */

			networkName = information[0];
			computationalTime = (FTime-ITime)/1000000000;
			
		}
	
	/**
	 * This method creates the graph in forward direction
	 * @param data
	 * @return
	 */
	private static PulseGraph createGraphF(DataHandler data) {
		int numNodes = data.NumNodes;
		PulseGraph Gd = new PulseGraph(numNodes);
		for (int i = 0; i < numNodes; i++) {
				Gd.addVertex(new VertexPulse(i) ); //Primero lo creo, y luego lo meto. El id corresponde al n�mero del nodo
		}
		for(int i = 0; i <data.NumArcs; i ++){
			Gd.addWeightedEdge( Gd.getVertexByID(data.Arcs[i][0]), Gd.getVertexByID(data.Arcs[i][1]),data.Distance[i],data.Time[i], i);			
		}
		return Gd;
	}
	
	/**
	 * This method creates the graph in backward direction
	 * @param data
	 * @return
	 */
	private static PulseGraph createGraphB(DataHandler data) {
		int numNodes = data.NumNodes;
		PulseGraph Gd = new PulseGraph(numNodes);
		for (int i = 0; i < numNodes; i++) {
				Gd.addVertex(new VertexPulse(i) ); //Primero lo creo, y luego lo meto. El id corresponde al n�mero del nodo
		}
		//System.out.println("Pase a los arcos");
		for(int i = 0; i <data.NumArcs; i ++){
			//System.out.println(data.Arcs[i][1]+ " - "+data.Arcs[i][0]+ " - "+data.Distance[i]+ " - "+data.Time[i]);
			Gd.addWeightedEdge( Gd.getVertexByID(data.Arcs[i][1]), Gd.getVertexByID(data.Arcs[i][0]),data.Distance[i], data.Time[i], i);			
		}
		return Gd;
	}
	
	private static void SPB(DataHandler data, PulseGraph network) throws InterruptedException {
		// Create two threads and run parallel SP for the initialization		
		Thread tTime = new Thread();
		Thread tDist = new Thread();
		// Reverse the network and run SP for distance and time 
		DukqstraDist spDist = new DukqstraDist(network, data.getLastNode()-1,2);
		DukqstraTime spTime = new DukqstraTime(network, data.getLastNode()-1,2);
		tDist = new Thread(new ShortestPathTask(1, spDist, null));
		tTime = new Thread(new ShortestPathTask(0, null,  spTime));
		tDist.start();
		tTime.start();
		tDist.join();
		tTime.join();
	}
	
	private static void SPF(DataHandler data, PulseGraph network) throws InterruptedException {
		Thread tTime2 = new Thread();
		Thread tDist2 = new Thread();
		// Reverse the network and run SP for distance and time 
		DukqstraDist spDist2 = new DukqstraDist(network, data.getSource()-1,1);
		DukqstraTime spTime2 = new DukqstraTime(network, data.getSource()-1,1);
		tDist2 = new Thread(new ShortestPathTask(1, spDist2, null));
		tTime2 = new Thread(new ShortestPathTask(0, null,  spTime2));
		tDist2.start();
		tTime2.start();
		tDist2.join();
		tTime2.join();
	}
	
	public static void runPulses(DataHandler data, PulseGraph network) throws InterruptedException {
		Thread tpulse1 = new Thread();
		Thread tpulse2 = new Thread();
		ArrayList threads = new ArrayList();
		pulseTask task1 = new pulseTask(1,network,threads,data.getLastNode(),data.getSource());
		pulseTask task2 = new pulseTask(2,network,threads,data.getLastNode(),data.getSource());
		threads.add(task1);
		threads.add(task2);
		tpulse1 = new Thread(task1);
		tpulse2 = new Thread(task2);
		tpulse1.start();
		tpulse2.start();
		tpulse1.join();
		tpulse2.join();
	}
	
	public static void runPulsesPerimeter(DataHandler data, PulseGraph network) throws InterruptedException {
		Thread tpulse1 = new Thread();
		Thread tpulse2 = new Thread();
		ArrayList threads = new ArrayList();
		pulseTask2 task1 = new pulseTask2(1,network,threads,data.getLastNode(),data.getSource());
		pulseTask2 task2 = new pulseTask2(2,network,threads,data.getLastNode(),data.getSource());
		threads.add(task1);
		threads.add(task2);
		tpulse1 = new Thread(task1);
		tpulse2 = new Thread(task2);
		tpulse1.start();
		tpulse2.start();
		tpulse1.join();
		tpulse2.join();
	}
	
	public static void resetAll(DataHandler data, PulseGraph network) {
		data.CvsInput = null;
		data.Arcs = null;
		data.Distance = null;
		data.pendingQueueB = null;
		data.pendingQueueF = null;
		network = null;
		System.gc();
		
	}
	
	//Additional methods to recover the path:
	
	/**
	 * Recovers the path when it comes from a path completion with the minimum cost path
	 * @param network
	 * @return path
	 */
	public static ArrayList<Integer> returnPathF(PulseGraph network) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = network.finalNodeF;
		boolean termine = false;
		double costoAcumulado = network.finalCostF;
		double tiempoAcumulado = network.finalTimeF2;
		
		while(termine == false) {
			int nodoActual = network.destiny;
			for(int i = 0; i < network.getVertexByID(nodoInicial).magicIndex.size(); i++) {
				int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(i);
				int a = DataHandler.Arcs[e][1];
				
				if(costoAcumulado + DataHandler.Distance[e] + network.getVertexByID(a).minDist == network.PrimalBound ) {
					if(tiempoAcumulado+ DataHandler.Time[e] + network.getVertexByID(a).maxTime == network.TimeStar) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
						nodoActual = a;	
					}
				}
				
			}
		
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
			}else {
				nodoInicial = nodoActual;
			}
		}
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		
		//Go back:
		
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostF2;
		tiempoAcumulado = network.TimeStar - network.finalTimeF2;
		nodoInicial = network.finalNodeF;
		while(termine == false) {
			int nodoActual = 0;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendF;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
							int a = DataHandler.Arcs[e][0];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendF;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == 0) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
					int a = DataHandler.Arcs[e][0];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	/**
	 * Recovers the path when it comes from a path completion with the minimum cost path
	 * @param network
	 * @return path
	 */
	public static ArrayList<Integer> returnPathB(PulseGraph network) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = network.finalNodeB;
		boolean termine = false;
		double costoAcumulado = network.finalCostB2;
		double tiempoAcumulado = network.finalTimeB2;
		while(termine == false) {
			int nodoActual = 0;
			for(int i = 0; i < network.getVertexByID(nodoInicial).magicIndex2.size(); i++) {
				int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(i);
				int a = DataHandler.Arcs[e][0];
				if(costoAcumulado+ DataHandler.Distance[e] + network.getVertexByID(a).minDistB == network.PrimalBound ) {
					if(tiempoAcumulado + DataHandler.Time[e] + network.getVertexByID(a).maxTimeB == network.TimeStar) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
						nodoActual = a;	
					}
				}
			}
		
			path.add(nodoActual);
			if(nodoActual == 0) {
				termine = true;
			}else {
				nodoInicial = nodoActual;
			}
		}
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		
		//Go back:
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostB2;
		tiempoAcumulado = network.TimeStar - network.finalTimeB2;
		nodoInicial = network.finalNodeB;
		while(termine == false) {
			int nodoActual = network.destiny;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendB;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
							int a = DataHandler.Arcs[e][1];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendB;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
					int a = DataHandler.Arcs[e][1];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	/**
	 * Recovers the path when it comes from a path completion with the minimum time path
	 * @param network
	 * @return path
	 */
	public static ArrayList<Integer> returnPathF2(PulseGraph network) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = network.finalNodeF;
		boolean termine = false;
		double costoAcumulado = network.finalCostF2;
		double tiempoAcumulado = network.finalTimeF2;
		
		while(termine == false) {
			int nodoActual = network.destiny;
			for(int i = 0; i < network.getVertexByID(nodoInicial).magicIndex.size(); i++) {
				int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(i);
				int a = DataHandler.Arcs[e][1];
				
				if(costoAcumulado + DataHandler.Distance[e] + network.getVertexByID(a).maxDist == network.PrimalBound ) {
					if(tiempoAcumulado + DataHandler.Time[e] + network.getVertexByID(a).minTime == network.TimeStar) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
						nodoActual = a;	
					}
				}
				
			}
		
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
			}else {
				nodoInicial = nodoActual;
			}
		}
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		//Go back:
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostF2;
		tiempoAcumulado = network.TimeStar - network.finalTimeF2;
		nodoInicial = network.finalNodeF;
		while(termine == false) {
			int nodoActual = 0;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendF;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
							int a = DataHandler.Arcs[e][0];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendF;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == 0) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
					int a = DataHandler.Arcs[e][0];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	/**
	 * Recovers the path when it comes from a path completion with the minimum time path
	 * @param network
	 * @return path
	 */
	public static ArrayList<Integer> returnPathB2(PulseGraph network) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = network.finalNodeB;
		boolean termine = false;
		double costoAcumulado = network.finalCostB2;
		double tiempoAcumulado = network.finalTimeB2;
		double prueba = 0;
		while(termine == false) {
			int nodoActual = 0;
			for(int i = 0; i < network.getVertexByID(nodoInicial).magicIndex2.size(); i++) {
				int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(i);
				int a = DataHandler.Arcs[e][0];
				if(costoAcumulado + DataHandler.Distance[e] + network.getVertexByID(a).maxDistB == network.PrimalBound ) {
					if(tiempoAcumulado + DataHandler.Time[e] + network.getVertexByID(a).minTimeB == network.TimeStar) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
						nodoActual = a;	
						prueba += DataHandler.Time[e];
					}
				}
			}

			path.add(nodoActual);
			if(nodoActual == 0) {
				termine = true;
			}else {
				nodoInicial = nodoActual;
			}
		}
	
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		//Go back:
		
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostB2;
		tiempoAcumulado = network.TimeStar - network.finalTimeB2;
		nodoInicial = network.finalNodeB;
		while(termine == false) {
			int nodoActual = network.destiny;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendB;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
							int a = DataHandler.Arcs[e][1];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendB;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
					int a = DataHandler.Arcs[e][1];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	
	/**
	 * Recovers the path when it comes from a path completion with the minimum time path
	 * @param network
	 * @return path
	 */
	public static ArrayList<Integer> returnPathJP(PulseGraph network) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = network.finalNodeB;
		boolean termine = false;
		double costoAcumulado = network.finalCostB2;
		double tiempoAcumulado = network.finalTimeB2;
		double prueba = 0;
	
		//Forward direction
		
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostF2;
		tiempoAcumulado = network.TimeStar - network.finalTimeF2;
		nodoInicial = network.finalNodeF;
		while(termine == false) {
			int nodoActual = 0;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendF;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
							int a = DataHandler.Arcs[e][0];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendF;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == 0) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex2.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex2.get(j);
					int a = DataHandler.Arcs[e][0];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		
		for (int i = 0; i < path.size() / 2; i++) {
		     Object temp = path.get(i);
		     path.set(i, path.get(path.size() - i - 1));
		     path.set(path.size() - i - 1, (Integer) temp);
		   }
		//Backward direction
		
		termine = false;
		costoAcumulado = network.PrimalBound - network.finalCostB2;
		tiempoAcumulado = network.TimeStar - network.finalTimeB2;
		nodoInicial = network.finalNodeB;
		while(termine == false) {
			int nodoActual = network.destiny;
			boolean cambie = false;
			ArrayList<PendingPulse> pendingPulses = network.getVertexByID(nodoInicial).pendB;
			for(int i = 0; i < pendingPulses.size() && !cambie;i++) {
				PendingPulse p = pendingPulses.get(i);
				if(p.getDist() +costoAcumulado == network.PrimalBound) {
					if(p.getTime() +  tiempoAcumulado == network.TimeStar) {
						nodoActual = p.getPredId();
						for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size() && !cambie; j++) {
							int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
							int a = DataHandler.Arcs[e][1];
							if(a == nodoActual) {
								ArrayList<PendingPulse> pendingPulsesAux = network.getVertexByID(nodoActual).pendB;
								for(int ii = 0; ii < pendingPulsesAux.size();ii++) {
									PendingPulse pp = pendingPulsesAux.get(ii);
									if(pp.getDist() + costoAcumulado + DataHandler.Distance[e] == network.PrimalBound && pp.getTime() + tiempoAcumulado + DataHandler.Time[e] == network.TimeStar ) {
										cambie = true;
										costoAcumulado+=DataHandler.Distance[e];
										tiempoAcumulado+=DataHandler.Time[e];
									}
								}
							}
						}
					}
				}
			}
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
				for(int j = 0; j < network.getVertexByID(nodoInicial).magicIndex.size(); j++) {
					int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(j);
					int a = DataHandler.Arcs[e][1];
					if(a == nodoActual) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
					}
				}
			}else {
				nodoInicial = nodoActual;
			}
		}		
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	public static ArrayList<Integer> returnPathIni(PulseGraph network) {
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		int nodoInicial = 0;
		boolean termine = false;
		double costoAcumulado = 0;
		double tiempoAcumulado = 0;
		path.add(0);
	
		while(termine == false) {
			int nodoActual = network.destiny;
			for(int i = 0; i < network.getVertexByID(nodoInicial).magicIndex.size(); i++) {
				int e = (Integer) network.getVertexByID(nodoInicial).magicIndex.get(i);
				int a = DataHandler.Arcs[e][1];
				if(costoAcumulado + DataHandler.Distance[e] + network.getVertexByID(a).maxDist == network.PrimalBound ) {
					if(tiempoAcumulado+ DataHandler.Time[e] + network.getVertexByID(a).minTime == network.TimeStar) {
						costoAcumulado+=DataHandler.Distance[e];
						tiempoAcumulado+=DataHandler.Time[e];
						nodoActual = a;	
					}
				}
				
			}
		
			path.add(nodoActual);
			if(nodoActual == network.destiny) {
				termine = true;
			}else {
				nodoInicial = nodoActual;
			}
		}
		if(costoAcumulado != network.PrimalBound) {
			System.out.println("El costo no me dio igual...peligro");
		}
		return path;
	}
	
	/**
	 * This method recovers the final path
	 * @param network
	 * @return
	 */
	public static ArrayList<Integer> recoverThePath(PulseGraph network){
		ArrayList<Integer> path = new ArrayList<Integer>();
	
		if(network.best == 1) {
			path = returnPathF(network);
		}
		else if (network.best == 2) {
			path = returnPathF2(network);
		}
		else if(network.best == 3) {
			path = returnPathB(network);
		}	
		else if(network.best == 4) {
			path = returnPathB2(network);
		}
		else if(network.best == 5){
			path = returnPathJP(network);
		}
		else {
			path = returnPathIni(network);
		}
		
		return path;
	}
	
	public static String whoFindThePath(PulseGraph network){
		String rta = "";
	
		if(network.best == 1) {
			rta = "Minimum cost path completion in forward direction";
		}
		else if (network.best == 2) {
			rta = "Minimum time path completion in forward direction";
		}
		else if(network.best == 3) {
			rta = "Minimum cost path completion in backward direction";
		}	
		else if(network.best == 4) {
			rta = "Minimum time path completion in backward direction";
		}
		else if(network.best == 5) {
			rta = "Join paths!";
		}
		else {
			rta = "The initialization step";
		}
		
		return rta;
	}
	
}
