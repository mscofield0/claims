package org.scofield.claims.utils

import org.scofield.claims.claim.Claim

data class Point(
    var x: Int,
    var y: Int
)

infix fun Point.inside(area: Rect): Boolean {
    return this.x >= area.x &&
           this.y >= area.y &&
           this.x <= area.x + area.width &&
           this.y <= area.y + area.height
}

infix fun Point.inside(claim: Claim): Boolean = this inside claim.area