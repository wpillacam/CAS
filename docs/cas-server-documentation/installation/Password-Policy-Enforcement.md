---
layout: default
title: CAS - Password Policy Enforcement
category: Authentication
---
{% include variables.html %}

# Password Policy Enforcement

Password policy enforcement attempts to:

- Detect a number of scenarios that would otherwise prevent user authentication based on user account status.
- Warn users whose account status is near a configurable expiration date and redirect the flow to an external identity management system.

## LDAP

The below scenarios are by default considered errors preventing authentication in a generic manner through
the normal CAS login flow. LPPE intercepts the authentication flow, detecting the above standard error codes.
Error codes are then translated into proper messages in the CAS login flow and would allow the user to take proper action,
fully explaining the nature of the problem.

- `ACCOUNT_LOCKED`
- `ACCOUNT_DISABLED`
- `ACCOUNT_EXPIRED`
- `INVALID_LOGON_HOURS`
- `INVALID_WORKSTATION`
- `PASSWORD_MUST_CHANGE`
- `PASSWORD_EXPIRED`

The translation of LDAP errors into CAS workflow is all
handled by [ldaptive](http://www.ldaptive.org/docs/guide/authentication/accountstate). 

{% include_cached casproperties.html properties="cas.authn.ldap" includes=".password-policy" %}

### Account Expiration Notification

LPPE is also able to warn the user when the account is about to expire. The expiration policy is
determined through pre-configured LDAP attributes with default values in place.

## JDBC

A certain number of database authentication schemes have limited support for detecting locked/disabled/etc accounts
via column names that are defined in the CAS settings. You will need to consult the documentation for the relevant
database authentication strategy and type and configure CAS to enforce password policy checks.

