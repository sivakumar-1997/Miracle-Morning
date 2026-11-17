package com.example.data.model

data class MagicLesson(
  val day: Int,
  val title: String,
  val lessonGuidance: String,
  val suggestions: List<Pair<String, String>>,
  val dailyBlessings: List<Pair<String, String>>
)

object TheMagicCurriculum {

  val lessons: List<MagicLesson> = listOf(
    MagicLesson(
      day = 1,
      title = "Day 1: Count Your Blessings",
      lessonGuidance = "Rhonda Byrne teaches: List 10 blessings each morning, state why you are grateful, read each one, and feel it deeply while saying 'Thank you, thank you, thank you.'",
      suggestions = listOf(
        "the miracle of waking up with healthy breath" to "every breath gives me a fresh canvas to create joy today",
        "the sanctuary of my home" to "it keeps me warm, safe, and centered in peace",
        "my supportive loved ones" to "their care reminds me I am never alone",
        "clean pure water nourishing my body" to "it revitalizes every single cell with vibrant energy"
      ),
      dailyBlessings = listOf(
        "my strong, healthy body and rhythmic breath" to "it allows me to experience life with vitality and boundless energy",
        "the peaceful early morning Brahma Muhurta hours" to "the silence connects me directly to divine stillness and clarity",
        "the pure glass of water placed in front of me" to "it cleanses my cells and absorbs my highest thoughts of joy",
        "my supportive family and mentors" to "their love grounds me and reminds me that I am always guided",
        "the abundance of money flowing into my life" to "it gives me the freedom to create, share, and live comfortably",
        "my sharp intuition and inner guidance" to "they guide positive decisions and open doors to synchronicities",
        "the wisdom of sacred books and teachers" to "they revealed the scientific and spiritual law of gratitude to me",
        "the sanctuary of my home" to "it provides safety, serenity, and sacred rest every night",
        "all challenges I have overcome in the past" to "they cultivated my inner resilience and made my spirit strong",
        "the limitless possibilities awaiting me today" to "the universe is orchestrating miracles on my behalf"
      )
    ),
    MagicLesson(
      day = 2,
      title = "Day 2: The Magic Rock",
      lessonGuidance = "Lesson from 'The Magic': Hold your Magic Rock each night and recall the single best occurrence of the day—and carry that appreciation into tomorrow morning's 10 blessings.",
      suggestions = listOf(
        "peaceful deep sleep last night" to "my body and subconscious mind regenerated effortlessly while I rested",
        "the best moment from yesterday" to "it proved that goodness is actively flowing into my daily life",
        "my calm bedtime sanctuary" to "letting go of worry allows miracles to unfold overnight",
        "waking up with a peaceful heart" to "gratitude sets a high-vibration tone before my day even begins"
      ),
      dailyBlessings = listOf(
        "the restful, restorative sleep I received last night" to "I woke up refreshed and mentally clear",
        "the best blessing that happened to me yesterday" to "remembering it fills my heart with immediate warmth",
        "my bedside quiet space for evening reflection" to "it helps my subconscious absorb peace before sleep",
        "the gift of a brand-new morning free from yesterday's stress" to "every sunrise resets my vibrational setpoint",
        "my mind's ability to focus on what went right" to "what I appreciate multiplies rapidly",
        "the comfort of my bed, pillow, and quiet room" to "they cradle me in safety night after night",
        "the subtle synchronicities that brightened yesterday" to "they show me the Universe is listening attentively",
        "my calm nervous system this morning" to "stillness empowers me to respond with wisdom",
        "the promise of another unforgettable blessing today" to "I expect wonderful news to find me effortlessly",
        "the simple Magic Rock practice of evening gratitude" to "it turns every night into a ceremony of thankfulness"
      )
    ),
    MagicLesson(
      day = 3,
      title = "Day 3: Magical Relationships",
      lessonGuidance = "Lesson from 'The Magic': Bring to mind three key relationships and give genuine thanks for the finest qualities in each person—gratitude dissolves friction and deepens love.",
      suggestions = listOf(
        "the kindness and patience of my closest family" to "their warmth enriches my spirit every day",
        "a loyal friend who truly listens" to "having someone who understands me is priceless",
        "the mentors and teachers who believed in me" to "their encouragement shaped who I am becoming",
        "harmonious understanding with everyone I meet today" to "compassion opens every human heart"
      ),
      dailyBlessings = listOf(
        "the unconditional love within my family" to "it gives me roots of security and wings of confidence",
        "my closest friends who bring laughter into my life" to "shared joy multiplies every good experience",
        "the patience and kindness of someone who helped me recently" to "their generosity restored my faith in human warmth",
        "my ability to see the best qualities in others" to "appreciating people transforms every relationship into harmony",
        "conversations that uplift and inspire my soul" to "meaningful connection nourishes my mental health",
        "the mentors who shared wisdom at the exact right time" to "their guidance saved me years of struggle",
        "peaceful resolution in any challenging relationship" to "gratitude softens hearts and invites mutual respect",
        "colleagues and neighbors who cooperate smoothly with me" to "friendly community makes daily life effortless",
        "the love I am learning to give myself unconditionally" to "self-respect sets the standard for healthy bonds",
        "every smiling stranger I cross paths with today" to "we are all connected in one radiant human family"
      )
    ),
    MagicLesson(
      day = 4,
      title = "Day 4: Magical Health",
      lessonGuidance = "Lesson from 'The Magic': 'The gift of health is keeping me alive.' Give heartfelt thanks for your eyes, heart, lungs, legs, brain, and immune vitality.",
      suggestions = listOf(
        "my beating heart pumping life effortlessly" to "it works tirelessly every second out of pure love for me",
        "my clear eyesight taking in colors and light" to "seeing the beauty of the world is an extraordinary miracle",
        "my strong legs and hands serving me daily" to "mobility lets me move freely and embrace life",
        "my intelligent immune system protecting every cell" to "my body naturally knows how to heal and thrive"
      ),
      dailyBlessings = listOf(
        "my faithful heart beating steadily every moment" to "it sustains my life without me ever having to ask",
        "my lungs breathing fresh oxygen smoothly" to "every inhale energizes my mind and clears fatigue",
        "my eyesight allowing me to see faces, nature, and words" to "visual beauty fills my life with endless wonder",
        "my ears hearing music, laughter, and sacred frequencies" to "sound connects me deeply to emotion and harmony",
        "my resilient legs, knees, and feet" to "they carry me effortlessly wherever I wish to go",
        "my capable hands and fingers" to "they let me write, work, embrace loved ones, and create",
        "my brilliant brain coordinating trillions of cells" to "it stores wisdom, imagines futures, and solves problems",
        "my digestive system turning food into pure energy" to "every meal fuels my vitality and strength",
        "my self-healing immune system" to "it quietly guards my wellness twenty-four hours a day",
        "the vibrant feeling of being alive right now" to "health is the greatest wealth I will ever possess"
      )
    ),
    MagicLesson(
      day = 5,
      title = "Day 5: Magic Money",
      lessonGuidance = "Lesson from 'The Magic': Give thanks for all the money you have received throughout your entire life—complaining pushes money away, while gratitude magnetizes abundance.",
      suggestions = listOf(
        "every meal and home provided for me growing up" to "abundance supported me long before I earned a cent",
        "the steady flow of income and opportunities in my life" to "money enables me to live freely and give generously",
        "every bill I have ever been able to pay" to "paying a bill means I received valuable goods and services first",
        "unexpected financial blessings arriving effortlessly" to "the Universe has infinite channels of supply"
      ),
      dailyBlessings = listOf(
        "all the food, shelter, and education provided during my childhood" to "I was richly supported long before earning money myself",
        "every paycheck, reward, and payment I have ever received" to "each one empowered me to build my life",
        "the clean electricity, internet, and water in my home" to "money quietly buys modern comforts kings once dreamed of",
        "my bank account and financial tools" to "they securely hold and circulate abundance for my goals",
        "every opportunity I have to create value for others" to "serving well naturally expands my prosperity",
        "discounts, gifts, and unexpected savings that came my way" to "abundance loves finding creative ways to reach me",
        "my growing wisdom around managing and multiplying wealth" to "stewardship turns income into lasting freedom",
        "the ability to pay my bills on time with a grateful heart" to "every payment reflects trust and prosperity in motion",
        "generous people who inspire an abundance mindset" to "seeing others thrive proves there is more than enough for all",
        "financial peace flowing to me from expected and unexpected sources" to "gratitude keeps the channel of prosperity wide open"
      )
    ),
    MagicLesson(
      day = 6,
      title = "Day 6: Works Like Magic",
      lessonGuidance = "Lesson from 'The Magic': Imagine an invisible manager tracking every aspect of your work today—be genuinely thankful for your skills, workspace, tools, and colleagues.",
      suggestions = listOf(
        "my unique talents and problem-solving skills" to "they allow me to contribute meaningfully every day",
        "the technology and tools that amplify my work" to "my phone and computer connect me to limitless opportunity",
        "supportive teammates, clients, and collaborators" to "great work flows smoothly through mutual trust",
        "clarity, focus, and effortless productivity today" to "gratitude turns routine tasks into joyful mastery"
      ),
      dailyBlessings = listOf(
        "my creative mind and learned skills" to "they turn ideas into tangible value for the world",
        "the reliable smartphone and laptop I work with" to "they let me build, communicate, and learn in seconds",
        "every task that sharpens my mastery and confidence" to "consistent effort builds unstoppable momentum",
        "helpful coworkers, clients, and partners" to "goodwill makes teamwork genuinely enjoyable",
        "a comfortable, safe space to focus and produce" to "calm surroundings elevate the quality of my work",
        "the internet connecting me to global knowledge and markets" to "opportunity is literally at my fingertips",
        "past successes that proved what I am capable of" to "evidence of my competence fuels bold action",
        "constructive feedback that helped me improve quickly" to "every insight accelerates my professional growth",
        "fair compensation and recognition for my contributions" to "my time and energy are deeply valued",
        "purposeful work that brings me satisfaction and growth" to "loving what I build makes every morning exciting"
      )
    ),
    MagicLesson(
      day = 7,
      title = "Day 7: The Magical Way Out of Negativity",
      lessonGuidance = "Lesson from 'The Magic': Find 10 genuine blessings hidden inside any challenging situation—gratitude transmutes worry into clarity and forward motion.",
      suggestions = listOf(
        "the strength I discovered through past difficulties" to "pressure revealed resilience I never knew I had",
        "a challenge that redirected me toward a better path" to "closed doors protected me for higher opportunities",
        "my freedom to choose peaceful thoughts right now" to "no external circumstance can steal my inner stillness",
        "one silver lining inside today's biggest concern" to "looking for good always reveals hidden solutions"
      ),
      dailyBlessings = listOf(
        "the hidden resilience I gained from difficult seasons" to "surviving hard days made my spirit unshakeable",
        "my ability to pause, breathe, and choose a calmer thought" to "mindfulness keeps me from reacting out of fear",
        "lessons learned from past mistakes" to "wisdom prevents me from repeating old patterns",
        "problems that revealed who my truest supporters are" to "authentic relationships shine brightest in tough moments",
        "delays that unexpectedly protected me from wrong turns" to "divine timing is wiser than my impatience",
        "my capacity to forgive myself and begin anew today" to "releasing guilt frees my energy for creation",
        "every obstacle that forced me to grow more creative" to "constraints often spark my best breakthroughs",
        "the temporary nature of every passing storm" to "this challenge too is shifting toward resolution",
        "quiet inner strength available to me in this very breath" to "peace is always one grateful thought away",
        "the silver lining unfolding right beneath the surface" to "gratitude lights up solutions I couldn't see before"
      )
    ),
    MagicLesson(
      day = 8,
      title = "Day 8: The Magic Ingredient",
      lessonGuidance = "Lesson from 'The Magic': Before eating or drinking anything today, sprinkle 'magic dust' of gratitude—thank the soil, rain, sun, farmers, and hands that nourished you.",
      suggestions = listOf(
        "the fresh morning water hydrating my body" to "pure water is nature's simplest and greatest elixir",
        "the farmers, drivers, and grocers behind my meals" to "hundreds of caring hands brought nourishment to my table",
        "warm sunlight and rain that ripened every fruit and grain" to "nature generously feeds every living soul",
        "the flavor and vitality of wholesome food today" to "eating with gratitude nourishes body and spirit alike"
      ),
      dailyBlessings = listOf(
        "the pure, refreshing water I drink each morning" to "it awakens my metabolism and refreshes my mind",
        "the farmers and harvesters who grew my food" to "their daily labor sustains my family's table",
        "sunlight, soil, and rain working together in nature" to "the earth effortlessly turns seeds into abundance",
        "the delivery drivers and local shopkeepers" to "they make fresh ingredients accessible every day",
        "warm tea, coffee, or nourishing breakfast flavors" to "small daily pleasures make mornings deeply comforting",
        "my sense of taste enjoying sweet, savory, and fresh meals" to "flavor turns basic survival into joyful experience",
        "a clean kitchen and utensils to prepare healthy food" to "caring for my meals is an act of self-love",
        "sharing meals with people I care about" to "food tastes twice as rich when enjoyed in gratitude",
        "having more than enough nourishment in my home" to "plenty dissolves scarcity and brings deep security",
        "the energy and clarity wholesome nutrition gives me" to "thankfulness before eating blesses every cell"
      )
    ),
    MagicLesson(
      day = 9,
      title = "Day 9: The Money Magnet",
      lessonGuidance = "Lesson from 'The Magic': Write 'Thank You — Paid' on bills and feel gratitude for the electricity, internet, or service already delivered to you.",
      suggestions = listOf(
        "the electricity lighting and warming my home right now" to "flipping a switch brings instant comfort and power",
        "the internet connection linking me to the whole world" to "information and opportunity flow freely to my screen",
        "the water utilities delivering clean water to my tap" to "running clean water is a luxury billions pray for",
        "the joyful feeling of abundance covering every expense" to "thankfulness turns spending into confident circulation"
      ),
      dailyBlessings = listOf(
        "the electric lights, refrigerator, and climate comfort in my home" to "modern energy serves me silently day and night",
        "high-speed internet and mobile connectivity" to "they empower me to learn, earn, and connect anywhere",
        "clean running water available at the turn of a tap" to "convenience gives me hours of free time daily",
        "every service provider whose work makes my life easier" to "paying them circulates prosperity through our community",
        "my growing confidence whenever I look at expenses" to "gratitude replaces financial worry with calm trust",
        "all goods and experiences I have already enjoyed" to "remembering value received creates an abundance mindset",
        "new streams of income opening effortlessly this month" to "aligned action attracts generous rewards",
        "the habit of saying 'Thank you' every time I spend or receive" to "joyful circulation keeps money flowing back multiplied",
        "safe roads, bridges, and public infrastructure I use daily" to "shared resources support every journey I take",
        "a calm, magnetic relationship with wealth and generosity" to "when I feel rich inside, life mirrors richness outside"
      )
    ),
    MagicLesson(
      day = 10,
      title = "Day 10: Magic Dust Everyone",
      lessonGuidance = "Lesson from 'The Magic': Say 'Thank you' sincerely to 10 service workers today—cashiers, cleaners, drivers, receptionists—making eye contact so they feel truly valued.",
      suggestions = listOf(
        "sanitation and cleaning crews keeping our spaces spotless" to "their unseen work protects everyone's health and peace",
        "drivers, pilots, and transit workers moving us safely" to "their focus gets us home to the people we love",
        "cooks, baristas, and servers preparing food with care" to "their hospitality brightens busy days",
        "customer support helpers who solve problems patiently" to "a kind voice makes any hurdle effortless"
      ),
      dailyBlessings = listOf(
        "the drivers and transit workers keeping our cities moving" to "they help people reunite with work and family safely",
        "cleaners and caretakers maintaining welcoming spaces" to "dignified effort keeps our environment peaceful",
        "baristas, chefs, and restaurant servers" to "warm service brings brightness to ordinary days",
        "delivery couriers bringing essentials right to my door" to "they save me precious time every week",
        "nurses, doctors, and pharmacists caring for our community" to "their dedication protects human life daily",
        "teachers and childcare mentors shaping young minds" to "patient guidance builds a kinder future",
        "grocery clerks and market staff" to "friendly smiles turn simple errands into connection",
        "engineers and technicians maintaining power and networks" to "quiet expertise keeps the modern world running",
        "my own sincere smile and 'Thank you' today" to "acknowledging someone's humanity lifts both our vibrations",
        "the ripple effect of kindness passing from person to person" to "magic dust transforms an ordinary day into grace"
      )
    )
  ) + (11..28).map { dayNum ->
    // Complete Days 11 through 28 authentically following every remaining chapter of Rhonda Byrne's "The Magic"
    val meta = when (dayNum) {
      11 -> Triple(
        "Day 11: A Magic Morning",
        "From opening your eyes to brushing your teeth, turn every morning motion into a conscious wave of gratitude—starting your day in magic.",
        listOf(
          "waking up safely in a warm bed" to "every new dawn is an invitation to begin joyfully",
          "the sunlight streaming into my morning" to "natural light awakens clarity and optimism",
          "clean clothes, soap, and morning self-care" to "daily rituals prepare my spirit for excellence",
          "quiet morning minutes before checking messages" to "protecting my peace sets the tone for the whole day"
        )
      )
      12 -> Triple(
        "Day 12: Magical People Who Made a Difference",
        "Remember three people who changed the course of your life at crucial moments and silently send heartfelt thanks to each one.",
        listOf(
          "a teacher or elder who saw potential in me" to "their belief unlocked confidence I carry today",
          "a friend who stood by me during a turning point" to "loyal presence during change is priceless",
          "an author or speaker whose words awakened me" to "one right book can transform an entire destiny",
          "a family member whose sacrifices paved my way" to "their quiet generosity echoes through my life"
        )
      )
      13 -> Triple(
        "Day 13: Make All Your Wishes Come True",
        "Write your top desires as though you have already received them, feeling the relief and joy—'Thank you, thank you, thank you!'",
        listOf(
          "my dream life already unfolding behind the scenes" to "every grateful breath brings my vision closer",
          "abundant health, peace, and financial ease" to "I accept wellness and prosperity as my natural state",
          "joyful milestones ahead of me this year" to "anticipating good with certainty magnetizes results",
          "aligned opportunities meeting my prepared spirit" to "clarity and gratitude work together effortlessly"
        )
      )
      14 -> Triple(
        "Day 14: Have a Magical Day",
        "Before stepping out, say 'Thank you' in advance for smooth travel, wonderful meetings, and great news coming your way today.",
        listOf(
          "every task today flowing smoothly on schedule" to "calm preparation invites effortless timing",
          "wonderful news arriving at just the right hour" to "expectation tuned to joy notices every blessing",
          "clear communication in every call and message" to "goodwill makes collaboration easy",
          "safe journeys and green lights wherever I go" to "peaceful travel keeps my spirit light"
        )
      )
      15 -> Triple(
        "Day 15: Magically Heal Your Relationships",
        "Release resentment toward anyone by listing 10 things you genuinely appreciate about them—forgiveness restores your own freedom.",
        listOf(
          "the peace I feel when I release old hurt" to "forgiveness lightens my heart instantly",
          "lessons in patience and empathy I gained" to "understanding others deepens my own wisdom",
          "shared good memories worth honoring" to "choosing grace heals what anger cannot",
          "my own open, compassionate heart today" to "love returns to me multiplied"
        )
      )
      16 -> Triple(
        "Day 16: Magic and Miracles in Health",
        "Give thanks for past healings, present vitality, and the radiant future health of every organ and cell in your body.",
        listOf(
          "every past illness or injury my body healed from" to "my cells hold extraordinary regenerative intelligence",
          "deep, steady energy supporting my goals today" to "vibrant health lets me show up fully",
          "peaceful sleep and emotional balance" to "a calm mind protects physical longevity",
          "my commitment to nourish and move my body lovingly" to "respecting my body multiplies vitality"
        )
      )
      17 -> Triple(
        "Day 17: The Magic Check",
        "Visualize an abundant Magic Check written to your name for something you deeply wish to fund or bless others with.",
        listOf(
          "bold dreams that excite and expand my vision" to "giving permission to dream awakens creativity",
          "the joy of sharing abundance with loved ones" to "generosity feels deeply fulfilling",
          "unexpected avenues of prosperity opening up" to "resourcefulness creates new value",
          "trusting that my needs and goals are richly met" to "gratitude dissolves scarcity thinking"
        )
      )
      18 -> Triple(
        "Day 18: The Magical To-Do List",
        "Choose your three highest-priority challenges today and give thanks in advance as if each one is already gracefully resolved.",
        listOf(
          "clarity on what truly matters most today" to "focus replaces overwhelm with calm progress",
          "timely help arriving for my toughest tasks" to "support appears whenever I stay receptive",
          "creative breakthroughs solving lingering problems" to "a peaceful mind finds simpler paths",
          "the relief of completing today's priorities cleanly" to "momentum feels wonderful"
        )
      )
      19 -> Triple(
        "Day 19: Magic Footsteps",
        "Take 100 mindful steps saying 'Thank you' with every footstep—grounding your awareness into the miracle of movement.",
        listOf(
          "the earth beneath my feet supporting every step" to "grounding connects me to calm presence",
          "my balance, posture, and effortless movement" to "walking freely is a daily miracle",
          "fresh outdoor air and open sky above me" to "nature instantly restores perspective",
          "each small consistent step forward in my life" to "tiny daily habits build extraordinary destinies"
        )
      )
      20 -> Triple(
        "Day 20: Heart Magic",
        "Place your hand over your chest, feel your heartbeat, and say 'Thank you' directly from the emotional center of your heart.",
        listOf(
          "the radiant warmth in my heart center" to "heart coherence aligns thought with feeling",
          "moments of pure tenderness and compassion" to "an open heart attracts genuine love",
          "peace replacing anxiety whenever I breathe slowly" to "the heart remembers stillness instantly",
          "courage to live truthfully and kindly" to "sincerity makes every day meaningful"
        )
      )
      21 -> Triple(
        "Day 21: Magnificent Outcomes",
        "Before three key events today, say 'Thank you for the magnificent outcome!'—calibrating your energy toward the best resolution.",
        listOf(
          "favorable, win-win outcomes in today's meetings" to "positive expectation inspires confidence",
          "smooth timing and peaceful cooperation" to "calm leadership brings out the best in everyone",
          "surprising pleasant twists in my schedule" to "staying flexible lets better plans appear",
          "gratitude anchoring me regardless of small surprises" to "peace is my true superpower"
        )
      )
      22 -> Triple(
        "Day 22: Before Your Very Eyes",
        "Carry your top written desires with you and give heartfelt thanks whenever signs of their manifestation appear before your eyes.",
        listOf(
          "tangible progress already visible from my habits" to "consistency is quietly transforming my life",
          "meaningful coincidences reminding me I'm on track" to "awareness spots magic everywhere",
          "patience paired with joyful anticipation" to "trusting the process feels deeply peaceful",
          "celebrating small wins on the way to big dreams" to "appreciating steps speeds up the journey"
        )
      )
      23 -> Triple(
        "Day 23: The Magical Air That You Breathe",
        "Take five conscious deep breaths today, thanking the trees, oceans, and atmosphere for the life-giving air filling your lungs.",
        listOf(
          "the clean oxygen nourishing my brain and heart" to "every breath sustains consciousness effortlessly",
          "forests, plants, and oceans renewing Earth's air" to "nature generously keeps life in balance",
          "the calming effect of slow conscious breathing" to "deep breaths dissolve tension in seconds",
          "feeling deeply connected to the living planet" to "gratitude honors our shared home"
        )
      )
      24 -> Triple(
        "Day 24: The Magic Wand",
        "Wave an invisible magic wand of goodwill today—silently blessing people you see with health, wealth, and happiness.",
        listOf(
          "the joy of silently wishing strangers well" to "blessing others elevates my own spirit",
          "healing and comfort reaching people I love" to "kind intention radiates warmth",
          "my ability to contribute positivity into any room" to "one peaceful presence lifts everyone nearby",
          "seeing goodwill return in unexpected kindnesses" to "what we send out comes back multiplied"
        )
      )
      25 -> Triple(
        "Day 25: Cue the Magic",
        "Turn everyday cues—seeing an ambulance (thank for health), a happy family (thank for love), or a nice car (thank for abundance)—into gratitude triggers.",
        listOf(
          "everyday reminders sparking instant thankfulness" to "the world becomes a playground of gratitude",
          "celebrating other people's success without envy" to "appreciating abundance invites it into my life too",
          "staying alert to beauty in ordinary streets and rooms" to "wonder makes every hour vivid",
          "my habit of pivoting quickly back to appreciation" to "positive momentum grows stronger daily"
        )
      )
      26 -> Triple(
        "Day 26: Magically Transform Mistakes into Blessings",
        "Pick one past mistake and uncover 10 blessings that eventually grew out of what that experience taught you.",
        listOf(
          "wisdom gained from choices I once regretted" to "experience turned stumbles into stepping stones",
          "humility and empathy toward others' journeys" to "knowing imperfection makes me kinder",
          "better habits born from learning hard lessons" to "growth turns past errors into future protection",
          "complete self-compassion as I move forward" to "grace heals the past and frees today"
        )
      )
      27 -> Triple(
        "Day 27: The Magic Mirror",
        "Look at yourself in the mirror with genuine kindness and say 'Thank you' for three qualities you deeply appreciate within yourself.",
        listOf(
          "my perseverance and quiet courage" to "I kept showing up even when things felt uncertain",
          "the unique combination of gifts I bring to the world" to "no one else can offer my exact voice and heart",
          "my willingness to learn, heal, and grow daily" to "self-awareness is a lifelong treasure",
          "accepting and appreciating myself unconditionally today" to "inner peace begins in the mirror"
        )
      )
      else -> Triple(
        "Day 28: Remember the Magic",
        "Count the blessings you already received yesterday! Every day has its own unique magic when you look back with grateful eyes.",
        listOf(
          "every moment yesterday that made me smile" to "counting yesterday's wins reveals how rich life already is",
          "completing a full 28-day cycle of gratitude" to "daily practice rewires my brain for joy",
          "the steady peace now woven into my mornings" to "gratitude is no longer a task—it is who I am",
          "starting a fresh cycle with deeper wonder and faith" to "the magic continues every single sunrise"
        )
      )
    }

    val baseItems = listOf(
      "the peaceful early morning stillness welcoming Day $dayNum" to "starting in calm sets my vibration high",
      "my steady breath and vibrant physical energy" to "health powers everything I hope to achieve",
      "loving family, friends, and supportive souls" to "connection fills life with warmth",
      "safe shelter, clean water, and wholesome food" to "daily essentials are genuine abundance",
      "my mind's growing clarity and emotional resilience" to "inner peace anchors me through every hour",
      "opportunities to learn, create, and serve meaningfully" to "growth keeps my spirit alive"
    )
    val combined = (meta.third + baseItems).take(10)
    MagicLesson(
      day = dayNum,
      title = meta.first,
      lessonGuidance = meta.second,
      suggestions = meta.third,
      dailyBlessings = combined
    )
  }

  fun lessonForDay(day: Int): MagicLesson {
    val safeDay = ((day - 1).mod(28)) + 1
    return lessons.firstOrNull { it.day == safeDay } ?: lessons.first()
  }

  fun buildGratitudeItemsForDay(day: Int): List<GratitudeItem> {
    val lesson = lessonForDay(day)
    return lesson.dailyBlessings.mapIndexed { idx, (blessing, reason) ->
      GratitudeItem(
        id = idx + 1,
        blessing = blessing,
        reason = reason,
        isCompleted = false
      )
    }
  }
}
