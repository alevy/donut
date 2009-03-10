package edu.washington.cs.cse490h.donut;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.util.DonutTestCase;
import edu.washington.cs.cse490h.donut.util.DonutTestRunner;
import edu.washington.edu.cs.cse490h.donut.service.Constants;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy, jprouty, rylan
 */
public class DonutJoinLeaveTest {
    private static final int DEFAULT_TIME_TO_FIX_FINGERS = (int) (Constants.FIX_FINGERS_INTERVAL * 64 * 2);

    @Test
    public void testJoinOneNodeRing() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);

                assertEquals(node0.getTNode(), node0.getPredecessor());

                // All of node0's fingers should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node0.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), node0.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinTwoPositives() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(100, 1124);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).join(1, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);

                assertEquals(node1.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());

                // node0's first 11 fingers should be pointing to node1
                // The rest should be at node0 (itself)
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 11)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node0.getFinger(i));
                }

                // All of node1's fingers should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node1.getTNode() : node0.getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node1.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinTwoPositiveAndNegative() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0, -0x2000000000000000L);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).join(1, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);

                assertEquals(node1.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());

                // All of node0's fingers should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                // node1's first 62 fingers should be pointing to node0
                // The rest should be at node1 (itself)
                for (int i = 0; i < Constants.KEY_SPACE - 2; ++i) {
                    if (i < 62)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node1.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node1.getTNode() : node0.getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node1.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinTwoPositiveAndNegativeAllAtOnce() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0, -0x2000000000000000L);
        donutTestRunner.addEvent(0).join(1, 1);
        donutTestRunner.addEvent(0).join(0, 1);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);

                assertEquals(node1.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());

                // All of node0's fingers should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                // node1's first 62 fingers should be pointing to node0
                // The rest should be at node1 (itself)
                for (int i = 0; i < Constants.KEY_SPACE - 2; ++i) {
                    if (i < 62)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node1.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node1.getTNode() : node0.getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node1.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinTwoPositiveAndNegativeAllAtOnceWithEachOtherAsKnown() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0, -0x2000000000000000L);
        donutTestRunner.addEvent(0).join(1, 0);
        donutTestRunner.addEvent(0).join(0, 1);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);

                assertEquals(node1.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());

                // All of node0's fingers should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                }

                // node1's first 62 fingers should be pointing to node0
                // The rest should be at node1 (itself)
                for (int i = 0; i < Constants.KEY_SPACE - 2; ++i) {
                    if (i < 62)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node1.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node1.getTNode() : node0.getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node1.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourAllAtOnce() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        // Joining nodes in different orders and by linking different known hosts
        donutTestRunner.addEvent(0).join(3, 3);
        donutTestRunner.addEvent(0).join(2, 3);
        donutTestRunner.addEvent(0).join(0, 3);
        donutTestRunner.addEvent(0).join(1, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);

                // node0 -> node1 -> node2 -> node3 -> ... (node0)
                assertEquals(node3.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());
                assertEquals(node1.getTNode(), node2.getPredecessor());
                assertEquals(node2.getTNode(), node3.getPredecessor());

                // node0's first 63 fingers should be pointing to node1
                // node0's last finger should be pointing to node2
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node0.getFinger(i));
                }

                // node1's first 63 fingers should be pointing to node2
                // node1's last finger should be pointing to node3
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node1.getFinger(i));
                }

                // node2's first 63 fingers should be pointing to node3
                // node2's last finger should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node2.getFinger(i));
                }

                // node3's first 63 fingers should be pointing to node3
                // node3's last finger should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node3.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((1 + i) % 4).getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((2 + i) % 4).getTNode());
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((3 + i) % 4).getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((4 + i) % 4).getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourOutOfOrder() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        // Joining nodes in different orders and by linking different known hosts
        donutTestRunner.addEvent(0).join(2, 2);
        donutTestRunner.addEvent(0).join(0, 2);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(0).join(1, 2);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);

                // node0 -> node1 -> node2 -> node3 -> ... (node0)
                assertEquals(node3.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());
                assertEquals(node1.getTNode(), node2.getPredecessor());
                assertEquals(node2.getTNode(), node3.getPredecessor());

                // node0's first 63 fingers should be pointing to node1
                // node0's last finger should be pointing to node2
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node0.getFinger(i));
                }

                // node1's first 63 fingers should be pointing to node2
                // node1's last finger should be pointing to node3
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node1.getFinger(i));
                }

                // node2's first 63 fingers should be pointing to node3
                // node2's last finger should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node2.getFinger(i));
                }

                // node3's first 63 fingers should be pointing to node3
                // node3's last finger should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node3.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((1 + i) % 4).getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((2 + i) % 4).getTNode());
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((3 + i) % 4).getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((4 + i) % 4).getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourLeaveOne() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 0);
        donutTestRunner.addEvent(0).join(1, 0);
        donutTestRunner.addEvent(0).join(2, 0);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);

                // node0 -> node1 -> node2 -> node3 -> ... (node0)
                assertEquals(node3.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());
                assertEquals(node1.getTNode(), node2.getPredecessor());
                assertEquals(node2.getTNode(), node3.getPredecessor());

                // node0's first 63 fingers should be pointing to node1
                // node0's last finger should be pointing to node2
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node0.getFinger(i));
                }

                // node1's first 63 fingers should be pointing to node2
                // node1's last finger should be pointing to node3
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node1.getFinger(i));
                }

                // node2's first 63 fingers should be pointing to node3
                // node2's last finger should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node2.getFinger(i));
                }

                // node3's first 63 fingers should be pointing to node3
                // node3's last finger should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node3.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((1 + i) % 4).getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((2 + i) % 4).getTNode());
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((3 + i) % 4).getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((4 + i) % 4).getTNode());
                }
            }
        });
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).leave(3);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 3).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);

                // node0 -> node1 -> node2 -> ... (node0)
                assertEquals(node2.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node1.getPredecessor());
                assertEquals(node1.getTNode(), node2.getPredecessor());

                // node0's first 63 fingers should be pointing to node1
                // node0's last finger should be pointing to node2
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node0.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node0.getFinger(i));
                }

                // node1's first 63 fingers should be pointing to node2
                // node1's last finger should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node0.getTNode(), node1.getFinger(i));
                }

                // All of node2's fingers should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node2.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(donutTestRunner.node((1 + i) % 3).getTNode(), list.get(i));
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(donutTestRunner.node((2 + i) % 3).getTNode(), list.get(i));
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(donutTestRunner.node((3 + i) % 3).getTNode(), list.get(i));
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourLeaveTwo() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 1);
        donutTestRunner.addEvent(0).join(1, 2);
        donutTestRunner.addEvent(0).join(2, 3);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(2);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).leave(1);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 3).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node3 = donutTestRunner.node(3);

                assertEquals(node3.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node3.getPredecessor());

                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node0.getFinger(i));
                }

                for (int i = 0; i < Constants.KEY_SPACE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }
                assertEquals("Incorrect finger " + (Constants.KEY_SPACE - 1), node3.getTNode(),
                        node3.getFinger(Constants.KEY_SPACE - 1));

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node3.getTNode() : node0.getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node3.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourLeaveTwoNonConigousAtTheSameTime() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 1);
        donutTestRunner.addEvent(0).join(1, 2);
        donutTestRunner.addEvent(0).join(2, 3);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(1);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(3);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node2 = donutTestRunner.node(2);

                assertEquals(node2.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node2.getPredecessor());

                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node2.getTNode(), node0.getFinger(i));
                }

                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node2.getFinger(i));
                }

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node2.getTNode() : node0.getTNode());
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node2.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourLeaveTwoConigousAtTheSameTime() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(0, 1);
        donutTestRunner.addEvent(0).join(1, 2);
        donutTestRunner.addEvent(0).join(2, 3);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(1);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(2);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).test(new DonutTestCase() {
            public void test() {
                Node node0 = donutTestRunner.node(0);
                Node node3 = donutTestRunner.node(3);

                assertEquals(node3.getTNode(), node0.getPredecessor());
                assertEquals(node0.getTNode(), node3.getPredecessor());

                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    assertEquals("Incorrect finger " + i, node3.getTNode(), node0.getFinger(i));
                }

                for (int i = 0; i < Constants.KEY_SPACE - 1; ++i) {
                    assertEquals("Incorrect finger " + i, node0.getTNode(), node3.getFinger(i));
                }
                assertEquals("Incorrect finger " + (Constants.KEY_SPACE - 1), node3.getTNode(),
                        node3.getFinger(Constants.KEY_SPACE - 1));

                List<TNode> list = node0.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node3.getTNode() : node0.getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), (i % 2 == 0) ? node0.getTNode() : node3.getTNode());
                }
            }
        });
        donutTestRunner.run();
    }

    @Test
    public void testJoinFourLeaveOneThenRejoin() throws Exception {
        final DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0x4000000000000000L,
                0x8000000000000000L, 0xC000000000000000L);
        donutTestRunner.addEvent(0).join(2, 2);
        donutTestRunner.addEvent(0).join(0, 2);
        donutTestRunner.addEvent(0).join(3, 0);
        donutTestRunner.addEvent(0).join(1, 2);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS).leave(0);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 2).joinNewNode(2, "node4", 0x0L);
        donutTestRunner.addEvent(DEFAULT_TIME_TO_FIX_FINGERS * 3).test(new DonutTestCase() {
            public void test() {
                Node node4 = donutTestRunner.node(4);
                Node node1 = donutTestRunner.node(1);
                Node node2 = donutTestRunner.node(2);
                Node node3 = donutTestRunner.node(3);

                // node4 -> node1 -> node2 -> node3 -> ... (node4)
                assertEquals(node3.getTNode(), node4.getPredecessor());
                assertEquals(node4.getTNode(), node1.getPredecessor());
                assertEquals(node1.getTNode(), node2.getPredecessor());
                assertEquals(node2.getTNode(), node3.getPredecessor());

                // node0's first 63 fingers should be pointing to node1
                // node0's last finger should be pointing to node2
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node4.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node4.getFinger(i));
                }

                // node1's first 63 fingers should be pointing to node2
                // node1's last finger should be pointing to node3
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node2.getTNode(), node1.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node1.getFinger(i));
                }

                // node2's first 63 fingers should be pointing to node3
                // node2's last finger should be pointing to node0
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node3.getTNode(), node2.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node4.getTNode(), node2.getFinger(i));
                }

                // node3's first 63 fingers should be pointing to node3
                // node3's last finger should be pointing to node1
                for (int i = 0; i < Constants.KEY_SPACE; ++i) {
                    if (i < 63)
                        assertEquals("Incorrect finger " + i, node4.getTNode(), node3.getFinger(i));
                    else
                        assertEquals("Incorrect finger " + i, node1.getTNode(), node3.getFinger(i));
                }

                List<TNode> list = node4.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((i) % 4 + 1).getTNode());
                }

                list = node1.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((1 + i) % 4 + 1).getTNode());
                }

                list = node2.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((2 + i) % 4 + 1).getTNode());
                }

                list = node3.getSuccessorList();
                for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; ++i) {
                    assertEquals(list.get(i), donutTestRunner.node((3 + i) % 4 + 1).getTNode());
                }
            }
        });
        donutTestRunner.run();
    }
}
