package org.scofield.claims.utils

data class Rect(
    var topLeft: Point,
    var bottomRight: Point,
) {
}

infix fun Rect.intersects(other: Rect): Boolean {
    return this.topLeft.x <= other.bottomRight.x &&
            this.topLeft.y <= other.bottomRight.y &&
            this.bottomRight.x >= other.topLeft.x &&
            this.bottomRight.y >= other.topLeft.y
}

infix fun Rect.partOf(other: Rect): Boolean {
    return this.topLeft >= other.topLeft &&
            this.bottomRight <= other.bottomRight
}