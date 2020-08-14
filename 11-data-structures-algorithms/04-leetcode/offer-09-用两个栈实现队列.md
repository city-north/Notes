# offer-09-用两个栈实现队列

https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/

## 搓代码

```java
class CQueue {
    Stack<Integer> pushStack;
    Stack<Integer> popStack;

    public CQueue() {
        popStack = new Stack<Integer>();
        pushStack = new Stack<Integer>();

    }

    public void appendTail(int value) {
        pushStack.push(value);
    }

    public int deleteHead() {
        if (!pushStack.isEmpty()) {
            while (!pushStack.isEmpty()) {
                popStack.push(pushStack.pop());
            }
        }
      if(popStack)
        return popStack.pop();
    }
}
```

## 优化

```java
class CQueue {
    Stack<Integer> pushStack;
    Stack<Integer> popStack;

    public CQueue() {
        popStack = new Stack<Integer>();
        pushStack = new Stack<Integer>();

    }

    public void appendTail(int value) {
        pushStack.push(value);
    }

    public int deleteHead() {
        if (popStack.isEmpty()) {
            while (!pushStack.isEmpty()) {
                popStack.push(pushStack.pop());
            }
        }
        if (popStack.isEmpty()) {
            return -1;
        } else {
            return popStack.pop();
        }
    }
}
```

