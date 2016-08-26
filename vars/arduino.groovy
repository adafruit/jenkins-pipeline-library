def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  for(board in config.boards) {

    node(board) {

      currentBuild.result = "SUCCESS"

      try {

         stage board

         checkout scm

         lock(env.NODE_NAME + "-" + board) {
           installBoards(config.platforms)
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
