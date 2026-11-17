// Rhonda Byrne's "The Magic" — Complete 28-Day Gratitude Curriculum
// Mirrored 1:1 with Android's TheMagicLessons.kt for full cross-platform parity.

(function () {
  const DAYS_1_TO_10 = [
    {
      day: 1,
      title: "Day 1: Count Your Blessings",
      lessonGuidance: "Rhonda Byrne teaches: List 10 blessings each morning, state why you are grateful, read each one, and feel it deeply while saying 'Thank you, thank you, thank you.'",
      suggestions: [
        { blessing: "the miracle of waking up with healthy breath", reason: "every breath gives me a fresh canvas to create joy today" },
        { blessing: "the sanctuary of my home", reason: "it keeps me warm, safe, and centered in peace" },
        { blessing: "my supportive loved ones", reason: "their care reminds me I am never alone" },
        { blessing: "clean pure water nourishing my body", reason: "it revitalizes every single cell with vibrant energy" }
      ],
      dailyBlessings: [
        { blessing: "my strong, healthy body and rhythmic breath", reason: "it allows me to experience life with vitality and boundless energy" },
        { blessing: "the peaceful early morning Brahma Muhurta hours", reason: "the silence connects me directly to divine stillness and clarity" },
        { blessing: "the pure glass of water placed in front of me", reason: "it cleanses my cells and absorbs my highest thoughts of joy" },
        { blessing: "my supportive family and mentors", reason: "their love grounds me and reminds me that I am always guided" },
        { blessing: "the abundance of money flowing into my life", reason: "it gives me the freedom to create, share, and live comfortably" },
        { blessing: "my sharp intuition and inner guidance", reason: "they guide positive decisions and open doors to synchronicities" },
        { blessing: "the wisdom of sacred books and teachers", reason: "they revealed the scientific and spiritual law of gratitude to me" },
        { blessing: "the sanctuary of my home", reason: "it provides safety, serenity, and sacred rest every night" },
        { blessing: "all challenges I have overcome in the past", reason: "they cultivated my inner resilience and made my spirit strong" },
        { blessing: "the limitless possibilities awaiting me today", reason: "the universe is orchestrating miracles on my behalf" }
      ]
    },
    {
      day: 2,
      title: "Day 2: The Magic Rock",
      lessonGuidance: "Lesson from 'The Magic': Hold your Magic Rock each night and recall the single best occurrence of the day—and carry that appreciation into tomorrow morning's 10 blessings.",
      suggestions: [
        { blessing: "peaceful deep sleep last night", reason: "my body and subconscious mind regenerated effortlessly while I rested" },
        { blessing: "the best moment from yesterday", reason: "it proved that goodness is actively flowing into my daily life" },
        { blessing: "my calm bedtime sanctuary", reason: "letting go of worry allows miracles to unfold overnight" },
        { blessing: "waking up with a peaceful heart", reason: "gratitude sets a high-vibration tone before my day even begins" }
      ],
      dailyBlessings: [
        { blessing: "the restful, restorative sleep I received last night", reason: "I woke up refreshed and mentally clear" },
        { blessing: "the best blessing that happened to me yesterday", reason: "remembering it fills my heart with immediate warmth" },
        { blessing: "my bedside quiet space for evening reflection", reason: "it helps my subconscious absorb peace before sleep" },
        { blessing: "the gift of a brand-new morning free from yesterday's stress", reason: "every sunrise resets my vibrational setpoint" },
        { blessing: "my mind's ability to focus on what went right", reason: "what I appreciate multiplies rapidly" },
        { blessing: "the comfort of my bed, pillow, and quiet room", reason: "they cradle me in safety night after night" },
        { blessing: "the subtle synchronicities that brightened yesterday", reason: "they show me the Universe is listening attentively" },
        { blessing: "my calm nervous system this morning", reason: "stillness empowers me to respond with wisdom" },
        { blessing: "the promise of another unforgettable blessing today", reason: "I expect wonderful news to find me effortlessly" },
        { blessing: "the simple Magic Rock practice of evening gratitude", reason: "it turns every night into a ceremony of thankfulness" }
      ]
    },
    {
      day: 3,
      title: "Day 3: Magical Relationships",
      lessonGuidance: "Lesson from 'The Magic': Bring to mind three key relationships and give genuine thanks for the finest qualities in each person—gratitude dissolves friction and deepens love.",
      suggestions: [
        { blessing: "the kindness and patience of my closest family", reason: "their warmth enriches my spirit every day" },
        { blessing: "a loyal friend who truly listens", reason: "having someone who understands me is priceless" },
        { blessing: "the mentors and teachers who believed in me", reason: "their encouragement shaped who I am becoming" },
        { blessing: "harmonious understanding with everyone I meet today", reason: "compassion opens every human heart" }
      ],
      dailyBlessings: [
        { blessing: "the unconditional love within my family", reason: "it gives me roots of security and wings of confidence" },
        { blessing: "my closest friends who bring laughter into my life", reason: "shared joy multiplies every good experience" },
        { blessing: "the patience and kindness of someone who helped me recently", reason: "their generosity restored my faith in human warmth" },
        { blessing: "my ability to see the best qualities in others", reason: "appreciating people transforms every relationship into harmony" },
        { blessing: "conversations that uplift and inspire my soul", reason: "meaningful connection nourishes my mental health" },
        { blessing: "the mentors who shared wisdom at the exact right time", reason: "their guidance saved me years of struggle" },
        { blessing: "peaceful resolution in any challenging relationship", reason: "gratitude softens hearts and invites mutual respect" },
        { blessing: "colleagues and neighbors who cooperate smoothly with me", reason: "friendly community makes daily life effortless" },
        { blessing: "the love I am learning to give myself unconditionally", reason: "self-respect sets the standard for healthy bonds" },
        { blessing: "every smiling stranger I cross paths with today", reason: "we are all connected in one radiant human family" }
      ]
    },
    {
      day: 4,
      title: "Day 4: Magical Health",
      lessonGuidance: "Lesson from 'The Magic': 'The gift of health is keeping me alive.' Give heartfelt thanks for your eyes, heart, lungs, legs, brain, and immune vitality.",
      suggestions: [
        { blessing: "my beating heart pumping life effortlessly", reason: "it works tirelessly every second out of pure love for me" },
        { blessing: "my clear eyesight taking in colors and light", reason: "seeing the beauty of the world is an extraordinary miracle" },
        { blessing: "my strong legs and hands serving me daily", reason: "mobility lets me move freely and embrace life" },
        { blessing: "my intelligent immune system protecting every cell", reason: "my body naturally knows how to heal and thrive" }
      ],
      dailyBlessings: [
        { blessing: "my faithful heart beating steadily every moment", reason: "it sustains my life without me ever having to ask" },
        { blessing: "my lungs breathing fresh oxygen smoothly", reason: "every inhale energizes my mind and clears fatigue" },
        { blessing: "my eyesight allowing me to see faces, nature, and words", reason: "visual beauty fills my life with endless wonder" },
        { blessing: "my ears hearing music, laughter, and sacred frequencies", reason: "sound connects me deeply to emotion and harmony" },
        { blessing: "my resilient legs, knees, and feet", reason: "they carry me effortlessly wherever I wish to go" },
        { blessing: "my capable hands and fingers", reason: "they let me write, work, embrace loved ones, and create" },
        { blessing: "my brilliant brain coordinating trillions of cells", reason: "it stores wisdom, imagines futures, and solves problems" },
        { blessing: "my digestive system turning food into pure energy", reason: "every meal fuels my vitality and strength" },
        { blessing: "my self-healing immune system", reason: "it quietly guards my wellness twenty-four hours a day" },
        { blessing: "the vibrant feeling of being alive right now", reason: "health is the greatest wealth I will ever possess" }
      ]
    },
    {
      day: 5,
      title: "Day 5: Magic Money",
      lessonGuidance: "Lesson from 'The Magic': Give thanks for all the money you have received throughout your entire life—complaining pushes money away, while gratitude magnetizes abundance.",
      suggestions: [
        { blessing: "every meal and home provided for me growing up", reason: "abundance supported me long before I earned a cent" },
        { blessing: "the steady flow of income and opportunities in my life", reason: "money enables me to live freely and give generously" },
        { blessing: "every bill I have ever been able to pay", reason: "paying a bill means I received valuable goods and services first" },
        { blessing: "unexpected financial blessings arriving effortlessly", reason: "the Universe has infinite channels of supply" }
      ],
      dailyBlessings: [
        { blessing: "all the food, shelter, and education provided during my childhood", reason: "I was richly supported long before earning money myself" },
        { blessing: "every paycheck, reward, and payment I have ever received", reason: "each one empowered me to build my life" },
        { blessing: "the clean electricity, internet, and water in my home", reason: "money quietly buys modern comforts kings once dreamed of" },
        { blessing: "my bank account and financial tools", reason: "they securely hold and circulate abundance for my goals" },
        { blessing: "every opportunity I have to create value for others", reason: "serving well naturally expands my prosperity" },
        { blessing: "discounts, gifts, and unexpected savings that came my way", reason: "abundance loves finding creative ways to reach me" },
        { blessing: "my growing wisdom around managing and multiplying wealth", reason: "stewardship turns income into lasting freedom" },
        { blessing: "the ability to pay my bills on time with a grateful heart", reason: "every payment reflects trust and prosperity in motion" },
        { blessing: "generous people who inspire an abundance mindset", reason: "seeing others thrive proves there is more than enough for all" },
        { blessing: "financial peace flowing to me from expected and unexpected sources", reason: "gratitude keeps the channel of prosperity wide open" }
      ]
    },
    {
      day: 6,
      title: "Day 6: Works Like Magic",
      lessonGuidance: "Lesson from 'The Magic': Imagine an invisible manager tracking every aspect of your work today—be genuinely thankful for your skills, workspace, tools, and colleagues.",
      suggestions: [
        { blessing: "my unique talents and problem-solving skills", reason: "they allow me to contribute meaningfully every day" },
        { blessing: "the technology and tools that amplify my work", reason: "my phone and computer connect me to limitless opportunity" },
        { blessing: "supportive teammates, clients, and collaborators", reason: "great work flows smoothly through mutual trust" },
        { blessing: "clarity, focus, and effortless productivity today", reason: "gratitude turns routine tasks into joyful mastery" }
      ],
      dailyBlessings: [
        { blessing: "my creative mind and learned skills", reason: "they turn ideas into tangible value for the world" },
        { blessing: "the reliable smartphone and laptop I work with", reason: "they let me build, communicate, and learn in seconds" },
        { blessing: "every task that sharpens my mastery and confidence", reason: "consistent effort builds unstoppable momentum" },
        { blessing: "helpful coworkers, clients, and partners", reason: "goodwill makes teamwork genuinely enjoyable" },
        { blessing: "a comfortable, safe space to focus and produce", reason: "calm surroundings elevate the quality of my work" },
        { blessing: "the internet connecting me to global knowledge and markets", reason: "opportunity is literally at my fingertips" },
        { blessing: "past successes that proved what I am capable of", reason: "evidence of my competence fuels bold action" },
        { blessing: "constructive feedback that helped me improve quickly", reason: "every insight accelerates my professional growth" },
        { blessing: "fair compensation and recognition for my contributions", reason: "my time and energy are deeply valued" },
        { blessing: "purposeful work that brings me satisfaction and growth", reason: "loving what I build makes every morning exciting" }
      ]
    },
    {
      day: 7,
      title: "Day 7: The Magical Way Out of Negativity",
      lessonGuidance: "Lesson from 'The Magic': Find 10 genuine blessings hidden inside any challenging situation—gratitude transmutes worry into clarity and forward motion.",
      suggestions: [
        { blessing: "the strength I discovered through past difficulties", reason: "pressure revealed resilience I never knew I had" },
        { blessing: "a challenge that redirected me toward a better path", reason: "closed doors protected me for higher opportunities" },
        { blessing: "my freedom to choose peaceful thoughts right now", reason: "no external circumstance can steal my inner stillness" },
        { blessing: "one silver lining inside today's biggest concern", reason: "looking for good always reveals hidden solutions" }
      ],
      dailyBlessings: [
        { blessing: "the hidden resilience I gained from difficult seasons", reason: "surviving hard days made my spirit unshakeable" },
        { blessing: "my ability to pause, breathe, and choose a calmer thought", reason: "mindfulness keeps me from reacting out of fear" },
        { blessing: "lessons learned from past mistakes", reason: "wisdom prevents me from repeating old patterns" },
        { blessing: "problems that revealed who my truest supporters are", reason: "authentic relationships shine brightest in tough moments" },
        { blessing: "delays that unexpectedly protected me from wrong turns", reason: "divine timing is wiser than my impatience" },
        { blessing: "my capacity to forgive myself and begin anew today", reason: "releasing guilt frees my energy for creation" },
        { blessing: "every obstacle that forced me to grow more creative", reason: "constraints often spark my best breakthroughs" },
        { blessing: "the temporary nature of every passing storm", reason: "this challenge too is shifting toward resolution" },
        { blessing: "quiet inner strength available to me in this very breath", reason: "peace is always one grateful thought away" },
        { blessing: "the silver lining unfolding right beneath the surface", reason: "gratitude lights up solutions I couldn't see before" }
      ]
    },
    {
      day: 8,
      title: "Day 8: The Magic Ingredient",
      lessonGuidance: "Lesson from 'The Magic': Before eating or drinking anything today, sprinkle 'magic dust' of gratitude—thank the soil, rain, sun, farmers, and hands that nourished you.",
      suggestions: [
        { blessing: "the fresh morning water hydrating my body", reason: "pure water is nature's simplest and greatest elixir" },
        { blessing: "the farmers, drivers, and grocers behind my meals", reason: "hundreds of caring hands brought nourishment to my table" },
        { blessing: "warm sunlight and rain that ripened every fruit and grain", reason: "nature generously feeds every living soul" },
        { blessing: "the flavor and vitality of wholesome food today", reason: "eating with gratitude nourishes body and spirit alike" }
      ],
      dailyBlessings: [
        { blessing: "the pure, refreshing water I drink each morning", reason: "it awakens my metabolism and refreshes my mind" },
        { blessing: "the farmers and harvesters who grew my food", reason: "their daily labor sustains my family's table" },
        { blessing: "sunlight, soil, and rain working together in nature", reason: "the earth effortlessly turns seeds into abundance" },
        { blessing: "the delivery drivers and local shopkeepers", reason: "they make fresh ingredients accessible every day" },
        { blessing: "warm tea, coffee, or nourishing breakfast flavors", reason: "small daily pleasures make mornings deeply comforting" },
        { blessing: "my sense of taste enjoying sweet, savory, and fresh meals", reason: "flavor turns basic survival into joyful experience" },
        { blessing: "a clean kitchen and utensils to prepare healthy food", reason: "caring for my meals is an act of self-love" },
        { blessing: "sharing meals with people I care about", reason: "food tastes twice as rich when enjoyed in gratitude" },
        { blessing: "having more than enough nourishment in my home", reason: "plenty dissolves scarcity and brings deep security" },
        { blessing: "the energy and clarity wholesome nutrition gives me", reason: "thankfulness before eating blesses every cell" }
      ]
    },
    {
      day: 9,
      title: "Day 9: The Money Magnet",
      lessonGuidance: "Lesson from 'The Magic': Write 'Thank You — Paid' on bills and feel gratitude for the electricity, internet, or service already delivered to you.",
      suggestions: [
        { blessing: "the electricity lighting and warming my home right now", reason: "flipping a switch brings instant comfort and power" },
        { blessing: "the internet connection linking me to the whole world", reason: "information and opportunity flow freely to my screen" },
        { blessing: "the water utilities delivering clean water to my tap", reason: "running clean water is a luxury billions pray for" },
        { blessing: "the joyful feeling of abundance covering every expense", reason: "thankfulness turns spending into confident circulation" }
      ],
      dailyBlessings: [
        { blessing: "the electric lights, refrigerator, and climate comfort in my home", reason: "modern energy serves me silently day and night" },
        { blessing: "high-speed internet and mobile connectivity", reason: "they empower me to learn, earn, and connect anywhere" },
        { blessing: "clean running water available at the turn of a tap", reason: "convenience gives me hours of free time daily" },
        { blessing: "every service provider whose work makes my life easier", reason: "paying them circulates prosperity through our community" },
        { blessing: "my growing confidence whenever I look at expenses", reason: "gratitude replaces financial worry with calm trust" },
        { blessing: "all goods and experiences I have already enjoyed", reason: "remembering value received creates an abundance mindset" },
        { blessing: "new streams of income opening effortlessly this month", reason: "aligned action attracts generous rewards" },
        { blessing: "the habit of saying 'Thank you' every time I spend or receive", reason: "joyful circulation keeps money flowing back multiplied" },
        { blessing: "safe roads, bridges, and public infrastructure I use daily", reason: "shared resources support every journey I take" },
        { blessing: "a calm, magnetic relationship with wealth and generosity", reason: "when I feel rich inside, life mirrors richness outside" }
      ]
    },
    {
      day: 10,
      title: "Day 10: Magic Dust Everyone",
      lessonGuidance: "Lesson from 'The Magic': Say 'Thank you' sincerely to 10 service workers today—cashiers, cleaners, drivers, receptionists—making eye contact so they feel truly valued.",
      suggestions: [
        { blessing: "sanitation and cleaning crews keeping our spaces spotless", reason: "their unseen work protects everyone's health and peace" },
        { blessing: "drivers, pilots, and transit workers moving us safely", reason: "their focus gets us home to the people we love" },
        { blessing: "cooks, baristas, and servers preparing food with care", reason: "their hospitality brightens busy days" },
        { blessing: "customer support helpers who solve problems patiently", reason: "a kind voice makes any hurdle effortless" }
      ],
      dailyBlessings: [
        { blessing: "the drivers and transit workers keeping our cities moving", reason: "they help people reunite with work and family safely" },
        { blessing: "cleaners and caretakers maintaining welcoming spaces", reason: "dignified effort keeps our environment peaceful" },
        { blessing: "baristas, chefs, and restaurant servers", reason: "warm service brings brightness to ordinary days" },
        { blessing: "delivery couriers bringing essentials right to my door", reason: "they save me precious time every week" },
        { blessing: "nurses, doctors, and pharmacists caring for our community", reason: "their dedication protects human life daily" },
        { blessing: "teachers and childcare mentors shaping young minds", reason: "patient guidance builds a kinder future" },
        { blessing: "grocery clerks and market staff", reason: "friendly smiles turn simple errands into connection" },
        { blessing: "engineers and technicians maintaining power and networks", reason: "quiet expertise keeps the modern world running" },
        { blessing: "my own sincere smile and 'Thank you' today", reason: "acknowledging someone's humanity lifts both our vibrations" },
        { blessing: "the ripple effect of kindness passing from person to person", reason: "magic dust transforms an ordinary day into grace" }
      ]
    }
  ];

  const DAYS_11_TO_28_META = {
    11: {
      title: "Day 11: A Magic Morning",
      lessonGuidance: "From opening your eyes to brushing your teeth, turn every morning motion into a conscious wave of gratitude—starting your day in magic.",
      suggestions: [
        { blessing: "waking up safely in a warm bed", reason: "every new dawn is an invitation to begin joyfully" },
        { blessing: "the sunlight streaming into my morning", reason: "natural light awakens clarity and optimism" },
        { blessing: "clean clothes, soap, and morning self-care", reason: "daily rituals prepare my spirit for excellence" },
        { blessing: "quiet morning minutes before checking messages", reason: "protecting my peace sets the tone for the whole day" }
      ]
    },
    12: {
      title: "Day 12: Magical People Who Made a Difference",
      lessonGuidance: "Remember three people who changed the course of your life at crucial moments and silently send heartfelt thanks to each one.",
      suggestions: [
        { blessing: "a teacher or elder who saw potential in me", reason: "their belief unlocked confidence I carry today" },
        { blessing: "a friend who stood by me during a turning point", reason: "loyal presence during change is priceless" },
        { blessing: "an author or speaker whose words awakened me", reason: "one right book can transform an entire destiny" },
        { blessing: "a family member whose sacrifices paved my way", reason: "their quiet generosity echoes through my life" }
      ]
    },
    13: {
      title: "Day 13: Make All Your Wishes Come True",
      lessonGuidance: "Write your top desires as though you have already received them, feeling the relief and joy—'Thank you, thank you, thank you!'",
      suggestions: [
        { blessing: "my dream life already unfolding behind the scenes", reason: "every grateful breath brings my vision closer" },
        { blessing: "abundant health, peace, and financial ease", reason: "I accept wellness and prosperity as my natural state" },
        { blessing: "joyful milestones ahead of me this year", reason: "anticipating good with certainty magnetizes results" },
        { blessing: "aligned opportunities meeting my prepared spirit", reason: "clarity and gratitude work together effortlessly" }
      ]
    },
    14: {
      title: "Day 14: Have a Magical Day",
      lessonGuidance: "Before stepping out, say 'Thank you' in advance for smooth travel, wonderful meetings, and great news coming your way today.",
      suggestions: [
        { blessing: "every task today flowing smoothly on schedule", reason: "calm preparation invites effortless timing" },
        { blessing: "wonderful news arriving at just the right hour", reason: "expectation tuned to joy notices every blessing" },
        { blessing: "clear communication in every call and message", reason: "goodwill makes collaboration easy" },
        { blessing: "safe journeys and green lights wherever I go", reason: "peaceful travel keeps my spirit light" }
      ]
    },
    15: {
      title: "Day 15: Magically Heal Your Relationships",
      lessonGuidance: "Release resentment toward anyone by listing 10 things you genuinely appreciate about them—forgiveness restores your own freedom.",
      suggestions: [
        { blessing: "the peace I feel when I release old hurt", reason: "forgiveness lightens my heart instantly" },
        { blessing: "lessons in patience and empathy I gained", reason: "understanding others deepens my own wisdom" },
        { blessing: "shared good memories worth honoring", reason: "choosing grace heals what anger cannot" },
        { blessing: "my own open, compassionate heart today", reason: "love returns to me multiplied" }
      ]
    },
    16: {
      title: "Day 16: Magic and Miracles in Health",
      lessonGuidance: "Give thanks for past healings, present vitality, and the radiant future health of every organ and cell in your body.",
      suggestions: [
        { blessing: "every past illness or injury my body healed from", reason: "my cells hold extraordinary regenerative intelligence" },
        { blessing: "deep, steady energy supporting my goals today", reason: "vibrant health lets me show up fully" },
        { blessing: "peaceful sleep and emotional balance", reason: "a calm mind protects physical longevity" },
        { blessing: "my commitment to nourish and move my body lovingly", reason: "respecting my body multiplies vitality" }
      ]
    },
    17: {
      title: "Day 17: The Magic Check",
      lessonGuidance: "Visualize an abundant Magic Check written to your name for something you deeply wish to fund or bless others with.",
      suggestions: [
        { blessing: "bold dreams that excite and expand my vision", reason: "giving permission to dream awakens creativity" },
        { blessing: "the joy of sharing abundance with loved ones", reason: "generosity feels deeply fulfilling" },
        { blessing: "unexpected avenues of prosperity opening up", reason: "resourcefulness creates new value" },
        { blessing: "trusting that my needs and goals are richly met", reason: "gratitude dissolves scarcity thinking" }
      ]
    },
    18: {
      title: "Day 18: The Magical To-Do List",
      lessonGuidance: "Choose your three highest-priority challenges today and give thanks in advance as if each one is already gracefully resolved.",
      suggestions: [
        { blessing: "clarity on what truly matters most today", reason: "focus replaces overwhelm with calm progress" },
        { blessing: "timely help arriving for my toughest tasks", reason: "support appears whenever I stay receptive" },
        { blessing: "creative breakthroughs solving lingering problems", reason: "a peaceful mind finds simpler paths" },
        { blessing: "the relief of completing today's priorities cleanly", reason: "momentum feels wonderful" }
      ]
    },
    19: {
      title: "Day 19: Magic Footsteps",
      lessonGuidance: "Take 100 mindful steps saying 'Thank you' with every footstep—grounding your awareness into the miracle of movement.",
      suggestions: [
        { blessing: "the earth beneath my feet supporting every step", reason: "grounding connects me to calm presence" },
        { blessing: "my balance, posture, and effortless movement", reason: "walking freely is a daily miracle" },
        { blessing: "fresh outdoor air and open sky above me", reason: "nature instantly restores perspective" },
        { blessing: "each small consistent step forward in my life", reason: "tiny daily habits build extraordinary destinies" }
      ]
    },
    20: {
      title: "Day 20: Heart Magic",
      lessonGuidance: "Place your hand over your chest, feel your heartbeat, and say 'Thank you' directly from the emotional center of your heart.",
      suggestions: [
        { blessing: "the radiant warmth in my heart center", reason: "heart coherence aligns thought with feeling" },
        { blessing: "moments of pure tenderness and compassion", reason: "an open heart attracts genuine love" },
        { blessing: "peace replacing anxiety whenever I breathe slowly", reason: "the heart remembers stillness instantly" },
        { blessing: "courage to live truthfully and kindly", reason: "sincerity makes every day meaningful" }
      ]
    },
    21: {
      title: "Day 21: Magnificent Outcomes",
      lessonGuidance: "Before three key events today, say 'Thank you for the magnificent outcome!'—calibrating your energy toward the best resolution.",
      suggestions: [
        { blessing: "favorable, win-win outcomes in today's meetings", reason: "positive expectation inspires confidence" },
        { blessing: "smooth timing and peaceful cooperation", reason: "calm leadership brings out the best in everyone" },
        { blessing: "surprising pleasant twists in my schedule", reason: "staying flexible lets better plans appear" },
        { blessing: "gratitude anchoring me regardless of small surprises", reason: "peace is my true superpower" }
      ]
    },
    22: {
      title: "Day 22: Before Your Very Eyes",
      lessonGuidance: "Carry your top written desires with you and give heartfelt thanks whenever signs of their manifestation appear before your eyes.",
      suggestions: [
        { blessing: "tangible progress already visible from my habits", reason: "consistency is quietly transforming my life" },
        { blessing: "meaningful coincidences reminding me I'm on track", reason: "awareness spots magic everywhere" },
        { blessing: "patience paired with joyful anticipation", reason: "trusting the process feels deeply peaceful" },
        { blessing: "celebrating small wins on the way to big dreams", reason: "appreciating steps speeds up the journey" }
      ]
    },
    23: {
      title: "Day 23: The Magical Air That You Breathe",
      lessonGuidance: "Take five conscious deep breaths today, thanking the trees, oceans, and atmosphere for the life-giving air filling your lungs.",
      suggestions: [
        { blessing: "the clean oxygen nourishing my brain and heart", reason: "every breath sustains consciousness effortlessly" },
        { blessing: "forests, plants, and oceans renewing Earth's air", reason: "nature generously keeps life in balance" },
        { blessing: "the calming effect of slow conscious breathing", reason: "deep breaths dissolve tension in seconds" },
        { blessing: "feeling deeply connected to the living planet", reason: "gratitude honors our shared home" }
      ]
    },
    24: {
      title: "Day 24: The Magic Wand",
      lessonGuidance: "Wave an invisible magic wand of goodwill today—silently blessing people you see with health, wealth, and happiness.",
      suggestions: [
        { blessing: "the joy of silently wishing strangers well", reason: "blessing others elevates my own spirit" },
        { blessing: "healing and comfort reaching people I love", reason: "kind intention radiates warmth" },
        { blessing: "my ability to contribute positivity into any room", reason: "one peaceful presence lifts everyone nearby" },
        { blessing: "seeing goodwill return in unexpected kindnesses", reason: "what we send out comes back multiplied" }
      ]
    },
    25: {
      title: "Day 25: Cue the Magic",
      lessonGuidance: "Turn everyday cues—seeing an ambulance (thank for health), a happy family (thank for love), or a nice car (thank for abundance)—into gratitude triggers.",
      suggestions: [
        { blessing: "everyday reminders sparking instant thankfulness", reason: "the world becomes a playground of gratitude" },
        { blessing: "celebrating other people's success without envy", reason: "appreciating abundance invites it into my life too" },
        { blessing: "staying alert to beauty in ordinary streets and rooms", reason: "wonder makes every hour vivid" },
        { blessing: "my habit of pivoting quickly back to appreciation", reason: "positive momentum grows stronger daily" }
      ]
    },
    26: {
      title: "Day 26: Magically Transform Mistakes into Blessings",
      lessonGuidance: "Pick one past mistake and uncover 10 blessings that eventually grew out of what that experience taught you.",
      suggestions: [
        { blessing: "wisdom gained from choices I once regretted", reason: "experience turned stumbles into stepping stones" },
        { blessing: "humility and empathy toward others' journeys", reason: "knowing imperfection makes me kinder" },
        { blessing: "better habits born from learning hard lessons", reason: "growth turns past errors into future protection" },
        { blessing: "complete self-compassion as I move forward", reason: "grace heals the past and frees today" }
      ]
    },
    27: {
      title: "Day 27: The Magic Mirror",
      lessonGuidance: "Look at yourself in the mirror with genuine kindness and say 'Thank you' for three qualities you deeply appreciate within yourself.",
      suggestions: [
        { blessing: "my perseverance and quiet courage", reason: "I kept showing up even when things felt uncertain" },
        { blessing: "the unique combination of gifts I bring to the world", reason: "no one else can offer my exact voice and heart" },
        { blessing: "my willingness to learn, heal, and grow daily", reason: "self-awareness is a lifelong treasure" },
        { blessing: "accepting and appreciating myself unconditionally today", reason: "inner peace begins in the mirror" }
      ]
    },
    28: {
      title: "Day 28: Remember the Magic",
      lessonGuidance: "Count the blessings you already received yesterday! Every day has its own unique magic when you look back with grateful eyes.",
      suggestions: [
        { blessing: "every moment yesterday that made me smile", reason: "counting yesterday's wins reveals how rich life already is" },
        { blessing: "completing a full 28-day cycle of gratitude", reason: "daily practice rewires my brain for joy" },
        { blessing: "the steady peace now woven into my mornings", reason: "gratitude is no longer a task—it is who I am" },
        { blessing: "starting a fresh cycle with deeper wonder and faith", reason: "the magic continues every single sunrise" }
      ]
    }
  };

  const ALL_LESSONS = [...DAYS_1_TO_10];
  for (let dayNum = 11; dayNum <= 28; dayNum++) {
    const meta = DAYS_11_TO_28_META[dayNum];
    const baseItems = [
      { blessing: `the peaceful early morning stillness welcoming Day ${dayNum}`, reason: "starting in calm sets my vibration high" },
      { blessing: "my steady breath and vibrant physical energy", reason: "health powers everything I hope to achieve" },
      { blessing: "loving family, friends, and supportive souls", reason: "connection fills life with warmth" },
      { blessing: "safe shelter, clean water, and wholesome food", reason: "daily essentials are genuine abundance" },
      { blessing: "my mind's growing clarity and emotional resilience", reason: "inner peace anchors me through every hour" },
      { blessing: "opportunities to learn, create, and serve meaningfully", reason: "growth keeps my spirit alive" }
    ];
    ALL_LESSONS.push({
      day: dayNum,
      title: meta.title,
      lessonGuidance: meta.lessonGuidance,
      suggestions: meta.suggestions,
      dailyBlessings: [...meta.suggestions, ...baseItems].slice(0, 10)
    });
  }

  window.MAGIC_LESSONS = ALL_LESSONS;

  window.getMagicLesson = function (day) {
    const num = parseInt(day, 10) || 1;
    const safeDay = (((num - 1) % 28) + 28) % 28 + 1;
    return ALL_LESSONS.find(l => l.day === safeDay) || ALL_LESSONS[0];
  };

  window.buildMagicBlessingsForDay = function (day) {
    const lesson = window.getMagicLesson(day);
    return lesson.dailyBlessings.map((item, idx) => ({
      id: idx + 1,
      blessing: item.blessing,
      reason: item.reason,
      done: false
    }));
  };
})();
