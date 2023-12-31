---
layout: default
title: CAS - Service Registry Initialization
category: Services
---

{% include variables.html %}

# Service Registry Initialization

Upon startup and configuration permitting, the registry is able to auto initialize 
itself from default JSON service definitions available to CAS. Populating the 
registry with a set of default services serves as *a starting point* and an 
example at that where one is able to immediately start integrating applications. 
For production purposes, it is recommended that you pick a more 
relevant [option for storage](Service-Management.html) and disable this behavior.

The default service definitions that are used by the initializer component ship 
with CAS by default and are put on the classpath. The initializer is able to detect 
all service definitions files found on the classpath (i.e. `src/main/resources/services`) 
and import them into the *real* service registry used while also taking into account all
active Spring application profiles. For example, if the current Spring application profiles are `dev` and `qa`,
then the following locations will be evaluated by CAS to locate service definitions:

- `classpath*:/services/dev/*.json`
- `classpath*:/services/qa/*.json`
- `classpath*:/services/*.json`

This means that if this behavior is enabled and additional files are found on the classpath at the relevant 
paths, CAS will take the default services as well as any and all other services found 
in order to import them into the service registry used. Note that the location of the JSON files while typically set to the 
classpath may be controlled via CAS properties; the same setting property that controls 
the location of the JSON service files for the JSON service registry is used by the 
initialization logic to locate service files. 

Again, this behavior is only useful as a starting point, an example and for small and 
specialized deployments. It is recommended that you take explicit control 
over the registry and register services and applications which you have fully authorized.

{% include_cached casproperties.html properties="cas.service-registry.core" %}
