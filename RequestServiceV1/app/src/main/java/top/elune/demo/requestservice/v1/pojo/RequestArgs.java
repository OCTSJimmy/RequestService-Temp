package top.elune.demo.requestservice.v1.pojo;
import org.apache.http.client.methods.*;
import android.text.*;

public class RequestArgs
{
    private String mUrl;
    private String mMeyhod;
    private String mContent;
    private HttpHead mHeaders;
    private RequestArgs(String url){
        mUrl=url;
    }

    public final static class Builder {
        private RequestArgs mRequestArgs;
        public Builder(String url, String method){
            setUrl(url);
            setMethod(method);
        }
        
        public Builder setUrl(String url){
            if(TextUtils.isEmpty(url))
                throw new IllegalArgumentException("The url cannot be empty!!");
            mRequestArgs = new RequestArgs(url);
            return this;
        }
        
        public Builder setMethod(String method){
            if(TextUtils.isEmpty(method)){
                mRequestArgs.mMeyhod = "GET";
            } else {
                mRequestArgs.mMeyhod = method;
            }
            return this;
        }
        
        public Builder setContent(String content){
            mRequestArgs.mContent = content;
            return this;
        }
        public Builder setHeaders(HttpHead headers){
            mRequestArgs.mHeaders = headers;
            return this;
        }
        public RequestArgs build(){
            return mRequestArgs;
        }
    }
    
    public String getUrl()
    {
        return mUrl;
    }

    public String getMeyhod()
    {
        return mMeyhod;
    }

    public String getContent()
    {
        return mContent;
    }

    public HttpHead getHeaders()
    {
        return mHeaders;
    }
}
