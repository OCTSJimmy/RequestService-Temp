package top.elune.demo.requestservice.v1.impls;
import top.elune.demo.requestservice.v1.interfaces.*;
import java.io.*;
import java.net.*;
import top.elune.demo.requestservice.v1.pojo.*;

public abstract class AbstractRequestService implements IRequestService
{
    
    public HttpURLConnection getHttpURLConnect(RequestArgs args) throws IOException{
        HttpURLConnection httpURLConnect =
            (HttpURLConnection) new URL(args.getUrl())
            .openConnection();
            httpURLConnect.setRequestMethod(args.getMeyhod());
            httpURLConnect.connect();
          
            return httpURLConnect;
    }
    
    public String readResponse(InputStream is) throws IOException{
        byte[] buffer = new byte[4096];
        int len = -1;
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        while((len = is.read(buffer))!=-1){
            baos.write(buffer,0, len);
            baos.flush();
        }
        return new String( baos.toByteArray());
    }
}
