# Java Multi-threading Evolution and Topics

## JDK release-wise multi-threading concepts

As per [**JDK 1.x release**](https://www.cs.princeton.edu/courses/archive/fall97/cs461/jdkdocs/), there were only few classes present in this initial release. To be very specific, there classes/interfaces were:

- `java.lang.Thread`
- `java.lang.ThreadGroup`
- `java.lang.Runnable`
- `java.lang.Process`
- `java.lang.ThreadDeath`
- and some exception classes

e.g.

1. `java.lang.IllegalMonitorStateException`
2. `java.lang.IllegalStateException`
3. `java.lang.IllegalThreadStateException`.

It also had few synchronized collections e.g. `java.util.Hashtable`.

**JDK 1.2** and **JDK 1.3** had no noticeable changes related to multi-threading. (Correct me if I have missed anything).

**JDK 1.4**, there were few JVM level changes to suspend/resume multiple threads with single call. But no major API changes were present.

[**JDK 1.5**](https://docs.oracle.com/javase/1.5.0/docs/guide/concurrency/overview.html) was first big release after JDK 1.x; and it had included multiple concurrency utilities. `Executor`, `semaphore`, `mutex`, `barrier`, `latches`, `concurrent collections` and `blocking queues`; all were included in this release itself. The biggest change in java multi-threading applications cloud happened in this release.

> Read full set of changes in this link: [http://docs.oracle.com/javase/1.5.0/docs/guide/concurrency/overview.html](https://docs.oracle.com/javase/1.5.0/docs/guide/concurrency/overview.html)

**JDK 1.6** was more of platform fixes than API upgrades. So new change was present in JDK 1.6.

[**JDK 1.7**](https://docs.oracle.com/javase/7/docs/technotes/guides/concurrency/changes7.html) added support for `ForkJoinPool` which implemented **work-stealing technique** to maximize the throughput. Also `Phaser` class was added.

[**JDK 1.8**](https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/changes8.html) is largely known for Lambda changes, but it also had few concurrency changes as well. Two new interfaces and four new classes were added in **java.util.concurrent** package e.g. `CompletableFuture` and `CompletionException`.

The Collections Framework has undergone a major revision in Java 8 to add aggregate operations based on the newly added **streams facility** and **lambda expressions**; resulting in large number of methods added in almost all `Collection` classes, and thus in concurrent collections as well.

> Read full set of changes in this link: [http://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/changes8.html](https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/changes8.html)