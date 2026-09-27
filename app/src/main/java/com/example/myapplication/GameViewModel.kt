package com.example.myapplication

import android.app.Application
import android.content.Context
import java.security.SecureRandom
import java.util.UUID
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class GameViewModel(application: Application) : AndroidViewModel(application) {
    
    private val secureRandom = SecureRandom()
    private val prefs = application.getSharedPreferences("ImposterGamePrefs", Context.MODE_PRIVATE)
    
    private val playerColors = listOf(
        0xFFFFD600, 0xFF00B0FF, 0xFFFF3D00, 0xFFFF6D00, 
        0xFF00E676, 0xFFD500F9, 0xFF651FFF, 0xFF3D5AFE,
        0xFFF50057, 0xFF00E5FF, 0xFF1DE9B6, 0xFFC6FF00
    )

    val players = mutableStateListOf<Player>(
        Player(1, "Player 1", color = 0xFFFF4444),
        Player(2, "Player 2", color = 0xFF33B5E5),
        Player(3, "Player 3", color = 0xFFAA66CC),
        Player(4, "Player 4", color = 0xFF00E676)
    )
    val gameMode = mutableStateOf(GameMode.CLASSIC)
    val gameState = mutableStateOf(GameState.HOME)
    val currentPlayerIndex = mutableIntStateOf(0)
    val language = mutableStateOf(Language.AZ)
    val difficulty = mutableStateOf(Difficulty.MEDIUM)
    val selectedCategories = mutableStateListOf<String>("General")
    val accentColor = mutableLongStateOf(0xFF9D50BB)
    val discussionStarterId = mutableStateOf<Int?>(null)
    val revealOrder = mutableStateListOf<Int>()
    val votingPlayerIndex = mutableIntStateOf(0) 
    val timerDurationSeconds = mutableIntStateOf(60)

    init {
        restoreGameState()
    }

    fun saveGameState() {
        try {
            val editor = prefs.edit()
            editor.putString("gameState", gameState.value.name)
            editor.putInt("currentPlayerIndex", currentPlayerIndex.intValue)
            editor.putInt("votingPlayerIndex", votingPlayerIndex.intValue)
            editor.putString("language", language.value.name)
            editor.putString("difficulty", difficulty.value.name)
            editor.putString("gameMode", gameMode.value.name)
            editor.putLong("accentColor", accentColor.longValue)
            editor.putInt("timerDuration", timerDurationSeconds.intValue)
            
            // Save players count and details
            editor.putInt("playerCount", players.size)
            players.forEachIndexed { idx, p ->
                editor.putInt("p_id_$idx", p.id)
                editor.putString("p_name_$idx", p.name)
                editor.putString("p_role_$idx", p.role.name)
                editor.putString("p_word_$idx", p.word)
                editor.putString("p_def_$idx", p.definition)
                editor.putBoolean("p_elim_$idx", p.isEliminated)
                editor.putBoolean("p_rev_$idx", p.isRevealed)
                editor.putLong("p_color_$idx", p.color)
                editor.putInt("p_voted_$idx", p.votedFor ?: -1)
                editor.putInt("p_score_$idx", p.imposterScore)
                editor.putInt("p_played_$idx", p.gamesPlayed)
                editor.putInt("p_civW_$idx", p.civilianWins)
                editor.putInt("p_impW_$idx", p.imposterWins)
            }
            editor.apply()
        } catch (_: Exception) {}
    }

    private fun restoreGameState() {
        try {
            val savedState = prefs.getString("gameState", null) ?: return
            if (savedState == "HOME") return // Don't auto-restore if home
            
            gameState.value = GameState.valueOf(savedState)
            currentPlayerIndex.intValue = prefs.getInt("currentPlayerIndex", 0)
            votingPlayerIndex.intValue = prefs.getInt("votingPlayerIndex", 0)
            language.value = Language.valueOf(prefs.getString("language", "AZ") ?: "AZ")
            difficulty.value = Difficulty.valueOf(prefs.getString("difficulty", "MEDIUM") ?: "MEDIUM")
            gameMode.value = GameMode.valueOf(prefs.getString("gameMode", "CLASSIC") ?: "CLASSIC")
            accentColor.longValue = prefs.getLong("accentColor", 0xFF9D50BB)
            timerDurationSeconds.intValue = prefs.getInt("timerDuration", 60)

            val count = prefs.getInt("playerCount", 0)
            if (count > 0) {
                players.clear()
                for (idx in 0 until count) {
                    val id = prefs.getInt("p_id_$idx", idx + 1)
                    val name = prefs.getString("p_name_$idx", "Player ${idx + 1}") ?: "Player ${idx + 1}"
                    val role = Role.valueOf(prefs.getString("p_role_$idx", "CIVILIAN") ?: "CIVILIAN")
                    val word = prefs.getString("p_word_$idx", "") ?: ""
                    val def = prefs.getString("p_def_$idx", "") ?: ""
                    val elim = prefs.getBoolean("p_elim_$idx", false)
                    val rev = prefs.getBoolean("p_rev_$idx", false)
                    val color = prefs.getLong("p_color_$idx", playerColors[idx % playerColors.size])
                    val voted = prefs.getInt("p_voted_$idx", -1).let { if (it == -1) null else it }
                    val score = prefs.getInt("p_score_$idx", 0)
                    val played = prefs.getInt("p_played_$idx", 0)
                    val civW = prefs.getInt("p_civW_$idx", 0)
                    val impW = prefs.getInt("p_impW_$idx", 0)

                    players.add(
                        Player(
                            id = id, name = name, role = role, word = word, definition = def,
                            isEliminated = elim, isRevealed = rev, color = color, votedFor = voted,
                            imposterScore = score, gamesPlayed = played, civilianWins = civW, imposterWins = impW
                        )
                    )
                }
                revealOrder.clear()
                revealOrder.addAll(players.indices.filter { !players[it].isEliminated })
            }
        } catch (_: Exception) {}
    }

    fun addPlayer() {
        if (players.size < 50) {
            val nextId = (players.maxByOrNull { it.id }?.id ?: 0) + 1
            val nextColor = playerColors[players.size % playerColors.size]
            players.add(Player(nextId, "Player $nextId", color = nextColor))
            saveGameState()
        }
    }

    fun updatePlayerColor(player: Player) {
        val index = players.indexOf(player)
        if (index != -1) {
            val currentColorIndex = playerColors.indexOf(player.color)
            val nextColorIndex = (currentColorIndex + 1) % playerColors.size
            players[index] = player.copy(color = playerColors[nextColorIndex])
            saveGameState()
        }
    }

    fun removePlayer(player: Player) {
        if (players.size > 3) {
            players.remove(player)
            saveGameState()
        }
    }

    fun updatePlayerName(player: Player, newName: String) {
        val index = players.indexOf(player)
        if (index != -1) {
            players[index] = player.copy(name = newName)
            saveGameState()
        }
    }

    fun addCustomPair(word1: String, word2: String, hint: String) {
        WordDatabase.addCustomPair(word1, word2, hint)
        if (!selectedCategories.contains("Custom")) {
            selectedCategories.add("Custom")
        }
    }

    fun setupGame() {
        assignRoles(gameMode.value)
        
        players.forEachIndexed { index, player ->
            players[index] = player.copy(votedFor = null, isEliminated = false, isRevealed = false)
        }
        
        votingPlayerIndex.intValue = 0
        revealOrder.clear()
        
        val shuffledIndices = players.indices.toMutableList()
        for (i in shuffledIndices.size - 1 downTo 1) {
            val j = secureRandom.nextInt(i + 1)
            val temp = shuffledIndices[i]
            shuffledIndices[i] = shuffledIndices[j]
            shuffledIndices[j] = temp
        }
        revealOrder.addAll(shuffledIndices)
        
        currentPlayerIndex.intValue = 0
        gameState.value = GameState.LOADING
        discussionStarterId.value = null
        saveGameState()
    }

    fun startSelection() {
        gameState.value = GameState.SELECTION
        saveGameState()
    }

    fun startReveal() {
        gameState.value = GameState.REVEAL
        saveGameState()
    }

    private fun assignRoles(mode: GameMode) {
        val wordPair = WordDatabase.getRandomPair(selectedCategories.toList(), difficulty.value)
        val isAz = language.value == Language.AZ

        for (i in players.indices) {
            val civilianWord = if (isAz) wordPair.word1.az else wordPair.word1.en
            players[i] = players[i].copy(
                role = Role.CIVILIAN,
                word = civilianWord,
                definition = civilianWord, // Definition is the same as the word itself!
                isEliminated = false,
                isRevealed = false,
                votedFor = null
            )
        }

        val specialRoleIndices = pickSpecialRoleIndices(mode, players)
        
        specialRoleIndices.forEach { index ->
            // Increase imposterScore by 1 when chosen as imposter, thus decreasing future chances
            players[index] = players[index].copy(imposterScore = players[index].imposterScore + 1)
            
            val role = when (mode) {
                GameMode.UNDERCOVER -> Role.UNDERCOVER
                GameMode.SPY -> Role.SPY
                GameMode.BLANK -> Role.MR_WHITE
                else -> Role.IMPOSTER
            }
            
            val assignedWord: String
            val assignedDef: String

            when (role) {
                Role.IMPOSTER -> {
                    assignedWord = "IMPOSTER"
                    assignedDef = "IMPOSTER"
                }
                Role.UNDERCOVER -> {
                    assignedWord = if (isAz) wordPair.word2.az else wordPair.word2.en
                    assignedDef = assignedWord // Definition is the same as the word itself!
                }
                Role.SPY -> {
                    val hintText = when (difficulty.value) {
                        Difficulty.EASY -> getCategoryNameAzEn(wordPair.category, language.value)
                        Difficulty.MEDIUM -> if (isAz) wordPair.hint.az else wordPair.hint.en
                        Difficulty.HARD -> if (isAz) wordPair.word2.az else wordPair.word2.en
                    }
                    assignedWord = (if (isAz) "İPUCU: " else "HINT: ") + hintText
                    assignedDef = assignedWord
                }
                Role.MR_WHITE -> {
                    assignedWord = if (isAz) "BOŞLUQ" else "BLANK"
                    assignedDef = assignedWord
                }
                else -> {
                    assignedWord = ""
                    assignedDef = ""
                }
            }
            
            players[index] = players[index].copy(role = role, word = assignedWord, definition = assignedDef)
        }
        
        if (mode == GameMode.TROLL) {
            players.indices.forEach { i ->
                val pair = WordDatabase.getRandomPair(selectedCategories.toList(), difficulty.value)
                val trollWord = if (isAz) pair.word1.az else pair.word1.en
                players[i] = players[i].copy(
                    word = trollWord,
                    definition = trollWord
                )
            }
        }
    }

    /**
     * Weighted Imposter Selection:
     * Weight = 1.0 / (imposterScore + 1.0).
     * If one person becomes an imposter, their imposterScore increases by 1,
     * thus decreasing their chances of becoming imposter again.
     */
    private fun pickSpecialRoleIndices(mode: GameMode, currentPlayers: List<Player>): List<Int> {
        val countNeeded = if (mode == GameMode.DUO) 2 else 1
        val selectedIndices = mutableListOf<Int>()
        val candidateIndices = currentPlayers.indices.toMutableList()

        for (step in 0 until countNeeded) {
            if (candidateIndices.isEmpty()) break

            val weights = candidateIndices.map { idx ->
                1.0 / (currentPlayers[idx].imposterScore + 1.0)
            }
            val totalWeight = weights.sum()

            val r = secureRandom.nextDouble() * totalWeight
            var cumulative = 0.0
            var chosenIdx = candidateIndices.first()

            for (i in candidateIndices.indices) {
                cumulative += weights[i]
                if (r <= cumulative) {
                    chosenIdx = candidateIndices[i]
                    break
                }
            }

            selectedIndices.add(chosenIdx)
            candidateIndices.remove(chosenIdx)
        }

        return selectedIndices
    }

    private fun getCategoryNameAzEn(category: String, language: Language): String {
        val az = mapOf(
            "General" to "Ümumi / Asan", "Food" to "Qida & İçki", "Animal" to "Heyvanlar",
            "School" to "Məktəb & Elm", "Tech" to "Texnologiya", "Hospital" to "Tibb / Xəstəxana",
            "City" to "Şəhərlər", "Country" to "Ölkələr", "Space" to "Kosmos",
            "Culture" to "Mədəniyyət & İncəsənət", "Profession" to "Peşələr", "Sport" to "İdman",
            "Vehicle" to "Nəqliyyat", "Everyday" to "Məişət Əşyaları", "Custom" to "Xüsusi"
        )
        return if (language == Language.AZ) az[category] ?: category else category
    }

    fun proceedToNextState() {
        val activeCount = if (revealOrder.isNotEmpty()) revealOrder.size else players.size
        if (currentPlayerIndex.intValue < activeCount - 1) {
            gameState.value = GameState.PASS_PHONE
        } else {
            finalizeDiscussionStarter()
            gameState.value = GameState.PLAYING
            currentPlayerIndex.intValue = 0
        }
        saveGameState()
    }

    fun finalizeDiscussionStarter() {
        val alivePlayers = players.filter { !it.isEliminated }
        if (alivePlayers.isNotEmpty()) {
            val randomChoice = UUID.randomUUID().hashCode() % alivePlayers.size
            val safeIdx = if (randomChoice < 0) -randomChoice else randomChoice
            discussionStarterId.value = alivePlayers[safeIdx].id
        }
    }

    val lastEliminatedPlayer = mutableStateOf<Player?>(null)

    fun confirmPass() {
        val activeCount = if (revealOrder.isNotEmpty()) revealOrder.size else players.size
        if (currentPlayerIndex.intValue < activeCount - 1) {
            currentPlayerIndex.intValue++
            gameState.value = GameState.REVEAL
        }
        saveGameState()
    }

    fun votePlayer(votedPlayer: Player) {
        val voters = players.filter { !it.isEliminated }
        val currentVoter = voters[votingPlayerIndex.intValue]
        
        val voterActualIndex = players.indexOf(currentVoter)
        players[voterActualIndex] = currentVoter.copy(votedFor = votedPlayer.id)
        
        if (votingPlayerIndex.intValue < voters.size - 1) {
            votingPlayerIndex.intValue++
            gameState.value = GameState.PLAYING
        } else {
            gameState.value = GameState.VOTING_RESULTS
        }
        saveGameState()
    }

    fun finalizeElimination() {
        val alivePlayers = players.filter { !it.isEliminated }
        val voteCounts = alivePlayers.associateBy({ it.id }, { 0 }).toMutableMap()
        alivePlayers.forEach { p ->
            p.votedFor?.let { votedId ->
                voteCounts[votedId] = (voteCounts[votedId] ?: 0) + 1
            }
        }
        
        val maxVotes = voteCounts.values.maxOrNull() ?: 0
        val candidates = voteCounts.filter { it.value == maxVotes }.keys
        
        if (candidates.size == 1) {
            val eliminatedId = candidates.first()
            val eliminatedPlayerIndex = players.indexOfFirst { it.id == eliminatedId }
            if (eliminatedPlayerIndex != -1) {
                val eliminatedPlayer = players[eliminatedPlayerIndex]
                players[eliminatedPlayerIndex] = eliminatedPlayer.copy(isEliminated = true)
                lastEliminatedPlayer.value = eliminatedPlayer
                gameState.value = GameState.EJECT_ANIMATION
                saveGameState()
                return
            }
        }
        
        lastEliminatedPlayer.value = null
        startNewRoundWithSameRoles()
    }

    fun proceedAfterEject() {
        processGameProgression()
        saveGameState()
    }

    private fun processGameProgression() {
        val alivePlayers = players.filter { !it.isEliminated }
        val remainingImposters = alivePlayers.count { it.role != Role.CIVILIAN }
        val remainingCivilians = alivePlayers.count { it.role == Role.CIVILIAN }

        val isGameOver = remainingImposters == 0 || remainingCivilians <= remainingImposters
        if (isGameOver) {
            val imposterWon = remainingImposters > 0
            players.forEachIndexed { index, player ->
                val newGames = player.gamesPlayed + 1
                val isImp = player.role != Role.CIVILIAN
                val newImpWins = if (isImp && imposterWon) player.imposterWins + 1 else player.imposterWins
                val newCivWins = if (!isImp && !imposterWon) player.civilianWins + 1 else player.civilianWins
                players[index] = player.copy(
                    gamesPlayed = newGames,
                    imposterWins = newImpWins,
                    civilianWins = newCivWins
                )
            }
            gameState.value = GameState.RESULT
        } else {
            startNewRoundWithSameRoles()
        }
    }

    private fun startNewRoundWithSameRoles() {
        players.indices.forEach { i -> players[i] = players[i].copy(votedFor = null) }
        votingPlayerIndex.intValue = 0

        val wordPair = WordDatabase.getRandomPair(selectedCategories.toList(), difficulty.value)
        val isAz = language.value == Language.AZ

        players.indices.forEach { index ->
            val player = players[index]
            if (!player.isEliminated) {
                when (player.role) {
                    Role.CIVILIAN -> {
                        val word = if (isAz) wordPair.word1.az else wordPair.word1.en
                        players[index] = player.copy(word = word, definition = word)
                    }
                    Role.UNDERCOVER -> {
                        val word = if (isAz) wordPair.word2.az else wordPair.word2.en
                        players[index] = player.copy(word = word, definition = word)
                    }
                    Role.SPY -> {
                        val hintText = when (difficulty.value) {
                            Difficulty.EASY -> getCategoryNameAzEn(wordPair.category, language.value)
                            Difficulty.MEDIUM -> if (isAz) wordPair.hint.az else wordPair.hint.en
                            Difficulty.HARD -> if (isAz) wordPair.word2.az else wordPair.word2.en
                        }
                        val word = (if (isAz) "İPUCU: " else "HINT: ") + hintText
                        players[index] = player.copy(word = word, definition = word)
                    }
                    Role.IMPOSTER -> {
                        players[index] = player.copy(word = "IMPOSTER", definition = "IMPOSTER")
                    }
                    Role.MR_WHITE -> {
                        val word = if (isAz) "BOŞLUQ" else "BLANK"
                        players[index] = player.copy(word = word, definition = word)
                    }
                }
            }
        }
        
        currentPlayerIndex.intValue = 0
        revealOrder.clear()
        
        val aliveIndices = players.indices.filter { !players[it].isEliminated }.toMutableList()
        for (i in aliveIndices.size - 1 downTo 1) {
            val j = secureRandom.nextInt(i + 1)
            val temp = aliveIndices[i]
            aliveIndices[i] = aliveIndices[j]
            aliveIndices[j] = temp
        }
        revealOrder.addAll(aliveIndices)
        
        gameState.value = GameState.REVEAL
        saveGameState()
    }
    
    fun toggleLanguage() {
        language.value = if (language.value == Language.AZ) Language.EN else Language.AZ
        saveGameState()
    }
}
