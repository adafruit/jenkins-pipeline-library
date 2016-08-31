def call(config) {

  def completed = []

  node('master') {
    step([$class: 'GitHubSetCommitStatusBuilder'])
  }

  for(board in config.boards) {

    def _label = Jenkins.instance.getLabel(board)
    def _nodes = _label.getNodes()

    for(_node in _nodes) {

      def name = _node.getNodeName()

      if(! (name in completed)) {

        node(n) {
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
