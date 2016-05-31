package gettime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ��ȡ����ʱ��
 *
 * @author 
 * @date   
 */
public class Gettime {

    public static String main(String[] args) {
    	String newtime;
        //String webUrl1 = "http://www.bjtime.cn";//bjTime
        //String webUrl2 = "http://www.baidu.com";//�ٶ�
        //String webUrl3 = "http://www.taobao.com";//�Ա�
        String webUrl4 = "http://www.ntsc.ac.cn";//�й���ѧԺ������ʱ����
        //String webUrl5 = "http://www.360.cn";//360
        //String webUrl6 = "http://www.beijing-time.org";//beijing-time
        //System.out.println(getWebsiteDatetime(webUrl1) + " [bjtime]");
        //System.out.println(getWebsiteDatetime(webUrl2) + " [�ٶ�]");
        //System.out.println(getWebsiteDatetime(webUrl3) + " [�Ա�]");
        newtime = getWebsiteDatetime(webUrl4);
        System.out.println( newtime + " [�й���ѧԺ������ʱ����]");
        //System.out.println(getWebsiteDatetime(webUrl5) + " [360��ȫ��ʿ]");
        //System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");
        return newtime;
    }

    /**
     * ��ȡָ����վ������ʱ��
     * 
     * @param webUrl
     * @return
     * @author 
     * @date   
     */
    private static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// ȡ����Դ����
            URLConnection uc = url.openConnection();// �������Ӷ���
            uc.connect();// ��������
            long ld = uc.getDate();// ��ȡ��վ����ʱ��
            Date date = new Date(ld);// ת��Ϊ��׼ʱ�����
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// �������ʱ��
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


