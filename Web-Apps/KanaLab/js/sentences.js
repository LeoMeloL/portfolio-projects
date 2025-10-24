function startSentenceTraining() {
  document.getElementById("start-menu").style.display = "none";
  document.getElementById("sentence-container").style.display = "block";
  shuffle(hiraganaSentences);
  sentenceIndex = 0;
  showSentence();
}

// --- MODIFICADA ---
function showSentence() {
  const sentence = hiraganaSentences[sentenceIndex];
  const questionEl = document.getElementById("sentence-question");
  const optionsEl = document.getElementById("syllable-options");
  const selectedEl = document.getElementById("selected-order");

  let questionText;
  let buttonBreakdown;

  if (isInverseMode) {
    questionText = sentence.romaji;
    buttonBreakdown = [...sentence.breakdown_hiragana];
  } else {
    questionText = sentence.hiragana;
    buttonBreakdown = [...sentence.breakdown_romaji];
  }

  questionEl.textContent = questionText;
  optionsEl.innerHTML = "";
  selectedEl.innerHTML = "";
  selected = [];

  const shuffled = shuffle(buttonBreakdown);

  shuffled.forEach((syllable, index) => { 
    const btn = document.createElement("button");
    btn.textContent = syllable;
    btn.className = "option-btn";
    btn.id = `syllable-btn-${index}`; 

    btn.onclick = () => selectSyllable(btn, syllable, selectedEl);
    optionsEl.appendChild(btn);
  });
}

function selectSyllable(btn, syllable, selectedEl) {
  if (btn.classList.contains('selected')) {
    btn.classList.remove('selected');
    
    const itemIndex = selected.findIndex(item => item.id === btn.id);
    if (itemIndex !== -1) {
      selected.splice(itemIndex, 1);
    }

  } else {
    btn.classList.add('selected');
    
    selected.push({ syllable: syllable, id: btn.id });
  }

  selectedEl.textContent = selected.map(item => item.syllable).join(" - ");
}

function checkSentence() {
  let correctBreakdown;

  if (isInverseMode) {
    correctBreakdown = hiraganaSentences[sentenceIndex].breakdown_hiragana;
  } else {
    correctBreakdown = hiraganaSentences[sentenceIndex].breakdown_romaji;
  }
  
  const selectedSyllables = selected.map(item => item.syllable);
  const isCorrect = JSON.stringify(selectedSyllables) === JSON.stringify(correctBreakdown);

  if (isCorrect) {
    alert("✅ Correto!");
  } else {
    alert("❌ Errado!\nCorreto seria: " + correctBreakdown.join(" - "));
  }
}

function nextSentence() {
  sentenceIndex++;
  if (sentenceIndex >= hiraganaSentences.length) {
    alert("Treino finalizado!");
    returnMenu();
  } else {
    showSentence();
  }
}
