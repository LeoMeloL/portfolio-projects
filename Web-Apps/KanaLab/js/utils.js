/**
 * Mostra uma animação de feedback (certo/errado).
 * @param {boolean} isCorrect - Se a resposta foi correta (true) or errada (false).
 * @param {function} onAnimationEndCallback - A função a ser chamada quando a animação terminar.
 */


// Variáveis globais compartilhadas
let flashcards = [];
let currentIndex = 0;
let type = "hiragana";
let sentenceIndex = 0;
let selected = [];
let currentUser = null;
let lives = 3;
let loseFlag = 0;
let isInverseMode = false;
let isTestMode = false;
let isHardMode = false;

let mult = 1;

let maxXP = 100;
let currentXP = 0;
let level = 1;

const xpRewards = {
  hiragana: 10,
  katakana: 15,
  kanji: 50,
  sentence: 30,
  test: 500
};

// Função para embaralhar arrays
function shuffle(array) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

function updateLivesDisplay() {
  const livesDisplays = document.querySelectorAll('.lives-display');
  
  let hearts = "";
  for (let i = 0; i < lives; i++) {
    hearts += "❤️";
  }
  livesDisplays.forEach(display => {
    display.innerHTML = hearts;
  });
}

/**
 * FUNÇÃO PRINCIPAL
 * Mostra uma animação de popup genérica.
 * @param {string} text - O texto a ser exibido (ex: "✔", "✖", "Congrats!").
 * @param {string} styleClass - A classe CSS (ex: 'correct', 'wrong', 'congrats').
 * @param {function} onAnimationEndCallback - Ação a ser executada no final.
 */
function showPopupAnimation(text, styleClass, onAnimationEndCallback) {
  const feedbackPopup = document.getElementById('feedback-popup');
  
  // Define a duração total da animação (deve ser o mesmo do CSS)
  const ANIMATION_DURATION = 1200; // 1.2 segundos

  // 1. Define o conteúdo e a cor
  feedbackPopup.textContent = text;
  
  // Limpa classes de estilo antigas
  feedbackPopup.classList.remove('correct', 'wrong', 'congrats');
  
  // Adiciona a nova classe de estilo
  if (styleClass) {
    feedbackPopup.classList.add(styleClass);
  }

  // 2. Dispara a animação
  feedbackPopup.classList.add('show');

  // 3. Agenda a limpeza e a próxima ação
  setTimeout(() => {
    // Esconde o popup
    feedbackPopup.classList.remove('show');
    
    // Executa a função de "próximo passo" (se ela foi fornecida)
    if (onAnimationEndCallback) {
      onAnimationEndCallback();
    }
  }, ANIMATION_DURATION);
}

function showFeedbackAnimation(isCorrect, onAnimationEndCallback) {
  if (isCorrect) {
    showPopupAnimation("✔", "correct", onAnimationEndCallback);
  } else {
    showPopupAnimation("✖", "wrong", onAnimationEndCallback);
  }
}

function showCongratsAnimation(onAnimationEndCallback) {
  showPopupAnimation("Congrats!", "congrats", onAnimationEndCallback);

}

function showLevelUpAnimation(onAnimationEndCallback) {
  showPopupAnimation("Level Up!", "level up", onAnimationEndCallback)
}

function addXP(amount) {
  amount *= ((mult * 0.5) + (lives * 0.3)) * (isInverseMode ? 2 : 1) * (isHardMode ? 3 : 1);
  currentXP += amount;

  if (currentXP >= maxXP) {
    currentXP -= maxXP;
    level++;  
    maxXP = Math.floor(maxXP * 1.2); // aumenta o requisito a cada nível
    showLevelUpAnimation(returnMenu);
  }

  const xpPercent = (currentXP / maxXP) * 100;
  document.getElementById("xp-bar").style.width = xpPercent + "%";
  document.getElementById("xp-text").textContent = `Nível ${level} — ${currentXP} / ${maxXP} XP`;
}

function rewardManager(){

  const reward = xpRewards[type] || 0;
  addXP(reward);
  
}
