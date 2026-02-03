import { GoogleGenAI, Type, Modality } from "@google/genai";
import { TriviaQuestion, GroundingSource } from "../types";

// Initialize the client
const ai = new GoogleGenAI({ apiKey: process.env.API_KEY });

// --- Classic Mode: Question Generation ---

interface GeneratedQuestionsResponse {
  questions: TriviaQuestion[];
  groundingChunks?: any[];
}

export const generateQuestions = async (
  topic: string,
  count: number = 3
): Promise<GeneratedQuestionsResponse> => {
  try {
    const model = 'gemini-3-flash-preview';
    const prompt = `Generate ${count} fun and challenging trivia questions about "${topic}".
    Return a JSON object with a "questions" array.
    Each question object should have:
    - question (string)
    - options (array of 4 strings)
    - correctAnswer (string, must be one of the options)
    - explanation (string, short fact about the answer)`;

    const response = await ai.models.generateContent({
      model,
      contents: prompt,
      config: {
        tools: [{ googleSearch: {} }], // Search Grounding for accuracy
        responseMimeType: 'application/json',
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            questions: {
              type: Type.ARRAY,
              items: {
                type: Type.OBJECT,
                properties: {
                  question: { type: Type.STRING },
                  options: { type: Type.ARRAY, items: { type: Type.STRING } },
                  correctAnswer: { type: Type.STRING },
                  explanation: { type: Type.STRING },
                },
                required: ["question", "options", "correctAnswer", "explanation"],
              },
            },
          },
        },
      },
    });

    const text = response.text;
    if (!text) throw new Error("No text returned from Gemini");

    const parsed = JSON.parse(text);
    const groundingChunks = response.candidates?.[0]?.groundingMetadata?.groundingChunks || [];

    return {
      questions: parsed.questions,
      groundingChunks
    };
  } catch (error) {
    console.error("Error generating questions:", error);
    throw error;
  }
};

// --- Classic Mode: TTS ---

export const generateSpeech = async (text: string, voiceName: string): Promise<string | null> => {
  try {
    const response = await ai.models.generateContent({
      model: "gemini-2.5-flash-preview-tts",
      contents: [{ parts: [{ text }] }],
      config: {
        responseModalities: [Modality.AUDIO],
        speechConfig: {
          voiceConfig: {
            prebuiltVoiceConfig: { voiceName },
          },
        },
      },
    });

    const base64Audio = response.candidates?.[0]?.content?.parts?.[0]?.inlineData?.data;
    return base64Audio || null;
  } catch (error) {
    console.error("Error generating speech:", error);
    return null;
  }
};

// --- Live Mode Accessor ---
// We export the `ai` instance so the LiveSession component can use `ai.live.connect`
export const getGenAIInstance = () => ai;
