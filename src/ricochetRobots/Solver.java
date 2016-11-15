package ricochetRobots;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class Solver {
    
    public static void solve(Board board) {

        HashSet<Integer>prev_configs = new HashSet<Integer>();
        Queue<Integer> configs_queue = new LinkedList<Integer>();
        configs_queue.add(board.getConfig());

        while(!board.solved() && !configs_queue.isEmpty()) {
            
            int prev_config = configs_queue.poll();
            board.setConfig(prev_config);

            for(char bot_id: board.getRobotIds()) {
                for(char dir: new char[] {'N', 'S', 'E', 'W'}) {
                    Position p = board.moveRobot(bot_id, dir);
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
        
        Board myBoard = new Board(2, 2);
        myBoard.addPerimeterWalls();
        myBoard.addWall('E', new Position(0,0));
        myBoard.addWall('E', new Position(0, 1));
        myBoard.addRobot('R', new Position(0, 0));
        myBoard.addGoal('R', new Position(1, 0));
        System.out.println(myBoard);
        solve(myBoard);
        System.out.println(myBoard);


    }
    
}
