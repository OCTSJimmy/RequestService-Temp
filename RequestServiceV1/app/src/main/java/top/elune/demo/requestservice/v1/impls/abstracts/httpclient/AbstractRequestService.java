package top.elune.demo.requestservice.v1.impls.abstracts.httpclient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import top.elune.demo.requestservice.v1.exceptions.CannotSupportRequestMethodException;
import top.elune.demo.requestservice.v1.interfaces.IRequestService;
import top.elune.demo.requestservice.v1.pojo.RequestArgs;

public abstract class AbstractRequestService implements IRequestService {

    @NonNull
    protected HttpClient getHttpClient() throws IOException {
        return new DefaultHttpClient();
    }

    @SuppressWarnings("WeakerAccess")
    protected void setUpHeaders(@NonNull HttpUriRequest httpUriRequest, @NonNull RequestArgs args) {
        Map<String, String> headers = args.getHeaders();
        if (headers != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> e : entries) {
                httpUriRequest.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    @NonNull
    protected HttpResponse getHttpResponse(@NonNull HttpClient httpClient, @NonNull HttpUriRequest httpUriRequest) throws IOException {
        return httpClient.execute(httpUriRequest);
    }

    @NonNull
    protected String readResponse(HttpResponse httpResponse) throws IOException {
        InputStream is = httpResponse.getEntity().getContent();
        byte[] buffer = new byte[4096];
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
            baos.flush();
        }
        return new String(baos.toByteArray());
    }

    @Override
    public String request(@NonNull RequestArgs args) throws IOException {
        HttpClient httpClient = getHttpClient();
        HttpUriRequest httpUriRequest = getHttpUriRequest(args);
        if (httpUriRequest == null) {
            throw new CannotSupportRequestMethodException();
        }
        setUpHeaders(httpUriRequest, args);

        if (httpUriRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) httpUriRequest;
            HttpEntity httpEntity = getHttpEntity(args);
            if (httpEntity != null) {
                entityEnclosingRequest.setEntity(httpEntity);
            }
        }

        HttpResponse httpResponse = getHttpResponse(httpClient, httpUriRequest);
        return readResponse(httpResponse);
    }

    @Nullable
    protected abstract HttpUriRequest getHttpUriRequest(RequestArgs args);

    @Nullable
    protected abstract HttpEntity getHttpEntity(RequestArgs args);
}
