package Genetic
import java.util.HashMap
class Algs{

public fun fillKnapsackDynamic(load: Int, items: List<Item>, storage: HashMap<LoadCount, Fill> = hashMapOf()): Fill {
    if (load <= 0 || items.isEmpty()) return Fill(0, emptySet())
    val loadCount = LoadCount(load, items.size)
    return storage.getOrPut(loadCount) {
        val itemsWithoutLast = items.subList(0, items.size - 1)
        val fillWithoutLast = fillKnapsackDynamic(load, itemsWithoutLast, storage)
        val last = items.last()
        if (last.weight > load) fillWithoutLast
        else {
            val fillWithLast = fillKnapsackDynamic(load - last.weight, itemsWithoutLast, storage) + Fill(last)
            if (fillWithLast.cost > fillWithoutLast.cost) fillWithLast
            else fillWithoutLast
        }
    }
}

private fun fillKnapsackGreedySorted(load: Int, items: List<Item>): Fill {
    if (load <= 0 || items.isEmpty()) return Fill(0, emptySet())
    val itemsWithoutLast = items.subList(0, items.size - 1)
    val last = items.last()
    if (last.weight > load) return fillKnapsackGreedySorted(load, itemsWithoutLast)
    else return fillKnapsackGreedySorted(load - last.weight, itemsWithoutLast) + Fill(last)
}

public fun fillKnapsackGreedy(load: Int, items: List<Item>): Fill {
    val sorted = items.sortedWith(Comparator<Item> {
        o1, o2 -> (o1.cost.toDouble() / o1.weight).compareTo(o2.cost.toDouble() / o2.weight)
    })
    return fillKnapsackGreedySorted(load, sorted)
}

}