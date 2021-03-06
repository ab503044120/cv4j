package com.cv4j.core.filters;

import com.cv4j.core.datamodel.ColorProcessor;
import com.cv4j.core.datamodel.ImageProcessor;

public class CarveFilter implements CommonFilter {
	private boolean isCarve;
	
	public CarveFilter()
	{
		isCarve = true;
	}
	
	public void setCarve(boolean carve)
	{
		isCarve =carve;
	}
	@Override
	public ImageProcessor filter(ImageProcessor src) {
		int width = src.getWidth();
		int height = src.getHeight();

		byte[] R = ((ColorProcessor)src).getRed();
		byte[] G = ((ColorProcessor)src).getGreen();
		byte[] B = ((ColorProcessor)src).getBlue();
		byte[][] output = new byte[3][R.length];

        int index = 0;
        for(int row=1; row<height-1; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=1; col<width-1; col++) {
        		// Index of the pixel in the array      
        		index = row * width + col;
        		int bidx = row * width + (col - 1);
        		int aidx = row * width + (col + 1);

                int br = R[bidx] & 0xff;
                int bg = G[bidx] & 0xff;
                int bb = B[bidx] & 0xff;

                int ar = R[aidx] & 0xff;
                int ag = G[aidx] & 0xff;
                int ab = B[aidx] & 0xff;
       
                // calculate new RGB value 
                if(isCarve)
                {
	                tr = ar - br + 128;
	                tg = ag - bg + 128;
	                tb = ab - bb + 128; 
                }
                else
                {
	                tr = br - ar + 128;
	                tg = bg - ag + 128;
	                tb = bb - ab + 128; 
                }
				output[0][index] = (byte)clamp(tr);
				output[1][index] = (byte)clamp(tg);
				output[2][index] = (byte)clamp(tb);
        	}
        }
		((ColorProcessor) src).putRGB(output[0], output[1], output[2]);
		output = null;
		return src;
	}

	public static int clamp(int p) {
		return p < 0 ? 0 : ((p > 255) ? 255 : p);
	}
}
