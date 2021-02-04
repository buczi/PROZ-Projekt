package app;

import map.Map;
import mvc.Controller;
import org.junit.Assert;
import org.junit.Test;

public class DemoDisplay {
    @Test
    public void testDemo()
    {
        Controller controller = null;
        try{
            controller = new Controller("/maps/map.txt");
            Map.stopDemo();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        Assert.assertNotNull(controller);
    }
}
