import React from 'react';
import { GroundingSource } from '../types';

interface Props {
  chunks: any[];
}

const GroundingSources: React.FC<Props> = ({ chunks }) => {
  if (!chunks || chunks.length === 0) return null;

  // Extract URLs from the chunks
  const sources: GroundingSource[] = [];
  chunks.forEach(chunk => {
    if (chunk.web?.uri && chunk.web?.title) {
        sources.push({ title: chunk.web.title, uri: chunk.web.uri });
    }
  });

  if (sources.length === 0) return null;

  return (
    <div className="mt-4 p-3 bg-slate-800 rounded-lg border border-slate-700 text-xs">
      <h4 className="text-slate-400 font-semibold mb-2 flex items-center gap-1">
        <svg xmlns="http://www.w3.org/2000/svg" className="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        Sources (Google Search)
      </h4>
      <div className="flex flex-wrap gap-2">
        {sources.map((source, idx) => (
          <a 
            key={idx} 
            href={source.uri} 
            target="_blank" 
            rel="noopener noreferrer"
            className="text-primary hover:text-indigo-300 transition-colors truncate max-w-[200px]"
          >
            {source.title}
          </a>
        ))}
      </div>
    </div>
  );
};

export default GroundingSources;