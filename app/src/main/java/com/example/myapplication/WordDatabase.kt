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

        val def1Az = selectedPair.word1Az
        val def1En = selectedPair.word1En
        val def2Az = selectedPair.word2Az
        val def2En = selectedPair.word2En

        return WordPair(
            category = selectedPair.category,
            word1 = LocalizedWord(selectedPair.word1Az, selectedPair.word1En, def1Az, def1En),
            word2 = LocalizedWord(selectedPair.word2Az, selectedPair.word2En, def2Az, def2En),
            hint = LocalizedWord(selectedPair.hintAz, selectedPair.hintEn, "Mövzu ipucu", "Topic hint")
        )
    }

    // --- Truth or Dare Questions & Dares ---
    val truthOrDareItems = listOf(
        // 9th Grade (School/Teen) Truths
        TruthOrDareItem("Məktəbdə xoşladığın amma heç vaxt demədiyin şəxs kimdir?", "Who is your school crush you never confessed to?", true, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("İmtahanda etdiyin ən gülməli və ya biabırçı köçürmə üsulu nə olub?", "What is the funniest or most embarrassing cheat method you used in an exam?", true, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("Heç müəlliməyə yalan danışıb dərsdən yayınmısan?", "Have you ever lied to a teacher to skip class?", true, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("Otağında ən son neçə gün təmizlik etməmisən?", "What is the longest you've gone without cleaning your room?", true, TruthOrDareMode.NINTH_GRADE),
        // 9th Grade Dares
        TruthOrDareItem("Məktəb müəlliminin səsini və ya hərəkətini təqlid et!", "Impersonate a school teacher's voice or gesture!", false, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("Ucadan bir bənd milli şeir və ya mahnı oxu!", "Recite a poem stanza or sing a song out loud!", false, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("10 dəfə turnik və ya jim et!", "Do 10 pushups right now!", false, TruthOrDareMode.NINTH_GRADE),
        TruthOrDareItem("Otaqdakı hər hansı bir əşyaya animasiya personajı kimi müraciət et!", "Talk to an object in the room like an animated character!", false, TruthOrDareMode.NINTH_GRADE),

        // 18+ Adult Truths
        TruthOrDareItem("Həyatında etdiyin ən böyük çılğınlıq nə olub?", "What is the wildest thing you've ever done in your life?", true, TruthOrDareMode.ADULT_18),
        TruthOrDareItem("Ən son kimə vurulmusan və niyə gizlətmisən?", "Who was your last secret crush and why did you hide it?", true, TruthOrDareMode.ADULT_18),
        TruthOrDareItem("Heç kimə demədiyin ən böyük sirrin nədir?", "What is your biggest secret you've never told anyone?", true, TruthOrDareMode.ADULT_18),
        TruthOrDareItem("İkinci şans vermək istədiyin keçmiş münasibətin var?", "Is there an ex you would give a second chance to?", true, TruthOrDareMode.ADULT_18),
        // 18+ Adult Dares
        TruthOrDareItem("Otaqdakı bir nəfər haqqında ən səmimi tərifini de!", "Give your most sincere compliment to someone in the room!", false, TruthOrDareMode.ADULT_18),
        TruthOrDareItem("30 saniyə ərzində gülmədən hər hansı bir dramatik monoloq danış!", "Deliver a dramatic monologue for 30 seconds without laughing!", false, TruthOrDareMode.ADULT_18),
        TruthOrDareItem("Telefonundakı ən son şəklini hər kəsə göstər!", "Show the last photo in your phone gallery to everyone!", false, TruthOrDareMode.ADULT_18)
    )

    // --- Who Am I? Character Suggestions ---
    val whoAmISuggestions = listOf(
        "Albert Einstein", "Adolf Hitler", "Nizami Gəncəvi", "Lionel Messi",
        "Cristiano Ronaldo", "Elon Musk", "Napoleon Bonapart", "Cleopatra",
        "Sherlock Holmes", "Harry Potter", "Şah İsmayıl Xətai", "Üzeyir Hacıbəyov",
        "Steve Jobs", "Məhəmməd Əmin Rəsulzadə", "Məhəmməd Füzuli", "Miras Aslanov",
        "Isaac Newton", "William Shakespeare", "Leonardo da Vinci", "Julius Caesar"
    )

    // --- Alias / Hot Potato Words ---
    val aliasWords = listOf(
        "Teleskop", "Kompüter", "Soyuducu", "Təyyarə", "Şahmat", "Avtobus",
        "Delfin", "Piramida", "Fotosintez", "Xalça", "Helikopter", "Plov",
        "Mikroskop", "Tısbağa", "Pərəstişkar", "Aktyor", "Vulkan", "İldırım"
    )
}
