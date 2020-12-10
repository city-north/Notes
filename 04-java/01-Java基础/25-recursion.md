# A Guide to Recursion in Java

**Recursion** is referred to a programming style where a **method invokes itself** repeatedly until a certain predefined condition is met. Such method calls are also called **recursive methods**.

## 1. Syntax of recursive methods

A recursion based method MUST two basic components to solve a problem correctly.

1. recursive method call
2. a precondition which break the recursion

Please keep in mind that if precondition is not reachable or not defined, then stack overflow problem will happen.

```java
method(T parameters...) 
{
    if(precondition == true)      //precondition
    {
        return result;
    }
 
    return method(T parameters...);      //recursive call
}
```

## 2. Recursion Types

Recursion are of two types based on when the recursive method call is made.

#### 1. Tail recursion

A recursive method is **tail recursive** when recursive method call is the last statement executed inside the method (usually along with a **return statement**).

```java
method(T parameters...) 
{
    if(precondition == true)      
    {
        return result;
    }
 
    return method(T parameters...);     
}
```

#### 2. Head recursion

Any recursion which is not tail recursion, can be referred as head recursion.

```java
method(T parameters...) 
{
    if(some condition)      
    {
        return method(T parameters...);
    }
 
    return result;     
}
```

## 3. Java recursion examples

#### 3.1. Fibonacci Series

Fibonacci series is a sequence of numbers where each number is defined as the sum of the two numbers proceeding it.

For example – 1, 1, 2, 3, 5, 8, 13, 21, 34 and so on…

This function gives us the Fibonacci number at nth position starting from 1.

```java
public int fibonacci(int n) 
{
    if (n <= 1) {
        return n;
    }
    return fibonacci(n-1) + fibonacci(n-2);
}
```

Let’s test this function to print the fibonacci series upto n = 10;

```java
public static void main(String[] args) 
{
  int number = 10;
 
  for (int i = 1; i <= number; i++) 
  {
    System.out.print(fibonacci(i) + " ");
  }
 
}
```

Program output.

```
1 1 2 3 5 8 13 21 34 55
```

#### 3.2. Fibonacci Series

The *greatest common divisor* (gcd) of two positive integers is the largest integer that divides evenly into both of them.

```java
public static int gcd(int p, int q) {
    if (q == 0) return p;
    else return gcd(q, p % q);
}
```

Let’s test this function to print the fibonacci series upto n = 10;

```java
public static void main(String[] args) 
{
  int number1 = 40;
  int number2 = 500;
 
  System.out.print( gcd(number1, number2) );
}
```