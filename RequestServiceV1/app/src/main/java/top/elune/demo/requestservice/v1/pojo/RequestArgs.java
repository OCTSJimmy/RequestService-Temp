package top.elune.demo.requestservice.v1.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RequestArgs {
    private String mUrl;
    @RequestMethod
    private String mMethod;
    private String mContent;
    private Map<String, String> mHeaders;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({"GET", "POST"})
    private @interface RequestMethod {
    }

    private RequestArgs(String url) {
        mUrl = url;
    }

    public final static class Builder {
        private RequestArgs mRequestArgs;

        public Builder(@NonNull String url, @RequestMethod @NonNull String method) {
            setUrl(url);
            setMethod(method);
        }

        @NonNull
        public Builder setUrl(@NonNull String url) {
            if (TextUtils.isEmpty(url))
                throw new IllegalArgumentException("The url cannot be empty!!");
            mRequestArgs = new RequestArgs(url);
            return this;
        }

        @NonNull
        public Builder setMethod(@RequestMethod @NonNull String method) {
            if (TextUtils.isEmpty(method)) {
                mRequestArgs.mMethod = "GET";
            } else {
                mRequestArgs.mMethod = method;
            }
            return this;
        }

        @NonNull
        public Builder setContent(@NonNull String content) {
            mRequestArgs.mContent = content;
            return this;
        }

        @NonNull
        public Builder setHeaders(@NonNull Map<String, String> headers) {
            mRequestArgs.mHeaders = headers;
            return this;
        }

        @NonNull
        public Builder addHeader(@NonNull String key, @NonNull String value) {
            if (mRequestArgs.mHeaders == null) {
                mRequestArgs.mHeaders = new HashMap<>();
            }
            mRequestArgs.mHeaders.put(key, value);
            return this;
        }

        @NonNull
        public Builder removeHeader(@NonNull String key) {
            if (mRequestArgs.mHeaders != null) {
                mRequestArgs.mHeaders.remove(key);
            }
            return this;
        }

        @NonNull
        public RequestArgs build() {
            return mRequestArgs;
        }
    }

    @NonNull
    public String getUrl() {
        return mUrl;
    }

    @RequestMethod
    public String getMethod() {
        return mMethod;
    }

    @Nullable
    public String getContent() {
        return mContent;
    }

    /**
     * @return 返回Headers集合副本（浅拷贝）
     */
    @Nullable
    public Map<String, String> getHeaders() {
        try {
            return setupHeaderResultWithClone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setupHeaderResult();
    }

    /**
     * @return 使用复杂反射，构建Header集合并返回
     */
    @NonNull
    private Map<String, String> setupHeaderResult() {
        try {
            Class<? extends Map> aClass = mHeaders.getClass();
            //Maybe it will throw exception;
            Map map = aClass.newInstance();
            Set<Entry<String, String>> entries = mHeaders.entrySet();
            for (Entry<String, String> e : entries) {
                //noinspection unchecked
                map.put(e.getKey(), e.getValue());
            }
            //noinspection unchecked
            return map;
        } catch (Exception e) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            Set<Entry<String, String>> entries = mHeaders.entrySet();
            for (Entry<String, String> entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }
    }

    /**
     * @return 使用Clone方法构建Header集合，并返回
     */
    @Nullable
    private Map<String, String> setupHeaderResultWithClone() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (mHeaders == null) return null;
        if (mHeaders instanceof Cloneable) {
            Cloneable headers = (Cloneable) mHeaders;
            Method clone = Cloneable.class.getDeclaredMethod("clone");
            clone.setAccessible(true);
            //noinspection unchecked
            return (Map<String, String>) clone.invoke(headers);
        }
        return setupHeaderResult();
    }
}
