package top.elune.demo.requestservice.v1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import top.elune.demo.requestservice.v1.impls.abstracts.RequestService;
import top.elune.demo.requestservice.v1.managers.RequestManager;
import top.elune.demo.requestservice.v1.pojo.RequestArgs;
import top.elune.demo.requestservice.v1.pojo.RequestArgs.Builder;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView tv = (TextView) findViewById(R.id.tv);
        Builder builder = new Builder("http://www.baidu.com", "GET");
        final RequestArgs args = builder.build();
        RequestService service = new RequestService();
        RequestManager.addRequestService(service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String request = RequestManager.request(args);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(request);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
