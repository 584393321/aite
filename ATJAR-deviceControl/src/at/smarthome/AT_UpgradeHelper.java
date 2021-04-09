package at.smarthome;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xhc.sbh.tool.lists.logcats.LogUitl;

/**
 * @author fr
 * 版本升級
 */
public class AT_UpgradeHelper {

	private static final String VERSION_URL_BASE = "http://server.atsmartlife.com/getversion?devicetype=%s&&devicemodel=1.0";//版本更新URL

	public static enum ResultCode{
		RESULT_CHECK_FAILED , //文件校验失败
		RESULT_DOWNLOAD_SUCCESS, //下载成功
		RESULT_DOWNLOAD_EXCEPTION //下载异常
	};

	public class VersionInfo implements Serializable{
		public String tagName; //下载tag名称 如果有多个下载任务，可通过该tag进行区分
		public String versiondesc; //版本描述，提示框中显示
		public int filesize; //文件大小
		public String versionurl; //下载更新的url
		public String result;
		public int version ;//版本号
		public String md5sum; //md5校验
		public String destPath;
		@Override
		public String toString() {
			return "VersionInfo [tagName=" + tagName + ", versiondesc=" + versiondesc + ", versionurl=" + versionurl
					+ ", result=" + result + ", version=" + version + "]";
		}
	}

	private AT_UpgradeHelper(){}

	/**
	 * 版本檢測
	 */
	public static void doVersionCheck(final String appTag,final VersionCheckListener listner)
	{
		if(!TextUtils.isEmpty(appTag)){

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String url = String.format(VERSION_URL_BASE,appTag);
						Log.d("version", "version url ===>"+url);
						String result = HttpUtils2.doHttpGet(url);
						Log.d("version", "version info===>"+result);
						if(!TextUtils.isEmpty(result)){
							Type type = new TypeToken<VersionInfo>() {}.getType();
							VersionInfo vInfo = new Gson().fromJson(result, type);
							vInfo.tagName = appTag;
							Log.d("version","version info code = "+ vInfo.version);
							if("success".equals(vInfo.result)){
								if(listner != null){
									listner.onCheckOver(true, vInfo);
								}
							}else{
								if(listner != null){
									listner.onCheckOver(false, vInfo);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("xhc" , "e-->"+e.getMessage());
						if(listner != null){
							listner.onCheckOver(false, null);
						}
					}
				}
			}).start();
		}
	}

	/**
	 * 版本檢測
	 */
	public static void doVersionCheck(final String appTag,final VersionCheckListener listner ,final int nowVersionCode)
	{
		if(!TextUtils.isEmpty(appTag)){

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String url = String.format(VERSION_URL_BASE,appTag);
						Log.d("version", "version url ===>"+url);
						String result = HttpUtils2.doHttpGet(url);
						Log.d("version", "version info===>"+result);
						if(!TextUtils.isEmpty(result)){
							Type type = new TypeToken<VersionInfo>() {}.getType();
							VersionInfo vInfo = new Gson().fromJson(result, type);
							vInfo.tagName = appTag;
							Log.d("version","version info code = "+ vInfo.version);
							if(vInfo.version == nowVersionCode){
								if(listner != null){
									//服务器和当前版本一致
									listner.onCheckOver(false, vInfo);
								}
							}
							else if("success".equals(vInfo.result)){
								if(listner != null){
									listner.onCheckOver(true, vInfo);
								}
							}else{
								if(listner != null){
									listner.onCheckOver(false, vInfo);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("xhc" , "e-->"+e.getMessage());
						if(listner != null){
							listner.onCheckOver(false, null);
						}
					}
				}
			}).start();
		}
	}



	/**
	 * 检测是否需要下载
	 * @param path  下载的文件路径
	 * @param srcMd5 服务端md5校验和
	 * @return true 需要下載
	 */
	public static boolean checkIfNeedDownLoad(Context appContext,VersionInfo vInfo)
	{
		String apkUrl = vInfo.versionurl;
		String dir = appContext.getExternalFilesDir("apk").getAbsolutePath();
		File folder = Environment.getExternalStoragePublicDirectory(dir);
		if(folder.exists() && folder.isDirectory()) {
			//do nothing
		}else {
			folder.mkdirs();
		}
		String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
		final String fileFullPath =  new StringBuffer(dir).append(File.separator).append(filename).toString();

		boolean bNeedDownLoad = true;
		if(isFileExist(fileFullPath)){//本地是否存在待更新文件
			File file = new File(fileFullPath);
			String md5Sum;
			try {
				md5Sum = getFileMD5(file);//如果已经下载，则获取其md5值
				Log.d("version", "download file md5 sum:"+md5Sum);
				Log.d("version", "version info md5 sum:"+vInfo.md5sum);
				if(md5Sum != null && md5Sum.equals(vInfo.md5sum)){//校验通过 ，则不需要下载
					bNeedDownLoad = false;
				}else{
					bNeedDownLoad = true;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				bNeedDownLoad = true;
			} catch(IOException e){

				bNeedDownLoad = true;
			}
		}
		return bNeedDownLoad;
	}

	/**下载更新任务
	 * @param vInfo
	 */
	public static void downLoadTask(Context appContext,final VersionInfo vInfo,final DownLoadListner listener)
	{
		if(listener != null){
			if(!TextUtils.isEmpty(vInfo.versionurl)){
				//prepare download path
				String dir = appContext.getExternalFilesDir("apk").getAbsolutePath();
				File folder = Environment.getExternalStoragePublicDirectory(dir);
				if(folder.exists() && folder.isDirectory()) {
					//do nothing
				}else {
					folder.mkdirs();
				}
				String filename = vInfo.versionurl.substring(vInfo.versionurl.lastIndexOf("/"),vInfo.versionurl.length());
				vInfo.destPath  =  new StringBuffer(dir).append(File.separator).append(filename).append(".tmp").toString();
				if(isFileExist(vInfo.destPath)){
					delFile(vInfo.destPath);
				}
				Log.d("version", "update path==>"+vInfo.destPath );
			}
			listener.downLoadPrepare();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// download the file
				InputStream input = null;
				OutputStream output = null;
				try {
					URL url = new URL(vInfo.versionurl);
					URLConnection connection = url.openConnection();
					connection.connect();
					int fileLength = connection.getContentLength();

					input = new BufferedInputStream(connection.getInputStream());
					output = new FileOutputStream(vInfo.destPath);
					byte data[] = new byte[100];
					long total = 0;
					int count;
					int progress = 0;
					String fileNewName=null;
					while ((count = input.read(data)) != -1) {
						total += count;
						int n = (int) (total * 100 / fileLength);
						if(n > progress){
							progress = n ;
							LogUitl.d("progress==="+progress);
							// publishing the progress....
							if(listener != null){
								listener.publishProgress(progress, vInfo);
								if(progress == 100){
									if(!TextUtils.isEmpty(vInfo.destPath)){
										File apkFile = new File(vInfo.destPath);
										fileNewName = vInfo.destPath.substring(0, vInfo.destPath.lastIndexOf("."));

									}

								}
							}
						}
						output.write(data, 0, count);
					}
					output.flush();
					input.close();
					try{
						String md5Sum = fileMD5(vInfo.destPath);
						Log.d("version", "download file md5 sum:"+md5Sum);
						Log.d("version", "version info md5 sum:"+vInfo.md5sum);
						if(md5Sum != null && md5Sum.equals(vInfo.md5sum)){//check file
							renameFile(vInfo.destPath, fileNewName);
							listener.downLoadOver(ResultCode.RESULT_DOWNLOAD_SUCCESS,fileNewName);
						}else{
							delFile(vInfo.destPath);
							listener.downLoadOver(ResultCode.RESULT_CHECK_FAILED,"");
						}
					}catch (Exception e){
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
					if(listener != null){
						listener.downLoadOver(ResultCode.RESULT_DOWNLOAD_EXCEPTION,"");
					}
				}finally{
					try {
						input.close();
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	/**
	 * @author fr
	 * 升級檢測接口
	 */
	public interface VersionCheckListener{

		/**檢測回調  运行于线程
		 * @param result  true 獲取版本信息成功 否則失敗
		 * @param versionInfo  版本信息
		 */
		public void onCheckOver(boolean result,VersionInfo versionInfo);
	}

	/**
	 * @author fr
	 * 下載接口
	 */
	public interface DownLoadListner{

		/**
		 * 下載前回調 如显示下载进度框
		 */
		public void downLoadPrepare();

		/**
		 * 下载进度  
		 * 该方法运行在线程中，需要在内部使用handler进行界面的更新
		 * @param progress
		 * @param vInfo
		 */
		public void publishProgress(int progress ,VersionInfo vInfo);

		/**下载完成通告  运行于线程 
		 * @param code 完成码 具体见ResultCode
		 * @param filePath 如果下载成功 保存的文件路径
		 */
		public void downLoadOver(ResultCode code,String filePath);
	}

	/*****************************************************************************************/

	private static boolean isFileExist(String pathName)
	{
		File file = new File(pathName);
		if(file.isFile()){
			return file.exists();
		}
		return false;
	}

	private static void delFile(String pathName)
	{
		File file = new File(pathName);
		if (file.isFile()){
			file.delete();
		}
	}


	private static void renameFile(String oldname,String newname){
		if(!oldname.equals(newname)){
			File oldfile=new File(oldname);
			File newfile=new File(newname);
			if(newfile.exists()){
				newfile.delete();
			}
			oldfile.renameTo(newfile);
		}
	}

	public static String fileMD5(String inputFile) throws IOException {
		// 缓冲区大小（这个可以抽出一个参数）
		int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			// 拿到一个MD5转换器（同样，这里可以换成SHA1）
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			// 使用DigestInputStream
			fileInputStream = new FileInputStream(inputFile);
			digestInputStream = new DigestInputStream(fileInputStream,messageDigest);
			// read的过程中进行MD5处理，直到读完文件
			byte[] buffer =new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0);
			// 获取最终的MessageDigest
			messageDigest= digestInputStream.getMessageDigest();
			// 拿到结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {
			}
			try {
				fileInputStream.close();
			} catch (Exception e) {
			}
		}
	}
	public static String byteArrayToHex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1) {
				hs = hs + "";
			}
		}
		// return hs.toUpperCase();
		return hs;
	}
	private static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest;
		FileInputStream in;
		byte buffer[] = new byte[1024];
		int len;
		digest = MessageDigest.getInstance("MD5");
		in = new FileInputStream(file);
		while ((len = in.read(buffer, 0, 1024)) != -1) {
			digest.update(buffer, 0, len);
		}
		in.close();
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
