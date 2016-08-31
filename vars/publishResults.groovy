def call(boards) {

  stage "Publish Results"

  for(board in boards) {

    node(board) {
      step([$class: 'TapPublisher', discardOldReports: true, enableSubtests: false, failIfNoResults: false, failedTestsMarkBuildAsFailure: false, flattenTapResult: false, includeCommentDiagnostics: false, outputTapToConsole: true, planRequired: true, showOnlyFailures: false, skipIfBuildNotOk: false, stripSingleParents: false, testResults: '', todoIsFailure: false, validateNumberOfTests: false, verbose: true])
    }

  }

  node('master') {
    step([$class: 'GitHubCommitStatusSetter'])
  }

}
