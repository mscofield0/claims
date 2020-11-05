package org.scofield.claims.utils

import org.scofield.claims.claim.Claim

data class Point(
    var x: Int,
    var y: Int
) {
    infix fun inside(area: Rect): Boolean {
        return this.x >= area.x &&
                this.y >= area.y &&
                this.x <= area.x + area.width &&
                this.y <= area.y + area.height
    }

    infix fun inside(claim: Claim): Boolean = this inside claim.area

    fun add(rhs: Point): Point = Point(x + rhs.x, y + rhs.y)
    fun add(x: Int, y: Int): Point = Point(this.x + x, this.y + y)
}
