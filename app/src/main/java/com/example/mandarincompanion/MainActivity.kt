package com.example.mandarincompanion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Ink = Color(0xFF173B4D)
private val Teal = Color(0xFF218C83)
private val Pale = Color(0xFFF3F7F6)
private val SoftTeal = Color(0xFFE2F1EE)
private val SoftBlue = Color(0xFFEAF0F5)
private val Good = Color(0xFF287A53)
private val Bad = Color(0xFFB34747)

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null
    private var ttsAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.SIMPLIFIED_CHINESE)
                ttsAvailable = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
                tts?.setSpeechRate(0.88f)
            }
        }
        setContent {
            MaterialTheme {
                MandarinApp(
                    speak = { phrase ->
                        if (ttsAvailable) {
                            tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "mandarin-${phrase.hashCode()}")
                        } else {
                            Toast.makeText(this, "Install a Chinese (Mandarin) voice in Android Text-to-Speech settings.", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

private data class DialogueLine(val chinese: String, val pinyin: String, val english: String)
private data class Dialogue(val title: String, val scene: String, val lines: List<DialogueLine>)
private data class Word(
    val hanzi: String,
    val pinyin: String,
    val meaning: String,
    val example: String,
    val examplePinyin: String,
    val exampleEnglish: String
)
private data class GrammarPoint(
    val title: String,
    val explanation: String,
    val pattern: String,
    val examples: List<DialogueLine>
)
private data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String,
    val exerciseType: String
)
private data class LessonContent(
    val number: Int,
    val title: String,
    val hanziTitle: String,
    val textbookPages: String,
    val workbookPages: String,
    val dialogues: List<Dialogue>,
    val words: List<Word>,
    val grammar: List<GrammarPoint>,
    val questions: List<QuizQuestion>,
    val pronunciation: String
)

private val lessons = listOf(
    LessonContent(
        number = 1,
        title = "September is the best time to visit Beijing",
        hanziTitle = "九月去北京旅游最好",
        textbookPages = "PDF pages 17–24",
        workbookPages = "PDF pages 9–18",
        dialogues = listOf(
            Dialogue("1 · Choosing a time to visit", "In the school · Textbook dialogue 01-1", listOf(
                DialogueLine("我 要 去 北京 旅游，你 觉得 什么 时候 去 最 好？", "Wǒ yào qù Běijīng lǚyóu, nǐ juéde shénme shíhou qù zuì hǎo?", "I want to travel to Beijing. When do you think is the best time?"),
                DialogueLine("九 月 去 北京 旅游 最 好。", "Jiǔ yuè qù Běijīng lǚyóu zuì hǎo.", "September is the best time to visit Beijing."),
                DialogueLine("为 什么？", "Wèi shénme?", "Why?"),
                DialogueLine("九 月 的 北京 天气 不 冷 也 不 热。", "Jiǔ yuè de Běijīng tiānqì bù lěng yě bù rè.", "The weather in Beijing in September is neither cold nor hot.")
            )),
            Dialogue("2 · Favourite sport", "Looking at a picture · Textbook dialogue 01-2", listOf(
                DialogueLine("你 喜欢 什么 运动？", "Nǐ xǐhuan shénme yùndòng?", "What sport do you like?"),
                DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
                DialogueLine("下 午 我们 一起 去 踢 足球 吧。", "Xiàwǔ wǒmen yìqǐ qù tī zúqiú ba.", "Let's go and play football together this afternoon."),
                DialogueLine("好 啊！", "Hǎo a!", "Great!")
            )),
            Dialogue("3 · Buying chairs", "At home · Textbook dialogue 01-3", listOf(
                DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Should we buy a few new chairs?"),
                DialogueLine("好 啊。什么 时候 去 买？", "Hǎo a. Shénme shíhou qù mǎi?", "All right. When shall we buy them?"),
                DialogueLine("明天 下 午 怎么 样？", "Míngtiān xiàwǔ zěnmeyàng?", "How about tomorrow afternoon?"),
                DialogueLine("你 明天 几 点 能 回来？", "Nǐ míngtiān jǐ diǎn néng huílai?", "What time can you come back tomorrow?"),
                DialogueLine("三 点 多。", "Sān diǎn duō.", "A little after three.")
            )),
            Dialogue("4 · The cat Huahua", "At home · Textbook dialogue 01-4", listOf(
                DialogueLine("桌子 下 面 有 个 猫。", "Zhuōzi xiàmiàn yǒu ge māo.", "There is a cat under the table."),
                DialogueLine("那 是 我 的 猫，它 叫 花花。", "Nà shì wǒ de māo, tā jiào Huāhua.", "That is my cat. Its name is Huahua."),
                DialogueLine("它 很 漂亮。", "Tā hěn piàoliang.", "It is beautiful."),
                DialogueLine("是 啊，我 觉得 它 的 眼睛 最 漂亮。", "Shì a, wǒ juéde tā de yǎnjing zuì piàoliang.", "Yes. I think its eyes are the most beautiful."),
                DialogueLine("它 多 大 了？", "Tā duō dà le?", "How old is it?"),
                DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
            ))
        ),
        words = listOf(
            Word("旅游", "lǚyóu", "to travel; to take a trip", "我 要 去 北京 旅游。", "Wǒ yào qù Běijīng lǚyóu.", "I want to travel to Beijing."),
            Word("觉得", "juéde", "to think; to feel", "你 觉得 什么 时候 去 最 好？", "Nǐ juéde shénme shíhou qù zuì hǎo?", "When do you think is the best time?"),
            Word("最", "zuì", "most; the -est", "我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
            Word("为什么", "wèishénme", "why", "为 什么？", "Wèi shénme?", "Why?"),
            Word("也", "yě", "also; too", "天 气 不 冷 也 不 热。", "Tiānqì bù lěng yě bù rè.", "The weather is neither cold nor hot."),
            Word("运动", "yùndòng", "sport; to exercise", "你 喜欢 什么 运动？", "Nǐ xǐhuan shénme yùndòng?", "What sport do you like?"),
            Word("踢足球", "tī zúqiú", "to play football", "我 喜欢 踢 足球。", "Wǒ xǐhuan tī zúqiú.", "I like playing football."),
            Word("一起", "yìqǐ", "together", "我们 一起 去。", "Wǒmen yìqǐ qù.", "Let's go together."),
            Word("要", "yào", "to want to; would like to", "我们 要 不 要 买 新 椅子？", "Wǒmen yào bu yào mǎi xīn yǐzi?", "Should we buy new chairs?"),
            Word("新", "xīn", "new", "新 的 椅子。", "Xīn de yǐzi.", "New chairs."),
            Word("它", "tā", "it (for an animal or thing)", "它 叫 花花。", "Tā jiào Huāhua.", "Its name is Huahua."),
            Word("眼睛", "yǎnjing", "eye; eyes", "它 的 眼睛 最 漂亮。", "Tā de yǎnjing zuì piàoliang.", "Its eyes are the most beautiful.")
        ),
        grammar = listOf(
            GrammarPoint("要 · wish or intention", "要 is placed before a verb to say that someone wants or intends to do something. The textbook contrasts it with 不想.", "Subject + 要 + verb  /  Subject + 不想 + verb", listOf(
                DialogueLine("我 要 去 北京。", "Wǒ yào qù Běijīng.", "I want to go to Beijing."),
                DialogueLine("我 不 想 买 椅子。", "Wǒ bù xiǎng mǎi yǐzi.", "I don't want to buy chairs.")
            )),
            GrammarPoint("最 · highest degree", "最 marks the top degree within a group or in a particular respect. It commonly appears before a verb or adjective phrase.", "Subject + 最 + verb / adjective", listOf(
                DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
                DialogueLine("它 的 眼睛 最 漂亮。", "Tā de yǎnjing zuì piàoliang.", "Its eyes are the most beautiful.")
            )),
            GrammarPoint("几 and 多 · approximate quantities", "几 before a measure word indicates an indefinite small number. 多 expresses an approximate quantity: after the measure word with a number below ten, or before the measure word with numbers above ten.", "几 + measure word + noun  /  六个多月  /  十多个月", listOf(
                DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Should we buy a few new chairs?"),
                DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
            ))
        ),
        questions = listOf(
QuizQuestion("According to Dialogue 1, when is the best time to visit Beijing?", listOf("January", "June", "September", "December"), 2, "九月去北京旅游最好。 September is best.", "Textbook lesson 1, dialogue 01-1"),
    QuizQuestion("Why is September recommended?", listOf("It is neither cold nor hot", "It always snows", "The city is empty", "It is the cheapest month"), 0, "九月的北京天气不冷也不热。 The weather is neither cold nor hot.", "Textbook lesson 1, dialogue 01-1"),
    QuizQuestion("What do the speakers plan to do this afternoon?", listOf("Go travelling", "Play football together", "Buy a cat", "Study English"), 1, "下午我们一起去踢足球吧 — let's go and play football together this afternoon.", "Textbook lesson 1, comprehension exercise 2"),
    QuizQuestion("When do they suggest buying the new chairs?", listOf("This evening", "Tomorrow morning", "Tomorrow afternoon", "Next week"), 2, "明天下午怎么样？ — What about tomorrow afternoon?", "Textbook lesson 1, comprehension exercise 3"),
    QuizQuestion("Where is Huahua?", listOf("On the chair", "Under the table", "At school", "In the garden"), 1, "桌子下面有个猫 — there is a cat under the table.", "Textbook lesson 1, comprehension exercise 4"),
    QuizQuestion("How old is Huahua?", listOf("Six days", "Exactly six months", "More than six months", "Six years"), 2, "六个多月 means a little over six months.", "Textbook lesson 1, comprehension exercise 5"),
    QuizQuestion("What does 觉得 (juéde) mean?", listOf("to travel", "to think / to feel", "together", "new"), 1, "觉得 means to think or to feel.", "Textbook lesson 1, new word 2"),
    QuizQuestion("Complete: 王方（___）买一个新杯子。", listOf("为什么", "要", "眼睛", "也"), 1, "要 is placed before a verb to express wanting to do something.", "Workbook lesson 1, fill-in exercise 21"),
    QuizQuestion("Complete: 昨天你（___）没来我家吃饭？", listOf("最", "新", "为什么", "一起"), 2, "为什么 asks 'why'.", "Workbook lesson 1, fill-in exercise 22"),
    QuizQuestion("Complete: 我（___）这个衣服太大了。", listOf("觉得", "旅游", "踢足球", "眼睛"), 0, "觉得 means 'I think / feel that…'.", "Workbook lesson 1, fill-in exercise 23"),
    QuizQuestion("Complete: 我的小猫两岁多了，大卫的小猫（___）两岁多了。", listOf("为什么", "也", "最", "新"), 1, "也 means 'also / too'.", "Workbook lesson 1, fill-in exercise 24"),
    QuizQuestion("Complete: 王老师（___）喜欢吃苹果。", listOf("最", "眼睛", "一起", "猫"), 0, "最 expresses the highest degree: likes something the most.", "Workbook lesson 1, fill-in exercise 25"),
    QuizQuestion("What does 六个多月 mean?", listOf("exactly six days", "less than six months", "more than six months", "six years"), 2, "For numbers below ten, 多 follows the measure word: 六个多月.", "Textbook lesson 1, dialogue 01-4"),
    QuizQuestion("What is the usual negative form given for 要 in this lesson?", listOf("不想", "没有", "不在", "不会"), 0, "The textbook says the negative form is usually 不想.", "Textbook lesson 1, grammar note 1"),
    QuizQuestion("True or false: The speaker says they can dance, but not very well. The statement 'I dance extremely well' matches that meaning.", listOf("True", "False"), 1, "不怎么样 means 'not very well', so the statement is false.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'Miss Wang's kitten is at my house; my kitten is at my mother's house.' The speaker says they have a kitten.", listOf("True", "False"), 0, "The sentence explicitly says 我的小猫在我妈妈家 — my kitten is at my mother's home.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'I don't want to buy a table; I want to buy some new chairs.' This means I want to go to the store to buy a table.", listOf("True", "False"), 1, "The speaker wants chairs, not a table.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'I heard you are learning to cook Chinese food. Let's cook together.' They want to cook Chinese food together.", listOf("True", "False"), 0, "我们一起做吧 means 'let's do it together'.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'August is hot in Beijing; September's weather is good. Come in September.' August is the best time to visit Beijing.", listOf("True", "False"), 1, "The recommendation is September, not August.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("Match the reply: 桌子上有一本新书。那是你的书吗？", listOf("不是我的，我没买书。", "三岁多。", "我没时间，明天吧。", "他已经走了，十分钟后能到。"), 0, "The reply 'It's not mine; I didn't buy a book' answers whether the book belongs to the speaker.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 王老师什么时候能到？", listOf("我不想开车。", "他已经走了，十分钟后能到。", "三岁多。", "不是我的。"), 1, "The reply gives an arrival estimate: he has left and can arrive in ten minutes.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 这个小猫很漂亮。它多大了？", listOf("我没时间，明天吧。", "三岁多。", "不是我的。", "我觉得坐出租车很好。"), 1, "三岁多 answers the question about the cat's age.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 你为什么不开车去学校？", listOf("我不想开，我觉得坐出租车很好。", "他已经走了。", "三岁多。", "不是我的。"), 0, "The reply gives a reason for not driving: the speaker prefers taking a taxi.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 他们都去踢足球了，你去不去？", listOf("不是我的。", "三岁多。", "我没时间，明天吧。", "他已经走了。"), 2, "我没时间，明天吧 means 'I'm busy; how about tomorrow?'.", "Workbook lesson 1, matching exercise")
        ),
        pronunciation = "The textbook introduces stress patterns in two-syllable words. Use Mandarin TTS to listen to examples such as 旅游 lǚyóu, 可能 kěnéng, 时候 shíhou and 你们 nǐmen."
    ),
    LessonContent(
        number = 2,
        title = "I get up at six every day",
        hanziTitle = "我每天六点起床",
        textbookPages = "PDF pages 25–32",
        workbookPages = "PDF pages 19–28",
        dialogues = listOf(
            Dialogue("1 · Morning exercise", "On the playground · Textbook dialogue 02-1", listOf(
                DialogueLine("你很少生病，是不是喜欢运动？", "Nǐ hěn shǎo shēngbìng, shì bu shì xǐhuan yùndòng?", "You rarely get sick. Is it because you like sports?"),
                DialogueLine("是啊，我每天早上都要出去跑步。", "Shì a, wǒ měitiān zǎoshang dōu yào chūqù pǎobù.", "Yes. I go out jogging every morning."),
                DialogueLine("你每天几点起床？", "Nǐ měitiān jǐ diǎn qǐchuáng?", "What time do you get up every day?"),
                DialogueLine("我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day.")
            )),
            Dialogue("2 · At the hospital", "In the hospital · Textbook dialogue 02-2", listOf(
                DialogueLine("吃药了吗？现在身体怎么样？", "Chī yào le ma? Xiànzài shēntǐ zěnmeyàng?", "Have you taken your medicine? How do you feel now?"),
                DialogueLine("吃了。现在好多了。", "Chī le. Xiànzài hǎo duō le.", "Yes. I feel much better now."),
                DialogueLine("什么时候能出院？", "Shénme shíhou néng chūyuàn?", "When can you leave the hospital?"),
                DialogueLine("医生说下个星期。", "Yīshēng shuō xià ge xīngqī.", "The doctor says next week.")
            )),
            Dialogue("3 · Asking about David", "On the playground · Textbook dialogue 02-3", listOf(
                DialogueLine("大卫今年多大？", "Dàwèi jīnnián duō dà?", "How old is David this year?"),
                DialogueLine("二十多岁。", "Èrshí duō suì.", "A little over twenty."),
                DialogueLine("他多高？", "Tā duō gāo?", "How tall is he?"),
                DialogueLine("一米八几。", "Yì mǐ bā jǐ.", "A little over 1.8 metres."),
                DialogueLine("你怎么知道这么多啊？", "Nǐ zěnme zhīdào zhème duō a?", "How do you know so much?"),
                DialogueLine("他是我同学。", "Tā shì wǒ tóngxué.", "He is my classmate.")
            )),
            Dialogue("4 · A busy teacher", "In the room · Textbook dialogue 02-4", listOf(
                DialogueLine("张老师星期六也不休息啊？", "Zhāng lǎoshī xīngqīliù yě bù xiūxi a?", "Does Teacher Zhang not rest even on Saturdays?"),
                DialogueLine("是啊，他这几天很忙，没有时间休息。", "Shì a, tā zhè jǐ tiān hěn máng, méiyǒu shíjiān xiūxi.", "Yes. He has been very busy these days and has no time to rest."),
                DialogueLine("那会很累吧？", "Nà huì hěn lèi ba?", "That must be tiring, right?"),
                DialogueLine("他每天回来都很累。", "Tā měitiān huílai dōu hěn lèi.", "He comes home tired every day.")
            ))
        ),
        words = listOf(
            Word("生病", "shēngbìng", "to fall ill; to be sick", "他很少生病。", "Tā hěn shǎo shēngbìng.", "He rarely gets sick."),
            Word("每", "měi", "every; each", "我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day."),
            Word("早上", "zǎoshang", "morning", "我每天早上跑步。", "Wǒ měitiān zǎoshang pǎobù.", "I jog every morning."),
            Word("跑步", "pǎobù", "to run; to jog", "我出去跑步。", "Wǒ chūqù pǎobù.", "I go out jogging."),
            Word("起床", "qǐchuáng", "to get up; to get out of bed", "我六点起床。", "Wǒ liù diǎn qǐchuáng.", "I get up at six."),
            Word("药", "yào", "medicine; drug", "你吃药了吗？", "Nǐ chī yào le ma?", "Have you taken your medicine?"),
            Word("身体", "shēntǐ", "body; health", "现在身体怎么样？", "Xiànzài shēntǐ zěnmeyàng?", "How is your health now?"),
            Word("出院", "chūyuàn", "to leave hospital; to be discharged", "下个星期能出院。", "Xià ge xīngqī néng chūyuàn.", "I can leave hospital next week."),
            Word("高", "gāo", "tall; high", "他多高？", "Tā duō gāo?", "How tall is he?"),
            Word("米", "mǐ", "metre", "他一米八几。", "Tā yì mǐ bā jǐ.", "He is a little over 1.8 metres."),
            Word("知道", "zhīdào", "to know", "你怎么知道？", "Nǐ zěnme zhīdào?", "How do you know?"),
            Word("休息", "xiūxi", "to rest; to take a break", "星期六也不休息。", "Xīngqīliù yě bù xiūxi.", "He does not rest even on Saturday."),
            Word("忙", "máng", "busy", "他这几天很忙。", "Tā zhè jǐ tiān hěn máng.", "He has been very busy these days."),
            Word("时间", "shíjiān", "time", "没有时间休息。", "Méiyǒu shíjiān xiūxi.", "There is no time to rest.")
        ),
        grammar = listOf(
            GrammarPoint("是不是 · confirming a guess", "Use 是不是 when you have a fairly strong idea about a situation and want the other person to confirm it. It usually appears before the predicate, but it can also appear at the beginning or end of a question.", "Statement + 是不是 + predicate？", listOf(
                DialogueLine("你很少生病，是不是喜欢运动？", "Nǐ hěn shǎo shēngbìng, shì bu shì xǐhuan yùndòng?", "You rarely get sick; is it because you like exercise?"),
                DialogueLine("我们星期一去北京，是不是？", "Wǒmen xīngqīyī qù Běijīng, shì bu shì?", "We are going to Beijing on Monday, right?")
            )),
            GrammarPoint("每 · each / every", "每 is used before a measure word or time expression to refer to every member of a group: 每天 (every day), 每年 (every year), 每个星期 (every week).", "每 + measure word / time word", listOf(
                DialogueLine("我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day."),
                DialogueLine("你每个星期六都工作吗？", "Nǐ měi ge xīngqīliù dōu gōngzuò ma?", "Do you work every Saturday?")
            )),
            GrammarPoint("多 + adjective · asking about degree", "The question word 多 comes before an adjective to ask about degree or measurement. The answer gives a number or quantity.", "Subject + 多 + adjective？", listOf(
                DialogueLine("你多大？", "Nǐ duō dà?", "How old are you?"),
                DialogueLine("他多高？", "Tā duō gāo?", "How tall is he?"),
                DialogueLine("一米八几。", "Yì mǐ bā jǐ.", "A little over 1.8 metres.")
            ))
        ),
        questions = listOf(
            QuizQuestion("What time does the speaker get up?", listOf("Five o'clock", "Six o'clock", "Seven o'clock", "Eight o'clock"), 1, "我每天六点起床。", "Workbook listening-style choice"),
            QuizQuestion("What does the speaker do every morning?", listOf("Swims", "Plays football", "Goes jogging", "Drives to work"), 2, "我每天早上都要出去跑步。", "Dialogue comprehension"),
            QuizQuestion("When can the patient leave hospital?", listOf("Tomorrow", "Next week", "Next month", "Today"), 1, "医生说下个星期。", "Dialogue comprehension"),
            QuizQuestion("How tall is David?", listOf("1.60 m", "1.70 m", "A little over 1.80 m", "2 metres"), 2, "一米八几 means a little over 1.8 metres.", "Listening-style choice"),
            QuizQuestion("Why does Teacher Zhang not rest on Saturday?", listOf("He is busy", "He is travelling", "He is ill", "He is studying Chinese"), 0, "他这几天很忙，没有时间休息。", "Dialogue comprehension"),
            QuizQuestion("In this lesson, 每 means…", listOf("Some", "Every / each", "Already", "Very"), 1, "每 is used in expressions such as 每天 and 每个星期.", "Vocabulary"),
            QuizQuestion("Choose the natural question asking someone's height.", listOf("他几高？", "他多高？", "他很高？", "他高多少吗？"), 1, "多 + adjective asks about degree: 他多高？", "Workbook grammar practice"),
            QuizQuestion("Choose the correct word: 医生说下个星期可以____。", listOf("起床", "跑步", "出院", "休息"), 2, "出院 means to be discharged from hospital.", "Workbook fill-in"),
            QuizQuestion("True or false: The speaker says Teacher Zhang comes home tired every day.", listOf("True", "False"), 0, "他每天回来都很累。", "Workbook true / false"),
            QuizQuestion("What does 是不是 do in this lesson?", listOf("Confirms a likely guess", "Marks the past tense", "Indicates possession", "Means because"), 0, "是不是 is used to ask for confirmation of a fairly confident guess.", "Grammar" )
        ),
        pronunciation = "Workbook pronunciation practice asks you to listen for stress in words and phrases. Practise hearing the three-syllable rhythm in examples such as 收音机 shōuyīnjī and 新加坡 Xīnjiāpō. Publisher audio was not included in the PDFs, so the Listen buttons use Android Mandarin TTS."
    ),
    LessonContent(
        number = 3,
        title = "The red one on the left is mine",
        hanziTitle = "左边那个红色的是我的",
        textbookPages = "PDF pages 33–40",
        workbookPages = "PDF pages 29–38",
        dialogues = listOf(
            Dialogue("1 · A watch", "In the room · Textbook dialogue 03-1", listOf(
                DialogueLine("这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?"),
                DialogueLine("不是我的，是我爸爸的。", "Bú shì wǒ de. Shì wǒ bàba de.", "No, it isn't mine. It is my father's."),
                DialogueLine("多少钱买的？", "Duōshao qián mǎi de?", "How much did it cost?"),
                DialogueLine("三千多块。", "Sānqiān duō kuài.", "More than 3,000 yuan.")
            )),
            Dialogue("2 · The newspaper", "At home · Textbook dialogue 03-2", listOf(
                DialogueLine("这是今天早上的报纸吗？", "Zhè shì jīntiān zǎoshang de bàozhǐ ma?", "Is this this morning's newspaper?"),
                DialogueLine("不是，是昨天的。", "Bú shì, shì zuótiān de.", "No, it is yesterday's."),
                DialogueLine("你听，是不是送报纸的来了？", "Nǐ tīng, shì bu shì sòng bàozhǐ de lái le?", "Listen. Is the newspaper delivery person here?"),
                DialogueLine("我看一下。不是，是送牛奶的。", "Wǒ kàn yíxià. Bú shì, shì sòng niúnǎi de.", "I'll take a look. No, it is the milk delivery person.")
            )),
            Dialogue("3 · Whose room?", "At home · Textbook dialogue 03-3", listOf(
                DialogueLine("这是谁的房间？", "Zhè shì shéi de fángjiān?", "Whose room is this?"),
                DialogueLine("这是我和我丈夫的，旁边那个小的房间是我女儿的。", "Zhè shì wǒ hé wǒ zhàngfu de, pángbiān nàge xiǎo de fángjiān shì wǒ nǚ'ér de.", "It is my husband's and mine. The small room beside it is my daughter's."),
                DialogueLine("你女儿的房间真漂亮！都是粉色的。", "Nǐ nǚ'ér de fángjiān zhēn piàoliang! Dōu shì fěnsè de.", "Your daughter's room is really beautiful! It is all pink."),
                DialogueLine("是啊，粉色是我女儿最喜欢的颜色。", "Shì a, fěnsè shì wǒ nǚ'ér zuì xǐhuan de yánsè.", "Yes. Pink is my daughter's favourite colour.")
            )),
            Dialogue("4 · The cups", "In the office · Textbook dialogue 03-4", listOf(
                DialogueLine("你看见我的杯子了吗？", "Nǐ kànjiàn wǒ de bēizi le ma?", "Have you seen my cup?"),
                DialogueLine("这里有几个杯子，哪个是你的？", "Zhèlǐ yǒu jǐ ge bēizi, nǎge shì nǐ de?", "There are several cups here. Which one is yours?"),
                DialogueLine("左边那个红色的是我的。", "Zuǒbian nàge hóngsè de shì wǒ de.", "The red one on the left is mine."),
                DialogueLine("给你。", "Gěi nǐ.", "Here you are.")
            ))
        ),
        words = listOf(
            Word("手表", "shǒubiǎo", "watch", "这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?"),
            Word("千", "qiān", "thousand", "三千多块。", "Sānqiān duō kuài.", "More than three thousand yuan."),
            Word("报纸", "bàozhǐ", "newspaper", "这是今天早上的报纸吗？", "Zhè shì jīntiān zǎoshang de bàozhǐ ma?", "Is this this morning's newspaper?"),
            Word("送", "sòng", "to send; to deliver", "送牛奶的来了。", "Sòng niúnǎi de lái le.", "The milk delivery person has arrived."),
            Word("一下", "yíxià", "briefly; once (after a verb)", "我看一下。", "Wǒ kàn yíxià.", "I'll take a look."),
            Word("牛奶", "niúnǎi", "milk", "这是送牛奶的。", "Zhè shì sòng niúnǎi de.", "This is the milk delivery person."),
            Word("房间", "fángjiān", "room", "这是谁的房间？", "Zhè shì shéi de fángjiān?", "Whose room is this?"),
            Word("丈夫", "zhàngfu", "husband", "这是我和我丈夫的。", "Zhè shì wǒ hé wǒ zhàngfu de.", "This belongs to my husband and me."),
            Word("旁边", "pángbiān", "beside; next to", "旁边那个小的房间。", "Pángbiān nàge xiǎo de fángjiān.", "The small room beside it."),
            Word("真", "zhēn", "really; indeed", "你女儿的房间真漂亮！", "Nǐ nǚ'ér de fángjiān zhēn piàoliang!", "Your daughter's room is really beautiful!"),
            Word("粉色", "fěnsè", "pink", "都是粉色的。", "Dōu shì fěnsè de.", "It is all pink."),
            Word("颜色", "yánsè", "colour", "粉色是我女儿最喜欢的颜色。", "Fěnsè shì wǒ nǚ'ér zuì xǐhuan de yánsè.", "Pink is my daughter's favourite colour."),
            Word("左边", "zuǒbian", "left side; on the left", "左边那个红色的是我的。", "Zuǒbian nàge hóngsè de shì wǒ de.", "The red one on the left is mine."),
            Word("红色", "hóngsè", "red", "红色的是我的。", "Hóngsè de shì wǒ de.", "The red one is mine.")
        ),
        grammar = listOf(
            GrammarPoint("的 phrase · leaving out the noun", "A pronoun, adjective or verb phrase followed by 的 can stand in for a noun phrase when the noun is already understood. 我的 can mean 我的杯子; 昨天的 can mean 昨天的报纸.", "Modifier + 的  →  understood noun phrase", listOf(
                DialogueLine("这本书不是我的。", "Zhè běn shū bú shì wǒ de.", "This book is not mine."),
                DialogueLine("这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?")
            )),
            GrammarPoint("一下 · a short or brief action", "一下 follows a verb to express one brief action or an attempt. The object can be omitted when the context is clear.", "Verb + 一下 (+ object)", listOf(
                DialogueLine("我看一下。", "Wǒ kàn yíxià.", "I'll have a look."),
                DialogueLine("你休息一下吧。", "Nǐ xiūxi yíxià ba.", "Have a short rest.")
            )),
            GrammarPoint("真 + adjective · exclamation", "真 before an adjective adds an exclamatory sense: really, indeed. It expresses the speaker's strong impression.", "真 + adjective", listOf(
                DialogueLine("你女儿的房间真漂亮！", "Nǐ nǚ'ér de fángjiān zhēn piàoliang!", "Your daughter's room is really beautiful!"),
                DialogueLine("今天天气真好！", "Jīntiān tiānqì zhēn hǎo!", "The weather is really nice today!")
            ))
        ),
        questions = listOf(
            QuizQuestion("How much did the watch cost?", listOf("300 yuan", "More than 3,000 yuan", "Exactly 3,000 yuan", "More than 300 yuan"), 1, "三千多块 means more than 3,000 yuan.", "Comprehension"),
            QuizQuestion("Is the newspaper from this morning?", listOf("Yes", "No, it is yesterday's", "It is tomorrow's", "The dialogue does not say"), 1, "不是，是昨天的。", "Workbook true / false"),
            QuizQuestion("Who delivered the item at the door?", listOf("The newspaper person", "The milk delivery person", "The postman", "A friend"), 1, "不是，是送牛奶的。", "Comprehension"),
            QuizQuestion("Whose room is the small room beside the couple's room?", listOf("Their son's", "Their daughter's", "Their guest's", "The husband's"), 1, "旁边那个小的房间是我女儿的。", "Comprehension"),
            QuizQuestion("What is the daughter's favourite colour?", listOf("Red", "White", "Pink", "Blue"), 2, "粉色是我女儿最喜欢的颜色。", "Vocabulary"),
            QuizQuestion("Which cup is the speaker's?", listOf("The red one on the left", "The white one on the right", "The black one", "The biggest one"), 0, "左边那个红色的是我的。", "Workbook picture-description style"),
            QuizQuestion("What does 一下 after a verb indicate?", listOf("A brief action or attempt", "A future plan", "A completed event", "A person's possession"), 0, "看一下 means to take a quick look.", "Grammar"),
            QuizQuestion("Choose the natural exclamation.", listOf("你女儿的房间很真漂亮。", "你女儿的房间真漂亮！", "真你女儿房间漂亮。", "你女儿真房间漂亮。"), 1, "真 comes before the adjective: 真漂亮.", "Workbook grammar practice"),
            QuizQuestion("What does 我的 mean in a context where 杯子 is already known?", listOf("My cup", "Your cup", "The cup's colour", "A new cup"), 0, "的 allows the understood noun 杯子 to be omitted.", "Grammar"),
            QuizQuestion("Choose the correct word for 'beside'.", listOf("左边", "旁边", "红色", "颜色"), 1, "旁边 means beside or next to.", "Vocabulary")
        ),
        pronunciation = "The textbook includes pronunciation and character-recognition sections. Listen to the dialogue words with Mandarin TTS and practise the contrasts 左边 zuǒbian / 右边 yòubian, 红色 hóngsè / 粉色 fěnsè. Character handwriting drills are intentionally omitted."
    ),
    LessonContent(
        number = 4,
        title = "He recommended me for this job",
        hanziTitle = "这个工作是他帮我介绍的",
        textbookPages = "PDF pages 41–48",
        workbookPages = "PDF pages 39–48",
        dialogues = listOf(
            Dialogue("1 · A birthday present", "In the classroom · Textbook dialogue 04-1", listOf(
                DialogueLine("生日快乐！这是送给你的！", "Shēngrì kuàilè! Zhè shì sòng gěi nǐ de!", "Happy birthday! This is for you."),
                DialogueLine("是什么？是一本书吗？", "Shì shénme? Shì yì běn shū ma?", "What is it? Is it a book?"),
                DialogueLine("对，这本书是我写的。", "Duì, zhè běn shū shì wǒ xiě de.", "Yes. This book was written by me."),
                DialogueLine("太谢谢你了！", "Tài xièxie nǐ le!", "Thank you so much!")
            )),
            Dialogue("2 · A telephone call", "At home · Textbook dialogue 04-2", listOf(
                DialogueLine("早上有你一个电话。", "Zǎoshang yǒu nǐ yí ge diànhuà.", "Someone called for you this morning."),
                DialogueLine("电话是谁打的？", "Diànhuà shì shéi dǎ de?", "Who made the phone call?"),
                DialogueLine("不知道，是儿子接的。", "Bù zhīdào, shì érzi jiē de.", "I don't know. Our son answered it."),
                DialogueLine("好，晚上我问一下儿子。", "Hǎo, wǎnshang wǒ wèn yíxià érzi.", "All right. I'll ask our son this evening.")
            )),
            Dialogue("3 · Playing football", "On the playground · Textbook dialogue 04-3", listOf(
                DialogueLine("你喜欢踢足球吗？", "Nǐ xǐhuan tī zúqiú ma?", "Do you like playing football?"),
                DialogueLine("非常喜欢。", "Fēicháng xǐhuan.", "I like it very much."),
                DialogueLine("你是什么时候开始踢足球的？", "Nǐ shì shénme shíhou kāishǐ tī zúqiú de?", "When did you start playing football?"),
                DialogueLine("我十一岁的时候开始踢足球，已经踢了十年了。", "Wǒ shíyī suì de shíhou kāishǐ tī zúqiú, yǐjīng tī le shí nián le.", "I started playing football when I was eleven. I have been playing for ten years.")
            )),
            Dialogue("4 · A job recommendation", "In the company · Textbook dialogue 04-4", listOf(
                DialogueLine("你在这儿工作多长时间了？", "Nǐ zài zhèr gōngzuò duō cháng shíjiān le?", "How long have you worked here?"),
                DialogueLine("已经两年多了，我是二〇一一年来的。", "Yǐjīng liǎng nián duō le, wǒ shì èr líng yīyī nián lái de.", "More than two years. I came in 2011."),
                DialogueLine("你认识谢先生吗？", "Nǐ rènshi Xiè xiānsheng ma?", "Do you know Mr Xie?"),
                DialogueLine("认识，我们是大学同学，这个工作是他帮我介绍的。", "Rènshi, wǒmen shì dàxué tóngxué, zhège gōngzuò shì tā bāng wǒ jièshào de.", "Yes. We were university classmates. He recommended me for this job.")
            ))
        ),
        words = listOf(
            Word("生日", "shēngrì", "birthday", "生日快乐！", "Shēngrì kuàilè!", "Happy birthday!"),
            Word("快乐", "kuàilè", "happy; glad", "生日快乐！", "Shēngrì kuàilè!", "Happy birthday!"),
            Word("给", "gěi", "to; for (after a verb)", "这是送给你的。", "Zhè shì sòng gěi nǐ de.", "This is for you."),
            Word("接", "jiē", "to receive; to answer (a call)", "是儿子接的。", "Shì érzi jiē de.", "Our son answered it."),
            Word("晚上", "wǎnshang", "evening; night", "晚上我问一下儿子。", "Wǎnshang wǒ wèn yíxià érzi.", "I'll ask our son this evening."),
            Word("问", "wèn", "to ask", "我问一下儿子。", "Wǒ wèn yíxià érzi.", "I'll ask our son."),
            Word("非常", "fēicháng", "very; extremely", "非常喜欢。", "Fēicháng xǐhuan.", "I like it very much."),
            Word("开始", "kāishǐ", "to begin; to start", "十一岁的时候开始踢足球。", "Shíyī suì de shíhou kāishǐ tī zúqiú.", "I started playing football at eleven."),
            Word("已经", "yǐjīng", "already", "已经踢了十年了。", "Yǐjīng tī le shí nián le.", "I have already played for ten years."),
            Word("长", "cháng", "long", "多长时间？", "Duō cháng shíjiān?", "How long?"),
            Word("两", "liǎng", "two (before a measure word)", "已经两年多了。", "Yǐjīng liǎng nián duō le.", "More than two years already."),
            Word("帮", "bāng", "to help; to assist", "他帮我介绍工作。", "Tā bāng wǒ jièshào gōngzuò.", "He helped recommend me for the job."),
            Word("介绍", "jièshào", "to introduce; to recommend", "这个工作是他帮我介绍的。", "Zhège gōngzuò shì tā bāng wǒ jièshào de.", "He recommended me for this job.")
        ),
        grammar = listOf(
            GrammarPoint("是……的 · emphasising the person who did an action", "When an action is already known to have happened, 是……的 can emphasise who performed it. In the negative, 不 is placed before 是.", "Object + 是 + person + verb + 的", listOf(
                DialogueLine("这本书是我写的。", "Zhè běn shū shì wǒ xiě de.", "This book was written by me."),
                DialogueLine("这个工作是他帮我介绍的。", "Zhège gōngzuò shì tā bāng wǒ jièshào de.", "He recommended me for this job."),
                DialogueLine("电话不是我接的。", "Diànhuà bú shì wǒ jiē de.", "I was not the one who answered the call.")
            )),
            GrammarPoint("……的时候 · indicating a time", "A time expression or a verb phrase followed by 的时候 indicates when something happens.", "Time / verb phrase + 的时候", listOf(
                DialogueLine("我十一岁的时候开始踢足球。", "Wǒ shíyī suì de shíhou kāishǐ tī zúqiú.", "I started playing football when I was eleven."),
                DialogueLine("我睡觉的时候，我妈妈在做饭。", "Wǒ shuìjiào de shíhou, wǒ māma zài zuò fàn.", "When I sleep, my mother is cooking.")
            )),
            GrammarPoint("已经 · already", "已经 indicates that an action has been completed or that a state has reached a certain degree. It commonly appears before the verb or adjective phrase.", "已经 + verb / adjective (+ 了)", listOf(
                DialogueLine("已经两年多了。", "Yǐjīng liǎng nián duō le.", "It has already been more than two years."),
                DialogueLine("已经踢了十年了。", "Yǐjīng tī le shí nián le.", "I have already played for ten years.")
            ))
        ),
        questions = listOf(
            QuizQuestion("What was the birthday present?", listOf("A watch", "A book", "A phone", "A football"), 1, "是一本书；这本书是我写的。", "Comprehension"),
            QuizQuestion("Who answered the telephone?", listOf("The father", "The mother", "The son", "A colleague"), 2, "是儿子接的。", "Comprehension"),
            QuizQuestion("How old was the speaker when he started football?", listOf("Nine", "Ten", "Eleven", "Twelve"), 2, "我十一岁的时候开始踢足球。", "Comprehension"),
            QuizQuestion("How long has he played football?", listOf("Five years", "Eight years", "Ten years", "Eleven years"), 2, "已经踢了十年了。", "Comprehension"),
            QuizQuestion("How long has the speaker worked there?", listOf("More than one year", "More than two years", "Five years", "Ten years"), 1, "已经两年多了。", "Comprehension"),
            QuizQuestion("Who recommended him for the job?", listOf("His teacher", "His father", "His university classmate Mr Xie", "His son"), 2, "这个工作是他帮我介绍的。", "Comprehension"),
            QuizQuestion("Choose the structure that emphasises who did an action.", listOf("是……的", "是不是", "有点儿", "每……"), 0, "是……的 can emphasise the agent of an action.", "Grammar"),
            QuizQuestion("What does 已经 express?", listOf("A future plan", "An action already completed or a state reached", "A question about height", "A location"), 1, "已经 indicates completion or reaching a degree.", "Vocabulary"),
            QuizQuestion("Complete: 我十一岁____开始踢足球。", listOf("的时候", "以后", "旁边", "一下"), 0, "……的时候 indicates the time when an action happens.", "Workbook fill-in"),
            QuizQuestion("In 这本书是我写的, who wrote the book?", listOf("The speaker", "The speaker's father", "The son", "Mr Xie"), 0, "我 is placed in the 是……的 structure to emphasise the agent.", "Workbook reading comprehension")
        ),
        pronunciation = "Workbook tasks include word listening and pronunciation practice. Listen to words such as 生日 shēngrì, 快乐 kuàilè, 开始 kāishǐ and 介绍 jièshào with the phone's Mandarin TTS."
    ),
    LessonContent(
        number = 5,
        title = "Take this one",
        hanziTitle = "就买这件吧",
        textbookPages = "PDF pages 49–56",
        workbookPages = "PDF pages 49–58",
        dialogues = listOf(
            Dialogue("1 · Dinner at home", "At home · Textbook dialogue 05-1", listOf(
                DialogueLine("晚上我们去饭馆吃饭，怎么样？", "Wǎnshang wǒmen qù fànguǎn chī fàn, zěnmeyàng?", "Shall we go to a restaurant for dinner tonight?"),
                DialogueLine("我不想去外面吃，我想在家吃。", "Wǒ bù xiǎng qù wàimiàn chī, wǒ xiǎng zài jiā chī.", "I don't want to eat out. I want to eat at home."),
                DialogueLine("那你准备做什么呢？", "Nà nǐ zhǔnbèi zuò shénme ne?", "Then what are you planning to cook?"),
                DialogueLine("就做你爱吃的鱼吧。", "Jiù zuò nǐ ài chī de yú ba.", "Then I'll make the fish you like.")
            )),
            Dialogue("2 · Choosing clothes", "In a store · Textbook dialogue 05-2", listOf(
                DialogueLine("帮我看一下这件衣服怎么样。", "Bāng wǒ kàn yíxià zhè jiàn yīfu zěnmeyàng.", "Help me take a look. What do you think of this garment?"),
                DialogueLine("颜色还可以，就是有点儿大。", "Yánsè hái kěyǐ, jiùshì yǒudiǎnr dà.", "The colour is acceptable; it is just a little big."),
                DialogueLine("这件小的怎么样？", "Zhè jiàn xiǎo de zěnmeyàng?", "What about this smaller one?"),
                DialogueLine("这件不错，就买这件吧。", "Zhè jiàn búcuò, jiù mǎi zhè jiàn ba.", "This one is pretty good. Let's buy this one.")
            )),
            Dialogue("3 · After an exam", "In the classroom · Textbook dialogue 05-3", listOf(
                DialogueLine("今天去不去打球？", "Jīntiān qù bu qù dǎ qiú?", "Are you going to play ball today?"),
                DialogueLine("这两天有点儿累，不去打球了。", "Zhè liǎng tiān yǒudiǎnr lèi, bú qù dǎ qiú le.", "I've been a little tired these two days, so I'm not going to play."),
                DialogueLine("你在做什么呢？是在想昨天的考试吗？", "Nǐ zài zuò shénme ne? Shì zài xiǎng zuótiān de kǎoshì ma?", "What are you doing? Are you thinking about yesterday's exam?"),
                DialogueLine("是啊，我觉得听和说还可以，读和写不好，很多字我都不知道是什么意思。", "Shì a, wǒ juéde tīng hé shuō hái kěyǐ, dú hé xiě bù hǎo, hěn duō zì wǒ dōu bù zhīdào shì shénme yìsi.", "Yes. Listening and speaking were acceptable, but reading and writing were not. I did not know the meaning of many characters.")
            )),
            Dialogue("4 · A coffee break", "In the company · Textbook dialogue 05-4", listOf(
                DialogueLine("休息一下吧，喝咖啡吗？", "Xiūxi yíxià ba, hē kāfēi ma?", "Let's take a break. Would you like some coffee?"),
                DialogueLine("不了，我已经喝两杯了。", "Bù le, wǒ yǐjīng hē liǎng bēi le.", "No thanks. I've already had two cups."),
                DialogueLine("是啊，咖啡喝多了对身体不好。", "Shì a, kāfēi hē duō le duì shēntǐ bù hǎo.", "Yes. Drinking too much coffee is bad for your health."),
                DialogueLine("以后我少喝一点儿，每天喝一杯。", "Yǐhòu wǒ shǎo hē yìdiǎnr, měitiān hē yì bēi.", "From now on I'll drink a little less, one cup a day.")
            ))
        ),
        words = listOf(
            Word("外面", "wàimiàn", "outside", "我不想去外面吃。", "Wǒ bù xiǎng qù wàimiàn chī.", "I don't want to eat out."),
            Word("准备", "zhǔnbèi", "to intend; to plan", "你准备做什么呢？", "Nǐ zhǔnbèi zuò shénme ne?", "What are you planning to do?"),
            Word("就", "jiù", "then; in that case", "就做你爱吃的鱼吧。", "Jiù zuò nǐ ài chī de yú ba.", "Then I'll make the fish you like."),
            Word("鱼", "yú", "fish", "就做鱼吧。", "Jiù zuò yú ba.", "Let's make fish."),
            Word("吧", "ba", "sentence-final particle for suggestions or requests", "就买这件吧。", "Jiù mǎi zhè jiàn ba.", "Let's buy this one."),
            Word("件", "jiàn", "measure word for clothes and some items", "这件衣服怎么样？", "Zhè jiàn yīfu zěnmeyàng?", "What do you think of this garment?"),
            Word("还", "hái", "fairly; passably; still", "颜色还可以。", "Yánsè hái kěyǐ.", "The colour is acceptable."),
            Word("可以", "kěyǐ", "okay; not bad; can", "颜色还可以。", "Yánsè hái kěyǐ.", "The colour is okay."),
            Word("不错", "búcuò", "pretty good; not bad", "这件不错。", "Zhè jiàn búcuò.", "This one is pretty good."),
            Word("考试", "kǎoshì", "exam; test", "昨天的考试。", "Zuótiān de kǎoshì.", "Yesterday's exam."),
            Word("意思", "yìsi", "meaning", "是什么意思？", "Shì shénme yìsi?", "What does it mean?"),
            Word("咖啡", "kāfēi", "coffee", "喝咖啡吗？", "Hē kāfēi ma?", "Would you like coffee?"),
            Word("对", "duì", "towards; with regard to", "对身体不好。", "Duì shēntǐ bù hǎo.", "Bad for your health."),
            Word("以后", "yǐhòu", "afterwards; from now on; later", "以后我少喝一点儿。", "Yǐhòu wǒ shǎo hē yìdiǎnr.", "From now on I'll drink a little less.")
        ),
        grammar = listOf(
            GrammarPoint("就 + verb · reaching a conclusion", "就 before a verb can introduce a conclusion or decision based on what has just been said. In a suggestion, it can sound like 'then / in that case'.", "Context + 就 + verb phrase", listOf(
                DialogueLine("你不想去，就在家休息吧。", "Nǐ bù xiǎng qù, jiù zài jiā xiūxi ba.", "If you don't want to go, then rest at home."),
                DialogueLine("就买这件吧。", "Jiù mǎi zhè jiàn ba.", "Then let's buy this one.")
            )),
            GrammarPoint("还 + adjective · acceptable / passable", "还 before an adjective can mean that something is just about acceptable or fairly good. In 颜色还可以，就是有点儿大, 就是 introduces a contrast or reservation.", "还 + adjective  /  A，就是 + B", listOf(
                DialogueLine("颜色还可以。", "Yánsè hái kěyǐ.", "The colour is acceptable."),
                DialogueLine("听和说还可以，读和写不好。", "Tīng hé shuō hái kěyǐ, dú hé xiě bù hǎo.", "Listening and speaking are acceptable, but reading and writing are not.")
            )),
            GrammarPoint("有点儿 + adjective / verb · a little (often negative)", "有点儿 commonly expresses a small amount with a negative, inconvenient or dissatisfied feeling, for example being a little tired or an item being a little too big.", "有点儿 + adjective / verb", listOf(
                DialogueLine("这两天有点儿累。", "Zhè liǎng tiān yǒudiǎnr lèi.", "I've been a little tired these two days."),
                DialogueLine("这件衣服有点儿大。", "Zhè jiàn yīfu yǒudiǎnr dà.", "This garment is a little too big.")
            ))
        ),
        questions = listOf(
            QuizQuestion("Where does the speaker want to eat dinner?", listOf("At a restaurant", "At home", "At the office", "At a café"), 1, "我想在家吃。", "Comprehension"),
            QuizQuestion("What does the speaker plan to cook?", listOf("Chicken", "Fish", "Noodles", "Rice"), 1, "就做你爱吃的鱼吧。", "Comprehension"),
            QuizQuestion("What is the problem with the first garment?", listOf("It is too small", "It is a little too big", "The colour is bad", "It is too expensive"), 1, "颜色还可以，就是有点儿大。", "Workbook picture-description style"),
            QuizQuestion("Which garment do they decide to buy?", listOf("The first one", "The smaller one", "The red one", "Neither"), 1, "这件不错，就买这件吧。", "Comprehension"),
            QuizQuestion("Why is the speaker not going to play ball?", listOf("He is busy", "He is a little tired", "He is ill", "He has no shoes"), 1, "这两天有点儿累。", "Comprehension"),
            QuizQuestion("Which skills does the speaker say are acceptable?", listOf("Reading and writing", "Listening and speaking", "Reading and listening", "Speaking and writing"), 1, "听和说还可以，读和写不好。", "Comprehension"),
            QuizQuestion("How many cups of coffee has the speaker already had?", listOf("One", "Two", "Three", "Four"), 1, "我已经喝两杯了。", "Comprehension"),
            QuizQuestion("What does 就 + verb express in this lesson?", listOf("A conclusion or decision", "A question about age", "Possession", "A past action only"), 0, "就做你爱吃的鱼吧 is a decision based on the previous statement.", "Grammar"),
            QuizQuestion("Choose the best completion: 这件衣服____大。", listOf("有点儿", "已经", "每", "旁边"), 0, "有点儿 + adjective expresses a small amount, often with a negative feeling.", "Workbook fill-in"),
            QuizQuestion("What does 以后 mean?", listOf("Before", "Beside", "Afterwards / from now on", "Every day"), 2, "以后 means afterwards, later or from now on.", "Vocabulary")
        ),
        pronunciation = "The textbook introduces sentence-level grammatical stress. Practise hearing which part of a sentence is emphasised, then listen to examples such as 这件衣服 zhè jiàn yīfu and 咖啡喝多了 kāfēi hē duō le."
    )
)

@Composable
private fun MandarinApp(speak: (String) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("mandarin_progress", android.content.Context.MODE_PRIVATE) }
    var selectedLesson by remember { mutableStateOf<LessonContent?>(null) }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var rootPage by rememberSaveable { mutableStateOf("Course") }
    var bestScores by remember { mutableStateOf((1..5).associateWith { prefs.getInt("best_$it", -1) }) }
    var knownWords by remember { mutableStateOf(prefs.getStringSet("known_words", emptySet())?.toSet() ?: emptySet()) }

    MaterialTheme(colorScheme = lightColorScheme(primary = Teal, secondary = Ink, background = Pale)) {
        Scaffold(
            containerColor = Pale,
            bottomBar = {
                if (selectedLesson == null) {
                    NavigationBar(containerColor = Color.White) {
                        listOf("Course", "Progress").forEach { page ->
                            NavigationBarItem(
                                selected = rootPage == page,
                                onClick = { rootPage = page },
                                icon = { Text(if (page == "Course") "书" else "✓", fontSize = 18.sp) },
                                label = { Text(page) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            val openLesson: (LessonContent) -> Unit = { lesson -> selectedLesson = lesson; selectedTab = 0 }
            if (selectedLesson != null) {
                LessonScreen(
                    modifier = Modifier.padding(padding),
                    lesson = selectedLesson!!,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onBack = { selectedLesson = null },
                    speak = speak,
                    knownWords = knownWords,
                    onToggleKnown = { word ->
                        knownWords = if (knownWords.contains(word)) knownWords - word else knownWords + word
                        prefs.edit().putStringSet("known_words", knownWords).apply()
                    },
                    onQuizFinished = { lessonNumber, score ->
                        val old = bestScores[lessonNumber] ?: -1
                        if (score > old) {
                            bestScores = bestScores + (lessonNumber to score)
                            prefs.edit().putInt("best_$lessonNumber", score).apply()
                        }
                    }
                )
            } else if (rootPage == "Progress") {
                ProgressScreen(Modifier.padding(padding), bestScores, knownWords.size, onOpenLesson = openLesson)
            } else {
                CourseScreen(Modifier.padding(padding), bestScores, openLesson)
            }
        }
    }
}

@Composable
private fun CourseScreen(modifier: Modifier, bestScores: Map<Int, Int>, openLesson: (LessonContent) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("MANDARIN COMPANION", color = Teal, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.5.sp)
            Text("你好！", color = Ink, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text("HSK 2 · Lessons 1–5", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Based on your Standard Course 2 textbook and workbook scans.", color = Ink.copy(alpha = 0.72f), fontSize = 14.sp)
        }
        item { InfoPanel("Study offline", "All lesson text, vocabulary, grammar and quizzes are stored on this phone. Mandarin pronunciation uses Android Text-to-Speech; the publisher's audio files were not included in the PDFs.") }
        items(lessons) { lesson ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { openLesson(lesson) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("LESSON ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                        val score = bestScores[lesson.number] ?: -1
                        if (score >= 0) Text("Best score: $score/${lesson.questions.size}", color = Good, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(lesson.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(lesson.hanziTitle, color = Teal, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    Text("${lesson.dialogues.size} dialogues · ${lesson.words.size} words · ${lesson.grammar.size} grammar points · ${lesson.questions.size} workbook questions", color = Color.Gray, fontSize = 12.sp)
                    Text("Textbook ${lesson.textbookPages}  ·  Workbook ${lesson.workbookPages}", color = Color.Gray, fontSize = 11.sp)
                    Button(onClick = { openLesson(lesson) }, modifier = Modifier.fillMaxWidth()) { Text("Open lesson") }
                }
            }
        }
        item {
            Text("Learning focus: character recognition, vocabulary, pinyin, grammar, listening and speaking. No handwriting or stroke-order drills.", color = Ink.copy(alpha = 0.75f), fontSize = 13.sp)
        }
    }
}

@Composable
private fun LessonScreen(
    modifier: Modifier,
    lesson: LessonContent,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBack: () -> Unit,
    speak: (String) -> Unit,
    knownWords: Set<String>,
    onToggleKnown: (String) -> Unit,
    onQuizFinished: (Int, Int) -> Unit
) {
    val tabs = listOf("Dialogues", "Vocabulary", "Grammar", "Workbook")
    Column(modifier.fillMaxSize()) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 6.dp)) {
            TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) { Text("← All lessons", color = Teal) }
            Text("HSK 2  /  LESSON ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
            Text(lesson.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 21.sp)
            Text(lesson.hanziTitle, color = Teal, fontSize = 20.sp)
            Text("Textbook ${lesson.textbookPages} · Workbook ${lesson.workbookPages}", color = Color.Gray, fontSize = 10.sp)
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 8.dp, containerColor = Pale) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { onTabSelected(index) }, text = { Text(title) })
            }
        }
        when (selectedTab) {
            0 -> DialoguesTab(Modifier.weight(1f), lesson, speak)
            1 -> VocabularyTab(Modifier.weight(1f), lesson, speak, knownWords, onToggleKnown)
            2 -> GrammarTab(Modifier.weight(1f), lesson, speak)
            else -> WorkbookTab(Modifier.weight(1f), lesson, speak, onQuizFinished)
        }
    }
}

@Composable
private fun DialoguesTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Listen and repeat", "Read the Chinese line first, check pinyin and meaning, then tap Listen. Repeat the line aloud. The voice is generated by Android TTS, not the publisher's recording.") }
        items(lesson.dialogues) { dialogue ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text(dialogue.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(dialogue.scene, color = Teal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    dialogue.lines.forEachIndexed { index, line ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(line.chinese, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            Text(line.pinyin, color = Teal, fontSize = 13.sp)
                            Text(line.english, color = Ink.copy(alpha = 0.78f), fontSize = 13.sp)
                            TextButton(onClick = { speak(line.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) { Text("▶ Listen to line ${index + 1}") }
                        }
                        if (index != dialogue.lines.lastIndex) HorizontalDivider(color = Pale)
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Pronunciation focus", color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(lesson.pronunciation, color = Ink.copy(alpha = 0.8f), fontSize = 13.sp)
                    TextButton(onClick = { speak(lesson.hanziTitle) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen to lesson title") }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun VocabularyTab(
    modifier: Modifier,
    lesson: LessonContent,
    speak: (String) -> Unit,
    knownWords: Set<String>,
    onToggleKnown: (String) -> Unit
) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        item { InfoPanel("Textbook new words", "Mark a word as known when you can recognise it and understand it. Tap Listen to hear the word and its example.") }
        items(lesson.words) { word ->
            Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(word.hanzi, color = Ink, fontWeight = FontWeight.Bold, fontSize = 26.sp, modifier = Modifier.weight(1f))
                        TextButton(onClick = { speak(word.hanzi) }) { Text("▶ Listen") }
                    }
                    Text(word.pinyin, color = Teal, fontSize = 14.sp)
                    Text(word.meaning, color = Ink.copy(alpha = 0.8f), fontSize = 14.sp)
                    HorizontalDivider(color = Pale)
                    Text(word.example, color = Ink, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text(word.examplePinyin, color = Teal, fontSize = 12.sp)
                    Text(word.exampleEnglish, color = Color.Gray, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { speak(word.example) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Example") }
                        Spacer(Modifier.weight(1f))
                        Text(if (knownWords.contains(word.hanzi)) "Known ✓" else "Mark known", color = if (knownWords.contains(word.hanzi)) Good else Teal, fontSize = 12.sp, modifier = Modifier.clickable { onToggleKnown(word.hanzi) }.padding(8.dp))
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun GrammarTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Grammar from the textbook", "Each point includes a short explanation, a pattern and examples from the lesson or the textbook notes.") }
        items(lesson.grammar) { point ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(point.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(point.explanation, color = Ink.copy(alpha = 0.82f), fontSize = 13.sp)
                    Surface(color = SoftTeal, shape = RoundedCornerShape(10.dp)) {
                        Text(point.pattern, color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(10.dp), fontSize = 13.sp)
                    }
                    point.examples.forEach { example ->
                        Text(example.chinese, color = Ink, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        Text(example.pinyin, color = Teal, fontSize = 12.sp)
                        Text(example.english, color = Color.Gray, fontSize = 12.sp)
                        TextButton(onClick = { speak(example.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen") }
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun WorkbookTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit, onFinished: (Int, Int) -> Unit) {
    var index by remember(lesson.number) { mutableIntStateOf(0) }
    var selected by remember(lesson.number) { mutableIntStateOf(-1) }
    var score by remember(lesson.number) { mutableIntStateOf(0) }
    var saved by remember(lesson.number) { mutableStateOf(false) }
    val finished = index >= lesson.questions.size

    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Workbook practice · adapted", "These questions are adapted from the lesson content and exercise formats in your workbook: listening-style choices, comprehension, matching, fill-in and true/false. Publisher audio is not included; use the Listen button where shown.") }
        if (!finished) {
            val question = lesson.questions[index]
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Question ${index + 1} of ${lesson.questions.size}", color = Teal, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Score: $score", color = Ink, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(progress = { (index + 1).toFloat() / lesson.questions.size.toFloat() }, modifier = Modifier.fillMaxWidth(), color = Teal, trackColor = SoftTeal)
            }
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(question.exerciseType.uppercase(), color = Teal, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
                        Text(question.prompt, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (question.prompt.any { it in '\u4e00'..'\u9fff' }) {
                            TextButton(onClick = { speak(question.prompt) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen to question") }
                        }
                        question.options.forEachIndexed { optionIndex, option ->
                            val optionColor = when {
                                selected == -1 -> Color.White
                                optionIndex == question.answer -> SoftTeal
                                optionIndex == selected -> Color(0xFFF9E8E8)
                                else -> Color.White
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable(enabled = selected == -1) {
                                    selected = optionIndex
                                    if (optionIndex == question.answer) score++
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = optionColor)
                            ) {
                                Text(option, color = Ink, modifier = Modifier.padding(13.dp), fontSize = 14.sp)
                            }
                        }
                        if (selected != -1) {
                            Text(if (selected == question.answer) "Correct" else "Not quite", color = if (selected == question.answer) Good else Bad, fontWeight = FontWeight.Bold)
                            Text(question.explanation, color = Ink, fontSize = 13.sp)
                            Button(onClick = { index++; selected = -1 }, modifier = Modifier.fillMaxWidth()) { Text(if (index == lesson.questions.lastIndex) "See result" else "Next question") }
                        }
                    }
                }
            }
        } else {
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("练习完成", color = Teal, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("$score / ${lesson.questions.size}", color = Ink, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        Text("${score * 100 / lesson.questions.size}% correct", color = Ink)
                        Text(if (score * 100 / lesson.questions.size >= 70) "Good result. Review any missed items, then say the dialogues aloud." else "Review the vocabulary and grammar, then try the workbook practice again.", color = Color.Gray, fontSize = 13.sp)
                        Button(onClick = { if (!saved) { onFinished(lesson.number, score); saved = true } }, enabled = !saved, modifier = Modifier.fillMaxWidth()) { Text(if (saved) "Progress saved" else "Save lesson progress") }
                        OutlinedButton(onClick = { index = 0; selected = -1; score = 0; saved = false }, modifier = Modifier.fillMaxWidth()) { Text("Try again") }
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun ProgressScreen(modifier: Modifier, bestScores: Map<Int, Int>, knownCount: Int, onOpenLesson: (LessonContent) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 18.dp), contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Your progress", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Scores are stored locally on this phone.", color = Color.Gray, fontSize = 13.sp)
        }
        item { MiniStat("Words marked known", knownCount.toString()) }
        items(lessons) { lesson ->
            val score = bestScores[lesson.number] ?: -1
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Lesson ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(lesson.title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(if (score >= 0) "Best workbook score: $score/${lesson.questions.size}" else "Not completed yet", color = Color.Gray, fontSize = 12.sp)
                    }
                    TextButton(onClick = { onOpenLesson(lesson) }) { Text("Open") }
                }
            }
        }
        item { Text("No account, cloud sync or paid API is required. Android TTS availability depends on the Mandarin voice installed on the device.", color = Color.Gray, fontSize = 12.sp) }
    }
}

@Composable
private fun MiniStat(title: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = Color.Gray, modifier = Modifier.weight(1f), fontSize = 13.sp)
            Text(value, color = Ink, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
    }
}

@Composable
private fun InfoPanel(title: String, body: String) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SoftBlue), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(body, color = Ink.copy(alpha = 0.82f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun SourceNote(lesson: LessonContent) {
    Column(Modifier.fillMaxWidth().padding(top = 7.dp)) {
        HorizontalDivider(color = Color(0xFFD7E2E0))
        Spacer(Modifier.height(7.dp))
        Text("SOURCE CHECK", color = Teal, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
        Text("Textbook ${lesson.textbookPages}; workbook ${lesson.workbookPages}. Dialogues, vocabulary and grammar are transcribed/adapted from the scanned lesson pages. Workbook practice questions are adapted to work offline. Original publisher audio and image-matching pages are not embedded. Handwriting/stroke-order exercises are omitted.", color = Color.Gray, fontSize = 10.sp)
    }
}
