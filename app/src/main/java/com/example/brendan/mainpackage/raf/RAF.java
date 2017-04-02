package com.example.brendan.mainpackage.raf;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by brendan on 3/29/17.
 */

public class RAF {

    Double temp;
    Double prcp;

    void read(RandomAccessFile raf) throws IOException {
        temp = raf.readDouble();
        prcp = raf.readDouble();
    }

    void write(RandomAccessFile raf) throws IOException{
        raf.writeDouble(temp);
        raf.writeDouble(prcp);
    }
    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getPrcp() {
        return prcp;
    }

    public void setPrcp(Double prcp) {
        this.prcp = prcp;
    }
}
