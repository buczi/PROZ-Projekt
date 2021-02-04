package crash;

import mvc.Controller;
import mvc.exception.CriticalException;
import org.junit.Assert;
import org.junit.Test;

public class CriticalExceptionTest {
    @Test
    public void TestCriticalException(){
        boolean exceptionTriggered;
        try{
            new Controller("ERROR.txt");
            exceptionTriggered = false;
        }
        catch (CriticalException e){
            exceptionTriggered = true;
        }
        Assert.assertTrue(exceptionTriggered);
    }
}
