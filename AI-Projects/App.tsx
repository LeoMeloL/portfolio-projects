import React, { useState } from 'react';
import { GameMode, Persona } from './types';
import { PERSONAS, TOPICS } from './constants';
import ClassicSession from './components/ClassicSession';
import LiveSession from './components/LiveSession';

function App() {
  const [mode, setMode] = useState<GameMode>(GameMode.MENU);
  const [selectedPersona, setSelectedPersona] = useState<Persona>(PERSONAS[0]);
  const [selectedTopic, setSelectedTopic] = useState<string>(TOPICS[0]);

  const handleStartGame = (gameMode: GameMode) => {
    // Requires API Key to be present
    if (!process.env.API_KEY) {
      alert("API Key is missing in process.env");
      return;
    }
    setMode(gameMode);
  };

  const renderMenu = () => (
    <div className="max-w-4xl mx-auto py-12 px-6">
      <header className="text-center mb-16">
        <h1 className="text-5xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary mb-4">
          TriviaAI
        </h1>
        <p className="text-xl text-slate-400">
          Challenge an AI personality to a battle of wits.
        </p>
      </header>

      <section className="mb-12">
        <h3 className="text-lg font-semibold text-slate-300 mb-4 uppercase tracking-wider">1. Choose your Host</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {PERSONAS.map(p => (
            <div 
              key={p.id}
              onClick={() => setSelectedPersona(p)}
              className={`p-4 rounded-xl border-2 cursor-pointer transition-all flex items-center gap-4 ${selectedPersona.id === p.id ? 'border-primary bg-primary/10' : 'border-slate-700 bg-slate-800 hover:border-slate-600'}`}
            >
              <div className={`w-12 h-12 rounded-full ${p.avatarColor} flex items-center justify-center font-bold text-white`}>
                {p.name[0]}
              </div>
              <div>
                <h4 className="font-bold text-white">{p.name}</h4>
                <p className="text-sm text-slate-400">{p.description}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      <section className="mb-12">
        <h3 className="text-lg font-semibold text-slate-300 mb-4 uppercase tracking-wider">2. Choose Topic (Classic Mode)</h3>
        <div className="flex flex-wrap gap-2">
          {TOPICS.map(t => (
            <button
              key={t}
              onClick={() => setSelectedTopic(t)}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${selectedTopic === t ? 'bg-secondary text-white' : 'bg-slate-800 text-slate-400 hover:bg-slate-700'}`}
            >
              {t}
            </button>
          ))}
        </div>
      </section>

      <section>
        <h3 className="text-lg font-semibold text-slate-300 mb-4 uppercase tracking-wider">3. Start Game</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
           {/* Classic Card */}
           <div className="group relative bg-slate-800 rounded-2xl p-6 border border-slate-700 hover:border-primary transition-all">
              <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-blue-500 to-cyan-500 rounded-t-2xl"></div>
              <h3 className="text-2xl font-bold mb-2 text-white">Classic Mode</h3>
              <p className="text-slate-400 mb-6 min-h-[48px]">
                Standard trivia format. AI generates questions using Search Grounding and reads them out loud.
              </p>
              <button 
                onClick={() => handleStartGame(GameMode.CLASSIC)}
                className="w-full py-3 bg-slate-700 hover:bg-primary text-white font-bold rounded-lg transition-colors"
              >
                Play Classic
              </button>
           </div>

           {/* Live Card */}
           <div className="group relative bg-slate-800 rounded-2xl p-6 border border-slate-700 hover:border-secondary transition-all">
              <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-pink-500 to-purple-500 rounded-t-2xl"></div>
              <div className="flex justify-between items-start">
                  <h3 className="text-2xl font-bold mb-2 text-white">Voice Mode</h3>
                  <span className="px-2 py-1 bg-red-500/20 text-red-400 text-xs font-bold rounded uppercase border border-red-500/50">Live API</span>
              </div>
              <p className="text-slate-400 mb-6 min-h-[48px]">
                Have a real-time voice conversation with the host. No buttons, just talk!
              </p>
              <button 
                onClick={() => handleStartGame(GameMode.LIVE)}
                className="w-full py-3 bg-slate-700 hover:bg-secondary text-white font-bold rounded-lg transition-colors flex items-center justify-center gap-2"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z" /></svg>
                Start Voice Call
              </button>
           </div>
        </div>
      </section>
    </div>
  );

  return (
    <div className="min-h-screen bg-dark text-slate-100 selection:bg-primary selection:text-white">
      {mode === GameMode.MENU && renderMenu()}
      {mode === GameMode.CLASSIC && (
        <ClassicSession 
          persona={selectedPersona} 
          topic={selectedTopic} 
          onExit={() => setMode(GameMode.MENU)} 
        />
      )}
      {mode === GameMode.LIVE && (
        <LiveSession 
          persona={selectedPersona} 
          onExit={() => setMode(GameMode.MENU)} 
        />
      )}
    </div>
  );
}

export default App;