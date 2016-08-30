def call(body) {


  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def _nodes = getNodeNames(config.boards)

  currentBuild.result = "SUCCESS"

  try {

    node('master') {
      step([$class: 'GitHubSetCommitStatusBuilder', statusMessage: [content: "Testing on ${_nodes.size()} nodes"]])
    }

    for(n in _nodes) {
      node(n) {
        stage "Setup: ${n}"
        checkout scm
        env.PATH = '$HOME/arduino_ide:$PATH'
        generateTest()
        installBoards(config.platforms)
        installLibraries(config.libraries)
      }
    }

    //verifyExamples(config.boards)
    testBoards(config.boards)
    publishResults(config.boards)

  } catch (err) {

    currentBuild.result = "FAILURE"

    throw err

  }

}
