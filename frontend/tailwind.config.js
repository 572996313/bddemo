/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        // Industrial Logic 色板（来自 DESIGN.md）
        primary: '#001e40',
        'on-primary': '#ffffff',
        'primary-container': '#003366',
        'on-primary-container': '#799dd6',
        secondary: '#165db2',
        'on-secondary': '#ffffff',
        'secondary-container': '#6ca3fd',
        'on-secondary-container': '#003874',
        surface: '#f8f9ff',
        'surface-bright': '#f8f9ff',
        'surface-dim': '#cbdbf5',
        'surface-container-lowest': '#ffffff',
        'surface-container-low': '#eff4ff',
        'surface-container': '#e5eeff',
        'surface-container-high': '#dce9ff',
        'surface-container-highest': '#d3e4fe',
        'on-surface': '#0b1c30',
        'on-surface-variant': '#43474f',
        outline: '#737780',
        'outline-variant': '#c3c6d1',
        error: '#ba1a1a',
        'on-error': '#ffffff',
        'error-container': '#ffdad6',
        background: '#f8f9ff',
        'on-background': '#0b1c30'
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace']
      },
      spacing: {
        'toolbar-height': '56px',
        'panel-width': '320px'
      },
      borderRadius: {
        DEFAULT: '0.125rem'
      }
    }
  },
  plugins: []
}
