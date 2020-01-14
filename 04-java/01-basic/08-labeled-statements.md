# Labeled Statements in Java

>  By Lokesh Gupta | Filed Under: [Java Basics](https://howtodoinjava.com/java/basics/)

Java [labeled blocks](https://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.15) are *logically* similar to to `goto` statements in C/C++.

> A label is any valid java identifier followed by a colon. e.g. outer:, inner:, inner123:, inner_: etc.

## 1. Labeled statement in String class

How many times, we have been told that ***“goto” statements are evil***. I myself have read about this so called evil through many respected authors of our time. But, if you look at the sourcecode of [String.java](http://www.docjar.com/html/api/java/lang/String.java.html), and read the sourcecode of ***public String toLowerCase(Locale locale)*** method, you will something like this:

```
scan :
    for (firstUpper = 0 ; firstUpper &lt; count; ) 
    {
        char c = value[offset+firstUpper];
        if ((c >= Character.MIN_HIGH_SURROGATE) &amp;&amp;
            (c <= Character.MAX_HIGH_SURROGATE)) {
            int supplChar = codePointAt(firstUpper);
            if (supplChar != Character.toLowerCase(supplChar)) {
                break scan;
            }
            firstUpper += Character.charCount(supplChar);
        } else {
            if (c != Character.toLowerCase(c)) {
                break scan;
            }
            firstUpper++;
        }
    }
    return this;
}
```

What is this “scan:”. This is the labeled block which we are going to learn about today. Well, they always told us not to use them and used it in perhaps most used class in JDK distribution. 🙂

## 2. Labeled statement with ‘break’ and ‘continue’ keywords

In Java, we all know for what purpose the keywords “break” and “continue” exist. Basically, statements `break` and `continue` alter the normal control flow of compound statements.

#### 2.1. break keyword with and without labeled statement

```java
while (Some condition) 
{
  if ( a specific condition ) 
        break;        //Default usage
  else
        normal business goes here..
}
```

Another way is to use `break` with a labeled statement is.

```java
hackit:
while (Some condition) 
{
  if ( a specific condition ) 
        break hackit;       //Usage with label
  else
        normal business goes here..
}
```

Whenever during a program execution, a labeled break statement is encountered that control immediately goes out of enclosing labeled block.

Similarly, labeled continue will bring control back to start. Just like in normal break and continue statements, with additional names given to blocks.

#### 2.2. More examples

Let’s look at more example usages:

```java
outer: for (int i = 0; i &lt; 10; i++) {
  inner: for (int j = 10; j > 0; j--) {
    if (i != j) {
      System.out.println(i);
      break outer;
    }else{
      System.out.println("-->>" + i);
      continue inner;
    }
  }
}
```

```java
int a = 10;
int b = 12;
 
block1: {
    if (a &lt; 0) {
      break block1;
    }
    if (b &lt; 0) {
      break block1;
    }
    System.out.println( a + b );
  }
}
```

## 3. Summary

- Java does not have a general **goto** statement.
- The statements `break` and `continue` in Java alter the normal control flow of compound statements. They can use labels which are valid java identifiers with a colon.
- Labeled blocks can only be used with break and continue statements.
- They must be called within its scope. You can not refer them scope of labeled block.
- The break statement immediately jumps to the end (and out) of the appropriate compound statement.
- The continue statement immediately jumps to the next iteration (if any) of the appropriate loop.
- A continue statement does not apply to a switch statement or a block statement, only to compound statements that loop: for, while, and do.