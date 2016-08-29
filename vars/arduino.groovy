def call(body) {


  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def _nodes = getNodeNames(config.boards)

  node {
    step([$class: 'GitHubSetCommitStatusBuilder', statusMessage: [content: "Testing on ${_nodes.size()} nodes"]])
  }

  for(n in _nodes) {
    node(n) {
      stage "Setup: ${n}"
      installBoards(config.platforms)
      installLibraries(config.libraries)
    }
  }

  verifyExamples(config.boards)
  publishResults(config.boards)

}
