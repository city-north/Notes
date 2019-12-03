# SpringBoot 教程

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其中代码仅用于学习笔记,不用于商业用途

## 目录结构

#### 快速实例

-  [01-spring-boot-kick-start.md](01-getting-started/01-spring-boot-kick-start.md) 

#### maven 配置

- [02-spring-boot-starts.md](02-maven-configuration/02-spring-boot-starts.md) 

- [03-using-springboot-without-parent-pom.md](02-maven-configuration/03-using-springboot-without-parent-pom.md) 

使用 maven 的 import 作为继承`spring-boot-starter-parent.`的另一个选择

- [04-spring-boot-maven-plugin.md](02-maven-configuration/04-spring-boot-maven-plugin.md) 

使用 SpringBoot maven 插件

#### 自动配置

-  [05-enable-auto-configuration.md](03-auto-configuration/05-enable-auto-configuration.md) 
-  [06-component-scan.md](03-auto-configuration/06-component-scan.md) 
-  [07-spring-boot-application.md](03-auto-configuration/07-spring-boot-application.md) 
-  [08-customizing-app-startup.md](03-auto-configuration/08-customizing-app-startup.md) 
-  [09-how-auto-configuration-works.md](03-auto-configuration/09-how-auto-configuration-works.md)

#### Web Application

-  [10-serving-static-web-contents.md](04-web-application/10-serving-static-web-contents.md) 
-  [12-setting-cache-period-for-static-resources.md](04-web-application/12-setting-cache-period-for-static-resources.md) 
-  [13-serving-jsp-pages.md](04-web-application/13-serving-jsp-pages.md) 
-  [14-custom-favicon.md](04-web-application/14-custom-favicon.md) 
-  [15-message-http-message-converters.md](04-web-application/15-message-http-message-converters.md) 
-  [16-using-servlet-components.md](04-web-application/16-using-servlet-components.md) 
-  [17-bean-name-view-resolver-auto-registration.md](04-web-application/17-bean-name-view-resolver-auto-registration.md) 
-  [18-understand-spring-boot-mvc-auto-configuration.md](04-web-application/18-understand-spring-boot-mvc-auto-configuration.md) 
-  [19-registering-a-custom-formatter.md](04-web-application/19-registering-a-custom-formatter.md) 
-  [20-register-custom-configurable-web-binding-initializer-as-a-bean.md](04-web-application/20-register-custom-configurable-web-binding-initializer-as-a-bean.md) 

#### Web Application - Views

-  [10-serving-static-web-contents.md](04-web-application/10-serving-static-web-contents.md) 
-  [12-setting-cache-period-for-static-resources.md](04-web-application/12-setting-cache-period-for-static-resources.md) 
-  [13-serving-jsp-pages.md](04-web-application/13-serving-jsp-pages.md) 
-  [14-custom-favicon.md](04-web-application/14-custom-favicon.md) 
-  [15-message-http-message-converters.md](04-web-application/15-message-http-message-converters.md) 
-  [16-using-servlet-components.md](04-web-application/16-using-servlet-components.md) 
-  [17-bean-name-view-resolver-auto-registration.md](04-web-application/17-bean-name-view-resolver-auto-registration.md) 
-  [18-understand-spring-boot-mvc-auto-configuration.md](04-web-application/18-understand-spring-boot-mvc-auto-configuration.md) 
-  [19-registering-a-custom-formatter.md](04-web-application/19-registering-a-custom-formatter.md) 
-  [20-register-custom-configurable-web-binding-initializer-as-a-bean.md](04-web-application/20-register-custom-configurable-web-binding-initializer-as-a-bean.md) 

#### Web Application - Error Handling

- [ 01-disable-whitelable-error-page.md](06-web-application-error-handling/01-disable-whitelable-error-page.md) 
-  [02-custom-error-page-jsp.md](06-web-application-error-handling/02-custom-error-page-jsp.md) 
-  [03-custom-error-page-thymeleaf.md](06-web-application-error-handling/03-custom-error-page-thymeleaf.md) 
-  [04-mapping-http-response-status-code-to-custom-jsp-error-page.md](06-web-application-error-handling/04-mapping-http-response-status-code-to-custom-jsp-error-page.md) 
-  [05-mapping-http-response-status-code-to-static-html-pages.md](06-web-application-error-handling/05-mapping-http-response-status-code-to-static-html-pages.md) 
-  [06-mapping-http-response-status-code-to-a-template-engine.md](06-web-application-error-handling/06-mapping-http-response-status-code-to-a-template-engine.md) 
-  [07-mapping-error-to-custom-controller.md](06-web-application-error-handling/07-mapping-error-to-custom-controller.md) 
-  [08-using-error-attributes-in-custom-controller.md](06-web-application-error-handling/08-using-error-attributes-in-custom-controller.md) 
-  [09-return-json-response-from-custom-error-controller.md](06-web-application-error-handling/09-return-json-response-from-custom-error-controller.md) 
-  [10-mapping-errors-in-servlet-based-application-outside-of-spring-mvc.md](06-web-application-error-handling/10-mapping-errors-in-servlet-based-application-outside-of-spring-mvc.md) 

#### Boot Project Structure

-  [01-exploded-project-structure.md](07-boot-project-structure/01-exploded-project-structure.md) 
-  [02-execuable-jar-file-structure.md](07-boot-project-structure/02-execuable-jar-file-structure.md) 
-  [03-execuable-war-file-structure.md](07-boot-project-structure/03-execuable-war-file-structure.md) 
-  [04-exploded-web-application-with-jar-and-war.md](07-boot-project-structure/04-exploded-web-application-with-jar-and-war.md) 

#### Boot Developer tools

-  [01-auto-restart-using-spring-boot-devtools.md](08-boot-develop-tools/01-auto-restart-using-spring-boot-devtools.md) 
-  [02-auto-browser-refresh-with-live-reload.md](08-boot-develop-tools/02-auto-browser-refresh-with-live-reload.md) 
-  [03-devtools.md](08-boot-develop-tools/03-devtools.md) 
-  [04-include-exclude-file-to-trigger-restart.md](08-boot-develop-tools/04-include-exclude-file-to-trigger-restart.md) 
-  [05-remote-auto-restart-and-live-reload.md](08-boot-develop-tools/05-remote-auto-restart-and-live-reload.md) 

#### Application Helpers

-  [01-accessing-app-arguments.md](09-application-helper/01-accessing-app-arguments.md) 
-  [02-use-application-runner-and-command-line-runner.md](09-application-helper/02-use-application-runner-and-command-line-runner.md) 
-  [03-destruction-callback.md](09-application-helper/03-destruction-callback.md) 
-  [04-application-exit-code.md](09-application-helper/04-application-exit-code.md) 

#### Application events and listeners

-  [01-listening-to-application-events-with-eventlistener.md](10-application-event-and-listener/01-listening-to-application-events-with-eventlistener.md) 
-  [02-springapplication-addlisteners.md](10-application-event-and-listener/02-springapplication-addlisteners.md) 
-  [03-registering-application-listener-via-spring-factory.md](10-application-event-and-listener/03-registering-application-listener-via-spring-factory.md) 

#### Externalized Configuration

-  [01-using-value-annotation-in-spring-boot.md](11-externalized-configuration/01-using-value-annotation-in-spring-boot.md) 

-  [02-load-application-property-file-from-current-dir.md](11-externalized-configuration/02-load-application-property-file-from-current-dir.md) 
-  [03-property-expansion-using-maven-resource-filter.md](11-externalized-configuration/03-property-expansion-using-maven-resource-filter.md) 
-  [04-using-placeholders-in-property-file.md](11-externalized-configuration/04-using-placeholders-in-property-file.md) 
-  [05-use-yaml-property-file.md](11-externalized-configuration/05-use-yaml-property-file.md) 
-  [06-type-safe-properties.md](11-externalized-configuration/06-type-safe-properties.md) 
-  [07-type-safe-binding-of-nestd-object.md](11-externalized-configuration/07-type-safe-binding-of-nestd-object.md) 
-  [08-diff-from-configuration-properties-value.md](11-externalized-configuration/08-diff-from-configuration-properties-value.md) 
-  [09-configuration-properties.md](11-externalized-configuration/09-configuration-properties.md) 
-  [10-configuration-properties-validation-by-jsr-303-349.md](11-externalized-configuration/10-configuration-properties-validation-by-jsr-303-349.md) 
-  [11-configuration-properties-cascaded-validation-by-valid.md](11-externalized-configuration/11-configuration-properties-cascaded-validation-by-valid.md) 
-  [12-generating-random-properties.md](11-externalized-configuration/12-generating-random-properties.md) 
-  [13-passing-json-properties-from-command-line.md](11-externalized-configuration/13-passing-json-properties-from-command-line.md) 
-  [14-binding-command-line-json-properties-using-configuration-properties.md](11-externalized-configuration/14-binding-command-line-json-properties-using-configuration-properties.md) 

#### profile

-  [01-setting-active-profile-in-external-properties.md](12-profile/01-setting-active-profile-in-external-properties.md) 
-  [02-add-active-profile-unconfitionally-by-using-spring-profile-include-property.md](12-profile/02-add-active-profile-unconfitionally-by-using-spring-profile-include-property.md) 
-  [03-programmatically-setting-profiles.md](12-profile/03-programmatically-setting-profiles.md) 
-  [04-profile-specific-properties.md](12-profile/04-profile-specific-properties.md) 
-  [05-profile-specific-properties-with-spring-profile-include.md](12-profile/05-profile-specific-properties-with-spring-profile-include.md) 
-  [06-selecting-profile-config-in-yml.md](12-profile/06-selecting-profile-config-in-yml.md) 

#### logging

-  [01-understanding-springboot-default-logging-config.md](13-logging/01-understanding-springboot-default-logging-config.md) 
-  [02-slf4j-with-log4j2-in-spring-boot.md](13-logging/02-slf4j-with-log4j2-in-spring-boot.md) 
-  [03-slf4j-jul-with-log4j-in-spring-boot.md](13-logging/03-slf4j-jul-with-log4j-in-spring-boot.md) 
-  [04-enabling-color-coded-output.md](13-logging/04-enabling-color-coded-output.md) 
-  [05-logging-system-abstraction-and-loging-config.md](13-logging/05-logging-system-abstraction-and-loging-config.md) 
-  [06-customizing-console-logging-format.md](13-logging/06-customizing-console-logging-format.md) 
-  [07-setting-log-file-by-using-logging-file-and-logging-path.md](13-logging/07-setting-log-file-by-using-logging-file-and-logging-path.md) 
-  [08-profile-specifi-logback-configuration-selection-in-logback-spring.md](13-logging/08-profile-specifi-logback-configuration-selection-in-logback-spring.md) 

#### JAX-RS Integration

- [01-integrating-jax-rs-jersey-in-springboot.md](14-jax-rs-intergration/01-integrating-jax-rs-jersey-in-springboot.md) 

#### actuator

-  [01-actuator-end-points.md](15-actuator/01-actuator-end-points.md) 
-  [02-accessing-actuator-jmx-mbean-endpoints.md](15-actuator/02-accessing-actuator-jmx-mbean-endpoints.md) 

#### Work with database

-  [01-in-menmory-h2-database.md](16-work-with-databases/01-in-menmory-h2-database.md) 
-  [02-h2-web-console.md](16-work-with-databases/02-h2-web-console.md) 
-  [03-connect-remote-production-database.md](16-work-with-databases/03-connect-remote-production-database.md) 

#### security

-  [01-security-getting-started.md](17-security/01-security-getting-started.md) 
-  [02-security-custom-configuration.md](17-security/02-security-custom-configuration.md) 
-  [03-jdbc-authentication.md](17-security/03-jdbc-authentication.md) 
-  [04-jdbc-authentcation-with-h2-console.md](17-security/04-jdbc-authentcation-with-h2-console.md) 

#### validation

-  [01-mvc-form-input-validation-jsr-303-349-380.md](18-validation/01-mvc-form-input-validation-jsr-303-349-380.md) 
-  [02-method-validation.md](18-validation/02-method-validation.md) 