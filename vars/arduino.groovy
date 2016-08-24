def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  for(board in config.boards) {

    node(board) {

      currentBuild.result = "SUCCESS"

      try {

         stage 'Clone'

           git url: config.repo

         stage 'Test'

           lock(env.NODE_NAME + "-" + board) {
             sh 'prove --formatter TAP::Formatter::Jenkins /home/pi/test.t'
           }

         stage 'Publish'

           step([$class: "TapPublisher", testResults: "**/target/tap-unit.log"])


      } catch (err) {

          currentBuild.result = "FAILURE"

          throw err

      }

    }

  }

}
