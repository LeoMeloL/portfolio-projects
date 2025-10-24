/**
 * Inicia uma Prova Focada (25 questões, todos os grupos)
 */

const tests = {
  hiragana: 0,
  katakana: 0,
  kanji: 0
};



function startTest(type) {
  isTestMode = true;
  let groups;
  if (type === "katakana") {
    groups = katakanaGroups;
  } else if (type === "kanji") {
    groups = kanjiGroups;
  } else {
    groups = flashcardGroups;
  }

  let initialPool = Object.values(groups).flatMap(group => group);

  if (initialPool.length === 0) {
    return alert("Não há questões disponíveis para este teste.");
  }

  const targetSize = 25; 
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