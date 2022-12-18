package kr.devsnote.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import kr.devsnote.config.Constants;
import kr.devsnote.file.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component(value="CommonUtil")
public class CommonUtil {

    /**
     * 두 시간에 대한 차리를 분 단위로 계산한다.
     * @param startDate yyyyMMddHHmmss
     * @param endDAte yyyyMMddHHmmss
     * @return 차이 분
     */
    public static long getDifferSec(String startDate, String endDAte){

        try {
            Date frDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(startDate);
            Date toDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(endDAte);

            long diffMil = toDate.getTime() - frDate.getTime();
            long diffSec = diffMil/1000;
            return diffSec;

        } catch (ParseException e) {

            return -1;
        }

    }//:

    /**
     * 접속환경 확인(웹/모바일)
     * @author JJT
     * @since 2018.06.11
     * @return boolean
     */
    public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        boolean mobile1 = userAgent.matches(".*(iPhone|iPod|iPad|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
        boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
        if(mobile1 || mobile2) {
            return true;
        }
        return false;
    }
    /**
     * 휴대폰번호양식에 맞는지 확인
     * @param str
     * @return boolean
     */
    public static boolean isCellphone(String str) {
        //010, 011, 016, 017, 018, 019
        return str.matches("(01[016789])\\d{7,8}");
    }
    /**
     * 이메일 양식에 맞는지 확인
     * @param str
     * @return boolean
     */
    public static boolean isEmail(String str) {
        //aaa@bbb.com
        return str.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    }

    /**
     * 특정자리수만큼 난수 발생
     * @throws NoSuchAlgorithmException
     * @since 2018.06.11
     **/
    public static String generateNumber(int length) throws NoSuchAlgorithmException {
        String numStr = "1";
        String plusNumStr = "1";

        for (int i = 0; i < length; i++) {
            numStr += "0";
            if (i != length - 1) {
                plusNumStr += "0";
            }
        }

        Random random = SecureRandom.getInstanceStrong();
        int result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);
        if (result > Integer.parseInt(numStr)) {
            result = result - Integer.parseInt(plusNumStr);
        }
        return "" + result;
    }

    /**
     * 널값 "" 로 변경
     * @since 2018.06.11
     **/
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 현재 일시에 해당하는 Calendar 객체를 반환함.
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getCalendarInstance()
     * </pre>
     *
     * @return 결과 calendar객체
     */
    public static Calendar getCalendarInstance() {
        Calendar retCal = Calendar.getInstance();
        return retCal;
    }



    /**
     * 입력한 년, 월, 일에 해당하는 Calendar 객체를 반환함. 일자를 바르게 입력하지않으면 엉뚱한 결과가 나타남..
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getCalendarInstance(1982, 12, 02)
     * </pre>
     *
     * @param year
     *            년
     * @param month
     *            월
     * @param date
     *            일
     * @return 결과 calendar객체
     */
    public static Calendar getCalendarInstance(int year, int month, int date) {
        Calendar retCal = Calendar.getInstance();
        month--;

        retCal.set(year, month, date);

        return retCal;
    }

    /**
     * 입력한 년, 월, 일, 시, 분, 초에 해당하는 Calendar 객체를 반환함.<br>
     * 일자를 바르게 입력하지않으면 엉뚱한 결과가 나타남..
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getCalendarInstance(1982, 12, 02, 12, 59, 59)
     * </pre>
     *
     * @param year
     *            년
     * @param month
     *            월
     * @param date
     *            일
     * @param hour
     *            시
     * @param minute
     *            분
     * @param second
     *            초
     * @return 결과 calendar객체
     */
    public static Calendar getCalendarInstance(int year, int month, int date,
                                               int hour, int minute, int second) {
        Calendar retCal = Calendar.getInstance();
        month--;

        retCal.set(year, month, date, hour, minute, second);

        return retCal;
    }

    /**
     * calendar에 해당하는 일자를 type의 날짜형식으로 반환합니다.<br>
     * 타입의 형식을 반드시 지켜야 합니다.<br>
     * (자세한 사항은 SimpleDateFormat java document 참조.)
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getCalendarInstance(1982, 12, 02, 12, 59, 59);
     *      DateUtil.getDateFormat(cal, "yyyyMMddHHmmssSSS")
     *      DateUtil.getDateFormat(cal, "yyyy-MM-dd hh:mm:ss")
     *      DateUtil.getDateFormat(cal, "yyyy년MM월dd일 hh시mm분ss초")
     * </pre>
     *
     * @param cal
     *            calender객체
     * @param type
     *            변환타입
     * @return 변환된 문자열
     */
    public static String getDateFormat(Calendar cal, String type) {
        SimpleDateFormat dfmt = new SimpleDateFormat(type);
        return dfmt.format(cal.getTime());
    }

    /**
     * 현재 일자를 입력된 type의 날짜로 반환합니다.<br>타입의 형식을 반드시 지켜야 합니다.<br>
     * (자세한 사항은 SimpleDateFormat java document 참조.)
     *
     * <pre>
     *  ex) DateUtil.getDateFormat("yyyyMMddHHmmssSSS")
     *      DateUtil.getDateFormat("yyyy-MM-dd hh:mm:ss")
     *      DateUtil.getDateFormat("yyyy년MM월dd일 hh시mm분ss초")
     * </pre>
     *
     * @param type
     *            날짜타입
     * @return 결과 문자열
     */
    public static String getDateFormat(String type) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(date);
    }

    /**
     * Calender에 해당하는 날짜와 시각을 yyyyMMdd 형태로 변환 후 return.
     *
     * <pre>
     *  ex) String date = DateUtil.getYyyymmdd()
     * </pre>
     *
     * @param cal
     *            Calender객체
     * @return 결과 일자
     */
    public static String getYyyymmdd(Calendar cal) {
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMdd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                currentLocale);
        return formatter.format(cal.getTime());
    }

    /**
     * 현재 날짜와 시각을 yyyyMMddhhmmss 형태로 변환 후 return.
     *
     * <pre>
     *
     *  ex) String date = DateUtil.getCurrentDateTime()
     * </pre>
     *
     * @return 현재 년월일시분초
     */
    public static String getCurrentDateTime() {
        //자바 1.4 버전 현재 비스타에서 동작시 로컬타임을 잘못가져옴
        //표준시에서 9시간 차이나는것 만큼 표시해야 하는데..
        //비스타에서는 표준시로만 나타냅니다.
        //해결방안으로 setproperty를 추가합니다.
        // 2007.10.10 고재선
        System.setProperty("user.timezone", "Asia/Seoul");
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                currentLocale);
        return formatter.format(today);
    }

    /**
     * 현재 시각을 hhmmss 형태로 변환 후 return.
     *
     * <pre>
     *  ex) String date = DateUtil.getCurrentDateTime()
     * </pre>
     *
     * @return 현재 시분초
     */
    public static String getCurrentTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "HHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                currentLocale);
        return formatter.format(today);

    }

    /**
     * 현재 날짜를 yyyyMMdd 형태로 변환 후 return.
     *
     * <pre>
     *
     *  ex) String date = DateUtil.getCurrentDate()
     * </pre>
     *
     * @return 현재 년월일
     */
    public static String getCurrentDate() {
        return getCurrentDateTime().substring(0, 8);
    }

    /**
     * 현재 날짜를 yyyy-MM-dd 형태로 변환 후 return.
     *
     * <pre>
     *
     *  ex) String date = DateUtil.getCurrentDate()
     * </pre>
     *
     * @return 현재 년월일
     */
    public static String getCurrentDateYYYYMMDD() {
        return getCurrentDateTime().substring(0, 4)+"-"+getCurrentDateTime().substring(4, 6)+"-"+getCurrentDateTime().substring(6, 8);
    }


    /**
     * 입력된 일자를 더한 날짜를 yyyyMMdd 형태로 변환 후 return.
     *
     *
     * @param yyyymmdd
     *            기준일자
     * @param addDay
     *            추가일
     * @return 연산된 일자
     * @see java.util.Calendar
     */
    public static String getDate(String yyyymmdd, int addDay) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);
        cal.add(Calendar.DATE, addDay);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        return sdf.format(cal.getTime());

        // TODO
    }


    /**
     * 년월주로 일자를 구하는 메소드.
     *
     * <pre>
     *  ex) String date = DateUtil.getWeekToDay("200801" , 1, "yyyyMMdd")
     * </pre>
     *
     * @param yyyymm
     *            년월
     * @param week
     *            몇번째 주
     * @param pattern
     *            리턴되는 날짜패턴 (ex:yyyyMMdd)
     * @return 연산된 날짜
     * @see java.util.Calendar
     */
    public static String getWeekToDay(String yyyymm, int week, String pattern) {

        Calendar cal = Calendar.getInstance(Locale.FRANCE);

        int new_yy = Integer.parseInt(yyyymm.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymm.substring(4, 6));
        int new_dd = 1;

        cal.set(new_yy, new_mm - 1, new_dd);

        // 임시 코드
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week = week - 1;
        }

        cal.add(Calendar.DATE, (week - 1) * 7
                + (cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK)));

        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                Locale.FRANCE);

        return formatter.format(cal.getTime());

    }

    /**
     * 지정된 플래그에 따라 calendar에 해당하는 연도 , 월 , 일자를 연산한다.<br>
     * (calendar객체의 데이터는 변하지 않는다.)
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getCalendarInstance(1982, 12, 02, 12, 59, 59)
     *      String date = DateUtil.getComputeDate(java.util.Calendar.MONTH , -10, cal);
     *      결과 : 19820202
     * </pre>
     *
     * @param field
     *            연산 필드
     * @param amount
     *            더할 수
     * @param cal
     *            연산 대상 calendar객체
     * @return 연산된 날짜
     * @see java.util.Calendar
     */
    public static String getComputeDate(int field, int amount, Calendar cal) {
        Calendar cpCal = (Calendar) cal.clone();
        cpCal.add(field, amount);
        return getYyyymmdd(cpCal);
    }

    /**
     * 지정된 플래그에 따라 현재의 연도 , 월 , 일자를 연산한다.
     *
     * <pre>
     *  ex) DateUtil.getComputeDate(java.util.Calendar.MONTH , 10);
     *      결과 : 19820202
     * </pre>
     *
     * @param field
     *            연산 필드
     * @param amount
     *            더할 수
     * @return 연산된 날짜
     * @see java.util.Calendar
     */
    public static String getComputeDate(int field, int amount) {
        return getComputeDate(field, amount, CommonUtil.getCalendarInstance());
    }

    /**
     * 입력된 일자를 더한 주를 구하여 return한다
     *
     * <pre>
     *  ex) int date = DateUtil.getWeek(DateUtil.getCurrentYyyymmdd() , 0)
     * </pre>
     *
     * @param yyyymmdd
     *            년도별
     * @param addDay
     *            추가일
     * @return 연산된 주
     * @see java.util.Calendar
     */
    public static int getWeek(String yyyymmdd, int addDay) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);
        cal.add(Calendar.DATE, addDay);

        int week = cal.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    /**
     * 입력된 년월의 마지막 일수를 return 한다.
     *
     * <pre>
     *  ex) int date = DateUtil.getLastDayOfMon(2008 , 1)
     * </pre>
     *
     * @param year
     *            년
     * @param month
     *            월
     * @return 마지막 일수
     * @see java.util.Calendar
     */
    public static int getLastDayOfMon(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }// :

    /**
     * 입력된 년월의 마지막 일수를 return한다
     *
     * <pre>
     *  ex) int date = DateUtil.getLastDayOfMon("2008")
     * </pre>
     *
     * @param yyyymm
     *            년월
     * @return 마지막 일수
     */
    public static int getLastDayOfMon(String yyyymm) {

        Calendar cal = Calendar.getInstance();
        int yyyy = Integer.parseInt(yyyymm.substring(0, 4));
        int mm = Integer.parseInt(yyyymm.substring(4)) - 1;

        cal.set(yyyy, mm, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 입력된 날자가 올바른지 확인합니다.
     *
     * <pre>
     *  ex) boolean b = DateUtil.isCorrect("20080101")
     * </pre>
     *
     * @param yyyymmdd
     * @return boolean
     */
    public static boolean isCorrect(String yyyymmdd) {
        boolean flag = false;
        if (yyyymmdd.length() < 8)
            return false;
        try {
            int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
            int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
            int dd = Integer.parseInt(yyyymmdd.substring(6));
            flag = CommonUtil.isCorrect(yyyy, mm, dd);
        } catch (Exception ex) {
            return false;
        }
        return flag;
    }

    /**
     * 입력된 날자가 올바른 날자인지 확인합니다.
     *
     * <pre>
     *  ex) boolean b = DateUtil.isCorrect(2008,1,1)
     * </pre>
     *
     * @param yyyy
     * @param mm
     * @param dd
     * @return boolean
     */
    public static boolean isCorrect(int yyyy, int mm, int dd) {
        if (yyyy < 0 || mm < 0 || dd < 0)
            return false;
        if (mm > 12 || dd > 31)
            return false;

        String year = "" + yyyy;
        String month = "00" + mm;
        String year_str = year + month.substring(month.length() - 2);
        int endday = CommonUtil.getLastDayOfMon(year_str);

        if (dd > endday)
            return false;

        return true;

    }//:

    /**
     * 현재의 요일을 구한다.
     *
     * <pre>
     *  ex) int day = DateUtil.getDayOfWeek()
     *      SUNDAY    = 1
     *      MONDAY    = 2
     *      TUESDAY   = 3
     *      WEDNESDAY = 4
     *      THURSDAY  = 5
     *      FRIDAY    = 6
     * </pre>
     *
     * @return 요일
     * @see java.util.Calendar
     */
    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }//:


    /**
     * 입력받은 날짜의 요일을 반환한다.
     * @param yyyymmdd
     * @return
     */
    public static int getDayOfWeek(String yyyymmdd) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);

        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }//:



    /**
     * 현재주가 올해 전체의 몇째주에 해당되는지 계산한다.
     *
     * <pre>
     *  ex) int day = DateUtil.getWeekOfYear()
     * </pre>
     *
     * @return 주
     * @see java.util.Calendar
     */
    public static int getWeekOfYear() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_year = rightNow.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }//:

    /**
     * 입력받은 yyyymmdd 가 전체의 몇주에 해당되는지 계산한다.
     * @param yyyymmdd
     * @return 주
     */
    public static int getWeekOfYear(String yyyymmdd) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }//:



    /**
     * 현재주가 현재월에 몇째주에 해당되는지 계산한다.
     *
     * <pre>
     *  ex) int day = DateUtil.getWeekOfMonth()
     * </pre>
     *
     * @return 주
     * @see java.util.Calendar
     */
    public static int getWeekOfMonth() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_month = rightNow.get(Calendar.WEEK_OF_MONTH);
        return week_of_month;
    }//:


    /**
     * 입력받은 yyyymmdd 해당월에 몇째주에 해당되는지 계산한다.
     *
     * <pre>
     *  ex) int day = DateUtil.getWeekOfMonth("20110401")
     * </pre>
     *
     * @return 주
     * @see java.util.Calendar
     */
    public static int getWeekOfMonth(String yyyymmdd) {

        Calendar cal = Calendar.getInstance(Locale.KOREA);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);

        int week = cal.get(Calendar.WEEK_OF_MONTH);
        return week;

    }//:




    /**
     * 두 날짜간의 날짜수를 반환(윤년을 감안함)
     *
     * <pre>
     *  ex) long date = DateUtil.getDifferDays("20080101", "20080202")
     * </pre>
     *
     * @param startDate
     *            시작 날짜
     * @param endDate
     *            끝 날짜
     * @return 날수
     * @see java.util.GregorianCalendar
     */
    public static long getDifferDays(String startDate, String endDate) {
        GregorianCalendar StartDate = getGregorianCalendar(startDate);
        GregorianCalendar EndDate = getGregorianCalendar(endDate);
        long difer = (EndDate.getTime().getTime() - StartDate.getTime()
                .getTime()) / 86400000;
        return difer;
    }//:



    /**
     * 두 시간에 대한 차리를 분 단위로 계산한다.
     * @param startDate yyyyMMddHHmmss
     * @param endDAte yyyyMMddHHmmss
     * @return 차이 분
     */
    public static long getDifferMin(String startDate, String endDAte){

        try {
            Date frDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(startDate);
            Date toDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(endDAte);

            long diffMil = toDate.getTime() - frDate.getTime();
            long diffSec = diffMil/1000;
            long  Min = (diffSec) / 60;

            if(Min < 0){
                Min = Min*-1;
            }

            return Min;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return -1;
        }

    }//:


    /**
     * 두 날짜간의 월수를 반환
     *
     * <pre>
     *  ex) long date = DateUtil.getDifferMonths("20080101", "20080202")
     * </pre>
     *
     * @param startDate
     *            시작 날짜
     * @param endDate
     *            끝 날짜
     * @return 월수
     * @see java.util.GregorianCalendar
     */
    public static int getDifferMonths(String startDate, String endDate) {
        GregorianCalendar cal1 = getGregorianCalendar(startDate);
        GregorianCalendar cal2 = getGregorianCalendar(endDate);

        int m = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        int months = (m * 12) + (cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH));
        return Math.abs(months);
    }//:


    /**
     * GregorianCalendar 객체를 반환함.
     *
     * <pre>
     *  ex) Calendar cal = DateUtil.getGregorianCalendar(DateUtil.getCurrentYyyymmdd())
     * </pre>
     *
     * @param yyyymmdd
     *            날짜 인수
     * @return GregorianCalendar
     * @see java.util.Calendar
     * @see java.util.GregorianCalendar
     */

    private static GregorianCalendar getGregorianCalendar(String yyyymmdd) {

        int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int dd = Integer.parseInt(yyyymmdd.substring(6));

        GregorianCalendar calendar = new GregorianCalendar(yyyy, mm - 1, dd, 0,
                0, 0);

        return calendar;

    }//:




    /**
     * 날짜 형식 출력
     * <p/>
     * yyyy-MM-dd 형식
     *
     * @param date 대상 날짜
     * @return 문자열
     */
    public  String formatDate(Timestamp date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");


        return format.format(date);
    }

    /**
     * 날짜 형식 출력
     * <p/>
     * yyyy-MM-dd HH:mm:ss 형식
     *
     * @param date 대상 날짜
     *
     * @return 문자열
     */
    public  String formatDateWithTime(Timestamp date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }
    /**
     * 날짜 형식 출력
     *
     * @param date 대상 날짜 문자열
     * @param separator 구분자
     *
     * @return formatted date string
     */
    public  String formatDate(String date, String separator) {
        if (date == null || date.length() != 8) {
            return "";
        }
        if (separator == null) {
            separator = ". ";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(date.substring(0, 4))
                .append(separator)
                .append(date.substring(4, 6))
                .append(separator)
                .append(date.substring(6, 8));
        return sb.toString();
    }
    /**
     * 월 컨트롤
     *
     * @param c 대상 날짜
     * @param gap 변경할 월
     * @param format 날짜의 포맷 지정
     *
     * @return formatted date string
     */
    public static String getYearMonth(Calendar c,int gap,String format){
        String resultString = "";
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1 + gap;
        c.set(year, month - 1, 1);
        resultString = getDateFormat(c,format);
        return resultString;
    }
    /**
     * {@code URLDecoder}를 이용하여 decoding된 URL을 반환한다.
     *
     * @param url 대상 문자열
     *
     * @return decoding된 URL
     */
    public  String decodeURL(String url) {
        String decodedURL = "";

        if (CommonUtil.isNotEmpty(url)) {
            try {
                decodedURL = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }
        }

        return decodedURL;
    }

    /**
     * nvl
     * <p/>
     * value값이 null이면 replacement를 리턴하고,
     * value값이 null이 아니면 value값을 리턴한다.
     *
     * @param value 원래 문자열
     * @param replacement 치환문자열
     *
     * @return 결과문자열
     */
    public  String nvl(String value, String replacement) {
        if (value == null || "".equals(value)) {
            return replacement;
        }
        return value;
    }

    /**
     * nvl0
     * <p/>
     * value값이 null이거나 '0'이면 replacement를 리턴하고,
     * value값이 null이 아니면 value값을 리턴한다.
     *
     * @param value 원래 문자열
     * @param replacement 치환문자열
     *
     * @return 결과문자열
     */
    public  String nvl0(String value, String replacement) {
        if (value == null || "".equals(value) || "0".equals(value)) {
            return replacement;
        }
        return value;
    }

    /**
     * 문자열로부터 HTML/XML 태그를 제거함
     *
     * @param message
     *
     * @return String message without XML or HTML
     */
    public  String stripHTMLTags(String message) {
        String noHTMLString = message.replaceAll("\\<.*?\\>", "");
        return noHTMLString;
    }

    /**
     * 정규화 표현식을 문자열로 변환
     * @param str
     * @return
     */
    public static  String codeToStr(String str) {
        str = str.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&#039;", "'")
                .replaceAll("&#034;", "\"")
                .replaceAll("&amp;", "&");

        return str;
    }

    /**
     * 문자열을 정규화 표현식으로 변환
     * @param str
     * @return
     */
    public static String strToCode(String str) {
        str = str.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("'", "&#039;")
                .replaceAll("\"", "&#034;");

        return str;
    }
    /**
     * 문자열 길이만큼 줄임
     *
     * @param content 문자열
     * @param length 길이
     *
     * @return 잘린 문자열
     */
    public  String getFitString(String content, int length) {
        if (content == null) {
            return "";
        }
        String tmp = content;
        int slen = 0;
        int blen = 0;
        if (tmp.getBytes().length > length) {
            while (blen + 1 < length && slen < tmp.length()) {
                char c = tmp.charAt(slen);
                blen++;
                slen++;
                if (c > '\177') {
                    blen++;
                }
            }
            tmp = tmp.substring(0, slen);
            if (content.length() > tmp.length()) {
                tmp += "...";
            }
        }
        return tmp;
    }

    /**
     * 소문자 -> 대문자로 변경
     *
     * @param str {@code String}
     * @return 대문자 반환
     */
    public  String toUpperCase(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.toUpperCase();
        }
        return "";
    }

    /**
     * 대문자 -> 소문자로 변경
     * @param str {@code String}
     * @return 소문자 반환
     */
    public  String toLowerCase(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.toLowerCase();
        }
        return "";
    }

    /**
     * CLOB로 되어 있는 것을 String 으로 변경한다.
     * @param clob
     * @return
     */
    public static String getStringFromCLOB(java.sql.Clob clob) {
        StringBuffer sbf = new StringBuffer();
        java.io.Reader br = null;
        char[] buf = new char[1024];
        int readcnt;
        try {
            br = clob.getCharacterStream();
            while ((readcnt = br.read(buf, 0, 1024)) != -1) {
                sbf.append(buf, 0, readcnt);
            }
        } catch (Exception e) {

        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {

                }
        }
        return sbf.toString();
    }

    /**
     * 이름을 숨길때 사용
     * 예) 홍길동  ==> 홍○동
     *
     * @param name
     * @return
     */
    public  String getHiddenName(String name) {
        String tmp = "";

        if(name.length() > 1) {
            tmp = name.substring(0, 1) + "○" + name.substring(2);
        } else {
            tmp = name;
        }

        return tmp;
    }

    /**
     * Compute the hash value to check for "real person" submission.
     *
     * @param  value  the entered value
     * @return  its hash value
     */
    public String rpHash(String value) {
        int hash = 5381;
        value = value.toUpperCase();
        for(int i = 0; i < value.length(); i++) {
            hash = ((hash << 5) + hash) + value.charAt(i);
        }
        return String.valueOf(hash);
    }


    /**
     * 만 20세 이상인지 확인
     *
     * @param year 년
     * @param month 월 (1~12)
     * @param day 일
     *
     * @return 만 20세 이상인지 여부
     */
    public boolean isOver20Years(int year, int month, int day) {
        Calendar birthday = Calendar.getInstance();
        birthday.set(Calendar.YEAR, year);
        birthday.set(Calendar.MONTH, month-1);
        birthday.set(Calendar.DAY_OF_MONTH, day);

        // 14년전 오늘 Calendar
        Calendar theday = Calendar.getInstance();
        theday.add(Calendar.YEAR, -19);

        return birthday.before(theday);
    }


    /**
     * 주민번호를 받아서 생년월일(yyyyMMdd)을 리턴한다.
     *
     * @param ssn 주민번호
     * @return 생년월일
     */
    public String getBirth8(String ssn) {
        if (ssn == null) {
            //"ssn is null."
            return null;
        }
        if (ssn.length() < 7) {
            //ssn.length()
            return null;
        }
        String prefix = "20";
        char sexCode = ssn.charAt(6);
        if (sexCode == '1' || sexCode == '2' ||
                sexCode == '5' || sexCode == '6') {
            prefix = "19";
        }
        return prefix + ssn.substring(0, 6);
    }

    /**
     * 응답을 HTML로 보낸다.
     *
     * @param response 응답
     * @param data HTML DATA
     */
    public static  void outHTML(HttpServletResponse response, String data, String charset) {
        charset = (charset == null) ? "utf-8" : charset;
        response.setContentType("text/html; charset=" + charset);
        //response.setStatus(response.SC_OK);  // 정상

        response.setStatus(HttpServletResponse.SC_OK);  // 정상

        printToClient(response, data, charset);
    }//:

    /**
     * data를 응답으로 보낸다.
     *
     * @param response 웹응답
     * @param data 응답데이타
     * @param charset 문자셋
     */
    public static  void printToClient(HttpServletResponse response, String data, String charset) {
        PrintWriter out = null;

        try {
            out = new PrintWriter(response.getWriter());
            out.print(data);
            out.flush();
        } catch (Exception e) {
            response.setStatus(500);
            log.error(e.getMessage());
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * 난수 생성
     * @param scerno 난수영역 시작 값
     * @param ecerno 난수영역 끝 값
     * @return 생성된 난수
     * @throws Exception
     */
    public int makeRandomInt(int scerno , int ecerno) {
        int result = 0;
        double cerno_range = ecerno - scerno +1d;

        try{
            Random randomGenerator = SecureRandom.getInstanceStrong();
            result= (int) (randomGenerator.nextDouble() * cerno_range + scerno);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return result;
    }


    /**
     * request 정보 로그 출력
     * @param request
     */
    @SuppressWarnings("rawtypes")
    public void logforRequestParameter(HttpServletRequest request){

        Map map = request.getParameterMap();
        Iterator it = map.keySet().iterator();
        Object key = null;
        String[] value = null;

        while(it.hasNext()){
            key = it.next();
            value = (String[]) map.get(key);
            for(int i = 0 ; i < value.length; i++) {
                // log.info("key ==> " + key +  " value ===> " +value[i]  + " index i ==> " + i);
            }
        }

    }

    /**
     * shell 명령어 실행
     * @param ContentsDwonloadPath
     * @param url
     * @return
     * @throws Exception
     */
    public boolean callShellCommand(String ContentsDwonloadPath, String url) {

        boolean result = true;
        String command  = ContentsDwonloadPath + "getContents.sh " + ContentsDwonloadPath + "temp.html " + url + " "+ ContentsDwonloadPath + "temputf8.html";

        log.debug("************************");
        log.debug("command  ==> {}", command);
        log.debug("************************");

        BufferedReader br = null;

        try {
            java.lang.Runtime runTime = java.lang.Runtime.getRuntime();
            java.lang.Process process = runTime.exec(command);
            //log.info(process.waitFor());

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            //for(String str; (str = br.readLine())!=null;){
            //log.info(str);
            //}

            if(process.exitValue()!=0){
                log.error("************************");
                log.error("셀이 정상종료 되지 않았습니다.");
                log.error("************************");
                result = false;
            }

            log.debug("************************");
            log.debug("프로그램 종료");
            log.debug("************************");
        } catch(IOException ioE) {
            log.error(ioE.getMessage());
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(IOException ioE) {
                    ioE.getMessage();
                }
            }
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj)
    {
        if( obj instanceof String ) return obj==null || "".equals(obj.toString().trim());
        else if( obj instanceof List ) return obj==null || ((List<?>)obj).isEmpty();
        else if( obj instanceof Map ) return obj==null || ((Map)obj).isEmpty();
        else if( obj instanceof Object[] ) return obj==null || Array.getLength(obj)==0;
        else return obj==null;
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }

    public static boolean isEquals(Object sobj, Object tobj)
    {
        if(CommonUtil.isNotEmpty(sobj))
        {
            return sobj.equals(tobj);
        }
        return false;
    }
    public static boolean isNotEquals(Object sobj, Object tobj)
    {
        return !isEquals(sobj,tobj);
    }

    public static String subString(String str, int start, int end)
    {
        return str.substring(start, end);
    }


    /**
     * joson JSONObject를  response에 전달한다.
     * @param response
     * @param jsononject
     * @param resultcode 결과코드 200 정상 500 에러 etc...
     * @throws Exception
     */
    public void sendjson(HttpServletResponse response, JSONPObject jsononject , int resultcode) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(resultcode);
        PrintWriter out = null;

        try {
            out = response.getWriter();

            out.println(jsononject.toString());
        } catch(IOException ioE) {
            log.error(ioE.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) out.close();
        }
    }

    /**
     * joson JSONArray를  response에 전달한다.
     * @param response
     * @param jsononarry
     * @param resultcode 결과코드 200 정상 500 에러 etc...
     * @throws Exception
     */
    public void sendjson(HttpServletResponse response, JSONArray jsononarry , int resultcode) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(resultcode);
        PrintWriter out = null;

        try {
            out = response.getWriter();

            out.println(jsononarry.toString());
        } catch(IOException ioE) {
            log.error(ioE.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) out.close();
        }
    }

    /**
     *
     * convertMapToObject
     *
     * 최초 생성일  : 2014. 4. 22. : 오후 4:09:10
     * file : CommonUtil.java
     * @param map
     * @param objClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMapToObject(Map map, Object objClass){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while(itr.hasNext()){

            keyAttribute = (String) itr.next();
            methodString = setMethodString + keyAttribute.substring(0,1).toUpperCase() + keyAttribute.substring(1);

            try {
                Class<? extends Object> paramClass = objClass.getClass();

                Method[] methods = paramClass.getDeclaredMethods();	//VO
                Method[] superClassmethods = paramClass.getSuperclass().getDeclaredMethods();	//BaseVO

                for(int i=0; i <= methods.length-1; i++) {
                    if(methodString.equals(methods[i].getName())) {
                        if(map.get(keyAttribute) instanceof Boolean) {
                            if((Boolean) map.get(keyAttribute)) {
                                methods[i].invoke(objClass, true);
                            } else {
                                methods[i].invoke(objClass, false);
                            }
                        }else if(!(map.get(keyAttribute) instanceof ArrayList)) {
                            if(map.get(keyAttribute) == null) {
                                methods[i].invoke(objClass, "");
                            }else {
                                methods[i].invoke(objClass, map.get(keyAttribute).toString());
                            }
                        }
                    }
                }

                for(int i=0; i <= superClassmethods.length-1; i++) {

                    if(methodString.equals(superClassmethods[i].getName())) {
                        if(map.get(keyAttribute) instanceof Boolean) {
                            if((Boolean) map.get(keyAttribute)) {
                                superClassmethods[i].invoke(objClass, true);
                            } else {
                                superClassmethods[i].invoke(objClass, false);
                            }
                        }else if(!(map.get(keyAttribute) instanceof ArrayList)) {
                            if(map.get(keyAttribute) == null) {
                                superClassmethods[i].invoke(objClass, "");
                            }else {
                                superClassmethods[i].invoke(objClass, map.get(keyAttribute).toString());
                            }
                        }
                    }
                }
            } catch (SecurityException e) {

            } catch (IllegalAccessException e) {

            } catch (IllegalArgumentException e) {

            } catch (InvocationTargetException e) {

            }
        }
        return objClass;
    }

    /**
     *
     * ConverObjectToMap
     *
     * 최초 생성일  : 2014. 4. 22. : 오후 4:09:18
     * file : CommonUtil.java
     * @param obj
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map ConverObjectToMap(Object obj){
        try {
            //Field[] fields = obj.getClass().getFields(); //private field는 나오지 않음.
            Field[] fields = obj.getClass().getDeclaredFields();
            Map resultMap = new HashMap();
            for(int i=0; i<=fields.length-1;i++){
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }

            log.debug("resultMap[{}]" , resultMap);

            return resultMap;
        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }

        return null;
    }

    /**
     * Object를 Map으로 변환, 키를 전부 소문자로
     * @author KIMDONGUK
     * @param obj
     * @return
     */
    public static Map<String, Object> ConvertObjectToMapLowerKey(Object obj){
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> resultMap = new HashMap<String, Object>();

            for(int i=0; i<=fields.length-1;i++) {
                fields[i].setAccessible(true);

                resultMap.put(fields[i].getName().toLowerCase(), fields[i].get(obj));
            }

            log.debug("resultMap[{}]" , resultMap);

            return resultMap;

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }

        return null;
    }

    /**
     * 렌덤 스트링 생성
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Description  : 숫자인지 여부 리턴
     * 최초 생성일  : 2014. 6. 16.
     * file         : CommonUtil.java
     * RETURN       : boolean
     * @param s
     * @return
     */
    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static Properties loadPropertiesFile(String pathandFile) {
        Properties prop = System.getProperties();
        String webRoot = prop.getProperty("web.root");
        log.info("tmpRoot=" + webRoot);
        String properties_dir = "";
        if (webRoot == null) {
            properties_dir = prop.getProperty("user.dir") + "/src/main/resources/properties/";
        } else {
            properties_dir = webRoot + "WEB-INF/classes/resources/properties/";
        }

        String properties_file = "";
        if (pathandFile == null) {
            properties_file = properties_dir + "globals.xml";
        } else {
            properties_file = pathandFile;
        }

        log.info("properties_file=" + properties_file);

        // read globals.properties
        Properties global_properties = new Properties();
        try {
            global_properties.load(new FileInputStream(properties_file));
        } catch (IOException ioe) {
            ioe.getMessage();
        }

        return global_properties;
    }

    /**
     * genexon.alert 호출
     */
    public static void sendGenexonAlert(HttpServletResponse response, String info, String contents, String message) {

        String sContents = "";
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            out = response.getWriter();
            sContents += "<script type='text/javascript' src='/resources/js/jquery/jquery.min.js'></script>";
            sContents += "<script type='text/javascript' src='/resources/js/common/genexon.js'></script>";
            sContents += "<script type='text/javascript'>";
            sContents += "$(document).ready(function(){";
            sContents += "genexon.initKendoUI_notification();";
            sContents += "genexon.alert('"+ info +"', '"+ contents +"', '" + message + "');";
            sContents += "})";
            sContents += "</script>";

            out.println(sContents);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) out.close();
        }
    }

    /**
     * alert 메시지 전송후 메인으로 이동
     * @param response
     * @param message
     */
    public static void sendAlertMsg(HttpServletResponse response, String message) {

        String sContents = "";
        PrintWriter out = null;

        try {
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();

            sContents = "<script type='text/javascript'>";
            sContents += "alert('" + message + "');";
            sContents += "window.parent.location.href='/index.go';";
            sContents += "</script>";

            out.println(sContents);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) out.close();
        }

    }

    // 임시비밀번호 만들기
    public static String temporaryPassword(int size) {

        StringBuffer buffer = new StringBuffer();
        SecureRandom random = new SecureRandom();
        random.setSeed(new Date().getTime());

        String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9".split(",");
        String SpecialChars[] = "~,!,@,#,$,%,^,&,*,(,),+,-".split(",");

        int SpCnt = random.nextInt(9);

        for (int i = 0; i < size; i++) {
            if (SpCnt == i) {
                // 특수문자
                buffer.append(SpecialChars[random.nextInt(SpecialChars.length)]);
            } else {
                // 영어+숫자
                buffer.append(chars[random.nextInt(chars.length)]);
            }
        }

        return buffer.toString();
    }

    public static String parseMessage(String message, String...args) {
        if (message == null || message.trim().length() <= 0)
            return message;

        if (args == null || args.length <= 0) return message;

        String[] splitMsgs = message.split("%");
        if (splitMsgs == null || splitMsgs.length <= 1)
            return message;

        for (int i = 0; i < args.length; i++) {
            String replaceChar = "%" + (i + 1);
            message = message.replaceFirst(replaceChar, args[i]);
        }
        return message;
    }

    /**
     * 페이징 쿼리에서 페이지 시작점 구하기
     * @return Integer
     */
    public static Integer getPageOffset(String page, String pageSize) {
        return ((Integer.parseInt(page) - 1) * Integer.parseInt(pageSize));
    }

    /**
     * request 매핑
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> setParams(HttpServletRequest request) throws Exception {
        Enumeration params = request.getParameterNames();
        Map<String, String> map = new HashMap<String, String>();
        String key = "";
        String mapData = "";

        while (params.hasMoreElements()){
            key = (String)params.nextElement();
            mapData = request.getParameter(key) == null ? "" : request.getParameter(key).toString();
            if(mapData.length() > 0){
                if("request".equals(key)){
                    continue;
                }
                map.put(key, mapData);
            }
        }

        return map;
    }

    // 주민번호로 생일추출
    public String setRealBirthday(String ssn) {
        String ssnSubFristNumber = ssn.substring(7, 8);
        String realBirthday = "";
        String year = "";
        String month = "";
        String day = "";

        if("1".equals(ssnSubFristNumber)){
            realBirthday = "19"+ssn.substring(0, 6);
        }else if("2".equals(ssnSubFristNumber)){
            realBirthday = "19"+ssn.substring(0, 6);
        }else if("3".equals(ssnSubFristNumber)){
            realBirthday = "20"+ssn.substring(0, 6);
        }else if("4".equals(ssnSubFristNumber)){
            realBirthday = "20"+ssn.substring(0, 6);
        }else if("5".equals(ssnSubFristNumber)){
            realBirthday = "19"+ssn.substring(0, 6);
        }else if("6".equals(ssnSubFristNumber)){
            realBirthday = "19"+ssn.substring(0, 6);
        }else if("7".equals(ssnSubFristNumber)){
            realBirthday = "20"+ssn.substring(0, 6);
        }else if("8".equals(ssnSubFristNumber)){
            realBirthday = "20"+ssn.substring(0, 6);
        }else{
            realBirthday = "19"+ssn.substring(0, 6);
        }

        year = realBirthday.substring(0, 4) + "-";
        month = realBirthday.substring(4, 6) + "-";
        day = realBirthday.substring(6);

        realBirthday = year+month+day;

        return realBirthday;
    }

    // 주민번호로 성별추출
    public String setGenderType(String ssn) {
        String genderType = "";
        String ssnSubFristNumber = ssn.substring(7, 8);

        if("1".equals(ssnSubFristNumber)){
            genderType = "1";
        }else if("2".equals(ssnSubFristNumber)){
            genderType = "2";
        }else if("3".equals(ssnSubFristNumber)){
            genderType = "1";
        }else if("4".equals(ssnSubFristNumber)){
            genderType = "2";
        }else if("5".equals(ssnSubFristNumber)){
            genderType = "5";
        }else if("6".equals(ssnSubFristNumber)){
            genderType = "6";
        }else if("7".equals(ssnSubFristNumber)){
            genderType = "5";
        }else if("8".equals(ssnSubFristNumber)){
            genderType = "6";
        }else{
            genderType = "";
        }
        return genderType;

    }

    /**
     * 일반적인 엑셀파일이 아닌(CSV, TSV 등..) 파일을 XLSX로 변경
     * @author KIMDONGUK
     * @since 2020-01-13
     * @return String
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static String convertNonExcelToXLSX(FileVO fileVO, Constants.UPLOADS u, String originFileFormat) throws Exception {
        String file_no = fileVO.getFile_id();

        String originalCSVFilePath = fileVO.getFile_path();
        String convertXLSXFilePath = Constants.getPATH(u) + file_no + ".xlsx";

        SXSSFWorkbook workBook =null;
        FileInputStream  fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        FileOutputStream fileOutputStream = null;

        try {
            workBook = new SXSSFWorkbook();
            Sheet sheet = workBook.createSheet("Sheet1");
            String currentLine = null;

            int RowNum = 0;

            fileInputStream = new FileInputStream(originalCSVFilePath);
            inputStreamReader = new InputStreamReader(fileInputStream, "EUC-KR");
            BufferedReader br = new BufferedReader(inputStreamReader);

            String spliter = "";

            if("tsv".equals(originFileFormat)) {
                spliter = "\t(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";
            }else if("txt".equals(originFileFormat)){
                spliter = "\\|(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";
            }else {
                spliter = ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";
            }

            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(spliter);

                Row currentRow = sheet.createRow(RowNum);

                for (int i = 0; i < str.length; i++) {
                    try {
                        //원수사 데이터 너무 이상해..
                        str[i] = str[i].replaceAll("\" \"", "");
                        str[i] = str[i].replaceAll("\"", "");
                        str[i] = str[i].replaceAll("　", "");	//띄어쓰기, 탭 아님...
                        str[i] = str[i].replaceAll("（", "(");
                        str[i] = str[i].replaceAll("）", ")");
                        str[i] = str[i].replaceAll("１", "1");	//아니 무슨 이런 데이터가 들어오냐..
                    }catch (Exception e) {

                    }

                    currentRow.createCell(i).setCellValue(str[i]);
                }

                RowNum++;
            }

            fileOutputStream = new FileOutputStream(convertXLSXFilePath);
            workBook.write(fileOutputStream);
            workBook.close();

        }catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        }finally {
            workBook.close();
            fileOutputStream.close();
            inputStreamReader.close();
            fileInputStream.close();
        }

        return convertXLSXFilePath;
    }

    /**
     * XLS 파일을 XLSX로 변경
     * @author KIMDONGUK
     * @since 2020-02-11
     * @return String
     * @throws Exception
     */
    public static String convertXLSToXLSX(FileVO fileVO, Constants.UPLOADS u) throws Exception {
        String file_no = fileVO.getFile_id();

        String originalCSVFilePath = fileVO.getFile_path();
        String convertXLSXFilePath = Constants.getPATH(u) + file_no + ".xlsx";

        InputStream in = null;
        Workbook wbIn = null;
        Workbook wbOut = null;
        OutputStream out = null;

        try {
            //입력 파일 세팅
            in = new BufferedInputStream(new FileInputStream(originalCSVFilePath));
            wbIn = new HSSFWorkbook(in);

            //저장될 파일 존재하는지 확인
            File outF = new File(convertXLSXFilePath);

            //똑같은 파일 있으면 삭제
            if (outF.exists())
                outF.delete();

            wbOut = new XSSFWorkbook();
            int sheetCnt = wbIn.getNumberOfSheets();

            for (int i = 0; i < sheetCnt; i++) {
                Sheet sIn = wbIn.getSheetAt(0);
                Sheet sOut = wbOut.createSheet(sIn.getSheetName());
                Iterator<Row> rowIt = sIn.rowIterator();

                while (rowIt.hasNext()) {
                    Row rowIn = rowIt.next();
                    Row rowOut = sOut.createRow(rowIn.getRowNum());

                    Iterator<Cell> cellIt = rowIn.cellIterator();

                    while (cellIt.hasNext()) {
                        Cell cellIn = cellIt.next();
                        Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), cellIn.getCellType());

                        switch (cellIn.getCellType()) {
                            case BLANK:
                                break;

                            case BOOLEAN:
                                cellOut.setCellValue(cellIn.getBooleanCellValue());
                                break;

                            case ERROR:
                                cellOut.setCellValue(cellIn.getErrorCellValue());
                                break;

                            case FORMULA:
                                cellOut.setCellFormula(cellIn.getCellFormula());
                                break;

                            case NUMERIC:
                                cellOut.setCellValue(cellIn.getNumericCellValue());
                                break;

                            case STRING:
                                cellOut.setCellValue(cellIn.getStringCellValue());
                                break;

                            default :
                                cellOut.setCellValue("");
                                break;
                        }

                        CellStyle styleIn = cellIn.getCellStyle();
                        CellStyle styleOut = cellOut.getCellStyle();

                        styleOut.setDataFormat(styleIn.getDataFormat());
                        cellOut.setCellComment(cellIn.getCellComment());
                    }
                }
            }

            out = new BufferedOutputStream(new FileOutputStream(outF));
            wbOut.write(out);

        }catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        }finally {
            if(wbOut != null)
                wbOut.close();

            if(out != null)
                out.close();

            if(wbIn != null)
                wbIn.close();

            if(in != null)
                in.close();

        }

        return convertXLSXFilePath;
    }

    /**
     *
     * ConvertVOToMap
     *
     * 최초 생성일  : 2020. 03. 20.
     * file : CommonUtil.java
     * @param  obj
     * @return Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static HashMap<String, Object> ConvertVOToMap(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();	//VO
            HashMap<String, Object> resultMap = new HashMap();

            for(int i=0; i <= fields.length-1; i++) {
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }

            fields = obj.getClass().getSuperclass().getDeclaredFields();	//BaseVO

            for(int i=0; i <= fields.length-1; i++) {
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }

            log.debug("resultMap[{}]" , resultMap);

            return resultMap;

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }

        return null;
    }

    public static boolean isTrueCustomerId(String customerId) throws ParseException
    {
        boolean flag = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date birthDate = dateFormat.parse(customerId.substring(0, 6));
        Date dateChk = dateFormat.parse("200930");

        customerId = customerId.replace("-", "");
        int chkId = (customerId.charAt(12) - '0');
        int foreignChk = (customerId.charAt(6) - '0');
        int chkNum = 0;

        // 외국인 주민번호일 경우 검증 전부 통과 = true
        if (foreignChk == 5 || foreignChk == 6 || foreignChk == 7 || foreignChk == 8) {
            flag = true;
            return flag;
        }

        // 주민&기준일 비교 시 기준일 넘기면 1 기준일 넘기지 않으면 0.
        if (birthDate.compareTo(dateChk) > 0) {
            flag = false;
            return flag;
        }

        int[] multiply = { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };
        for (int i = 0; i < multiply.length; i++) {
            chkNum += (customerId.charAt(i) - '0') * multiply[i];
        }
        chkNum = 11 - (chkNum % 11);

        // 체크값 11이면 1, 10이면 0
        if (chkNum == 11) {
            chkNum = 1;
        } else if (chkNum == 10) {
            chkNum = 0;
        }

        // 주민끝자리와 체크값 비교
        if (chkId == chkNum) {
            flag = true;
        }

        return flag;
    }

    public static void sendAlertToView(HttpServletResponse response, String info, String contents, String message) {

        String sContents = "";
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            out = response.getWriter();
            sContents += "<script type='text/javascript' src='/js/jquery-3.6.0.min.js'></script>";
            //sContents += "<script type='text/javascript' src='/js/common/genexon.js'></script>";
            sContents += "<script type='text/javascript'>";
            sContents += "$(document).ready(function(){";
            //sContents += "genexon.initKendoUI_notification();";
            //sContents += "genexon.alert('"+ info +"', '"+ contents +"', '" + message + "');";
            sContents += "alert('" + message + "');";
            sContents += "})";
            sContents += "</script>";

            out.println(sContents);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) out.close();
        }
    }
}
