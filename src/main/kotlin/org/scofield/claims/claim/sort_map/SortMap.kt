package org.scofield.claims.claim.sort_map

import java.util.*

class SortMap<K, V>(
    val layerMap: TreeSet<SortMapPriorityQueueKey<K>>,
    val valueMap: HashMap<K, V>
) {
    operator fun set(key: K, value: Pair<Int, V>) {
        this.layerMap.add(SortMapPriorityQueueKey(value.first, key))
        this.valueMap[key] = value.second
    }
}