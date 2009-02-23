package edu.washington.cs.cse490h.donut.util;

public abstract class DonutTestCase extends DonutClosure {
    public abstract void test();
    
    @Override
    public void run() {
        test();
    }
}