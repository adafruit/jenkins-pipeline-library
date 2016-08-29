def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def _nodes = getNodeNames(config.boards)

  for(n in _nodes) {
    node(n) {
      stage "Setup: ${n}"
      installBoards(config.platforms)
      installLibraries(config.libraries)
    }
  }


  verifyExamples(config.boards)

  step([$class: "TapPublisher", testResults: "**/target/tap-unit.log"])
  step([$class: 'GitHubCommitStatusSetter'])

}
