'use strict';

import FirebaseImpl from 'firebase';
import Fireproof from 'fireproof';

function Firebase($http, AppSettings) {
  'ngInject';

  return new Fireproof(new FirebaseImpl(AppSettings.firebaseUrl));
}

export default {
  name: 'Firebase',
  fn: Firebase
};
