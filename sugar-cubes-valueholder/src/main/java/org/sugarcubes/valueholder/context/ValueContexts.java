package org.sugarcubes.valueholder.context;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

//CHECKSTYLE:OFF

/**
 * @author Maxim Butov
 */
public class ValueContexts {

    public static class GlobalValueContext implements ValueContext {

        private static final Map STORE = Collections.synchronizedMap(new WeakHashMap());

        @Override
        public Map getStore() {
            return STORE;
        }

        @Override
        public void remove() {
            STORE.clear();
        }

    }

    public static class ThreadLocalValueContext implements ValueContext {

        private static final ThreadLocal<Map> STORE = ThreadLocal.withInitial(HashMap::new);

        @Override
        public Map getStore() {
            return STORE.get();
        }

        @Override
        public void remove() {
            STORE.remove();
        }

    }

    public static final String STORE_KEY = ValueContext.class.getName() + ".store";

    private static final class Wrapper<S> implements Serializable {

        private S source;

        public S getSource() {
            return source;
        }

        public void setSource(S source) {
            this.source = source;
        }

    }

    public static class SpringApplicationValueContext extends AbstractStoreValueContext<ApplicationContext> {

        public SpringApplicationValueContext(ApplicationContext storeContext) {
            setStoreContext(storeContext);

            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
                ((ConfigurableApplicationContext) storeContext).getBeanFactory();

            if (beanFactory.getBeanDefinition(STORE_KEY) == null) {
                beanFactory.registerBeanDefinition(STORE_KEY,
                    BeanDefinitionBuilder.genericBeanDefinition(Wrapper.class).getBeanDefinition());
            }
        }

        private Wrapper getWrapper() {
            return getStoreContext().getBean(Wrapper.class, STORE_KEY);
        }

        @Override
        protected Object getFromContext() {
            return getWrapper().getSource();
        }

        @Override
        protected void putToContext(Object store) {
            getWrapper().setSource(store);
        }

        @Override
        protected void removeFromContext() {
            getWrapper().setSource(null);
        }

    }

    public static class ServletAppicationValueContext extends AbstractStoreValueContext<ServletContext> {

        public ServletAppicationValueContext(ServletContext storeContext) {
            setStoreContext(storeContext);
        }

        @Override
        protected Object getFromContext() {
            return getStoreContext().getAttribute(STORE_KEY);
        }

        @Override
        protected void putToContext(Object store) {
            getStoreContext().setAttribute(STORE_KEY, store);
        }

        @Override
        protected void removeFromContext() {
            getStoreContext().removeAttribute(STORE_KEY);
        }

    }

    private static class HttpSessionValueContext extends AbstractStoreValueContext<HttpSession> {

        public HttpSessionValueContext(HttpSession storeContext) {
            setStoreContext(storeContext);
        }

        @Override
        protected Object getFromContext() {
            return getStoreContext().getAttribute(STORE_KEY);
        }

        @Override
        protected void putToContext(Object store) {
            getStoreContext().setAttribute(STORE_KEY, store);
        }

        @Override
        protected void removeFromContext() {
            getStoreContext().removeAttribute(STORE_KEY);
        }

    }

    private static class ServletRequestValueContext extends AbstractStoreValueContext<ServletRequest> {

        public ServletRequestValueContext(ServletRequest storeContext) {
            setStoreContext(storeContext);
        }

        @Override
        protected Object getFromContext() {
            return getStoreContext().getAttribute(STORE_KEY);
        }

        @Override
        protected void putToContext(Object store) {
            getStoreContext().setAttribute(STORE_KEY, store);
        }

        @Override
        protected void removeFromContext() {
            getStoreContext().removeAttribute(STORE_KEY);
        }

    }

    private static class RequestAttributesValueContext extends AbstractStoreValueContext<RequestAttributes> {

        private final int scope;

        public RequestAttributesValueContext(int scope) {
            this.scope = scope;
        }

        @Override
        public RequestAttributes getStoreContext() {
            return RequestContextHolder.currentRequestAttributes();
        }

        @Override
        protected Object getFromContext() {
            return getStoreContext().getAttribute(STORE_KEY, scope);
        }

        @Override
        protected void putToContext(Object store) {
            getStoreContext().setAttribute(STORE_KEY, store, scope);
        }

        @Override
        protected void removeFromContext() {
            getStoreContext().removeAttribute(STORE_KEY, scope);
        }

    }

    private static class CustomValueContext extends AbstractStoreValueContext<Map> {

        private static final Map STORE = new WeakHashMap();

        private final Object context;

        public CustomValueContext(Object context) {
            this.context = context;
        }

        @Override
        protected Object getFromContext() {
            return STORE.get(context);
        }

        @Override
        protected void putToContext(Object store) {
            STORE.put(context, store);
        }

        @Override
        protected void removeFromContext() {
            STORE.remove(context);
        }
    }

    public static ValueContext global() {
        return new GlobalValueContext();
    }

    public static ValueContext threadLocal() {
        return new ThreadLocalValueContext();
    }

    public static ValueContext spring(ApplicationContext context) {
        return new SpringApplicationValueContext(context);
    }

    public static ValueContext servlet(ServletContext context) {
        return new ServletAppicationValueContext(context);
    }

    public static ValueContext httpSession(HttpSession context) {
        return new HttpSessionValueContext(context);
    }

    public static ValueContext httpRequest(ServletRequest context) {
        return new ServletRequestValueContext(context);
    }

    public static ValueContext httpAttributes(int scope) {
        return new RequestAttributesValueContext(scope);
    }

    public static ValueContext custom(Object context) {
        return new CustomValueContext(context);
    }

}

//CHECKSTYLE:ON
