package io.cex.test.framework.httputil;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
@Slf4j
public class OkHttpClientManager {


    private static OkHttpClientManager mInstance;
    private static OkHttpClientManager mInstanceTimeout;
    private OkHttpClient mOkHttpClient;


    private static final String TAG = "OkHttpClientManager";
    public static final MediaType JSON = MediaType.parse("application/json");

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null){
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
//            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());


        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }


    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder().build();

        //cookie enabled
//        mGson = new Gson();
    }

    private OkHttpClientManager(Long timeoutMs) {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(timeoutMs, TimeUnit.MILLISECONDS).
                readTimeout(timeoutMs, TimeUnit.MILLISECONDS).
                writeTimeout(timeoutMs, TimeUnit.MILLISECONDS).build();
    }


    public synchronized static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpClientManager();
        }
        return mInstance;
    }

    public synchronized static OkHttpClientManager getInstance(Long timeoutMs) {
        if (mInstanceTimeout == null) {
            mInstanceTimeout = new OkHttpClientManager(timeoutMs);
        }
        return mInstanceTimeout;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }


    /**
     * 同步的Post请求,默认application/json
     *
     * @param url
     * @param
     * @return
     */
    private Response _post(String url, String json) throws IOException {
        Request request = buildJsonPostRequest(url, json);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求字符串
     *
     * @param url
     * @param
     * @return 字符串
     */
    private String _postAsString(String url, String json) throws IOException {
        Response response = _post(url, json);
        return response.body().string();
    }

    /**
     * @desc  同步的post请求，指定content-type
     * @param
     **/
    private Response _post(String url, String json, String contentType) throws IOException{
        Request request = buildJsonPostRequest(url,json,contentType);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * @desc 同步post请求，带content-type
     * @param
     **/
    private  String _postAsString(String url, String json, String contentType) throws IOException{
        Response response = _post(url, json, contentType);
        return response.body().string();
    }


    /**
     * @desc  同步的post请求，指定content-type,header
     * @param
     **/
    private Response _post(String url, String json, String contentType,HashMap<String, String> header) throws IOException{
        Request request = buildJsonPostRequest(url,json,contentType,header);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * @desc 同步post请求，带content-type,header
     * @param
     **/
    private  String _postAsString(String url, String json, String contentType,HashMap<String, String> header) throws IOException{
        Response response = _post(url, json, contentType,header);
        return response.body().string();
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }

    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static String getAsString(String url, Long timeoutMs) throws IOException {
        return getInstance(timeoutMs)._getAsString(url);
    }

    public static Response post(String url, String json) throws IOException {
        return getInstance()._post(url, json);
    }

    public static String postAsString(String url, String json) throws IOException {
        return getInstance()._postAsString(url, json);
    }
    public static Response post(String url, String json, String contentType) throws IOException {
        return getInstance()._post(url, json, contentType);
    }

    public static String postAsString(String url, String json, String contentType) throws IOException {
        return getInstance()._postAsString(url, json, contentType);
    }
    public static Response post(String url, String json, String contentType,HashMap<String, String> header) throws IOException {
        return getInstance()._post(url, json, contentType,header);
    }

    public static String postAsString(String url, String json, String contentType,HashMap<String, String> header) throws IOException {
        return getInstance()._postAsString(url, json, contentType,header);
    }

    /**
     * timeout单位是毫秒（ms）
     *
     * @param url
     * @param json
     * @param timeoutMs
     * @return
     * @throws IOException
     */
    public static String postAsString(String url, String json, Long timeoutMs) throws IOException {
        return getInstance(timeoutMs)._postAsString(url, json);
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Request buildJsonPostRequest(String url, String json) {
        if (StringUtils.isBlank(json)) {
            log.error(json + " is blank");
            return null;
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }

    private Request buildJsonPostRequest(String url, String json, String contentType){
        if (StringUtils.isBlank(json)) {
            log.error(json + " is blank");
            return null;
        }
        MediaType type = MediaType.parse(contentType);
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }

    private Request buildJsonPostRequest(String url, String json, String contentType, HashMap<String, String> header){
        if (StringUtils.isBlank(json)) {
            log.error(json + " is blank");
            return null;
        }
        MediaType type = MediaType.parse(contentType);
        RequestBody body = RequestBody.create(type, json);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",header.get("Cookie"))
                .post(body)
                .build();
        return request;
    }

}
