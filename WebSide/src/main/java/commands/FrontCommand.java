package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrontCommand {
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected static final Logger LOG = LoggerFactory.getLogger("commands");

    public void initialize(ServletContext servletContext, HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
    }

    public abstract void processGet() throws ServletException, IOException;

    public abstract void processPost() throws ServletException, IOException;

    protected void forward(String target) throws ServletException, IOException {
        LOG.info("forward to {}.jspx", target);
        target = String.format("/WEB-INF/views/%s.jspx", target);
        RequestDispatcher dispatcher = context.getRequestDispatcher(target);
        dispatcher.forward(request, response);

    }

}