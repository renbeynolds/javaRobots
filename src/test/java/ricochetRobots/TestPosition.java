package ricochetRobots;

import static org.junit.Assert.*;

import ricochetRobots.Position;

import org.junit.Before;
import org.junit.Test;

public class TestPosition {

    private Position p1;
    private Position p2;

    @Before
    public void setUp() throws Exception {
        p1 = new Position(0, 1);
        p2 = new Position(p1);
    }

    @Test
    public void testConstructor() {
        assertEquals(p1.x, 0);
        assertEquals(p1.y, 1);
        assertEquals(p2.x, 0);
        assertEquals(p2.y, 1);
    }

}
