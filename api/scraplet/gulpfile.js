var gulp = require('gulp');
var zip = require('gulp-zip');
var del = require('del');
var install = require('gulp-install');
var runSequence = require('run-sequence');
var awsLambda = require("node-aws-lambda");
var jshint = require('gulp-jshint');
var babel = require('gulp-babel');
var async = require("async");

gulp.task('clean', function() {
  return del(['./dist', './dist.zip']);
});

gulp.task('js', function() {
  return gulp.src(['create.js', 'update.js', 'invoke.js', 'on_change.js'])
    .pipe(babel({ presets: [ "es2015" ] }))
    .pipe(gulp.dest('dist/'));
});

gulp.task('js:hub', function() {
  return gulp.src('hub/*.js')
    .pipe(babel({ presets: [ "es2015" ] }))
    .pipe(gulp.dest('dist/hub'));
});

gulp.task("check", function() {
  return gulp.src(['dist/create.js', 'dist/update.js',
      'dist/invoke.js', 'dist/on_change.js', 'dist/hub/*'])
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('node-mods', function() {
  return gulp.src('./package.json')
    .pipe(gulp.dest('dist/'))
    .pipe(install({production: true}));
});

gulp.task('zip', function() {
  return gulp.src(['dist/**/*', '!dist/package.json'])
    .pipe(zip('dist.zip'))
    .pipe(gulp.dest('./'));
});

gulp.task('upload', function(callback) {
  var lambdaConfig = require("./lambda-config.js");
  async.parallel(lambdaConfig.map(function(config) {
    return function(cb) {
      awsLambda.deploy('./dist.zip', config, cb);
    }
  }), callback);
});

gulp.task('deploy', function(callback) {
  return runSequence(
    ['clean'],
    ['js', 'js:hub', 'check', 'node-mods'],
    ['zip'],
    ['upload'],
    callback
  );
});
