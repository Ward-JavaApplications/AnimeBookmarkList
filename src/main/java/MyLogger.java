import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    private Logger logger;
    public MyLogger(){
        logger = Logger.getLogger("Markel");
        FileHandler fh;
        try{
            fh = new FileHandler("logging/MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fh.setFormatter(simpleFormatter);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void log(String msg){
        logger.info(msg);
    }
}
