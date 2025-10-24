let testMenu = document.getElementById("test-menu");

function generateStartMenu() {
  const startMenu = document.getElementById("start-menu");
  const mainMenu = document.getElementById("menu");

  document.getElementById("hiragana-btn").addEventListener("click", () => {
    startMenu.style.display = "none";
    mainMenu.style.display = "block";
    type = "hiragana";
    document.querySelector("#menu h1").textContent = "Treino de Hiragana";
    generateGroupSelection(type);
  });

  document.getElementById("katakana-btn").addEventListener("click", () => {
    startMenu.style.display = "none";
    mainMenu.style.display = "block";
    type = "katakana";
    document.querySelector("#menu h1").textContent = "Treino de Katakana";
    generateGroupSelection(type);
  });

  document.getElementById("kanji-btn").addEventListener("click", () => {
    startMenu.style.display = "none";
    mainMenu.style.display = "block";
    type = "kanji";
    document.querySelector("#menu h1").textContent = "Treino de Kanji";
    generateGroupSelection(type);
  });
}

function returnMenu() {
  document.getElementById("flashcard-container").style.display = "none";
  document.getElementById("menu").style.display = "none";
  document.getElementById("sentence-container").style.display = "none";
  document.getElementById("start-menu").style.display = "block";
  testMenu.style.display = "block";
  
  flashcards = [];
  currentIndex = 0;
  loseFlag = 0;
  lives = 3;
  const checkboxes = document.querySelectorAll("#group-selection input[type=checkbox]");
  checkboxes.forEach(checkbox => checkbox.checked = false);
}

function loadStats() {
  if (!currentUser) return;

  const rawData = localStorage.getItem(currentUser);
  if (!rawData) return;

  const data = JSON.parse(rawData);
  const stats = data.stats || {};

  // 🔹 Carrega XP e nível salvos
  currentXP = stats.currentXP || 0;
  maxXP = stats.maxXP || 100;
  level = stats.level || 1;

  // 🔹 Atualiza visualmente a barra de XP (se ela existir no HTML)
  const xpPercent = (currentXP / maxXP) * 100;
  const xpBar = document.getElementById("xp-bar");
  const xpText = document.getElementById("xp-text");

  if (xpBar && xpText) {
    xpBar.style.width = xpPercent + "%";
    xpText.textContent = `Nível ${level} — ${currentXP} / ${maxXP} XP`;
  }

  console.log(`✅ Dados de XP carregados: XP=${currentXP}, Max=${maxXP}, Nível=${level}`);
}


function showStatsScreen() {
  if (!currentUser) {
    return;
  } // Não faz nada se não houver usuário logado

  // Pega os dados do localStorage (usando a mesma lógica segura da função save())
  const rawData = localStorage.getItem(currentUser);
  let data = rawData ? JSON.parse(rawData) : {};

  // Garante que 'stats' exista
  if (!data.stats) {
    data.stats = {};
  }
  
  // Extrai os valores (usando "|| 0" para evitar 'undefined')
  const hiraganaWins = data.stats.hiragana || 0;
  const katakanaWins = data.stats.katakana || 0;
  const kanjiWins = data.stats.kanji || 0;
  const sentencesWins = data.stats.sentences || 0; // Adicionei 'sentences'
  const hiraganaTestsWins = data.tests ? data.tests.hiragana || 0 : 0;
  const katakanaTestsWins = data.tests ? data.tests.katakana || 0 : 0;
  const kanjiTestsWins = data.tests ? data.tests.kanji || 0 : 0;
  const losses = data.stats.losses || 0;

  // Atualiza o HTML com os valores
  document.getElementById('stats-username').textContent = currentUser;
  document.getElementById('stats-hiragana').textContent = hiraganaWins;
  document.getElementById('stats-katakana').textContent = katakanaWins;
  document.getElementById('stats-kanji').textContent = kanjiWins;
  document.getElementById('stats-sentences').textContent = sentencesWins;
  document.getElementById('stats-losses').textContent = losses;
  document.getElementById('tests-hiragana').textContent = hiraganaTestsWins;
  document.getElementById('tests-katakana').textContent = katakanaTestsWins;
  document.getElementById('tests-kanji').textContent = kanjiTestsWins;
  // Mostra o modal (o overlay)
  document.getElementById('stats-modal-overlay').style.display = 'flex';
}

function hideStatsScreen() {
  document.getElementById('stats-modal-overlay').style.display = 'none';
}

document.addEventListener("DOMContentLoaded", () => {
  const savedUser = localStorage.getItem('activeUser');

  if (savedUser) {
    currentUser = savedUser;
    document.getElementById("account-container").style.display = "none";
    document.getElementById("user-info").style.display = "block";
    document.getElementById("username-display").textContent = currentUser;
    document.getElementById("start-menu").style.display = "block";
    loadStats();
  } else {
    document.getElementById("account-container").style.display = "block";
    document.getElementById("start-menu").style.display = "none";
    document.getElementById("user-info").style.display = "none";

  }
  generateGroupSelection();
  generateStartMenu();

  document.getElementById("hiragana-btn").addEventListener("click", () => {
    testMenu.style.display = "none";
  });

  document.getElementById("return").addEventListener("click", () => {
    testMenu.style.display = "block";
  });
  document.getElementById("start-training").addEventListener("click", () => {
    isHardMode = document.getElementById("hard-mode-check").checked;
    startTraining(type);
  });
  document.getElementById("show-answer").addEventListener("click", showAnswer);
  document.getElementById("next").addEventListener("click", nextFlashcard);
  document.getElementById("return").addEventListener("click", returnMenu);
  document.getElementById("sentence-btn").addEventListener("click", startSentenceTraining);
  document.getElementById("check-sentence").addEventListener("click", checkSentence);
  document.getElementById("next-sentence").addEventListener("click", nextSentence);
  document.getElementById("return-sentence").addEventListener("click", returnMenu);
  document.getElementById("user-info").addEventListener("click", showStatsScreen);
  document.getElementById("test-hiragana-btn").addEventListener("click", () => {
    document.getElementById("start-menu").style.display = "none";
    testMenu.style.display = "none";
    startTest("hiragana");
  });
  document.getElementById("stats-close-btn").addEventListener("click", hideStatsScreen);
  const invertedCheckbox = document.getElementById("inverted-mode");
  invertedCheckbox.addEventListener("change", () => {
    isInverseMode = invertedCheckbox.checked;
  });
  document.getElementById("stats-modal-overlay").addEventListener("click", (event) => {
    if (event.target === document.getElementById("stats-modal-overlay")) {
      hideStatsScreen();
    }
  });
});

