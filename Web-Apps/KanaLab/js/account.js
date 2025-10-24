function register() {
  const user = document.getElementById("username").value;
  const pass = document.getElementById("password").value;

  if (!user || !pass) return alert("Preencha usuário e senha!");
  if (localStorage.getItem(user)) return alert("Usuário já existe!");

  const data = { password: pass, stats: { acertos: 0, erros: 0 } };
  localStorage.setItem(user, JSON.stringify(data));
  alert("Conta criada!");
}

// no seu account.js
function login() {
  const user = document.getElementById("username").value;
  const pass = document.getElementById("password").value;

  const data = localStorage.getItem(user);
  if (!data) return alert("Usuário não encontrado!");

  const parsed = JSON.parse(data);
  if (parsed.password !== pass) return alert("Senha incorreta!");

  currentUser = user;
  
  // --- ADICIONE ESTA LINHA ---
  localStorage.setItem('activeUser', user); // Salva quem está logado

  // O resto da sua função continua igual
  document.getElementById("account-container").style.display = "none";
  document.getElementById("user-info").style.display = "block";
  document.getElementById("username-display").textContent = user;
  document.getElementById("start-menu").style.display = "block";
  loadStats();
}
function save() {
  if (!currentUser) return;
  const rawData = localStorage.getItem(currentUser);
  let data = rawData ? JSON.parse(rawData) : {}; 

  if (!data.stats) {
    data.stats = {};
  }

  if (loseFlag == 0){
    data.stats[type] = (data.stats[type] || 0) + 1;
  }

  data.stats.losses = (data.stats.losses || 0) + loseFlag;

  data.stats.currentXP = currentXP;
  data.stats.maxXP = maxXP;
  data.stats.level = level;

  if (!data.tests) {
    data.tests = {};
  }
  data.tests.hiragana = tests.hiragana || 0;
  data.tests.katakana = tests.katakana || 0;
  data.tests.kanji = tests.kanji || 0;

  localStorage.setItem(currentUser, JSON.stringify(data));
}

// no seu account.js
function logout() {
  currentUser = null;
  localStorage.removeItem('activeUser'); // Remove o usuário logado
  
  // Recarrega a página para voltar ao estado inicial (tela de login)
  location.reload(); 
}
