/**
 * This class represents a graph. It is used for the SP and the pulse!!!
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
import java.util.Collection;
import java.util.Set;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

public class PulseGraph  implements Graph<VertexPulse, EdgePulse> {
	
	/**
	 * The nodes
	 */
	private static VertexPulse[] vertexes;
	/**
	 * Number of nodes
	 */
	private int numNodes;
	/**
	 * SP stuff
	 */
	private int Cd;
	private int Ct;
	
	/**
	 * The final path
	 */
	
	public ArrayList<Integer> Path;
	
	/**
	 * Is time to end
	 */
	
	public static boolean nowIsTime;
	
	/**
	 * Time constraint
	 */
	public static double TimeC;
	/**
	 * Primal bound
	 */
	public static double PrimalBound;
	
	/**
	 * The time for the best solution found (the distance is stored in the primal bound)
	 */
	public static double TimeStar;

	/**
	 * Network destiny
	 */

	public static int destiny;
	
	/**
	 * Bidirectional important info
	 */
	public static boolean termine;
	public static int terminePrimero;
	static int contadorTerm;
	
	/**
	 * Pulse queue depth limit
	 */
	public static int depth;

	
	/**
	 * Information to retrieve the path
	 */
	public static int finalNodeF;
	public static int finalNodeB;
	
	/**
	 * Costs when the path was found
	 */
	public static double finalCostF;
	public static double finalCostB;
	public static double finalCostF2;
	public static double finalCostB2;
	
	/**
	 * Times when the path was found
	 */
	public static double finalTimeF;
	public static double finalTimeB;
	public static double finalTimeF2;
	public static double finalTimeB2;
	/**
	 * Acceleration strategy
	 */
	public static int best;
	
	/**
	 * creates a pulse graph
	 * @param numNodes
	 */
	public PulseGraph( int numNodes) {
		super();
		this.numNodes = numNodes;
		Cd=0;
		Ct=0;
		setVertexes(new VertexPulse[numNodes]);
		destiny = 0;
		contadorTerm=0;
		termine = false;
		terminePrimero=0;
		finalNodeF = numNodes-1;
		finalNodeB = 0;
		best = 0;
	}
	
	public void setFinalNodeF(int dep) {
		finalNodeF = dep;
	}
	public void setFinalNodeB(int des) {
		finalNodeB = des;
	}
	
	public int getFinalNodeF() {
		return finalNodeF;
	}
	
	public int getFinalNodeB() {
		return finalNodeB;
	}
	
	
	public double getFinalCostF2() {
		return finalCostF2;
	}
	
	public double getFinalCostB2() {
		return finalCostB2;
	}
	
	public double getFinalTimeF2() {
		return finalTimeF2;
	}
	
	public double getFinalTimeB2() {
		return finalTimeB2;
	}
	
	public double getFinalCostF() {
		return finalCostF;
	}
	
	public double getFinalCostB() {
		return finalCostB;
	}
	
	public double getFinalTimeF() {
		return finalTimeF;
	}
	
	public double getFinalTimeB() {
		return finalTimeB;
	}
	
	
	



	/**
	 * Sets the pulse depth limit
	 * @param dep
	 */
	public void setDepth(int dep) {
		depth = dep;
	}
	public void setDestiny(int des) {
		destiny = des;
	}

	public void setFirst(int ter) {
		if(contadorTerm == 0) {
			terminePrimero = ter;
			contadorTerm = 1;
		}
	}
	
	
	/**
	 * Adds an edge
	 */
	public EdgePulse addEdge(VertexPulse sourceVertex, VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns the number of nodes
	 * @return
	 */
	public  int getNumNodes()
	{
		return numNodes;
	}
	/**
	 * Returns the node searched by an id
	 * @param id
	 * @return
	 */
	public static VertexPulse getVertexByID(int id){
		return getVertexes()[id];
	}
	
	/**
	 * Adds an edge with a weight
	 * @param sourceVertex arc tail
	 * @param targetVertex arc head
	 * @param d arc distance
	 * @param t arc time
	 * @param id arc id
	 * @return
	 */
	public EdgePulse addWeightedEdge(VertexPulse sourceVertex, VertexPulse targetVertex, int d, int t, int id) {
		//We keep track of the higher distance and the higher time in the network
		if(d>Cd){
			Cd=d;
		}
		if(t>Ct){
			Ct=t;
		}
		//To the head node we add an incoming arc
		getVertexes()[targetVertex.getID()].addReversedEdge(new EdgePulse(d, t, targetVertex , sourceVertex, id));
		//To the tail node we add an outgoing arc
		getVertexes()[sourceVertex.getID()].magicIndex.add(id);
		getVertexes()[targetVertex.getID()].magicIndex2.add(id);
		return null;
	}
	
	
	/**
	 * Adds and edge
	 */
	public boolean addEdge(VertexPulse sourceVertex, VertexPulse targetVertex, EdgePulse e) {
		return false;
	}

	/**
	 * Adds a vertex. The position in the list is the node id
	 */
	public boolean addVertex(VertexPulse v) {
		getVertexes()[v.getID()] = v;
		return true;
	}
	/**
	 * Adds a final vertex. The position in the list is the node id
	 * @param v
	 * @return
	 */
	public boolean addFinalVertex(FinalVertexPulse v) {
		getVertexes()[v.getID()] = v;
		return true;
	}

	/**
	 * Verifies if the edge is in
	 */
	public boolean containsEdge(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Verifies if the edge is in
	 */
	public boolean containsEdge(EdgePulse e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Verifies if the node is in
	 */
	public boolean containsVertex(VertexPulse v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<EdgePulse> edgeSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EdgePulse> edgesOf(VertexPulse vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EdgePulse> getAllEdges(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdgePulse getEdge(VertexPulse sourceVertex, VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdgeFactory<VertexPulse, EdgePulse> getEdgeFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VertexPulse getEdgeSource(EdgePulse e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VertexPulse getEdgeTarget(EdgePulse e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getEdgeWeight(EdgePulse e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean removeAllEdges(Collection<? extends EdgePulse> edges) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<EdgePulse> removeAllEdges(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllVertices(Collection<? extends VertexPulse> vertices) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EdgePulse removeEdge(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeEdge(EdgePulse e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeVertex(VertexPulse v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<VertexPulse> vertexSet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns the distance bound
	 * @return
	 */
	public int getCd()
	{
		return Cd;
	}
	/**
	 * Returns the time bound
	 * @return
	 */
	public int getCt()
	{
		return Ct;
	}
	
	/**
	 * Restarts the network
	 */
	public void resetNetwork(){
		for (int i = 0; i < numNodes ; i++) {
			getVertexes()[i].reset();
		}
	}
	
	/**
	 * Sets the constraint
	 * @param timeC
	 */
	public void SetConstraint(double timeC) {
		
		PulseGraph.TimeC=timeC;
		
	}
	/**
	 * Sets the primal bound
	 * @param bound
	 */
	public void setPrimalBound(int bound) {
		
		PulseGraph.PrimalBound=bound;
		
	}

	public void setTimeStar(int timeStar) {
		
		PulseGraph.TimeStar=timeStar;
		
	}

	public static VertexPulse[] getVertexes() {
		return vertexes;
	}

	public static void setVertexes(VertexPulse[] vertexes) {
		PulseGraph.vertexes = vertexes;
	}
	


}
