package at.smarthome;
/**
 * 环境联动的条件类型
 * @author th
 *
 */
public class AT_EnvirTypeFinal {
	
	public static final String TEMPERATURE="temperature";//温度 <0寒冷  <15冷   <23舒适  <31热  >=31酷热
	public static final String HUMIDITY="humidity";//湿度  <25干燥    <=55微干燥  <=64舒适 <=80微潮湿  >80潮湿
	public static final String PM="pm";	//PM2.5  //0-35优  36-75良 76-115中  116-150差  150以上极差
	public static final String AIRQ="airq";//空气质量  0-50优  51-100良 101-150中  151-200差  200以上极差
	public static final String CO2="co2";//co2   0-350优  351-1000良  1001-2000中  2001-50000差，5000以上极差
	public static final String ILLUMINATION="illumination";//光感illumination
	public static final String VOC="voc";//具有挥发性，如甲醛  0-100优  100-200中  200以上差
	public static final String PM10="pm10";//0-50优  51-150良 151-250中  251-350差  350以上极差
	public static final String PM25="pm2_5";//0-35优  36-75良 76-115中  116-150差  150以上极差
	public static final String TVOC="tvoc";// tvoc   差>3mg/L、中≤3 mg/L、优≤0.6 mg/L。
	public static final String HCHO="hcho";//甲醛  差>1.5mg/L、中≤1.5 mg/L、优≤0.5 mg/L。
}
/**
 *
 * co2
 a )250~350ppm—通常的户外空气等级。 　　
 b) 350~1,000ppm—通风良好的居住空间内的典型值。 　　
 c) 1,000~2,000ppm—氧气不足、令人困倦、足以引起抱怨的空气等级 　　
 d ) 2,000~5,000ppm—停滞、陈旧、闷热的空气等级。令人头痛、嗜睡，同时伴有精力不集中、注意力下降、心跳加速和轻微恶心的现象。 　　
 e) > 5,000 ppm—暴露在其中可能会严重缺氧，导致永久性脑损伤、昏迷甚至死亡。


 当TVOC浓度为3.0-25mg/m3时，会产生刺激和不适，与其他因素联合作用时，可能出现头痛；当VOC浓度大于25 mg/m3时，
 除头痛外，可能出现其他的神经毒性作用。国标限值为0.60mg/立方米


 甲醛限量等级分成三个级别，即E2≤5.0mg/L、E1≤1.5 mg/L、E0≤0.5 mg/L。
 */