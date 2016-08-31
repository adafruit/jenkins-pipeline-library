def call(config) {

  def completed = []

  node('master') {
    step([$class: 'GitHubSetCommitStatusBuilder'])
  }

  def _nodes = getNodeNames(config.boards)

  for(int i = 0; i < _nodes.size(); i++) {

    def name = _nodes[i].getNodeName()

    if(! (name in completed)) {

      node(name) {
        stage "Setup: ${name}"
        checkout scm
        env.PATH = '$HOME/arduino_ide:$PATH'
        installBoards(config.platforms)
        installLibraries(config.libraries)
      }

      completed << name

    }

  }

}
