package cn.eccto.study.sb.registservlet.filter;

import cn.eccto.study.sb.registservlet.servlet.MyServletListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * description
 *
 * @author EricChen 2019/12/09 21:40
 */
public class MyFilter extends HttpFilter {
    private static final Logger logger = LoggerFactory.getLogger(MyServletListener.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request instanceof HttpServletRequest ?
                ((HttpServletRequest) request).getRequestURL().toString() : "N/A";
        logger.debug("from filter, processing url: {}", url);
        chain.doFilter(request, response);
    }
}
