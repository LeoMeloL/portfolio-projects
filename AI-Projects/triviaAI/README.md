# TriviaAI: Dynamic Host

TriviaAI is a next-generation trivia application powered by Google's Gemini API. It demonstrates the capabilities of modern GenAI by combining dynamic content generation, text-to-speech synthesis, search grounding, and real-time multimodal streaming into a cohesive game experience.

## Key Features

### 🎭 Dynamic AI Personas
The core of the experience is the ability to choose your host. The app configures the underlying model with specific system instructions and voice settings to create distinct personalities:

*   **Quiz Master Steve:** An enthusiastic, supportive, and high-energy TV host (Voice: *Kore*).
*   **Salty Sam:** A sarcastic host who offers dry wit and backhanded compliments (Voice: *Puck*).
*   **Captain Quizbeard:** A boisterous pirate who treats points like gold doubloons (Voice: *Fenrir*).
*   **Unit 734:** A cold, logical AI construct that analyzes your "human inefficiency" (Voice: *Zephyr*).

### 🎮 Game Modes

#### 1. Classic Mode
A refined multiple-choice trivia experience.
*   **On-the-fly Generation:** Questions are created instantly based on your selected topic using `gemini-3-flash-preview`.
*   **Search Grounding:** The app uses the **Google Search Tool** to verify facts and displays source links for every question generated.
*   **Text-to-Speech (TTS):** The host reads questions and reacts to your answers aloud using `gemini-2.5-flash-preview-tts`.

#### 2. Voice Mode (Live API)
A hands-free, conversational game powered by the Gemini Live API.
*   **Real-time Conversation:** No buttons to press—just talk to the host naturally.
*   **Bidirectional Streaming:** Uses `gemini-2.5-flash-native-audio-preview-12-2025` to stream raw audio in and out via WebSockets.
*   **Visualizers:** Features real-time frequency visualization for both the user's microphone input and the AI's audio output.

## Technical Implementation

This project uses the **@google/genai** SDK and showcases several advanced patterns:

1.  **Structured Output:** In Classic Mode, the model returns strictly typed JSON data for questions using `responseSchema`.
2.  **Audio Streaming:** Implements custom `AudioContext` logic to handle raw PCM audio data (16kHz input, 24kHz output) required by the Live API.
3.  **Tool Use:** Demonstrates how to configure and parse `groundingMetadata` from Google Search.
4.  **System Instructions:** Shows how prompting changes the behavior and "vibe" of the model significantly between sessions.

## How to Run

1.  Ensure you have a valid **Google GenAI API Key**.
2.  The key is accessed via `process.env.API_KEY`.
3.  Select a Host, choose a Mode, and start playing!

**Prerequisites:**  Node.js

1. Install dependencies:
   `npm install`
2. Set the `GEMINI_API_KEY` in [.env.local](.env.local) to your Gemini API key
3. Run the app:
   `npm run dev`
