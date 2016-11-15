package ricochetRobots;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

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
            drawRobots(g2);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
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
        
        private void drawRobots(Graphics2D g2) {
            for(char bot_id: _board.getRobotIds()) {
                Position p = _board.getRobotPosition(bot_id);
                switch(bot_id) {
                    case 'R': g2.setPaint(Color.RED); break;
                    case 'G': g2.setPaint(Color.GREEN); break;
                    case 'B': g2.setPaint(Color.BLUE); break;
                    case 'Y': g2.setPaint(Color.YELLOW); break;
                }
                g2.drawOval(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
                g2.fillOval(_xoffset+5+30*p.x, _yoffset+5+30*p.y, 20, 20);
            }
        }
        
        private Board _board;
        int _xoffset;
        int _yoffset;
        
    }
    
    
    public static void solve(Board board, MapDraw map) {

        HashSet<Integer>prev_configs = new HashSet<Integer>();
        Queue<Integer> configs_queue = new LinkedList<Integer>();
        configs_queue.add(board.getConfig());

        while(!board.solved() && !configs_queue.isEmpty()) {
            
            int prev_config = configs_queue.poll();
            board.setConfig(prev_config);

            for(char bot_id: board.getRobotIds()) {
                for(char dir: new char[] {'N', 'S', 'E', 'W'}) {
                    Position p = board.moveRobot(bot_id, dir);
                    map.repaint();
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException e){
                        System.out.println("got interrupted!");
                    }
                    if(board.solved()){ break; }
                    int new_config = board.getConfig();
                    if(!prev_configs.contains(new_config)) {
                        prev_configs.add(new_config); 
                        configs_queue.add(new_config);
                    }
                    board.setRobotPosition(bot_id, p);
                }
            }

        }

    }
    
    public static void main(String args[]) {
        
        Solver mySolver = new Solver();
        
        Board myBoard = new Board(8, 8);
        myBoard.addPerimeterWalls();
        myBoard.addWall('E', new Position(0,0));
        myBoard.addWall('E', new Position(0, 1));
        myBoard.addRobot('R', new Position(0, 0));
        myBoard.addGoal('R', new Position(7, 7));
        // System.out.println(myBoard);
        
        JFrame window = new JFrame("CONTENT");
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapDraw map = mySolver.new MapDraw(myBoard);
        window.add(map);
        window.setVisible(true);

        solve(myBoard, map);

    }
    
}
