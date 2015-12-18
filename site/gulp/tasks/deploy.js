'use strict';

import gulp from 'gulp';
import { series } from 'async';
import git from 'gulp-git';

gulp.task('deploy:git-checkout-deploy', function(cb) {
  git.checkout("deploy", cb);
});

gulp.task('deploy:git-merge-master', function(cb) {
  git.merge("master", cb);
});

gulp.task('deploy:prepare', [
  'deploy:git-checkout-deploy',
  'deploy:git-merge-master',
  'build'
]);

gulp.task('deploy:git-add', function(cb) {
  return gulp.src("./public/**/*")
    .pipe(git.add());
});

gulp.task('deploy:git-commit', function(cb) {
  git.commit("BUILD-" + (new Date()).toISOString() , cb);
});

gulp.task('deploy:push', [ 'deploy:git-add', 'deploy:git-commit' ],
  function(cb) {
    git.push("heroku", cb);
  });

gulp.task('deploy', ['deploy:prepare', 'deploy:push'], function(cb) {
  git.checkout("master", cb);
});
