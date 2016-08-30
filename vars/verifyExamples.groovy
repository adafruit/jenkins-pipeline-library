def call(boards) {

  for(board in boards) {

    stage "Verify: ${board}"

    // bind the label variable before the closure
    def platform = board
    def examples = []

    node(platform) {

      sh "arduino --board \$${platform} --save-prefs"

      examples = sh(returnStdout: true, script: "find examples -name '*.ino' | sort").trim().split("\n")

      for(int i = 0; i < examples.size(); i++) {

        def name = sh(returnStdout: true, script: "basename ${examples[i]} .ino").trim()
        def example = examples[i]

        echo "Verifying ${name}.ino on ${platform}"
        sh "arduino --board \$${platform} --verify ${example}"

      }

    }

  }

}
