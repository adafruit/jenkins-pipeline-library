def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  currentBuild.result = "SUCCESS"

  try {

    node('master') {
      step([$class: 'GitHubSetCommitStatusBuilder', statusMessage: [content: "Testing on ${_nodes.size()} nodes"]])
    }

    installDependencies(config)
    verifyExamples(config.boards)
    testBoards(config.boards)
    publishResults(config.boards)

  } catch (err) {

    currentBuild.result = "FAILURE"

    throw err

  }

  step([$class: 'WsCleanup'])

}
