package at.smarthome;

import java.util.Locale;

public class AT_GetLanguage {	
	public static String getCurrSystemLanguage()
	{		
		String language = Locale.getDefault().getLanguage();
		String country = Locale.getDefault().getCountry();
		if (language.equalsIgnoreCase("zh")) {
			if (country.equalsIgnoreCase("tw")||country.equalsIgnoreCase("hk")) {
				return "tw";
			} else if (country.equalsIgnoreCase("cn")) {
				return "cn";
			}
		}
		return language;
	}
}
