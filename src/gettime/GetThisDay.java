package gettime;

import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
  
public class GetThisDay {  
      
    public static void main(String[] args) {  
        Date date = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String specifiedDay = sdf.format(date);  
        System.out.println(getSpecifiedDayBefore(specifiedDay));  
        System.out.println(getSpecifiedDayAfter(specifiedDay));  
    }  
      
    /** 
     * ���ָ�����ڵ�ǰһ�� 
     *  
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedDayBefore(String specifiedDay) {//������new Date().toLocalString()���ݲ���  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c  
                .getTime());  
        return dayBefore;  
    }  
  
    /** 
     * ���ָ�����ڵĺ�һ�� 
     *  
     * @param specifiedDay 
     * @return 
     */  
    public static String getSpecifiedDayAfter(String specifiedDay) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
  
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
                .format(c.getTime());  
        return dayAfter;  
    }  
}  
