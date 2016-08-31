def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  currentBuild.result = "SUCCESS"

  try {


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
