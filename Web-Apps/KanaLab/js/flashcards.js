function generateGroupSelection(type) {
  const container = document.getElementById("group-selection");
  container.innerHTML = "";
  
  // --- CORREÇÃO APLICADA AQUI ---
  let groups;
  if (type === "katakana") {
    groups = katakanaGroups;
  } else if (type === "kanji") {
    groups = kanjiGroups;
  } else {
    groups = flashcardGroups;
  }

  for (const groupName in groups) {
    const label = document.createElement("label");
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.value = groupName;
    label.appendChild(checkbox);
    label.appendChild(document.createTextNode(" " + groupName));
    container.appendChild(label);
    container.appendChild(document.createElement("br"));
  }
}

//StartTraining function:
//1. Responsavel por preparar o baralho de perguntas e respostas
//2. Criar o deck com 10 perguntas
function startTraining(type) {
  isTestMode = false;
  const checkboxes = document.querySelectorAll("#group-selection input[type=checkbox]:checked");
  const selectedGroups = Array.from(checkboxes).map(cb => cb.value);
  if (selectedGroups.length === 0) return alert("Selecione pelo menos um grupo.");

  mult = selectedGroups.length;

  let groups;
  if (type === "katakana") {
    groups = katakanaGroups;
  } else if (type === "kanji") {
    groups = kanjiGroups;
  } else {
    groups = flashcardGroups;
  }

  let initialPool = selectedGroups.flatMap(group => groups[group]);

  if (initialPool.length === 0) {
    return alert("Os grupos selecionados não contêm questões.");
  }

  const targetSize = 10;
  let finalFlashcards = [];

  if (initialPool.length >= targetSize) {

    shuffle(initialPool);
    finalFlashcards = initialPool.slice(0, targetSize);

  } else {

    let numCopies = Math.floor(targetSize / initialPool.length);
    for (let i = 0; i < numCopies; i++) {
      finalFlashcards.push(...initialPool);
    }

    let remaining = targetSize - finalFlashcards.length;
    if (remaining > 0) {
      shuffle(initialPool);
      finalFlashcards.push(...initialPool.slice(0, remaining));
    }

    shuffle(finalFlashcards);
  }

  flashcards = finalFlashcards; 

  currentIndex = 0;

  document.getElementById("menu").style.display = "none";
  document.getElementById("flashcard-container").style.display = "block";
  showFlashcard();
}


function showFlashcard() {

  const card = flashcards[currentIndex];
  const questionEl = document.getElementById("question");
  const answerEl = document.getElementById("answer");

  const counterEl = document.getElementById("question-counter");
  const currentQuestion = currentIndex + 1; 
  const totalQuestions = flashcards.length;
  counterEl.textContent = `${currentQuestion} / ${totalQuestions}`;

  let questionText, correctAnswer, wrongAnswerPoolType;

  if (isInverseMode) {
    questionText = card.answer;
    correctAnswer = card.question;
    wrongAnswerPoolType = 'question';
  } else {
    questionText = card.question;
    correctAnswer = card.answer;
    wrongAnswerPoolType = 'answer';
  }

  questionEl.textContent = questionText;
  answerEl.innerHTML = "";

  if (isHardMode && !isInverseMode && type !== 'kanji'){
    const input = document.createElement("input");
    input.type = "text";
    input.id = "hard-mode-input";
    input.placeholder = "Digite o romaji";
    input.autocomplete = "off";

    const checkBtn = document.createElement("button");
    checkBtn.textContent = "Verificar";
    checkBtn.className = "option-btn"; // Reusa o estilo
    checkBtn.style.width = "100%"; // Garante que ocupe a largura

    checkBtn.onclick = () => checkAnswer(null, input.value, correctAnswer);

    input.onkeyup = (event) => {
      if (event.key === "Enter") {
        checkBtn.click();
      }
    };

    answerEl.appendChild(input);
    answerEl.appendChild(checkBtn);

    setTimeout(() => input.focus(), 100);
  } else {
    let numWrongAnswers;
    if (isTestMode) {
      numWrongAnswers = 7; 
    } else {
      numWrongAnswers = 3; 
    }

    const wrongAnswers = getRandomWrongAnswers(correctAnswer, numWrongAnswers, wrongAnswerPoolType);
    const allAnswers = shuffle([correctAnswer, ...wrongAnswers]);

    allAnswers.forEach(option => {
      const btn = document.createElement("button");
      btn.textContent = option;
      btn.className = "option-btn";
      btn.onclick = () => checkAnswer(btn, option, correctAnswer);
      answerEl.appendChild(btn);
    });

  }
  
  updateLivesDisplay();
}
// Esta função agora é chamada de duas formas:
// 1. (Botão, "a", "a") -> modo normal
// 2. (null, "texto digitado", "a") -> modo hard
function checkAnswer(button, chosen, correct) {
  
  // Limpa e formata a resposta do usuário
  const chosenAnswer = chosen.toLowerCase().trim();
  const correctAnswer = correct.toLowerCase().trim();
  
  let isCorrect = (chosenAnswer === correctAnswer);
  const isLastQuestion = (currentIndex + 1 >= flashcards.length);

  // --- Lógica de UI (Muda baseado no modo) ---
  if (isHardMode && !isInverseMode && type !== 'kanji') {
    // MODO HARD (sem botões)
    const input = document.getElementById("hard-mode-input");
    input.disabled = true; // Desativa o input
    document.querySelector("#answer .option-btn").disabled = true; // Desativa o botão
    
    // Mostra a resposta correta no input se errar
    if (!isCorrect) {
      input.style.color = "red";
      input.value = `Errado! Correto: ${correct}`;
    } else {
      input.style.color = "green";
    }

  } else {
    // MODO NORMAL (com botões)
    const buttons = document.querySelectorAll(".option-btn");
    buttons.forEach(btn => (btn.disabled = true));
    
    if (isCorrect) {
      if(button) button.classList.add("correct");
    } else {
      if(button) button.classList.add("wrong");
      buttons.forEach(btn => {
        if (btn.textContent === correct) btn.classList.add("correct");
      });
    }
  }

  // --- Lógica de JOGO (Idêntica para os dois modos) ---
  if (isCorrect) {
    if (isLastQuestion) {
      rewardManager();
      save();
      setTimeout(() => showCongratsAnimation(returnMenu), 500); // Espera 0.5s
    } else {
      showFeedbackAnimation(true, nextFlashcard);
    }
  } else {
    // Errou
    lives -= 1;
    updateLivesDisplay();
    if (lives <= 0) {
      loseFlag = 1;
      save();
      showFeedbackAnimation(false, returnMenu);
      return;
    }
    // Errou, mas não é game over
    showFeedbackAnimation(false, nextFlashcard);
  }
}

function nextFlashcard() {
  currentIndex++;
  if (currentIndex >= flashcards.length) {
    if (isTestMode) {
      tests[type] += 1;
    }
    rewardManager();
    save();
    return;
  }
  showFlashcard();
}

/**
 * Retorna respostas erradas aleatórias.
 * @param {string} correct - A resposta correta (para excluir).
 * @param {number} count - O número de respostas erradas a obter.
 * @param {string} poolType - O tipo de pool: 'question' (símbolos) ou 'answer' (pronúncias).
 */

function getRandomWrongAnswers(correct, count, poolType) {
  // 1. Determina a fonte de dados completa
  let sourceData;
  if (type === "katakana") {
    sourceData = katakanaGroups;
  } else if (type === "kanji") {
    sourceData = kanjiGroups;
  } else {
    sourceData = flashcardGroups; // Hiragana
  }

  // 2. Transforma todos os grupos em um único array de cartas
  const allCards = Object.values(sourceData).flatMap(group => group);
  
  // 3. Mapeia o pool correto (ex: 'question' ou 'answer')
  const allAnswers = allCards.map(f => f[poolType]);
  
  // 4. Filtra por respostas únicas e remove a correta
  const uniqueWrongAnswers = [...new Set(allAnswers)]; // Pega valores únicos
  const filtered = uniqueWrongAnswers.filter(ans => ans !== correct);
  
  // 5. Embaralha e retorna o número solicitado
  shuffle(filtered);
  return filtered.slice(0, count);
}
function showAnswer() {
  const card = flashcards[currentIndex];
  
  if (isInverseMode) {
    // No modo invertido, a resposta correta é o símbolo
    alert("Resposta correta: " + card.question);
  } else {
    // No modo normal, a resposta correta é a pronúncia
    alert("Resposta correta: " + card.answer);
  }
}