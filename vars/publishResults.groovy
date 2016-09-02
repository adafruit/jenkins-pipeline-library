def call(boards) {

  stage "Publish Results"

  for(board in boards) {

    node(board) {
      step([$class: 'TapPublisher', discardOldReports: false, enableSubtests: false, failIfNoResults: false, failedTestsMarkBuildAsFailure: true, flattenTapResult: false, includeCommentDiagnostics: false, outputTapToConsole: true, planRequired: true, showOnlyFailures: false, skipIfBuildNotOk: false, stripSingleParents: false, testResults: '**/*.tap', todoIsFailure: false, validateNumberOfTests: true, verbose: true])
      step([$class: 'WsCleanup', deleteDirs: true, patterns: [[pattern: 'tests', type: 'INCLUDE']]])
    }

  }

  node('master') {
    step([$class: 'GitHubCommitStatusSetter'])
  }

}
