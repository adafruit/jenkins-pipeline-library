def call(labels) {

  def result = []

  for(label in labels) {

     def _label = Jenkins.instance.getLabel(label)
     def _nodes = _label.getNodes()

     for(_node in _nodes) {

       if(! (_node in result)) {
         result << _node.getNodeName()
       }

     }

  }

  return result

}
