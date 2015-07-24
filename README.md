# JZLocationConverter-for-Android
java 或者 android版本的JZLocationConverter，IOS版本请看https://github.com/JackZhouCn/JZLocationConverter
第一次上传代码，此次修改是在原有的基础上做了如下修改：

1.增加了从图片中读取的exif格式位置信息和GCJ-02坐标信息的相互功能；
	（直接调用gpsInfoConvert()和getLatLon()即可完成相互的转换）

2.取消了以前类中自定义经纬度类，直接应用Android的LatLon即可运行。

世界标准地理坐标(WGS-84) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
只在中国大陆的范围的坐标有效，以外直接返回世界标准坐标

public static LatLng wgs84ToGcj02(LatLng location);


中国国测局地理坐标（GCJ-02） 转换成 世界标准地理坐标（WGS-84）

此接口有1－2米左右的误差，需要精确定位情景慎用

public static LatLng gcj02ToWgs84(LatLng location);


世界标准地理坐标(WGS-84) 转换成 百度地理坐标（BD-09)

public static LatLng wgs84ToBd09(LatLng location);


中国国测局地理坐标（GCJ-02）<火星坐标> 转换成 百度地理坐标（BD-09)

public static LatLng gcj02ToBd09(LatLng location) ;


百度地理坐标（BD-09) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>

public static LatLng bd09ToGcj02(LatLng location);


百度地理坐标（BD-09) 转换成 世界标准地理坐标（WGS-84）

此接口有1－2米左右的误差，需要精确定位情景慎用

public static LatLng bd09ToWgs84(LatLng location);
