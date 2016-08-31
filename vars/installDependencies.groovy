def call(config) {

  def completed = []

  node('master') {
    step([$class: 'GitHubSetCommitStatusBuilder'])
  }

  for(board in config.boards) {

    def _label = Jenkins.instance.getLabel(board)
    def _nodes = _label.getNodes()

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

}
