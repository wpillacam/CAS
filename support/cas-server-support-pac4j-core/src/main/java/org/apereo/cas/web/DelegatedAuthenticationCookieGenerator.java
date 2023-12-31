package org.apereo.cas.web;

import org.apereo.cas.web.cookie.CookieGenerationContext;
import org.apereo.cas.web.support.gen.CookieRetrievingCookieGenerator;

import java.io.Serial;

/**
 * This is {@link DelegatedAuthenticationCookieGenerator}.
 *
 * @author Misagh Moayyed
 * @since 6.4.0
 */
public class DelegatedAuthenticationCookieGenerator extends CookieRetrievingCookieGenerator {
    @Serial
    private static final long serialVersionUID = -7787542644443030849L;

    public DelegatedAuthenticationCookieGenerator(final CookieGenerationContext context) {
        super(context);
    }
}
