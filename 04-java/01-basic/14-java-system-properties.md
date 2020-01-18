# Java System Properties

>  By Lokesh Gupta | Filed Under: [Java Basics](https://howtodoinjava.com/java/basics/)

Java maintains a set of system properties for its operations. Each **java system property** is a key-value (String-String) pair such as “**java.version”=”1.7.0_09**“. You can retrieve all the system properties via `System.getProperties()` or you can also retrieve individual property via `System.getProperty(key)`.

Please note that Access to [system properties](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html) can be restricted by the Java security manager and policy file. By default, Java programs have unrestricted access to all the system properties.

## Important Java System Properties

1. #### JRE related system properties

   | system properties      | Values                                                       |
   | ---------------------- | ------------------------------------------------------------ |
   | `java.home`            | JRE home directory, e.g., “`C:\Program Files\Java\jdk1.7.0_09\jre`“. |
   | `java.library.path`    | JRE library search path for search native libraries. It is usually but not necessarily taken from the environment variable PATH. |
   | `java.class.path`      | JRE classpath e.g., `'.'` (dot – used for current working directory). |
   | `java.ext.dirs`        | JRE extension library path(s), e.g, “`C:\Program Files\Java\jdk1.7.0_09\jre\lib\ext;C:\Windows\Sun\Java\lib\ext`“. |
   | `java.version`         | JDK version, e.g., `1.7.0_09`.                               |
   | `java.runtime.version` | JRE version, e.g. `1.7.0_09-b05`.                            |

2. #### File related system properties

   | System properties | values                                                       |
   | ----------------- | ------------------------------------------------------------ |
   | `file.separator`  | symbol for file directory separator such as `'d:\test\test.java'`. The default is `'\'` for windows or `'/'` for Unix/Mac. |
   | `path.separator`  | symbol for separating path entries, e.g., in `PATH` or `CLASSPATH`. The default is `';'` for windows or `':'` for Unix/Mac. |
   | `line.separator`  | symbol for end-of-line (or new line). The default is `"\r\n"` for windows or `"\n"` for Unix/Mac OS X. |

3. #### User related system properties

   | system properies | Desc                                  |
   | ---------------- | ------------------------------------- |
   | `user.name`      | the user’s name.                      |
   | `user.home`      | the user’s home directory.            |
   | `user.dir`       | the user’s current working directory. |

4. #### OS related system properties

   | system properties | Desc                                  |
   | ----------------- | ------------------------------------- |
   | `os.name`         | the OS’s name, e.g., “`Windows 7`“.   |
   | `os.version`      | the OS’s version, e.g., “`6.1`“.      |
   | `os.arch`         | the OS’s architecture, e.g., “`x86`“. |

## Get System Property

As discussed earlier, You can get all the system properties via `System.getProperties()` or also retrieve individual property via `System.getProperty(key)`.

```java
import java.util.Properties;
public class PrintSystemProperties 
{
   public static void main(String[] a) 
   {
      // List all System properties
      Properties pros = System.getProperties();
      pros.list(System.out);
  
      // Get a particular System property given its key
      // Return the property value or null
      System.out.println(System.getProperty("java.home"));
      System.out.println(System.getProperty("java.library.path"));
      System.out.println(System.getProperty("java.ext.dirs"));
      System.out.println(System.getProperty("java.class.path"));
   }
}
```



## Set System Property

In java, you can set a custom system property either from command tools or from java code itself.

1. #### Set system property from command line (“-D” option)

   `java -Dcustom_key=``"custom_value"` `application_launcher_class`

2. #### Set system property from code using System.setProperty() method

   `System.setProperty(``"custom_key"``, ``"custom_value"``);`