package org.apereo.cas.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * This is {@link DefaultRegisteredServiceProxyGrantingTicketExpirationPolicy}.
 *
 * @author Jerome LELEU
 * @since 6.2.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DefaultRegisteredServiceProxyGrantingTicketExpirationPolicy implements RegisteredServiceProxyGrantingTicketExpirationPolicy {
    @Serial
    private static final long serialVersionUID = 1122553887352573119L;

    private long maxTimeToLiveInSeconds;
}
