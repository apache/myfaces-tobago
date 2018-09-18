module.exports = (ctx) => ({
  map: {
    inline: false,
    annotation: true,
    sourcesContent: true
  },
  plugins: {
    autoprefixer: {
      cascade: false,
      browsers: [
        "last 1 version",
        "ie >= 10",
        "Firefox >= 45"
      ],
    }
  }
})
