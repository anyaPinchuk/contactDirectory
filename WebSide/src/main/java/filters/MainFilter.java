package filters;

import controllers.FrontControllerServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/")
public class MainFilter implements Filter{
    private static final Logger LOG = LoggerFactory.getLogger(FrontControllerServlet.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        LOG.info("redirect from / to app/contacts starting");
        httpServletResponse.sendRedirect("app/contacts");
    }

    @Override
    public void destroy() {

    }
}
