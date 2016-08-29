def call(boards) {

  def builders = [:]

  stage "Verify"

  for(board in boards) {

    // bind the label variable before the closure
    def platform = board

    builders[platform] = {

      node(platform) {
        checkout scm
        sh """#!/bin/bash
        arduino --board \$${platform} --save-prefs
        declare -a examples
        examples=(\$(find examples -name '*.ino'))
        for example in \"\${examples[@]}\"; do
          echo \"Verifying \${example} on ${platform}\"
          arduino --board \$${platform} --verify \${example} 2>&1
        done
        """
      }

    }

  }

  parallel builders

}
