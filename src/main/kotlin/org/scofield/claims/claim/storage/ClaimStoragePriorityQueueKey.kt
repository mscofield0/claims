package org.scofield.claims.claim.storage

import java.util.*

data class ClaimStoragePriorityQueueKey(
    val priority: Int,
    val key: UUID
) : Comparable<ClaimStoragePriorityQueueKey> {
    override fun compareTo(other: ClaimStoragePriorityQueueKey): Int = this.priority - other.priority
    override fun equals(other: Any?) = (other is ClaimStoragePriorityQueueKey) && this.key == other.key
    override fun hashCode(): Int {
        return key.hashCode()
    }
}