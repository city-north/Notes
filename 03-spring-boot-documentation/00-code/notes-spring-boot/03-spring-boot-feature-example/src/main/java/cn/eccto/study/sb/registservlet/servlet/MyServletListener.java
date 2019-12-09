package cn.eccto.study.sb.registservlet.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * description
 *
 * @author EricChen 2019/12/09 21:41
 */
public class MyServletListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(MyServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("from ServletContextListener: {} context initialized", sce.getServletContext().getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
