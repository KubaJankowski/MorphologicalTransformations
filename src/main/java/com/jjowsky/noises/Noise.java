package com.jjowsky.noises;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Noise {
    public static double generateGaussian() {
        double temp1d;
        double temp2d = 0;
        double resultd;
        int pui = 1; //std
        Random rand = new Random();

        while (pui > 0) {
            temp2d = ThreadLocalRandom.current().nextInt(0, 32767) / 32767d;

            if (temp2d == 0)
                pui = 1;
            else
                pui = -1;
        }

        temp1d = Math.cos((2.0 * Math.PI) * ThreadLocalRandom.current().nextInt(32767) / 32767d);
        resultd = Math.sqrt(-2.0 * Math.log(temp2d)) * temp1d;

        return  resultd;
    }
}

