package cn.eccto.study.netflix.eureka.client2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * description
 *
 * @author EricChen 2019/12/13 08:27
 */
@RestController
public class HelloWorldController {

    private Logger logger = LoggerFactory.getLogger(HelloWorldController.class);


    @GetMapping("/hello")
    public String helloWorld() throws Exception {
        //让处理线程等待几秒钟
        int sleepTime = new Random().nextInt(3000);
        Thread.sleep(sleepTime);
        logger.info("sleepTime:" + sleepTime);
        return "Hello World form client 8004";
    }

    @GetMapping("/")
    public Map index(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Map answer = new LinkedHashMap(4);
        Map<String, String> allRequestParam = getAllRequestParam(request);
        Map<String, String> allReqHeaderParam = getAllHeaderParam(request);
        Map<String, String> allRspHeaderParam = getAllHeaderParam(httpServletResponse);
        answer.put("info", "04-netflix-eureka-client-8004");
        answer.put("param", allRequestParam);
        answer.put("request_header", allReqHeaderParam);
        answer.put("response_header", allRspHeaderParam);
        return answer;
    }

    /**
     * 获取客户端请求参数中所有的信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //如果字段的值为空，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    private Map<String, String> getAllHeaderParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration temp = request.getHeaderNames();//获取请求头的所有name值
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getHeader(en);
                res.put(en, value);
                //如果字段的值为空，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    private Map<String, String> getAllHeaderParam(final HttpServletResponse response) {
        Map<String, String> res = new HashMap<String, String>();
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(header -> {
            String header1 = response.getHeader(header);
            if (!StringUtils.isEmpty(header)) {
                res.put(header, header1);
            }
        });
        return res;
    }

}
