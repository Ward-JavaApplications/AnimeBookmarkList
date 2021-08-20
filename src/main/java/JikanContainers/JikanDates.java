package JikanContainers;

import Exceptions.ErrorMessage;
import Managers.MyLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JikanDates {
    private Date from;
    private Date to;

    public JikanDates(String dates){
        boolean twoDates = dates.contains("to");
        dates = dates.replace("to",":");
        StringBuilder dateString = new StringBuilder();
        for(Character character: dates.toCharArray()){
            if(character == '?'){
                return;
            }
            if (character != ',') {
                if(character==':'){
                    try {
                        from = new SimpleDateFormat("MMM dd yyyy").parse(dateString.toString().substring(0,dateString.length()-1));
                        dateString = new StringBuilder();
                    }
                    catch (Exception e){
                        String msg = "Couldn't load the dates " + dateString.toString();
                        new ErrorMessage(msg);
                        MyLogger.log(msg);
                        e.printStackTrace();
                    }
                }
                dateString.append(character);
            }
        }
        try {
            if(twoDates)
                to = new SimpleDateFormat("MMM dd yyyy").parse(dateString.toString().substring(2));
            else
                to = from;
        } catch (ParseException e) {
            String msg = "Couldn't load the dates " + dateString.toString().substring(2);
            new ErrorMessage(msg);
            MyLogger.log(msg);
            e.printStackTrace();
        }
    }

    public Date getFrom(){
        return from;
    }
    public Date getTo(){
        return to;
    }
}
