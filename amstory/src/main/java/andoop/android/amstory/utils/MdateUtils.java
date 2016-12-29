package andoop.android.amstory.utils;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/1
* explain：时间格式化工具
* * * * * * * * * * * * * * * * * * */

import java.text.SimpleDateFormat;

public class MdateUtils {
    /**
     * 返回时间2016-12-01 15:27
     * @return
     */
    public static String getDate1(long timelong){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(timelong);
        return time;
    }

    /**
     * 返回时间2016-12-01 15:27 周四
     * @return
     */
    public static String getDate2(){
        return "";
    }
}
