package cn.wj.lbstest.utils;

import android.location.Location;

import com.amap.api.maps.model.LatLng;

public class JZLocationConverter {

    private static final double LAT_OFFSET_0(double x, double y) {
        return -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
    }

    private static final double LAT_OFFSET_1(double x, double y) {
        return (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LAT_OFFSET_2(double x, double y) {
        return (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LAT_OFFSET_3(double x, double y) {
        return (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_0(double x, double y) {
        return 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
    }

    private static final double LON_OFFSET_1(double x, double y) {
        return (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_2(double x, double y) {
        return (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_3(double x, double y) {
        return (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static double RANGE_LON_MAX = 137.8347;
    private static double RANGE_LON_MIN = 72.004;
    private static double RANGE_LAT_MAX = 55.8271;
    private static double RANGE_LAT_MIN = 0.8293;

    private static double jzA  = 6378245.0;
    private static double jzEE = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = LAT_OFFSET_0(x, y);
        ret += LAT_OFFSET_1(x, y);
        ret += LAT_OFFSET_2(x, y);
        ret += LAT_OFFSET_3(x, y);
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = LON_OFFSET_0(x, y);
        ret += LON_OFFSET_1(x, y);
        ret += LON_OFFSET_2(x, y);
        ret += LON_OFFSET_3(x, y);
        return ret;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < RANGE_LON_MIN || lon > RANGE_LON_MAX)
            return true;
        if (lat < RANGE_LAT_MIN || lat > RANGE_LAT_MAX)
            return true;
        return false;
    }

    public static LatLng gcj02Encrypt(double ggLat, double ggLon) {
//        LatLng resPoint = new LatLng();
        double mgLat;
        double mgLon;
        if (outOfChina(ggLat, ggLon)) {
//            resPoint.latitude = ggLat;
//            resPoint.longitude = ggLon;
            return new LatLng(ggLat, ggLon);
        }
        double dLat = transformLat(ggLon - 105.0, ggLat - 35.0);
        double dLon = transformLon(ggLon - 105.0, ggLat - 35.0);
        double radLat = ggLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - jzEE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((jzA * (1 - jzEE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (jzA / sqrtMagic * Math.cos(radLat) * Math.PI);
        mgLat = ggLat + dLat;
        mgLon = ggLon + dLon;

//        resPoint.latitude = mgLat;
//        resPoint.longitude = mgLon;
        return new LatLng(mgLat, mgLon);
    }

    public static LatLng gcj02Decrypt(double gjLat, double gjLon) {
        LatLng gPt = gcj02Encrypt(gjLat, gjLon);
        double dLon = gPt.longitude - gjLon;
        double dLat = gPt.latitude - gjLat;
        LatLng pt = new LatLng(gjLat - dLat,gjLon - dLon);
//        pt.latitude = gjLat - dLat;
//        pt.longitude = gjLon - dLon;
        return pt;
    }

    public static LatLng bd09Decrypt(double bdLat, double bdLon) {
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        LatLng gcjPt = new LatLng(z * Math.sin(theta),z * Math.cos(theta));
//        gcjPt.longitude = z * Math.cos(theta);
//        gcjPt.latitude = z * Math.sin(theta);
        return gcjPt;
    }

    public static LatLng bd09Encrypt(double ggLat, double ggLon) {
        double x = ggLon, y = ggLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        LatLng bdPt = new LatLng(z * Math.sin(theta) + 0.006,z * Math.cos(theta) + 0.0065);
//        bdPt.longitude = z * Math.cos(theta) + 0.0065;
//        bdPt.latitude = z * Math.sin(theta) + 0.006;
        return bdPt;
    }

    /**
     * @param location 世界标准地理坐标(WGS-84)
     * @return 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @brief 世界标准地理坐标(WGS-84) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
     *
     * ####只在中国大陆的范围的坐标有效，以外直接返回世界标准坐标
     */
    public static LatLng wgs84ToGcj02(LatLng location) {
        return gcj02Encrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 中国国测局地理坐标（GCJ-02）
     * @return 世界标准地理坐标（WGS-84）
     * @brief 中国国测局地理坐标（GCJ-02） 转换成 世界标准地理坐标（WGS-84）
     *
     * ####此接口有1－2米左右的误差，需要精确定位情景慎用
     */
    public static LatLng gcj02ToWgs84(LatLng location) {
        return gcj02Decrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 世界标准地理坐标(WGS-84)
     * @return 百度地理坐标（BD-09)
     * @brief 世界标准地理坐标(WGS-84) 转换成 百度地理坐标（BD-09)
     */
    public static LatLng wgs84ToBd09(LatLng location) {
        LatLng gcj02Pt = gcj02Encrypt(location.latitude, location.longitude);
        return bd09Encrypt(gcj02Pt.latitude, gcj02Pt.longitude);
    }

    /**
     * @param location 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @return 百度地理坐标（BD-09)
     * @brief 中国国测局地理坐标（GCJ-02）<火星坐标> 转换成 百度地理坐标（BD-09)
     */
    public static LatLng gcj02ToBd09(LatLng location) {
        return bd09Encrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 百度地理坐标（BD-09)
     * @return 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @brief 百度地理坐标（BD-09) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
     */
    public static LatLng bd09ToGcj02(LatLng location) {
        return bd09Decrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 百度地理坐标（BD-09)
     * @return 世界标准地理坐标（WGS-84）
     * @brief 百度地理坐标（BD-09) 转换成 世界标准地理坐标（WGS-84）
     *
     * ####此接口有1－2米左右的误差，需要精确定位情景慎用
     */
    public static LatLng bd09ToWgs84(LatLng location) {
        LatLng gcj02 = bd09ToGcj02(location);
        return gcj02Decrypt(gcj02.latitude, gcj02.longitude);
    }

	/**
	 * 将GCJ-02坐标信息转换成exif中存储的格式
	 * 
	 * @param gpsInfo
	 *            latitude , longitude  40.0447706987081
	 * @return <b>exif</b> 40/1,2/1,4117452/100000
	 */
	public static String gpsInfoConvert(double gpsInfo){
		gpsInfo = Math.abs(gpsInfo);
		String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
		String[] splits = dms.split(":");
		
		for (int i = 0; i < splits.length; i++) {
			if (splits[i].indexOf(".") != -1) {
				splits[i]=transforData(splits[i]);
			}else {
				splits[i]=splits[i]+"/1";
			}
		}
		return splits[0]+","+splits[1]+","+splits[2];
	}
	
	
	/**
	 * double型转换成整数相除
	 * @param str 41.17452
	 * @return 4117452/100000
	 */
	public static String transforData(String str) {
		String [] splitStrings = str.split("\\.");
		int length = splitStrings[1].length();
		int right = (int) Math.pow(10, length);
		int left = (int)Math.rint((Double.parseDouble(str)*(double)right));
		return left+"/"+right;
	}

	/**
	 * 将exif中存储的地理信息转换成GCJ-02坐标信息
	 * @param exifInfoString 40/1,2/1,4117452/100000
	 * @return latitude,longitude 40.0447706987081
	 */
	public static double getLatLon(String exifInfoString){
		String gpsString = getGps(exifInfoString);
		double convert = Location.convert(gpsString);
		return convert;
	}
	/**
	 * 将exif格式转换成gps格式
	 * @param exifInfoString  40/1,2/1,4117452/100000
	 * @return 40:2:36.6650
	 */
	private static String getGps(String exifInfoString) {
		String[] split = exifInfoString.split(",");
		for (int i = 0; i < split.length; i++) {
			if (i<split.length-1) {
				split[i] = split[i].split("/")[0];
			}else{
				String[] second = split[i].split("/");
				double result = Double.parseDouble(second[0])/Double.parseDouble(second[1]);
				split[i] = String.valueOf(result);
			}
		}
		return split[0]+":"+split[1]+":"+split[2];
	}
}
