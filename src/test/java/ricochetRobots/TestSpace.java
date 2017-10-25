package ricochetRobots;

import static org.junit.Assert.*;

import ricochetRobots.Space;

import org.junit.Before;
import org.junit.Test;

public class TestSpace {

    private Space s;

    @Before
    public void setUp() throws Exception {
        s = new Space();
    }

    @Test
    public void testConstructor() {
        assertEquals(s.isOccupied(), false);
        assertEquals(s.isGoal(), false);
        assertEquals(s.getWallNorth(), false);
        assertEquals(s.getWallSouth(), false);
        assertEquals(s.getWallEast(), false);
        assertEquals(s.getWallWest(), false);
    }

    @Test
    public void testOccupant() {
        s.setOccupant('R');
        assertEquals(s.isOccupied(), true);
        assertEquals(s.getOccupant(), 'R');
        s.setOccupant('G');
        assertEquals(s.getOccupant(), 'G');
    }

    @Test
    public void testWalls() {
        assertEquals(s.getWallNorth(), false);
        assertEquals(s.getWallSouth(), false);
        assertEquals(s.getWallEast(), false);
        assertEquals(s.getWallWest(), false);
        assertEquals(s.hasWall('N'), false);
        assertEquals(s.hasWall('S'), false);
        assertEquals(s.hasWall('E'), false);
        assertEquals(s.hasWall('W'), false);
        s.setWallNorth(true);
        assertEquals(s.getWallNorth(), true);
        assertEquals(s.hasWall('N'), true);
        s.setWallSouth(true);
        assertEquals(s.getWallSouth(), true);
        assertEquals(s.hasWall('S'), true);
        s.setWallEast(true);
        assertEquals(s.getWallEast(), true);
        assertEquals(s.hasWall('E'), true);
        s.setWallWest(true);
        assertEquals(s.getWallWest(), true);
        assertEquals(s.hasWall('W'), true);
    }

    @Test
    public void testGoals() {
        assertEquals(s.isGoal(), false);
        s.setGoal('R');
        assertEquals(s.isGoal(), true);
        assertEquals(s.getGoal(), 'R');
    }

}
