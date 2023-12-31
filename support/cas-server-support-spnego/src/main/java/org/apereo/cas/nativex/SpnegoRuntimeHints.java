package org.apereo.cas.nativex;

import org.apereo.cas.support.spnego.authentication.principal.SpnegoCredential;
import org.apereo.cas.support.spnego.authentication.principal.SpnegoPrincipalResolver;
import org.apereo.cas.util.nativex.CasRuntimeHintsRegistrar;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import java.util.Collection;
import java.util.List;

/**
 * This is {@link SpnegoRuntimeHints}.
 *
 * @author Misagh Moayyed
 * @since 7.0.0
 */
public class SpnegoRuntimeHints implements CasRuntimeHintsRegistrar {
    @Override
    public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
        hints.serialization().registerType(SpnegoCredential.class);

        registerReflectionHints(hints, List.of(
            SpnegoCredential.class,
            SpnegoPrincipalResolver.class
        ));
    }

    private static void registerReflectionHints(final RuntimeHints hints, final Collection entries) {
        entries.forEach(el -> hints.reflection().registerType((Class) el,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS,
            MemberCategory.PUBLIC_FIELDS));
    }
}
