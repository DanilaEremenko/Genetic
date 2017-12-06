package Genetic


data class Fill(val cost: Int, val items: Set<Item>) {
    operator fun plus(fill: Fill) = Fill(cost + fill.cost, items + fill.items)


    constructor(cost: Int, vararg items: Item): this(cost, items.toSet())

    constructor(item: Item): this(item.cost, item)
}

data class LoadCount(val load: Int, val count: Int)

data class Item(val cost: Int, val weight: Int)

