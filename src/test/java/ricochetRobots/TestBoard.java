package ricochetRobots;

import static org.junit.Assert.*;

import ricochetRobots.Board;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {

    private Board b;

    @Before
    public void setUp() throws Exception {
        b = new Board(2, 2);
    }

    @Test
    public void testConstructor() {
        assertEquals(b.getWidth(), 2);
        assertEquals(b.getHeight(), 2);
        assertEquals(b.solved(), true);
    }

}
