module.exports = {
  purge: [
    './src/**/*.cljs',
    './assets/**/*.css',
    './assets/**/*.html',
    '../srv/src/**/*.clj'
  ],  
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {}
  },
  variants: {
    outline: ['responsive', 'focus', 'hover', 'active']
  },
  plugins: [
    require('@tailwindcss/forms')
    // require('@tailwindcss/aspect-ratio'),
    // require('@tailwindcss/typography'),
    // require('tailwindcss-children'),
  ]
}
