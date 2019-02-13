# Deque

双向队列是一个支持从两端进行插入和移出操作的线性队列。由于同时实现了`Stack`和`Queue`队列，`Deque`是一个更加丰富的抽象数据类型，在`Deque`接口中，定义了插入，移出，检查方法。 [`ArrayDeque`](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html) 和[`LinkedList`](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html) implement the `Deque` interface.

注意，`Deque`接口可以作为后进先出（last-in-first-out）的栈和先进先出（ first-in-first-out ）的队列。

## Insert

 `addfirst` 和 `offerFirst`方法在`Deque`实例的开端插入元素.  `addLast` 和`offerLast` 方法从`Deque`实例的后端插入元素. 当`Deque`实例的容量被限制时，推荐使用 `offerFirst` 和`offerLast` 因为`addFirst` 在实例容量满时，抛出了一个 exception.

## Remove

 `removeFirst` 和`pollFirst`方法从双端队列实例的开头移除.`removeLast`和`pollLast`方法从从双端队列的终端移除元素，当`Deque`是空的时候，方法`pollFirst`和`pollLast`返回`null`，方法`removeFirst`和`removeLast`会抛出exception.

## Retrieve

`getFirst`方法和`peekFirst`方法会检索双向队列实例的第一个元素，但是，这些方法不会移除双向队列中的元素。同样，方法	`getLast`和	`peekLast`检索队列中的最后一个元素，当队列中没有元素时，`getFirst`和`getLast`会抛出异常，但是`peekFirst`和`peekLast`方法会返回null.

## 操作一览



| 操作类型    | 第一个元素                        | 最后一个元素                    |
| ----------- | --------------------------------- | ------------------------------- |
| **insert**  | `addFirst(e)`<br/>`offerFirst(e)` | `addLast(e)`<br/>`offerLast(e)` |
| **Remove**  | `removeFirst()`<br/>`pollFirst()` | `removeLast()`<br/>`pollLast()` |
| **Examine** | `getFirst()`<br/>`peekFirst()`    | `getLast()`<br/>`peekLast()`    |

除了一些基础的方法，如 insert、remove、和examine方法，`Deque`实例依然有多个重新定义的方法，例如其中一个`removeFirstOccurence`这个方法移出第一次出现指定元素，如果不存在，将不做更改，`removeLastOccurence`方法移除了最后一次出现的指定元素，返回值类型为boolean，如果指定元素存在，那么会返回`true`