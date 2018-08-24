package au.com.vacc.timesheets.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import au.com.vacc.timesheets.R;
import au.com.vacc.timesheets.views.CustomProgressDialog;

public class CustomJsonRequest extends AsyncTask<String, Void, String> {
    private CustomJsonCallBack callBack;
    private Context context;
    private String response;
    private boolean showDialog;
    private String error = null;
    public final static int GET = 1;
    public final static int POST = 2;
    private int method;
    private String url, content;

    public CustomJsonRequest(Context context, String url, String content, boolean showDialog, CustomJsonCallBack callBack, int method) {
        this.url = url;
        this.content = content;
        this.context = context;
        this.callBack = callBack;
        this.showDialog = showDialog;
        this.method = method;
        if(showDialog) {
            CustomProgressDialog.showProgressDialog(context);
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... string) {

        Log.v("sak", url);
        String exceptionError = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 20000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 30000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            Log.v("sak", content);
            if (method == POST) {
                HttpPost httpPost = new HttpPost(new String(url.getBytes("UTF-8"), "UTF-8"));
                 StringEntity postingString = new StringEntity(content, "UTF8");
                 httpPost.setEntity(postingString);
                 httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {

                HttpGet httpGet = new HttpGet(url + content);
                httpResponse= httpClient.execute(httpGet);

            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            httpEntity = httpResponse.getEntity();
            if(statusCode != 200) {
                error = "false";
            }
            response = EntityUtils.toString(httpEntity);
            Log.v("sak", statusCode + " " + response);
        } catch (UnsupportedEncodingException e) {
            error = e.getMessage();
                    e.printStackTrace();
        } catch (ClientProtocolException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (UnknownHostException e) {
            exceptionError = context.getString(R.string.error_connection);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            exceptionError = e.getMessage();
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            exceptionError = e.getMessage();
            e.printStackTrace();
        } catch (SocketException e) {
            exceptionError = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            exceptionError = e.getMessage();
            e.printStackTrace();
        }

        if(error == null) {
            error = exceptionError;
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(showDialog) {
           CustomProgressDialog.dismissProgressDialog();
        }
        if (error != null) {
            callBack.onError(error);
            error = null;
        } else {
            callBack.getResult(response);
        }
    }

}

