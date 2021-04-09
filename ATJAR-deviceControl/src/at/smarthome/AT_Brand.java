package at.smarthome;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

public class AT_Brand {	

	public static JSONArray readFromXLS2007(Context context, String country, String devClassType) {
		JSONArray jsonBrands = new JSONArray();
		String filePath = null;
		if (devClassType == null || country == null) {
			return jsonBrands;
		} else if (devClassType.equals(AT_DeviceClassType.AIRCONDITION)) {
			
				filePath = "codes/air_code_cn.xls";
			
		} else if (devClassType.equals(AT_DeviceClassType.TV)) {
			
				filePath = "codes/tv_code_cn.xls";
			
		} else if (devClassType.equals(AT_DeviceClassType.DVB)) {
			
				filePath = "codes/dvb_code_cn.xls";
			
		}
		if (filePath == null)
			return jsonBrands;

		InputStream is = null;// 输入流对象
		Workbook workbook2007 = null;
		try {
			LogUitl.d("filePath=======" + filePath);
			// is=context.getClass().getResourceAsStream("/assets/"+filePath);
			is = context.getResources().getAssets().open(filePath);// 获取文件输入流
			LogUitl.d("is is null==" + (is == null));
			// workbook2007 = new XSSFWorkbook(is);// 创建Excel2007文件对象
			workbook2007 = Workbook.getWorkbook(is);
			Sheet sheet = workbook2007.getSheet(0);// 取出第一个工作表，索引是0
			// 开始循环遍历行，表头不处理，从2开始
			String fristCellName = "";
			JSONObject jsonBrandInfo = null;// 每个大品牌
			JSONArray jsonANumber = null;// 每个品牌中的型号
			JSONArray otherJson = null;// 每个品牌中的其它型号
			for (int i = 1; i <= sheet.getRows(); i++) {
				if (i < 2) {
					continue;
				}
				Cell[] row = sheet.getRow(i);// 获取行对象
				if (row == null) {// 如果为空，不处理
					continue;
				}
				if (!row[1].getContents().equals(fristCellName)) {
					fristCellName = row[1].getContents();
					jsonBrandInfo = new JSONObject();
					if ("tw".equals(country)) {
						jsonBrandInfo.put("brand", row[2].getContents() == null ? "" : row[2].getContents());
                        jsonBrandInfo.put("pinyin", row[0].getContents() == null ? "" : row[0].getContents().replace(" ", "").replace("/", ""));
					    jsonBrandInfo.put("remen", row.length>8 ? row[8].getContents().replace(" ", ""):"" );
					} else if ("en".equals(country)) {

						jsonBrandInfo.put("brand", row[3].getContents() == null ? "" : row[3].getContents());
						 jsonBrandInfo.put("pinyin", row[0].getContents() == null ? "" : row[0].getContents().replace(" ", "").replace("/", ""));
						 jsonBrandInfo.put("remen",row.length>8 ? row[8].getContents().replace(" ", ""):"");
					} else {
						jsonBrandInfo.put("brand", fristCellName);
						 jsonBrandInfo.put("pinyin", row[0].getContents() == null ? "" : row[0].getContents().replace(" ", "").replace("/", ""));
						 jsonBrandInfo.put("remen",row.length>8 ? row[8].getContents().replace(" ", ""):"");
					}
					jsonBrands.put(jsonBrandInfo);
					jsonANumber = new JSONArray();
					jsonBrandInfo.put("model_numbers", jsonANumber);
					otherJson = new JSONArray();
					JSONObject jsonT = new JSONObject();
					if ("en".equals(country))
					{
						jsonT.put("model_number","Other");
					}else
					{
					jsonT.put("model_number","其它");
					}
					jsonT.put("codes", otherJson);
					jsonANumber.put(jsonT);
				}

				if (jsonANumber != null) {
					
					if ("tw".equals(country)) {
						String number = row.length > 5 ? row[5].getContents() : "";
						if (number.equals("其它")) {
							otherJson.put(row[4].getContents());
						} else {
							JSONObject jsonT = new JSONObject();
							jsonT.put("model_number", row.length > 6 ? row[6].getContents() : "");
							jsonT.put("codes",new JSONArray().put(row[4].getContents()));
							jsonANumber.put(jsonT);
							otherJson.put(row[4].getContents());
						}
						
					} else if ("en".equals(country)) {
						String number = row.length > 7 ? row[7].getContents() : "";
						if (number.equals("Other")) {
							otherJson.put(row[4].getContents());
						} else {
							JSONObject jsonT = new JSONObject();
							jsonT.put("model_number", row.length > 7 ? row[7].getContents() : "");
							jsonT.put("codes", new JSONArray().put(row[4].getContents()));
							jsonANumber.put(jsonT);
							otherJson.put(row[4].getContents());
						}
					} else {
						String number = row.length > 5 ? row[5].getContents() : "";
						if (number.equals("其它")) {
							otherJson.put(row[4].getContents());
						} else {
							JSONObject jsonT = new JSONObject();
							jsonT.put("model_number", row.length > 5 ? row[5].getContents() : "");
							jsonT.put("codes",new JSONArray().put(row[4].getContents()));
							jsonANumber.put(jsonT);
							otherJson.put(row[4].getContents());
						}
						
					}

				}
			}
		} catch (Exception e) {
			LogUitl.d("error===" + e.getMessage());
			e.printStackTrace();
		} finally {// 关闭文件流
			if (is != null) {
				try {
					is.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			workbook2007 = null;
			is = null;
		}
		LogUitl.d(jsonBrands.toString());
		return jsonBrands;
	}
}
