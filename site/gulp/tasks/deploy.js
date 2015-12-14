'use strict';

import gulp from 'gulp';
import exec from 'gulp-exec';

gulp.task('deploy', ['prod'], function() {
  return exec("firebase deploy");
});
