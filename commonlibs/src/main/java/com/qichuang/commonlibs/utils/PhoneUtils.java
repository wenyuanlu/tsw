package com.qichuang.commonlibs.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.qichuang.commonlibs.basic.CustomBasicApplication;
import com.qichuang.commonlibs.common.PreferencesKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机相关的工具类<b>
 */
public class PhoneUtils {

    /**
     * 检测手机号是否合法 </br>含有空值和长度少于11位
     *
     * @param number 手机号码
     * @return true 合法 false 不合法
     */
    public static boolean checkPhoneNumber (String number) {
        if (TextUtils.isEmpty(number) || number.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前客户端版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode (Context context) {
        int versionCode = -1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {

        }
        return versionCode;
    }

    /**
     * 获取当前APP版本描述
     *
     * @param context
     * @return
     */
    public static String getAppVersionName (Context context) {
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
        }

        return versionName;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo (Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {

        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 检测网络是否可用
     *
     * @return true - 网络已连接或正在连接
     * false - 网络未连接
     */
    public static boolean isNetworkConnected (Context context) {
        ConnectivityManager cm   = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     * 获取当前的网络类型
     *
     * @return
     */
    public static String getNetworkType (Context context) {
        // TODO
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                LogUtils.LOGE("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                LogUtils.LOGE("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        LogUtils.LOGE("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet (Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     * @version 1.0
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI (Context context) {
        String imei = "";

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                imei = tm.getDeviceId();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //imei = getUDID(context);
                imei = "";
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TelephonyManager tm2 = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm2.getImei();
            } else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
        //        return "2323rq234123r";
    }

    /**
     * 获取屏幕的宽度
     *
     * @param content
     * @return
     */
    public static int getScreenWidth (Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        int            width          = displayMetrics.widthPixels;
        return width;
    }

    /**
     * 获取屏幕的高度
     *
     * @param content
     * @return
     */
    public static int getScreenHeight (Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        int            height         = displayMetrics.heightPixels;
        return height;
    }

    /**
     * 获取屏幕的密度
     *
     * @param content
     * @return
     */
    public static float getScreenDensity (Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        float          density        = displayMetrics.density;
        return density;
    }

    /**
     * 获取系统可用内存大小
     */
    public static int getAvalMem (Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            return manager.getMemoryClass();
        }
        return -1;

    }

    /**
     * 获取唯一设备ID
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("MissingPermission")
    public static String getDeviceToken (Context context) {
        String ANDROID_ID = "";
        try {
            ANDROID_ID = "" + Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Exception e) {

        }
        //        if (Build.VERSION.SDK_INT < 29) {
        //            //如果android sdk小于Q
        //            String deviceid = "" + tm.getDeviceId();
        //            String SimSerialNumber = "" + tm.getSimSerialNumber();
        //            String SerialNumber = "" + Build.SERIAL;
        //            String totalid = deviceid + SimSerialNumber + ANDROID_ID + SerialNumber;
        //            return totalid;
        //        }
        return ANDROID_ID;
    }

    //    @SuppressWarnings("deprecation")
    //    @SuppressLint("MissingPermission")
    //    public static String getAndroidId(Context context) {
    //        String ANDROID_ID = "" + Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    //        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    //        if (Build.VERSION.SDK_INT < 29) {
    //            //如果android sdk小于Q
    //            String deviceid = "" + tm.getDeviceId();
    //            return deviceid;
    //        }
    //        return ANDROID_ID;
    //    }

    @SuppressWarnings("deprecation")
    @SuppressLint("MissingPermission")
    public static String getAndroidId (Context context) {
        String ANDROID_ID = "";
        try {

            ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            if (ANDROID_ID.equals("9774d56d682e549c")) {

            }
            return ANDROID_ID;
            //            ANDROID_ID = "" + Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            //            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //            if (Build.VERSION.SDK_INT < 29) {
            //                //如果android sdk小于Q
            //                String deviceid = "" + tm.getDeviceId();
            //                return deviceid;
            //            }
        } catch (Exception e) {

        }
        return ANDROID_ID;
    }

    /**
     * MD5后的唯一设备ID
     **/
    public static String getMD5Token (Context context) {
        String src = getDeviceToken(context);
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(src.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取当前系统的android版本号
     */
    public static String getVersion () {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前系统的android版本
     */
    public static int getVersionSDK () {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机型号
     */
    public static String getModel () {
        return Build.MODEL;
    }

    /**
     * ping网络获取信息
     *
     * @param str
     * @return
     */
    public static List<String> Ping (String str) {
        List<String>   resault = new ArrayList<>();
        Process        process = null;
        BufferedReader in      = null;
        try {
            //ping -c 15 -w 100  中  ，-c 是指ping的次数 15是指ping 15次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
            process = Runtime.getRuntime().exec("ping -c 15 -w 100 " + str);

            InputStream input = process.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            String       line       = "";
            List<String> listBuffer = new ArrayList<>();
            while ((line = in.readLine()) != null) {

                listBuffer.add(line);
            }

            for (int i = 0; i < listBuffer.size(); i++) {
                String data       = listBuffer.get(i);
                int    startIndex = data.indexOf("time=");
                if (startIndex != -1) {
                    int endIndex = data.indexOf("ms");
                    data = data.substring(startIndex, endIndex).trim();
                    data = data.replace("time=", "");
                    resault.add(data);
                } else {
                    startIndex = data.indexOf("received");
                    int endIndex = data.indexOf("packet loss");
                    if (startIndex != -1 && endIndex != -1) {
                        int startIndex1 = data.indexOf("time");
                        int endIndex1   = data.indexOf("ms");
                        if (startIndex != -1 && endIndex != -1) {
                            String time = data.substring(startIndex1, endIndex1);
                            time = time.replace("time", "").trim();
                            resault.add(time);
                        }
                        String packetLoss = data.substring(startIndex, endIndex);
                        packetLoss = packetLoss.replace("received,", "").trim();
                        resault.add(packetLoss);
                    }
                }

            }
        } catch (IOException e) {

        } finally {
            if (process != null) {
                process.destroy();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        if (resault.size() == 0) {
            resault.add("100%");
        }

        return resault;
    }

    /**
     * 返回运营商 需要加入权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/> <BR>
     *
     * @return 1, 代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
     * @author youzc@yiche.com
     */
    @SuppressLint("MissingPermission")
    public static int getOperatorId (Context context) {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        int    operatorId = 0;
        String IMSI       = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
        System.out.println(IMSI);
        if (TextUtils.isEmpty(IMSI)) {
            operatorId = 0;
        } else {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                operatorId = 1;
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                operatorId = 2;
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
                operatorId = 3;
            }
        }

        return operatorId;
    }

    public static String getOperatorName (Context context) {
        int    operatorId   = getOperatorId(context);
        String operatorName = "";
        switch (operatorId) {
            case 1:
                operatorName = "中国移动";
                break;
            case 2:
                operatorName = "中国联通";
                break;
            case 3:
                operatorName = "中国电信";
                break;
            default:
                operatorName = "未知";
                break;
        }
        return operatorName;
    }

    /**
     * get CPU rate
     *
     * @return
     */
    public static String getProcessCpuRate () {
        int            rate    = 0;
        BufferedReader br      = null;
        Process        process = null;
        try {
            String Result;
            process = Runtime.getRuntime().exec("top -n 1");
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    if (CPUusr.length > 1) {
                        String[] CPUusage = CPUusr[0].split("User");
                        String[] SYSusage = CPUusr[1].split("System");
                        if (SYSusage.length > 1) {
                            rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {

        } finally {
            if (process != null) {
                process.destroy();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
        return rate + "%";
    }

    public static long getAvailMemory (Context context) {// 获取android当前可用内存大小

        ActivityManager            am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return mi.availMem;
        //Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
    }

    public static long getTotalMemory () {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String         path    = "/proc/meminfo";
        String         content = null;
        BufferedReader br      = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息
        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal * 1024;
    }

    /**
     * 获取手机内部存储的使用率
     *
     * @return
     */
    public static String getStorageUsage () {
        String storageUsage                = "";
        long   availableInternalMemorySize = 0;
        long   totalInternalMemorySize     = 0;
        File   path                        = Environment.getDataDirectory();
        StatFs stat                        = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableInternalMemorySize = stat.getAvailableBytes();
            totalInternalMemorySize = stat.getTotalBytes();
        } else {
            long blockSize       = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long totalBlocks     = stat.getBlockCount();   //block总数
            totalInternalMemorySize = totalBlocks * blockSize;
            availableInternalMemorySize = availableBlocks * blockSize;
        }
        //        storageUsage = GeneralUtils.roundUp("" + (1 - (totalInternalMemorySize - availableInternalMemorySize) / (float) totalInternalMemorySize) * 100, 2) + "%";
        return storageUsage;
    }

    /**
     * 获取手机内部总空间大小
     *
     * @return
     */
    public static long getTotalInternalMemorySize () {
        File   path = Environment.getDataDirectory();//Gets the Android data directory
        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return stat.getTotalBytes();
        } else {
            long blockSize   = stat.getBlockSize();      //每个block 占字节数
            long totalBlocks = stat.getBlockCount();   //block总数
            return totalBlocks * blockSize;
        }
    }

    /**
     * 获取App名字
     *
     * @return
     */
    public static String getApplicationName () {
        PackageManager  packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = CustomBasicApplication.instance.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(CustomBasicApplication.instance.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getMac (Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //            LogUtils.LOGE("=====", "6.0以下");
            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //            LogUtils.LOGE("=====", "6.0以上7.0以下");
            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //            LogUtils.LOGE("=====", "7.0以上");
            if (!TextUtils.isEmpty(getMacAddress())) {
                //                LogUtils.LOGE("=====", "7.0以上1");
                strMac = getMacAddress();
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                //                LogUtils.LOGE("=====", "7.0以上2");

                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                //                LogUtils.LOGE("=====", "7.0以上3");
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }

        return "02:00:00:00:00:00";


    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo (Context context) {
        WifiManager wifi  = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo    winfo = wifi.getConnectionInfo();
        String      mac   = winfo.getMacAddress();
        return mac;

    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress (Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }
        String str       = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir    = new InputStreamReader(pp.getInputStream());
            LineNumberReader  input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            LogUtils.LOGE("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.LOGE("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());
            }

        }
        return macSerial;
    }

    private static String getMacAddress0 (Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                LogUtils.LOGE("----->" + "NetInfoManager",
                        "getMacAddress0:" + e.toString());
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized (Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            LogUtils.LOGE("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
                    + "access wifi state is enabled");
            return true;
        } else
            return false;
    }

    private static String loadFileAsString (String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String     text   = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString (Reader reader) throws Exception {
        StringBuilder builder    = new StringBuilder();
        char[]        buffer     = new char[4096];
        int           readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress () {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress () {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress () {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress () {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String           hardWareAddress = null;
        NetworkInterface iF              = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString (byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }
    /**
     * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
     *
     */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox () {
        String result = "";
        String Mac    = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd (String cmd, String filter) {
        String result = "";
        String line   = "";
        try {
            Process           proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is   = new InputStreamReader(proc.getInputStream());
            BufferedReader    br   = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning (Context context, final String className) {
        ActivityManager                          activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info            = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0)
            return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName()))
                return true;
        }
        return false;
    }

    public static boolean isXiaomi () {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    public static boolean isSansung () {
        return "samsung".equals(Build.MANUFACTURER);
    }

    public static boolean isHuawei () {
        return ("HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)) || ("HONOR".equalsIgnoreCase(Build.MANUFACTURER));
    }

    public static boolean isMeizu () {
        return "Meizu".equals(Build.MANUFACTURER);
    }

    public static boolean isOppoVivo () {
        return ("vivo".equalsIgnoreCase(Build.MANUFACTURER) || "oppo".equalsIgnoreCase(Build.MANUFACTURER));
    }


    public static String getEmui () {
        try {
            Class  localClass = Class.forName("android.os.SystemProperties");
            String str        = (String) localClass.getDeclaredMethod("get", new Class[]{String.class}).invoke(localClass, new Object[]{"ro.build.version.emui"});
            if ((!TextUtils.isEmpty(str)) && (str.contains("EmotionUI_")) && (str.length() >= 13)) {
                str = str.substring(10, 13);
            }
            return str;
        } catch (Exception localException) {
            for (; ; ) {
                localException.printStackTrace();
                String str = "0";
            }
        }
    }

    public static String getChannel (Context context) {
        try {
            PackageManager  pm      = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (NameNotFoundException ignored) {
        }
        return "";
    }


    /**
     * 获取唯一标识码
     *
     * @param mContext
     * @return
     */
    public synchronized static String getUDID (Context mContext) {
        String uuid = null;
        if (uuid == null) {
            if (uuid == null) {
                String uuIds = PreferencesUtils.getString(PreferencesKey.UUID, null);

                if (uuIds != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = uuIds;
                } else {

                    final String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
                    // unless it's not available, then fallback on a random number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        } else {
                            @SuppressLint("MissingPermission") final String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                        }
                        if (!TextUtils.isEmpty(uuid)) {
                            try {
                                String uuids[] = uuid.split("-");
                                if (uuids != null && uuids.length > 2) {
                                    uuid = uuids[0] + uuids[1] + uuids[2];
                                } else {
                                    uuid.replaceAll("-", "");
                                }
                            } catch (Exception e) {
                                uuid.replaceAll("-", "");
                            }
                        }
                        PreferencesUtils.putString(PreferencesKey.UUID, uuid);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    // Write the value out to the prefs file
                    //                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }
            }
        }

        return uuid;
    }

    public static String getUserAgent () {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = System.getProperty("http.agent");
            } catch (Exception e) {
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * ping代理地址
     */
    public static boolean pingProxy(String ip) {
        try {
            if (!TextUtils.isEmpty(ip)) {
                //ping -c 3 -w 10——>代表ping 3 次 超时时间为10秒
                Process p = Runtime.getRuntime().exec("ping -c 1 -w 1 " + ip);
                return p.waitFor() == 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
