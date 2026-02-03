import React, { useEffect, useRef, useState } from 'react';
import { Persona } from '../types';
import { getGenAIInstance } from '../services/geminiService';
import { createPcmBlob, decodeAudioData, base64ToUint8Array } from '../services/audioUtils';
import { LiveServerMessage, Modality } from '@google/genai';
import AudioVisualizer from './AudioVisualizer';

interface Props {
  persona: Persona;
  onExit: () => void;
}

const LiveSession: React.FC<Props> = ({ persona, onExit }) => {
  const [isConnected, setIsConnected] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [status, setStatus] = useState("Initializing audio...");

  // Refs for audio handling to avoid re-renders
  const audioContextRef = useRef<AudioContext | null>(null);
  const inputContextRef = useRef<AudioContext | null>(null);
  const nextStartTimeRef = useRef<number>(0);
  const sourcesRef = useRef<Set<AudioBufferSourceNode>>(new Set());
  const sessionRef = useRef<any>(null);
  const streamRef = useRef<MediaStream | null>(null);
  
  // Visualizer Analysers
  const [inputAnalyser, setInputAnalyser] = useState<AnalyserNode | null>(null);
  const [outputAnalyser, setOutputAnalyser] = useState<AnalyserNode | null>(null);

  useEffect(() => {
    let mounted = true;

    const startSession = async () => {
      try {
        // 1. Setup Audio Contexts
        const inputCtx = new (window.AudioContext || (window as any).webkitAudioContext)({ sampleRate: 16000 });
        const outputCtx = new (window.AudioContext || (window as any).webkitAudioContext)({ sampleRate: 24000 });
        
        audioContextRef.current = outputCtx;
        inputContextRef.current = inputCtx;

        // Analysers for visualization
        const inAnal = inputCtx.createAnalyser();
        inAnal.fftSize = 256;
        setInputAnalyser(inAnal);

        const outAnal = outputCtx.createAnalyser();
        outAnal.fftSize = 256;
        setOutputAnalyser(outAnal);
        const outputGain = outputCtx.createGain();
        // Connect output chain: Source -> OutputAnalyser -> Gain -> Destination
        outputGain.connect(outputCtx.destination);


        // 2. Get Microphone Stream
        setStatus("Requesting microphone...");
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        streamRef.current = stream;

        // 3. Connect to Live API
        setStatus("Connecting to AI Host...");
        const ai = getGenAIInstance();
        
        // Define System Instruction specially for Live Trivia
        const liveInstruction = `${persona.systemInstruction} 
        You are hosting a trivia game. 
        Rules:
        1. Ask ONE question at a time.
        2. Wait for the user to answer vocally.
        3. Confirm if they are correct or wrong and explain briefly.
        4. Keep track of the score mentally and mention it occasionally.
        5. Start immediately by introducing yourself and asking the first question.`;

        const sessionPromise = ai.live.connect({
          model: 'gemini-2.5-flash-native-audio-preview-12-2025',
          config: {
            responseModalities: [Modality.AUDIO],
            speechConfig: {
              voiceConfig: { prebuiltVoiceConfig: { voiceName: persona.voiceName } },
            },
            systemInstruction: liveInstruction,
          },
          callbacks: {
            onopen: () => {
              if (!mounted) return;
              console.log("Live Session Open");
              setIsConnected(true);
              setStatus("Connected! Say 'Hello' to start.");

              // Setup Input Processing
              const source = inputCtx.createMediaStreamSource(stream);
              source.connect(inAnal);
              
              // Use ScriptProcessor for raw PCM capture (as per guidelines)
              const processor = inputCtx.createScriptProcessor(4096, 1, 1);
              processor.onaudioprocess = (e) => {
                 const inputData = e.inputBuffer.getChannelData(0);
                 const pcmBlob = createPcmBlob(inputData);
                 
                 // Send to Gemini
                 sessionPromise.then(session => {
                    session.sendRealtimeInput({ media: pcmBlob });
                 });
              };
              
              source.connect(processor);
              processor.connect(inputCtx.destination);
              sessionRef.current = sessionPromise; // store for cleanup? No method to close on promise
            },
            onmessage: async (msg: LiveServerMessage) => {
              if (!mounted) return;
              
              const audioData = msg.serverContent?.modelTurn?.parts?.[0]?.inlineData?.data;
              if (audioData) {
                const ctx = audioContextRef.current;
                if (!ctx) return;

                // Sync playback time
                nextStartTimeRef.current = Math.max(nextStartTimeRef.current, ctx.currentTime);
                
                const buffer = await decodeAudioData(base64ToUint8Array(audioData), ctx);
                const source = ctx.createBufferSource();
                source.buffer = buffer;
                source.connect(outAnal); // Vis
                outAnal.connect(outputGain); // Out
                
                source.start(nextStartTimeRef.current);
                nextStartTimeRef.current += buffer.duration;
                
                sourcesRef.current.add(source);
                source.onended = () => sourcesRef.current.delete(source);
              }

              // Handle Interruptions
              if (msg.serverContent?.interrupted) {
                console.log("Interrupted!");
                sourcesRef.current.forEach(s => s.stop());
                sourcesRef.current.clear();
                nextStartTimeRef.current = 0;
              }
            },
            onclose: () => {
              console.log("Session Closed");
              if (mounted) setIsConnected(false);
            },
            onerror: (err) => {
              console.error("Session Error", err);
              if (mounted) setError("Connection Error. Please restart.");
            }
          }
        });

      } catch (e: any) {
        console.error(e);
        if (mounted) setError(e.message || "Failed to start live session");
      }
    };

    startSession();

    return () => {
      mounted = false;
      // Cleanup
      streamRef.current?.getTracks().forEach(t => t.stop());
      audioContextRef.current?.close();
      inputContextRef.current?.close();
      // Close the session if available
      sessionRef.current?.then((session: any) => session.close && session.close());
    };
  }, [persona]);

  return (
    <div className="flex flex-col items-center justify-center h-full p-6 text-center">
      {/* Header */}
      <div className="absolute top-6 left-6">
        <button onClick={onExit} className="text-slate-400 hover:text-white flex items-center gap-2">
            <span className="text-2xl">&larr;</span> End Call
        </button>
      </div>

      <div className="max-w-md w-full relative">
        {/* Avatar Pulse */}
        <div className="relative mx-auto w-48 h-48 mb-12">
           {isConnected && (
             <div className={`absolute inset-0 rounded-full ${persona.avatarColor} opacity-20 animate-ping`}></div>
           )}
           <div className={`relative z-10 w-full h-full rounded-full ${persona.avatarColor} flex items-center justify-center border-4 border-slate-700 shadow-2xl`}>
              <span className="text-6xl font-bold text-white">{persona.name[0]}</span>
           </div>
           
           {/* Connection Badge */}
           <div className={`absolute bottom-2 right-4 px-3 py-1 rounded-full text-xs font-bold border ${isConnected ? 'bg-green-500/20 border-green-500 text-green-400' : 'bg-yellow-500/20 border-yellow-500 text-yellow-400'}`}>
              {isConnected ? "LIVE" : "CONNECTING"}
           </div>
        </div>

        <h2 className="text-3xl font-bold mb-2">{persona.name}</h2>
        <p className="text-slate-400 mb-8 h-6">{error ? <span className="text-red-400">{error}</span> : status}</p>

        {/* Visualizers */}
        <div className="space-y-4">
            <div className="relative">
                <span className="absolute top-2 left-2 text-xs text-slate-500 font-mono z-10">AI VOICE</span>
                <AudioVisualizer analyser={outputAnalyser} color="rgb(236, 72, 153)" /> 
            </div>
            
            <div className="relative">
                <span className="absolute top-2 left-2 text-xs text-slate-500 font-mono z-10">YOUR MIC</span>
                 <AudioVisualizer analyser={inputAnalyser} color="rgb(99, 102, 241)" />
            </div>
        </div>

        <div className="mt-8 text-sm text-slate-500">
           Speak clearly. The host is listening.
        </div>
      </div>
    </div>
  );
};

export default LiveSession;