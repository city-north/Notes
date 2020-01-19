## 1. What Is an Operator?

An operator is a **symbol that performs a specific kind of operation** on one, two, or three operands, and produces a result. The type of the operator and its operands determines the kind of operation performed on the operands and the type of the result produced.

| Operators            | Precedence                               |
| -------------------- | ---------------------------------------- |
| postfix              | `*expr*++ *expr*--`                      |
| unary                | `++*expr* --*expr* +*expr* -*expr* ~ !`  |
| multiplicative       | `* / %`                                  |
| additive             | `+ -`                                    |
| shift                | `<< >> >>>`                              |
| relational           | `< > <= >= instanceof`                   |
| equality             | `== !=`                                  |
| bitwise AND          | `&`                                      |
| bitwise exclusive OR | `^`                                      |
| bitwise inclusive OR | `|`                                      |
| logical AND          | `&&`                                     |
| logical OR           | `||`                                     |
| ternary              | `? :`                                    |
| assignment           | `= += -= *= /= %= &= ^= |= <<= >>= >>>=` |

#### 1.1. Classification of Java Operators

Operators in Java can be categorized based on two criteria:

- **Number of operands** – There are three types of operators based on the number of operands. An operator is called a unary, binary, or ternary operator based on the number of operands. If an operator takes one operand, it called a **unary operator**; if it takes two operands, it called a **binary operator**; if it takes three operands, it called a **ternary operator**.
- **Type of operation they perform** – An operator is called an **arithmetic operator**, a **relational operator**, a **logical operator**, or a **bitwise operator**, depending on the kind of operation it performs on its operands.

## 2. Assignment Operator (=)

> 赋值运算符

- An assignment operator (=) is used to assign a value to a variable.
- It is a binary operator. It takes two operands.
- The value of the right-hand operand is assigned to the left-hand operand.
- The left-hand operand must be a variable.

```java
//26 is the right-hand operand. 
//counter is the left-hand operand, which is a variable of type int.
 
int counter = 26; 
```

## 3. Arithmetic Operators 

> 算数运算符

- Operators like (**+** (plus), **–** (minus), ***** (multiply), **/** (divide)) are called arithmetic operators in Java.
- It can only be used with numeric type operands. It means, both operands to arithmetic operators must be one of types `byte`, `short`, `char`, `int`, `long`, `float`, and `double`.
- These operators cannot have operands of `boolean` primitive type and reference type.

```
int sum = 10 + 20; 
 
int difference = 50 - 20; 
 
long area = 20l * 30l;    
 
int percentage = 20 / 100;
```

#### 3.1. Unary Arithmetic Operators

> 一元运算符

| **OPERATOR** | **DESCRIPTION**                                              |
| ------------ | ------------------------------------------------------------ |
| `'+'`        | **Unary plus operator**; indicates positive value (numbers are positive without this, however) |
| `'-'`        | **Unary minus operator**; negates an expression value        |
| `'++'`       | **Increment operator**; increments a value by 1              |
| `'--'`       | **Decrement operator**; decrements a value by 1              |
| `'!'`        | **Logical complement operator**; inverts the value of a boolean |

#### 3.2. Binary Arithmetic Operators

> 二元运算符

| **OPERATOR** | **DESCRIPTION**                                              |
| ------------ | ------------------------------------------------------------ |
| `'+'`        | **Addition** – Adds values on either side of the operator    |
| `'-'`        | **Subtraction** – Subtracts right hand operand from left hand operand |
| `'*'`        | **Multiplication** – Multiplies values on either side of the operator |
| `'/'`        | **Division** – Divides left hand operand by right hand operand |
| `'%'`        | **Modulus** – Divides left hand operand by right hand operand and returns remainder |

## 4. String Concatenation Operator (+)

The `'+'` operator is overloaded in Java. An operator is said to be **overloaded operator** if it is used to perform more than one function.

#### 4.1. Concatenate two strings

So far, you have seen its use as an arithmetic addition operator to add two numbers. It can also be used to **concatenate two strings**.

```java
String str1 = "Hello";
String str2 = " World";
 
String str3 = str1 + str2;      // Assigns "Hello World" to str3
```

#### 3.2. Concatenate primitive to string

The string concatenation operator is also used to concatenate a primitive and a reference data type value to a string.

```
int num = 26;
 
String str1 = "Alphabets";
 
String str2 = num + str1;    // Assigns "26Alphabets" to str2
```

#### 4.2. Concatenate null

If a reference variable contains the ‘null’ reference, the concatenation operator uses a string “null”.

```
String str1 = "I am ";
 
String str2 = null;
 
String str3 = str1 + str2;    // Assigns "I am null" to str3
```

## 5. Relational Operators

- All relational operators are binary operators.
- They take two operands.
- The result produced by a relational operator is always a Boolean value `true` or `false`.
- They are mostly used in Java control statements such as [if statements](https://howtodoinjava.com/java/basics/if-else-statement-in-java/), [while statements](https://howtodoinjava.com/java/basics/while-loop-in-java/) etc.

Let’s see below all available relational operators in java.

| **OPERATOR** | **DESCRIPTION**                                              |
| ------------ | ------------------------------------------------------------ |
| `'=='`       | **Equals to** – Checks if the values of two operands are equal or not, if yes then condition becomes true. |
| `'!='`       | **Not equals to** – Checks if the values of two operands are equal or not, if values are not equal then condition becomes true. |
| `'>'`        | **Greater than** – Checks if the value of left operand is greater than the value of right operand, if yes then condition becomes true. |
| `'<'`        | **Less than** – Checks if the value of left operand is less than the value of right operand, if yes then condition becomes true. |
| `'>='`       | **Greater than or equals to** – Checks if the value of left operand is greater than or equal to the value of right operand, if yes then condition becomes true. |
| `'<='`       | **Less than or equals to** – Checks if the value of left operand is less than or equal to the value of right operand, if yes then condition becomes true. |

```java
int result = 20; 
         
if( result > 10) {                  //true
    //some operation
}
 
boolean isEqual = ( 10 == 20 );     //false
```

## 6. Boolean Logical Operators

- All Boolean logical operators can be used only with boolean operand(s).
- They are mostly used in control statements to compare two (or more) conditions.

| **OPERATOR** | **DESCRIPTION**                                              |
| ------------ | ------------------------------------------------------------ |
| `'!'`        | returns true if the operand is false, and false if the operand is true. |
| `'&&'`       | returns true if both operands are true. If either operand is false, it returns false. |
| `'&'`        | returns true if both operands are true. If either operand is false, it returns false. |
| `'||'`       | returns true if either operand is true. If both operands are false, it returns false. |
| `'|'`        | returns true if either operand is true. If both operands are false, it returns false. |
| `'^'`        | it returns true if one of the operands is true, but not both. If both operands are the same, it returns false. |
| `'&=;'`      | if both operands evaluate to true, &= returns true. Otherwise, it returns false. |
| `'|='`       | if either operand evaluates to true, != returns true. Otherwise, it returns false. |
| `'^='`       | if both operands evaluate to different values, that is, one of the operands is true but not both, ^= returns true. Otherwise, it returns false. |

```java
int result = 20; 
         
if( result > 10 && result < 30) {      
    //some operation
}
 
if( result > 10 || result < 30) {      
    //some operation
}
```

1. The **logical AND operator** (&) works the same way as the logical short-circuit AND operator (&&), except for one difference. The logical AND operator (&) evaluates its right-hand operand even if its left-hand operand evaluates to false.
2. The **logical OR operator** works the same way as the logical short-circuit OR operator, except for one difference. The logical OR operator evaluates its right-hand operand even if its left-hand operand evaluates to true.

## 7. Bitwise Operators

A bitwise operator **manipulates individual bits** of its operands. Java defines several bitwise operators, which can be applied to the integer types, long, int, short, char, and byte.

| **OPERATOR** | **DESCRIPTION**                                              |
| ------------ | ------------------------------------------------------------ |
| `'&'`        | **Binary AND Operator** copies a bit to the result if it exists in both operands. |
| `'|'`        | **Binary OR Operator** copies a bit if it exists in either operand. |
| `'^'`        | **Binary XOR Operator** copies the bit if it is set in one operand but not both. |
| `'~'`        | **Binary Ones Complement Operator** is unary and has the effect of ‘flipping’ bits. |
| `<<`         | **Binary Left Shift Operator**. The left operands value is moved left by the number of bits specified by the right operand. |
| `>>`         | **Binary Right Shift Operator**. The left operands value is moved right by the number of bits specified by the right operand. |
| `>>>`        | **Shift right zero fill operator**. The left operands value is moved right by the number of bits specified by the right operand and shifted values are filled up with zeros. |

## 8. Ternary Operator 

> 三目表达式

- Java has one conditional operator. It is called a ternary operator as it takes **three operands**.

- The two symbols of “?” and “:” make the ternary operator.

- If the boolean-expression evaluates to true, it evaluates the true-expression; otherwise, it evaluates false-expression.

#### 8.1. Syntax

```java
boolean-expression ? true-expression : false-expression
```

#### 8.2. Ternary Operator Example

```java
int number1 = 40;
int number2 = 20;
 
int biggerNumber = (number1 > number2) ? number1 : number2;
 
//Compares both numbers and return which one is bigger
```

## 9. Java Operator Precedence Table

Java has well-defined rules for specifying the order in which the operators in an expression are evaluated when the expression has several operators. For example, multiplication and division have a higher precedence than addition and subtraction.

> Precedence rules can be overridden by explicit parentheses.

When two operators share an operand the operator with the higher precedence goes first. For example, `1 + 2 * 3` is treated as `1 + (2 * 3)` because precedence of multiplication is higher than addition.

In above expression, if you want to add values first then use explicit parentheses like this – `(1 + 2) * 3`.

| PRECEDENCE | OPERATOR                | TYPE                                                         | ASSOCIATIVITY |
| ---------- | ----------------------- | ------------------------------------------------------------ | ------------- |
| 15         | () [] ·                 | Parentheses Array subscript Member selection                 | Left to Right |
| 14         | ++ —                    | Unary post-increment Unary post-decrement                    | Right to left |
| 13         | ++ — + – ! ~ ( *type* ) | Unary pre-increment Unary pre-decrement Unary plus Unary minus Unary logical negation Unary bitwise complement Unary type cast | Right to left |
| 12         | * / %                   | Multiplication Division Modulus                              | Left to right |
| 11         | + –                     | Addition Subtraction                                         | Left to right |
| 10         | << >> >>>               | Bitwise left shift Bitwise right shift with sign extension Bitwise right shift with zero extension | Left to right |
| 9          | < <= > >= instanceof    | Relational less than Relational less than or equal Relational greater than Relational greater than or equal Type comparison (objects only) | Left to right |
| 8          | == !=                   | Relational is equal to Relational is not equal to            | Left to right |
| 7          | &                       | Bitwise AND                                                  | Left to right |
| 6          | ^                       | Bitwise exclusive OR                                         | Left to right |
| 5          | \|                      | Bitwise inclusive OR                                         | Left to right |
| 4          | &&                      | Logical AND                                                  | Left to right |
| 3          | \|\|                    | Logical OR                                                   | Left to right |
| 2          | ? :                     | Ternary conditional                                          | Right to left |
| 1          | = += -= *= /= %=        | Assignment Addition assignment Subtraction assignment Multiplication assignment Division assignment Modulus assignment | Right to left |

# Summary of Operators

The following quick reference summarizes the operators supported by the Java programming language.

## Simple Assignment Operator

```
=       Simple assignment operator
```

## Arithmetic Operators

```
+       Additive operator (also used for String concatenation)
-       Subtraction operator
*       Multiplication operator
/       Division operator
%       Remainder operator
```

## Unary Operators

```
+       Unary plus operator; indicates positive value (numbers are positive without this, however)
-       Unary minus operator; negates an expression
++      Increment operator; increments a value by 1
--      Decrement operator; decrements a value by 1
!       Logical complement operator;inverts the value of a boolean
```

## Equality and Relational Operators

```
==      Equal to
!=      Not equal to
>       Greater than
>=      Greater than or equal to
<       Less than
<=      Less than or equal to
```

## Conditional Operators

```
&&      Conditional-AND
||      Conditional-OR
?:      Ternary (shorthand for if-then-else statement)
```

## Type Comparison Operator

```
instanceof      Compares an object to a specified type 
```

## Bitwise and Bit Shift Operators

```
~       Unary bitwise complement
<<      Signed left shift
>>      Signed right shift
>>>     Unsigned right shift
&       Bitwise AND
^       Bitwise exclusive OR
|       Bitwise inclusive OR
```