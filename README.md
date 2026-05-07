# Mapping System: Record Equality & Hashing (JavaBasics_Task_394_V0.1)

## 📖 Description
In geographic information systems (GIS), coordinate precision and identity are paramount. This project demonstrates the **Contractual Consistency of `equals()` and `hashCode()`** within Java Records. Records provide an out-of-the-box implementation where equality is determined by the state of their components (X and Y coordinates). Crucially, they also ensure that equal objects produce identical hash codes, which is a mandatory requirement for Java's hash-based collections like `HashSet` or `HashMap`.

## 📋 Requirements Compliance
- **Value-Based Identity**: Demonstrated that independent Record instances with the same data are logically equal.
- **Hash Code Consistency**: Verified that equal Record objects yield identical hash results.
- **Precision Management**: Used `double` types for coordinates to simulate realistic mapping data.
- **Modern Standards**: Adhered to Java 16+ Record specifications and professional documentation templates.

## 🚀 Architectural Stack
- Java 16+ (Records, Object Identity API)

## 🏗️ Implementation Details
- **Point**: A record representing a 2D coordinate with automatic identity logic.
- **MapLauncherApp**: The entry point for validating the equality and hashing contracts.

## 📋 Expected result
```text
true
true
```

## 💻 Code Example

Project Structure:

    JavaBasics_Task_394/
    ├── src/
    │   └── com/yurii/pavlenko/
    │                 ├── app/
    │                 │   └── MapLauncherApp.java
    │                 └── mapping/
    │                     └── models/
    │                         └── Point.java
    ├── LICENSE
    ├── TASK.md
    ├── THEORY.md
    └── README.md

Code
```java
package com.yurii.pavlenko.app;

import com.yurii.pavlenko.mapping.models.Point;

public class MapLauncherApp {
    public static void main(String[] args) {
        Point point1 = new Point(40.7128, -74.0060);
        Point point2 = new Point(40.7128, -74.0060);

        boolean equalsResult = point1.equals(point2);
        boolean sameHash = point1.hashCode() == point2.hashCode();

        System.out.println(equalsResult);
        System.out.println(sameHash);
    }
}
```
```java
package com.yurii.pavlenko.mapping.models;

public record Point(double x, double y) {
}
```

## ⚖️ License
This project is licensed under the **MIT License**.

Copyright (c) 2026 Yurii Pavlenko

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files...

License: [MIT](LICENSE)
