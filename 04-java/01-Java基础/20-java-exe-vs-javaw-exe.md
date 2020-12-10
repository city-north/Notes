# Java.exe vs javaw.exe

**“java.exe” and “javaw.exe”**, both are Java executables on the Windows platform. These files are nearly identical versions of the Java Application Launcher utility. Both versions of the launcher take the same arguments and options. The launcher is invoked with “java” or “javaw” followed by launcher options, the class or Java archive (JAR) file name and application arguments.

## javaw.exe

This `non-console version` of the application launcher is used to launch java applications usually with graphical user interfaces (GUIs). These applications have windows with menus, buttons and other interactive elements. Essentially, Use javaw.exe when you don’t want a command prompt window to appear either to take further input or showing output.

> The javaw.exe launcher will, however, display a dialog box with error information if a launch of java application fails for some reason.

## java.exe

java.exe is very similar to javaw.exe. The `console version` of the launcher is used for applications with text-based interfaces or that output text. Any application launched with “java” will cause the command-line waits for the application response till it closes.

When launched using javaw, the application launches and the command line exits immediately and ready for next command.

That’s only noticeable **difference between java.exe and javaw.exe**. If you know any other noticeable differences, please share wit all of us.