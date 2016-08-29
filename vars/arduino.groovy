def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def _nodes = getNodeNames(config.boards)

  stage "Setup"
  for(n in _nodes) {
    node(n) {
      installBoards(config.platforms)
      installLibraries(config.libraries)
    }
  }

  for(board in config.boards) {

    node(board) {

      currentBuild.result = "SUCCESS"

      try {

         stage board

         checkout scm

         lock(env.NODE_NAME + "-" + board) {
           verifyExamples(board)
         }

         step([$class: "TapPublisher", testResults: "**/target/tap-unit.log"])
         step([$class: 'GitHubCommitStatusSetter'])


      } catch (err) {

          currentBuild.result = "FAILURE"

          throw err

      }

    }

  }

}
