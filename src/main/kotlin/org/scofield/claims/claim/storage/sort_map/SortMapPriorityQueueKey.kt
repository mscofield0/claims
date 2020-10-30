package org.scofield.claims.claim.storage.sort_map

data class SortMapPriorityQueueKey<T>(
    val priority: Int,
    val key: T
) : Comparable<SortMapPriorityQueueKey<T>> {
    override fun compareTo(other: SortMapPriorityQueueKey<T>): Int = this.priority - other.priority
    override fun equals(other: Any?) = (other is SortMapPriorityQueueKey<*>) && this.key == other.key
    override fun hashCode(): Int {
        return key?.hashCode() ?: 0
    }
}