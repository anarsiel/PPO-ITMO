import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HashMapLRUTest {
    @Test
    fun ` input less than cache size `() {
        val hashMap = HashMapLRU<Int, Int>()

        for (i in 1..5) {
            hashMap.insert(i, i)
        }
        val expected = listOf(5, 4, 3, 2, 1)
        assertEquals(expected, hashMap.getCache())
    }

    @Test
    fun ` input more than cache size `() {
        val hashMap = HashMapLRU<Int, Int>()

        for (i in 1..10) {
            hashMap.insert(i, i)
        }
        val expected = listOf(10, 9, 8, 7, 6)
        assertEquals(expected, hashMap.getCache())
    }

    @Test
    fun ` input non standart types `() {
        class NonStandartType()

        val hashMap = HashMapLRU<String, NonStandartType>()

        for (i in 1..5) {
            hashMap.insert(i.toString(), NonStandartType())
        }
        val expected = listOf("5", "4", "3", "2", "1")
        assertEquals(expected , hashMap.getCache())
    }

    @Test
    fun ` key reuse `() {
        val hashMap = HashMapLRU<String, Int>()

        for (i in 1..5) {
            hashMap.insert(i.toString(), i)
        }

        hashMap.insert("3", 1)
        hashMap.insert("2", 1)
        hashMap.insert("5", 1)

        val expected = listOf("5", "2", "3", "4", "1")
        assertEquals(expected , hashMap.getCache())
    }

    @Test
    fun ` remove cache element `() {
        val hashMap = HashMapLRU<String, Int>()

        for (i in 1..5) {
            hashMap.insert(i.toString(), i)
        }

        hashMap.insert("3", 1)
        hashMap.insert("2", 1)
        hashMap.remove("3")
        hashMap.insert("5", 1)

        val expected = listOf("5", "2", "4", "1")
        assertEquals(expected , hashMap.getCache())
    }

    @Test
    fun ` has check `() {
        val hashMap = HashMapLRU<String, Int>()

        for (i in 1..6) {
            hashMap.insert(i.toString(), i)
        }

        hashMap.remove("3")

        assertTrue(hashMap.has("2"))
        assertTrue(hashMap.has("1"))
        assertFalse(hashMap.has("3"))
    }
}