package top.elune.demo.requestservice.v1.impls.abstracts.urlconnect;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import top.elune.demo.requestservice.v1.interfaces.IRequestService;
import top.elune.demo.requestservice.v1.pojo.RequestArgs;

public abstract class AbstractRequestService implements IRequestService {

    protected final HttpURLConnection getHttpURLConnect(RequestArgs args) throws IOException {
        HttpURLConnection httpURLConnect =
                (HttpURLConnection) new URL(args.getUrl())
                        .openConnection();
        httpURLConnect.setRequestMethod(args.getMethod());
        Map<String, String> headers = args.getHeaders();
        if (headers != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> e : entries) {
                httpURLConnect.addRequestProperty(e.getKey(), e.getValue());
            }
        }
        return httpURLConnect;
    }

    protected final void setUpContent(HttpURLConnection httpURLConnection, RequestArgs args) throws IOException {
        String content = args.getContent();
        if (TextUtils.isEmpty(content)) return;
        OutputStream outputStream = httpURLConnection.getOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
        int len = -1;
        byte[] buffer = new byte[4096];
        while ((len = bais.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
            outputStream.flush();
        }
        outputStream.close();
    }

    protected final String readResponse(InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
            baos.flush();
        }
        return new String(baos.toByteArray());
    }

    protected final String request(HttpURLConnection httpURLConnect, RequestArgs args) throws IOException {
        httpURLConnect.connect();
        setUpContent(httpURLConnect, args);
        InputStream is = httpURLConnect.getInputStream();
        String result = readResponse(is);
        is.close();
        httpURLConnect.disconnect();
        return result;
    }
}
