const flashcardGroups = {
  "Vogais": [
    { question: "あ", answer: "a" },
    { question: "い", answer: "i" },
    { question: "う", answer: "u" },
    { question: "え", answer: "e" },
    { question: "お", answer: "o" }
  ],
  "K": [
    { question: "か", answer: "ka" },
    { question: "き", answer: "ki" },
    { question: "く", answer: "ku" },
    { question: "け", answer: "ke" },
    { question: "こ", answer: "ko" }
  ],
  "S": [
    { question: "さ", answer: "sa" },
    { question: "し", answer: "shi" },
    { question: "す", answer: "su" },
    { question: "せ", answer: "se" },
    { question: "そ", answer: "so" }
  ],
  "T": [
    { question: "た", answer: "ta" },
    { question: "ち", answer: "chi" },
    { question: "つ", answer: "tsu" },
    { question: "て", answer: "te" },
    { question: "と", answer: "to" }
  ],
  "N": [
    { question: "な", answer: "na" },
    { question: "に", answer: "ni" },
    { question: "ぬ", answer: "nu" },
    { question: "ね", answer: "ne" },
    { question: "の", answer: "no" }
  ],
  "H": [
    { question: "は", answer: "ha" },
    { question: "ひ", answer: "hi" },
    { question: "ふ", answer: "fu" },
    { question: "へ", answer: "he" },
    { question: "ほ", answer: "ho" }
  ],
  "M": [
    { question: "ま", answer: "ma" },
    { question: "み", answer: "mi" },
    { question: "む", answer: "mu" },
    { question: "め", answer: "me" },
    { question: "も", answer: "mo" }
  ],
  "Y": [
    { question: "や", answer: "ya" },
    { question: "ゆ", answer: "yu" },
    { question: "よ", answer: "yo" }
  ],
  "R": [
    { question: "ら", answer: "ra" },
    { question: "り", answer: "ri" },
    { question: "る", answer: "ru" },
    { question: "れ", answer: "re" },
    { question: "ろ", answer: "ro" }
  ],
  "W": [
    { question: "わ", answer: "wa" },
    { question: "を", answer: "wo" }
  ],
  "N final": [
    { question: "ん", answer: "n" }
  ]
};

const katakanaGroups = {
  "Vogais": [
    { question: "ア", answer: "a" },
    { question: "イ", answer: "i" },
    { question: "ウ", answer: "u" },
    { question: "エ", answer: "e" },
    { question: "オ", answer: "o" }
  ],
  "K": [
    { question: "カ", answer: "ka" },
    { question: "キ", answer: "ki" },
    { question: "ク", answer: "ku" },
    { question: "ケ", answer: "ke" },
    { question: "コ", answer: "ko" }
  ],
  "S": [
    { question: "サ", answer: "sa" },
    { question: "シ", answer: "shi" },
    { question: "ス", answer: "su" },
    { question: "セ", answer: "se" },
    { question: "ソ", answer: "so" }
  ],
  "T": [
    { question: "タ", answer: "ta" },
    { question: "チ", answer: "chi" },
    { question: "ツ", answer: "tsu" },
    { question: "テ", answer: "te" },
    { question: "ト", answer: "to" }
  ],
  "N": [
    { question: "ナ", answer: "na" },
    { question: "ニ", answer: "ni" },
    { question: "ヌ", answer: "nu" },
    { question: "ネ", answer: "ne" },
    { question: "ノ", answer: "no" }
  ],
  "H": [
    { question: "ハ", answer: "ha" },
    { question: "ヒ", answer: "hi" },
    { question: "フ", answer: "fu" },
    { question: "ヘ", answer: "he" },
    { question: "ホ", answer: "ho" }
  ],
  "M": [
    { question: "マ", answer: "ma" },
    { question: "ミ", answer: "mi" },
    { question: "ム", answer: "mu" },
    { question: "メ", answer: "me" },
    { question: "モ", answer: "mo" }
  ],
  "Y": [
    { question: "ヤ", answer: "ya" },
    { question: "ユ", answer: "yu" },
    { question: "ヨ", answer: "yo" }
  ],
  "R": [
    { question: "ラ", answer: "ra" },
    { question: "リ", answer: "ri" },
    { question: "ル", answer: "ru" },
    { question: "レ", answer: "re" },
    { question: "ロ", answer: "ro" }
  ],
  "W": [
    { question: "ワ", answer: "wa" },
    { question: "ヲ", answer: "wo" }
  ],
  "N final": [
    { question: "ン", answer: "n" }
  ]
};


const hiraganaSentences = [
  {
    hiragana: "ねこが すきです",
    romaji: "neko ga sukidesu",
    breakdown_romaji: ["ne", "ko", "ga", "su", "ki", "de", "su"],
    breakdown_hiragana: ["ね", "こ", "が", "す", "き", "で", "す"]
  },
  {
    hiragana: "いぬは かわいいです",
    romaji: "inu wa kawaiidesu",
    breakdown_romaji: ["i", "nu", "wa", "ka", "wa", "ii", "de", "su"],
    breakdown_hiragana: ["い", "ぬ", "は", "か", "わ", "いい", "で", "す"]
  },
  {
    hiragana: "わたしは がくせいです",
    romaji: "watashi wa gakuseidesu",
    breakdown_romaji: ["wa", "ta", "shi", "wa", "ga", "ku", "se", "i", "de", "su"],
    breakdown_hiragana: ["わ", "た", "し", "は", "が", "く", "せ", "い", "で", "す"]
  },
  {
    hiragana: "これは ほんです",
    romaji: "kore wa hondesu",
    breakdown_romaji: ["ko", "re", "wa", "ho", "n", "de", "su"],
    breakdown_hiragana: ["こ", "れ", "は", "ほ", "ん", "で", "す"]
  },
  {
    hiragana: "ごはんを たべます",
    romaji: "gohan o tabemasu",
    breakdown_romaji: ["go", "ha", "n", "o", "ta", "be", "ma", "su"],
    breakdown_hiragana: ["ご", "は", "ん", "を", "た", "べ", "ま", "す"]
  },
  {
    hiragana: "みずを のみます",
    romaji: "mizu o nomimasu",
    breakdown_romaji: ["mi", "zu", "o", "no", "mi", "ma", "su"],
    breakdown_hiragana: ["み", "ず", "を", "の", "み", "ま", "す"]
  },
  {
    hiragana: "おはようございます",
    romaji: "ohayougozaimasu",
    breakdown_romaji: ["o", "ha", "yo", "u", "go", "za", "i", "ma", "su"],
    breakdown_hiragana: ["お", "は", "よ", "う", "ご", "ざ", "い", "ま", "す"]
  },
  {
    hiragana: "ありがとうございます",
    romaji: "arigatougozaimasu",
    breakdown_romaji: ["a", "ri", "ga", "to", "u", "go", "za", "i", "ma", "su"],
    breakdown_hiragana: ["あ", "り", "が", "と", "う", "ご", "ざ", "い", "ま", "す"]
  },
  {
    hiragana: "おげんきですか",
    romaji: "ogenkidesuka",
    breakdown_romaji: ["o", "ge", "n", "ki", "de", "su", "ka"],
    breakdown_hiragana: ["お", "げ", "ん", "き", "で", "す", "か"]
  },
  {
    hiragana: "きょうは あついです",
    romaji: "kyou wa atsuidesu",
    breakdown: ["kyo", "u", "wa", "a", "tsu", "i", "de", "su"],
    breakdown_hiragana: ["きょ", "う", "は", "あ", "つ", "い", "で", "す"]
  },
  {
    hiragana: "えきは どこですか",
    romaji: "eki wa dokodesuka",
    breakdown_romaji: ["e", "ki", "wa", "do", "ko", "de", "su", "ka"],
    breakdown_hiragana: ["え", "き", "は", "ど", "こ", "で", "す", "か"]
  },
  {
    hiragana: "わたしは にほんじんです",
    romaji: "watashi wa nihonjindesu",
    breakdown_romaji: ["wa", "ta", "shi", "wa", "ni", "ho", "n", "ji", "n", "de", "su"],
    breakdown_hiragana: ["わ", "た", "し", "は", "に", "ほ", "ん", "じ", "ん", "で", "す"]
  },
  {
    hiragana: "ともだちと はなします",
    romaji: "tomodachi to hanashimasu",
    breakdown_romaji: ["to", "mo", "da", "chi", "to", "ha", "na", "shi", "ma", "su"],
    breakdown_hiragana: ["と", "も", "だ", "ち", "と", "は", "な", "し", "ま", "す"]
  }
];

const kanjiGroups = {
  "Números": [
    { question: "一", answer: "Um" },
    { question: "二", answer: "Dois" },
    { question: "三", answer: "Três" },
    { question: "四", answer: "Quatro" },
    { question: "五", answer: "Cinco" },
    { question: "六", answer: "Seis" },
    { question: "七", answer: "Sete" },
    { question: "八", answer: "Oito" },
    { question: "九", answer: "Nove" },
    { question: "十", answer: "Dez" },
    { question: "百", answer: "Cem" },
    { question: "千", answer: "Mil" }
  ],
  "Natureza": [
    { question: "日", answer: "Sol / Dia" },
    { question: "月", answer: "Lua / Mês" },
    { question: "火", answer: "Fogo" },
    { question: "水", answer: "Água" },
    { question: "木", answer: "Árvore / Madeira" },
    { question: "金", answer: "Ouro / Dinheiro" },
    { question: "土", answer: "Terra / Solo" },
    { question: "山", answer: "Montanha" },
    { question: "川", answer: "Rio" },
    { question: "花", answer: "Flor" }
  ],
  "Pessoas e Corpo": [
    { question: "人", answer: "Pessoa" },
    { question: "男", answer: "Homem" },
    { question: "女", answer: "Mulher" },
    { question: "子", answer: "Criança" },
    { question: "目", answer: "Olho" },
    { question: "耳", answer: "Ouvido" },
    { question: "口", answer: "Boca" },
    { question: "手", answer: "Mão" },
    { question: "足", answer: "Pé / Perna" }
  ],
  "Direções e Tamanho": [
    { question: "上", answer: "Cima / Acima" },
    { question: "下", answer: "Baixo / Abaixo" },
    { question: "左", answer: "Esquerda" },
    { question: "右", answer: "Direita" },
    { question: "中", answer: "Dentro / Meio" },
    { question: "大", answer: "Grande" },
    { question: "小", answer: "Pequeno" }
  ],
  "Tempo e Verbos Básicos": [
    { question: "年", answer: "Ano" },
    { question: "今", answer: "Agora" },
    { question: "見", answer: "Ver" },
    { question: "行", answer: "Ir" },
    { question: "来", answer: "Vir" },
    { question: "食", answer: "Comer" },
    { question: "飲", answer: "Beber" },
    { question: "言", answer: "Dizer" },
    { question: "学", answer: "Estudar" }
  ],
  "Família": [
    { question: "父", answer: "Pai" },
    { question: "母", answer: "Mãe" },
    { question: "兄", answer: "Irmão mais velho" },
    { question: "弟", answer: "Irmão mais novo" },
    { question: "姉", answer: "Irmã mais velha" },
    { question: "妹", answer: "Irmã mais nova" },
    { question: "家", answer: "Casa / Família" },
    { question: "友", answer: "Amigo" }
  ],
  "Lugares Comuns": [
    { question: "学校", answer: "Escola" },
    { question: "駅", answer: "Estação" },
    { question: "店", answer: "Loja" },
    { question: "病院", answer: "Hospital" },
    { question: "会社", answer: "Empresa" },
    { question: "家", answer: "Casa" },
    { question: "道", answer: "Caminho / Rua" },
    { question: "国", answer: "País" },
    { question: "町", answer: "Cidade / Vila" }
  ],
  "Objetos e Coisas": [
    { question: "車", answer: "Carro" },
    { question: "本", answer: "Livro" },
    { question: "紙", answer: "Papel" },
    { question: "机", answer: "Mesa" },
    { question: "椅子", answer: "Cadeira" },
    { question: "電話", answer: "Telefone" },
    { question: "時計", answer: "Relógio" }
  ],
  "Cores e Sensações": [
    { question: "白", answer: "Branco" },
    { question: "黒", answer: "Preto" },
    { question: "赤", answer: "Vermelho" },
    { question: "青", answer: "Azul" },
    { question: "黄", answer: "Amarelo" },
    { question: "新", answer: "Novo" },
    { question: "古", answer: "Velho" }
  ],
  "Dias e Tempo": [
    { question: "朝", answer: "Manhã" },
    { question: "昼", answer: "Tarde / Meio-dia" },
    { question: "夜", answer: "Noite" },
    { question: "週", answer: "Semana" },
    { question: "月", answer: "Mês" },
    { question: "日", answer: "Dia" },
    { question: "時", answer: "Hora / Tempo" },
    { question: "分", answer: "Minuto" }
  ],
  "Adjetivos Comuns": [
    { question: "高", answer: "Alto / Caro" },
    { question: "安", answer: "Barato / Seguro" },
    { question: "早", answer: "Rápido / Cedo" },
    { question: "長", answer: "Longo / Líder" },
    { question: "短", answer: "Curto" },
    { question: "強", answer: "Forte" },
    { question: "弱", answer: "Fraco" }
  ]
};
