---
layout: default
title: CAS - Amazon Cloud Directory Authentication
category: Authentication
---
{% include variables.html %}


# Amazon Cloud Directory Authentication

Amazon Cloud Directory is a highly available multi-tenant directory-based store 
in AWS. These directories scale automatically to hundreds of millions of 
objects as needed for applications. This lets operation's staff 
focus on developing and deploying applications that drive the business, not managing directory infrastructure.

To learn more, please [see this guide](http://docs.aws.amazon.com/directoryservice/latest/admin-guide/directory_amazon_cd.html).

## Configuration

Support is enabled by including the following dependency in the WAR overlay:

{% include_cached casmodule.html group="org.apereo.cas" module="cas-server-support-cloud-directory-authentication" %}

{% include_cached casproperties.html properties="cas.authn.cloud-directory"  %}

AWS credentials are fetched from the following sources automatically, where relevant 
and made possible via CAS configuration:

1. EC2 instance metadata linked to the IAM role.
2. External properties file that contains `accessKey` and `secretKey` as property keys.
3. AWS profile path and profile name.
4. System properties that include `aws.accessKeyId`, `aws.secretKey` and `aws.sessionToken`
5. Environment variables that include `AWS_ACCESS_KEY_ID`, `AWS_SECRET_KEY` and `AWS_SESSION_TOKEN`.
6. Properties file on the classpath as `awscredentials.properties` that contains `accessKey` and `secretKey` as property keys.
7. Static credentials for access key and secret provided directly by the configuration at hand (logging, etc).

## Troubleshooting

To enable additional logging, configure the log4j configuration file to add the following levels:

```xml
...
<Logger name="com.amazonaws" level="debug" additivity="false">
    <AppenderRef ref="casConsole"/>
    <AppenderRef ref="casFile"/>
</Logger>
...
