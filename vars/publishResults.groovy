def call(boards) {

  stage "Publish Results"

  for(board in boards) {

    node(board) {
      step([$class: "TapPublisher", testResults: "**/target/tap-unit.log"])
    }

  }

  node('master') {
    step([$class: 'GitHubCommitStatusSetter'])
  }

}
