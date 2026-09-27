package com.example.myapplication

import java.security.SecureRandom

object WordDatabase {
    private val secureRandom = SecureRandom()
    private val usedPairIds = mutableSetOf<String>()
    
    val customPairs = mutableListOf<WordPairData>()

    fun addCustomPair(word1: String, word2: String, hint: String) {
        if (word1.isNotBlank() && word2.isNotBlank()) {
            val newPair = WordPairData(
                category = "Custom",
                word1Az = word1.trim(),
                word1En = word1.trim(),
                word2Az = word2.trim(),
                word2En = word2.trim(),
                hintAz = if (hint.isNotBlank()) hint.trim() else "Xüsusi Söz",
                hintEn = if (hint.isNotBlank()) hint.trim() else "Custom Word",
                difficulty = Difficulty.MEDIUM,
                def1Az = word1.trim(),
                def1En = word1.trim(),
                def2Az = word2.trim(),
                def2En = word2.trim()
            )
            customPairs.add(newPair)
        }
    }

    private val allBaseWords: List<WordPairData>
        get() = EasyWords.list + MediumWords.list + HardWords.list

    val categories: Map<String, List<WordPairData>>
        get() = allBaseWords.groupBy { it.category }

    fun getRandomPair(selectedCategories: List<String>, targetDifficulty: Difficulty? = null): WordPair {
        val catMap = categories
        val categoryList = if (selectedCategories.isEmpty()) catMap.keys.toList() else selectedCategories
        
        val poolFromCategories = categoryList.flatMap { cat ->
            if (cat == "Custom") customPairs else (catMap[cat] ?: emptyList())
        }
        
        val difficultyMatches = if (targetDifficulty != null) {
            val matched = poolFromCategories.filter { it.difficulty == targetDifficulty }
            if (matched.isNotEmpty()) matched else poolFromCategories
        } else {
            poolFromCategories
        }
        
        val pool = if (difficultyMatches.isNotEmpty()) difficultyMatches else (allBaseWords + customPairs)
        
        val unusedPairs = pool.filter { pair ->
            val id = "${pair.category}_${pair.word1Az}_${pair.word2Az}"
            !usedPairIds.contains(id)
        }
        
        val candidatePool = if (unusedPairs.isNotEmpty()) {
            unusedPairs
        } else {
            pool.forEach { pair ->
                val id = "${pair.category}_${pair.word1Az}_${pair.word2Az}"
                usedPairIds.remove(id)
            }
            pool
        }
        
        val selectedPair = candidatePool[secureRandom.nextInt(candidatePool.size)]
        usedPairIds.add("${selectedPair.category}_${selectedPair.word1Az}_${selectedPair.word2Az}")

        // Definition of the word is the same as the word itself
        val def1Az = selectedPair.word1Az
        val def1En = selectedPair.word1En
        val def2Az = selectedPair.word2Az
        val def2En = selectedPair.word2En

        return WordPair(
            category = selectedPair.category, // Guaranteed TRUE parent category!
            word1 = LocalizedWord(selectedPair.word1Az, selectedPair.word1En, def1Az, def1En),
            word2 = LocalizedWord(selectedPair.word2Az, selectedPair.word2En, def2Az, def2En),
            hint = LocalizedWord(selectedPair.hintAz, selectedPair.hintEn, "Mövzu ipucu", "Topic hint")
        )
    }
}
