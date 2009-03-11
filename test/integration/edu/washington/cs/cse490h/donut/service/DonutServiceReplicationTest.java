package edu.washington.cs.cse490h.donut.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.washington.cs.cse490h.donut.service.thrift.Constants;
import edu.washington.cs.cse490h.donut.service.thrift.DataPair;
import edu.washington.cs.cse490h.donut.service.thrift.EntryKey;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.util.DonutClosure;
import edu.washington.cs.cse490h.donut.util.DonutTestCase;
import edu.washington.cs.cse490h.donut.util.DonutTestRunner;

/**
 * @author alevy
 */
public class DonutServiceReplicationTest {

    @Test
    public void testLeave() throws Exception {
        // This test relies on the successor list size being of size 3. If that size
        // changes, the number of nodes tested here has to change.
        assertEquals(3, Constants.SUCCESSOR_LIST_SIZE);

        final EntryKey key21 = new EntryKey(new KeyId(21), "key21");
        final DataPair value21 = new DataPair("value21".getBytes(), Constants.SUCCESSOR_LIST_SIZE);
        final EntryKey key22 = new EntryKey(new KeyId(22), "key22");
        final DataPair value22 = new DataPair("value22".getBytes(), Constants.SUCCESSOR_LIST_SIZE);

        final DonutTestRunner donutTestRunner = new DonutTestRunner(30, 40, 50, 60, 70);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(2000).join(2, 0);
        donutTestRunner.addEvent(3000).join(3, 0);
        donutTestRunner.addEvent(4000).join(4, 0);
        donutTestRunner.addEvent(5000).setClosure(new DonutClosure() {
            public void run() throws Exception {
                donutTestRunner.iface(0).put(key22, value22.getData());
                donutTestRunner.iface(0).put(key21, value21.getData());
            }
        });
        donutTestRunner.addEvent(6000).leave(0);
        donutTestRunner.addEvent(7000).test(new DonutTestCase() {
            public void test() throws Exception {
                assertEquals(value21.getData(), donutTestRunner.iface(1).get(key21));
                assertEquals(value22.getData(), donutTestRunner.iface(1).get(key22));

                //assertEquals(value21, donutTestRunner.service(4).get(key21));
                //assertEquals(value22, donutTestRunner.service(4).get(key22));
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoin() throws Exception {
        final EntryKey key21 = new EntryKey(new KeyId(21), "key21");
        final DataPair value21 = new DataPair("value21".getBytes(), Constants.SUCCESSOR_LIST_SIZE);
        final EntryKey key22 = new EntryKey(new KeyId(22), "key22");
        final DataPair value22 = new DataPair("value22".getBytes(), Constants.SUCCESSOR_LIST_SIZE);

        final DonutTestRunner donutTestRunner = new DonutTestRunner(20, 30, 40, 50, 60, 25);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(2000).join(2, 0);
        donutTestRunner.addEvent(3000).join(3, 0);
        donutTestRunner.addEvent(4000).join(4, 0);
        donutTestRunner.addEvent(5000).setClosure(new DonutClosure() {
            public void run() throws Exception {
                donutTestRunner.iface(1).put(key22, value22.getData());
                donutTestRunner.iface(1).put(key21, value21.getData());
            }
        });
        donutTestRunner.addEvent(6000).join(5, 0);
        donutTestRunner.addEvent(7000).test(new DonutTestCase() {
            public void test() {
                assertEquals(value21, donutTestRunner.service(5).get(key21));
                assertEquals(value22, donutTestRunner.service(5).get(key22));

                assertEquals(null, donutTestRunner.service(0).get(key21));
            }
        });
        donutTestRunner.run();
    }
}
