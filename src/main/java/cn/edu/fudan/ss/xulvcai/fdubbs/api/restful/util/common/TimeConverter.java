package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;


	import java.text.ParseException;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.HashMap;
	import java.util.Map;
	import java.util.TimeZone;

	public class TimeConverter
	{
	    
	    public static void main(String[] args)
	    {
	        System.out.println("默认时区：" + getLocalTimeId());
	        System.out.println("时区列表：" + getZoneList());
	        
	        //原时区的时间
	        String sourceTime = "2013-08-06 15:30:00";
	        //原时区东八区Asia/Shanghai,GMT+8:00
	        String sourceId = "Asia/Shanghai";
	        //目标时区Asia/Dili,GMT+09:00
	        String targetId = "Asia/Dili";
	        //转换后的时间
	        String targetTime = timeConvert(sourceTime, sourceId, targetId);
	        System.out.println("原来为：" + sourceTime);
	        System.out.println("转换为：" + targetTime);
	        
	    }
	    
	    public static int getHourOfTimeZone(String targetTimeZone) {
	    	//String sourceTimeZone = "Universal";
	    	return 0;
	    }
	    
	    /**
	     * 获取本地默认时区id
	     * @return string 本地时区id
	     */
	    public static String getLocalTimeId()
	    {
	        TimeZone defaultTimeZone = TimeZone.getDefault();
	        String sourceId = defaultTimeZone.getID();
	        return sourceId;
	    }
	    
	    /**
	     * 获取受支持的所有可用 ID
	     * 用来作为页面显示的时区下拉列表
	     * 以绝对时区显示（不考虑夏令时）
	     * @return map 存储时区列表+偏移量的map(可用来显示如Hongkong,GMT+08:00)
	     * 实际使用时，传给服务器是零时区，值传递时区id就可以了，不传递偏移量
	     */
	    public static Map<String, String> getZoneList()
	    {
	        String[] zoneIds = TimeZone.getAvailableIDs();
	        int length = zoneIds.length;
	        TimeZone timeZone = null;
	        //存储时区列表+偏移量到map中
	        Map<String, String> map = new HashMap<String, String>(650);
	        long offset = 0L;
	        String diplayOffset = "";
	        for (int i = 0; i < length; i++)
	        {
	            //获取给定 ID 的 TimeZone
	            timeZone = TimeZone.getTimeZone(zoneIds[i]);
	            //返回添加到 UTC 以获取此时区中的标准时间的时间偏移量（以毫秒为单位）。
	            offset = timeZone.getRawOffset();
	            //对偏移量做显示，如GMT-09:30、GMT+09:30
	            diplayOffset = appendZoneSuffix(offset);
	            //存储到map中，形式为Hongkong---GMT+08:00
	            map.put(zoneIds[i], diplayOffset);
	        }
	        return map;
	    }
	    
	    /**
	     * 添加时区偏移量
	     * @param offset 偏移量（以毫秒为单位）
	     * @return 日期
	     */
	    public static String appendZoneSuffix(long offset)
	    {
	        //将偏移量转化为小时（小数去除不要）
	        long hour = Long.valueOf((offset / 3600000));
	        //偏移量对小时取余数，得到小数（以毫秒为单位）
	        double decimals = offset % 3600000;
	        //显示为09:30分钟形式
	        double decimalsZone = (decimals / 3600000) * 60 / 100;
	        String sAdd = "";
	        if (hour >= 0)
	        {
	            sAdd = "+";
	        }
	        else
	        {
	            sAdd = "-";
	        }
	        hour = hour > 0 ? hour : -hour;
	        String sHour = hour + "";
	        if (sHour.length() == 1)
	        {
	            sHour = '0' + sHour;
	        }
	        
	        decimalsZone = decimalsZone < 0 ? -decimalsZone : decimalsZone;
	        String sDecimalsZone = decimalsZone + "";
	        sDecimalsZone = sDecimalsZone.substring(2);
	        if (sDecimalsZone.length() == 1)
	        {
	            sDecimalsZone = sDecimalsZone + '0';
	        }
	        else if (sDecimalsZone.length() >= 3)
	        {
	            sDecimalsZone = sDecimalsZone.substring(0, 2);
	        }
	        return "GMT" + sAdd + sHour + ':' + sDecimalsZone;
	    }
	    
	    /**
	     * 时区 时间转换方法:将当前时间（可能为其他时区）转化成目标时区对应的时间
	     * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
	     * @param sourceId 入参的时间的时区id
	     * @param targetId 要转换成目标时区id（一般是是零时区：取值UTC）
	     * @return string 转化时区后的时间
	     */
	    public static String timeConvert(String sourceTime, String sourceId,
	            String targetId)
	    {
	        //校验入参是否合法
	        if (null == sourceId || "".equals(sourceId) || null == targetId
	                || "".equals(targetId) || null == sourceTime
	                || "".equals(sourceTime))
	        {
	            return "";
	        }
	        //校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
	        String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
	        if (!sourceTime.matches(reg))
	        {
	            return "";
	        }
	        
	        try
	        {
	            //时间格式
	            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            //根据入参原时区id，获取对应的timezone对象
	            TimeZone sourceTimeZone = TimeZone.getTimeZone(sourceId);
	            //设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
	            df.setTimeZone(sourceTimeZone);
	            //将字符串sourceTime转化成原时区对应的date对象
	            Date sourceDate = df.parse(sourceTime);
	            
	            //开始转化时区：根据目标时区id设置目标TimeZone
	            TimeZone targetTimeZone = TimeZone.getTimeZone(targetId);
	            //设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
	            df.setTimeZone(targetTimeZone);
	            //得到目标时间字符串
	            String targetTime = df.format(sourceDate);
	            return targetTime;
	        }
	        catch (ParseException e)
	        {
	            e.printStackTrace();
	        }
	        return "";
	        
	    }
	}