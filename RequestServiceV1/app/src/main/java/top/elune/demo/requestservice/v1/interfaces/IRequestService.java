package top.elune.demo.requestservice.v1.interfaces;
import top.elune.demo.requestservice.v1.pojo.*;
import java.util.*;

public interface IRequestService
{
    /**
     * 请求核心代码
     * @param RequestArgs 请求参数列表
     * @return 请求结果
     */
    String request(RequestArgs args);
    /**
     * 提供该类支持的请求方式
     * @return 所支持的请求方式
     */
    List<String> getRequiredMethods();
    /**
     * @return
     * 取得优先级，
     * 返回值的绝对值越小，
     * 则优先级越高，从1开始计算
     * 0将作为保留数，为0，则表示放弃优先级
     * (即优先级最低)
     * 相同优先级，将先按类名ASCII升序排序
     * 再按hashcode()升序排序，
     * 最后按系统原始hashcode升序排序
     * 
     * 优先级较高的类对象，将优先参与解析
     * 当该类对象支持的请求方式存在，
     * 与即将开始的请求中，
     * 参数表配置的请求方式一致的设定，
     * 即进入请求解析流程，这将导致
     * 优先级较低的类对象，将会被忽略
     * 不论请求成功与否，都不再参与解析
     * 
     * 当该类不存在匹配的请求方式，
     * 则取较低且最临近的对象展开新一轮匹配
     */
    int getPriority();
}
