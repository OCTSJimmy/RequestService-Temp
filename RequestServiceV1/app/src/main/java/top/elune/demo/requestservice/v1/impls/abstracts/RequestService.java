package top.elune.demo.requestservice.v1.impls.abstracts;

import android.support.annotation.NonNull;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import top.elune.demo.requestservice.v1.impls.abstracts.httpclient.AbstractRequestService;
import top.elune.demo.requestservice.v1.pojo.RequestArgs;

/**
 * Created by Guoheming on 2017/4/3.
 */

public class RequestService extends AbstractRequestService {
    @Override
    public List<String> getSupportMethods() {
        List<String> list = new ArrayList<>();
        list.add("GET");
        return list;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(RequestArgs args) {
        switch (args.getMethod()) {
            case "GET":
                return new HttpGet(args.getUrl());
        }
        return null;
    }

    @Override
    protected HttpEntity getHttpEntity(@NonNull RequestArgs args) {
        try {
            StringEntity stringEntity = new StringEntity(args.getContent());
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            return stringEntity;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
