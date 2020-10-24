package org.scofield.claims.utils

data class Rect(
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int,
)

infix fun Rect.intersects(other: Rect): Boolean {
    return this.x <= other.x + other.width &&
           this.y <= other.y + other.height &&
           other.x <= this.x + this.width &&
           other.y <= this.y + this.height
}

infix fun Rect.partOf(other: Rect): Boolean {
    return this.x + this.width <= other.x + other.width &&
           this.y + this.height <= other.y + other.height
}