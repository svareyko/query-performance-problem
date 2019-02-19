package com.example.test.configuration;

import org.springframework.util.Assert;

/**
 * @author s.vareyko
 * @since 22.10.2018
 */
public class TenantContextHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static void setContext(final String context) {
        Assert.notNull(context, "context key cannot be null");
        holder.set(context);
    }

    public static String getContext() {
        return holder.get();
    }
}
