package com.example.myapplication.ui

import android.app.Application
import com.example.myapplication.R
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.*
import com.example.myapplication.ui.theme.ImposterTheme
import kotlinx.coroutines.delay
import java.util.Random

// --- UI Constants & Helpers ---

val NeonBlue = Color(0xFF6E48AA)
val NeonCyan = Color(0xFF00E5FF)
val NeonRed = Color(0xFFFF3D00)

val GlassBackground = Color.White.copy(alpha = 0.1f)
val GlassBorder = Color.White.copy(alpha = 0.2f)

fun getDynamicMainGradient(accentColor: Color): Brush {
    val darkBase = Color(0xFF090712)
    val accentTintTop = Color(
        red = (accentColor.red * 0.35f + darkBase.red * 0.65f),
        green = (accentColor.green * 0.35f + darkBase.green * 0.65f),
        blue = (accentColor.blue * 0.35f + darkBase.blue * 0.65f),
        alpha = 1f
    )
    val accentTintMid = Color(
        red = (accentColor.red * 0.20f + darkBase.red * 0.80f),
        green = (accentColor.green * 0.20f + darkBase.green * 0.80f),
        blue = (accentColor.blue * 0.20f + darkBase.blue * 0.80f),
        alpha = 1f
    )
    val darkBottom = Color(0xFF040308)
    return Brush.verticalGradient(listOf(accentTintTop, accentTintMid, darkBottom))
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = GlassBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

fun t(key: String, language: Language): String {
    val az = mapOf(
        "start" to "BAŞLA",
        "players" to "OYUNÇULAR",
        "add_player" to "Oyunçu Əlavə Et",
        "categories" to "KATEQORİYALAR",
        "modes" to "MODLAR",
        "reveal_title" to "ROLUNU GÖR",
        "hold_to_reveal" to "Görmək üçün basıb saxlayın",
        "pass_to" to "Telefonu ötürün: ",
        "next" to "NÖVBƏTİ",
        "loading" to "YÜKLƏNİR...",
        "selecting" to "İMPOSTER SEÇİLİR...",
        "ready" to "HAZIRDIR!",
        "start_game" to "OYUNU BAŞLAT",
        "discussion" to "MÜZAKİRƏ BAŞLAYIR!",
        "starter" to "Müzakirəni başlayır: ",
        "vote_out" to "Kim IMPOSTER-dir?",
        "victory" to "QƏLƏBƏ!",
        "defeat" to "MƏĞLUBİYYƏT!",
        "disqualified" to "KƏNARLAŞDIRILDI",
        "play_again" to "YENİDƏN OYNA",
        "back_home" to "ANA SƏHİFƏ",
        "credits" to "KREDİTLƏR",
        "developed_by" to "Hazırlayan: Zaur",
        "version" to "Versiya 1.0",
        "imposter_reveal" to "İMPOSTERİ GÖR",
        "word_reveal" to "SÖZÜ GÖR",
        "how_to_play" to "Necə Oynamalı?",
        "hint_text" to "Hər kəsə bir söz verilir, amma İmposter(lər) başqa bir şey və ya heç nə görmür. Müzakirə edin və saxtakarı tapın!",
        "scoreboard" to "LİDERLƏR TAXTASI",
        "custom_words" to "XÜSUSİ SÖZLƏR",
        "add_word_pair" to "Söz Cütlüyü Əlavə Et",
        "timer" to "TAYMER",
        "confirm_elimination" to "DAVAM ET",
        "mode_classic" to "Klassik",
        "mode_classic_desc" to "1 İmposter, digərləri eyni sözü görür.",
        "mode_undercover" to "Casus",
        "mode_undercover_desc" to "Bir nəfərə çox bənzər, amma fərqli söz verilir.",
        "mode_spy" to "İpucu",
        "mode_spy_desc" to "İmposter söz yerinə yalnız mövzu ipucunu görür.",
        "mode_blank" to "Bəyaz",
        "mode_blank_desc" to "Mr. White heç bir söz görmür, təxmin etməlidir.",
        "mode_troll" to "Troll",
        "mode_troll_desc" to "Hər kəsə fərqli, amma əlaqəli sözlər verilir (Xaos!).",
        "mode_duo" to "Cütlük",
        "mode_duo_desc" to "2 İmposter birlikdə işləyir."
    )
    val en = mapOf(
        "start" to "START",
        "players" to "PLAYERS",
        "add_player" to "Add Player",
        "categories" to "CATEGORIES",
        "modes" to "MODES",
        "reveal_title" to "REVEAL ROLE",
        "hold_to_reveal" to "Hold to reveal",
        "pass_to" to "Pass phone to: ",
        "next" to "NEXT",
        "loading" to "LOADING...",
        "selecting" to "SELECTING IMPOSTER...",
        "ready" to "READY!",
        "start_game" to "START GAME",
        "discussion" to "DISCUSSION STARTS!",
        "starter" to "Discussion starter: ",
        "vote_out" to "Who is the imposter?",
        "victory" to "VICTORY!",
        "defeat" to "DEFEAT!",
        "disqualified" to "DISQUALIFIED",
        "play_again" to "PLAY AGAIN",
        "back_home" to "HOME",
        "credits" to "CREDITS",
        "developed_by" to "Developed by: Zaur",
        "version" to "Version 1.0",
        "imposter_reveal" to "REVEAL IMPOSTER",
        "word_reveal" to "REVEAL WORD",
        "how_to_play" to "How to Play?",
        "hint_text" to "Everyone gets a word, but the Imposter sees something else or nothing. Discuss and find the betrayer!",
        "scoreboard" to "SCOREBOARD",
        "custom_words" to "CUSTOM WORDS",
        "add_word_pair" to "Add Custom Pair",
        "timer" to "TIMER",
        "confirm_elimination" to "PROCEED",
        "mode_classic" to "Classic",
        "mode_classic_desc" to "1 Imposter, others see the same word.",
        "mode_undercover" to "Undercover",
        "mode_undercover_desc" to "One person gets a similar but different word.",
        "mode_spy" to "Spy",
        "mode_spy_desc" to "Imposter sees only a category hint instead of the word.",
        "mode_blank" to "Mr. White",
        "mode_blank_desc" to "Mr. White sees no word at all.",
        "mode_troll" to "Troll",
        "mode_troll_desc" to "Everyone gets slightly different words (Chaos!).",
        "mode_duo" to "Duo",
        "mode_duo_desc" to "2 Imposters working together."
    )
    return (if (language == Language.AZ) az[key] else en[key]) ?: key
}

// --- Main Navigation Entry ---

@Composable
fun ImposterGame(viewModel: GameViewModel) {
    val accentColor = Color(viewModel.accentColor.longValue)
    val dynamicBackground = remember(viewModel.accentColor.longValue) {
        getDynamicMainGradient(accentColor)
    }
    var showHomeConfirmDialog by remember { mutableStateOf(false) }
    val lang = viewModel.language.value

    if (showHomeConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showHomeConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showHomeConfirmDialog = false
                    viewModel.gameState.value = GameState.HOME
                    viewModel.saveGameState()
                }) {
                    Text(
                        if (lang == Language.AZ) "Bəli" else "Yes", 
                        color = NeonRed, 
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showHomeConfirmDialog = false }) {
                    Text(
                        if (lang == Language.AZ) "Xeyr" else "No", 
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            },
            title = {
                Text(
                    if (lang == Language.AZ) "Ana Səhifəyə Qayıt" else "Return to Home",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    if (lang == Language.AZ) "Oyundan çıxıb ana səhifəyə qayıtmaq istədiyinizdən əminsiniz?" 
                    else "Are you sure you want to exit to the home screen?",
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            containerColor = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(24.dp)
        )
    }

    ImposterTheme {
        Box(modifier = Modifier.fillMaxSize().background(dynamicBackground)) {
            AnimatedContent(
                targetState = viewModel.gameState.value,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "ScreenTransition"
            ) { targetState ->
                when (targetState) {
                    GameState.HOME -> HomeScreen(viewModel)
                    GameState.SETUP -> SetupScreen(viewModel)
                    GameState.LOADING -> LoadingScreen(viewModel)
                    GameState.SELECTION -> SelectionScreen(viewModel)
                    GameState.REVEAL -> RevealScreen(viewModel)
                    GameState.PASS_PHONE -> PassPhoneScreen(viewModel)
                    GameState.PLAYING -> PlayScreen(viewModel)
                    GameState.VOTING_RESULTS -> VotingResultsScreen(viewModel)
                    GameState.EJECT_ANIMATION -> EjectAnimationScreen(viewModel)
                    GameState.RESULT -> ResultScreen(viewModel)
                    GameState.CREDITS -> CreditsScreen(viewModel)
                    GameState.SCOREBOARD -> ScoreboardScreen(viewModel)
                    GameState.CUSTOM_WORDS -> CustomWordsScreen(viewModel)
                }
            }

            if (viewModel.gameState.value != GameState.HOME) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(top = 16.dp, end = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { showHomeConfirmDialog = true },
                        modifier = Modifier
                            .size(44.dp)
                            .background(GlassBackground, CircleShape)
                            .border(1.dp, GlassBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Return to Home",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

// --- 1. Home Screen (Decluttered & Ultra-Clean) ---

@Composable
fun ScreenshotCarousel(modifier: Modifier = Modifier) {
    val screenshots = listOf(
        R.drawable.ss_1, R.drawable.ss_2, R.drawable.ss_3, R.drawable.ss_4,
        R.drawable.ss_5, R.drawable.ss_6, R.drawable.ss_7, R.drawable.ss_8,
        R.drawable.ss_9, R.drawable.ss_10, R.drawable.ss_11, R.drawable.ss_12,
        R.drawable.ss_13, R.drawable.ss_14
    )
    var selectedResId by remember { mutableStateOf<Int?>(null) }

    if (selectedResId != null) {
        Dialog(onDismissRequest = { selectedResId = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.95f))
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = selectedResId!!),
                        contentDescription = "Full Screenshot",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 450.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = { selectedResId = null }) {
                        Text("BAĞLA / CLOSE", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(screenshots) { resId ->
            Box(
                modifier = Modifier
                    .size(64.dp, 100.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(GlassBackground)
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                    .clickable { selectedResId = resId }
            ) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    var showHowToPlay by remember { mutableStateOf(false) }

    if (showHowToPlay) {
        AlertDialog(
            onDismissRequest = { showHowToPlay = false },
            confirmButton = {
                TextButton(onClick = { showHowToPlay = false }) {
                    Text("OK", color = accentColor, fontWeight = FontWeight.Bold)
                }
            },
            title = { Text(t("how_to_play", lang), color = accentColor, fontWeight = FontWeight.Bold) },
            text = { Text(t("hint_text", lang), color = Color.White) },
            containerColor = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Clean Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showHowToPlay = true },
                modifier = Modifier.background(GlassBackground, CircleShape)
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { viewModel.gameState.value = GameState.SCOREBOARD },
                    modifier = Modifier.background(GlassBackground, CircleShape)
                ) {
                    Icon(Icons.Default.Leaderboard, contentDescription = null, tint = Color.White)
                }
                IconButton(
                    onClick = { viewModel.gameState.value = GameState.CUSTOM_WORDS },
                    modifier = Modifier.background(GlassBackground, CircleShape)
                ) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = Color.White)
                }
                ColorPicker(viewModel)
            }
        }

        // Hero Branding
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "IMPOSTER",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 44.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Black
                )
            )
            Text(
                text = "WHO?",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 52.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            )
        }

        // Screenshots Carousel (Small Format)
        ScreenshotCarousel()

        // Language Segmented Selector dynamically controlled by accentColor
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(GlassBackground)
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageButton("AZ", viewModel.language.value == Language.AZ, accentColor) {
                viewModel.language.value = Language.AZ
            }
            LanguageButton("EN", viewModel.language.value == Language.EN, accentColor) {
                viewModel.language.value = Language.EN
            }
        }

        // Primary Action Button
        Button(
            onClick = { viewModel.gameState.value = GameState.SETUP },
            modifier = Modifier.height(64.dp).fillMaxWidth(0.85f),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                t("start", lang), 
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

@Composable
fun ColorPicker(viewModel: GameViewModel) {
    val colors = listOf(0xFF9D50BB, 0xFF00E5FF, 0xFFFF3D00, 0xFF00E676, 0xFFFFBB33)
    var showColors by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { showColors = !showColors },
            modifier = Modifier.background(GlassBackground, CircleShape)
        ) {
            Icon(Icons.Default.Palette, contentDescription = null, tint = Color(viewModel.accentColor.longValue))
        }
        if (showColors) {
            Row(
                modifier = Modifier
                    .offset(y = 52.dp, x = (-120).dp)
                    .background(Color.Black.copy(0.9f), CircleShape)
                    .border(1.dp, GlassBorder, CircleShape)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(color))
                            .clickable {
                                viewModel.accentColor.longValue = color
                                showColors = false
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageButton(text: String, isSelected: Boolean, accentColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(54.dp, 44.dp)
            .clip(CircleShape)
            .background(if (isSelected) accentColor else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, 
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f), 
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 15.sp
        )
    }
}

@Composable
fun DifficultyButton(difficulty: Difficulty, isSelected: Boolean, lang: Language, modifier: Modifier, onClick: () -> Unit) {
    val color = when(difficulty) {
        Difficulty.EASY -> Color(0xFF4CAF50)
        Difficulty.MEDIUM -> Color(0xFFFFC107)
        Difficulty.HARD -> Color(0xFFF44336)
    }
    val text = when(difficulty) {
        Difficulty.EASY -> if (lang == Language.AZ) "ASAN" else "EASY"
        Difficulty.MEDIUM -> if (lang == Language.AZ) "ORTA" else "MEDIUM"
        Difficulty.HARD -> if (lang == Language.AZ) "ÇƏTİN" else "HARD"
    }

    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) color else color.copy(alpha = 0.12f))
            .border(1.dp, if (isSelected) color else color.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, 
            color = if (isSelected) Color.White else color, 
            fontSize = 13.sp, 
            fontWeight = FontWeight.Bold
        )
    }
}

// --- 2. Setup Screen ---

@Composable
fun SetupScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showModeDialog by remember { mutableStateOf(false) }

    if (showCategoryDialog) CategoryDialog(viewModel) { showCategoryDialog = false }
    if (showModeDialog) ModeDialog(viewModel) { showModeDialog = false }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Setup Top Header with Back Arrow
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.gameState.value = GameState.HOME },
                modifier = Modifier.background(GlassBackground, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(t("players", lang), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp)) // balance layout
        }

        // Difficulty Selection Pills
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DifficultyButton(Difficulty.EASY, viewModel.difficulty.value == Difficulty.EASY, lang, Modifier.weight(1f)) { viewModel.difficulty.value = Difficulty.EASY }
            DifficultyButton(Difficulty.MEDIUM, viewModel.difficulty.value == Difficulty.MEDIUM, lang, Modifier.weight(1f)) { viewModel.difficulty.value = Difficulty.MEDIUM }
            DifficultyButton(Difficulty.HARD, viewModel.difficulty.value == Difficulty.HARD, lang, Modifier.weight(1f)) { viewModel.difficulty.value = Difficulty.HARD }
        }

        GlassCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), 
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(viewModel.players) { player ->
                    PlayerItem(
                        player = player,
                        viewModel = viewModel,
                        onNameChange = { viewModel.updatePlayerName(player, it) },
                        onRemove = { viewModel.removePlayer(player) }
                    )
                }
                item {
                    TextButton(onClick = { viewModel.addPlayer() }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("add_player", lang), color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Discussion Timer Control
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(t("timer", lang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(0, 30, 60, 90).forEach { sec ->
                    val isSelected = viewModel.timerDurationSeconds.intValue == sec
                    val label = if (sec == 0) "OFF" else "${sec}s"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) accentColor else GlassBackground)
                            .clickable { viewModel.timerDurationSeconds.intValue = sec }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SelectionCard(
                t("categories", lang), 
                if (viewModel.selectedCategories.size == 1) translateCategory(viewModel.selectedCategories.first(), lang) 
                else "${viewModel.selectedCategories.size} ${t("categories", lang)}", 
                Modifier.weight(1f),
                accentColor
            ) {
                showCategoryDialog = true
            }
            SelectionCard(
                t("modes", lang), 
                t("mode_${viewModel.gameMode.value.name.lowercase()}", lang), 
                Modifier.weight(1f),
                accentColor
            ) {
                showModeDialog = true
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.setupGame() },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(t("start_game", lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlayerItem(player: Player, viewModel: GameViewModel, onNameChange: (String) -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(player.color))
                .clickable { viewModel.updatePlayerColor(player) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        TextField(
            value = player.name,
            onValueChange = onNameChange,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(player.color)
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = null, tint = NeonRed)
        }
    }
}

@Composable
fun SelectionCard(title: String, value: String, modifier: Modifier, accentColor: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.labelLarge, color = Color.White.copy(0.5f))
            Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp), color = Color.White, maxLines = 1)
        }
    }
}

@Composable
fun LoadingScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    
    LaunchedEffect(Unit) {
        delay(1500)
        viewModel.startSelection()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = accentColor, strokeWidth = 6.dp, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text(t("loading", lang), color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun SelectionScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    var text by remember { mutableStateOf(t("selecting", lang)) }
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500)
        text = t("ready", lang)
        isReady = true
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .border(2.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                color = if (isReady) NeonCyan else Color.White,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(80.dp))
        
        if (isReady) {
            Button(
                onClick = { viewModel.startReveal() },
                modifier = Modifier.height(64.dp).fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.White),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(t("start", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp))
            }
        }
    }
}

// --- 3. Reveal Screen ---

@Composable
fun RevealScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    val currentIndex = viewModel.currentPlayerIndex.intValue
    val players = viewModel.players
    val revealOrder = viewModel.revealOrder
    
    if (currentIndex >= revealOrder.size || currentIndex >= players.size) {
        LaunchedEffect(Unit) { 
            viewModel.finalizeDiscussionStarter()
            viewModel.gameState.value = GameState.PLAYING 
        }
        return
    }
    
    val playerIndex = revealOrder[currentIndex]
    val player = players[playerIndex]
    val playerColor = Color(player.color)
    var isHolding by remember(currentIndex) { mutableStateOf(false) }
    var hasPeekedOnce by remember(currentIndex) { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isHolding) 1.05f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "HoldScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            t("reveal_title", lang), 
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(0.6f)
        )
        Text(
            text = player.name, 
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
            color = playerColor,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Box(
            modifier = Modifier
                .size(280.dp, 380.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .pointerInput(currentIndex) { 
                    detectTapGestures(
                        onPress = {
                            isHolding = true
                            hasPeekedOnce = true
                            try {
                                awaitRelease()
                            } finally {
                                isHolding = false
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(playerColor.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                    .border(2.dp, playerColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            )

            GlassCard(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = if (isHolding) Color.Black.copy(alpha = 0.9f) else Color.Transparent
            ) {
                if (isHolding) {
                    RevealContent(player)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            t("hold_to_reveal", lang), 
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                            color = playerColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))

        if (!isHolding && hasPeekedOnce) {
            Button(
                onClick = { viewModel.proceedToNextState() },
                modifier = Modifier.height(60.dp).fillMaxWidth(0.85f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                Text(
                    text = t("next", lang), 
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Black),
                    color = Color.White
                )
            }
        } else {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun RevealContent(player: Player) {
    val playerColor = Color(player.color)
    val wordText = player.word
    val dynamicFontSize = when {
        wordText.length > 20 -> 18.sp
        wordText.length > 14 -> 22.sp
        wordText.length > 8 -> 26.sp
        else -> 32.sp
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = wordText,
            fontSize = dynamicFontSize,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = (dynamicFontSize.value * 1.25f).sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth(0.4f)
                .background(playerColor)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = player.definition,
            fontSize = 18.sp,
            color = playerColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp
        )
    }
}

// --- 4. Pass Phone Screen ---

@Composable
fun PassPhoneScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val currentIndex = remember { viewModel.currentPlayerIndex.intValue }
    val players = viewModel.players
    val revealOrder = viewModel.revealOrder
    
    val nextPlayer = remember(currentIndex, revealOrder.size) {
        if (currentIndex + 1 < revealOrder.size) {
            val nextIdx = revealOrder[currentIndex + 1]
            players[nextIdx]
        } else null
    }

    if (nextPlayer == null) {
        LaunchedEffect(Unit) { 
            viewModel.finalizeDiscussionStarter()
            viewModel.gameState.value = GameState.PLAYING 
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            t("pass_to", lang), 
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(0.7f),
            textAlign = TextAlign.Center
        )
        
        Text(
            nextPlayer.name, 
            fontSize = 48.sp, 
            fontWeight = FontWeight.Black, 
            color = Color(nextPlayer.color),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = { viewModel.confirmPass() },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(nextPlayer.color)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(t("next", lang), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// --- 5. Play Screen with Timer ---

@Composable
fun PlayScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val voters = viewModel.players.filter { !it.isEliminated }
    val currentVoterIndex = viewModel.votingPlayerIndex.intValue
    
    if (currentVoterIndex >= voters.size) return
    
    val currentVoter = voters[currentVoterIndex]
    val starterId = viewModel.discussionStarterId.value
    val starter = viewModel.players.find { it.id == starterId }
    val starterName = starter?.name ?: "..."
    val starterColor = starter?.let { Color(it.color) } ?: Color.White

    val timerDuration = viewModel.timerDurationSeconds.intValue
    var timeLeft by remember(timerDuration) { mutableIntStateOf(timerDuration) }
    var isTimerActive by remember(timerDuration) { mutableStateOf(timerDuration > 0) }

    LaunchedEffect(isTimerActive, timeLeft) {
        if (isTimerActive && timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else if (timeLeft == 0) {
            isTimerActive = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding()) {
        GlassCard(
            modifier = Modifier.fillMaxWidth().border(2.dp, starterColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            backgroundColor = starterColor.copy(alpha = 0.1f)
        ) {
            Text(
                t("discussion", lang), 
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp, color = starterColor)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${t("starter", lang)} $starterName",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 24.sp, color = Color.White)
            )

            if (timerDuration > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Timer, 
                        contentDescription = null, 
                        tint = if (timeLeft <= 10) NeonRed else NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${timeLeft}s", 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = if (timeLeft <= 10) NeonRed else NeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${currentVoter.name}, ${t("vote_out", lang).lowercase()}", 
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
            color = Color(currentVoter.color)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.players) { player ->
                PlayerVoteCard(player, lang) { viewModel.votePlayer(player) }
            }
        }
    }
}

@Composable
fun PlayerVoteCard(player: Player, lang: Language, onVote: () -> Unit) {
    var showConfirm by remember(player.id, player.isEliminated, player.votedFor == null) { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (player.isEliminated) Color.Black.copy(alpha = 0.5f) 
                else if (showConfirm) NeonRed.copy(0.3f) 
                else GlassBackground
            )
            .border(
                1.dp, 
                if (player.isEliminated) Color.Gray 
                else if (showConfirm) NeonRed 
                else Color(player.color).copy(alpha = 0.3f), 
                RoundedCornerShape(20.dp)
            )
            .clickable(enabled = !player.isEliminated) { showConfirm = !showConfirm },
        contentAlignment = Alignment.Center
    ) {
        if (player.isEliminated) {
            Text(
                text = t("disqualified", lang), 
                color = Color.Gray, 
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        } else if (showConfirm) {
            Button(onClick = {
                showConfirm = false
                onVote()
            }, colors = ButtonDefaults.buttonColors(containerColor = NeonRed)) {
                Text("VOTE", color = Color.White)
            }
        } else {
            Text(player.name, color = Color(player.color), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VotingResultsScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    val alivePlayers = viewModel.players.filter { !it.isEliminated }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            t("voting_results", lang),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            t("who_voted_who", lang),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(alivePlayers) { voter ->
                    val votedFor = viewModel.players.find { it.id == voter.votedFor }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(voter.name, color = Color(voter.color), fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
                        Text(votedFor?.name ?: "?", color = votedFor?.let { Color(it.color) } ?: Color.White)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.finalizeElimination() },
            modifier = Modifier.height(56.dp).fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(t("confirm_elimination", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp), color = Color.White)
        }
    }
}

// --- 6. Result Screen ---

@Composable
fun ResultScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    val isImposterVictory = viewModel.players.any { !it.isEliminated && it.role != Role.CIVILIAN }
    
    var showImposter by remember { mutableStateOf(false) }
    var showWord by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!isImposterVictory) {
            ConfettiEffect()
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isImposterVictory) t("defeat", lang) else t("victory", lang),
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                color = if (isImposterVictory) NeonRed else NeonCyan,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(40.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { showImposter = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if(showImposter) NeonRed else GlassBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(t("imposter_reveal", lang), style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
                Button(
                    onClick = { showWord = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if(showWord) NeonCyan else GlassBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(t("word_reveal", lang), style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(modifier = Modifier.fillMaxWidth().height(150.dp), backgroundColor = Color.White.copy(alpha = 0.05f)) {
                if (showImposter) {
                    val imposters = viewModel.players.filter { it.role != Role.CIVILIAN }
                    Text("İMPOSTERS:", fontWeight = FontWeight.Bold, color = NeonRed, fontSize = 14.sp)
                    imposters.forEach { p ->
                        Text(p.name, color = Color(p.color), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp))
                    }
                }
                
                if (showWord) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("SECRET WORD:", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 14.sp)
                    val word = viewModel.players.find { it.role == Role.CIVILIAN }?.word ?: "???"
                    Text(word, color = Color.White, style = MaterialTheme.typography.displayLarge.copy(fontSize = 24.sp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { viewModel.setupGame() },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(t("play_again", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 16.sp))
                }

                Button(
                    onClick = { viewModel.gameState.value = GameState.HOME },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f), contentColor = Color.White),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(t("back_home", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 16.sp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.gameState.value = GameState.CREDITS },
                modifier = Modifier.height(48.dp).fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(t("credits", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp), color = Color.White)
            }
        }
    }
}

// --- 7. Scoreboard Screen ---

@Composable
fun ScoreboardScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.gameState.value = GameState.HOME },
                modifier = Modifier.background(GlassBackground, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(t("scoreboard", lang), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }

        GlassCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.players.sortedByDescending { it.civilianWins + it.imposterWins }) { player ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(player.color))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(player.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    "Oyunlar: ${player.gamesPlayed} | Civ W: ${player.civilianWins} | Imp W: ${player.imposterWins}",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Text(
                            "${player.civilianWins + player.imposterWins} Qələbə",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// --- 8. Custom Words Screen ---

@Composable
fun CustomWordsScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)

    var word1Text by remember { mutableStateOf("") }
    var word2Text by remember { mutableStateOf("") }
    var hintText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.gameState.value = GameState.HOME },
                modifier = Modifier.background(GlassBackground, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(t("custom_words", lang), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = word1Text,
                onValueChange = { word1Text = it },
                label = { Text("Mülki Söz (Word 1)", color = Color.White.copy(alpha = 0.7f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = GlassBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = word2Text,
                onValueChange = { word2Text = it },
                label = { Text("Casus Sözü (Word 2)", color = Color.White.copy(alpha = 0.7f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = GlassBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = hintText,
                onValueChange = { hintText = it },
                label = { Text("Mövzu İpucu (Hint)", color = Color.White.copy(alpha = 0.7f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = GlassBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (word1Text.isNotBlank() && word2Text.isNotBlank()) {
                        viewModel.addCustomPair(word1Text, word2Text, hintText)
                        word1Text = ""
                        word2Text = ""
                        hintText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(t("add_word_pair", lang), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (WordDatabase.customPairs.isEmpty()) {
                Text(
                    "Hələ ki heç bir xüsusi söz əlavə olunmayıb.",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(WordDatabase.customPairs) { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${pair.word1Az} / ${pair.word2Az}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("İpucu: ${pair.hintAz}", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreditsScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = t("credits", lang),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp),
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(t("developed_by", lang), style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(t("version", lang), color = Color.White.copy(alpha = 0.5f))
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Button(
            onClick = { viewModel.gameState.value = GameState.HOME },
            modifier = Modifier.height(56.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(t("back_home", lang), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp))
        }
    }
}

@Composable
fun ConfettiEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "Confetti")
    val yOffset by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Y"
    )

    val particles = remember {
        val random = Random(42)
        List(20) {
            Triple(random.nextFloat(), 0.4f + random.nextFloat() * 0.6f, Color(random.nextInt()))
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { (xRatio, speedMult, color) ->
            drawCircle(
                color = color.copy(alpha = 0.6f),
                radius = 5f,
                center = Offset(xRatio * size.width, (yOffset * speedMult) % size.height)
            )
        }
    }
}

// --- Dialogs ---

fun translateCategory(category: String, language: Language): String {
    val az = mapOf(
        "General" to "Ümumi / Asan",
        "School" to "Məktəb & Təhsil",
        "Food" to "Qida & İçki",
        "Animal" to "Heyvanlar",
        "Tech" to "Texnologiya & İT",
        "Hospital" to "Tibb & Sağlamlıq",
        "City" to "Şəhərlər",
        "Country" to "Ölkələr",
        "Space" to "Kosmos",
        "Culture" to "Mədəniyyət & İncəsənət",
        "Profession" to "Peşələr",
        "Sport" to "İdman",
        "Vehicle" to "Nəqliyyat",
        "Everyday" to "Məişət Əşyaları",
        "Custom" to "Xüsusi (Custom)"
    )
    return (if (language == Language.AZ) az[category] else category) ?: category
}

@Composable
fun CategoryDialog(viewModel: GameViewModel, onDismiss: () -> Unit) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK", color = accentColor, fontWeight = FontWeight.Bold) }
        },
        title = { Text(t("categories", lang), fontWeight = FontWeight.Bold, color = accentColor) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(350.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WordDatabase.categories.keys.toList() + if (WordDatabase.customPairs.isNotEmpty()) listOf("Custom") else emptyList()) { category ->
                    val isSelected = viewModel.selectedCategories.contains(category)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) accentColor else GlassBackground)
                            .clickable {
                                if (isSelected) {
                                    if (viewModel.selectedCategories.size > 1) {
                                        viewModel.selectedCategories.remove(category)
                                    }
                                } else {
                                    viewModel.selectedCategories.add(category)
                                }
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            translateCategory(category, lang), 
                            color = if (isSelected) Color.White else Color.White.copy(0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ModeDialog(viewModel: GameViewModel, onDismiss: () -> Unit) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK", color = accentColor, fontWeight = FontWeight.Bold) }
        },
        title = { Text(t("modes", lang), fontWeight = FontWeight.Bold, color = accentColor) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(GameMode.entries.toList()) { mode ->
                    val isSelected = viewModel.gameMode.value == mode
                    val modeKey = "mode_${mode.name.lowercase()}"
                    val descKey = "${modeKey}_desc"
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) accentColor else GlassBackground)
                            .clickable {
                                viewModel.gameMode.value = mode
                                onDismiss()
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            t(modeKey, lang), 
                            color = Color.White, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            t(descKey, lang), 
                            color = Color.White.copy(0.7f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun EjectAnimationScreen(viewModel: GameViewModel) {
    val lang = viewModel.language.value
    val accentColor = Color(viewModel.accentColor.longValue)
    val eliminated = viewModel.lastEliminatedPlayer.value
    val isImp = eliminated != null && eliminated.role != Role.CIVILIAN
    val name = eliminated?.name ?: "Someone"
    val color = eliminated?.let { Color(it.color) } ?: Color.White

    val infiniteTransition = rememberInfiniteTransition(label = "SpaceFloat")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Float"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer { translationX = floatOffset }
                .clip(CircleShape)
                .background(color)
                .border(4.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1).uppercase(), fontSize = 64.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "$name was ${if (isImp) "" else "not "}an Imposter.",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
            color = if (isImp) NeonRed else NeonCyan,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = { viewModel.proceedAfterEject() },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(t("next", lang), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val app = (context.applicationContext as? Application) ?: Application()
    val vm = remember { GameViewModel(app) }
    HomeScreen(vm)
}

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    val context = LocalContext.current
    val app = (context.applicationContext as? Application) ?: Application()
    val vm = remember { GameViewModel(app) }
    SetupScreen(vm)
}
