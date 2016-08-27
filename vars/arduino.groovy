def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  for(board in config.boards) {

    node(board) {

      currentBuild.result = "SUCCESS"

      sh 'source /etc/profile'

      try {

         stage board

         checkout scm

         lock(env.NODE_NAME + "-" + board) {
           installBoards(config.platforms)
           installLibraries(config.libraries)
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
