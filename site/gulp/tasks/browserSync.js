'use strict';

import config      from '../config';
import url         from 'url';
import browserSync from 'browser-sync';
import gulp        from 'gulp';

import server      from '../../server';
import express     from 'express';

var api = express();
api.use("/api", server);

gulp.task('browserSync', function() {

  const DEFAULT_FILE = 'index.html';
  const ASSET_EXTENSION_REGEX = new RegExp(`\\b(?!\\?)\\.(${config.assetExtensions.join('|')})\\b(?!\\.)`, 'i');

  browserSync.init({
    server: {
      baseDir: config.buildDir,
      middleware: function(req, res, next) {
        let fileHref = url.parse(req.url).href;

        if (/^\/api\/?.*$/.test(fileHref))
          return api.handle(req, res, next);

        if ( !ASSET_EXTENSION_REGEX.test(fileHref) ) {
          req.url = '/' + DEFAULT_FILE;
        }

        return next();
      }
    },
  	port: config.browserPort,
  	ui: {
    	port: config.UIPort
    },
    ghostMode: {
      links: false
    }
  });

});
