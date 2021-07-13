/**
 * This class represents a node, contains the pulse main logic.
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

package dataStructures;
import java.util.ArrayList;

public class VertexPulse {
	/**
	 * This array contains the indexes for all the outgoing arcs from this node
	 */
	public ArrayList<Integer> magicIndex;
	
	/**
	 * This array contains the indexes for all the incoming arcs to this node
	 */
	public ArrayList<Integer> magicIndex2;
	
	/**
	 * Boolean that tells if the node is visited for first time in the forward pulse
	 */
	boolean firstTime = true;
	/**
	 * Boolean that tells if the node is visited for first time in the backward pulse
	 */
	boolean firstTime2 = true;
	
	/**
	 * Bounds to reach the end node
	 */
	public int minDist;
	public int maxTime;
	public int minTime;
	public int maxDist;
	
	/**
	 * Bounds to reach the source node
	 */
	public int minDistB;
	public int maxTimeB;
	public int minTimeB;
	public int maxDistB;
	/**
	 * SP stuff
	 */
	public static final int infinity = (int)Double.POSITIVE_INFINITY;
	/**
	 * The edge that is coming to the node
	 */
	private EdgePulse reverseEdges;
	/**
	 * The vertex id
	 */
	private int id;
	private VertexPulse leftDist;
	private VertexPulse rigthDist;
	private VertexPulse leftTime;
	private VertexPulse rigthTime;
	private boolean insertedDist;
	private boolean insertedTime;
	

	public ArrayList<PendingPulse> pendF;
	public ArrayList<PendingPulse> pendB;
	

	/**
	 * Creates a node
	 * @param i the id
	 */
	public VertexPulse(int i) {
		id = i;
		insertedDist = false;
		minDist = infinity;
		minTime = infinity;
		maxTime = 0;
		maxDist = 0;
		reverseEdges = null;
		leftDist = this;
		rigthDist = this;
		leftTime = this;
		rigthTime = this;
	
		magicIndex = new ArrayList<Integer>();
		magicIndex2 = new ArrayList<Integer>();
		
		pendF = new ArrayList<PendingPulse>();
		pendB = new ArrayList<PendingPulse>();
		
	}
	
	public void resetAlmostEverything() {
		insertedDist = false;
		minDist = infinity;
		minTime = infinity;
		maxTime = 0;
		maxDist = 0;

		leftDist = this;
		rigthDist = this;
		leftTime = this;
		rigthTime = this;
	
		magicIndex = new ArrayList<Integer>();
		magicIndex2 = new ArrayList<Integer>();
		
		reverseEdges = null;
		
	}
	/**
	 * Returns the node id
	 * @return
	 */
	public int  getID()
	{
		return id;
	}
	
	/**
	 * Adds an edge to the coming arcs list
	 * @param e the edge
	 */
	public void addReversedEdge(EdgePulse e)
	{
		if(reverseEdges!=null){
			reverseEdges.addNextCommonTailEdge(e);
		}else
			reverseEdges = e;
	}
	
	/**
	 * Returns the list of reversed edges
	 * @return
	 */
	public EdgePulse getReversedEdges() {
		if(reverseEdges!= null){
			return reverseEdges;
		}return new EdgePulse(1,1, this,this , -1);
	}
	
	/**
	 * Sets the minimum distance
	 * @param c
	 */
	public void setMinDistB(int c){
		minDistB = c;
	}
	
	/**
	 * Returns the minimum distance
	 * @return
	 */
	public int getMinDistB(){
		return minDistB;
	}
	/**
	 * Sets the maximum time
	 * @param mt
	 */
	public void setMaxDistB(int mt){
		maxDistB = mt;
	}
	/**
	 * Sets the maximum time
	 * @param mt
	 */
	public void setMaxTimeB(int mt){
		maxTimeB = mt;
	}
	/**
	 * Returns the maximum time
	 * @return
	 */
	public int getMaxTimeB(){
		return maxTimeB;
	}
	/**
	 * Sets the minimum time
	 * @param t
	 */
	public void setMinTimeB(int t){
		minTimeB = t;
	}
	/**
	 * Returns the minimum time
	 * @return
	 */
	public int getMinTimeB(){
		return minTimeB;
	}
	/**
	 * Sets the maximum distance
	 * @param md
	 */
	public void maxTimeB(int md){
		maxDistB = md;
	}
	/**
	 * Returns the maximum distance
	 * @return
	 */
	public int getMaxDistB(){
		return maxDistB;
	}
	
	//*****************************
	
	/**
	 * Sets the minimum distance
	 * @param c
	 */
	public void setMinDist(int c){
		minDist = c;
	}
	
	/**
	 * Returns the minimum distance
	 * @return
	 */
	public int getMinDist(){
		return minDist;
	}
	
	/**
	 * Sets the maximum time
	 * @param mt
	 */
	public void setMaxTime(int mt){
		maxTime = mt;
	}
	/**
	 * Returns the maximum time
	 * @return
	 */
	public int getMaxTime(){
		return maxTime;
	}
	/**
	 * Sets the minimum time
	 * @param t
	 */
	public void setMinTime(int t){
		minTime = t;
	}
	/**
	 * Returns the minimum time
	 * @return
	 */
	public int getMinTime(){
		return minTime;
	}
	/**
	 * Sets the maximum distance
	 * @param md
	 */
	public void setMaxDist(int md){
		maxDist = md;
	}
	/**
	 * Returns the maximum distance
	 * @return
	 */
	public int getMaxDist(){
		return maxDist;
	}
	
	
	
	//*****************************
	
	
	
	
	/**
	 * Unlink a vertex from the bucket
	 * @return true, if the buckets gets empty
	 */
	public boolean unLinkVertexDist(){
		if(rigthDist.getID() == id){
			leftDist=this;
			rigthDist=this;
			return true;
		}else{
			leftDist.setRigthDist(rigthDist);
			rigthDist.setLeftDist(leftDist);
			leftDist = this;
			rigthDist = this;
			return false;
		}
	}
	/**
	 * 
	 * @return
	 */
	public boolean unLinkVertexTime(){
		if(rigthTime.getID() == id){
			leftTime=this;
			rigthTime=this;
			return true;
		}else{
			leftTime.setRigthTime(rigthTime);
			rigthTime.setLeftTime(leftTime);
			leftTime = this;
			rigthTime = this;
			return false;
		}
	}

	public void fastUnlinkDist(){
		leftDist=this;
		rigthDist=this;
	}
	public void fastUnlinkTime(){
		leftTime = this;
		rigthTime = this;
	}
	public void unlinkRighBoundDist()
	{
		rigthDist = null;
	}
	public void unlinkRighBoundTime()
	{
		rigthTime = null;
	}
	/**
	 * Insert a vertex in a bucket. 
	 * New vertex is inserted on the left of the bucket entrance 
	 * @param v vertex in progress to be inserted
	 */
	public void insertVertexDist(VertexPulse v) {
		v.setLeftDist(leftDist);
		v.setRigthDist(this);
		leftDist.setRigthDist(v);
		leftDist = v;
	}
	
	/**
	 * 
	 * @param v
	 */
	public void insertVertexTime(VertexPulse v) {
		v.setLeftTime(leftTime);
		v.setRigthTime(this);
		leftTime.setRigthTime(v);
		leftTime = v;
	}
	
	/**
	 * Distance basic methods
	 */
	public void setLeftDist(VertexPulse v){
		leftDist= v;
	}
	public void setRigthDist(VertexPulse v){
		rigthDist= v;
	}
	public VertexPulse getBLeftDist(){
		return leftDist;
	}
	public VertexPulse getBRigthDist(){
		return rigthDist;
	}
	public void setInsertedDist(){
		insertedDist = true;
	}
	public boolean isInserteDist(){
		return insertedDist;
	}
	/**
	 * Time basic methods
	 */
	public void setLeftTime(VertexPulse v){
		leftTime= v;
	}
	public void setRigthTime(VertexPulse v){
		rigthTime= v;
	}
	public VertexPulse getBLeftTime(){
		return leftTime;
	}
	public VertexPulse getBRigthTime(){
		return rigthTime;
	}
	public void setInsertedTime(){
		insertedTime = true;
	}
	public boolean isInsertedTime(){
		return insertedTime;
	}
	
	/**
	 * 
	 */
	public void reset(){
		insertedDist = false;
	}
	
	
//********************* This is the pulse algorithm************************
	
	/**
	 * This is the pulse procedure in the forward direction.
	 * @param PTime time for the current pulse
	 * @param PDist distance for the current pulse
	 * @param step number of steps (For queues)
	 * @param pred the predecessor for the current pulse
	 */
	
	public void pulseF(int[] pulseWeights,int step,int pred){
		
		//Checks if the other thread is still alive
		if(PulseGraph.termine == false) {
		
			// if a node is visited for first time, sort the arcs
				
			if (this.firstTime) {
				this.firstTime = false;
				this.Sort(this.magicIndex); //Reviso primero al arco que tiene una menor distancia. (Menor costo)
				leftDist = null;
				rigthDist = null;
				reverseEdges = null;
			}
				
			//Checks if the pulse is dominated
			if(notDominatedF(pulseWeights,id)) {
				
				//Creates a pending pulse
				PendingPulse p = new PendingPulse(this.id,pulseWeights,pred);
				p.setSortCriteriaF(pulseWeights[1] + PulseGraph.getVertexes()[this.id].minDist );
				p.setNotTreated(false);
				this.pendF.add(p);

				// Check for cycles
					
					//Checks if the current path should be completed
					
					if(completePathCheckF(pulseWeights)) {
						
						//Tries to join paths
						checksPathJoinF(pulseWeights);
						// Pulse all the head nodes for the outgoing arcs
						for (int i = 0; i < magicIndex.size(); i++) { //Recordemos que cojo primero al arco que tiene menor costo
							
							// Update distance and time
								int e = (Integer) magicIndex.get(i);
								int[] newWeights = new int[2];
								newWeights[0] = (pulseWeights[0] + DataHandler.Time[e]); //Tiempo del camino al agregar el nodo
								newWeights[1] = (pulseWeights[1] + DataHandler.Distance[e]); //Distancia del camino al agregar el nodo
								int a = DataHandler.Arcs[e][1];
								
								// Pruning strategies: infeasibility, bounds and labels
							
								if ( (newWeights[0] + PulseGraph.getVertexes()[a].getMinTime() <= PulseGraph.TimeC) && (newWeights[1] + PulseGraph.getVertexes()[a].getMinDist() < PulseGraph.PrimalBound)){
							
									step++;
									if(step >= PulseGraph.depth) {
										//Checks if the stopped pulse is dominated
										if(notDominatedF(newWeights,a)) {
											p = new PendingPulse(a,newWeights,id);
											p.setSortCriteriaF(newWeights[1] + PulseGraph.getVertexes()[a].minDist );
											DataHandler.addPendingPulse_DOrder(p, DataHandler.pendingQueueF);
											//DataHandler.addPendingPulse_DOrder(p, PulseGraph.vertexes[a].pendF);
											PulseGraph.getVertexes()[a].pendF.add(p);
											
										}
									}
									else {
										PulseGraph.getVertexes()[a].pulseF(newWeights, step,id);
									}
									step--;
								}
								//En caso de no haber cumplido se pasaria al siguiente sucesor
							}
						}
					  //  PulseGraph.Visited[id]=0;
					//}
				}
			}
	}
	
	/**
	 * This is the pulse procedure in the forward direction. (When the queue is empty)
	 * @param PTime time for the current pulse
	 * @param PDist distance for the current pulse
	 * @param step number of steps (For queues)
	 * @param pred the predecessor for the current pulse
	 */
	
	public void pulseFWithQueues(int[] pulseWeights,int step,int pred){
		//Checks if the other thread is still alive
		if(PulseGraph.termine == false) {
		
			// if a node is visited for first time, sort the arcs
			if (this.firstTime) {
				this.firstTime = false;
				this.Sort(this.magicIndex); //Reviso primero al arco que tiene una menor distancia. (Menor costo)
				leftDist = null;
				rigthDist = null;
				reverseEdges = null;
			}
			

				//Tries to complete the path
				if(completePathCheckF(pulseWeights)) {
					//Tries to join paths
					checksPathJoinF(pulseWeights);
					// Pulse all the head nodes for the outgoing arcs
					for (int i = 0; i < magicIndex.size(); i++) { //Recordemos que cojo primero al arco que tiene menor costo
						// Update distance and time

						int e = (Integer) magicIndex.get(i);	
						int[] newWeights = new int[2];
						newWeights[0] = (pulseWeights[0] + DataHandler.Time[e]); //Tiempo del camino al agregar el nodo
						newWeights[1] = (pulseWeights[1] + DataHandler.Distance[e]); //Distancia del camino al agregar el nodo
						int a = DataHandler.Arcs[e][1];
							
						// Pruning strategies: infeasibility, bounds and labels
						
						if ( (newWeights[0]+ PulseGraph.getVertexes()[a].getMinTime() <= PulseGraph.TimeC) && (newWeights[1] + PulseGraph.getVertexes()[a].getMinDist() < PulseGraph.PrimalBound)){
								
							step++;
							if(step >= PulseGraph.depth) {
								//Checks if the stopped pulse is dominated
								if(notDominatedF(newWeights,a)) {
									PendingPulse p = new PendingPulse(a,newWeights,id);
									p.setSortCriteriaF(newWeights[1] + PulseGraph.getVertexes()[a].minDist );
									DataHandler.addPendingPulse_DOrder(p, DataHandler.pendingQueueF);
									PulseGraph.getVertexes()[p.getNodeID()].pendF.add(p);
									//DataHandler.addPendingPulse_DOrder(p, PulseGraph.vertexes[p.getNodeID()].pendF);
								}
							}
							else {
								PulseGraph.getVertexes()[a].pulseF(newWeights, step,id);
							}
							step--;
						}
							
					}
				}
		}
	}

	/**
	 * This is the pulse procedure in the backward direction.
	 * @param PTime time for the current pulse
	 * @param PDist distance for the current pulse
	 * @param step number of steps (For queues)
	 * @param pred the predecessor for the current pulse
	 */
	public void pulseB(int pulseWeights[], int step,int pred){
		//Checks if the other thread is still alive
		if(PulseGraph.termine == false) {
			// if a node is visited for first time, sort the arcs
			
			if (this.firstTime2) {
				this.firstTime2 = false;
				this.Sort2(this.magicIndex2); //Reviso primero al arco que tiene una menor distancia. (Menor costo)
				leftDist = null;
				rigthDist = null;
				reverseEdges = null;
			}	
			
			//Checks if the current pulse is dominated
			if(notDominatedB(pulseWeights,id)) {
				
				//Creates a pending pulse
				PendingPulse p = new PendingPulse(this.id,pulseWeights,pred);
				p.setSortCriteriaB(pulseWeights[1] + PulseGraph.getVertexes()[this.id].minDistB );
				p.setNotTreated(false);
				this.pendB.add(p);

					//Checks if we can complete the path
					if(completePathCheckB(pulseWeights)) {
						//Tries to join paths
						checksPathJoinB(pulseWeights);
						// Pulse all the head nodes for the outgoing arcs
						for (int i = 0; i < magicIndex2.size(); i++) { //Recordemos que cojo primero al arco que tiene menor costo
							// Update distance and time
							int e = (Integer) magicIndex2.get(i);	
							int[] newWeights = new int[2];
							
							newWeights[0] = pulseWeights[0] + DataHandler.Time[e]; //Tiempo del camino al agregar el nodo
							newWeights[1] = pulseWeights[1] + DataHandler.Distance[e]; //Distancia del camino al agregar el nodo
							int a = DataHandler.Arcs[e][0];
								
							// Pruning strategies: infeasibility, bounds and labels	
						
							if ( (newWeights[0] + PulseGraph.getVertexes()[a].getMinTimeB() <= PulseGraph.TimeC) && (newWeights[1] + PulseGraph.getVertexes()[a].getMinDistB() < PulseGraph.PrimalBound)){
								// If not pruned the pulse travels to the next head node ){//
	
								step++;
								if(step >= PulseGraph.depth) {
									//Checks if the stopped pulse is dominated
									if(notDominatedB(newWeights,a)) {
										p = new PendingPulse(a,newWeights,id);
										p.setSortCriteriaB(newWeights[1] + PulseGraph.getVertexes()[a].minDistB );
										DataHandler.addPendingPulse_DOrder(p, DataHandler.pendingQueueB);
										//DataHandler.addPendingPulse_DOrder(p, PulseGraph.vertexes[p.getNodeID()].pendB);
										PulseGraph.getVertexes()[p.getNodeID()].pendB.add(p);
									}
								}
								else {
									PulseGraph.getVertexes()[a].pulseB(newWeights,step,id);
								}
								step--;
							}
						}
					}
			}
		}
	}
	
	/**
	 * This is the pulse procedure in the backward direction. (When the queue is empty)
	 * @param PTime time for the current pulse
	 * @param PDist distance for the current pulse
	 * @param step number of steps (For queues)
	 * @param pred the predecessor for the current pulse
	 */
	public void pulseBWithQueues(int[] pulseWeights, int step,int pred){
		//
		//Checks if the other thread is still alive
		if(PulseGraph.termine == false) {
			// if a node is visited for first time, sort the arcs
			if (this.firstTime2) {
				this.firstTime2 = false;
				this.Sort2(this.magicIndex2); //Reviso primero al arco que tiene una menor distancia. (Menor costo)
				leftDist = null;
				rigthDist = null;
				reverseEdges = null;
			}	
		
			// Checks if we can complete the path
				if(completePathCheckB(pulseWeights)) {
					//Tries to join paths
					checksPathJoinB(pulseWeights);
					// Pulse all the head nodes for the outgoing arcs
					for (int i = 0; i < magicIndex2.size(); i++) { //Recordemos que cojo primero al arco que tiene menor costo
						// Update distance and time
					
						int e = (Integer) magicIndex2.get(i);
						int[] newWeights = new int[2];
						
						newWeights[0] = pulseWeights[0] + DataHandler.Time[e]; //Tiempo del camino al agregar el nodo
						newWeights[1] = pulseWeights[1] + DataHandler.Distance[e]; //Distancia del camino al agregar el nodo
						
						int a = DataHandler.Arcs[e][0];
						
						
						if ( (newWeights[0] + PulseGraph.getVertexes()[a].getMinTimeB() <= PulseGraph.TimeC) && (newWeights[1] + PulseGraph.getVertexes()[a].getMinDistB() < PulseGraph.PrimalBound)){
							step++;
							if(step >= PulseGraph.depth) {
								//Checks if the stopped pulse is dominated
								if(notDominatedB(newWeights,a)) {
									PendingPulse p = new PendingPulse(a,newWeights,id);
									p.setSortCriteriaB(newWeights[1] + PulseGraph.getVertexes()[a].minDistB );
									DataHandler.addPendingPulse_DOrder(p, DataHandler.pendingQueueB);
									//DataHandler.addPendingPulse_DOrder(p, PulseGraph.vertexes[p.getNodeID()].pendB);
									PulseGraph.getVertexes()[p.getNodeID()].pendB.add(p);
								}
							}
							else {
								PulseGraph.getVertexes()[a].pulseB(newWeights, step,id);
							}
							step--;		
						}
					}
				}
		}
	}
	
	
	/**
	 * This method sorts the outgoing arcs
	 * @param set
	 */
	private void Sort(ArrayList<Integer> set) 
	{
		QS(magicIndex, 0, magicIndex.size()-1);
	}
	
	/**
	 * This method sorts the incoming arcs
	 * @param set
	 */
	private void Sort2(ArrayList<Integer> set) 
	{
		QS2(magicIndex2, 0, magicIndex2.size()-1);
	}
	
	/**
	 * 
	 * @param e
	 * @param b
	 * @param t
	 * @return
	 */
	public int colocar(ArrayList<Integer> e, int b, int t)
	{
	    int i;
	    int pivote, valor_pivote;
	    int temp;
	    //System.out.println("Encontre  al colocar");
	    pivote = b;
	    valor_pivote = PulseGraph.getVertexes()[DataHandler.Arcs[e.get(pivote)][1]].getCompareCriteria();
	    for (i=b+1; i<=t; i++){
	        if (PulseGraph.getVertexes()[DataHandler.Arcs[e.get(i)][1]].getCompareCriteria() < valor_pivote){
	                pivote++;    
	                temp= e.get(i);
	                e.set(i, e.get(pivote));
	                e.set(pivote, temp);
	        }
	    }
	    temp=e.get(b);
	    e.set(b, e.get(pivote));
        e.set(pivote, temp);
	    return pivote;
	    
	} 
	 
	 
	/**
	 * 
	 * @param e
	 * @param b
	 * @param t
	 */
	public void QS(ArrayList<Integer> e, int b, int t)
	{
		 int pivote;
	     if(b < t){
	        pivote=colocar(e,b,t);
	        QS(e,b,pivote-1);
	        QS(e,pivote+1,t);
	     }  
	}
	
	/**
	 * 
	 * @param e
	 * @param b
	 * @param t
	 * @return
	 */
	public int colocar2(ArrayList<Integer> e, int b, int t)
	{
	    int i;
	    int pivote, valor_pivote;
	    int temp;
	   // System.out.println("Encontre  al colocar");
	    pivote = b;
	    valor_pivote = PulseGraph.getVertexes()[DataHandler.Arcs[e.get(pivote)][0]].getCompareCriteria2();
	    for (i=b+1; i<=t; i++){
	        if (PulseGraph.getVertexes()[DataHandler.Arcs[e.get(i)][0]].getCompareCriteria2() < valor_pivote){
	                pivote++;    
	                temp= e.get(i);
	                e.set(i, e.get(pivote));
	                e.set(pivote, temp);
	        }
	    }
	    temp=e.get(b);
	    e.set(b, e.get(pivote));
        e.set(pivote, temp);
	    return pivote;
	    
	} 
	 
	 
	/**
	 * 
	 * @param e
	 * @param b
	 * @param t
	 */
	public void QS2(ArrayList<Integer> e, int b, int t)
	{
		 int pivote;
	     if(b < t){
	        pivote=colocar2(e,b,t);
	        QS2(e,b,pivote-1);
	        QS2(e,pivote+1,t);
	     }  
	}
	
	/**
	 * This method checks if the current pulse is dominated. Also, it eliminates dominated pulses.
	 * @param pulseWeights
	 * @param nodeID
	 * @return
	 */
	public boolean notDominatedF(int[] pulseWeights,int nodeID) {
		boolean notdominated = true;
		PendingPulse n;
		int rIndex;

		for(int i = PulseGraph.getVertexes()[nodeID].pendF.size()-1; i >=0 ;i--){
			n = PulseGraph.getVertexes()[nodeID].pendF.get(i);
			if((n.getDist()<=pulseWeights[1] && n.getTime() <= pulseWeights[0])){
				notdominated = false;
				}
			else if (n.getDist()>=pulseWeights[1] && n.getTime() >= pulseWeights[0] && DataHandler.pendingQueueF.size() > 0){
				
				if(n.getNotTreated()){
					rIndex = DataHandler.binarySearch(n,DataHandler.pendingQueueF);
					DataHandler.pendingQueueF.remove(rIndex);
				}
				PulseGraph.getVertexes()[nodeID].pendF.remove(i);
				n = null;
			}
		}
	
		return notdominated;
	}
	
	/**
	 * This method checks if the current pulse is dominated. Also, it eliminates dominated pulses.
	 * @param pulseWeights
	 * @param nodeID
	 * @return
	 */
	public boolean notDominatedB(int[] pulseWeights,int nodeID) {
		boolean notdominated = true;
		PendingPulse n;
		int rIndex;

		for(int i = PulseGraph.getVertexes()[nodeID].pendB.size()-1; i >=0 ;i--){
			n = PulseGraph.getVertexes()[nodeID].pendB.get(i);
			if((n.getDist()<=pulseWeights[1] && n.getTime() <= pulseWeights[0])){
				notdominated = false;
			}
			else if (n.getDist()>=pulseWeights[1] && n.getTime() >= pulseWeights[0] && DataHandler.pendingQueueB.size() > 0){
				if(n.getNotTreated()){
					rIndex = DataHandler.binarySearch(n,DataHandler.pendingQueueB);
					DataHandler.pendingQueueB.remove(rIndex);
				}
				PulseGraph.getVertexes()[nodeID].pendB.remove(i);
				n = null;
			}
		}
	
		return notdominated;
	}
	/**
	 * This method checks path completion for time and cost minimum paths
	 * @param PTime
	 * @param PDist
	 * @return true if the search must go on
	 */
	public boolean completePathCheckF(int[] pulseWeights){
		if(pulseWeights[0] + this.getMaxTime() <= PulseGraph.TimeC && pulseWeights[1] + this.getMinDist() < PulseGraph.PrimalBound){
			PulseGraph.PrimalBound = pulseWeights[1] + this.getMinDist();
			PulseGraph.TimeStar = pulseWeights[0] + this.getMaxTime();
			PulseGraph.finalCostF = this.getMinDist();
			PulseGraph.finalTimeF = this.getMaxTime();
			PulseGraph.finalNodeF = this.getID();
			PulseGraph.finalCostF2 = pulseWeights[1];
			PulseGraph.finalTimeF2 = pulseWeights[0];
			PulseGraph.best = 1;
			//System.out.println("MCF \t"+PulseGraph.PrimalBound);
			return false;
		}
		else{
			if(pulseWeights[0] + this.getMinTime() <= PulseGraph.TimeC && pulseWeights[1] + this.getMaxDist() < PulseGraph.PrimalBound){
				PulseGraph.PrimalBound = pulseWeights[1] + this.getMaxDist();
				PulseGraph.TimeStar = pulseWeights[0] + this.getMinTime();
				PulseGraph.finalCostF = (double)this.getMaxDist();
				PulseGraph.finalTimeF = (double)this.getMinTime();
				PulseGraph.finalNodeF = id;
				PulseGraph.finalCostF2 = (double)pulseWeights[1];
				PulseGraph.finalTimeF2 = (double)pulseWeights[0];
				PulseGraph.best = 2;
				//System.out.println("MTF \t"+PulseGraph.PrimalBound);
			}
			return true;
		}	
	}
	
	
	/**
	 * This method checks path completion in backward direction
	 * @param PTime
	 * @param PDist
	 * @return true if the search must go on
	 */
	public boolean completePathCheckB(int[] pulseWeights){
		if(pulseWeights[0] + this.getMaxTimeB() <= PulseGraph.TimeC && pulseWeights[1] + this.getMinDistB() < PulseGraph.PrimalBound){
			PulseGraph.PrimalBound = pulseWeights[1] + this.getMinDistB();
			PulseGraph.TimeStar = pulseWeights[0] + this.getMaxTimeB();
			PulseGraph.finalCostB = this.getMinDistB();
			PulseGraph.finalTimeB = this.getMaxTimeB();
			PulseGraph.finalNodeB = id;
			PulseGraph.finalCostB2 = pulseWeights[1];
			PulseGraph.finalTimeB2 = pulseWeights[0];
			PulseGraph.best = 3;
			//System.out.println("MCB \t"+PulseGraph.PrimalBound);
			return false;
		}
		else{
			if(pulseWeights[0] + this.getMinTimeB() <= PulseGraph.TimeC && pulseWeights[1] + this.getMaxDistB() < PulseGraph.PrimalBound){
				PulseGraph.PrimalBound = pulseWeights[1] + this.getMaxDistB();
				PulseGraph.TimeStar = pulseWeights[0] + this.getMinTimeB();
				PulseGraph.finalCostB = this.getMaxDistB();
				PulseGraph.finalTimeB = this.getMinTimeB();
				PulseGraph.finalNodeB = id;
				PulseGraph.finalCostB2 = pulseWeights[1];
				PulseGraph.finalTimeB2 = pulseWeights[0];
				PulseGraph.best = 4;
				//System.out.println("MTB \t"+PulseGraph.PrimalBound);
			}
			return true;
		}	
	}
		
	
	/**
	 * 
	 * @return
	 */
	public int getCompareCriteria(){
		return getMinDist();
	}

	public int getCompareCriteria2(){
		return getMinDistB();
	}
	
	public void setEveryBound(Integer[] lista) {
		maxDistB = lista[0];
		minDistB = lista[2];
		maxTimeB = lista[1];
		minTimeB = lista[3];
	}
	
	/**
	 * This method tries to join paths for a pulse in the forward direction, using information from the backward direction
	 * @param pulseWeights
	 */
	public void checksPathJoinF(int[] pulseWeights) {
		for(int i=0;i<pendB.size();i++) {
			PendingPulse p = pendB.get(i);
			if(p!=null) {
				if(p.getTime() + pulseWeights[0] <= PulseGraph.TimeC && p.getDist() + pulseWeights[1] < PulseGraph.PrimalBound) {
					PulseGraph.PrimalBound = p.getDist() + pulseWeights[1];
					PulseGraph.TimeStar =p.getTime() + pulseWeights[0];
					PulseGraph.best = 5;
					PulseGraph.finalNodeB = id;
					PulseGraph.finalCostB2 = p.getDist();
					PulseGraph.finalTimeB2 = p.getTime();
					PulseGraph.finalNodeF = id;
					PulseGraph.finalCostF2 = pulseWeights[1];
					PulseGraph.finalTimeF2 = pulseWeights[0];
					
					//System.out.println("JP \t"+PulseGraph.PrimalBound+" - "+PulseGraph.finalCostB2);
				}
			}
			p = null;
		}
	}
	
	/**
	 * This method tries to join paths for a pulse in the backward direction, using information from the forward direction
	 * @param pulseWeights
	 */
	public void checksPathJoinB(int[] pulseWeights) {
		for(int i=0;i<pendF.size();i++) {
			PendingPulse p = pendF.get(i);
			if(p!=null) {
				if(p.getTime() + pulseWeights[0] <= PulseGraph.TimeC && p.getDist() + pulseWeights[1] < PulseGraph.PrimalBound) {
					PulseGraph.PrimalBound = p.getDist() + pulseWeights[1];
					PulseGraph.TimeStar = p.getTime() + pulseWeights[0];
					PulseGraph.finalNodeB = id;
					PulseGraph.finalCostB2 = pulseWeights[1];
					PulseGraph.finalTimeB2 = pulseWeights[0];
					PulseGraph.finalNodeF = id;
					PulseGraph.finalCostF2 = p.getDist();
					PulseGraph.finalTimeF2 = p.getTime();
					PulseGraph.best = 5;
					//System.out.println("JP \t"+PulseGraph.PrimalBound+" - "+PulseGraph.finalCostB2);
				}
			}
		}
	}
}

