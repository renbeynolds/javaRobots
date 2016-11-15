package ricochetRobots;

public class Position {

    public Position(int X, int Y) {
        x = X;
        y = Y;
    }
    
    public Position(Position other) {
        this.x = other.x;
        this.y = other.y;
    }

    public int x;
    public int y;
}
