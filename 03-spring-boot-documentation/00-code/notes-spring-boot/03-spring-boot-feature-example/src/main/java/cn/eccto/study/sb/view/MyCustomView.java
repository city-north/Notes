package cn.eccto.study.sb.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 自定义 View
 *
 * @author EricChen 2019/12/09 21:56
 */
@Component("myCustomView")
public class MyCustomView implements View {

    private static final Logger logger = LoggerFactory.getLogger(MyCustomView.class);

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        map.forEach((k, v) -> logger.debug("render map key={},value={}", k, v));
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write("msg rendered in MyCustomView: " + map.get("msg"));
    }
}
