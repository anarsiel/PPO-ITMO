class HashMapLRU<K, V>() {
    private val MAX_CACHE_SIZE = 5
    private val keysCache = ArrayDeque<K>(MAX_CACHE_SIZE)
    private val data = HashMap<K, V>()

    fun insert(key: K, value: V) {
        if (cacheContains(key)) {
            updateCache(key)
            assert(cacheContains(key))
            return
        }

        pushToCache(key)
        data[key] = value
        assert(cacheContains(key))
    }

    fun remove(key: K) {
        if (cacheContains(key)) {
            removeFromCache(key)
        }

        data.remove(key)
        assert(!cacheContains(key))
    }

    fun has(key: K): Boolean {
        return cacheContains(key) or data.containsKey(key)
    }

    fun getCache(): ArrayDeque<K> {
        return keysCache
    }

    private fun updateCache(key: K) {
        if (cacheContains(key)) {
            removeFromCache(key)
        }
        pushToCache(key)
    }

    private fun cacheContains(key: K): Boolean {
        return keysCache.contains(key)
    }

    private fun pushToCache(key: K) {
        if (keysCache.size == MAX_CACHE_SIZE) {
            keysCache.removeLast()
            assert(keysCache.size == MAX_CACHE_SIZE - 1)
        }
        keysCache.addFirst(key)
    }

    private fun removeFromCache(key: K) {
        keysCache.remove(key)
    }
}