module.exports = {
  purge: [
    './src/**/*.cljs',
    './assets/**/*.css',
    './assets/**/*.html',
    '../srv/src/**/*.clj'
  ],  
  theme: {
    extend: {}
  },
  variants: {
    outline: ['responsive', 'focus', 'hover', 'active']
  },
  plugins: []
}
