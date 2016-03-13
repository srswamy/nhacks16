package com.nhacks.share.Network;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

/**
 * Created by SagarkumarDave on 12/21/2015.
 */
public class NetworkRequestManager {
    @IntRange(from = 0) public static final int TIMEOUT_MS = 10000;
    @IntRange (from = 0) public static final int TIMEOUT_MS_MAX = 30000;

    private static RequestQueue mRequestQueue;
    private static RetryPolicy sRetryPolicy;

    public static synchronized void init(@NonNull final Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
            sRetryPolicy = new DefaultRetryPolicy(TIMEOUT_MS, 3, 1.5f);
        }
    }

    /**
     * Adds the provided {@link Request}s in order to the {@link RequestQueue}. Retry policy and caching are set automatically to their default values.
     */
    public static void addRequests(@NonNull final Request... requests) {
        for (Request request : requests) {
            if (request != null) {
                // request.setShouldCache(BuildConfig.ENABLE_VOLLEY_CACHE);
                request.setRetryPolicy(sRetryPolicy);
                mRequestQueue.add(request);
            }
        }
    }

    /**
     * Attempts to cancel the provided request. If the request has already been completed, this method does nothing.
     */
    public static void removeRequest(@NonNull final Request request) {
        request.cancel();
    }

    /**
     * Deletes all cached responses from the underlying {@link RequestQueue}. This does not cancel any pending requests.
     */
    public static void reset() {
        if (mRequestQueue != null) {
            mRequestQueue.getCache().clear();
        }
    }
}