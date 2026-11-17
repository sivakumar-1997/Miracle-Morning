// Sacred Sound Engine for Miracle Mornings (Web Audio API)
// Provides authentic Solfeggio frequencies and continuous 432 Hz Pythagorean ambient soundscape

class SacredSoundEngine {
  constructor() {
    this.ctx = null;
    this.isAmbientPlaying = false;
    this.ambientNodes = [];
    this.activeChakraSource = null;
    this.speechSynthesis = window.speechSynthesis || null;
    this.femaleVoice = null;
    this.isHooponoponoActive = false;
    this.hooponoponoTimer = null;

    this.initSpeech();
  }

  ensureContext() {
    if (!this.ctx) {
      const AudioContext = window.AudioContext || window.webkitAudioContext;
      this.ctx = new AudioContext();
    }
    if (this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
    return this.ctx;
  }

  initSpeech() {
    if (!this.speechSynthesis) return;
    const loadVoices = () => {
      const voices = this.speechSynthesis.getVoices();
      if (!voices || voices.length === 0) return;
      
      // Prioritize natural female English voice
      this.femaleVoice = voices.find(v => {
        const n = v.name.toLowerCase();
        return (v.lang.startsWith('en') && (n.includes('female') || n.includes('samantha') || n.includes('karen') || n.includes('victoria') || n.includes('zira') || n.includes('aria')));
      }) || voices.find(v => v.lang.startsWith('en-US')) || voices[0];
    };

    loadVoices();
    if (this.speechSynthesis.onvoiceschanged !== undefined) {
      this.speechSynthesis.onvoiceschanged = loadVoices;
    }
  }

  // --- 432 HZ CONTINUOUS AMBIENT SOUNDSCAPE ---
  start432HzAmbient(onStarted) {
    this.stop432HzAmbient();
    const ctx = this.ensureContext();
    this.isAmbientPlaying = true;

    const masterGain = ctx.createGain();
    masterGain.gain.setValueAtTime(0.001, ctx.currentTime);
    masterGain.gain.exponentialRampToValueAtTime(0.35, ctx.currentTime + 2.5);
    masterGain.connect(ctx.destination);
    this.ambientNodes.push(masterGain);

    // 432 Hz Sacred Harmonics multi-octave oscillators
    const harmonics = [
      { freq: 108, gain: 0.28, pan: -0.2 }, // Sub bass Om
      { freq: 216, gain: 0.26, pan: 0.15 }, // Warm Cello octave
      { freq: 324, gain: 0.22, pan: -0.1 }, // Cosmic fifth
      { freq: 432, gain: 0.30, pan: 0.0 },  // Sacred Heart fundamental
      { freq: 540, gain: 0.16, pan: 0.25 }, // Major third
      { freq: 864, gain: 0.08, pan: -0.3 }  // Crystal bowl chime
    ];

    harmonics.forEach(h => {
      const osc = ctx.createOscillator();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(h.freq, ctx.currentTime);

      const gain = ctx.createGain();
      gain.gain.setValueAtTime(h.gain, ctx.currentTime);

      // Stereo panner if available
      if (ctx.createStereoPanner) {
        const panner = ctx.createStereoPanner();
        panner.pan.setValueAtTime(h.pan, ctx.currentTime);
        osc.connect(gain);
        gain.connect(panner);
        panner.connect(masterGain);
        this.ambientNodes.push(panner);
      } else {
        osc.connect(gain);
        gain.connect(masterGain);
      }

      osc.start();
      this.ambientNodes.push(osc, gain);
    });

    // Slow prana breathing LFO (0.08 Hz = ~12s cycle)
    const lfo = ctx.createOscillator();
    lfo.frequency.setValueAtTime(0.08, ctx.currentTime);
    const lfoGain = ctx.createGain();
    lfoGain.gain.setValueAtTime(0.12, ctx.currentTime);
    lfo.connect(lfoGain);
    lfoGain.connect(masterGain.gain);
    lfo.start();
    this.ambientNodes.push(lfo, lfoGain);

    // Subtle pink noise breeze generator
    const bufferSize = ctx.sampleRate * 2;
    const noiseBuffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
    const output = noiseBuffer.getChannelData(0);
    let b0 = 0, b1 = 0, b2 = 0;
    for (let i = 0; i < bufferSize; i++) {
      const white = Math.random() * 2 - 1;
      b0 = 0.99765 * b0 + white * 0.0990460;
      b1 = 0.96300 * b1 + white * 0.2965164;
      b2 = 0.57000 * b2 + white * 1.0526913;
      output[i] = (b0 + b1 + b2 + white * 0.1848) * 0.015;
    }

    const whiteNoise = ctx.createBufferSource();
    whiteNoise.buffer = noiseBuffer;
    whiteNoise.loop = true;

    const noiseFilter = ctx.createBiquadFilter();
    noiseFilter.type = 'lowpass';
    noiseFilter.frequency.setValueAtTime(650, ctx.currentTime);

    const noiseGain = ctx.createGain();
    noiseGain.gain.setValueAtTime(0.06, ctx.currentTime);

    whiteNoise.connect(noiseFilter);
    noiseFilter.connect(noiseGain);
    noiseGain.connect(masterGain);
    whiteNoise.start();

    this.ambientNodes.push(whiteNoise, noiseFilter, noiseGain);

    if (onStarted) onStarted();
  }

  stop432HzAmbient() {
    this.isAmbientPlaying = false;
    if (this.ambientNodes.length > 0) {
      this.ambientNodes.forEach(node => {
        try {
          if (node.stop) node.stop();
          if (node.disconnect) node.disconnect();
        } catch (e) {}
      });
      this.ambientNodes = [];
    }
  }

  // --- 7 CHAKRAS PURE SOLFEGGIO FREQUENCY SOUND ---
  playChakraSound(freqHz, duration = 6.0, onComplete) {
    this.stopChakraSound();
    const ctx = this.ensureContext();

    const masterGain = ctx.createGain();
    masterGain.gain.setValueAtTime(0.001, ctx.currentTime);
    masterGain.gain.linearRampToValueAtTime(0.55, ctx.currentTime + 0.15); // gentle attack
    masterGain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + duration); // singing bowl decay
    masterGain.connect(ctx.destination);

    // Fundamental Pure Solfeggio Tone
    const oscFund = ctx.createOscillator();
    oscFund.type = 'sine';
    oscFund.frequency.setValueAtTime(freqHz, ctx.currentTime);

    // Subharmonic octave for resonance
    const oscSub = ctx.createOscillator();
    oscSub.type = 'sine';
    oscSub.frequency.setValueAtTime(freqHz * 0.5, ctx.currentTime);
    const gainSub = ctx.createGain();
    gainSub.gain.setValueAtTime(0.24, ctx.currentTime);
    oscSub.connect(gainSub);
    gainSub.connect(masterGain);

    // 5th overtone singing bowl shimmer
    const oscFifth = ctx.createOscillator();
    oscFifth.type = 'sine';
    oscFifth.frequency.setValueAtTime(freqHz * 1.5, ctx.currentTime);
    const gainFifth = ctx.createGain();
    gainFifth.gain.setValueAtTime(0.12, ctx.currentTime);
    oscFifth.connect(gainFifth);
    gainFifth.connect(masterGain);

    // High bowl bell harmonic
    const oscHigh = ctx.createOscillator();
    oscHigh.type = 'sine';
    oscHigh.frequency.setValueAtTime(freqHz * 2.756, ctx.currentTime);
    const gainHigh = ctx.createGain();
    gainHigh.gain.setValueAtTime(0.05, ctx.currentTime);
    oscHigh.connect(gainHigh);
    gainHigh.connect(masterGain);

    oscFund.connect(masterGain);

    oscFund.start();
    oscSub.start();
    oscFifth.start();
    oscHigh.start();

    const nodes = [oscFund, oscSub, oscFifth, oscHigh, gainSub, gainFifth, gainHigh, masterGain];
    this.activeChakraSource = {
      nodes,
      timer: setTimeout(() => {
        this.stopChakraSound();
        if (onComplete) onComplete();
      }, duration * 1000)
    };
  }

  stopChakraSound() {
    if (this.activeChakraSource) {
      clearTimeout(this.activeChakraSource.timer);
      this.activeChakraSource.nodes.forEach(node => {
        try {
          if (node.stop) node.stop();
          if (node.disconnect) node.disconnect();
        } catch (e) {}
      });
      this.activeChakraSource = null;
    }
  }

  // --- HO'OPONOPONO SPEECH LOOP ---
  startHooponoponoVoice(onPhraseSpoken) {
    this.stopHooponoponoVoice();
    this.isHooponoponoActive = true;

    const phrases = [
      "I am sorry.",
      "Please forgive me.",
      "Thank you.",
      "I love you."
    ];

    let index = 0;
    const speakPhrase = () => {
      if (!this.isHooponoponoActive || !this.speechSynthesis) return;

      const phrase = phrases[index];
      if (onPhraseSpoken) onPhraseSpoken(index, phrase);

      const utterance = new SpeechSynthesisUtterance(phrase);
      if (this.femaleVoice) utterance.voice = this.femaleVoice;
      utterance.pitch = 1.08;
      utterance.rate = 0.82;
      utterance.volume = 1.0;

      this.speechSynthesis.cancel();
      this.speechSynthesis.speak(utterance);

      index = (index + 1) % phrases.size || (index + 1) % phrases.length;
      if (this.isHooponoponoActive) {
        this.hooponoponoTimer = setTimeout(speakPhrase, 4600);
      }
    };

    speakPhrase();
  }

  stopHooponoponoVoice() {
    this.isHooponoponoActive = false;
    if (this.hooponoponoTimer) {
      clearTimeout(this.hooponoponoTimer);
      this.hooponoponoTimer = null;
    }
    if (this.speechSynthesis) {
      this.speechSynthesis.cancel();
    }
  }

  stopAll() {
    this.stop432HzAmbient();
    this.stopChakraSound();
    this.stopHooponoponoVoice();
  }
}

window.soundEngine = new SacredSoundEngine();
