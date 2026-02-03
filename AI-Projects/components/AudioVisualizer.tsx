import React, { useEffect, useRef } from 'react';

interface AudioVisualizerProps {
  analyser: AnalyserNode | null;
  color?: string; // e.g. 'rgb(99, 102, 241)'
}

const AudioVisualizer: React.FC<AudioVisualizerProps> = ({ analyser, color = 'rgb(99, 102, 241)' }) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const animationRef = useRef<number>();

  useEffect(() => {
    if (!analyser || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // High resolution for crisp rendering
    const dpr = window.devicePixelRatio || 1;
    const rect = canvas.getBoundingClientRect();
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    ctx.scale(dpr, dpr);

    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    const draw = () => {
      animationRef.current = requestAnimationFrame(draw);
      analyser.getByteFrequencyData(dataArray);

      ctx.fillStyle = 'rgba(15, 23, 42, 0.2)'; // Fade out effect
      ctx.fillRect(0, 0, rect.width, rect.height);

      const barWidth = (rect.width / bufferLength) * 2.5;
      let barHeight;
      let x = 0;

      for (let i = 0; i < bufferLength; i++) {
        barHeight = dataArray[i] / 2; // Scale down height

        // Gradient fill
        ctx.fillStyle = color;
        
        // Center the bars vertically
        const y = (rect.height - barHeight) / 2;
        
        ctx.fillRect(x, y, barWidth, barHeight);

        x += barWidth + 1;
      }
    };

    draw();

    return () => {
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    };
  }, [analyser, color]);

  return (
    <canvas 
      ref={canvasRef} 
      className="w-full h-32 rounded-xl bg-slate-900 border border-slate-700 shadow-inner"
    />
  );
};

export default AudioVisualizer;