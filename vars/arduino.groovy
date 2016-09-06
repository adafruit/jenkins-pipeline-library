def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  currentBuild.result = "SUCCESS"

  try {

    def platform = new com.adafruit.platforms.Arduino()

    platform.installDependencies(config)

    if(config.verify) {
      platform.verifyExamples(config.boards)
    }

    platform.testBoards(config.boards)
    platform.publishResults(config.boards)

  } catch (err) {

    currentBuild.result = "FAILURE"
    throw err

  }

}
