  let flashcards = [];
  let currentIndex = 0;
  let type = "hiragana";
  let sentenceIndex = 0;
  let selected = [];

  function generateGroupSelection(type) {
    const container = document.getElementById("group-selection");
    container.innerHTML = "";

    const groups = type === "katakana" ? katakanaGroups : flashcardGroups;

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

  // Inicia o treino com os grupos selecionados
  function startTraining(type) {
    const checkboxes = document.querySelectorAll("#group-selection input[type=checkbox]:checked");
    const selectedGroups = Array.from(checkboxes).map(cb => cb.value);

    if (selectedGroups.length === 0) {
      alert("Selecione pelo menos um grupo para começar o treino.");
      return;
    }

    const groups = type === "katakana" ? katakanaGroups : flashcardGroups;
    flashcards = selectedGroups.flatMap(group => groups[group]);
    shuffle(flashcards);
    currentIndex = 0;

    document.getElementById("menu").style.display = "none";
    document.getElementById("flashcard-container").style.display = "block";
    showFlashcard();
  }

  // Mostra o flashcard atual com múltipla escolha
  function showFlashcard() {
    const card = flashcards[currentIndex];
    const questionEl = document.getElementById("question");
    const answerEl = document.getElementById("answer");

    questionEl.textContent = card.question;
    answerEl.textContent = "";

    // Gera 3 respostas erradas aleatórias
    const wrongAnswers = getRandomWrongAnswers(card.answer, 3);
    const allAnswers = shuffle([card.answer, ...wrongAnswers]);

    // Cria botões de alternativas
    answerEl.innerHTML = "";
    allAnswers.forEach(option => {
      const btn = document.createElement("button");
      btn.textContent = option;
      btn.className = "option-btn";
      btn.onclick = () => checkAnswer(btn, option, card.answer);
      answerEl.appendChild(btn);
    });
  }

  // Verifica se a resposta está certa
  function checkAnswer(button, chosen, correct) {
    const buttons = document.querySelectorAll(".option-btn");
    buttons.forEach(btn => (btn.disabled = true));

    if (chosen === correct) {
      button.classList.add("correct");
    } else {
      button.classList.add("wrong");
      // Destaca a resposta correta
      buttons.forEach(btn => {
        if (btn.textContent === correct) btn.classList.add("correct");
      });
    }
  }

  // Mostra a resposta (útil se quiser ver sem clicar)
  function showAnswer() {
    const card = flashcards[currentIndex];
    alert("Resposta correta: " + card.answer);
  }

  // Vai para o próximo flashcard
  function nextFlashcard() {
    currentIndex++;
    if (currentIndex >= flashcards.length) {
      alert("Treino finalizado! Você completou todos os flashcards.");
      location.reload();
      return;
    }
    showFlashcard();
  }

  function returnMenu() {
      // Esconde a tela de flashcards e mostra o menu principal
      document.getElementById("flashcard-container").style.display = "none";
      document.getElementById("menu").style.display = "none";
      document.getElementById("sentence-container").style.display = "none";
      document.getElementById("start-menu").style.display = "block";
      document.getElementById("menu-katakana").style.display = "none";

      // Opcional, mas recomendado: Reseta o estado do treino
      flashcards = [];
      currentIndex = 0;

      // Desmarca todas as caixas de seleção para um novo começo
      const checkboxes = document.querySelectorAll("#group-selection input[type=checkbox]");
      checkboxes.forEach(checkbox => {
          checkbox.checked = false;
      });
  }

  // Retorna 3 respostas erradas aleatórias
  function getRandomWrongAnswers(correct, count) {
    const allAnswers = flashcards.map(f => f.answer);
    const filtered = allAnswers.filter(ans => ans !== correct);
    shuffle(filtered);
    return filtered.slice(0, count);
  }

  // Função para embaralhar arrays
  function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

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
      alert("Modo Kanji ainda não implementado.");
    });
  }

  function startSentenceTraining() {
    document.getElementById("start-menu").style.display = "none";
    document.getElementById("sentence-container").style.display = "block";

    shuffle(hiraganaSentences);
    
    sentenceIndex = 0;
    showSentence();
}

  function showSentence() {
    const sentence = hiraganaSentences[sentenceIndex];
    const questionEl = document.getElementById("sentence-question");
    const optionsEl = document.getElementById("syllable-options");
    const selectedEl = document.getElementById("selected-order");

    questionEl.textContent = sentence.sentence;
    optionsEl.innerHTML = "";
    selectedEl.innerHTML = "";
    selected = [];

    const shuffled = shuffle([...sentence.breakdown]);

    shuffled.forEach((syllable) => {
      const btn = document.createElement("button");
      btn.textContent = syllable;
      btn.className = "option-btn"; // O botão começa normal

      btn.onclick = () => {
        
        // Verifica se o botão JÁ TEM a classe 'selected'
        if (btn.classList.contains('selected')) {
          // Se sim, remove a classe (reativa visualmente)
          btn.classList.remove('selected');
          
          // E remove do array 'selected'
          const index = selected.indexOf(syllable);
          if (index !== -1) selected.splice(index, 1);
          
        } 
        // Se o botão AINDA NÃO TEM a classe 'selected'
        else {
          // Adiciona a sílaba ao array
          selected.push(syllable);
          // Adiciona a classe (desativa visualmente)
          btn.classList.add('selected');
        }

        // Atualiza visualização
        selectedEl.textContent = selected.join(" - ");
      };

      optionsEl.appendChild(btn);
    });
  }


  function checkSentence() {
    const correct = hiraganaSentences[sentenceIndex].breakdown;
    const isCorrect = JSON.stringify(selected) === JSON.stringify(correct);

    if (isCorrect) {
      alert("✅ Correto!");
    } else {
      alert("❌ Errado!\nCorreto seria: " + correct.join(" - "));
    }
  }

  function nextSentence() {
    sentenceIndex++;
    if (sentenceIndex >= hiraganaSentences.length) {
      alert("Treino finalizado!");
      location.reload();
    } else {
      showSentence();
    }
  }

  let currentUser = null;

// Registrar novo usuário
  function register() {
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;

    if (!user || !pass) return alert("Preencha usuário e senha!");
    if (localStorage.getItem(user)) return alert("Usuário já existe!");

    const data = { password: pass, stats: { acertos: 0, erros: 0 } };
    localStorage.setItem(user, JSON.stringify(data));
    alert("Conta criada!");
  }

  function login() {
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;

    const data = localStorage.getItem(user);
    if (!data) return alert("Usuário não encontrado!");
    
    const parsed = JSON.parse(data);
    if (parsed.password !== pass) return alert("Senha incorreta!");

    currentUser = user;
    alert(`Bem-vindo, ${user}!`);
    
    // Esconde a tela de login
    document.getElementById("account-container").style.display = "none";
    
    // Mostra o nome do usuário no topo
    document.getElementById("user-info").style.display = "block"; // NOVO
    document.getElementById("username-display").textContent = user; // NOVO

    // Exibe o menu principal
    document.getElementById("start-menu").style.display = "block";
  }


  // Exemplo de salvar progresso
  function save() {
    if (!currentUser) return;
    const data = JSON.parse(localStorage.getItem(currentUser));
    data.stats.acertos += acertos;
    data.stats.erros += erros;
    localStorage.setItem(currentUser, JSON.stringify(data));
  }


  document.getElementById("start-training").addEventListener("click", () => startTraining(type));
  document.getElementById("show-answer").addEventListener("click", showAnswer);
  document.getElementById("next").addEventListener("click", nextFlashcard);
  document.getElementById("return").addEventListener("click", returnMenu);
  document.getElementById("sentence-btn").addEventListener("click", startSentenceTraining);
  document.getElementById("check-sentence").addEventListener("click", checkSentence);
  document.getElementById("next-sentence").addEventListener("click", nextSentence);
  document.getElementById("return-sentence").addEventListener("click", returnMenu);

  generateGroupSelection();
  generateStartMenu();
