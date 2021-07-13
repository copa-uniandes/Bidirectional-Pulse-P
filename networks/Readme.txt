===========================================================================================================================================================================

 Author:       Leonardo Lozano (leo-loza@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes
 URL:          http://www.leo-loza.com


 Author:       Daniel Duque (d.duque25@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes
               
 Author:       Nicolás Cabrera (n.cabrera10@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes             

 Author:       Andres L. Medaglia (amedagli@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes
 URL:          http://wwwprof.uniandes.edu.co/~amedagli


===========================================================================================================================================================================

This file contains important information about the Java code for the CSP.
===========================================================================================================================================================================

This file contains all the source code for executing the pulse algorithm for the Constrained Shortest Path Problem (CSP). 
 
We include the configuration file for each instance in Cabrera et al. (2020) (configX.txt) and two sample data files (USA-road-NY.txt and USA-road-BAY.txt). The first line presents the number of nodes and arcs in the network.
From the second line to the end, the arcs information is presented in the form: (tail, head, cost, weight).


===========================================================================================================================================================================
Sample Network
===========================================================================================================================================================================

Both sample networks have been taken from the 9th DIMACS Implementation Challenge.

===========================================================================================================================================================================
References
===========================================================================================================================================================================

- Demetrescu, C., Goldberg, A., & Johnson, D. (2006). 9th DIMACS Implementation Challenge - Shortest Paths.
	 http://www.dis.uniroma1.it/~challenge9/

	 
===========================================================================================================================================================================
Usage & License
===========================================================================================================================================================================

This is the Java implementation of the bidirectional pulse algorithm as published in: "Cabrera et al. (2020). An exact bidirectional pulse algorithm for the constrained shortest path. If you use (or modified) this code, please cite the paper by Cabrera et al. (2020). The authors would really enjoy to know the (good) use of the pulse algorithm in different fields, so please send a line to amedagli@uniandes.edu.co or copa@uniandes.edu.co describing us your application (as brief as you want). 
