package cn.finalteam.glue;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


public class BaseAsyncHttpResponseHandler extends JsonHttpResponseHandler {
    
    private static final String TAG = "BaseAsyncHttpResponseHandler";
    
    private final OnFinishedListener mOnFinishedListener;
    
    public interface OnFinishedListener {
        void onFinished(boolean success);
    }
    
    public BaseAsyncHttpResponseHandler() {
        mOnFinishedListener = null;
    }
    
    public BaseAsyncHttpResponseHandler(OnFinishedListener onFinishedListener) {
        mOnFinishedListener = onFinishedListener;
    }
    
    public void onFinished(boolean success) {
        if (success) {
//            deleteUploadTask();
        }
        
        if (mOnFinishedListener != null) {
            mOnFinishedListener.onFinished(success);
        }
    }
    
    public void onError(int errorcode, String description) {
        onFinished(false);
    }
    
    @Override
    public void onCancel() {
        onFinished(false);
        super.onCancel();
    }
    
    public void onFailure(int statusCode, HashMap<String, Object> responseObjects) {
        if (responseObjects != null) {
            int errorcode = 0;
            if (responseObjects.containsKey(NetworkUtils.errorcode)) {
                errorcode = (Integer) responseObjects.get(NetworkUtils.errorcode);
            }
            String description = "";
            if (responseObjects.containsKey(NetworkUtils.description)) {
                description = (String) responseObjects.get(NetworkUtils.description);
            }
            onError(errorcode, description);
        } else {
            onError(0, "Unknown error!");
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        HashMap<String, Object> responseObjects = parseResponseObjects(response);
        int status = getStatus(response);
        if (status == 0){
            onSuccess(statusCode, responseObjects);
        } else {
            onFailure(statusCode, responseObjects);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        HashMap<String, Object> responseObjects = parseResponseObjects(errorResponse);
        onFailure(statusCode, responseObjects);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onFailure(statusCode, null);
    }

    public void onSuccess(int statusCode, HashMap<String, Object> responseObjects) {
    }

    protected HashMap<String, Object> parseResponseObjects(Object result) {
        final HashMap<String, Object> responseObjects = new HashMap<String, Object>();
        if (result == null) {
            return null;
        }
        try {
            if (result instanceof JSONObject) {
                JSONObject response = (JSONObject) result;
                if (response.has(NetworkUtils.head)) {
                    JSONObject head = response.getJSONObject(NetworkUtils.head);
                    if (head.has(NetworkUtils.status)) {
                        responseObjects.put(NetworkUtils.status, head.get(NetworkUtils.status));
                    }
                    if (head.has(NetworkUtils.error)) {
                        JSONObject error = head.getJSONObject(NetworkUtils.error);
                        if (error.has(NetworkUtils.errorcode)) {
                            responseObjects.put(NetworkUtils.errorcode, error.get(NetworkUtils.errorcode));
                        }
                        if (error.has(NetworkUtils.description)) {
                            responseObjects.put(NetworkUtils.description, error.get(NetworkUtils.description));
                        }
                    }
                } 
                if (response.has(NetworkUtils.body)) {
                    responseObjects.put(NetworkUtils.body, response.get(NetworkUtils.body));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return responseObjects;
        }
        return responseObjects;
    }

    public static int getStatus(JSONObject obj) {
        JSONObject head;
        int ret = 0;
        try {
            head = obj.getJSONObject(NetworkUtils.head);
            ret = head.getInt(NetworkUtils.status);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }
}
