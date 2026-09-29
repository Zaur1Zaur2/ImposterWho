package com.example.myapplication

enum class Language {
    AZ, EN
}

enum class ActiveGame {
    HUB,
    IMPOSTER_WHO,
    WHO_WITH_WHOM,
    TRUTH_OR_DARE,
    ALIAS_HOT_POTATO,
    WHO_AM_I
}

enum class GameMode {
    CLASSIC,        // 1 Imposter, others Civilians
    UNDERCOVER,     // Civilians vs 1 Undercover (slightly different word)
    SPY,            // Civilians vs 1 Spy (no word, but a hint)
    BLANK,          // Civilians vs 1 Mr. White (blank)
    TROLL,          // Randomly related words to everyone
    DUO             // 2 Imposters
}

enum class Role {
    CIVILIAN,
    IMPOSTER,
    UNDERCOVER,
    SPY,
    MR_WHITE
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}

enum class TruthOrDareMode {
    NINTH_GRADE,
    ADULT_18
}

enum class GameState {
    HOME, SETUP, LOADING, SELECTION, REVEAL, PASS_PHONE, PLAYING, VOTING_RESULTS, EJECT_ANIMATION, RESULT, CREDITS, SCOREBOARD, CUSTOM_WORDS
}

data class WordPairData(
    val category: String,
    val word1Az: String,
    val word1En: String,
    val word2Az: String,
    val word2En: String,
    val hintAz: String,
    val hintEn: String,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val def1Az: String = "",
    val def1En: String = "",
    val def2Az: String = "",
    val def2En: String = ""
)

data class Player(
    val id: Int,
    var name: String,
    var role: Role = Role.CIVILIAN,
    var word: String = "",
    var definition: String = "",
    var isEliminated: Boolean = false,
    var isRevealed: Boolean = false,
    val animationType: Int = (0..3).random(),
    val color: Long = 0xFFFFFFFF,
    var votedFor: Int? = null,
    var imposterScore: Int = 0,
    var gamesPlayed: Int = 0,
    var civilianWins: Int = 0,
    var imposterWins: Int = 0,
    var assignedTargetPlayerId: Int? = null,
    var secretCharacterName: String = "",
    var whoAmIScore: Int = 0
)

data class LocalizedWord(
    val az: String,
    val en: String,
    val defAz: String = "",
    val defEn: String = ""
)

data class WordPair(
    val category: String,
    val word1: LocalizedWord,
    val word2: LocalizedWord,
    val hint: LocalizedWord
)

data class CustomPair(
    val word1: String,
    val word2: String,
    val hint: String,
    val category: String = "Custom"
)

data class TruthOrDareItem(
    val textAz: String,
    val textEn: String,
    val isTruth: Boolean,
    val mode: TruthOrDareMode
)
