package com.yurii.pavlenko.app;

import com.yurii.pavlenko.mapping.models.Point;

/**
 * Application to verify the equality and hashing contract of Java Records.
 */
public class MapLauncherApp {

    public static void main(String[] args) {
        // Create two points with exactly the same coordinates (e.g., New York City)
        Point point1 = new Point(40.7128, -74.0060);
        Point point2 = new Point(40.7128, -74.0060);

        // Compare objects using equals()
        // Expected: true, because records compare components by value
        boolean equalsResult = point1.equals(point2);

        // Compare their hash codes
        // Expected: true, because equal objects must have the same hash code
        boolean sameHash = point1.hashCode() == point2.hashCode();

        // Print results to confirm consistency
        System.out.println(equalsResult);
        System.out.println(sameHash);
    }
}