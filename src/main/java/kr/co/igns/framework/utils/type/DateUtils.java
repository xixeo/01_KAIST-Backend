package kr.co.igns.framework.utils.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getFolderDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(new Date());
	}

	public static String getNowDate() {
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdfCurrent.format(today);
	}

	public static String getNowDate(String format) {
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(format);
		return sdfCurrent.format(today);
	}

	public static String convertLongToDateString(Long dateLong) {
		if (dateLong != null && dateLong != 0L) {
			Date date = new Date(dateLong);
			SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdfCurrent.format(date);
		} else {
			return null;
		}
	}

	public static Date getDateByFormat(String date, String format) throws Exception {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(format);
		return sdfCurrent.parse(date);
	}

	public static String getDateByFormat(Date date, String format) throws Exception {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(format);
		return sdfCurrent.format(date);
	}

	public static String getDateByFormatToString(String date, String originFormat, String toFormat)
			throws ParseException {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(originFormat);
		Date tempDate = sdfCurrent.parse(date);
		sdfCurrent = new SimpleDateFormat(toFormat);
		return sdfCurrent.format(tempDate);
	}

	public static String addYearMonthDay(String sDate, int year, int month, int day) {
		String dateStr = validChkDate(sDate);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException var8) {
			throw new IllegalArgumentException(dateStr);
		}

		if (year != 0) {
			cal.add(1, year);
		}

		if (month != 0) {
			cal.add(2, month);
		}

		if (day != 0) {
			cal.add(5, day);
		}

		return sdf.format(cal.getTime());
	}

	public static String validChkDate(String dateStr) {
		String _dateStr = dateStr;
		if (dateStr != null && (dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) {
			if (dateStr.length() == 10) {
				_dateStr = replace(dateStr, "-", "");
			}

			return _dateStr;
		} else {
			throw new IllegalArgumentException(dateStr);
		}
	}

	public static String replace(String str, String oldStr, String newStr) {
		StringBuffer sb = new StringBuffer();
		int savedPos = 0;

		do {
			int pos = str.indexOf(oldStr, savedPos);
			if (pos == -1) {
				break;
			}

			sb.append(str.substring(savedPos, pos));
			sb.append(newStr);
			savedPos = pos + oldStr.length();
		} while (savedPos < str.length());

		sb.append(str.substring(savedPos, str.length()));
		return sb.toString();
	}

	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {
		if (dateToValidate == null) {
			return false;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
			sdf.setLenient(false);

			try {
				sdf.parse(dateToValidate);
				return true;
			} catch (ParseException var4) {
				return false;
			}
		}
	}

	public static String timeStamp(int size) {
		return timeStamp().substring(0, size);
	}

	public static String getLogTimeStamp() {
		return timeStamp().substring(0, 14);
	}

	public static String timeStamp() {
		return timeStamp("yyyyMMddHHmmssSSS");
	}

	public static String timeHHmmss() {
		return timeStamp("yyyyMMddHHmmss");
	}

	public static String timeStamp(String format) {
		return (new SimpleDateFormat(format)).format((new GregorianCalendar()).getTime());
	}

	public static String timeStamp(String format, long date) {
		return (new SimpleDateFormat(format)).format(new Date(date));
	}

	public static String getSessionId() {
		long temp = Math.round(Math.random() * 1000.0D);
		timeStamp();
		return String.format("%04d", temp);
	}

	public static Date nowToDate() {
		return Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(9)));
	}

	public static Date nowAfterSecondsToDate(Long seconds) {
		return Date.from(LocalDateTime.now().plusSeconds(seconds).toInstant(ZoneOffset.ofHours(9)));
	}

	public static Date nowAfterHoursToDate(Long days) {
		return Date.from(LocalDateTime.now().plusHours(days).toInstant(ZoneOffset.ofHours(9)));
	}

	public static Date nowAfterDaysToDate(Long days) {
		return Date.from(LocalDateTime.now().plusDays(days).toInstant(ZoneOffset.ofHours(9)));
	}
	
	public long getTimeDifference(String time1, String time2) {
		//System.out.println("time : " + time1 + " / "+ time2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(time1);
			d2 = sdf.parse(time2);
			//System.out.println("d : " + d1 + " / "+ d2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long diff = d1.getTime() - d2.getTime();
		long min = diff / (1000*60);
		System.out.println("getTimeDifference : " + min);
		return min;
	}
	
	
	public String AddDate(String strDate, int year, int month, int day) { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal = Calendar.getInstance(); 
		Date dt = null;
		try {
			dt = dtFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		cal.setTime(dt); cal.add(Calendar.YEAR, year); 
		cal.add(Calendar.MONTH, month); 
		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}

	public int calDateBetweenAandB(String startDt, String endDt)
	{
	    String date1 = startDt;
	    String date2 = endDt;
	    int calDateDays = 0;
	    //System.out.println(" startDt : " + date1 + " endDt : " +date2);
	    try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
	        Date FirstDate = format.parse(date1);
	        Date SecondDate = format.parse(date2);
	        //System.out.println(" FirstDate : " + FirstDate + " SecondDate : " +SecondDate);
	        // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
	        // 연산결과 -950400000. long type 으로 return 된다.
	        long calDate = FirstDate.getTime() - SecondDate.getTime(); 
	        // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다. 
	        // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
	        calDateDays = (int) (calDate / ( 24*60*60*1000)); 
	        calDateDays = Math.abs(calDateDays);
	        //System.out.println(" calDateDays : " + calDateDays);
        }
        catch(ParseException e) {
            // 예외 처리
        }
	    return calDateDays;
	}
		


	//특정 날짜에 대하여 요일을 구함(일 ~ 토)
	public String getDateDayOfWeek(String date, String dateType) {
//		System.out.println("getDateDayOfWeek : " + date);
	    String day = "" ;
	    SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
	    Date nDate = null;
		try {
			nDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Calendar cal = Calendar.getInstance() ;
	    cal.setTime(nDate);
	    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
	    switch(dayNum){
	        case 1:
	            day = "일";
	            break ;
	        case 2:
	            day = "월";
	            break ;
	        case 3:
	            day = "화";
	            break ;
	        case 4:
	            day = "수";
	            break ;
	        case 5:
	            day = "목";
	            break ;
	        case 6:
	            day = "금";
	            break ;
	        case 7:
	            day = "토";
	            break ;        
	    }    
	    return day ;
	}
}