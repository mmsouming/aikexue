package com.dy.aikexue.ssolibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.OrgInfo;
import com.dy.photopicker.util.PhotoPicker;
import com.dy.photopicker.util.theme.BaseTheme;
import com.dy.photopicker.util.theme.GreenTheme;
import com.dy.sdk.bean.BeanSelectKeyValue;
import com.dy.sdk.utils.CFileTool;
import com.dy.sdk.utils.GsonUtil;

import org.cny.awf.net.http.H;

import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zengdl
 * @since 2015-02-11
 */
public class Tools {

    public static String getOrgInfoBaseStr(OrgInfo orgInfo, Context context) {

        String scaleStr = "";
        String industryStr = "";
        String city = "";
        if (orgInfo != null) {
//            0代表初创型20人一下，1 20-50， 2 50-100， 3 100-500，4 500-1000， 5 1000-
            int scale = orgInfo.getScale();
            List<String> industry = orgInfo.getIndustry();
            switch (scale) {
                case 1:
                    scaleStr = context.getString(R.string.peopleOne);
                    break;
                case 2:
                    scaleStr = context.getString(R.string.peopleTwo);
                    ;
                    break;
                case 3:
                    scaleStr = context.getString(R.string.peopleThree);
                    break;
                case 4:
                    scaleStr = context.getString(R.string.peopleFour);
                    break;
                case 5:
                    scaleStr = context.getString(R.string.peopleFive);
                    break;
            }
            if (industry != null && !industry.isEmpty()) {
                industryStr = industry.get(0);
            }
            if (orgInfo.getProvince() != null) {
                city += orgInfo.getProvince();
            }
            if (orgInfo.getCity() != null) {
                city += orgInfo.getCity();
            }

        }

        String s = "";
        if (!scaleStr.isEmpty()) {
            s += scaleStr;
        }
        if (!industryStr.isEmpty()) {
            if (!scaleStr.isEmpty()) {
                s += " / ";
            }
            s += industryStr;
        }
        if (!city.isEmpty()) {
            if (!scaleStr.isEmpty() || !industryStr.isEmpty()) {
                s += " / ";
            }
            s += city;
        }

        return s;
    }

    /**
     * 根据给定的长度截取字符串
     *
     * @param str
     * @param lenght
     * @return
     */
    public static String subString(String str, int lenght) {
        if (str == null) {
            return "";
        }
        if (str.length() < lenght || str.length() == lenght) return str;

        String newStr = str.substring(0, lenght);
        str = newStr + ".....";

        return str;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3458]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isValidEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        if (TextUtils.isEmpty(email))
            return false;
        else
            return m.matches();
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean existPackage(String packageName, Context context) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    /**
     * 判断是否是正确的手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isRightMobilePhone(String phone) {
        if (phone == null) {
            return false;
        }
        String head1 = "";
        String head2 = "";

        // 去除前后的空白
        phone = phone.trim();

        // 判断输入的电话号码是否合法
        if (phone.length() < 11) {
            return false;
        } else {
            // 处理国内的+86开头
            if (phone.startsWith("+")) {
                phone = phone.substring(1);
            }
            if (phone.startsWith("86")) {
                phone = phone.substring(2);
            }
        }
        // 去除+86后电话号码应为11位
        if (phone.length() != 11) {

            return false;
        }
        // 判断去除+86后剩余的是否全为数字
        if (!isNum(phone)) {
            System.out.println(" not num");
            return false;
        }
        // 截取前3或前4位电话号码，判断运营商
        head1 = phone.substring(0, 3);
        head2 = phone.substring(0, 4);

        // 移动前三位
        boolean cmcctemp3 = head1.equals("135") || head1.equals("136")
                || head1.equals("137") || head1.equals("138")
                || head1.equals("139") || head1.equals("147")
                || head1.equals("150") || head1.equals("151")
                || head1.equals("152") || head1.equals("157")
                || head1.equals("158") || head1.equals("159")
                || head1.equals("182") || head1.equals("183")
                || head1.equals("184") || head1.equals("187")
                || head1.equals("188") || head1.equals("178");

        if (cmcctemp3) {
            return true;
        }
        // 移动前4位
        boolean cmcctemp4 = head2.equals("1340") || head2.equals("1341")
                || head2.equals("1342") || head2.equals("1343")
                || head2.equals("1344") || head2.equals("1345")
                || head2.equals("1346") || head2.equals("1347")
                || head2.equals("1348");

        if (cmcctemp4) {
            return true;
        }
        // 联通前3位
        boolean unicomtemp = head1.equals("130") || head1.equals("131")
                || head1.equals("132") || head1.equals("145")
                || head1.equals("155") || head1.equals("156")
                || head1.equals("185") || head1.equals("186") || head1.equals("176");

        if (unicomtemp) {
            return true;
        }
        // 电信前3位
        boolean telecomtemp = head1.equals("133") || head1.equals("153") || head1.equals("149")
                || head1.equals("180") || head1.equals("189") || head1.equals("181") || head1.equals("177");

        if (telecomtemp) {
            return true;
        }
        //虚拟运营商
        boolean virtualtemp = head1.equals("170");

        if (virtualtemp) {
            return true;
        }

        return false;
    }

    /*
     * 判断输入的是否为数字
	 *
	 * @返回true说明是数字，false说明不全是数字
	 */
    private static boolean isNum(String phoneNum) {
        for (int i = 0; i < phoneNum.length(); i++) {
            if (!Character.isDigit(phoneNum.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true 有网络，false 无
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }


    /**
     * 身份证验证的工具（支持5位或18位省份证）
     * 身份证号码结构：
     * 17位数字和1位校验码：6位地址码数字，8位生日数字，3位出生时间顺序号，1位校验码。
     * 地址码（前6位）：表示对象常住户口所在县（市、镇、区）的行政区划代码，按GB/T2260的规定执行。
     * 出生日期码，（第七位 至十四位）：表示编码对象出生年、月、日，按GB按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 顺序码（第十五位至十七位）：表示在同一地址码所标示的区域范围内，对同年、同月、同日出生的人编订的顺序号，
     * 顺序码的奇数分配给男性，偶数分配给女性。
     * 校验码（第十八位数）：
     * 十七位数字本体码加权求和公式 s = sum(Ai*Wi), i = 0,,16，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值.Wi:表示第i位置上的加权因.Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；
     * 计算模 Y = mod(S, 11)
     * 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    //    final static int[] zoneNumber={11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,71,81,82,91};
    final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10,
            5, 8, 4, 2};

    /**
     * 身份证验证
     *
     * @param certNo 号码内容
     * @return 是否有效 null和"" 都是false
     */
    public static boolean isRightIDCardNum(String certNo) {
        if (certNo == null || (certNo.length() != 15 && certNo.length() != 18))
            return false;
        final char[] cs = certNo.toUpperCase().toCharArray();
        //校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X')
                break;//最后一位可以 是X或x
            if (cs[i] < '0' || cs[i] > '9')
                return false;
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }

        //校验区位码
        if (!zoneNum.containsKey(Integer.valueOf(certNo.substring(0, 2)))) {
            return false;
        }

        //校验年份
        String year = certNo.length() == 15 ? getIdcardCalendar() + certNo.substring(6, 8) : certNo.substring(6, 10);

        final int iyear = Integer.parseInt(year);
        if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR))
            return false;//1900年的PASS，超过今年的PASS

        //校验月份
        String month = certNo.length() == 15 ? certNo.substring(8, 10) : certNo.substring(10, 12);
        final int imonth = Integer.parseInt(month);
        if (imonth < 1 || imonth > 12) {
            return false;
        }

        //校验天数
        String day = certNo.length() == 15 ? certNo.substring(10, 12) : certNo.substring(12, 14);
        final int iday = Integer.parseInt(day);
        if (iday < 1 || iday > 31)
            return false;

        //校验"校验码"
        if (certNo.length() == 15)
            return true;
        return cs[cs.length - 1] == PARITYBIT[power % 11];
    }

    /**
     * 校验年份
     *
     * @return
     */
    public static int getIdcardCalendar() {
        GregorianCalendar curDay = new GregorianCalendar();
        int curYear = curDay.get(Calendar.YEAR);
        int year2bit = Integer.parseInt(String.valueOf(curYear).substring(2));
        return year2bit;
    }

    public static String formatMilliseconds2Time(long time, String format) {
        String formatTime = "";
        try {
            SimpleDateFormat sdr = new SimpleDateFormat(format, Locale.CHINA);

            Calendar nowCal = Calendar.getInstance();
            TimeZone localZone = nowCal.getTimeZone();
            //设定SDF的时区为本地
            sdr.setTimeZone(localZone);

            formatTime = sdr.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            return formatTime;
        }
        return formatTime;
    }

    public static long formatTime2Milliseconds(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date.getTime();
    }


    /**
     * 获取字符串的md5值 （十六进制，长度为32位）。MessageDigest提供信息摘要算法的功能，
     *
     * @param str
     * @return 返回md5串
     */
    public static String encodeStrByMd5(String str) {
        String md5Str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定byte[]更新摘要
            md.update(str.getBytes());
            // 完成计算，返回结果数组
            byte[] b = md.digest();
            md5Str = byteArrayToHex(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }

    /**
     * 将字节数组转为十六进制字符串
     *
     * @param bytes
     * @return 返回16进制字符串
     */
    public static String byteArrayToHex(byte[] bytes) {
        // 字符数组，用来存放十六进制字符
        char[] hexReferChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 一个字节占8位，一个十六进制字符占4位；十六进制字符数组的长度为字节数组长度的两倍
        char[] hexChars = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            // 取字节的高4位
            hexChars[index++] = hexReferChars[b >>> 4 & 0xf];
            // 取字节的低4位
            hexChars[index++] = hexReferChars[b & 0xf];
        }
        return new String(hexChars);
    }

    private static List<BeanSelectKeyValue> locationDataList;

    /**
     * 得到所有省市数据
     *
     * @return
     */
    public static List<BeanSelectKeyValue> getProvinceCityData() {
        if (locationDataList != null) {
            return locationDataList;
        }

        locationDataList = new ArrayList<>();
        List<Map> allProvinces = getLocationFormatData();
        if (allProvinces != null) {
            for (int i = 0; i < allProvinces.size(); i++) {
                //第一层，省份
                Map m = allProvinces.get(i);
                BeanSelectKeyValue oneBean = new BeanSelectKeyValue();
                String keyProvince = (String) m.get("p");
                oneBean.setName(keyProvince);
                if (i == 0) {
                    oneBean.setSelect(true);
                }

                List<BeanSelectKeyValue> twoList = null;
                List<Map> cities = (List<Map>) m.get("c");
                if (cities != null) {
                    twoList = new ArrayList<>();
                    for (Map n : cities) {
                        //第二层，城市
                        BeanSelectKeyValue twoBean = new BeanSelectKeyValue();
                        String keyCity = (String) n.get("n");
                        twoBean.setName(keyCity);
                        twoList.add(twoBean);
                    }
                }

                oneBean.setSubList(twoList);
                locationDataList.add(oneBean);
            }
        }
        return locationDataList;
    }

    public static List<Map> getLocationFormatData() {
        List<Map> mCities = null;
        if (mCities != null) {
            return mCities;
        }
        try {
            InputStream is = CFileTool.loadFileFromAsset(H.CTX, "cities.json");
            String str = CFileTool.readString(is);
            Map t = GsonUtil.fromJson(str, Map.class);
            mCities = (List<Map>) t.get("citylist");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCities;
    }

    /**
     * 判断软键盘是否正在显示
     */
    public static boolean isSoftKeyBoardShowing(View edit) {
//        boolean bool = false;
//        InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            bool = true;
//        }
//        return bool;
        return ((Activity) edit.getContext()).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    }

    /**
     * 显示或隐藏软键盘
     * 如果需要在AndroidManifest里面设置，直接弹出键盘，用android:windowSoftInputMode="stateVisible|adjustResize"
     */
    public static void showSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        if (isSoftKeyBoardShowing(view)) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 　　* 计算gridview高度
     * 　　* @param gridView
     */
    public static int setGridViewHeightBasedOnChildren(GridView gridView, int columns, int spaceHeight) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int rows;
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }

        View item = listAdapter.getView(0, null, gridView);
        item.measure(0, 0);
        int oneHeight = item.getMeasuredHeight();
        int spaceTotal = spaceHeight * rows;
        int totalHeight = oneHeight * rows + spaceTotal;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;// 最后加上分割线总高度
        gridView.setLayoutParams(params);
        return params.height;
    }

    /**
     * 返回字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStringNull(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return
     */
    public static boolean isListNotNull(List list) {
        return list != null && list.size() > 0;
    }

    /**
     * 获取res string 资源
     *
     * @param context
     * @param id
     * @return
     */
    public static String getResString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static int getResColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    /**
     * 打开选择图片
     *
     * @param activity
     * @param max
     */
    public static void openSelectPicture(Activity activity, int max) {
        BaseTheme theme = new GreenTheme();
        theme.setIsCanTakePhoto(true);
        theme.setContentType(BaseTheme.TYPE_IMAGE);
        theme.setMaxSelectImageSize(max);
        PhotoPicker.startPhotoPicker(activity, theme);
    }
}
