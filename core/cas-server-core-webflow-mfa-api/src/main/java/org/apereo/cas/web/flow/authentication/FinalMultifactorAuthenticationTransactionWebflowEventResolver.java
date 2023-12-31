package org.apereo.cas.web.flow.authentication;

import org.apereo.cas.audit.AuditActionResolvers;
import org.apereo.cas.audit.AuditResourceResolvers;
import org.apereo.cas.audit.AuditableActions;
import org.apereo.cas.web.flow.resolver.impl.CasWebflowEventResolutionConfigurationContext;
import org.apereo.inspektr.audit.annotation.Audit;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import java.util.Set;

/**
 * This is {@link FinalMultifactorAuthenticationTransactionWebflowEventResolver}.
 *
 * @author Misagh Moayyed
 * @since 7.0.0
 */
public class FinalMultifactorAuthenticationTransactionWebflowEventResolver extends BaseMultifactorAuthenticationProviderEventResolver {

    public FinalMultifactorAuthenticationTransactionWebflowEventResolver(
        final CasWebflowEventResolutionConfigurationContext webflowEventResolutionConfigurationContext) {
        super(webflowEventResolutionConfigurationContext);
    }

    @Override
    public Set<Event> resolveInternal(final RequestContext context) throws Throwable {
        return handleAuthenticationTransactionAndGrantTicketGrantingTicket(context);
    }

    @Audit(action = AuditableActions.AUTHENTICATION_EVENT,
        actionResolverName = AuditActionResolvers.AUTHENTICATION_EVENT_ACTION_RESOLVER,
        resourceResolverName = AuditResourceResolvers.AUTHENTICATION_EVENT_RESOURCE_RESOLVER)
    @Override
    public Event resolveSingle(final RequestContext context) throws Throwable {
        return super.resolveSingle(context);
    }
}
