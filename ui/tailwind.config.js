module.exports = {
  purge: [
    './src/**/*.cljs',
    './assets/**/*.css',
    './assets/**/*.html'
  ],  
  theme: {
    extend: {}
  },
  variants: {
    outline: ['responsive', 'focus', 'hover', 'active']
  },
  plugins: []
}
