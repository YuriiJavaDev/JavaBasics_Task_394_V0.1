### You're developing a mapping application where coordinate accuracy is important. You need to ensure that two points with identical coordinates are considered the same.

#### - Create a record class that represents a point on a two-dimensional plane, containing its X and Y coordinates.

#### - In the main program, create two point objects with exactly the same coordinate values.

#### - Your goal is to check their equality using the standard comparison method, and then compare their hash codes. Print both results to ensure that both the comparison and the hashing are positive, confirming the points are identical.

```java
public class MapLauncherApp {
    public static void main(String[] args) {
        // Create two points with exactly the same coordinates
        Point point1 = new Point(40.7128, -74.0060);
        Point point2 = new Point(40.7128, -74.0060);

        // Compare objects using equals (the method is automatically generated for record)
        boolean equalsResult = point1.equals(point2);

        // Compare their hash codes (hashCode is also automatically generated for record)
        boolean sameHash = point1.hashCode() == point2.hashCode();

        // Print both results: true and true are expected
        System.out.println(equalsResult);
        System.out.println(sameHash);
    }
}
```
