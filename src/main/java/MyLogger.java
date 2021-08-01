import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public enum MyLogger {
    INSTANCE;
    private static Logger logger;
    MyLogger(){
        initialize();
    }
    private static void initialize(){
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
    public static void log(String msg){
        logger.info(msg);
    }
}
