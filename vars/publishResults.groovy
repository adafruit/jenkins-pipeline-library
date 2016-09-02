def call(boards) {

  stage "Publish Results"

  def _nodes = getNodeNames(boards)

  for(int i = 0; i < _nodes.size(); i++) {

    def name = _nodes[i]

    node(name) {
      step([$class: 'TapPublisher', discardOldReports: false, enableSubtests: false, failIfNoResults: false, failedTestsMarkBuildAsFailure: true, flattenTapResult: false, includeCommentDiagnostics: false, outputTapToConsole: true, planRequired: true, showOnlyFailures: false, skipIfBuildNotOk: false, stripSingleParents: false, testResults: '**/*.tap', todoIsFailure: false, validateNumberOfTests: true, verbose: true])
      step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true, patterns: [[pattern: '*.tap', type: 'INCLUDE'], [pattern: 'tests', type: 'INCLUDE']]])
    }

  }

  node('master') {
    step([$class: 'GitHubCommitStatusSetter'])
  }

}
