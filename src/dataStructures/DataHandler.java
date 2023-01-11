/**
 * This is a class that holds all the relevant data for an instance.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataHandler {
	
	/**
	 *  Name of the instance
	 */
	public String CvsInput;
	/**
	 * Number of arcs
	 */
	public int NumArcs;
	/**
	 * Number of nodes
	 */
	public static int NumNodes;
	/**
	 * Destination node
	 */
	private int LastNode;
	/**
	 * Source node
	 */
	private int Source;
	/**
	 * All the arcs in the network stored in a vector where Arcs[i][0]= Tail for arc i and Arcs[i][1]= Head for arc i 
	 */
	public static int[][] Arcs;
	/**
	 * The distance attribute for any arc i
	 */
	public static int[] Distance;
	/**
	 * The time attribute for any arc i
	 */
	public static int[] Time;
	/**
	 * Data structure for storing the graph
	 */
	private PulseGraph Gd;
	
	/**
	 * Type of pulse
	 */
	private int type;
	
	/**
	 * Acronym of the txt config file
	 */
	private String acro;
	
	/**
	 * Queue of pulses in the forward direction
	 */
	public static ArrayList<PendingPulse> pendingQueueF;
	
	/**
	 * Queue of pulses in the backward direction
	 */
	public static ArrayList<PendingPulse> pendingQueueB;
	

	/**
	 * Read data from an instance
	 * @param Instance
	 */
	public DataHandler(int numNodes, int numArcs, int sourceNode, int lastNode, int ty,String acronym) {
		
		NumArcs = numArcs;
		NumNodes = numNodes;
		setLastNode(lastNode);
		setSource(sourceNode);
		acro = acronym;
		setType(ty);
		
		//Creates the list of arcs. A list of distances and a list of times   --- Serian independientes del sentido de la red ! 
		Arcs = new int[numArcs][2];
		Distance = new int[numArcs];
		Time = new int[numArcs];
		
		//Creates the graph
		Gd = new PulseGraph(NumNodes);
		pendingQueueF = new ArrayList<PendingPulse>();
		pendingQueueB = new ArrayList<PendingPulse>();

	}

	public void addPendingPulseF(PendingPulse p) {
		pendingQueueF.add(p);
	}
	
	public void addPendingPulseB(PendingPulse p) {
		pendingQueueB.add(p);
	}
	public static int normalSearch(PendingPulse p, ArrayList<PendingPulse> labels) {
		int rta = -1;
		System.out.println("************************");
		System.out.println(p.getNodeID() + " - "+p.getTime() + " - "+p.getDist());
		
		for(int i = 0;i < labels.size(); i++) {
			PendingPulse current = labels.get(i);
			System.out.println(current.getNodeID() + " - "+current.getTime() + " - "+current.getDist());
			if(current.equals(p)) {
				return i;
			}
		}
		
		return rta;
	}
	
	
	public static int binarySearch(PendingPulse p, ArrayList<PendingPulse> labels) {
		double cScore = p.getSortCriteria();
		boolean cond = true;
		int l = 0; //izq
		int r = labels.size()-1; //der
		int m = (int) ((l + r) / 2); //medio
		double mVal = 0;
		//		System.out.println("Inicia :"+labels.get(l).getSortCriteria()+"\t"+labels.get(m).getSortCriteria()+"\t"+labels.get(r).getSortCriteria());
		if(labels.size() == 1){
			return 0;
		}else{
			mVal = labels.get(m).getSortCriteria();
		}
		while (cond) {
			//			 System.out.println("murio");
			if (r - l > 1) {
				if (cScore > mVal) {
					r = m;
					m = (int) ((l + r) / 2);
				} else if (cScore < mVal) {
					l = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()>labels.get(m).getNodeID()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()<labels.get(m).getNodeID()){
					l = m;
					m = (int) ((l + r) / 2);
				}  else if (p.getTime()>labels.get(m).getTime()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getTime()<labels.get(m).getTime()){
					l = m;
					m = (int) ((l + r) / 2);
				} else {
					return m;
				}
				mVal = labels.get(m).getSortCriteria();
			} else {
				cond = false;

				///if (p.getNodeID()==labels.get(r).getNodeID() && p.getSortCriteria() == labels.get(r).getSortCriteria() && p.getTime() == labels.get(r).getTime()){
					//return r;
				//}else if (p.getNodeID()==labels.get(l).getNodeID() && p.getSortCriteria() == labels.get(l).getSortCriteria()&& p.getTime() == labels.get(l).getTime()){
					//return l;
				//}
				
				if (p.equals(labels.get(r))){
					return r;
				}else if (p.equals(labels.get(l))){
					return l;
				}

			}
		}
		return -1;

	}

	/**
	 * This procedure creates the nodes for the graph
	 */
	public void upLoadNodes(){
		// All nodes are VertexPulse except the final node
		for (int i = 0; i < NumNodes; i++) {
			Gd.addVertex(new VertexPulse(i) ); //Primero lo creo, y luego lo meto. El id corresponde al n�mero del nodo
		}
	}
	
	/**
	 * This procedure returns a graph
	 * @return the graph
	 */
	public PulseGraph getGd()
	{
		return Gd;
	}

	/**
	 * This procedure reads data from a data file in DIMACS format
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void ReadDimacs(String netPath) throws NumberFormatException, IOException {
			File file2 = null;
			file2 = new File(netPath+acro);
			@SuppressWarnings("resource")
			BufferedReader bufRdr2 = new BufferedReader(new FileReader(file2));
			String line2 = null;
			int row2 = 0;
			while ((line2 = bufRdr2.readLine()) != null && row2 < NumArcs) {
				String[] Actual = line2.split(" ");
				Arcs[row2][0] = Integer.parseInt(Actual[0])-1;
				Arcs[row2][1] =  Integer.parseInt(Actual[1])-1;
				Distance[row2] = Integer.parseInt(Actual[2]);
				Time[row2] = Integer.parseInt(Actual[3]);
				row2++;
			}
		
	}
	
	
	public static void addPendingPulse_DOrder(PendingPulse p, ArrayList<PendingPulse>labels){

		double cScore = p.getSortCriteria();
		boolean cond = true;
		int l = 0; //Por izquierda
		int r = labels.size(); //Por derecha
		int m = (int) ((l + r) / 2); //La mitad
		double mVal = 0;
		if(labels.size() == 0) {
			labels.add(p);
			return;
		}
		else if(labels.size()  == 1) {
			mVal = labels.get(m).getSortCriteria();
			if(cScore == mVal) {
				if(p.getNodeID() == labels.get(m).getNodeID()) {
					labels.add(p.getTime()>labels.get(m).getTime()?0:1,p);
				}
				else {
					labels.add(p.getNodeID()>labels.get(m).getNodeID()?0:1,p);
				}
			}else {
				labels.add(cScore>mVal?0:1,p);
				return;
			}
		}
		else {
			mVal = labels.get(m).getSortCriteria();
		}
		while(cond) {
			if (r - l > 1) {
				if (cScore > mVal) {
					r = m;
					m = (int) ((l + r) / 2);
				} else if (cScore < mVal) {
					l = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()>labels.get(m).getNodeID()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()<labels.get(m).getNodeID()){
					l = m;
					m = (int) ((l + r) / 2);
				}  else if (p.getTime()>labels.get(m).getTime()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getTime()<labels.get(m).getTime()){
					l = m;
					m = (int) ((l + r) / 2);
				}  
				else {
					labels.add(m, p);
					return;
				}
				mVal = labels.get(m).getSortCriteria();
			} else {
				cond = false;
				if(l == m ){
					if (cScore == mVal){
						if(p.getNodeID()==labels.get(m).getNodeID()){
							labels.add(p.getTime()>labels.get(m).getTime()?l:l+1,p);
						}else{
							labels.add(p.getNodeID()>labels.get(m).getNodeID()?l:l+1,p);
						}						
					}else{
						labels.add(cScore>mVal?l:l+1,p);
					}
				}else if (r == m){
					if (cScore == mVal){
						if(p.getNodeID()==labels.get(m).getNodeID()){
							labels.add(p.getTime()>labels.get(m).getTime()?r:Math.min(r+1, labels.size()),p);
						}else{
							labels.add(p.getNodeID()>labels.get(m).getNodeID()?r:Math.min(r+1, labels.size()),p);
						}
					}else{
						labels.add(cScore>mVal?r:Math.min(r+1, labels.size()),p);
					}
				}else
				{
					System.err.println("LABEL, addLabel ");
				}
				return;
			}
			
			
		}
		
	}

	public int getLastNode() {
		return LastNode;
	}

	public void setLastNode(int lastNode) {
		LastNode = lastNode;
	}

	public int getSource() {
		return Source;
	}

	public void setSource(int source) {
		Source = source;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}	
	





/**
 * 
		row = 0;
		while ((line2 = bufRdr2.readLine()) != null && row < NumArcs+1) {
			String[] Actual1 = line2.split("\t");
			//System.out.println(line);
			int netActual = Integer.parseInt(Actual1[0]);
			if(netActual == networkId) {
				//System.out.println(line);
				readed2[row][0] = Actual1[1];
				readed2[row][1] = Actual1[2];
				readed2[row][2] = Actual1[3];
				
				row++;
			}
		}
try{
			
			PrintWriter pw = new PrintWriter("./instances/Santos/network"+networkId+".txt");
			
			int cuenta = 0;
			for(int i=0;i<NumNodes;i++) {
				int numAct = Integer.parseInt(readed[i+1]);
				int com = cuenta;
				int fin = numAct-Integer.parseInt(readed[i])+com;
				//System.out.println(i + " - "+com+" - "+fin);
				for(int j=com;j<fin && com <= NumArcs-1;j++) {
					pw.println(i + "\t"+(Integer.parseInt(readed2[j][0])-1) + "\t" +Integer.parseInt(readed2[j][1]) + "\t"+ Integer.parseInt(readed2[j][2])+"\t"+cuenta);
					cuenta+=1;
				}
				
			}
			pw.close();
			//System.out.println("Pase por aca 2");
		}
		catch(Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println("Hubo un error imprimiendo");
		}
*/