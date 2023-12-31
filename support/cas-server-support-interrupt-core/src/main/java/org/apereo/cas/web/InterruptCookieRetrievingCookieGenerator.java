package org.apereo.cas.web;

import org.apereo.cas.web.cookie.CookieGenerationContext;
import org.apereo.cas.web.support.gen.CookieRetrievingCookieGenerator;

import java.io.Serial;

/**
 * This is {@link InterruptCookieRetrievingCookieGenerator}.
 *
 * @author Misagh Moayyed
 * @since 6.5.0
 */
public class InterruptCookieRetrievingCookieGenerator extends CookieRetrievingCookieGenerator {

    @Serial
    private static final long serialVersionUID = 196881424248847492L;

    public InterruptCookieRetrievingCookieGenerator(final CookieGenerationContext context) {
        super(context);
    }
}

