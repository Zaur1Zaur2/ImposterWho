package com.example.myapplication

object HardWords {
    val list: List<WordPairData> = listOf(
        WordPairData(
            category = "School",
            word1Az = "DNT Molekulu", word1En = "DNA Molecule",
            word2Az = "RNT Molekulu", word2En = "RNA Molecule",
            hintAz = "Məktəb biologiyasında genetik kodu daşıyan nüklein turşusu", hintEn = "Nucleic acid carrying genetic code in school biology",
            difficulty = Difficulty.HARD,
            def1Az = "İkiqat sarmal strukturlu, genetik irsi məlumatı saxlayan molekul.",
            def1En = "A double-helix molecule storing hereditary genetic info.",
            def2Az = "Tək zəncirli, zülal sintezində genetik informasiyanı köçürən molekul.",
            def2En = "A single-stranded molecule transferring genetic info in protein synthesis."
        ),
        WordPairData(
            category = "School",
            word1Az = "Periyodik Cədvəl", word1En = "Periodic Table",
            word2Az = "Kimyəvi Element", word2En = "Chemical Element",
            hintAz = "Məktəb kimya fənninin təməl struktur cədvəli", hintEn = "Fundamental classification in school chemistry",
            difficulty = Difficulty.HARD,
            def1Az = "Mendeleyev tərəfindən atom çəkilərinə görə düzülmüş elementlər cədvəli.",
            def1En = "Mendeleev's table ordering elements by atomic structure.",
            def2Az = "Eyni növ atomlardan ibarət, sadə kimyəvi maddə növü.",
            def2En = "A pure chemical substance consisting of one type of atom."
        ),
        WordPairData(
            category = "School",
            word1Az = "Atom Nüvəsi", word1En = "Atomic Nucleus",
            word2Az = "Elektron Buludu", word2En = "Electron Cloud",
            hintAz = "Məktəb fizika və kimyasında atomun daxili hissəsi", hintEn = "Internal atomic component in physics and chemistry",
            difficulty = Difficulty.HARD,
            def1Az = "Atomun mərkəzində proton və neytronlardan ibarət sıx kütlə.",
            def1En = "Dense central core of an atom containing protons and neutrons.",
            def2Az = "Atom nüvəsinin ətrafında elektronların hərəkət etdiyi fəza sahəsi.",
            def2En = "Area around nucleus where electrons orbit."
        ),
        WordPairData(
            category = "School",
            word1Az = "Ekvator", word1En = "Equator",
            word2Az = "Məridian", word2En = "Meridian",
            hintAz = "Məktəb coğrafiyasında Yerin şərti xətti", hintEn = "Imaginary Earth line in school geography",
            difficulty = Difficulty.HARD,
            def1Az = "Yeri Şimal və Cənub yarımkürələrinə bölən ən böyük en dairəsi.",
            def1En = "Greatest parallel dividing Earth into Northern and Southern hemispheres.",
            def2Az = "Şimal və Cənub qütblərini birləşdirən şərti coğrafi uzunluq xətti.",
            def2En = "Imaginary longitudinal line connecting North and South poles."
        ),
        WordPairData(
            category = "School",
            word1Az = "Paralelipiped", word1En = "Parallelepiped",
            word2Az = "Silindr", word2En = "Cylinder",
            hintAz = "Məktəb həndəsəsində fəza fiquru", hintEn = "Spatial geometric figure in geometry class",
            difficulty = Difficulty.HARD,
            def1Az = "Üzləri paralelogramlardan ibarət altıüzlü 3D həndəsi fiqur.",
            def1En = "A 3D geometric figure with six parallelogram faces.",
            def2Az = "Oturacaqları dairə olan, bükülmüş düzbucaqlı formalı cisim.",
            def2En = "A 3D spatial body with two parallel circular bases."
        ),
        WordPairData(
            category = "Tech",
            word1Az = "Blockchain", word1En = "Blockchain",
            word2Az = "Kriptoqrafik Həş", word2En = "Cryptographic Hash",
            hintAz = "Mərkəzsizləşdirilmiş rəqəmsal şəbəkə təhlükəsizliyi", hintEn = "Decentralized digital network security concept",
            difficulty = Difficulty.HARD,
            def1Az = "Məlumatların bloklar zəncirində dəyişdirilməz saxlandığı paylanmış baza.",
            def1En = "A distributed ledger where data blocks are immutably linked.",
            def2Az = "İxtiyari ölçülü məlumatı sabit uzunluqlu koda çevirən riyazi funksiya.",
            def2En = "A mathematical function converting data to a fixed-length string."
        ),
        WordPairData(
            category = "Space",
            word1Az = "Qravitasiya Dalğası", word1En = "Gravitational Wave",
            word2Az = "Qaranlıq Maddə", word2En = "Dark Matter",
            hintAz = "Astrofizika və fəza elmində nəzəri hadisə", hintEn = "Theoretical phenomenon in astrophysics and space science",
            difficulty = Difficulty.HARD,
            def1Az = "Nəhəng kütləli kosmik toqquşmalardan fəza-zamanda yaranan ləpələr.",
            def1En = "Ripples in spacetime caused by massive cosmic collisions.",
            def2Az = "İşıq saçmayan, lakin cazibə qüvvəsi ilə fəzada hiss olunan gizli kütlə.",
            def2En = "Invisible matter detected solely through gravitational effects."
        ),
        WordPairData(
            category = "Hospital",
            word1Az = "Sümük İliyi Transferi", word1En = "Bone Marrow Transplant",
            word2Az = "Kök Hüceyrə Terapiyası", word2En = "Stem Cell Therapy",
            hintAz = "Tibb elmində regenerative transplantasiya müalicəsi", hintEn = "Regenerative transplant treatment in medical science",
            difficulty = Difficulty.HARD,
            def1Az = "Qan xəstəliklərində zədələnmiş iliyn sağlam donor hüceyrəsi ilə əvəzlənməsi.",
            def1En = "Replacing damaged marrow with healthy donor cells in blood disorders.",
            def2Az = "Bərpa olunmayan toxumaları yeniləmək üçün diferensiasiya etməyən hüceyrə müalicəsi.",
            def2En = "Using undifferentiated cells to repair damaged tissues."
        ),
        WordPairData(
            category = "School",
            word1Az = "Kombinatorika", word1En = "Combinatorics",
            word2Az = "Differensial Tənliklər", word2En = "Differential Equations",
            hintAz = "Ali riyaziyyat və ehtimal nəzəriyyəsi bölməsi", hintEn = "Advanced mathematics and calculus branch",
            difficulty = Difficulty.HARD,
            def1Az = "Çoxluqların elementlərinin düzülüş və birləşmələrini öyrənən riyaziyyat.",
            def1En = "Math studying arrangements and combinations of finite sets.",
            def2Az = "Naməlum funksiya ilə onun törəmələri arasındakı əlaqə tənliyi.",
            def2En = "An equation relating an unknown function to its derivatives."
        ),
        WordPairData(
            category = "Culture",
            word1Az = "Simfoniya", word1En = "Symphony",
            word2Az = "Opera", word2En = "Opera",
            hintAz = "Klassik musiqi və teatr sənəti janrı", hintEn = "Classical music and theatrical art genre",
            difficulty = Difficulty.HARD,
            def1Az = "Böyük simfonik orkestr üçün yazılmış 4 hissəli musiqi əsəri.",
            def1En = "A four-movement musical composition written for a full orchestra.",
            def2Az = "Musiqi, vokal oxuma və dramatik teatr səhnəsinin sintezi olan əsər.",
            def2En = "A theatrical art form combining singing, music, and acting."
        )
    )
}
