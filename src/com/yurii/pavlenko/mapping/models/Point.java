package com.yurii.pavlenko.mapping.models;

/**
 * A record representing a point on a 2D plane.
 * Automatically implements equals() and hashCode() based on x and y.
 */
public record Point(double x, double y) {
}