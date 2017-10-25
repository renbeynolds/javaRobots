package ricochetRobots;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

public class Board {

    public Board(int width, int height) {
        
        _width = width;
        _height = height;
        _spaces = new Space[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                _spaces[i][j] = new Space();
            }
        }
        _robots = new TreeMap<Character, Position>();
        _goals = new TreeMap<Character, Position>();

    }
    
    // MODIFIERS
    public void addPerimeterWalls() {
        for(int i = 0; i < _width; i++) {
            _spaces[i][0].setWallNorth(true);
            _spaces[i][_height-1].setWallSouth(true);
        }
        for(int j = 0; j < _height; j++) {
            _spaces[0][j].setWallWest(true);
            _spaces[_width-1][j].setWallEast(true);
        }
    }
    
    public void addWall(char dir, Position p) {
        switch(dir) {
            case 'N':
                _spaces[p.x][p.y].setWallNorth(true);
                if(p.y-1 >= 0) { _spaces[p.x][p.y-1].setWallSouth(true); }
                break;
            case 'S':
                _spaces[p.x][p.y].setWallSouth(true);
                if(p.y+1 < _height) { _spaces[p.x][p.y+1].setWallNorth(true); }
                break;
            case 'E':
                _spaces[p.x][p.y].setWallEast(true);
                if(p.x+1 < _width) { _spaces[p.x+1][p.y].setWallWest(true); }
                break;
            case 'W':
                _spaces[p.x][p.y].setWallWest(true);
                if(p.x-1 >= 0) { _spaces[p.x-1][p.y].setWallEast(true); }
                break;
        }
    }

    public void addRobot(char id, Position p) {
        _robots.put(id, p);
        _spaces[p.x][p.y].setOccupant(id);
    }
    
    public void addGoal(char id, Position p) {
        _goals.put(id, p);
        _spaces[p.x][p.y].setGoal(id); 
    }
    
    public Position moveRobot(char id, char dir) {
        Position p = _robots.get(id);
        Position old_p = new Position(p);
        _spaces[p.x][p.y].clearOccupant();
        while(!_spaces[p.x][p.y].hasWall(dir) && !getSpace(dir, p).isOccupied()) {
            switch (dir) {
                case 'N': p.y -= 1; break;
                case 'S': p.y += 1; break; 
                case 'E': p.x += 1; break;
                case 'W': p.x -= 1; break;
            }
        }
        _robots.put(id, p);
        _spaces[p.x][p.y].setOccupant(id); 
        return old_p;
    }
    
    public void setConfig(int config) {
        int shift = 24; 
        for(char bot_id : _robots.keySet()) {
            int pos = (config >> shift) & 0xFF;
            int x = pos >> 4;
            int y = pos & 0x0F;
            setRobotPosition(bot_id, new Position(x, y));
            shift -= 8;
        }
    }
    
    public void setRobotPosition(char id, Position new_p) {
        Position old_p = _robots.get(id);
        _spaces[old_p.x][old_p.y].clearOccupant();
        _spaces[new_p.x][new_p.y].setOccupant(id);
        _robots.put(id, new_p); 
    }

    // ACCESSORS
    public int getWidth() { return _width; }
    public int getHeight() { return _height; }
    public Position getRobotPosition(char id) { return _robots.get(id); }
    public Map<Character, Position> getRobots() { return _robots; }
    public Map<Character, Position> getGoals() { return _goals; }
    public Space getSpace(Position p) { return _spaces[p.x][p.y]; }
    
    public Space getSpace(char dir, Position p) {
        switch(dir) {
            case 'N': return _spaces[p.x][p.y-1];
            case 'S': return _spaces[p.x][p.y+1];
            case 'E': return _spaces[p.x+1][p.y];
            case 'W': return _spaces[p.x-1][p.y];
        }
        return null;
    }

    public int getConfig() {
        int config = 0;
        int shift = 24;
        for(Position p : _robots.values()) {
            int pos = (p.x << 4) | p.y;
            config = config | (pos << shift);
            shift -= 8;
        }
        return config;
    }
    
    public boolean solved() {
        Iterator<Map.Entry<Character, Position>> it = _goals.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Character, Position> pair = (Map.Entry<Character, Position>)it.next();
            Position p = pair.getValue();
            if(pair.getKey() != _spaces[p.x][p.y].getOccupant()) {
                return false; 
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int j=0; j < _height; j++) {
            for(int i=0; i < _width; i++) {
                s += ('-' + (_spaces[i][j].getWallNorth()?"---":"   "));
            }
            s += "-\n";
            for(int i=0; i < _width; i++) {
                if(_spaces[i][j].isOccupied()) {
                    char bot_id = _spaces[i][j].getOccupant();
                    s += _spaces[i][j].getWallWest()?("| "+bot_id+" "):("  "+bot_id+" ");
                } else if(_spaces[i][j].isGoal()){
                    String goal_id = String.valueOf(_spaces[i][j].getGoal()).toLowerCase();
                    s += _spaces[i][j].getWallWest()?("| "+goal_id+" "):("  "+goal_id+" ");
                } else {
                    s += _spaces[i][j].getWallWest()?("|   "):("    ");
                }
            }
            s += "|\n";
        }
        s += new String(new char[(4*_width)+1]).replace("\0", "-");
        return s;
    }
    
    // MEMBERS 
    private int _width;
    private int _height;
    private Space[][] _spaces;
    private Map<Character, Position> _robots; 
    private Map<Character, Position> _goals;
    
}
