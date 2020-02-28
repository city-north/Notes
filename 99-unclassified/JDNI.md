# JNDI

Java Naming and Directory InterfaceTM

Java 命名与目录接口

## What is JNDI ?

Naming and directory services play a vital role in intranets and the Internet by providing network-wide sharing of a variety of information about users, machines, networks, services, and applications.

JNDI is an API specified in Java technology that provides naming and directory functionality to applications written in the Java programming language. It is designed especially for the Java platform using Java's object model. Using JNDI, applications based on Java technology can store and retrieve named Java objects of any type. In addition, JNDI provides methods for performing standard directory operations, such as associating attributes with objects and searching for objects using their attributes.

JNDI is also defined independent of any specific naming or directory service implementation. It enables applications to access different, possibly multiple, naming and directory services using a common API. Different naming and directory service providers can be plugged in seamlessly behind this common API. This enables Java technology-based applications to take advantage of information in a variety of existing naming and directory services, such as LDAP, NDS, DNS, and NIS(YP), as well as enabling the applications to coexist with legacy software and systems.

Using JNDI as a tool, you can build new powerful and portable applications that not only take advantage of Java's object model but are also well-integrated with the environment in which they are deployed.

### What is its basic use?

JNDI allows distributed applications to look up services in an abstract, resource-independent way.

### When it is used?

The most common use case is to set up a database connection pool on a Java EE application server. Any application that's deployed on that server can gain access to the connections they need using the JNDI name `java:comp/env/FooBarPool` without having to know the details about the connection.

his has several advantages:

1. If you have a deployment sequence where apps move from `devl->int->test->prod` environments, you can use the same JNDI name in each environment and hide the actual database being used. Applications don't have to change as they migrate between environments.
2. You can minimize the number of folks who need to know the credentials for accessing a production database. Only the Java EE app server needs to know if you use JNDI.