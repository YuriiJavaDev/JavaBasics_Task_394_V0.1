## Auto-generation: equals, hashCode, toString.

### 1. Automatic generation of equals, hashCode, toString

#### Why are these methods needed?

When working with objects in Java, you quickly run into the same problems. Sometimes you need to check if two objects are equal. For example, to see if it already exists in a collection like Set or Map. In other cases, an object is used as a key in a HashMap, and special comparison rules are essential. And almost always, you want to print the object in a log or on the screen so that the output isn't just gibberish like MyClass@7b23ec81, but something meaningful.

For these cases, every class in Java has three special methods:

- equals(Object o) is responsible for checking equality.
- hashCode() gives the object a numeric "fingerprint," which is needed by collections like hashtables. - **toString()** returns a convenient string representation of the object, which greatly simplifies debugging and printing.

#### Why is this a pain in regular classes?

In regular classes, these methods have to be written manually. And that's where the tedium and headache begin. You end up with a bunch of boilerplate code that only clutters the class. It's very easy to make a mistake: forget to compare a field, miscalculate the **hashCode**, and then catch mysterious bugs. And if you add a new field to the class, you have to go through all these methods again and rewrite everything.

**Example of a Regular Class**

```java
public class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() {
        return x;
    }
    public int y() {
        return y;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

Look familiar? Yes, and that's only for two fields! What if there are twenty?

#### How record does it

The Record class does all this for you. Simply declare:

```java
public record Point(int x, int y) {
}
```

And Java will automatically generate:

- Constructor
- Getters (**x(), y()**)
- **equals, hashCode, toString**

**Automatically generated methods**

- **equals** compares all components of a record by value.
- **hashCode** is calculated over all components.
- **toString** returns a string like **Point[x=1, y=2]**.

#### Let's see it live!

```java
public record Point(int x, int y) {
}

public class Demo {
    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        
        System.out.println(p1.equals(p2)); // true
        System.out.println(p1.hashCode() == p2.hashCode()); // true
        System.out.println(p1); // Point[x=1, y=2]
    }
}
```

**Conclusion:**
```
true
true
Point[x=1, y=2]
```
Everything works as expected, without a single extra line of code!

### 2. Why this is important: collections, debugging, and security

#### Correct work with collections

Imagine using objects as keys in a HashMap or elements in a HashSet. If equals and hashCode are implemented incorrectly, collections will behave strangely: they won't find the element you just added, or, conversely, they'll consider two different objects to be the same.

With record classes, you can be sure that comparison and hashing always consider all components of a record (in the order they are declared).

**Example: using a record as a key**

```java
import java.util.HashMap;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        record Point(int x, int y) {
        }
        
        Map<Point, String> map = new HashMap<>();
        Point p1 = new Point(3, 4);
        map.put(p1, "Hello!");
        
        Point p2 = new Point(3, 4);
        System.out.println(map.get(p2)); // "Hello!" — works!
    }
}
```

Note: **p1** and **p2** are different objects (different references), but they contain the same field values, so they are considered equal. We'll talk more about **Map** and **HashMap** later.

#### Ease of debugging and logging

Instead of the dull **Point@1a2b3c4d** (as is the default for regular classes), the record class is printed beautifully and informatively:

**Point[x=3, y=4]**

This saves a lot of time when debugging and logging.

### 3. How equals, hashCode, and toString work inside record

#### The equals method

- **How the method works**

**📌 By default (from `Object`)**

```java
a.equals(b)
```

👉 Compares **references (memory addresses)**

That is:

```java
    new Student("Alex", 20).equals(new Student("Alex", 20))
```

❌ will be `false`

---

**📌 If equals() is NOT overridden**

👉 Behavior:

> Comparison as `==` (references)

---

**📌 If equals is overridden**

For example:

```java
class Student {
    String name;
    int age;
    
    @Override
    public boolean equals(Object o) {
        Student s = (Student) o;
        return name.equals(s.name) && age == s.age;
    }
}
```

---

👉 then:

```java
    new Student("Alex", 20).equals(new Student("Alex", 20))
```

✔️ will be `true`

The Record class implements **equals** so that two objects are considered equal if:

- They are the same type (the same record class)
- All their components are equal (**==** for primitives, **equals()** for objects)

**Comparison example**

```java
Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);
Point p3 = new Point(1, 3);

System.out.println(p1.equals(p2)); // true
System.out.println(p1.equals(p3)); // false
```

#### The hashCode Method

The hash code is calculated over all components of a record, typically using the standard **Objects.hash(...)** method.

```java
System.out.println(p1.hashCode()); // For example, 994
System.out.println(p2.hashCode()); // Also 994
System.out.println(p3.hashCode()); // Another number
```

#### The toString Method

The string representation is always in the format:

```
ClassName[field1=value1, field2=value2, ...]
```

```java
System.out.println(p1); // Point[x=1, y=2]
```

### 4. Overriding equals, hashCode, toString: When and How?

Sometimes (rarely, but it happens) you need to change the default behavior of these methods. For example, you might want **toString** to return a string in a different format, or want comparisons to only occur on some fields.

**Warning:** If you override **equals/hashCode**, do so very carefully! Violating their "contract" can lead to bugs that are difficult to catch.

#### How to override a method

Simply declare your method inside the body of the record class:

```java
public record Point(int x, int y) {
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
}
```

```java
    Point p = new Point(3, 5);
    System.out.println(p); // (3; 5)
```

#### Is it possible to override equals/hashCode?

Yes, but it's highly discouraged unless you're sure what you're doing. For example, if you want to only compare on the **x** field (which is weird):

```java
public record Point(int x, int y) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point other)) return false;
        return x == other.x;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(x);
    }
}
```

```java
    Point p1 = new Point(1, 2);
    Point p2 = new Point(1, 999);
    
    System.out.println(p1.equals(p2)); // true (!)
```

But be careful: if you override **equals**, always override **hashCode** as well—otherwise, collections will behave incorrectly.

**Best practice**

- If you're unsure why you're overriding it, don't!
- For **toString**, feel free to create your own format if you want.
- For **equals/hashCode**, only if you have a compelling reason and understand the consequences.

**🧠 REMINDER: `record.equals()`**

**🔹 1. Basic rule**

```
record.equals() → compares ALL fields using equals()
```

👉 essentially:

```
Objects.equals(field1,other.field1)&&
Objects.equals(field2,other.field2)
```

---

**📌 🔹 2. If the field is a primitive**

```
int,double
```

✔️ compared by value

👉 everything's ok

---

**📌 🔹 3. If the field is an object**

**✔️ There's `equals()`**

```
String, another record, a normal class with equals
```

👉 Compare by contents

✔️ Result **expected**

---

**❌ No `equals()`**

```java
new Person("Alex")
```

👉 Compare by reference

```java
new Person("Alex") != new Person("Alex")
```

❌ equals → **false**

---

**📌 🔹 4. If the field is an array**

```
String[]
```

👉 ALWAYS:

❌ Compare by reference

```java
new String[]{"a"} != new String[]{"a"}
```

---

#### ❗ even if the internals are the same:

```
["a"] vs ["a"] → false
```

- **How to solve the problem**

**📌 Case: record contains only an array**

```java
record MyData(String[] arr) {}
```

---

**❌ Problem (default)**

```java
MyData d1 = new MyData(new String[]{"a", "b"});
MyData d2 = new MyData(new String[]{"a", "b"});

System.out.println(d1.equals(d2));
```

👉 Result:

```
false
```

---

**📌 Why?**

Record does this:

```
arr.equals(other.arr)
```

And for an array:

```
equals() → compare references
```

👉 These are two different arrays in memory → ❌ false

---

**🔍 Visual**

```java
String[] a1 = {"a", "b"};
String[] a2 = {"a", "b"};

System.out.println(a1.equals(a2)); // false
```

---

**✅ How to compare arrays correctly**

```java
Arrays.equals(a1,a2)// true
```

---

**📌 How to fix record**

You need to **override equals manually**

```java
import java.util.Arrays;

record MyData(String[] arr) {
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyData other)) return false;
        
        return Arrays.equals(this.arr, other.arr);
    }
}
```

---

**✅ Now it works**

```java
MyData d1 = new MyData(new String[]{"a", "b"});
MyData d2 = new MyData(new String[]{"a", "b"});

System.out.println(d1.equals(d2)); // true
```

---

**⚠️ But there's one more thing (important)**

👉 You need to override `hashCode` as well

```java
@Override
public int hashCode() {
    return Arrays.hashCode(arr);
}
```

---

**💡 A better solution (in real life)**

👉 Don't use an array

```java
record MyData(List<String>arr) {}
```

---

**✔️ Then:**

```java
List<String> l1 = List.of("a", "b");
List<String> l2 = List.of("a", "b");

System.out.println(l1.equals(l2)); // true
```

👉 and record will work without any hacks

---

### 📌 🔹 5. Combined case (most important)

```
recordR(String s,Person p,String[]arr)
```

| field | result |
| --- | --- |
| String | ✔️ ok |
| Person without equals | ❌ breaks equals |
| array | ❌ breaks equals |

👉 result: `equals` = false

---

### 5. Practice: Comparing Objects and Using Records in Collections

#### Example: Comparing Two Record Objects

```java
public record User(String name, int age) {
}

public class Demo {
    public static void main(String[] args) {
        User u1 = new User("Alice", 20);
        User u2 = new User("Alice", 20);
        User u3 = new User("Bob", 25);
        
        System.out.println(u1.equals(u2)); // true
        System.out.println(u1.equals(u3)); // false
        
        System.out.println(u1.hashCode() == u2.hashCode()); // true
        System.out.println(u1); // User[name=Alice, age=20]
    }
}
```

#### Example: Using a record as a key in a HashMap

Let's imagine we have an application where we store the number of visits of users by their name and age (just in case there are two "Ivans 20" at the club).

```java
import java.util.HashMap;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        record User(String name, int age) {
        }
        
        Map<User, Integer> visits = new HashMap<>();
        User ivan20 = new User("Ivan", 20);
        User ivan22 = new User("Ivan", 22);
        
        visits.put(ivan20, 5);
        visits.put(ivan22, 2);
        
        // Let's check that the value lookup works correctly
        System.out.println(visits.get(new User("Ivan", 20))); // 5
        System.out.println(visits.get(new User("Ivan", 22))); // 2
    }
}
```

If **equals** and **hashCode** weren't implemented correctly, the lookup wouldn't work. You'll learn more about **Map** and **HashMap** later.

### 6. Common Mistakes When Working with equals, hashCode, and toString in Record Classes

**Mistake №1: Expecting fields to be modified after creation.**

Record fields are always final, and comparisons are based on their values, which are specified in the constructor. If you "change" the internal state in some clever way (for example, through a mutable object inside a field), the comparison and hash may become incorrect.

**Mistake №2: Overriding equals but forgetting hashCode.**

If you override one of these methods, always override the other! Otherwise, collections (HashSet, HashMap) will behave unpredictably.

**Mistake №3: Expecting toString to be in some other format.**

If you need a custom string format, simply override toString. The default format is always ClassName[field1=value1, field2=value2]**.

**Mistake №4: Using record for complex classes with mutable fields.**

Record fields must be immutable. If you use, for example, an **ArrayList** as a field and someone modifies its contents, the comparison and hash code may break. It's best to use only immutable types for records.

**Mistake №5: Using records for classes with behavior that isn't a value-object.**

A record isn't a "small class with short syntax." It's a value-object, designed to store a set of values. If you have complex logic, mutable state, or need inheritance, use a regular class.
