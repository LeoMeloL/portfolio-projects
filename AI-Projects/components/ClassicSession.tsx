import React, { useState, useEffect, useRef } from 'react';
import { Persona, TriviaQuestion } from '../types';
import { generateQuestions, generateSpeech } from '../services/geminiService';
import { base64ToUint8Array, decodeAudioData } from '../services/audioUtils';
import GroundingSources from './GroundingSources';
import AudioVisualizer from './AudioVisualizer';

interface Props {
  persona: Persona;
  topic: string;
  onExit: () => void;
}

const ClassicSession: React.FC<Props> = ({ persona, topic, onExit }) => {
  const [loading, setLoading] = useState(true);
  const [questions, setQuestions] = useState<TriviaQuestion[]>([]);
  const [groundingChunks, setGroundingChunks] = useState<any[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
  const [score, setScore] = useState(0);
  const [audioContext, setAudioContext] = useState<AudioContext | null>(null);
  const [analyser, setAnalyser] = useState<AnalyserNode | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);

  // Init audio context
  useEffect(() => {
    const ctx = new (window.AudioContext || (window as any).webkitAudioContext)({ sampleRate: 24000 });
    const anal = ctx.createAnalyser();
    anal.fftSize = 256;
    setAudioContext(ctx);
    setAnalyser(anal);

    return () => {
      ctx.close();
    };
  }, []);

  // Fetch questions
  useEffect(() => {
    let mounted = true;
    const loadGame = async () => {
      try {
        const data = await generateQuestions(topic, 5);
        if (mounted) {
          setQuestions(data.questions);
          setGroundingChunks(data.groundingChunks || []);
          setLoading(false);
          // Auto-speak first question once loaded
          speakText(data.questions[0].question);
        }
      } catch (err) {
        console.error(err);
        if (mounted) setLoading(false); // In a real app, show error state
      }
    };
    loadGame();
    return () => { mounted = false; };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [topic]);

  const speakText = async (text: string) => {
    if (!audioContext || !analyser) return;
    setIsPlaying(true);
    
    const base64 = await generateSpeech(text, persona.voiceName);
    if (!base64) {
      setIsPlaying(false);
      return;
    }

    const bytes = base64ToUint8Array(base64);
    const buffer = await decodeAudioData(bytes, audioContext);
    
    const source = audioContext.createBufferSource();
    source.buffer = buffer;
    source.connect(analyser);
    analyser.connect(audioContext.destination);
    
    source.onended = () => setIsPlaying(false);
    source.start();
  };

  const handleAnswer = (option: string) => {
    if (selectedOption) return; // Prevent double answer

    setSelectedOption(option);
    const currentQ = questions[currentIndex];
    const correct = option === currentQ.correctAnswer;
    setIsCorrect(correct);

    if (correct) {
      setScore(s => s + 10);
      speakText(`Correct! ${currentQ.explanation}`);
    } else {
      speakText(`Incorrect. The answer was ${currentQ.correctAnswer}. ${currentQ.explanation}`);
    }
  };

  const nextQuestion = () => {
    if (currentIndex < questions.length - 1) {
      setCurrentIndex(c => c + 1);
      setSelectedOption(null);
      setIsCorrect(null);
      speakText(questions[currentIndex + 1].question);
    } else {
        // Game Over logic could go here
        speakText("Game over! Thanks for playing.");
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-full text-center p-8">
        <div className="w-16 h-16 border-4 border-primary border-t-transparent rounded-full animate-spin mb-4"></div>
        <h2 className="text-xl font-bold animate-pulse">Generating Trivia...</h2>
        <p className="text-slate-400 mt-2">Consulting Google Search for facts...</p>
      </div>
    );
  }

  const currentQ = questions[currentIndex];
  const isGameOver = currentIndex === questions.length - 1 && selectedOption !== null;

  return (
    <div className="max-w-3xl mx-auto w-full h-full flex flex-col p-4">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <button onClick={onExit} className="text-slate-400 hover:text-white transition">
            &larr; Exit
        </button>
        <div className="bg-slate-800 px-4 py-2 rounded-full border border-slate-700">
            Score: <span className="text-primary font-bold">{score}</span>
        </div>
      </div>

      {/* Host Area */}
      <div className="flex gap-4 items-center mb-6 bg-slate-800/50 p-4 rounded-xl border border-slate-700/50">
        <div className={`w-16 h-16 rounded-full ${persona.avatarColor} flex items-center justify-center text-2xl shadow-lg`}>
          {persona.name[0]}
        </div>
        <div className="flex-1">
            <h3 className="font-bold text-lg">{persona.name}</h3>
            <p className="text-slate-400 text-sm">{isPlaying ? "Speaking..." : "Waiting..."}</p>
        </div>
        <div className="w-32 hidden sm:block">
            <AudioVisualizer analyser={analyser} />
        </div>
      </div>

      {/* Question Card */}
      <div className="flex-1 flex flex-col justify-center">
        {currentQ ? (
          <div className="bg-slate-800 rounded-2xl p-6 md:p-8 shadow-2xl border border-slate-700">
             <div className="flex justify-between items-center mb-4">
                <span className="text-xs font-bold uppercase tracking-wider text-slate-500">Question {currentIndex + 1} of {questions.length}</span>
                <span className="text-xs text-slate-500">{topic}</span>
             </div>
             
             <h2 className="text-2xl font-bold mb-8 leading-relaxed">{currentQ.question}</h2>

             <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {currentQ.options.map((opt, idx) => {
                    let btnClass = "p-4 rounded-xl border-2 text-left transition-all duration-200 font-medium ";
                    if (selectedOption === null) {
                        btnClass += "border-slate-600 hover:border-primary hover:bg-slate-700 bg-slate-700/30";
                    } else if (opt === currentQ.correctAnswer) {
                         btnClass += "border-green-500 bg-green-500/10 text-green-400";
                    } else if (opt === selectedOption) {
                         btnClass += "border-red-500 bg-red-500/10 text-red-400";
                    } else {
                         btnClass += "border-slate-700 opacity-50";
                    }

                    return (
                        <button 
                            key={idx}
                            disabled={selectedOption !== null}
                            onClick={() => handleAnswer(opt)}
                            className={btnClass}
                        >
                            {opt}
                        </button>
                    );
                })}
             </div>

             {selectedOption && (
                <div className={`mt-6 p-4 rounded-lg ${isCorrect ? 'bg-green-900/20 border border-green-800' : 'bg-red-900/20 border border-red-800'}`}>
                    <p className="font-medium text-lg mb-1">{isCorrect ? 'Correct!' : 'Wrong!'}</p>
                    <p className="text-slate-300">{currentQ.explanation}</p>
                </div>
             )}
          </div>
        ) : (
            <div className="text-center">Game Over!</div>
        )}
      </div>

       {/* Grounding Footer */}
       <GroundingSources chunks={groundingChunks} />

       {/* Next Button */}
       {selectedOption && (
           <div className="mt-6 flex justify-end">
               <button 
                onClick={isGameOver ? onExit : nextQuestion}
                className="bg-primary hover:bg-indigo-600 text-white font-bold py-3 px-8 rounded-full shadow-lg transition-transform transform hover:scale-105"
               >
                 {isGameOver ? "Finish Game" : "Next Question ->"}
               </button>
           </div>
       )}
    </div>
  );
};

export default ClassicSession;