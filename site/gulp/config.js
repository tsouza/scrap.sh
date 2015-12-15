'use strict';

export default {

  browserPort: 3000,
  UIPort: 3001,

  sourceDir: './src/',
  buildDir: './public/',

  styles: {
    src: 'src/styles/**/*.scss',
    dest: 'public/css',
    prodSourcemap: false,
    sassIncludePaths: []
  },

  scripts: {
    src: 'src/js/**/*.js',
    dest: 'public/js'
  },

  images: {
    src: 'src/images/**/*',
    dest: 'public/images'
  },

  fonts: {
    src: ['src/fonts/**/*'],
    dest: 'public/fonts'
  },

  assetExtensions: [
    'js',
    'css',
    'png',
    'jpe?g',
    'gif',
    'svg',
    'eot',
    'otf',
    'ttc',
    'ttf',
    'woff2?'
  ],

  views: {
    index: 'src/index.html',
    src: 'src/views/**/*.html',
    dest: 'src/js'
  },

  gzip: {
    src: 'public/**/*.{html,xml,json,css,js,js.map,css.map}',
    dest: 'public/',
    options: {}
  },

  browserify: {
    bundleName: 'main.js',
    prodSourcemap: false
  },

  test: {
    karma: 'test/karma.conf.js',
    protractor: 'test/protractor.conf.js'
  },

  init: function() {
    this.views.watch = [
      this.views.index,
      this.views.src
    ];

    return this;
  }

}.init();
