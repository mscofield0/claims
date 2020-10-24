package org.scofield.claims.claim

import kotlin.Comparator

import java.util.*

class SortMap<K, V>(
    val layerMap: TreeMap<Int, K>,
    val valueMap: HashMap<K, V>
) {
    operator fun set(key: K, value: Pair<Int, V>) {
        this.layerMap[value.first] = key
        this.valueMap[key] = value.second
    }
}
