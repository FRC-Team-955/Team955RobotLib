package util.math;

import java.util.Arrays;

/**
 * Class which defines a simple median filter. After
 * initilization, the "filter()" method can simply be called with the newest
 * input, and the most recent filtered output will be returned.
 * 
 * @author Trevor
 *
 */
public class MedianFilter {

	int filterLength; // length of filter
	double[] circBuffer; // circular buffer to hold all values
	int index; // "pointer" to the starting index in the buffer

	/**
	 * initialize all things needed for the averaging filter.
	 * 
	 * @param length How many inputs to store and average
	 * @param initVal Initial value to use for the filter
	 * 		  entire buffer gets filled with this initially
	 */
	public MedianFilter(int length, double initVal) {
		index = 0;
		filterLength = length;
		// Allocate buffer
		circBuffer = new double[filterLength];
		// Fill the buffer with the initial value
		Arrays.fill(circBuffer, initVal);
	}

	/**
	 * add a new input to the filter and get the current output from the filter
	 * 
	 * @param input new value to add to the filter
	 * @return the present output filtered value
	 */
	public double filter(double input) {
		double[] tempBuffer = new double[filterLength];

		// put new value into the buffer
		circBuffer[index] = input;
		// Update index (circularly since circular buffer)
		index = (index + 1) % filterLength;

		// Copy the buffer to a temporary spot
		System.arraycopy(circBuffer, 0, tempBuffer, 0, circBuffer.length);

		// sort the temporary array
		Arrays.sort(tempBuffer);

		// calculate median based on the middle of the sorted array
		int middle = filterLength / 2;
		if (filterLength % 2 == 1) { // case - length is odd so a middle point actually exists
			return tempBuffer[middle];
		} else { // case - length is even so the middle is the average of the two middle samples.
			return (tempBuffer[middle - 1] + tempBuffer[middle]) / 2;
		}

	}

}