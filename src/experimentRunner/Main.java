package experimentRunner;

import java.io.IOException;

import model.Manager;
import model.PulseAlgorithm;
import view.ManagerView;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		/*********************************************************************************************************
		 ************************ Select the initial and final id of the instances you want **********************
		 **BAY: 1 - 40
		 **NY: 41 - 80
		 **COL: 81 - 120
		 **FLA: 121 - 160
		 **NE: 161 - 200
		 **CAL: 201 - 240
		 **LKS: 261 - 280
		 **E: 281 - 320
		 **W: 321 - 360
		 **CTR: 361 - 400
		 **USA: 401 - 420
		 *********************************************************************************************************
		 */
		
		int ins = 20;
		
		/*********************************************************************************************************
		 ************************************** Select the pulse depth limit *************************************
		 *********************************************************************************************************
		 */
		
		int depth = 2;  //Pulse depth limit

		//Create the manager's
		
		Manager model = new Manager();
		ManagerView view = new ManagerView();
		
		/*********************************************************************************************************
		 ************************ Modify this line according to the path on your computer ************************
		 *********************************************************************************************************
		 */
		
		String netPath = "./Networks/";
		
		/*********************************************************************************************************
		 ***************************** The following lines run the selected instances*****************************
		 *********************************************************************************************************
		 */
		
		PulseAlgorithm bidirectionalPulse = model.runBidirectionalPulse(depth, ins, netPath);
		view.printResults(bidirectionalPulse);
				
	}
	
}
