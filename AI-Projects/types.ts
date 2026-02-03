import { Modality } from "@google/genai";

export enum GameMode {
  MENU = 'MENU',
  CLASSIC = 'CLASSIC',
  LIVE = 'LIVE',
}

export interface Persona {
  id: string;
  name: string;
  description: string;
  systemInstruction: string;
  voiceName: string; // For TTS/Live
  avatarColor: string;
}

export interface TriviaQuestion {
  question: string;
  options: string[];
  correctAnswer: string;
  explanation: string;
}

export interface GroundingSource {
  title: string;
  uri: string;
}

// Live API specific types
export interface LiveConfig {
  model: string;
  systemInstruction: string;
  voiceName: string;
}
