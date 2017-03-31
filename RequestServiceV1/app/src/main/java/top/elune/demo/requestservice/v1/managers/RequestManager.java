package top.elune.demo.requestservice.v1.managers;
import java.util.*;
import top.elune.demo.requestservice.v1.interfaces.*;
import top.elune.demo.requestservice.v1.pojo.*;

public class RequestManager
{
    private static Comparator<IRequestService> mRComparator = new RequestComparator();
    private static TreeSet<IRequestService> iRequestServices = new TreeSet<IRequestService>(mRComparator);
    
    private static class RequestComparator implements Comparator<IRequestService>
    {

        @Override
        public int compare(IRequestService p1, IRequestService p2)
        {  try
            {
                int result = 0;
                int p1Val = Math.abs(p1.getPriority());
                int p2Val = Math.abs(p2.getPriority());
                if(p1Val == p2Val){
                    if(p1Val == 0){
                        return 0;
                    }
                    String p1Name = p1.getClass().getSimpleName();
                    String p2Name = p2.getClass().getSimpleName();
                    result = p1Name.compareTo(p2Name);
                    if(result != 0) return result;
                    result = p1.hashCode() - p2.hashCode();
                    if(result != 0)return result > 0?1:-1;
                    int originP1Hashcode = System.identityHashCode(p1);
                    int originP2Hashcode = System.identityHashCode(p2);
                    result = originP1Hashcode - originP2Hashcode;
                    return result > 0? 1: result < 0?-1:0;
                }
                result = p1Val > p2Val ? 1 : -1;
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return 0;
            }

        }
    }
    public static boolean addRequestService(IRequestService iRequestService){
        return iRequestServices.add(iRequestService);
    }
    
    public static boolean removeRequestService(IRequestService iRequestServoce){
        return iRequestServices.remove(iRequestServoce);
    }
    
    public static String request(RequestArgs args){
        return requestCore(args);
    }
    
    private static String requestCore(RequestArgs args){
        if(iRequestServices.size() < 0){
            throw new IllegalStateException("There is no IRequestSercice has been added.");
        }
        if(args == null){
            throw new IllegalArgumentException("RequestArgs cannot be null");
        }
        for(IRequestService service : iRequestServices){
          List<String> supportMethods =  service.getSupportMethods();
          boolean isSupport = supportMethods.contains(args.getMeyhod());
          if(!isSupport){
              continue;
          }
          
          return service.request(args);
        }
        throw new IllegalStateException(
        "All of the IReqiestService " + 
        "that has been added could not "+
        "supported the request method:" 
        + args.getMeyhod());
    }
}
