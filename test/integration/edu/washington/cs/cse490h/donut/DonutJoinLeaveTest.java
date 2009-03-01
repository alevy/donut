package edu.washington.cs.cse490h.donut;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.util.DonutTestCase;
import edu.washington.cs.cse490h.donut.util.DonutTestRunner;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class DonutJoinLeaveTest {

    @Test
    public void testJoinTwoPositives() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(100, 1124);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(2000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                for (int i = 0; i < 11; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }
                
                assertEquals(node1.getTNode(), node0.getPredecessor());
                
                for (int i = 11; i < Node.KEYSPACESIZE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node0.getFinger(i));
                }

                for (int i = 0; i < Node.KEYSPACESIZE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                }
                
                assertEquals(node0.getTNode(), node1.getPredecessor());
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinTwoPositiveAndNegative() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0, -0x2000000000000000L);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(3000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                for (int i = 0; i < Node.KEYSPACESIZE; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }
                
                assertEquals(node0.getTNode(), node1.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 2; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                }

                for (int i = Node.KEYSPACESIZE - 2; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node1.getFinger(i));
                }
                
                assertEquals(node1.getTNode(), node0.getPredecessor());
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourDistributedInOrderLeaveOne() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(2000).join(2, 0);
        donutTestRunner.addEvent(3000).join(3, 0);
        donutTestRunner.addEvent(5000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);
                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node2.getTNode(), node0
                        .getFinger(Node.KEYSPACESIZE - 1));
                assertEquals(node3.getTNode(), node0.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node3.getTNode(), node1
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node0.getTNode(), node1.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node0.getTNode(), node2
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node1.getTNode(), node2.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node1.getTNode(), node3
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node2.getTNode(), node3.getPredecessor());
            }
        });
        donutTestRunner.addEvent(8000).leave(2);
        donutTestRunner.addEvent(12000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node3 = donutTestRunner.node(3);
                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node3.getTNode(), node0
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node3.getTNode(), node0.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node1.getFinger(i));
                }
                
                assertEquals(node0.getTNode(), node1.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node1.getTNode(), node3
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node1.getTNode(), node3.getPredecessor());
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourReverseOrder() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(2, 2);
        donutTestRunner.addEvent(1000).join(0, 2);
        donutTestRunner.addEvent(2000).join(3, 0);
        donutTestRunner.addEvent(3000).join(1, 2);
        donutTestRunner.addEvent(5000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);
                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node2.getTNode(), node0
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node3.getTNode(), node0.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node3.getTNode(), node1
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node0.getTNode(), node1.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node0.getTNode(), node2
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node1.getTNode(), node2.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node1.getTNode(), node3
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node2.getTNode(), node3.getPredecessor());
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testTwoSimultaneously() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(1000).join(1, 0);
        donutTestRunner.addEvent(2000).join(2, 0);
        donutTestRunner.addEvent(3000).join(3, 0);
        donutTestRunner.addEvent(5000).leave(2);
        donutTestRunner.addEvent(5000).leave(1);
        donutTestRunner.addEvent(7000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node3 = donutTestRunner.node(3);
                for (int i = 0; i < Node.KEYSPACESIZE; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node0.getFinger(i));
                }
                
                assertEquals(node3.getTNode(), node0.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }
                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node3.getTNode(), node3
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node0.getTNode(), node3.getPredecessor());
            }
        });
        donutTestRunner.run();
    }
    
    @Test
    public void testJoinFourLeaveOneThenRejoin() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(2, 2);
        donutTestRunner.addEvent(1000).join(0, 2);
        donutTestRunner.addEvent(2000).join(3, 0);
        donutTestRunner.addEvent(3000).join(1, 2);
        donutTestRunner.addEvent(6000).leave(0);
        donutTestRunner.addEvent(9000).joinNewNode(2, "node4", 0x0L);
        donutTestRunner.addEvent(12000).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(4);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);
                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node2.getTNode(), node0
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node3.getTNode(), node0.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node3.getTNode(), node1
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node0.getTNode(), node1.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node0.getTNode(), node2
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node1.getTNode(), node2.getPredecessor());

                for (int i = 0; i < Node.KEYSPACESIZE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }

                assertEquals("Incorrect finger " + (Node.KEYSPACESIZE - 1), node1.getTNode(), node3
                        .getFinger(Node.KEYSPACESIZE - 1));
                
                assertEquals(node2.getTNode(), node3.getPredecessor());
            }
        });
        donutTestRunner.run();
    }

}
