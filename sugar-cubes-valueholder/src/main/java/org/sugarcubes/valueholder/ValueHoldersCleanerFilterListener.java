package org.sugarcubes.valueholder;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;

/**
 * Хелпер, который очищает объекты {@link ValueHolder}, созданные в контексте потока, по завершении запроса.
 * Может быть сконфигурирован в web.xml как filter либо listener.
 *
 * @author Maxim Butov
 */
public class ValueHoldersCleanerFilterListener implements Filter, ServletRequestListener {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }
        finally {
            cleanup();
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        cleanup();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    }

    private void cleanup() {
        ContextValueHolder.removeContext(Thread.currentThread());
    }

}
