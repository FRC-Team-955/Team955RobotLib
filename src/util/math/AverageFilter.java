package util.math;

import java.util.Arrays;

/**
 * Class which defines a simple average filter. After initilization, the "filter()" method 
 * can simply be called with the newest input, and the most recent filtered output will be returned.
 * 
 * @author Trevor
 */
public class AverageFilter {
	
	int filterLength; // length of filter 
	double[] circBuffer; // circular buffer to hold all values
	double sum; // hold sum of all numbers in the buffer at all times.
	int index; // index of oldest input in the buffer
	
	
	/**
	 * initialize all things needed for the averaging filter.
	 * @param length How many inputs to store and average
	 * @param initVal Initial value to use for the filter
	 * 		  entire buffer gets filled with this initially
	 */
	public AverageFilter(int length, double initVal){
		index = 0;
		filterLength = length;
		//Allocate buffer
		circBuffer = new double[filterLength];
		//Fill the buffer with the initial value
		Arrays.fill(circBuffer, initVal);
		//Calculate initial sum
		sum = filterLength*initVal;
	}
	
	/**
	 * Filter - add a new input to the filter and get the current output from the filter
	 * @param input new value to add to the filter
	 * @return the present output filtered value
	 */
	public double filter(double input){
		//use the running-sum method to compute the average. Better cuz it's O(1) time and O(n) memory.
		//Computing the sum from scratch is O(n) for both.
		sum -= circBuffer[index];
		
		circBuffer[index] = input;
		sum += circBuffer[index];
		//Update index (circularly due to buffer)
		index = (index + 1)%filterLength;
		//Return average = sum/length
		return sum/filterLength;
	}

}