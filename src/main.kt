val charsRoot = mutableListOf('R', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')

data class Roots(var root: Char, var chars: MutableList<Leaf> = mutableListOf())
data class Leaf(
    var leaf: Char,
    var listOfLeaf: MutableList<MutableList<Char>> = mutableListOf(),
    var listAfterNodeCreated: MutableList<MutableList<Char>> = mutableListOf()
)

fun createNode() {
    val list = listOfRoots[activeRoot].chars[activeEdge]
    list.listOfLeaf.forEach {
        list.listAfterNodeCreated.add(it)
    }
    val lastIndex = list.listOfLeaf.lastIndex
    // ostatak
    val leafList = list.listAfterNodeCreated.subList(activeLength-1, lastIndex + 1)

    val listOfLeafAfterNode = list.listOfLeaf[0]
    val leaf = if (list.listOfLeaf.size <= leafList.size) list.leaf else listOfLeafAfterNode[0]
    val newLeaf = Leaf(leaf,leafList) // ono sto ide u novi root
    val secondLeaf = Leaf(text[activeIndex])
    listOfRoots.add(Roots(insertNodeChar(), mutableListOf(newLeaf, secondLeaf)))
    list.listOfLeaf.clear()
    if (!leafList.contains(list.listAfterNodeCreated[0])){
        list.listOfLeaf.add(listOfLeafAfterNode)
    }




    if (activeRoot == 0) {
        activeEdge += 1
        activeLength -= 1
    }
}

fun insertIntoList() {
    listOfRoots.forEach {
        it.chars.forEach {
            it.listOfLeaf.add(mutableListOf(text[activeIndex]))
        }
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
var i = 0

fun main() {
    while (activeIndex < text.length) {
        remaining += 1
        while (remaining > 0) {
            if (activeIndex == text.lastIndex) {
                //ako je prosao vec jednom onda povecaj edge i smanji len za 1
                // ako nije root u 0 onda ga prebaci u prethodni i provjeri da li postoji taj znak  ako je je u 0 i ako je len u 0 onda provjeri za taj znak nalazi u rootu ako len nije 0 onda provjeri za taj edge
                if (i == 1) {
                    insertIntoList()
//                    activeLength -= 1
//                    activeEdge += 1
                }

                if (activeRoot != 0) {
                    listOfCreatedRoots.removeAt(listOfCreatedRoots.lastIndex)
                    activeRoot = listOfCreatedRoots.lastIndex
                }

                findCharInListLast()
                i++
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
        insertIntoList()
        findCharInList()
    }
}

fun findCharInList() {
    if (findChar()) {
        activeLength += 1
        activeIndex += 1
        remaining += 1
    } else {
        if (activeLength > 0) {
            createNode()
        } else {
            listOfRoots[activeRoot].chars.add(Leaf(text[activeIndex]))
        }
        remaining -= 1
    }
}

fun findChar(): Boolean {
    return if (activeEdge == -1) {
        var result = false
        listOfRoots[activeRoot].chars.forEachIndexed { index, it ->
            if (it.leaf == text[activeIndex]) {
                result = true
                activeEdge = index
            }
        }
        result
    } else {
        var result = false
        listOfRoots[activeRoot].chars[activeEdge].listOfLeaf.forEach {
            if (it[0] == text[activeIndex]) {
                result = true
            }
        }
        if (!result && listOfCreatedRoots.size > 1) {
            listOfRoots[activeRoot].chars[activeEdge].listAfterNodeCreated.forEach {
                val firstElement = it[0]
                if (firstElement == text[activeIndex]) {
                    result = true
                    activeEdge = listOfRoots[activeRoot].chars.indexOf(Leaf(firstElement))
                }
            }
        }
        result
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
    if (activeLength != 0) {
        isCharFinded = listOfRoots[activeRoot].chars[activeEdge].listOfLeaf[activeLength].contains(text[activeIndex])
        if (!isCharFinded) {
            createNode()
        }
    } else {
        listOfRoots[activeRoot].chars.forEach {
            if (it.leaf == text[activeIndex]) isCharFinded = true
        }
        if (!isCharFinded) {
            listOfRoots[activeRoot].chars.add(Leaf(text[activeIndex]))
        }
    }

    remaining -= 1
}