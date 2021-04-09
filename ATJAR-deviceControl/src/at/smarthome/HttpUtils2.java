package at.smarthome;

import android.util.Log;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils2 {
    private static final int CONNECTION_TIMEOUT_GET = 65000;
    private static final int CONNECTION_TIMEOUT_POST = 10000;

    public static String doHttpGet(String serverURL) throws ClientProtocolException, IOException, SocketTimeoutException {
        return doHttpGet(serverURL, CONNECTION_TIMEOUT_GET);
        //return doGetByURL(serverURL);
    }

    public static String doHttpGet(String serverURL, int timeOut) throws ClientProtocolException, IOException, SocketTimeoutException {
        /*
         * DefaultHttpClient client = new DefaultHttpClient()
         * client.getParams().setParameter(ClientPNames.
         * ALLOW_CIRCULAR_REDIRECTS, true); HttpResponse response =
         * client.execute(httpGet);
         */
        long tep = System.currentTimeMillis() / 1000;
        try {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
            HttpClient hc = new DefaultHttpClient();

            hc.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
            //hc.getParams().setParameter("Connection", "close");
            HttpGet get = new HttpGet(serverURL);
            get.addHeader("Content-Type", "application/json");
            get.setParams(httpParameters);
            //get.setHeader("Connection", "close");
            get.setHeader("anthouse", "anthouse");
            HttpResponse response = null;
            response = hc.execute(get);
            response.addHeader("charset", HTTP.UTF_8);
            int sCode = response.getStatusLine().getStatusCode();
            LogUitl.d("get sCode===" + sCode);
            if (sCode == HttpStatus.SC_OK) {
                LogUitl.d("header==" + response.getAllHeaders().toString());
                return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }
        } catch (SocketTimeoutException e) {
            LogUitl.d("get time out ");
        } catch (Exception e) {
            e.printStackTrace();
            LogUitl.d(System.currentTimeMillis() / 1000 - tep + "  doHttpGet  error==" + e.getMessage());
        }

        return null;
    }

    public static String doHttpsGet(String serverURL) throws Exception {
        return doHttpsGet(serverURL, CONNECTION_TIMEOUT_GET);
    }

    public static String doGetByURL(String urlContent) {
        HttpURLConnection conn = null;
        try {
            //get方式，url是直接在后面拼接地址的
            URL url = new URL(urlContent);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");//GET和POST必须全大写
            conn.setConnectTimeout(CONNECTION_TIMEOUT_GET);//连接的超时时间
            conn.setReadTimeout(CONNECTION_TIMEOUT_GET);//读数据的超时时间
            LogUitl.d("xxx===============");
            int responseCode = conn.getResponseCode();
            LogUitl.d("responseCode======" + responseCode);
            //  if(responseCode==200){
            //访问成功，通过流取的页面的数据信息
            InputStream is = conn.getInputStream();
            String status = getStringFromInputStream(is);
            return status;
            // }else{
            //    LogUitl.d("doGetByURL error==："+responseCode);
            // }

        } catch (Exception e) {
            LogUitl.d("doGetByURL error==" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();//释放链接
            }
        }
        return null;
    }

    /**
     * 通过字节输入流返回一个字符串信息
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String status = baos.toString();// 把流中的数据转换成字符串, 采用的编码是: utf-8  
        baos.close();
        return status;
    }

    public static String doHttpsGet(String serverURL, int timeOut) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
        HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
        HttpClient hc = initHttpClient(httpParameters);
        HttpGet get = new HttpGet(serverURL);
        get.addHeader("Content-Type", "text/xml");
        get.setParams(httpParameters);
        get.setHeader("Charset", "UTF-8");
        HttpResponse response = null;
        try {
            response = hc.execute(get);
            response.addHeader("charset", HTTP.UTF_8);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {

            return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static String doHttpPostnew(String url, String content) throws Exception {

        try {
            URL realurl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) realurl.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_POST);

            // 设置允许输入输出
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            // 设置请求报文头，设定请求数据类型
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求数据长度
            urlConnection.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
            // 设置POST方式请求数据
            urlConnection.setRequestMethod("POST");
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(content.getBytes());
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                return changeInputStream(urlConnection.getInputStream(), "UTF-8");
            }
        } catch (IOException e) {
            LogUitl.d("post error=" + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 把服务端返回的输入流转换成字符串格式
     *
     * @param inputStream 服务器返回的输入流
     * @param encode      编码格式
     * @return 解析后的字符串
     */
    private static String changeInputStream(InputStream inputStream, String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                LogUitl.d("len=======" + len);
                result = new String(outputStream.toByteArray(), encode);

            } catch (IOException e) {
                LogUitl.d("input error==" + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doHttpPost(String url, String content)
            throws ParseException, IOException, SocketTimeoutException {
        HttpParams parms = new BasicHttpParams();
        parms.setParameter("charset", HTTP.UTF_8);
        HttpConnectionParams.setConnectionTimeout(parms, CONNECTION_TIMEOUT_POST);
        HttpConnectionParams.setSoTimeout(parms, CONNECTION_TIMEOUT_POST);
        HttpClient httpclient = new DefaultHttpClient(parms);
        //httpclient.getParams().setParameter("Connection", "close");
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json");
        httppost.addHeader("charset", HTTP.UTF_8);
        httppost.setHeader("Range", "bytes=" + "");
        //httppost.setHeader("Connection", "close");
        httppost.setEntity(new StringEntity(content, HTTP.UTF_8));
        HttpResponse response;
        response = httpclient.execute(httppost);
        int sCode = response.getStatusLine().getStatusCode();
        Log.e("ATMainModel: ", url + "---" + sCode + "---");
        StringBuffer res = new StringBuffer();
        if (sCode == HttpStatus.SC_OK) {
            res.append(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
            /*
             * HttpEntity he = response.getEntity(); InputStream is =
             * he.getContent(); BufferedReader br = new BufferedReader(new
             * InputStreamReader(is)); String readLine = null; byte[] read=new
             * byte[2048]; while((readLine =br.readLine()) != null){
             * //br.read(read); res.append(readLine); } is.close(); br.close();
             */
            LogUitl.d("res=====" + res.length());
            //LogUitl.d("res=="+res);
            return res.toString();

        } else {
            if (sCode == 401)
                return sCode + "";
            else
                return "";
        }
    }

    public static String doHttpPostAccessToken(String url, String content, String access_token)
            throws ParseException, IOException, SocketTimeoutException {
        HttpParams parms = new BasicHttpParams();
        parms.setParameter("charset", HTTP.UTF_8);
        HttpConnectionParams.setConnectionTimeout(parms, CONNECTION_TIMEOUT_POST);
        HttpConnectionParams.setSoTimeout(parms, CONNECTION_TIMEOUT_POST);
        HttpClient httpclient = new DefaultHttpClient(parms);
        //httpclient.getParams().setParameter("Connection", "close");
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json");
        httppost.addHeader("charset", HTTP.UTF_8);
        httppost.addHeader("accessToken", access_token);
        httppost.setHeader("Range", "bytes=" + "");
        //httppost.setHeader("Connection", "close");
        httppost.setEntity(new StringEntity(content, HTTP.UTF_8));
        HttpResponse response;
        response = httpclient.execute(httppost);
        int sCode = response.getStatusLine().getStatusCode();
//        if (sCode == 404 || sCode == 401)
        Log.e("ATMainModel: ", url + "---" + sCode + "---" + access_token);
        StringBuilder res = new StringBuilder();
        if (sCode == HttpStatus.SC_OK) {
            res.append(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
            /*
             * HttpEntity he = response.getEntity(); InputStream is =
             * he.getContent(); BufferedReader br = new BufferedReader(new
             * InputStreamReader(is)); String readLine = null; byte[] read=new
             * byte[2048]; while((readLine =br.readLine()) != null){
             * //br.read(read); res.append(readLine); } is.close(); br.close();
             */
            LogUitl.d("res=====" + res.length());
            //LogUitl.d("res=="+res);
            return res.toString();

        } else {
            if (sCode == 401)
                return sCode + "";
            else
                return "";
        }
    }

    public static String doHttpPost1(String serverURL, String xmlString) throws Exception {
        LogUitl.d("serverURL=" + serverURL);
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT_POST);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT_POST);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(serverURL);
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(new StringEntity(xmlString, "UTF-8"));
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
            response.addHeader("charset", HTTP.UTF_8);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        LogUitl.d("sCode=" + sCode);
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static String doHttpsPost(String serverURL, String xmlString) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT_POST);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT_POST);
        HttpClient hc = initHttpClient(httpParameters);
        HttpPost post = new HttpPost(serverURL);
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(new StringEntity(xmlString, "UTF-8"));
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static HttpClient initHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}