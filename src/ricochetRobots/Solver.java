package ricochetRobots;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.ArrayList;

import java.awt.*;
import javax.swing.*;

public class Solver {
    
    private class MapDraw extends JPanel {
        
        public MapDraw(Board board) {
            _board = board;
            _xoffset = (500 - (30*board.getWidth()))/2;
            _yoffset = (500 - (30*board.getHeight()))/2;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            drawGrid(g2);
            drawWalls(g2);
            drawGoals(g2);
            drawRobots(g2);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }
        
        public void showSolution(ArrayList<Integer> steps) {
            for(int config: steps) {
                _board.setConfig(config);
                this.repaint();
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        private void drawGrid(Graphics2D g2) {
            g2.setStroke(new BasicStroke(1));
            g2.setPaint(Color.GRAY);
            for(int i=0; i <= _board.getWidth(); i++) {
                g2.drawLine(_xoffset+30*i, _yoffset,
                    _xoffset+30*i, _yoffset+30*_board.getHeight()); 
            }
            for(int j=0; j <= _board.getHeight(); j++) {
                g2.drawLine(_xoffset, _yoffset+30*j,
                    _xoffset+30*_board.getWidth(), _yoffset+30*j); 
            }
        }
        
        private void drawWalls(Graphics2D g2) {
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(Color.BLACK);
            g2.drawLine(_xoffset, _yoffset, _xoffset, _yoffset+30*_board.getHeight());
            g2.drawLine(_xoffset, _yoffset, _xoffset+30*_board.getWidth(), _yoffset);
            for(int i=0; i < _board.getWidth(); i++) {
                for(int j=0; j < _board.getHeight(); j++) {
                    Space s = _board.getSpace(new Position(i, j));
                    if(s.getWallSouth()) {
                        g2.drawLine(_xoffset+30*(i), _yoffset+30*(j+1),
                            _xoffset+30*(i+1), _yoffset+30*(j+1));
                    }
                    if(s.getWallEast()) {
                        g2.drawLine(_xoffset+30*(i+1), _yoffset+30*(j),
                            _xoffset+30*(i+1), _yoffset+30*(j+1));
                    }
                }
            }
        }
        
        private void drawGoals(Graphics2D g2) {
            for(Map.Entry<Character, Position> goal: _board.getGoals().entrySet()) {
                Position p = goal.getValue();
                switch(goal.getKey()) {
                    case 'R': g2.setPaint(Color.RED); break;
                    case 'G': g2.setPaint(Color.GREEN); break;
                    case 'B': g2.setPaint(Color.BLUE); break;
                    case 'Y': g2.setPaint(Color.YELLOW); break;
                }
                g2.drawRect(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
                g2.fillRect(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
            }
        }
        
        private void drawRobots(Graphics2D g2) {
            for(char bot_id: _board.getRobots().keySet()) {
                Position p = _board.getRobotPosition(bot_id);
                switch(bot_id) {
                    case 'R': g2.setPaint(Color.RED); break;
                    case 'G': g2.setPaint(Color.GREEN); break;
                    case 'B': g2.setPaint(Color.BLUE); break;
                    case 'Y': g2.setPaint(Color.YELLOW); break;
                }
                g2.fillOval(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
                g2.setStroke(new BasicStroke(1));
                g2.setPaint(Color.BLACK);
                g2.drawOval(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
            }
        }
        
        private Board _board;
        int _xoffset;
        int _yoffset;
        
    }
    
    
    public static ArrayList<Integer> solve(Board board) {

        HashSet<Integer> prev_configs = new HashSet<Integer>();
        Map<Integer, Integer> steps = new HashMap<Integer, Integer>();
        Queue<Integer> configs_queue = new LinkedList<Integer>();
        configs_queue.add(board.getConfig());
        int prev_config = 0;
        int initial_config = board.getConfig();

        while(!board.solved() && !configs_queue.isEmpty()) {
            
            prev_config = configs_queue.poll();
            board.setConfig(prev_config);

            for(char bot_id: board.getRobots().keySet()) {
                for(char dir: new char[] {'N', 'S', 'E', 'W'}) {

                    Position p = board.moveRobot(bot_id, dir);
                    if(board.solved()) { break; }
                    int new_config = board.getConfig();
                    if(!prev_configs.contains(new_config)) {
                        prev_configs.add(new_config); 
                        configs_queue.add(new_config);
                        steps.put(new_config, prev_config);
                    }
                    board.setRobotPosition(bot_id, p);

                }
            }

        }

        ArrayList<Integer> configs = new ArrayList<Integer>();
        configs.add(board.getConfig());
        while(prev_config != initial_config) {
            configs.add(0, prev_config);
            prev_config = steps.get(prev_config);
        }
        configs.add(0, initial_config);
        return configs;

    }
    
    public static void main(String args[]) {
        
        Solver mySolver = new Solver();
        
        Board myBoard = new Board(4, 4);
        myBoard.addPerimeterWalls();
        myBoard.addWall('E', new Position(0,0));

        myBoard.addRobot('R', new Position(0, 0));
        myBoard.addGoal('R', new Position(1, 3));

        myBoard.addRobot('G', new Position(0, 1));
        myBoard.addGoal('G', new Position(1, 0));
        
        ArrayList<Integer> steps = solve(myBoard);

        JFrame window = new JFrame("CONTENT");
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapDraw map = mySolver.new MapDraw(myBoard);
        window.add(map);
        window.setVisible(true);

        map.showSolution(steps);

    }
    
}
