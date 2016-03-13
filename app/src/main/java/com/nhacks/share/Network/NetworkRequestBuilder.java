package com.nhacks.share.Network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SagarkumarDave on 12/21/2015.
 */
public class NetworkRequestBuilder {

    /**
     * Generates a request that will fetch a raw string response from the provided URL.
     *
     * @param url       The URL of the endpoint.
     * @param onSuccess Listener for when the call finishes.
     * @param onError   Listener for when the call fails.
     *
     * @return A request object which can be passed to the NetworkRequestManager
     */
    @NonNull
    public static Request<String> getRawRequest(@NonNull final String url, @Nullable final Response.Listener<String> onSuccess,
                                                @Nullable final Response.ErrorListener onError) {
        return new StringRequest(
                Request.Method.GET,
                url,
                onSuccess,
                onError
        );
    }

    /**
     * Generates a request that will fetch a JSONObject from the provided URL. onSuccess will never be called with a null JSONObject.
     *
     * @param url       The URL of the JSON.
     * @param onSuccess Listener for when the call finishes.
     * @param onError   Listener for when the call fails.
     *
     * @return A request object which can be passed to the NetworkRequestManager
     */
    @NonNull
    public static Request<JSONObject> getRawJSONRequest(@NonNull final String url, @Nullable final Response.Listener<JSONObject> onSuccess,
                                                        @Nullable final Response.ErrorListener onError) {
        return new JsonRequest<JSONObject>(
                Request.Method.GET,
                url,
                null,
                onSuccess,
                onError
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject parsedJson = new JSONObject(jsonString);
                    return Response.success(parsedJson, HttpHeaderParser.parseCacheHeaders(response));
                } catch (final Exception e) {
                }
                return Response.error(new VolleyError(response));
            }
        };
    }

    /**
     * Generates a request that will fetch a JSONArray from the provided URL. onSuccess will never be called with a null JSONArray.
     *
     * @param url       The URL of the JSON.
     * @param onSuccess Listener for when the call finishes.
     * @param onError   Listener for when the call fails.
     *
     * @return A request object which can be passed to the NetworkRequestManager
     */
    @NonNull
    public static Request<JSONArray> getRawJSONArrayRequest(@NonNull final String url, @Nullable final Response.Listener<JSONArray> onSuccess,
                                                            @Nullable final Response.ErrorListener onError) {
        return new JsonRequest<JSONArray>(
                Request.Method.GET,
                url,
                null,
                onSuccess,
                onError
        ) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONArray parsedJson = new JSONArray(jsonString);
                    return Response.success(parsedJson, HttpHeaderParser.parseCacheHeaders(response));
                } catch (final Exception e) {
                }
                return Response.error(new VolleyError(response));
            }
        };
    }

}
