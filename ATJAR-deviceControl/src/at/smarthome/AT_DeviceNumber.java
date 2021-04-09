package at.smarthome;

/**
 * 设备号
 * 
 * @author th
 *
 */
public class AT_DeviceNumber {

	/**
	 * 根据国家简称和设备类型获取设备号
	 * 
	 * @param country
	 * @param dev_class_type
	 * @return
	 */
	public final static int getDeviceNumberByCountryAndDevClaType(String country, String dev_class_type) {
		int result = -1;
		if (dev_class_type != null) {
			if (dev_class_type.equals(AT_DeviceClassType.AIRCONDITION)) {
				result = 6;
			} else if (country == null || country.equals("cn")) {
				if (dev_class_type.equals(AT_DeviceClassType.DVB)) {
					result = 1;
				} else if (dev_class_type.equals(AT_DeviceClassType.TV)) {
					result = 3;
				}
			} else {
				if (dev_class_type.equals(AT_DeviceClassType.DVB)) {
					result = 2;
				} else if (dev_class_type.equals(AT_DeviceClassType.TV)) {
					result = 4;
				}
			}
		}
		return result;
	}
}
