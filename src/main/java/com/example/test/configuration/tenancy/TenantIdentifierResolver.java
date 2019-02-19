package com.example.test.configuration.tenancy;

import com.example.test.configuration.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author s.vareyko
 * @since 23.10.2018
 */
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    private final static String UNKNOWN_TENANT = "unknown";

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Optional.ofNullable(TenantContextHolder.getContext()).orElse(UNKNOWN_TENANT);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
