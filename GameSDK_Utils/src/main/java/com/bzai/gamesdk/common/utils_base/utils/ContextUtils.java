package com.bzai.gamesdk.common.utils_base.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bzai on 2018/4/16.
 * <p>
 * Desc:
 *
 *   用于获取系统的一些信息
 */

public class ContextUtils {

    public static Object invokeTelephonyManagerMethod(String methodName,
                                                      Context cxt) {
        try {
            Method m = Context.class.getMethod("getS" + "yste" + "mSer"
                    + "vice", new Class<?>[]{String.class});
            Object phone = m.invoke(cxt, new Object[]{"phone"});

            Method m2 = phone.getClass().getMethod(methodName,
                    (Class<?>[]) null);
            return m2.invoke(phone, (Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Object invokeTelephoneManagerMethod(String methodName,
                                                      Class<?>[] paramTypes, Object[] params, Context cxt) {
        try {
            Method m = Context.class.getMethod("getS" + "yste" + "mSer"
                    + "vice", new Class<?>[]{String.class});
            Object phone = m.invoke(cxt, new Object[]{"phone"});

            Method m2 = phone.getClass().getMethod(methodName, paramTypes);
            return m2.invoke(phone, (Object[]) params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 返回设备类型
     * @return
     */
    public static String getDeviceName(){
        return "android";
    }


    public static String getVersionName(Context appContext) {
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(
                    appContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    public static final String UNKNOWN = "unknown";
    public static final String WIFI = "wifi";
    public static final String MOBILE = "mobile";

    /***
     * Return {@link #UNKNOWN} if the network type can not be accessed.
     *
     * @return 'wifi' or 'uninet' etc.
     */
    public static String getNetworkType(Context context) {
        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return UNKNOWN;
        }
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService("connectivity");
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return UNKNOWN;
        }
        int net_type = networkInfo.getType();
        if (net_type == ConnectivityManager.TYPE_MOBILE) {
            networkInfo = manager.getNetworkInfo(0);
            String netString = networkInfo.getExtraInfo();
            if (TextUtils.isEmpty(netString)) {
                return UNKNOWN;
            } else {
                return netString.length() > 10 ? netString.substring(0, 10)
                        : netString;
            }
        } else {
            return WIFI;
        }
    }

    public static final float UI_DESIGN_DENSITY = 1.5F;

    /**
     * dip2px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }

    public static int fitToImage(Context context, float uiValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((uiValue / UI_DESIGN_DENSITY) * scale + 0.5f);// 因为图是按照480*800做的，密度为1.5
    }

    /**
     * 拿到resolution as width X height
     *
     * @param context
     * @return
     */
    public static String getResolutionAsString(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int widthPixels = display.getWidth();
        int heightPixels = display.getHeight();
        return widthPixels < heightPixels ? widthPixels + "X" + heightPixels
                : heightPixels + "X" + widthPixels;
    }

    /**
     * Check whether the specified permission is granted to the current package.
     *
     * @param context
     * @param permissionName The permission.
     * @return True if granted, false otherwise.
     */
    public static boolean checkPermission(Context context, String permissionName) {
        PackageManager packageManager = context.getPackageManager();
        String pkgName = context.getPackageName();
        return packageManager.checkPermission(permissionName, pkgName) == PackageManager.PERMISSION_GRANTED;
    }

    public static String getDeviceId(Context context) {
        String imei = getIMEI(context);
        if (!imei.equals(UNKNOWN)) {
            return imei;
        }
        return getLocalMacAddress(context);
    }

    /**
     * Get IMEI of the device. If it has no IMEI or no
     * {@link Manifest.permission#READ_PHONE_STATE} permission or it is an
     * emulator, {@link #UNKNOWN} will be returned.
     */
    public static String getIMEI(Context context) {
        String id = null;
        if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            id = (String) invokeTelephonyManagerMethod("getD" + "evi" + "ceId",
                    context);
        }
        if (TextUtils.isEmpty(id) || isZero(id)) {
            return UNKNOWN;
        }

        return id;
    }

    /**
     * Return the WIFI-MAC address.
     *
     * @return {@link #UNKNOWN} will be returned if any unexpected occurs.
     */
    public static String getLocalMacAddress(Context context) {
        if (!checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            return UNKNOWN;
        }
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (null != info) {
            String mac = info.getMacAddress();
            if (!TextUtils.isEmpty(mac)) {
                return mac;
            }
        }

        return UNKNOWN;
    }

    // get cpu frequence
    public static long getCpuFre() {
        // #cat "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
        // /proc/cpuinfo

        String cpuFreFile = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
        return readLong(cpuFreFile);
    }

    private static long readLong(String file) {
        RandomAccessFile raf = null;

        try {
            raf = getFile(file);
            return Long.valueOf(raf.readLine());
        } catch (Exception e) {
            return 0;
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static RandomAccessFile getFile(String filename) throws IOException {
        File f = new File(filename);
        return new RandomAccessFile(f, "r");
    }

    private static boolean isZero(String id) {
        for (int i = 0; i < id.length(); i++) {
            char index = id.charAt(i);
            if (index != '0')
                return false;
        }
        return true;
    }

    /**
     * Return the current application's label.
     */
    public static String getLabel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(
                    context.getPackageName(), 0);
            String label = info.loadLabel(pm).toString();
            return label;
        } catch (PackageManager.NameNotFoundException e) {
            // Should never happen
        }
        return null;
    }

    public final static int SIM_TYPE_CMCC = 1;
    public final static int SIM_TYPE_UNICOM = 2;
    public final static int SIM_TYPE_TELECOM = 3;
    public final static int SIM_TYPE_UNKNOWN = -1;

    /**
     * 取sim卡所属运营商
     *
     * @return 如果判定不了，则返回{@link #SIM_TYPE_UNKNOWN}
     * @see {@link #SIM_TYPE_CMCC}, {@link #SIM_TYPE_UNICOM},
     * {@link #SIM_TYPE_TELECOM}
     */
    public static int getSimCardType(Context context) {
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return SIM_TYPE_UNKNOWN;
        }
        int state = (Integer) invokeTelephonyManagerMethod("getSi" + "mState",
                context);
        if (!(state == TelephonyManager.SIM_STATE_READY)) {
            return SIM_TYPE_UNKNOWN;
        }
        String imsi = (String) invokeTelephonyManagerMethod("getSu" + "bscr"
                + "ibe" + "rId", context);
        if (TextUtils.isEmpty(imsi)) {
            return SIM_TYPE_UNKNOWN;
        }
        if (imsi.contains("46000") || imsi.contains("46002")
                || imsi.contains("46007")) {
            return SIM_TYPE_CMCC;
        } else if (imsi.contains("46001") || imsi.contains("46006")) {
            return SIM_TYPE_UNICOM;
        } else if (imsi.contains("46003") || imsi.contains("46005")) {
            return SIM_TYPE_TELECOM;
        }
        return SIM_TYPE_UNKNOWN;
    }

    public static String getIMSI(Context context) {
        String id = null;
        if (checkPermission(context,
                Manifest.permission.READ_PHONE_STATE)) {
            id = (String) invokeTelephonyManagerMethod("getS" + "ubscr"
                    + "iberId", context);
        }
        if (TextUtils.isEmpty(id) || isZero(id)) {
            return UNKNOWN;
        }

        return id;
    }

    /**
     * Get IP address of the device.
     *
     * @return {@link #UNKNOWN} will be returned if any unexpected occurs.
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            if (en == null) {
                return UNKNOWN;
            }
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                if (enumIpAddr == null) {
                    continue;
                }

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return UNKNOWN;
    }

    /**
     * public static final int ORIENTATION_UNDEFINED = 0; public static final
     * int ORIENTATION_PORTRAIT = 1; public static final int
     * ORIENTATION_LANDSCAPE = 2; public static final int ORIENTATION_SQUARE =
     * 3;
     *
     * @param context
     * @return
     */
    public static int getOrientation(Context context) {
        android.content.res.Configuration configuration = context
                .getResources().getConfiguration();
        return configuration.orientation;
    }

    /**
     * Install the specified file.
     */
    public static void installPackage(Context context, File file) {
        try {
            Uri uri = Uri.fromFile(file);
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            context.startActivity(installIntent);
        } catch (Exception e) {
            e.printStackTrace();
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * 是否是横屏
     */
    public static boolean isLandScape(Context activity) {
        boolean b = false;
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            b = true;
        }
        return b;
    }


    /**
     * 判断是否是手机号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNO(String phoneNum) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        String tempPhoneNum = phoneNum.replace(" ", "");
        if (tempPhoneNum.startsWith("+86")) {
            tempPhoneNum = tempPhoneNum.replace("+86", "");
        }
        Matcher m = p.matcher(tempPhoneNum);
        return m.matches();
    }

    /**
     * 判断是否是手机设备
     *
     * @param context
     * @return
     */
    public static boolean isPhoneDevice(Context context) {
        boolean isPhoneDevice = true;
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            isPhoneDevice = false;
        } else {
            isPhoneDevice = true;
        }
        return isPhoneDevice;
    }

    /**
     * 判断网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断SD card 是否可读
     */
    public static boolean isSdcardReadable(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            // 如果是KitKat，先检查是否有read权限，如果没有，则直接返回false
            if (!checkPermission(context,
                    "android.permission.READ_EXTERNAL_STORAGE")) {
                return false;
            }
        }
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    /**
     * 获取特定包名的签名
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String md5Sign(Context context, String packageName) {

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            StringBuilder sb = new StringBuilder();
            for (Signature signature : signatures) {
                sb.append(signature.toCharsString());
            }
            return Md5Utils.getMd5(sb.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.debug_e(e.toString());
        }
        return null;
    }

    /**
     * 拿到当前包的签名的MD5值
     *
     * @param context
     * @return
     */
    public static String md5Sign(Context context) {
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            StringBuilder sb = new StringBuilder();
            for (Signature signature : signatures) {
                sb.append(signature.toCharsString());
            }
            return Md5Utils.getMd5(sb.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDensityInt(Context context) {
        return (int) (getDensity(context) * 240);
    }

    private static float sDensity = -1.0f;
    /**
     * The logical density of the display.
     */
    public static float getDensity(Context context) {
        if (sDensity < 0.0f) {
            sDensity = context.getResources().getDisplayMetrics().density / 1.5f;
        }
        return sDensity;
    }

    public static double getPhysicalScreenSize(Context context) {
        int[] screenWH = getResolution(context);
        double screenSize = Math.sqrt(screenWH[0] * screenWH[0] + screenWH[1]
                * screenWH[1]);
        int densityInt = getDensityInt(context);
        return screenSize / densityInt;
    }

    public static int[] getResolution(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return new int[] { metrics.widthPixels, metrics.heightPixels };
    }
}
