package MVC;

import Map.*;
import Utils.*;


public class Controller extends Thread implements Runnable {
    public Map map;
    public TimeController timeController;


    public Controller() {
        this.map = new Map(50, 1, 360, 15, "/home/maciek/Desktop/PROZ_PRO/src/Map1.txt");

        this.timeController = TimeController.getInstance();
        this.timeController.AddToTimeObjects(map);
    }

    @Override
    public void run() {
        this.timeController.StartClock();
        int prevSize = 0;

        while (true) {

            this.timeController.Run();

            if (this.timeController.get_time() >= this.map.getMax_level_time())
                break;

            if (prevSize != this.map.getCars().size())
                System.out.println(this.map.getCars().size());
            prevSize = this.map.getCars().size();

            //System.out.println(this.timeController.get_time());
        }


        System.out.println("X");

    }

    public static void main(String[] Args) {
        Controller controller = new Controller();
        controller.start();
    }
}