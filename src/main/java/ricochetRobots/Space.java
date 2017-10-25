package ricochetRobots;

public class Space {
    
    public Space() {
        _occupant = '\u0000';
        _goal = '\u0000';
        _wallNorth = false;
        _wallSouth = false;
        _wallEast = false; 
        _wallWest = false;
    }
    
    // MODIFIERS
    public void setOccupant(char _occupant) { this._occupant = _occupant; }
    public void clearOccupant() { this._occupant = '\u0000'; }
    public void setGoal(char _goal) { this._goal = _goal; }
    public void setWallNorth(boolean present) { _wallNorth = present; }
    public void setWallSouth(boolean present) { _wallSouth = present; }
    public void setWallEast(boolean present) { _wallEast = present; }
    public void setWallWest(boolean present) { _wallWest = present; }
    
    // ACCESSORS
    public char getOccupant() { return _occupant; }
    public boolean isOccupied() { return _occupant != '\u0000'; }
    public boolean isGoal() { return _goal != '\u0000'; }
    public char getGoal() { return _goal; }
    public boolean getWallNorth() { return _wallNorth; }
    public boolean getWallSouth() { return _wallSouth; }
    public boolean getWallEast() { return _wallEast; }
    public boolean getWallWest() { return _wallWest; }

    public boolean hasWall(char dir) {
        switch(dir) {
            case 'N': return this._wallNorth;
            case 'S': return this._wallSouth;
            case 'E': return this._wallEast;
            case 'W': return this._wallWest;
        }
        return false;
    }

    // MEMBERS
    private char _occupant;
    private char _goal;
    private boolean _wallNorth;
    private boolean _wallSouth;
    private boolean _wallEast;
    private boolean _wallWest;

}
