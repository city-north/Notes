package cn.eccto.study.netflix.eureka.client.controller;

import cn.eccto.study.common.utils.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World form client 8002";
    }

    @GetMapping("/")
    public Map index(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Map answer = new LinkedHashMap(4);
        Map<String, String> allRequestParam = getAllRequestParam(request);
        Map<String, String> allReqHeaderParam = getAllHeaderParam(request);
        Map<String, String> allRspHeaderParam = getAllHeaderParam(httpServletResponse);
        answer.put("info", "02-netflix-eureka-client-8002");
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
        headerNames.forEach(header->{
            String header1 = response.getHeader(header);
            if (!StringUtils.isEmpty(header)) {
                res.put(header, header1);
            }
        });
        return res;
    }

}
