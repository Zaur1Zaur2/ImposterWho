package com.example.myapplication

object MediumWords {
    val list: List<WordPairData> = listOf(
        WordPairData(
            category = "Food",
            word1Az = "Plov", word1En = "Pilaf",
            word2Az = "Dolma", word2En = "Dolma",
            hintAz = "Zəngin milli Azərbaycan yeməyi", hintEn = "Rich Azerbaijani national dish",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Düyü, zəfəran və qarağat-ət ilə bişirilən şah yemək.",
            def1En = "A royal dish prepared with rice, saffron, and meat.",
            def2Az = "Yarpaq və ya tərəvəz içinə ət qiyməsi bükülən yemək.",
            def2En = "Minced meat wrapped in grape leaves or stuffed vegetables."
        ),
        WordPairData(
            category = "Hospital",
            word1Az = "Rentgen", word1En = "X-Ray",
            word2Az = "MRT", word2En = "MRI Scan",
            hintAz = "Xəstəxanada daxili tibbi diaqnostika", hintEn = "Internal medical hospital diagnostic scan",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Şüalarla sümüklərin daxili şəklini çəkən müayinə.",
            def1En = "An examination imaging internal bones via radiation.",
            def2Az = "Magnit sahəsi ilə yumşaq toxumaları müayinə edən skan.",
            def2En = "A scan examining soft tissues using a magnetic field."
        ),
        WordPairData(
            category = "School",
            word1Az = "Teleskop", word1En = "Telescope",
            word2Az = "Mikroskop", word2En = "Microscope",
            hintAz = "Optik böyüdücü elmi cihaz", hintEn = "Optical magnifying scientific device",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Uzaq ulduzları və planetləri yaxından göstərən cihaz.",
            def1En = "A device showing distant stars and planets up close.",
            def2Az = "Kiçik hüceyrələri və zərrəcikləri böyüdən laboratoriya aləti.",
            def2En = "A lab tool magnifying tiny cells and particles."
        ),
        WordPairData(
            category = "School",
            word1Az = "Vulkan", word1En = "Volcano",
            word2Az = "Qeyzer", word2En = "Geyser",
            hintAz = "Yerin altından püskürən təbii hadisə", hintEn = "Natural event erupting from underground",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Dağ zirvəsindən isti lava və kül püskürən geoloji quruluş.",
            def1En = "Geological mountain erupting hot lava and ash.",
            def2Az = "Yerin altından müntəzəm fəvvarə vuran isti su mənbəyi.",
            def2En = "Hot water spring regularly spouting from underground."
        ),
        WordPairData(
            category = "School",
            word1Az = "Fotosintez", word1En = "Photosynthesis",
            word2Az = "Xlorofil", word2En = "Chlorophyll",
            hintAz = "Bitkilərdə günəş işığı ilə qida yaranması", hintEn = "Plant food production via sunlight",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Bitkilərin işıq enerjisi ilə oksigen və qida hazırlaması.",
            def1En = "Plants producing oxygen and food using light energy.",
            def2Az = "Bitkiyə yaşıl rəng verən və işığı udan piqment.",
            def2En = "Pigment giving plants green color and absorbing light."
        ),
        WordPairData(
            category = "Tech",
            word1Az = "Süni İntellekt", word1En = "Artificial Intelligence",
            word2Az = "Neyron Şəbəkə", word2En = "Neural Network",
            hintAz = "Ağıllı rəqəmsal kompyuter texnologiyası", hintEn = "Smart digital computer technology",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Kompyuterin insan kimi düşünmək və öyrənmək qabiliyyəti.",
            def1En = "Computer capability to think and learn like humans.",
            def2Az = "İnsan beynini təqlid edən alqoritm modeli.",
            def2En = "An algorithm model mimicking the human brain."
        ),
        WordPairData(
            category = "Culture",
            word1Az = "Piramida", word1En = "Pyramid",
            word2Az = "Sfenks", word2En = "Sphinx",
            hintAz = "Qədim Misir tarixi obyekti", hintEn = "Ancient Egyptian historical structure",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Fironlar üçün daşdan ucaldılmış üçbucaq formalı məqbərə.",
            def1En = "Triangular stone monument built for pharaohs.",
            def2Az = "İnsan başı və şir bədənli qədim daş heykəl.",
            def2En = "Ancient stone statue with a human head and lion body."
        ),
        WordPairData(
            category = "Profession",
            word1Az = "Mühəndis", word1En = "Engineer",
            word2Az = "Memar", word2En = "Architect",
            hintAz = "Tikinti və layihə mütəxəssisi", hintEn = "Construction and design expert",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Texniki hesablama və qurğular hazırlayan mütəxəssis.",
            def1En = "Specialist handling technical calculations and systems.",
            def2Az = "Binaların xarici və daxili görünüşünü layihələndirən şəxs.",
            def2En = "Person designing building exterior and interior blueprints."
        ),
        WordPairData(
            category = "City",
            word1Az = "Gəncə", word1En = "Ganja",
            word2Az = "Şəki", word2En = "Shaki",
            hintAz = "Qədim tarixi Azərbaycan şəhəri", hintEn = "Ancient historical Azerbaijani city",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Nizami Gəncəvinin vətəni olan böyük tarixi şəhər.",
            def1En = "Historic city known as the homeland of Nizami Ganjavi.",
            def2Az = "Xan sarayı və halvası ilə məşhur dağətəyi şəhər.",
            def2En = "Mountain-side city famous for Khan's Palace and Halva."
        ),
        WordPairData(
            category = "Culture",
            word1Az = "Şahmat", word1En = "Chess",
            word2Az = "Nərd", word2En = "Backgammon",
            hintAz = "Ənənəvi strateji masaüstü oyun", hintEn = "Traditional strategic board game",
            difficulty = Difficulty.MEDIUM,
            def1Az = "Şah, piyada və fiqurlarla xanalı lövhədə ağıl oyunu.",
            def1En = "A mind game on a checkered board with king, pawns, and pieces.",
            def2Az = "Zar ataraq daşları lövhədə evə yığmaq oyunu.",
            def2En = "A board game moving checkers based on dice rolls."
        )
    )
}
