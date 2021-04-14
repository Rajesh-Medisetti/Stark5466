pipeline {
 agent none
 environment {
  COVERAGE_LIMITS_SPREADSHEET_SECRET = credentials('COVERAGE_LIMITS_SPREADSHEET_SECRET')
 }
 stages {
  stage('set environment') {
   agent { node { label 'master' } }
   steps {
    script {
     env.GIT_REVISION = sh(script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
     env.GIT_BRANCH = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true)
    }
   }
  }
  stage('run lint') {
   agent { node { label 'master' } }
   steps {
    sh './lint.sh ci'
   }
  }
  stage('run tests') {
   agent { node { label 'master' } }
   steps {
    sh './test_runner.sh ci'
   }
  }
  stage('build, tag and push to staging') {
   agent { node { label 'master' } }
   when {
    anyOf {
     branch 'staging';
    }
   }
   steps {
    sh './build_tag_push.sh staging all'
   }
  }
  stage('proceed with production build') {
   agent none
   when {
    anyOf {
     branch 'release';
    }
   }
   steps {
    timeout(time: 1, unit: 'HOURS') {
     input message: 'Approve?'
    }
   }
  }
  stage('build, tag and push to production') {
   agent { node { label 'master' } }
   when {
    anyOf {
     branch 'release';
    }
   }
   steps {
    sh './build_tag_push.sh production all'
   }
  }
 }
}
