package org.scofield.claims.utils

import org.scofield.claims.claim.Claim

data class Point(
    var x: Int,
    var y: Int
) {
    infix fun inside(area: Rect): Boolean {
        return this >= area.topLeft &&
                this <= area.bottomRight
    }

    infix fun inside(claim: Claim): Boolean = this inside claim.area

    infix operator fun plus(rhs: Point) = Point(x + rhs.x, y + rhs.y)
    infix operator fun minus(rhs: Point) = Point(x - rhs.x, y - rhs.y)
    fun add(x: Int, y: Int) = Point(this.x + x, this.y + y)

    infix operator fun compareTo(rhs: Point): Int {
        val a = rhs.x - this.x
        val b = rhs.y - this.y
        return if (a < 0 && b < 0) {
            -1
        } else if (a > 0 && b > 0) {
            1
        } else {
            0
        }
    }
}
