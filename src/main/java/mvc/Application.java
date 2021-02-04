package mvc;


import mvc.event.CreateThread;
import mvc.exception.CriticalException;

public class Application implements CreateThread {
    Controller controller1;
    Controller controller2;
    public Application(){}
    public static void main(String[] Args) {
        Application app = new Application();
        try{
            app.controller1 = new Controller(true,app);
        }
        catch (CriticalException exception){
            System.out.println("[USER]: Problem related to loading files or graphical interface. Best to look for dev help");
            System.out.println(exception.getCriticalExceptionTrace());
            try{
                app.controller1.join();
            }
            catch (Exception e) {
                System.out.println("[USER]: Casual app error. Reset app");
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public void createThread()
    {
        try{
            controller1.join();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[USER]: Casual app error. Reset app");
        }

        try{
           controller2 = new Controller(false,this);
        }
        catch (CriticalException exception){
            System.out.println("[USER]: Problem related to loading files or graphical interface. Best to look for dev help");
            System.out.println(exception.getCriticalExceptionTrace());
            try{
                controller2.join();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("[USER]: Casual app error. Reset app");
            }
        }

        System.gc();
        controller1 = controller2;
    }

    @Override
    public void reportException(CriticalException exception){
        System.out.println(exception.getCriticalExceptionTrace());
        controller1.getView().closeWindow();
        try{
            controller1.join(1);
        }
        catch (Exception e) {
            System.out.println("[USER]: Casual app error. Reset app");
            System.out.println(e.getMessage());
        }
    }

    public Controller getController1() {
        return controller1;
    }

    public void setController1(Controller controller1) {
        this.controller1 = controller1;
    }
}
