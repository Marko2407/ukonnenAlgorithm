data class Roots(var root: Char, var chars: MutableList<Leaf> = mutableListOf())
data class Leaf(
    var leaf: Char,
    var listOfLeaf: MutableList<MutableList<Char>> = mutableListOf(),
    var listAfterNodeCreated: MutableList<MutableList<Char>> = mutableListOf()
)

val charsRoot =
    mutableListOf('R', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'O', 'P', 'R', 'S', 'T')

fun createNode() {
    val list = listOfRoots[activeRoot].chars[activeEdge]
    val listForLeafs = list.listAfterNodeCreated
    val length = if (activeLength > 0) activeLength - 1 else activeLength

    list.listOfLeaf.forEach {
        list.listAfterNodeCreated.add(it)
    }
    list.listOfLeaf.clear()

    val firstLeaf = Leaf(listForLeafs[length][0], listForLeafs.subList(1, listForLeafs.lastIndex))
    val secondLeaf = Leaf(text[activeIndex])

    listOfRoots.add(Roots(insertNodeChar(), mutableListOf(firstLeaf, secondLeaf)))

    if (activeRoot == 0) {
        activeEdge += 1
        activeLength -= 1
    }
    remaining -= 1
}

fun insertIntoList() {
    listOfRoots[activeRoot].chars.forEach {
        it.listOfLeaf.add(mutableListOf(text[activeIndex]))
    }
}

fun insertNodeChar(): Char {
    val rootChar = charsRoot[0]
    listOfCreatedRoots.add(charsRoot[0])
    charsRoot.removeAt(0)
    return rootChar
}

var activeRoot = 0
var remaining = 0
var activeEdge = -1
var activeLength = 0
var activeIndex = 0
var listOfRoots = mutableListOf<Roots>()
var listOfCreatedRoots = mutableListOf<Char>()
var text = "student$"

fun main() {
    while (activeIndex < text.length) {
        remaining += 1
        while (remaining > 0) {
            if (activeIndex == text.lastIndex) {
                if (activeRoot == 0) {
                    activeLength -= 1
                } else {
                    listOfCreatedRoots.removeAt(listOfCreatedRoots.lastIndex)
                    activeRoot = listOfCreatedRoots.lastIndex
                }
                findCharInListLast()
            } else {
                checkSearchedChar()
            }
        }
        activeIndex += 1
    }
    printResult()
}

//Dodaj prvi element u array
fun checkSearchedChar() {
    if (listOfRoots.isEmpty()) {
        listOfRoots.add(Roots(insertNodeChar(), mutableListOf()))
    } else {
        findCharInList(activeRoot)
    }
}

//nadogradit search kad se ubace nodovi
fun findCharInList(activeRoot: Int) {
    val isCharFinded = if (activeEdge == -1) {
        var result = false
        listOfRoots[activeRoot].chars.forEach {
            if (it.leaf == text[activeIndex]) {
                result = true
            }
        }
        result
    } else {
        var result = false
        listOfRoots[activeRoot].chars[activeEdge].listOfLeaf.forEach {
            if (it.contains(text[activeIndex])) result = true
        }
        result
    }
    insertIntoList()
    if (isCharFinded) {
        activeEdge = findIndex(listOfRoots[activeRoot])
        activeLength += 1
        remaining += 1
        activeIndex += 1
    } else {
        if (activeLength > 0) {
            createNode()
            activeLength -= 1
        } else {
            if (activeEdge < 0) {
                listOfRoots[activeRoot].chars.add(Leaf(text[activeIndex]))

            } else {
                listOfRoots[activeRoot].chars[activeEdge].listOfLeaf.add(mutableListOf(text[activeIndex]))
            }
        }
        remaining -= 1
    }
}

fun findIndex(roots: Roots): Int {
    return if (activeEdge < 0) {
        var index = 0
        roots.chars.forEachIndexed { i, it ->
            if (it.leaf == text[activeIndex]) {
                index = i
            }
        }
        index
    } else {
        roots.chars[activeEdge].listOfLeaf.forEach {
            if (it.contains(text[activeIndex])) {
                return activeEdge
            }
        }
        return roots.chars[activeEdge].listOfLeaf[0].indexOf(text[activeIndex])
    }
}

fun printResult() {
    println("Unesena riječ: $text")
    listOfRoots.forEach { root ->
        println("Korijen: " + root.root)
        root.chars.forEach {
            print("List: " + it.leaf + " Karakteri: ")
            it.listOfLeaf.forEach {
                it.forEach {
                    print("[$it]")
                }
            }
            println()
        }
        println()
    }
}

fun findCharInListLast() {
    var isCharFinded = false
    if (activeEdge != -1) {
        listOfRoots[activeRoot].chars[activeEdge].listOfLeaf.forEach {
            if (it.contains(text[activeIndex])) isCharFinded = true
        }
        insertIntoList()
        if (!isCharFinded) {
            createNode()
            remaining -= 1
        }
    } else {
        remaining -= 1
    }
}